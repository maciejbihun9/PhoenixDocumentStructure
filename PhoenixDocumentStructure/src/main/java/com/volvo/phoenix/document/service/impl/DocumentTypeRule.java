package com.volvo.phoenix.document.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.volvo.phoenix.document.datatype.ConflictType;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.DocumentAttributeConflictDTO;
import com.volvo.phoenix.document.dto.DocumentAttributeDTO;
import com.volvo.phoenix.document.dto.DocumentTypeConflictDTO;
import com.volvo.phoenix.document.dto.DocumentTypeDTO;
import com.volvo.phoenix.document.dto.DomainDTO;
import com.volvo.phoenix.document.dto.FamilyDTO;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.dto.SolutionParamDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentAttribute;
import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.Family;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.repository.AttributeDefinitionRepository;
import com.volvo.phoenix.document.repository.DocumentAttributeRepository;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.DocumentTypeRepository;
import com.volvo.phoenix.document.repository.DomainRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.service.ConflictRule;
import com.volvo.phoenix.document.translator.DocumentAttributeTranslator;
import com.volvo.phoenix.document.translator.DocumentTypeTranslator;
import com.volvo.phoenix.document.translator.DomainTranslator;
import com.volvo.phoenix.document.translator.FamilyTranslator;
import com.volvo.phoenix.orion.service.OrionDocumentService;

/**
 * Implements rule for document type. Checks if document type of moved document/folder is connected to domain of target folder. If not this means conflict.
 */
@Component
public class DocumentTypeRule implements ConflictRule {

    private final byte ruleOrder = 3;

    @Autowired
    private OrionDocumentService orionDocService;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DocumentAttributeRepository attrRepo;
    @Autowired
    private AttributeDefinitionRepository pnxAttrRepo;
    @Autowired
    private DocumentTypeRepository doctypeRepo;
    @Autowired
    private DocumentTypeTranslator documentTypeTranslator;
    @Autowired
    private DomainTranslator domainTranslator;
    @Autowired
    private DocumentAttributeTranslator documentAttributeTranslator;
    @Autowired
    private FamilyTranslator familyTranslator;

    /**
     * {@inheritDoc} Checks whether source document type is allowed document type in target folder domain.
     * 
     */
    @Override
    public ConflictDTO check(final OperationDTO operationDTO) {

        if (!isOperationPerformedOnDocument(operationDTO)) {
            return null;
        }

        final Folder targetFolder = folderRepository.findOne(operationDTO.getTarget().getId());
        final Document sourceDocument = documentRepository.findOne(operationDTO.getSource().getId());

        Assert.notNull(targetFolder, String.format("Logic error? Or someone remove/delete folder after the operation has been blocked? Missing folder id: %s",
                                                   operationDTO.getTarget().getId() != null ? operationDTO.getTarget().getId() : "N/A"));
        Assert.notNull(sourceDocument,
                       String.format("Logic error? Or someone remove/delete document after the operation has been blocked? Missing document id: %s",
                                     operationDTO.getSource().getId() != null ? operationDTO.getSource().getId() : "N/A"));

        if (!isDocumentDomainTheSameAsTargetAclDomain(sourceDocument, targetFolder)) {

            final List<AttributeDefinition> sourceDocumentTypeAttributes = sourceDocument != null ? sourceDocument.getType().getAttributes() : null;
            //TODO Collection could be empty
//            final List<AttributeDefinition> targetDocumentTypeAttributes = targetFolder != null && targetFolder.getAcl() != null
//                    && targetFolder.getAcl().getDocumentTypes() != null ? targetFolder.getAcl().getDocumentTypes().get(0)
//                                                                                                             .getAttributes() : null;

            final List<DocumentAttribute> documentTypeAttributes = extractDocumentTypeAttributes(sourceDocumentTypeAttributes, sourceDocument.getCmDocumentAttributes());
            final List<DocumentAttributeDTO> documentAttributeDTOs = documentAttributeTranslator.translateToDto(documentTypeAttributes);

            final Domain targetDomain = domainRepository.findByIdWithDoctypes(targetFolder.getAcl().getDomain().getId());

            if (targetFolderDomainsDoesNotContainDocumentType(sourceDocument, targetDomain)) {
                // return new DocumentAttributeConflictDTO(documentAttributeDTOs, targetAttributeDTOs, lostSourceDocumentAttributes);
                // return createDocumentTypeConflictDto(operationDTO, sourceDocument, targetDomain);

                final DocumentTypeDTO sourceDocumentTypeDTO = documentTypeTranslator.translateToDto(sourceDocument.getType());
                final DomainDTO targetDomainDTO = domainTranslator.translateToDto(targetDomain);
                final List<DocumentTypeDTO> targetDocumentTypeDTOs = documentTypeTranslator.translateToDto(targetDomain.getDocumentTypes());

                final List<DocumentType> documentTypes = targetDomain.getDocumentTypes();
                final Set<FamilyDTO> families = new HashSet<FamilyDTO>();
                for (final DocumentType docType : documentTypes) {
                    families.addAll(familyTranslator.translateToDto(docType.getFamilies()));
                }
                final List<FamilyDTO> familyDtos = new ArrayList<FamilyDTO>(families);
                Collections.sort(familyDtos, new Comparator<FamilyDTO>() {
                    @Override
                    public int compare(FamilyDTO o1, FamilyDTO o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                
                return new DocumentTypeConflictDTO(operationDTO, documentAttributeDTOs, sourceDocumentTypeDTO, targetDomainDTO, familyDtos);//targetDocumentTypeDTOs);
            }
        }

        return null;
    }

    private List<DocumentAttribute> extractDocumentTypeAttributes(final List<AttributeDefinition> documentTypeAttributes,
            final List<DocumentAttribute> documentAttributes) {

        final List<DocumentAttribute> documentDomainAttributes = new ArrayList<DocumentAttribute>();

        for (DocumentAttribute documentAttribute : documentAttributes) {
            for (AttributeDefinition documentTypeAttribute : documentTypeAttributes) {
                if (documentAttribute.getId().getAttribute().equals(documentTypeAttribute.getId())) {
                    documentDomainAttributes.add(documentAttribute);
                    break;
                }
            }
        }
        return documentDomainAttributes;
    }



    // private List<AttributeDefinition> populateDocumentAttributes(List<DocumentAttribute> soltuionAttributes, DocumentType docType) {
    // List<AttributeDefinition> phoenixAttrs = docType.getAttributes();
    // for (AttributeDefinition phoenixAttr : phoenixAttrs) {
    // DocumentAttribute docAttr = new DocumentAttribute();
    // docAttr.setAttribute(phoenixAttr);
    // soltuionAttributes.add(docAttr);
    // }
    // return phoenixAttrs;
    // }

    /**
     * Checks if operation is performed for document.
     * 
     * @param operation
     * @return
     */
    private boolean isOperationPerformedOnDocument(OperationDTO operation) {
        return NodeType.D.equals(operation.getSource().getType());
    }

    private boolean targetFolderDomainsDoesNotContainDocumentType(final Document sourceDocument, final Domain targetDomain) {

        final List<DocumentType> targetDomainDocumentTypes = targetDomain.getDocumentTypes();

        for (final DocumentType targetType : targetDomainDocumentTypes) {
            if (targetType.getId().equals(sourceDocument.getType().getId())) {
                return false;
            }
        }

        return true;
    }

    private boolean isDocumentDomainTheSameAsTargetAclDomain(final Document source, final Folder target) {
        return source.getDomain().getId().equals(target.getAcl().getDomain().getId());
    }

    @Override
    public byte getOrderInConflictRuleChain() {
        return ruleOrder;
    }

}

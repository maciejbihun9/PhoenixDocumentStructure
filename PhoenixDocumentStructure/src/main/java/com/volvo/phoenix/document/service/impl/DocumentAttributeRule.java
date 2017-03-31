package com.volvo.phoenix.document.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.volvo.phoenix.document.datatype.ConflictType;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.dto.AttributeDefinitionDTO;
import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.DocumentAttributeConflictDTO;
import com.volvo.phoenix.document.dto.DocumentAttributeDTO;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentAttribute;
import com.volvo.phoenix.document.entity.DomainAttributeDefinition;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.repository.DictionaryRepository;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.DomainRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.service.ConflictRule;
import com.volvo.phoenix.document.translator.AttributeDefinitionTranslator;
import com.volvo.phoenix.document.translator.DocumentAttributeTranslator;

@Component
@Transactional(propagation = Propagation.SUPPORTS)
public class DocumentAttributeRule implements ConflictRule {

    private final byte ruleOrder = 4;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DocumentAttributeTranslator documentAttributeTranslator;
    
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Autowired
    private AttributeDefinitionTranslator attributeDefinitionTranslator;

    /**
     * {@inheritDoc} Checks whether source document domain is diferent than target folder domain. If yes, then checks what are source document attributes
     * missing in target domain and what are target domain attributes missing in source document. Those missing attributes are encapsulated in returned {@Link
     *  ConflictDTO}.
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
        Assert.notNull(sourceDocument, String.format("Logic error? Or someone remove/delete document after the operation has been blocked? Missing document id: %s",
                                                     operationDTO.getSource().getId() != null ? operationDTO.getSource().getId() : "N/A"));

        // when the document has different domain
        if (isSourceDomainDifferentThanTargetDomain(targetFolder, sourceDocument)) {

            final List<AttributeDefinition> sourceDomainAttributes = sourceDocument != null ? sourceDocument.getDomain().getAttributes() : null;
            final List<AttributeDefinition> targetDomainAttributes = targetFolder != null ? targetFolder.getAcl().getDomain().getAttributes() : null;

            if (!targetDomainAttributes.isEmpty() || !sourceDomainAttributes.isEmpty()) {
                
                final List<DocumentAttribute> documentDomainAttributes = extractDocumentDomainAttributes(sourceDomainAttributes, sourceDocument.getCmDocumentAttributes());
                //final List<DocumentAttributeDTO> documentAttributeDTOs = documentAttributeTranslator.translateToDto(sourceDocument. getCmDocumentAttributes());
                final List<DocumentAttributeDTO> documentAttributeDTOs = documentAttributeTranslator.translateToDto(documentDomainAttributes);
                filterOutAttributesWithoutValue(documentAttributeDTOs);
                
                final List<AttributeDefinitionDTO> targetAttributeDTOs = attributeDefinitionTranslator.translateToDto(targetDomainAttributes);
                final List<Long> lostSourceDocumentAttributes = collectAttributesThatWillBeLost(documentAttributeDTOs, targetAttributeDTOs);

                if (documentAttributeDTOs.isEmpty() && targetAttributeDTOs.isEmpty()) {
                    return null;
                } else {
                    return new DocumentAttributeConflictDTO(documentAttributeDTOs, targetAttributeDTOs, lostSourceDocumentAttributes);
                }

            }
        }

        return null;
    }

    private void filterOutAttributesWithoutValue(final List<DocumentAttributeDTO> documentAttributeDTOs) {
        final Iterator<DocumentAttributeDTO> attrDtoIterator = documentAttributeDTOs.iterator();
        while (attrDtoIterator.hasNext()) {
            final DocumentAttributeDTO attrDto = attrDtoIterator.next();
            if (Strings.isNullOrEmpty(attrDto.getValue()) && Strings.isNullOrEmpty(attrDto.getDictionaryValue())) {
                attrDtoIterator.remove();
            }
        }
    }

    private List<DocumentAttribute> extractDocumentDomainAttributes(final List<AttributeDefinition> domainAttributes, final List<DocumentAttribute> documentAttributes) {
        
        final List<DocumentAttribute> documentDomainAttributes =  new ArrayList<DocumentAttribute>();
        
        for (DocumentAttribute documentAttribute : documentAttributes) {
            for (AttributeDefinition domainAttribute : domainAttributes) {
                if (documentAttribute.getId().getAttribute().equals(domainAttribute.getId())) {
                    documentDomainAttributes.add(documentAttribute);
                    break;
                }
            }
        }
        return documentDomainAttributes;
    }

    private List<Long> collectAttributesThatWillBeLost(final List<DocumentAttributeDTO> documentAttributes,
            final List<AttributeDefinitionDTO> attributeDefinitions) {

        final List<Long> lostAttributes = new ArrayList<Long>();
        for (final DocumentAttributeDTO documentAttributeDTO : documentAttributes) {
            if (!attributeDefinitions.contains(documentAttributeDTO.getAttribute())) {
                lostAttributes.add(documentAttributeDTO.getId().getAttributeId());
            }
        }
        return lostAttributes;
    }


//    private List<AttributeDefinition> determineTargetAttributesMissingInSourceDomain(Document sourceDocument, List<AttributeDefinition> sourceDomainAttributes,
//            List<AttributeDefinition> targetDomainAttributes) {
//
//        List<AttributeDefinition> targetAttributesMissingInSourceDomain = Lists.newArrayList();
//        for (AttributeDefinition targetAttribute : targetDomainAttributes) {
//            if (!attributesContainAnotherAttribute(sourceDomainAttributes, targetAttribute)) {
//                targetAttributesMissingInSourceDomain.add(targetAttribute);
//            }
//        }
//
//        return targetAttributesMissingInSourceDomain;
//    }
//
//    private List<AttributeDefinition> determineSourceAttributesMissingInTargetDomain(Document sourceDocument, List<AttributeDefinition> sourceAttributes,
//            List<AttributeDefinition> targetFolderAttributes) {
//        List<AttributeDefinition> missingSourceAttributesInTargetDomain = Lists.newArrayList();
//
//        for (AttributeDefinition sourceAttribute : sourceAttributes) {
//
//            if (!attributesContainAnotherAttribute(targetFolderAttributes, sourceAttribute)) {
//                missingSourceAttributesInTargetDomain.add(sourceAttribute);
//            }
//        }
//
//        return missingSourceAttributesInTargetDomain;
//    }

//    private boolean attributesContainAnotherAttribute(List<AttributeDefinition> attributes, AttributeDefinition anotherAttribute) {
//        for (AttributeDefinition attr : attributes) {
//            if (anotherAttribute.getId().equals(attr.getId())) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    private boolean isOperationPerformedOnDocument(OperationDTO op) {
        return NodeType.D == op.getSource().getType();
    }

    private boolean isSourceDomainDifferentThanTargetDomain(Folder target, Document source) {
        return !source.getDomain().getId().equals(target.getAcl().getDomain().getId());
    }

//    private void createDocumentAttributesMissingInTargetDomain(DocumentAttributeConflictDTO documentConflict) {
//        // List<AttributeDefinitionDTO> newAttrs = documentConflict.getNewAttributes();
//
//        // for (AttributeDefinitionDTO attr : newAttrs) {
//        // for (SolutionParamDTO param : documentConflict.getSolutionParams()) {
//        // if (param.getName().equals("ATTRIBUTE" + attr.getId().getAttributeId())) {
//        // attr.setValue(param.getValue());
//        // }
//        // }
//        //
//        // attrRepo.save(attributeDefinitionTranslator.translateToEntity(attr));
//        // }
//    }

//    private void deleteDocumentAttributesThatAreMissingInTargetDomain(DocumentAttributeConflictDTO documentConflict) {
//        // List<DocumentAttribute> missingAttrs = attributeDefinitionTranslator.translateToEntity(documentConflict.getNotFoundAttrs());
//        // for (DocumentAttribute attr : missingAttrs) {
//        // attrRepo.delete(attr);
//        // }
//    }

    @Override
    public byte getOrderInConflictRuleChain() {
        return ruleOrder;
    }

}

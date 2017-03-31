package com.volvo.phoenix.document.uploadtool.application;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.datatype.InfoClass;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.dto.PhoenixDocumentDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.DictionaryValue;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentAttributeValue;
import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.Family;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.NewDocumentDefaults;
import com.volvo.phoenix.document.entity.PhoenixAcl;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.service.DocumentStructureService;
import com.volvo.phoenix.document.service.TransactionStackService;
import com.volvo.phoenix.document.translator.DocumentTranslator;
import com.volvo.phoenix.document.uploadtool.application.dto.ValidateDocumentResultDTO;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolDocumentRepository;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationTreeNodeRepository;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocument;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocumentAttributeValue;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocumentStatus;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationTreeNode;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.dto.OrionUserDTO;
import com.volvo.phoenix.orion.service.OrionDocumentService;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Manage validated Upload Tool documents
 */
@Service
@Transactional
public class UploadToolDocumentServiceImpl implements UploadToolDocumentService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DocumentStructureService documentService;
    
    @Autowired
    private DocumentTranslator documentTranslator;
    
    @Autowired
    private UploadToolDocumentRepository repository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private OrionDocumentService orionDocumentService;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private UploadToolOperationTreeNodeRepository treeNodeRepository;
    
    @Autowired
    private TransactionStackService transactionStackService;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ValidateDocumentResultDTO createAndValidateDocument(UploadToolOperation operation, UploadToolOperationTreeNode node, List<Family> families,
            Domain domain, OrionUserDTO userDTO) {
        List<DocumentType> docTypesWithApproval = operation.getFolder().getAcl().getDocumentTypes();

        UploadToolDocument doc = new UploadToolDocument();
        doc.setTitle(node.getNodeText());
        doc.setTreeNode(node);
        processDocumentDetails(node.getDocumentAttributes(), doc, families, domain);
        if (doc.getRevision() == null) {
            doc.setRevision(1L);
        }
        if (doc.getType() != null) {
            processDocumentTypeAttributes(node.getDocumentAttributes(), doc);
        }
        if (doc.getName() != null) {
            evaluateAttributesForNewDocumentVersion(doc, hasAllMandatoryAttributes(doc));
        }
        if (!hasAllMandatoryAttributes(doc) && operation.getFolder().getNewDocumentDefaults() != null) {
            applyNewDocumentDefaultsForMissingMandatoryAttributes(doc, operation.getFolder().getNewDocumentDefaults());
        }
        if (doc.getType() != null && isDocTypeOnRequireApprovalList(doc.getType().getId(), docTypesWithApproval)) {
            doc.setStatus(UploadToolDocumentStatus.WORK);
        }
        processDomainAttributes(node.getDocumentAttributes(), doc, domain);
        autoEvaluateMissingAttributes(doc, userDTO);

        repository.save(doc);

        return new ValidateDocumentResultDTO(hasAllMandatoryAttributes(doc), doc.getRevision() > 1 , String.valueOf(doc.getRevision()));
    }

    
    private void autoEvaluateMissingAttributes(UploadToolDocument doc, OrionUserDTO userDTO) {
        if (doc.getIssueDate() == null) {
            doc.setIssueDate(new Date());
        }
        if (doc.getIssuerId() == null && doc.getIssuer() == null) {
            doc.setIssuerId(userDTO.getUsername());
            doc.setIssuer(userDTO.getRealname());
        }
        if (doc.getAuthorId() == null && doc.getAuthor() == null) {
            doc.setAuthorId(userDTO.getUsername());
            doc.setAuthor(userDTO.getRealname());
        }
    }

    private boolean hasAllMandatoryAttributes(UploadToolDocument doc) {
        return doc.getTitle() != null && doc.getIssueDate() != null && doc.getStatus() != null && doc.getStateId() != null && doc.getFamily() != null
                && doc.getType() != null && hasAllDocTypeAttributeValues(doc);
    }

    private void applyNewDocumentDefaultsForMissingMandatoryAttributes(UploadToolDocument doc, NewDocumentDefaults defaults) {
        if (doc.getStateId() == null) {
            doc.setStateId(defaults.getStateId());
        }
        if (doc.getStatus() == null && defaults.getStatus() != null) {
            doc.setStatus(UploadToolDocumentStatus.valueOf(defaults.getStatus().getStatus()));
        }
        if (doc.getFamily() == null || doc.getType() == null) {
            doc.setFamily(defaults.getFamily());
            doc.setType(defaults.getType());
            doc.setAttributesValues(new ArrayList<UploadToolDocumentAttributeValue>());
        }
    }

    private void evaluateAttributesForNewDocumentVersion(UploadToolDocument doc, boolean isAlreadyValid) {
        OrionDocumentDTO latestRevision = orionDocumentService.findLatestDocumentRevision(doc.getName());
        if (latestRevision != null) {
            doc.setRevision(Long.parseLong(latestRevision.getRevision()) + 1);
            if (!isAlreadyValid) {
                applyAttributesFromLatestVersion(doc, latestRevision);
            }
        }
    }

    private void applyAttributesFromLatestVersion(UploadToolDocument doc, OrionDocumentDTO latestRevision) {
        Document pnxDoc = documentRepository.findOne(latestRevision.getId());
        if (pnxDoc != null) {
            doc.setTitle(pnxDoc.getTitle());
            if (doc.getIssueDate() == null) {
                doc.setIssueDate(new Date());
            }
            if (doc.getStatus() == null) {
                doc.setStatus(UploadToolDocumentStatus.VALID);
            }
            if (doc.getStateId() == null) {
                doc.setStateId(latestRevision.getStateId());
            }
            if (doc.getFamily() == null || doc.getType() == null) {
                doc.setFamily(pnxDoc.getFamily());
                doc.setType(pnxDoc.getType());
                copyDocumentTypeAttributesFromLatestRevision(doc, pnxDoc);
            }
        }
    }

    private void copyDocumentTypeAttributesFromLatestRevision(UploadToolDocument doc, Document pnxDoc) {
        List<UploadToolDocumentAttributeValue> utDocAtrValues = new ArrayList<UploadToolDocumentAttributeValue>();
        for (DocumentAttributeValue atr : pnxDoc.getDocumentAttributes()) {
            UploadToolDocumentAttributeValue newVal = new UploadToolDocumentAttributeValue();
            newVal.setAttribute(atr.getId().getAttribute());
            newVal.setOwningDocument(doc);
            newVal.setValue(atr.getValue());
            utDocAtrValues.add(newVal);
        }
        doc.setAttributesValues(utDocAtrValues);
    }

    private void processDocumentDetails(Map<String, String> map, UploadToolDocument doc, List<Family> families, Domain domain) {
        doc.setName(map.get("Registration number"));
        doc.setAltDocId(map.get("Alternative number"));
        String issuerDateNode = map.get("Version date");
        if (issuerDateNode != null) {
            try {
                doc.setIssueDate(new SimpleDateFormat("yyyy-MM-dd").parse(issuerDateNode));
            } catch (ParseException e) {
                log.warn("Invalid attribute value 'Version date' =" + issuerDateNode);
            }
        }
        String protectInWorkeNode = map.get("Protect In-Work");
        if (protectInWorkeNode != null && protectInWorkeNode.toUpperCase().startsWith("Y")) {
            doc.setProtectInWork(true);
        }

        if (map.get("Title") != null) {
            doc.setTitle(map.get("Title"));
        }
        String authorNode = map.get("Author name");
        if (authorNode != null) {
            String[] authorNodes = authorNode.split(",");
            doc.setAuthor(authorNodes[0].trim());
            if (authorNodes.length > 1) {
                doc.setAuthorId(authorNodes[1].trim());
            }
        }
        String issuerNode = map.get("Issuer name");
        if (issuerNode != null) {
            String[] issuerNodes = issuerNode.split(",");
            doc.setIssuer(issuerNodes[0].trim());
            if (issuerNodes.length > 1) {
                doc.setIssuerId(issuerNodes[1].trim());
            }
        }
        doc.setDescription(map.get("Description"));
        doc.setNotes(map.get("Notes"));
        String versionNode = map.get("Version");
        if (versionNode != null) {
            try {
                doc.setRevision(Long.parseLong(versionNode.trim()));
            } catch (NumberFormatException e) {
                log.warn("Invalid attribute value 'Version' =" + versionNode);
            }
        }
        InfoClass state = InfoClass.getInfoClass(map.get("Info class"));
        if (state != null) {
            doc.setStateId(Long.valueOf(state.getId()));
        }

        String typeNode = map.get("Document type");
        if (typeNode != null) {
            doc.setType(findDocumentTypeByName(typeNode.trim(), domain));
        }

        String familyNode = map.get("Document family");
        if (familyNode != null) {
            doc.setFamily(findFamilyByName(familyNode.trim(), families));
        }
        if (doc.getType() != null && doc.getFamily() != null && !isValidFamilyForDocType(doc.getType().getFamilies(), doc.getFamily().getId())) {
            log.warn("Invalid family name =" + doc.getFamily().getName() + " for document type =" + doc.getType().getDescription());
            doc.setFamily(null);
        }
        doc.setStatus(UploadToolDocumentStatus.getStatus(map.get("Document status")));
    }

    private boolean isDocTypeOnRequireApprovalList(Long docTypeId, List<DocumentType> docTypesWithApproval) {
        for (DocumentType type : docTypesWithApproval) {
            if (type.getId().equals(docTypeId)) {
                return true;
            }
        }
        return false;
    }

    private void processDocumentTypeAttributes(Map<String, String> map, UploadToolDocument doc) {
        for (AttributeDefinition atr : doc.getType().getAttributes()) {
            if (atr.getLabel() != null) {
                String value = map.get(atr.getLabel());
                if (value != null) {
                    UploadToolDocumentAttributeValue atrValue = new UploadToolDocumentAttributeValue();
                    atrValue.setAttribute(atr.getId());
                    atrValue.setOwningDocument(doc);
                    atrValue.setValue(formatValueForPhoenixAttribute(value, atr));
                    doc.getAttributesValues().add(atrValue);
                }
            }
        }
    }

    private boolean hasAllDocTypeAttributeValues(UploadToolDocument doc) {
        boolean result = true;
        for (AttributeDefinition atr : doc.getType().getAttributes()) {
            result &= hasAttributeValue(doc, atr.getId());
        }
        return result;
    }

    private boolean hasAttributeValue(UploadToolDocument doc, Long atrId) {
        for (UploadToolDocumentAttributeValue atrValue : doc.getAttributesValues()) {
            if (atrValue.getAttribute().equals(atrId)) {
                return true;
            }
        }
        return false;
    }

    private void processDomainAttributes(Map<String, String> map, UploadToolDocument doc, Domain domain) {
        for (AttributeDefinition atr : domain.getAttributes()) {
            if (atr.getLabel() != null) {
                String value = map.get(atr.getLabel());
                if (value != null) {
                    UploadToolDocumentAttributeValue atrValue = new UploadToolDocumentAttributeValue();
                    atrValue.setAttribute(atr.getId());
                    atrValue.setOwningDocument(doc);
                    atrValue.setValue(formatValueForPhoenixAttribute(value, atr));
                    doc.getAttributesValues().add(atrValue);
                }
            }
        }
    }

    private String formatValueForPhoenixAttribute(String value, AttributeDefinition atr) {
        switch (atr.getType()) {
        case CHECKBOX:
            return (value.toUpperCase().startsWith("Y")) ? "Y" : null;
        case SELECT:
            for (DictionaryValue lovValue : atr.getDictionary().getValues()) {
                if (lovValue.getValue() != null && lovValue.getValue().equals(value)) {
                    return lovValue.getId().toString();
                }
            }
            log.warn("Invalid attribute value " + value + " for attribute " + atr.getLabel());
            return null;
        default:
            return value;
        }
    }

    private boolean isValidFamilyForDocType(List<Family> families, Long familyId) {
        for (Family family : families) {
            if (family.getId().equals(familyId)) {
                return true;
            }
        }
        return false;
    }

    private DocumentType findDocumentTypeByName(String name, Domain domain) {
        for (DocumentType type : domain.getDocumentTypes()) {
            if (type.getDescription() != null && type.getDescription().equals(name)) {
                return type;
            }
        }
        log.warn("Invalid attribute value 'Document type' =" + name + " for target domain =" + domain.getName());
        return null;
    }

    private Family findFamilyByName(String name, List<Family> families) {
        for (Family family : families) {
            if (family.getName() != null && family.getName().equals(name)) {
                return family;
            }
        }
        return null;
    }

    @Override
    public int getUploadToolValidatedDocumentsNumber(Long operationId) {
        return repository.getValidatedDocumentsNumberForOperation(operationId); 
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Folder createPhoenixFolder(UploadToolOperationTreeNode utFolder, Folder parentPnxFolder, PhoenixAcl acl, OrionUserDTO userDTO) {
        Folder folder = new Folder();
        folder.setText(utFolder.getNodeText());
        folder.setDescription(utFolder.getNodeText());
        folder.setType(NodeType.S);
        folder.setParent(parentPnxFolder);
        folder.setAcl(acl);
        folder.setOwner(utFolder.getOperation().getUser());
        if (userDTO != null) {
            folder.setOwnerRealname(userDTO.getRealname());
        }

        folder = folderRepository.save(folder);
        treeNodeRepository.updatePhoenixObjectReference(utFolder.getNodeId(), folder.getId());
        return folder;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Document createPhoenixDocument(OrionCMContext orionContext, UploadToolOperationTreeNode node, Folder destinationFolder, List<String> attachments) throws PhoenixDataAccessException, IOException {
        UploadToolDocument utDoc =  repository.findDocByTreeNodeId(node.getNodeId()); 
        
        PhoenixDocumentDTO pnxDocDTO = documentTranslator.translateUploadToolDocumentToPhoenixDocumentDTO(utDoc, destinationFolder);
        OrionDocumentDTO orionDocDTO = documentTranslator.translateUploadToolDocumentToOrionDocumentDTO(utDoc, destinationFolder, attachments);
        
        Long newVersionObjectId = null, previousVersionObjectId = null;
        
        if (orionDocDTO.isNewVersion()) {
            if (orionDocDTO.getName() == null) {
                OrionDocumentDTO latestRev = getLatestVersion(orionDocDTO, utDoc.getTitle(), destinationFolder);
                if (latestRev != null) {
                    orionDocDTO.setName(latestRev.getName());
                    previousVersionObjectId = latestRev.getId();
                }
            }
            newVersionObjectId = orionDocumentService.createNewVersion(orionContext, orionDocDTO);
        } else {
            newVersionObjectId = orionDocumentService.createDocument(orionContext, orionDocDTO);
        }
        
        pnxDocDTO.setObjectId(newVersionObjectId);
        
        Document document = documentService.createDocument(pnxDocDTO);
        treeNodeRepository.updatePhoenixObjectReference(node.getNodeId(), document.getId());
        
        if (previousVersionObjectId == null) {
            transactionStackService.documentCreated(newVersionObjectId);
        } else {
            transactionStackService.newVersionCreated(newVersionObjectId, previousVersionObjectId);
        }
        
        return document;
    }

    private OrionDocumentDTO getLatestVersion(OrionDocumentDTO orionDocDTO, String title, Folder folder) {
        List<Document> latestDocsWithSameTitle = documentRepository.findByFolder_IdAndTitleOrderByIdDesc(folder.getId(), title);
        if (latestDocsWithSameTitle.size() > 0) {
            Long objId = latestDocsWithSameTitle.get(0).getId();
            return orionDocumentService.findDocument(objId);
        }
        log.warn("Previous revision not found for document title {} under folder id: {}", title, folder.getId());
        return null;
    }

    @Override
    public UploadToolDocument getUploadToolDocumentByRootNode(long documentId) {
        return repository.findDocByTreeNodeId(documentId);
    }

}

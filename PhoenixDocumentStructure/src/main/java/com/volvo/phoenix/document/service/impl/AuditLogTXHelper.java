package com.volvo.phoenix.document.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.AuditLogDTO;
import com.volvo.phoenix.document.dto.DocumentAttributeAuditLogDTO;
import com.volvo.phoenix.document.dto.DocumentAuditLogDTO;
import com.volvo.phoenix.document.dto.FolderAuditLogDTO;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.DictionaryValue;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentAttribute;
import com.volvo.phoenix.document.entity.DocumentAttributePK;
import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.Operation;
import com.volvo.phoenix.document.repository.DocumentAttributeRepository;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.DocumentTypeRepository;
import com.volvo.phoenix.document.repository.DomainRepository;
import com.volvo.phoenix.document.service.AuditLogService;

@Service
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class AuditLogTXHelper {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    DocumentTypeRepository documentTypeRepository;
    @Autowired
    DocumentAttributeRepository documentAttributeRepository;
    
    @Autowired
    AuditLogService auditLogService;
    
    public <AL extends AuditLogDTO> AL populateWhoAndWhenAuditLog(Operation operation, AL auditLog) {
        auditLog.setWhen(new Date());
        auditLog.setUserVcnId(operation.getUser()); // not sure how to get user VCN ID and full name
        auditLog.setUserFullName(operation.getUser());

        return auditLog;
    }

    public FolderAuditLogDTO populateSourceFolderAuditLog(Operation operation, TreeNodeDTO sourceNode, TreeNodeDTO targetNode) {
        FolderAuditLogDTO folderAuditLog = new FolderAuditLogDTO();

        folderAuditLog.setSourcePath(sourceNode.getPath());
        folderAuditLog.setTargetPath(targetNode.getPath());
        folderAuditLog.setOperationType(operation.getOperationType());

        return folderAuditLog;
    }
    
    public DocumentAuditLogDTO populateTargetDocAuditLog(Operation operation, Long targetDocumentId, DocumentAuditLogDTO documentAuditLogDTO) {
        Document targetDoc = documentRepository.findOne(targetDocumentId);

        documentAuditLogDTO.setTargetDocType(targetDoc.getType().getName());
        documentAuditLogDTO.setTargetDomain(targetDoc.getDomain().getName());
        documentAuditLogDTO.setTargetMandatoryAttributes(extractMandatoryAttributes(targetDoc));
        documentAuditLogDTO.setTargetOptionalAttributes(extractOptionalAttributes(targetDoc));

        return documentAuditLogDTO;
    }

    public DocumentAuditLogDTO populateSourceDocAuditLog(Operation operation, TreeNodeDTO sourceNode, TreeNodeDTO targetNode) {
        Document sourceDoc = documentRepository.findOne(sourceNode.getId());

        DocumentAuditLogDTO documentAuditLogDTO = new DocumentAuditLogDTO();
        documentAuditLogDTO.setSourcePath(sourceNode.getPath());
        documentAuditLogDTO.setTargetPath(prepareTargetPathForDocument(sourceNode, targetNode));
        documentAuditLogDTO.setSourceDocType(sourceDoc.getType().getName());
        documentAuditLogDTO.setSourceDomain(sourceDoc.getDomain().getName());
        documentAuditLogDTO.setSourceMandatoryAttributes(extractMandatoryAttributes(sourceDoc));
        documentAuditLogDTO.setSourceOptionalAttributes(extractOptionalAttributes(sourceDoc));
        documentAuditLogDTO.setOperationType(operation.getOperationType());

        return documentAuditLogDTO;
    }
    
    private String prepareTargetPathForDocument(TreeNodeDTO sourceNode, TreeNodeDTO targetNode) {
        return targetNode.getPath() + "\\" + sourceNode.getName() ;
    }

    /**
     * Store audit log through service.
     * 
     * @param auditLog
     */
    public void storeAuditTrail(AuditLogDTO auditLog) {
        List<AuditLogDTO> auditLogs = Lists.newArrayList(auditLog);
        auditLogService.storeAuditLogs(auditLogs);
    }

    /**
     * Extracts optional attributes for given document.
     * 
     * @param document
     * 
     * @return mandatory attributes in form: attrName1=value1, attrName2=value2 ...
     */
    private List<DocumentAttributeAuditLogDTO> extractOptionalAttributes(Document document) {
        Domain domain = domainRepository.findById(document.getDomain().getId());
        return extractAttributesAndFormat(document, domain.getAttributes());
    }

    /**
     * Extracts mandatory attributes for given document.
     * 
     * @param document
     * 
     * @return mandatory attributes in form: attrName1=value1, attrName2=value2 ...
     */
    private List<DocumentAttributeAuditLogDTO> extractMandatoryAttributes(Document document) {
        DocumentType docType = documentTypeRepository.findByIdWithAttributes(document.getType().getId());
        return extractAttributesAndFormat(document, docType.getAttributes());
    }

    private List<DocumentAttributeAuditLogDTO> extractAttributesAndFormat(Document document,  List<AttributeDefinition> docTypemandatoryAttributes) {
        List<DocumentAttributeAuditLogDTO> attributeDtos = Lists.newArrayList();
        for (AttributeDefinition attr : document.getAttributes()) {
            for (AttributeDefinition mandatoryAttr : docTypemandatoryAttributes) {
                if (attr.getId().equals(mandatoryAttr.getId())) {

                    DocumentAttributePK pk = new DocumentAttributePK();
                    pk.setDocument(document.getId());
                    pk.setAttribute(attr.getId());
                    DocumentAttribute docAttr = documentAttributeRepository.findOne(pk);
                    
                    String name = attr.getName();

                    String value = prepareAttributeValue(attr, docAttr.getValue());


                    attributeDtos.add(new DocumentAttributeAuditLogDTO(name, value));
                    break;
                }
            }
        }
        return attributeDtos;
    }

    private String prepareAttributeValue(AttributeDefinition attr, String value) {
        String result = value;
        switch(attr.getType()){
            case CHECKBOX:
                result = value==null? "N" : "Y";
                break;
            case SELECT:
                if(value!=null){
                    Long valueId= Long.parseLong(value);
                    for (DictionaryValue dictionaryValue : attr.getDictionary().getValues()) {
                        if(dictionaryValue.getId().equals(valueId)){
                            result=dictionaryValue.getValue();
                            break;
                        }
                    }
                }
                break;
        }
        return result;
    }
}

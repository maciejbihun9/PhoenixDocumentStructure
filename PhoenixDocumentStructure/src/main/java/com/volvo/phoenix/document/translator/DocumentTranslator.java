package com.volvo.phoenix.document.translator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.dto.PhoenixDocumentDTO;
import com.volvo.phoenix.document.dto.PhoenixFieldDTO;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.entity.CurrentDocument;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentAttributePK;
import com.volvo.phoenix.document.entity.DocumentAttributeValue;
import com.volvo.phoenix.document.entity.DocumentStatus;
import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.Family;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocument;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocumentAttributeValue;
import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.dto.OrionAclStateDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.dto.OrionFileDTO;

@Component
@Transactional(propagation=Propagation.REQUIRED)
public class DocumentTranslator {

    private static final long NEW_DOC_INDEX_ID = 1L;
    private static final String NEW_DOC_OBJ_TYPE = "O";

    public List<TreeNodeDTO> translateToTreeNodeDto(List<Document> folders) {
        List<TreeNodeDTO> nodes = new ArrayList<TreeNodeDTO>();
        for (Document doc : folders) {
            nodes.add(translateToTreeNodeDto(doc));
        }
        return nodes;
    }

    public TreeNodeDTO translateToTreeNodeDto(Document doc) {
        TreeNodeDTO node = new TreeNodeDTO();
        node.setId(doc.getId());
        node.setName(doc.getTitle());
        node.setType(NodeType.D);
        node.setPath(doc.getPath());
        node.setFamily(doc.getFamily().getName());
        node.setDocType(doc.getType().getName());
        node.setOwner(doc.getAuthor());
        return node;
    }

    public TreeNodeDTO translateToTreeNodeDto(Document doc, OrionDocumentDTO orionDoc) {
        TreeNodeDTO node = new TreeNodeDTO();
        node.setId(doc.getId());
        node.setName(doc.getTitle());
        node.setType(NodeType.D);
        node.setPath(doc.getPath());
        node.setFamily(doc.getFamily().getName());
        node.setDocType(doc.getType().getName());
        node.setVersion(orionDoc.getRevision());
        node.setOwner(doc.getAuthorId());
        node.setOwnerRealname(doc.getAuthor());
        node.setInfoClass(orionDoc.getAclState().getName());
        node.setDate((new SimpleDateFormat("yyyy-MM-dd")).format(doc.getIssueDate()));
        node.setStatus(doc.getStatus().getDescription());
        node.setParentId(doc.getFolder().getId());
        return node;
    }

    public TreeNodeDTO translateCurrentDocumentToTreeNodeDto(CurrentDocument doc) {
        TreeNodeDTO node = new TreeNodeDTO();
        node.setId(doc.getId());
        node.setName(doc.getTitle());
        node.setType(NodeType.D);
        node.setPath(doc.getPath());
        node.setFamily(doc.getFamily().getName());
        node.setDocType(doc.getType().getName());
        node.setVersion(doc.getRevision());
        node.setOwner(doc.getAuthorId());
        node.setOwnerRealname(doc.getAuthor());
        node.setInfoClass(doc.getAclStateName());
        node.setDate((new SimpleDateFormat("yyyy-MM-dd")).format(doc.getIssueDate()));
        node.setStatus(doc.getStatus().getDescription());
        node.setParentId(doc.getFolder().getId());
        return node;
    }
    
    public PhoenixDocumentDTO translateToPhoenixDTO(Document doc) {
        PhoenixDocumentDTO d = new PhoenixDocumentDTO();
        
        d.setObject_no(doc.getObjectNo());
        d.setObjectType(doc.getObjectType());
        d.setIndexId(doc.getIndexId());
        d.setObject_no_suffix(doc.getObjectNoSuffix());
        d.setDecision(doc.getDecision());
        d.setFunction_group(doc.getFunctionGroup());
        d.setGate(doc.getGate());
        d.setProduct_class(doc.getProductClass());
        d.setBrand(doc.getBrand());
        d.setProtectInWork(doc.getProtectInWork());
        d.setNode_id(doc.getFolder().getId());
        d.setDomain_id(doc.getDomain().getId());
        d.setDoctype_id(doc.getType().getId());
        d.setFamily_id(doc.getFamily().getId());
        d.setDescription(doc.getDescription());
        d.setTitle(doc.getTitle());
        d.setIssuer(doc.getIssuer());
        d.setIssuer_id(doc.getIssuerId());
        d.setIssue_date(doc.getIssueDate());
        d.setAuthor(doc.getAuthor());
        d.setAuthor_id(doc.getAuthorId());
        d.setPersonal(doc.getPersonal());
        d.setNotes(doc.getNotes());
        d.setLogbookmajorversion(doc.getLogbookMajorVersion());
        d.setTempstore(doc.getTempstore());
        d.setDocument_status(doc.getStatus().getStatus());
        d.setContent_status(doc.getContentStatus());
        d.setAlt_doc_id(doc.getAltDocId());
        d.setDocumentcontainer(doc.getDocumentContainer());

        List<PhoenixFieldDTO> attributeValues = new ArrayList<PhoenixFieldDTO>();
        for (DocumentAttributeValue atr : doc.getDocumentAttributes()) {
            PhoenixFieldDTO f = new PhoenixFieldDTO();
            f.setFieldId(atr.getId().getAttribute());
            f.setValue(atr.getValue());
            f.setObjectType(atr.getObjectType());
            f.setIndexId(atr.getIndexId());
            attributeValues.add(f);
        }
        d.setAttributesValues(attributeValues);
        return d;
    }

    public Document translateToDocument(PhoenixDocumentDTO doc) {
        Document d = new Document();
        
        d.setId(doc.getObjectId());
        d.setObjectType(doc.getObjectType());
        d.setIndexId(doc.getIndexId());
        d.setObjectNo(doc.getobject_no());
        d.setObjectNoSuffix(doc.getobject_no_suffix());
        d.setDecision(doc.getdecision());
        d.setFunctionGroup(doc.getfunction_group());
        d.setGate(doc.getgate());
        d.setProductClass(doc.getproduct_class());
        d.setBrand(doc.getbrand());
        d.setProtectInWork(doc.getProtectInWork());
        Folder folder = new Folder();
        folder.setId(Long.valueOf(doc.getnode_id()));
        d.setFolder(folder);
        Domain domain = new Domain();
        domain.setId(Long.valueOf(doc.getdomain_id()));
        d.setDomain(domain);
        DocumentType documentType = new DocumentType();
        documentType.setId(Long.valueOf(doc.getdoctype_id()));
        d.setType(documentType);
        Family family = new Family();
        family.setId(Long.valueOf(doc.getfamily_id()));
        d.setFamily(family);
        d.setDescription(doc.getdescription());
        d.setTitle(doc.gettitle());
        d.setIssuer(doc.getissuer());
        d.setIssuerId(doc.getissuer_id());
        d.setIssueDate(doc.getissue_date());
        d.setAuthor(doc.getauthor());
        d.setAuthorId(doc.getauthor_id());
        d.setPersonal(doc.getpersonal());
        d.setNotes(doc.getnotes());
        d.setLogbookMajorVersion(doc.getlogbookmajorversion());
        d.setTempstore(doc.gettempstore());
        DocumentStatus status = new DocumentStatus();
        status.setStatus(doc.getdocument_status());
        d.setStatus(status);
        d.setContentStatus(doc.getcontent_status());
        d.setAltDocId(doc.getalt_doc_id());
        d.setDocumentContainer(doc.getdocumentcontainer());

        List<DocumentAttributeValue> attributeValues = new ArrayList<DocumentAttributeValue>(); 
        for (PhoenixFieldDTO f : doc.getAttributeValues()) {
            DocumentAttributeValue atr = new DocumentAttributeValue();
            DocumentAttributePK attributeId = new DocumentAttributePK();
            attributeId.setAttribute(Long.valueOf(f.getFieldId()));
            attributeId.setDocument(doc.getObjectId());
            atr.setId(attributeId);
            atr.setValue(f.getValue());
            atr.setObjectType(f.getObjectType());
            atr.setIndexId(f.getIndexId());
            attributeValues.add(atr);
        }
        d.setDocumentAttributes(attributeValues);       
        
        return d;
    }
    
    public PhoenixDocumentDTO translateUploadToolDocumentToPhoenixDocumentDTO(UploadToolDocument doc, Folder destinationFolder) {
        PhoenixDocumentDTO d = new PhoenixDocumentDTO();
        
        d.setObjectType(NEW_DOC_OBJ_TYPE);
        d.setIndexId(NEW_DOC_INDEX_ID);
        d.setProtectInWork((doc.getProtectInWork() != null &&  doc.getProtectInWork()) ? "Y" : null);
        d.setNode_id(destinationFolder.getId());
        d.setDomain_id(destinationFolder.getAcl().getDomain().getId());
        d.setDoctype_id(doc.getType().getId());
        d.setFamily_id(doc.getFamily().getId());
        d.setDescription(doc.getDescription());
        d.setTitle(doc.getTitle());
        d.setIssuer(doc.getIssuer());
        d.setIssuer_id(doc.getIssuerId());
        d.setIssue_date(doc.getIssueDate());
        d.setAuthor(doc.getAuthor());
        d.setAuthor_id(doc.getAuthorId());
        d.setNotes(doc.getNotes());
        d.setDocument_status(doc.getStatus().name());
        d.setAlt_doc_id(doc.getAltDocId());

        List<PhoenixFieldDTO> attributeValues = new ArrayList<PhoenixFieldDTO>();
        long index = 1;
        for (UploadToolDocumentAttributeValue atr : doc.getAttributesValues()) {
            PhoenixFieldDTO f = new PhoenixFieldDTO();
            f.setFieldId(atr.getAttribute());
            f.setValue(atr.getValue());
            f.setObjectType(NEW_DOC_OBJ_TYPE);
            f.setIndexId(index++);
            attributeValues.add(f);
        }
        d.setAttributesValues(attributeValues);
        return d;
    }
    
    public OrionDocumentDTO translateUploadToolDocumentToOrionDocumentDTO(UploadToolDocument doc, Folder destinationFolder, List<String> attachments) {
        OrionDocumentDTO dto = new OrionDocumentDTO();
        dto.setId(doc.getId());
        dto.setObjectType(NEW_DOC_OBJ_TYPE);
        dto.setName(doc.getName());
        dto.setRevision(doc.getRevision().toString());
        
        OrionAclStateDTO aclState = new OrionAclStateDTO();
        aclState.setId(doc.getStateId());
        dto.setAclState(aclState);
        
        OrionAclDTO aclDTO = new OrionAclDTO();
        aclDTO.setId(destinationFolder.getAcl().getId());
        dto.setAcl(aclDTO);
       
        for (String attachment : attachments) {
            OrionFileDTO fileDTO = new OrionFileDTO();
            fileDTO.setInputName(attachment);
            dto.getFileList().add(fileDTO);
        }
        return dto;
    }
    
}

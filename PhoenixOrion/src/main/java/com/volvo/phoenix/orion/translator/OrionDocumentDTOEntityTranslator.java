package com.volvo.phoenix.orion.translator;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.dto.OrionFileDTO;
import com.volvo.phoenix.orion.entity.OrionAcl;
import com.volvo.phoenix.orion.entity.OrionAclState;
import com.volvo.phoenix.orion.entity.OrionComponent;
import com.volvo.phoenix.orion.entity.OrionDocument;
import com.volvo.phoenix.orion.entity.OrionFile;

/**
 * Translator to translate DTO and entity
 * 
 * @author v0cn181
 */
@Component
@Transactional(propagation=Propagation.REQUIRED)
public class OrionDocumentDTOEntityTranslator {

    private static final String NEW_DOCUMENT_OBJ_TYPE = "O";
    private static final Long NEW_DOCUMENT_CTX_ID = -1L;

    public OrionDocumentDTO toDTO(OrionDocument orionDocument) {
        OrionDocumentDTO dto = new OrionDocumentDTO();
        dto.setId(orionDocument.getId());
        dto.setObjectType(orionDocument.getObjectType());
        dto.setName(orionDocument.getName());
        dto.setRevision(orionDocument.getRevision());
        dto.setAclState(AclStateDTOEntityTranslator.toDTO(orionDocument.getAclState()));
        
        OrionAclDTO aclDTO = new OrionAclDTO();
        aclDTO.setId(orionDocument.getAcl().getId());
        aclDTO.setName(orionDocument.getAcl().getName());
        dto.setAcl(aclDTO);
        
        for (OrionComponent comp : orionDocument.getComponents()) {
            if (comp.getRepresentations() != null &&
                comp.getRepresentations().size() > 0 &&
                comp.getRepresentations().get(0).getFiles().size() > 0 &&
                comp.getRepresentations().get(0).getFiles().get(0) != null) {
            
                OrionFile orionFile = comp.getRepresentations().get(0).getFiles().get(0);    
                    
                OrionFileDTO fileDTO = new OrionFileDTO();
                fileDTO.setAliasType(comp.getRepresentations().get(0).getRepresentationInfo().getAlasType());
                fileDTO.setInputName(orionFile.getInputName());
                fileDTO.setSheetName(comp.getName());
                
                dto.getFileList().add(fileDTO);
            }
        }
        return dto;
    }

    public OrionDocument toEntity(OrionDocumentDTO dto) {
        OrionDocument doc = new OrionDocument();
        doc.setObjectType(NEW_DOCUMENT_OBJ_TYPE);
        doc.setCtxId(NEW_DOCUMENT_CTX_ID);
        
        doc.setName(dto.getName());
        doc.setRevision(dto.getRevision());
        OrionAclState aclState = new OrionAclState();
        aclState.setId(dto.getStateId());
        doc.setAclState(aclState);
        
        OrionAcl acl = new OrionAcl();
        acl.setId(dto.getAclId());
        doc.setAcl(acl);
        
        return doc;
    }

}

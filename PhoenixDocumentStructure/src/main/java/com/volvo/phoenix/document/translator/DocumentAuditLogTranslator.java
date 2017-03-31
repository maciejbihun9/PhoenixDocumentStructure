package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.DocumentAttributeAuditLogDTO;
import com.volvo.phoenix.document.dto.DocumentAuditLogDTO;
import com.volvo.phoenix.document.entity.DocumentAttributeAuditLog;
import com.volvo.phoenix.document.entity.DocumentAuditLog;

/**
 * Entity <-> DTO translator for {@link DocumentAuditLog}.
 * @author bpld313
 *
 */
@Component
public class DocumentAuditLogTranslator {

    public List<DocumentAuditLogDTO> translateToDto(List<DocumentAuditLog> entities) {
        List<DocumentAuditLogDTO> dtos = Lists.newArrayList();
        
        for (DocumentAuditLog entity : entities) {
            dtos.add(translateToDto(entity));
        }
        return dtos;
    }

    public DocumentAuditLogDTO translateToDto(DocumentAuditLog entity) {
        DocumentAuditLogDTO dto = new DocumentAuditLogDTO();
        
        dto.setId(entity.getId());
        dto.setUserVcnId(entity.getUserVcnId());
        dto.setUserFullName(entity.getUserFullName());
        dto.setWhen(entity.getWhen());

        dto.setSourceDocType(entity.getSourceDocType());
        dto.setSourceDomain(entity.getSourceDomain());
        dto.setSourceMandatoryAttributes(translateAttributes(entity.getSourceMandatoryAttributesAsList()));
        dto.setSourceOptionalAttributes(translateAttributes(entity.getSourceOptionalAttributesAsList()));
        dto.setSourcePath(entity.getSourcePath());
        dto.setTargetDocType(entity.getTargetDocType());
        dto.setTargetDomain(entity.getTargetDomain());
        dto.setTargetMandatoryAttributes(translateAttributes(entity.getTargetMandatoryAttributesAsList()));
        dto.setTargetOptionalAttributes(translateAttributes(entity.getTargetOptionalAttributesAsList()));
        dto.setTargetPath(entity.getTargetPath());
        dto.setOperationType(entity.getOperationType());
        
        return dto;
    }

    private List<DocumentAttributeAuditLogDTO> translateAttributes(List<DocumentAttributeAuditLog> attributes) {
        List<DocumentAttributeAuditLogDTO> attributeDTOs = Lists.newArrayList();

        for (DocumentAttributeAuditLog attr : attributes) {
            attributeDTOs.add(translateAttribute(attr));
        }

        return attributeDTOs;
    }

    private DocumentAttributeAuditLogDTO translateAttribute(DocumentAttributeAuditLog attr) {
        return new DocumentAttributeAuditLogDTO(attr.getName(), attr.getValue());
    }

    public DocumentAuditLog translateToEntity(DocumentAuditLogDTO dto) {
        DocumentAuditLog entity = new DocumentAuditLog();
        
        entity.setId(dto.getId());
        entity.setWhen(dto.getWhen());
        entity.setUserVcnId(dto.getUserVcnId());
        entity.setUserFullName(dto.getUserFullName());
        entity.setOperationType(dto.getOperationType());

        entity.setSourceDocType(dto.getSourceDocType());
        entity.setSourceDomain(dto.getSourceDomain());
        entity.setSourceMandatoryAttributes(translateAttributeDtos(dto.getSourceMandatoryAttributes()));
        entity.setSourceOptionalAttributes(translateAttributeDtos(dto.getSourceOptionalAttributes()));
        entity.setSourcePath(dto.getSourcePath());
        entity.setTargetDocType(dto.getTargetDocType());
        entity.setTargetDomain(dto.getTargetDomain());
        entity.setTargetMandatoryAttributes(translateAttributeDtos(dto.getTargetMandatoryAttributes()));
        entity.setTargetOptionalAttributes(translateAttributeDtos(dto.getTargetOptionalAttributes()));
        entity.setTargetPath(dto.getTargetPath());
        
        return entity;
    }

    private String translateAttributeDtos(List<DocumentAttributeAuditLogDTO> attributeDTOs) {
        String attributes = "";

        for (DocumentAttributeAuditLogDTO attr : attributeDTOs) {
            if (!StringUtils.isEmpty(attributes)) {
                attributes += DocumentAuditLog.ATTRIBUTES_SEPARATOR;
            }
            attributes += translateAttribute(attr);
        }

        return attributes;
    }

    private String translateAttribute(DocumentAttributeAuditLogDTO attr) {
        return attr.getName() + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + attr.getValue();
    }
    
}

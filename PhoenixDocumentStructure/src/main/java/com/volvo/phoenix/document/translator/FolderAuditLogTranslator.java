package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.FolderAuditLogDTO;
import com.volvo.phoenix.document.entity.FolderAuditLog;

/**
 * Entity <-> DTO translator for {@link FolderAuditLog}.
 * @author bpld313
 *
 */
@Component
public class FolderAuditLogTranslator {

    public List<FolderAuditLogDTO> translateToDto(List<FolderAuditLog> entities) {
        List<FolderAuditLogDTO> dtos = Lists.newArrayList();
        
        for (FolderAuditLog entity : entities) {
            dtos.add(translateToDto(entity));
        }
        return dtos;
    }

    public FolderAuditLogDTO translateToDto(FolderAuditLog entity) {
        FolderAuditLogDTO dto = new FolderAuditLogDTO();
        
        dto.setId(entity.getId());
        dto.setSourcePath(entity.getSourcePath());
        dto.setTargetPath(entity.getTargetPath());
        dto.setWhen(entity.getWhen());
        dto.setUserFullName(entity.getUserFullName());
        dto.setUserVcnId(entity.getUserVcnId());
        dto.setOperationType(entity.getOperationType());
        
        return dto;
    }

    public FolderAuditLog translateToEntity(FolderAuditLogDTO dto) {
        FolderAuditLog entity = new FolderAuditLog();
        
        entity.setId(dto.getId());
        entity.setSourcePath(dto.getSourcePath());
        entity.setTargetPath(dto.getTargetPath());
        entity.setWhen(dto.getWhen());
        entity.setUserFullName(dto.getUserFullName());
        entity.setUserVcnId(dto.getUserVcnId());
        entity.setOperationType(dto.getOperationType());
        
        return entity;
    }

}

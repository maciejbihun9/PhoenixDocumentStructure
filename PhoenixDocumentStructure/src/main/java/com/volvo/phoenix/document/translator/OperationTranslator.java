package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.entity.DocumentOperation;
import com.volvo.phoenix.document.entity.FolderOperation;
import com.volvo.phoenix.document.entity.Operation;

@Component
@Transactional(propagation = Propagation.SUPPORTS)
public class OperationTranslator {

    @Autowired
    private NodeResolver nodeResolver;

    public List<OperationDTO> translateToDto(List<Operation> entities) {
        List<OperationDTO> dtos = Lists.newArrayList();

        for (Operation op : entities) {
            dtos.add(translateToDto(op));
        }
        return dtos;
    }

    public OperationDTO translateToDto(Operation operation) {
        if (operation instanceof DocumentOperation) {
            return translateToDto((DocumentOperation) operation);
        } else if (operation instanceof FolderOperation) {
            return translateToDto((FolderOperation) operation);
        } else {
            throw new IllegalArgumentException("Cannot translate unknown type of operation: " + operation.getClass());
        }

    }

    public OperationDTO translateToDto(DocumentOperation operation) {
        OperationDTO dto = new OperationDTO();
        dto.setId(operation.getId());
        dto.setOperationType(operation.getOperationType());
        dto.setSource(nodeResolver.resolveDocumentNode(operation.getSourceDocumenId()));

        dto.setStatus(operation.getStatus());
        dto.setCreateDate(operation.getCreateDate());
        dto.setTarget(nodeResolver.resolveFolderNode(operation.getTargetFolderId()));

        return dto;
    }

    public OperationDTO translateToDto(FolderOperation operation) {
        OperationDTO dto = new OperationDTO();
        dto.setId(operation.getId());
        dto.setOperationType(operation.getOperationType());
        dto.setSource(nodeResolver.resolveFolderNode(operation.getSourceFolderId()));

        dto.setStatus(operation.getStatus());
        dto.setCreateDate(operation.getCreateDate());
        dto.setTarget(nodeResolver.resolveFolderNode(operation.getTargetFolderId()));

        return dto;
    }

}

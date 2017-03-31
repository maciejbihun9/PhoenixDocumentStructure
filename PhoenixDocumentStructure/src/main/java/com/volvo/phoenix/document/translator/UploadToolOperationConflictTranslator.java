package com.volvo.phoenix.document.translator;

import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolOperationConflictDTO;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationConflict;

import java.util.ArrayList;
import java.util.List;

public final class UploadToolOperationConflictTranslator {

    public static List<UploadToolOperationConflictDTO> listToDTO(List<UploadToolOperationConflict> entities) {
        List<UploadToolOperationConflictDTO> dtos = new ArrayList<UploadToolOperationConflictDTO>();
        for (UploadToolOperationConflict e : entities) {
            dtos.add(toDTO(e));
        }
        return dtos;
    }

    public static UploadToolOperationConflictDTO toDTO(UploadToolOperationConflict operation) {
        UploadToolOperationConflictDTO dto = new UploadToolOperationConflictDTO();
        dto.setConflict(operation.getConflict().getDescription());
        dto.setFolderPath(operation.getTreeNode().getNodeText());
        return dto;
    }
}

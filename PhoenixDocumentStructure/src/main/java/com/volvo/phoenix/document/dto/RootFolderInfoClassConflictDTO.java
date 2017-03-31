package com.volvo.phoenix.document.dto;

import com.volvo.phoenix.document.datatype.ConflictLevel;
import com.volvo.phoenix.document.datatype.ConflictType;

public class RootFolderInfoClassConflictDTO extends ConflictDTO {

    private static final long serialVersionUID = -1938740593894366193L;

    private String missingInfoClass;

    public RootFolderInfoClassConflictDTO(final String infoClass) {
        setMissingInfoClass(infoClass);
    }

    public RootFolderInfoClassConflictDTO(final String infoClass, final OperationDTO operationDTO) {
        setMissingInfoClass(infoClass);
        setOperation(operationDTO);
    }

    @Override
    public ConflictType getType() {
        return ConflictType.ROOT;
    }

    @Override
    public ConflictLevel getLevel() {
        return ConflictLevel.FATAL_ERROR;
    }

    public String getMissingInfoClass() {
        return missingInfoClass;
    }

    public void setMissingInfoClass(final String missingInfoClass) {
        this.missingInfoClass = missingInfoClass;
    }
}

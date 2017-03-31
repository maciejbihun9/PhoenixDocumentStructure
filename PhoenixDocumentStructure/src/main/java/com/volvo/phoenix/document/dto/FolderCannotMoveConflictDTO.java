package com.volvo.phoenix.document.dto;

import com.volvo.phoenix.document.datatype.ConflictLevel;
import com.volvo.phoenix.document.datatype.ConflictType;

public class FolderCannotMoveConflictDTO extends ConflictDTO {

    private static final long serialVersionUID = 6769199307938208154L;

    private FolderDTO folder;

    public FolderDTO getFolder() {
        return folder;
    }

    public void setFolder(FolderDTO folder) {
        this.folder = folder;
    }

    public FolderCannotMoveConflictDTO(FolderDTO sourceFolder) {
        this.folder = sourceFolder;
    }

    @Override
    public ConflictType getType() {
        return ConflictType.APP;
    }

    @Override
    public ConflictLevel getLevel() {
        return ConflictLevel.FATAL_ERROR;
    }

}

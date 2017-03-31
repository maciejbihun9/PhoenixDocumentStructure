package com.volvo.phoenix.document.uploadtool.application.dto;

public class UploadToolOperationConflictDTO {

    private String conflict;
    private String folderPath;

    public String getConflict() {
        return conflict;
    }

    public void setConflict(String conflict) {
        this.conflict = conflict;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
}

package com.volvo.phoenix.document.uploadtool.model;

/**
 * Document validation conflict type Value Object
 */
public enum UploadToolConflictType {

    FOLDER_NAME("Folder conflict");

    private final String description;

    UploadToolConflictType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

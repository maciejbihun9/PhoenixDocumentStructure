package com.volvo.phoenix.document.uploadtool.model;

/**
 * Uploaded file type information Value Object
 */
public enum UploadToolFileType {
    
    Z ("zip archive to be deflated"),
    D ("document file");
    
    private final String description;
    
    private UploadToolFileType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

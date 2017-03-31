package com.volvo.phoenix.document.uploadtool.model;

/**
 * Uploaded tree structure element type information Value Object
 */
public enum UploadToolNodeType {
    
    S ("slave folder"),
    D ("document"), 
    F ("attached file");
    
    private final String description;
    
    private UploadToolNodeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

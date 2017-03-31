package com.volvo.phoenix.document.uploadtool.model;

/**
 * Possible document statuses for documents uploaded with Upload Tool Value Object
 */
public enum UploadToolDocumentStatus {
    
    WORK ("In-Work"),
    VALID ("Valid"); 
    
    private final String description;
    
    private UploadToolDocumentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    public static UploadToolDocumentStatus getStatus(final String statusDescription) {
        for (final UploadToolDocumentStatus status : values()) {
            if (status.description.equalsIgnoreCase(statusDescription)) {
                return status;
            }
        }
        return null;
    }

}

package com.volvo.phoenix.document.uploadtool.application.dto;

public class ValidateDocumentResultDTO {

    private boolean valid;
    private boolean newVersion;
    private String revision;

    public ValidateDocumentResultDTO(boolean valid, boolean newVersion, String revision) {
        super();
        this.valid = valid;
        this.newVersion = newVersion;
        this.revision = revision;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

}

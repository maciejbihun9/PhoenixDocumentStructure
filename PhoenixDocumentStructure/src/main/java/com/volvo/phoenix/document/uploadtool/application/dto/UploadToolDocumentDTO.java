package com.volvo.phoenix.document.uploadtool.application.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.volvo.phoenix.document.uploadtool.model.UploadToolDocumentStatus;
import com.volvo.phoenix.document.uploadtool.application.dto.inf.DocumentDTOInf;

public class UploadToolDocumentDTO implements DocumentDTOInf {

    private String title;
    private String name;
    private Long revision;
    private Map<String, String> documentProperties;
    private Long stateId;
    private UploadToolDocumentStatus status;
    private String description;
    private String author;
    private String issuer;
    private String issuerId;
    private String authorId;
    private Date issueDate;
    private String notes;
    private String altDocId;
    private String family;
    private String type;

    private Boolean protectInWork = Boolean.FALSE;

    public UploadToolDocumentDTO() {
        documentProperties = new HashMap<String, String>();
    }

    public void addDocumentProperty(String property, String value) {
        documentProperties.put(property, value);
    }

    public Map<String, String> getDocumentProperties() {
        return documentProperties;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public UploadToolDocumentStatus getStatus() {
        return status;
    }

    public void setStatus(UploadToolDocumentStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAltDocId() {
        return altDocId;
    }

    public void setAltDocId(String altDocId) {
        this.altDocId = altDocId;
    }

    public Boolean getProtectInWork() {
        return protectInWork;
    }

    public void setProtectInWork(Boolean protectInWork) {
        this.protectInWork = protectInWork;
    }

    public void setDocumentProperties(Map<String, String> documentProperties) {
        this.documentProperties = documentProperties;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

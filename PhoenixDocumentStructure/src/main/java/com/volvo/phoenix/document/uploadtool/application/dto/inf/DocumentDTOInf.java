package com.volvo.phoenix.document.uploadtool.application.dto.inf;

import java.util.Date;
import java.util.Map;

import com.volvo.phoenix.document.uploadtool.model.UploadToolDocumentStatus;

public interface DocumentDTOInf {

    Map<String, String> getDocumentProperties();

    String getTitle();

    String getName();

    Long getRevision();

    Long getStateId();

    UploadToolDocumentStatus getStatus();

    String getDescription();

    String getAuthor();

    String getIssuer();

    String getIssuerId();

    String getAuthorId();

    Date getIssueDate();

    String getNotes();

    String getAltDocId();

    Boolean getProtectInWork();

    String getFamily();

    String getType();
}

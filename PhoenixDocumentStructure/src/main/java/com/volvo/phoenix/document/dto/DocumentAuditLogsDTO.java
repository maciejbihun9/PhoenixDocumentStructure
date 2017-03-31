package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Container for DocumentAuditLogDTO objects.
 */
public class DocumentAuditLogsDTO implements Serializable {
    private static final long serialVersionUID = -2321099834070716764L;

    private List<DocumentAuditLogDTO> documentAuditLogs = Lists.newArrayList();
    private long totalCount;

    public DocumentAuditLogsDTO() {
    }

    public DocumentAuditLogsDTO(List<DocumentAuditLogDTO> documentAuditLogs, long totalCount) {
        super();
        this.documentAuditLogs = documentAuditLogs;
        this.totalCount = totalCount;
    }

    public List<DocumentAuditLogDTO> getDocumentAuditLogs() {
        return documentAuditLogs;
    }

    public void setDocumentAuditLogs(List<DocumentAuditLogDTO> documentAuditLogs) {
        this.documentAuditLogs = documentAuditLogs;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}

package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Container for FolderAuditLogDTO objects.
 */
public class FolderAuditLogsDTO implements Serializable {
    private static final long serialVersionUID = -2321099834070716764L;

    private List<FolderAuditLogDTO> folderAuditLogs = Lists.newArrayList();
    private long totalCount;

    public FolderAuditLogsDTO() {
    }

    public FolderAuditLogsDTO(List<FolderAuditLogDTO> folderAuditLogs, long totalCount) {
        super();
        this.folderAuditLogs = folderAuditLogs;
        this.totalCount = totalCount;
    }

    public List<FolderAuditLogDTO> getFolderAuditLogs() {
        return folderAuditLogs;
    }

    public void setDocumentAuditLogs(List<FolderAuditLogDTO> folderAuditLogs) {
        this.folderAuditLogs = folderAuditLogs;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}

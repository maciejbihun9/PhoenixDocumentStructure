package com.volvo.phoenix.document.service;

import java.util.List;

import com.volvo.phoenix.document.dto.AuditLogDTO;
import com.volvo.phoenix.document.dto.DocumentAuditLogDTO;
import com.volvo.phoenix.document.dto.DocumentAuditLogsDTO;
import com.volvo.phoenix.document.dto.FolderAuditLogsDTO;

/**
 * Exposes operations for audit taril.
 * 
 * @author bpld313
 *
 */
public interface AuditLogService {

    /**
     * Get <code>howMany</code> document audit logs starting from <code>startInx</code> for operations done by user identified by <code>userVcnId</code>.
     * 
     * @param userVcnId
     *            user VCN ID
     * @param page
     *            page number that should be included in return list, where howMany defines page size
     * @param howMany
     *            amount of entries that should be returned
     * 
     * @return container of list of audit logs for documents operations
     */
    DocumentAuditLogsDTO getDocumentAuditLogs(final String userVcnId, final int page, final int howMany);

    /**
     * Get <code>howMany</code> folder audit logs starting from <code>startInx</code> for operations done by user identified by <code>userVcnId</code>.
     * 
     * @param userVcnId
     *            user VCN ID
     * @param page
     *            page number that should be included in return list, where howMany defines page size
     * @param howMany
     *            amount of entries that should be returned
     * 
     * @return container of list of audit logs for folder operations
     */
    FolderAuditLogsDTO getFolderAuditLogs(final String userVcnId, final int page, final int howMany);

    /**
     * Store audit logs.
     * 
     * @param auditLogs
     *            list of audit log entries
     */
    void storeAuditLogs(final List<AuditLogDTO> auditLogs);

    /**
     * Get single audit log entry for given id.
     * 
     * @param auditLogId
     * @return audit log
     */
    DocumentAuditLogDTO getDocumentAuditLog(final Long auditLogId);

    /**
     * Removes from persistent storage indicated document audit log.
     * 
     * @param auditLogId
     *            set of audit log IDs to be removed
     */
    void removeDocumentAuditLog(final Long auditLogId);

    /**
     * Removes from persistent storage indicated folder audit log.
     * 
     * @param auditLogId
     *            set of audit log IDs to be removed
     */
    void removeFolderAuditLog(final Long auditLogId);
}

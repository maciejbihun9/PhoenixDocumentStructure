package com.volvo.phoenix.document.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.AuditLogDTO;
import com.volvo.phoenix.document.dto.DocumentAuditLogDTO;
import com.volvo.phoenix.document.dto.DocumentAuditLogsDTO;
import com.volvo.phoenix.document.dto.FolderAuditLogDTO;
import com.volvo.phoenix.document.dto.FolderAuditLogsDTO;
import com.volvo.phoenix.document.entity.DocumentAuditLog;
import com.volvo.phoenix.document.entity.FolderAuditLog;
import com.volvo.phoenix.document.repository.DocumentAuditLogRepository;
import com.volvo.phoenix.document.repository.FolderAuditLogRepository;
import com.volvo.phoenix.document.service.AuditLogService;
import com.volvo.phoenix.document.translator.DocumentAuditLogTranslator;
import com.volvo.phoenix.document.translator.FolderAuditLogTranslator;

/**
 * Implementation of {@link AuditLogService}.
 *
 * @author bpld313
 *
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private DocumentAuditLogRepository documentAuditLogRepository;

    @Autowired
    private FolderAuditLogRepository folderAuditLogRepository;

    @Autowired
    private DocumentAuditLogTranslator documentAuditLogTranslator;

    @Autowired
    private FolderAuditLogTranslator folderAuditLogTranslator;

    /**
     * {@inheritDoc}.
     */
    @Override
    public DocumentAuditLogsDTO getDocumentAuditLogs(final String userVcnId, final int page, final int howMany) {
        final Page<DocumentAuditLog> pagedResult = documentAuditLogRepository.findByUserVcnIdOrderByWhenDesc(userVcnId, populatePageRequest(page, howMany));
        return new DocumentAuditLogsDTO(this.documentAuditLogTranslator.translateToDto(pagedResult.getContent()), pagedResult.getTotalElements());
    }

    private PageRequest populatePageRequest(final int page, final int howMany) {
        return new PageRequest(page, howMany);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public FolderAuditLogsDTO getFolderAuditLogs(final String userVcnId, final int page, final int howMany) {
        final Page<FolderAuditLog> pagedResult = this.folderAuditLogRepository.findByUserVcnIdOrderByWhenDesc(userVcnId, populatePageRequest(page, howMany));
        return new FolderAuditLogsDTO(this.folderAuditLogTranslator.translateToDto(pagedResult.getContent()), pagedResult.getTotalElements());
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    @Transactional
    public void storeAuditLogs(final List<AuditLogDTO> auditLogs) {

        final List<DocumentAuditLog> docAuditLogs = Lists.newArrayList();
        final List<FolderAuditLog> folderAuditLogs = Lists.newArrayList();

        for (final AuditLogDTO dto : auditLogs) {
            if (dto instanceof DocumentAuditLogDTO) {
                docAuditLogs.add(this.documentAuditLogTranslator.translateToEntity((DocumentAuditLogDTO) dto));
            } else if (dto instanceof FolderAuditLogDTO) {
                folderAuditLogs.add(this.folderAuditLogTranslator.translateToEntity((FolderAuditLogDTO) dto));
            }
        }

        this.documentAuditLogRepository.save(docAuditLogs);
        this.folderAuditLogRepository.save(folderAuditLogs);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public DocumentAuditLogDTO getDocumentAuditLog(final Long auditLogId) {
        final DocumentAuditLog auditLog = this.documentAuditLogRepository.findOne(auditLogId);
        return this.documentAuditLogTranslator.translateToDto(auditLog);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void removeDocumentAuditLog(final Long auditLogId) {

        Assert.notNull(auditLogId, "The parameter 'auditLogId' cannot be null.");
        this.documentAuditLogRepository.delete(auditLogId);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void removeFolderAuditLog(final Long auditLogId) {

        Assert.notNull(auditLogId, "The parameter 'auditLogId' cannot be null.");
        this.folderAuditLogRepository.delete(auditLogId);
    }
}

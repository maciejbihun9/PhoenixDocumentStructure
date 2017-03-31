package com.volvo.phoenix.document.repository;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.datatype.OperationType;
import com.volvo.phoenix.document.entity.DocumentAuditLog;
import com.volvo.phoenix.document.entity.FolderAuditLog;

import static org.fest.assertions.Assertions.assertThat;


public class FolderAuditLogRepositoryIntegrationTest extends AbstractTransactionalTestCase {

    @Inject
    private FolderAuditLogRepository folderAuditLogRepository;
    @Inject
    private DocumentAuditLogRepository documentAuditLogRepository;

    @Test
    public void shouldCreateAndPersistFolderAuditLog() {
        // given
        FolderAuditLog folderAuditLog = new FolderAuditLog(new Date(), "vcnId", "Dummy User", "/from/path/folder", "/to/path/folder", OperationType.COPY);

        // when
        FolderAuditLog savedFolderAuditLog = folderAuditLogRepository.save(folderAuditLog);

        FolderAuditLog fal = folderAuditLogRepository.findOne(savedFolderAuditLog.getId());
        System.out.println("folder audit log: " + fal);



        // then
        assertThat(folderAuditLogRepository.exists(savedFolderAuditLog.getId())).isTrue();
        assertThat(fal).isNotNull();
        assertThat(fal.getWhen()).isEqualTo(folderAuditLog.getWhen());
        assertThat(fal.getUserVcnId()).isEqualTo(folderAuditLog.getUserVcnId());
        assertThat(fal.getUserFullName()).isEqualTo(folderAuditLog.getUserFullName());
        assertThat(fal.getSourcePath()).isEqualTo(folderAuditLog.getSourcePath());
        assertThat(fal.getTargetPath()).isEqualTo(folderAuditLog.getTargetPath());
        assertThat(fal.getOperationType()).isEqualTo(folderAuditLog.getOperationType());
    }

    @Test
    public void shouldCreateAndPersistDocAuditLog() {
        // given
        DocumentAuditLog docAuditLog = prepareDocumentAuditLog();

        // when
        DocumentAuditLog savedDocAuditLog = documentAuditLogRepository.save(docAuditLog);
        DocumentAuditLog dal = documentAuditLogRepository.findOne(savedDocAuditLog.getId());
        System.out.println("document audit log: " + dal);

        // then
        assertThat(documentAuditLogRepository.exists(savedDocAuditLog.getId())).isTrue();
        assertThat(dal).isNotNull();
        assertThat(dal.getWhen()).isEqualTo(docAuditLog.getWhen());
        assertThat(dal.getUserVcnId()).isEqualTo(docAuditLog.getUserVcnId());
        assertThat(dal.getUserFullName()).isEqualTo(docAuditLog.getUserFullName());
        assertThat(dal.getSourcePath()).isEqualTo(docAuditLog.getSourcePath());
        assertThat(dal.getTargetPath()).isEqualTo(docAuditLog.getTargetPath());
        assertThat(dal.getOperationType()).isEqualTo(docAuditLog.getOperationType());

        assertThat(dal.getSourceDocType()).isEqualTo(docAuditLog.getSourceDocType());
        assertThat(dal.getTargetDocType()).isEqualTo(docAuditLog.getTargetDocType());
        assertThat(dal.getSourceDomain()).isEqualTo(docAuditLog.getSourceDomain());
        assertThat(dal.getTargetDomain()).isEqualTo(docAuditLog.getTargetDomain());
        assertThat(dal.getSourceMandatoryAttributes()).isEqualTo(docAuditLog.getSourceMandatoryAttributes());
        assertThat(dal.getTargetMandatoryAttributes()).isEqualTo(docAuditLog.getTargetMandatoryAttributes());
        assertThat(dal.getSourceOptionalAttributes()).isEqualTo(docAuditLog.getSourceOptionalAttributes());
        assertThat(dal.getTargetOptionalAttributes()).isEqualTo(docAuditLog.getTargetOptionalAttributes());
    }

    private DocumentAuditLog prepareDocumentAuditLog() {
        DocumentAuditLog docAuditLog = new DocumentAuditLog(new Date(), "vcnId", "Dummy User", "/from/path/folder", "to/path/folder", OperationType.MOVE);
        docAuditLog.setSourceDocType("from doc TYPE");
        docAuditLog.setTargetDocType("to doc TYPE");
        docAuditLog.setSourceDomain("from domain");
        docAuditLog.setTargetDomain("to domain");
        docAuditLog.setSourceMandatoryAttributes("from mandatory attr1=val1");
        docAuditLog.setTargetMandatoryAttributes("to mandatory attr1=val2");
        docAuditLog.setSourceOptionalAttributes("from optional attr1=val1");
        docAuditLog.setTargetOptionalAttributes("to optional attr1=val2");
        return docAuditLog;
    }

    @Test
    public void shouldFindDocumentAuditLogByUserVcnId() {
        // given
        String userVcnId = "vcnId";
        Pageable pageable = new PageRequest(0, 10);
        DocumentAuditLog docAuditLog = prepareDocumentAuditLog();
        documentAuditLogRepository.save(docAuditLog);

        // when
        Page<DocumentAuditLog> auditLogs = documentAuditLogRepository.findByUserVcnIdOrderByWhenDesc(userVcnId, pageable);

        // then
        assertThat(auditLogs).isNotNull();
        assertThat(auditLogs.getContent()).isNotEmpty();
    }

    @Test
    public void shouldGetPagedFolderAuditLogsResult() {
        // given
        int page = 2;
        int howMany = 2;
        int numberOfAllLogs = 15;
        String userVcnId = "Summy User";
        prepareTestFolderAuditLogs(numberOfAllLogs, userVcnId);
        PageRequest pageRequest = new PageRequest(page, howMany);

        // when
        Page<FolderAuditLog> logs = folderAuditLogRepository.findByUserVcnIdOrderByWhenDesc(userVcnId, pageRequest);

        // then
        assertThat(logs.getNumber()).isEqualTo(howMany);
        assertThat(logs.getContent().size()).isEqualTo(howMany);
        assertThat(logs.getTotalElements()).isEqualTo(numberOfAllLogs);
    }

    private void prepareTestFolderAuditLogs(int numberOfAllLogs, String userVcnId) {

        for (int i = 0; i < numberOfAllLogs; i++) {
            FolderAuditLog log = new FolderAuditLog(new Date(), userVcnId, "Dummy Dummy " + i, "source" + i, "target" + i, OperationType.COPY);
            folderAuditLogRepository.save(log);
        }

    }
}

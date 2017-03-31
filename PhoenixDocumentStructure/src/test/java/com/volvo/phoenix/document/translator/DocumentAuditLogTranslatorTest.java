package com.volvo.phoenix.document.translator;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.datatype.OperationType;
import com.volvo.phoenix.document.dto.DocumentAttributeAuditLogDTO;
import com.volvo.phoenix.document.dto.DocumentAuditLogDTO;
import com.volvo.phoenix.document.dto.FolderAuditLogDTO;
import com.volvo.phoenix.document.entity.DocumentAuditLog;
import com.volvo.phoenix.document.entity.FolderAuditLog;

import static org.fest.assertions.Assertions.assertThat;

public class DocumentAuditLogTranslatorTest {

    private FolderAuditLogTranslator folderAuditLogTranslator = new FolderAuditLogTranslator();
    private DocumentAuditLogTranslator documentAuditLogTranslator = new DocumentAuditLogTranslator();

    @Test
    public void shouldTranslateFolderAuditLogEntity2DTO() {
        // given
        Date date = new Date();
        String userVcnId = "userVcnId";
        String userFullName = "Joe Doe";
        String sourcePath = "/source";
        String targetPath = "/target";
        
        FolderAuditLog entity = new FolderAuditLog(date, userVcnId, userFullName, sourcePath, targetPath, OperationType.COPY);
        
        // when
        FolderAuditLogDTO auditLogDTO = folderAuditLogTranslator.translateToDto(entity);
        
        // then
        assertThat(auditLogDTO.getWhen()).isEqualTo(date);
        assertThat(auditLogDTO.getUserVcnId()).isEqualTo(userVcnId);
        assertThat(auditLogDTO.getUserFullName()).isEqualTo(userFullName);
        assertThat(auditLogDTO.getSourcePath()).isEqualTo(sourcePath);
        assertThat(auditLogDTO.getTargetPath()).isEqualTo(targetPath);
    }

    @Test
    public void shouldTranslateFolderAuditLogDTO2Entity() {
        // given
        Date date = new Date();
        String userVcnId = "userVcnId";
        String userFullName = "Joe Doe";
        String sourcePath = "/source";
        String targetPath = "/target";

        FolderAuditLogDTO dto = new FolderAuditLogDTO(date, userVcnId, userFullName, sourcePath, targetPath, OperationType.COPY);

        // when
        FolderAuditLog auditLog = folderAuditLogTranslator.translateToEntity(dto);

        // then
        assertThat(auditLog.getWhen()).isEqualTo(date);
        assertThat(auditLog.getUserVcnId()).isEqualTo(userVcnId);
        assertThat(auditLog.getUserFullName()).isEqualTo(userFullName);
        assertThat(auditLog.getSourcePath()).isEqualTo(sourcePath);
        assertThat(auditLog.getTargetPath()).isEqualTo(targetPath);
    }

    @Test
    public void shouldTranslateDocumentAuditLogEntity2DTO() {
        // given
        Date date = new Date();
        String userVcnId = "userVcnId";
        String userFullName = "Joe Doe";
        String sourcePath = "/source";
        String targetPath = "/target";

        String sourceDocType = "sourceDocType";
        String sourceDomain = "sourceDomain";
        String sourceMandatoryAttributes = "sma1" + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v1" + DocumentAuditLog.ATTRIBUTES_SEPARATOR + "sma2"
                + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v2";
        String sourceOptionalAttributes = "soa1" + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v1" + DocumentAuditLog.ATTRIBUTES_SEPARATOR + "soa2"
                + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v2";

        String targetDocType = "targetDocType";
        String targetDomain = "targetDomain";
        String targetMandatoryAttributes = "tma1" + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v1" + DocumentAuditLog.ATTRIBUTES_SEPARATOR + "tma2"
                + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v2";
        String targetOptionalAttributes = "toa1" + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v1" + DocumentAuditLog.ATTRIBUTES_SEPARATOR + "toa2"
                + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v2";

        DocumentAuditLog entity = new DocumentAuditLog(date, userVcnId, userFullName, sourcePath, targetPath, OperationType.COPY);
        entity.setSourceDocType(sourceDocType);
        entity.setSourceDomain(sourceDomain);
        entity.setSourceMandatoryAttributes(sourceMandatoryAttributes);
        entity.setSourceOptionalAttributes(sourceOptionalAttributes);

        entity.setTargetDocType(targetDocType);
        entity.setTargetDomain(targetDomain);
        entity.setTargetMandatoryAttributes(targetMandatoryAttributes);
        entity.setTargetOptionalAttributes(targetOptionalAttributes);

        List<DocumentAttributeAuditLogDTO> sourceMandatoryAttributesDtos = Lists.newArrayList(new DocumentAttributeAuditLogDTO("sma1", "v1"),
                new DocumentAttributeAuditLogDTO("sma2", "v2"));
        List<DocumentAttributeAuditLogDTO> sourceOptionalAttributesDtos = Lists.newArrayList(new DocumentAttributeAuditLogDTO("soa1", "v1"),
                new DocumentAttributeAuditLogDTO("soa2", "v2"));
        List<DocumentAttributeAuditLogDTO> targetMandatoryAttributesDtos = Lists.newArrayList(new DocumentAttributeAuditLogDTO("tma1", "v1"),
                new DocumentAttributeAuditLogDTO("tma2", "v2"));
        List<DocumentAttributeAuditLogDTO> targetOptionalAttributesDtos = Lists.newArrayList(new DocumentAttributeAuditLogDTO("toa1", "v1"),
                new DocumentAttributeAuditLogDTO("toa2", "v2"));

        // when
        DocumentAuditLogDTO auditLogDTO = documentAuditLogTranslator.translateToDto(entity);

        // then
        assertThat(auditLogDTO.getWhen()).isEqualTo(date);
        assertThat(auditLogDTO.getUserVcnId()).isEqualTo(userVcnId);
        assertThat(auditLogDTO.getUserFullName()).isEqualTo(userFullName);
        assertThat(auditLogDTO.getSourcePath()).isEqualTo(sourcePath);
        assertThat(auditLogDTO.getTargetPath()).isEqualTo(targetPath);

        assertThat(auditLogDTO.getSourceDocType()).isEqualTo(sourceDocType);
        assertThat(auditLogDTO.getSourceDomain()).isEqualTo(sourceDomain);
        assertThat(auditLogDTO.getSourceMandatoryAttributes()).isEqualTo(sourceMandatoryAttributesDtos);
        assertThat(auditLogDTO.getSourceOptionalAttributes()).isEqualTo(sourceOptionalAttributesDtos);

        assertThat(auditLogDTO.getTargetDocType()).isEqualTo(targetDocType);
        assertThat(auditLogDTO.getTargetDomain()).isEqualTo(targetDomain);
        assertThat(auditLogDTO.getTargetMandatoryAttributes()).isEqualTo(targetMandatoryAttributesDtos);
        assertThat(auditLogDTO.getTargetOptionalAttributes()).isEqualTo(targetOptionalAttributesDtos);
    }

    @Test
    public void shouldTranslateDocumentAuditLogDTO2Entity() {
        // given
        Date date = new Date();
        String userVcnId = "userVcnId";
        String userFullName = "Joe Doe";
        String sourcePath = "/source";
        String targetPath = "/target";

        String sourceDocType = "sourceDocType";
        String sourceDomain = "sourceDomain";
        String sourceMandatoryAttributes = "sma1" + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v1" + DocumentAuditLog.ATTRIBUTES_SEPARATOR + "sma2"
                + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v2";
        String sourceOptionalAttributes = "soa1" + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v1" + DocumentAuditLog.ATTRIBUTES_SEPARATOR + "soa2"
                + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v2";

        String targetDocType = "targetDocType";
        String targetDomain = "targetDomain";
        String targetMandatoryAttributes = "tma1" + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v1" + DocumentAuditLog.ATTRIBUTES_SEPARATOR + "tma2"
                + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v2";
        String targetOptionalAttributes = "toa1" + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v1" + DocumentAuditLog.ATTRIBUTES_SEPARATOR + "toa2"
                + DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR + "v2";

        List<DocumentAttributeAuditLogDTO> sourceMandatoryAttributesDtos = Lists.newArrayList(new DocumentAttributeAuditLogDTO("sma1", "v1"),
                new DocumentAttributeAuditLogDTO("sma2", "v2"));
        List<DocumentAttributeAuditLogDTO> sourceOptionalAttributesDtos = Lists.newArrayList(new DocumentAttributeAuditLogDTO("soa1", "v1"),
                new DocumentAttributeAuditLogDTO("soa2", "v2"));
        List<DocumentAttributeAuditLogDTO> targetMandatoryAttributesDtos = Lists.newArrayList(new DocumentAttributeAuditLogDTO("tma1", "v1"),
                new DocumentAttributeAuditLogDTO("tma2", "v2"));
        List<DocumentAttributeAuditLogDTO> targetOptionalAttributesDtos = Lists.newArrayList(new DocumentAttributeAuditLogDTO("toa1", "v1"),
                new DocumentAttributeAuditLogDTO("toa2", "v2"));
        
        
        DocumentAuditLogDTO dto = new DocumentAuditLogDTO(date, userVcnId, userFullName, sourcePath, targetPath, OperationType.COPY);
        dto.setSourceDocType(sourceDocType);
        dto.setSourceDomain(sourceDomain);
        dto.setSourceMandatoryAttributes(sourceMandatoryAttributesDtos);
        dto.setSourceOptionalAttributes(sourceOptionalAttributesDtos);

        dto.setTargetDocType(targetDocType);
        dto.setTargetDomain(targetDomain);
        dto.setTargetMandatoryAttributes(targetMandatoryAttributesDtos);
        dto.setTargetOptionalAttributes(targetOptionalAttributesDtos);


        // when
        DocumentAuditLog auditLog = documentAuditLogTranslator.translateToEntity(dto);

        // then
        assertThat(auditLog.getWhen()).isEqualTo(date);
        assertThat(auditLog.getUserVcnId()).isEqualTo(userVcnId);
        assertThat(auditLog.getUserFullName()).isEqualTo(userFullName);
        assertThat(auditLog.getSourcePath()).isEqualTo(sourcePath);
        assertThat(auditLog.getTargetPath()).isEqualTo(targetPath);

        assertThat(auditLog.getSourceDocType()).isEqualTo(sourceDocType);
        assertThat(auditLog.getSourceDomain()).isEqualTo(sourceDomain);
        assertThat(auditLog.getSourceMandatoryAttributes()).isEqualTo(sourceMandatoryAttributes);
        assertThat(auditLog.getSourceOptionalAttributes()).isEqualTo(sourceOptionalAttributes);

        assertThat(auditLog.getTargetDocType()).isEqualTo(targetDocType);
        assertThat(auditLog.getTargetDomain()).isEqualTo(targetDomain);
        assertThat(auditLog.getTargetMandatoryAttributes()).isEqualTo(targetMandatoryAttributes);
        assertThat(auditLog.getTargetOptionalAttributes()).isEqualTo(targetOptionalAttributes);
    }

}

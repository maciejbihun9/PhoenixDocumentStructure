package com.volvo.phoenix.document.dto;

import java.util.Date;
import java.util.List;

import com.volvo.phoenix.document.datatype.OperationType;


/**
 * Counterpart for {@link com.volvo.phoenix.document.entity.DocumentAuditLog}.
 * 
 * @author bpld313
 *
 */
public class DocumentAuditLogDTO extends FolderAuditLogDTO {
    private static final long serialVersionUID = 6477751314986436532L;

    private String sourceDomain;
    private String sourceDocType;
    private List<DocumentAttributeAuditLogDTO> sourceMandatoryAttributes;
    private List<DocumentAttributeAuditLogDTO> sourceOptionalAttributes;
    private String targetDomain;
    private String targetDocType;
    private List<DocumentAttributeAuditLogDTO> targetMandatoryAttributes;
    private List<DocumentAttributeAuditLogDTO> targetOptionalAttributes;
    
    public DocumentAuditLogDTO() {}
    
    public DocumentAuditLogDTO(Date when, String userVcnId, String userFullName, String sourcePath, String targetPath, OperationType operationType) {
        super(when, userVcnId, userFullName, sourcePath, targetPath, operationType);
    }

    public String getSourceDomain() {
        return sourceDomain;
    }

    public void setSourceDomain(String fromDomain) {
        this.sourceDomain = fromDomain;
    }

    public String getSourceDocType() {
        return sourceDocType;
    }

    public void setSourceDocType(String fromDocType) {
        this.sourceDocType = fromDocType;
    }

    public String getTargetDomain() {
        return targetDomain;
    }

    public void setTargetDomain(String toDomain) {
        this.targetDomain = toDomain;
    }

    public String getTargetDocType() {
        return targetDocType;
    }

    public void setTargetDocType(String toDocType) {
        this.targetDocType = toDocType;
    }

    public List<DocumentAttributeAuditLogDTO> getSourceMandatoryAttributes() {
        return sourceMandatoryAttributes;
    }

    public void setSourceMandatoryAttributes(List<DocumentAttributeAuditLogDTO> sourceMandatoryAttributes) {
        this.sourceMandatoryAttributes = sourceMandatoryAttributes;
    }

    public List<DocumentAttributeAuditLogDTO> getSourceOptionalAttributes() {
        return sourceOptionalAttributes;
    }

    public void setSourceOptionalAttributes(List<DocumentAttributeAuditLogDTO> sourceOptionalAttributes) {
        this.sourceOptionalAttributes = sourceOptionalAttributes;
    }

    public List<DocumentAttributeAuditLogDTO> getTargetMandatoryAttributes() {
        return targetMandatoryAttributes;
    }

    public void setTargetMandatoryAttributes(List<DocumentAttributeAuditLogDTO> targetMandatoryAttributes) {
        this.targetMandatoryAttributes = targetMandatoryAttributes;
    }

    public List<DocumentAttributeAuditLogDTO> getTargetOptionalAttributes() {
        return targetOptionalAttributes;
    }

    public void setTargetOptionalAttributes(List<DocumentAttributeAuditLogDTO> targetOptionalAttributes) {
        this.targetOptionalAttributes = targetOptionalAttributes;
    }


}

package com.volvo.phoenix.document.dto;

import java.util.Date;

import com.volvo.phoenix.document.datatype.OperationType;


/**
 * Counterpart for {@link com.volvo.phoenix.document.entity.FolderAuditLog}.
 * 
 * @author bpld313
 *
 */
public class FolderAuditLogDTO extends AuditLogDTO {

    private static final long serialVersionUID = 2291631079368892126L;
    
    private String sourcePath;
    private String targetPath;
    private OperationType operationType;

    public FolderAuditLogDTO() {
    }

    public FolderAuditLogDTO(Date when, String userVcnId, String userFullName, String sourcePath, String targetPath, OperationType operationType) {
        super(when, userVcnId, userFullName);
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.operationType = operationType;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String fromPath) {
        this.sourcePath = fromPath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String toPath) {
        this.targetPath = toPath;
    }

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

}

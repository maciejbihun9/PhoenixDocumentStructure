package com.volvo.phoenix.document.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.volvo.phoenix.document.datatype.OperationType;

/**
 * Represents single entry in audit log for history of folder operations.
 * 
 * @author bpld313
 *
 */
@Entity
public class FolderAuditLog extends AuditLog {

    private static final long serialVersionUID = 961978804839817842L;

    @Override
    public String toString() {
        return "FolderAuditLog [getSourcePath()=" + getSourcePath() + ", getTargetPath()=" + getTargetPath() + ", getOperationType()=" + getOperationType()
                + "]";
    }

    public FolderAuditLog() {
    }

    public FolderAuditLog(Date when, String whoVcnId, String whoFullName, String sourcePath, String targetPath, OperationType operationType) {
        super(when, whoVcnId, whoFullName, sourcePath, targetPath, operationType);
    }

}

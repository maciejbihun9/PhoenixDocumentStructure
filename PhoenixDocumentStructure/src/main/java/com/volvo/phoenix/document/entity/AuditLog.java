package com.volvo.phoenix.document.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.volvo.phoenix.document.datatype.OperationType;

/**
 * Represents single entry in audit log for history of changes. This is base class for more concrete classes representing audit log entries.
 *
 * @author bpld313
 *
 */
@Entity
@Table(name = "CM_AUDIT_LOG", schema = EntityMapping.CM_SCHEMA)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "AUDIT_LOG_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(sequenceName = "CM_AUDIT_LOG_SEQ", name = "CM_AUDIT_LOG_SEQ", schema = EntityMapping.CM_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CM_AUDIT_LOG_SEQ")
    @Column(name = "id")
    private Long id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date when;

    @Column(name = "user_vcn_id")
    private String userVcnId;

    @Column(name = "user_full_name")
    private String userFullName;

    @Column(name = "from_path")
    private String sourcePath;

    @Column(name = "to_path")
    private String targetPath;

    @Column(name = "op_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    public AuditLog() {
    }

    public AuditLog(Date when, String whoVcnId, String whoFullName, String sourcePath, String targetPath, OperationType operationType) {
        this.when = when;
        this.userVcnId = whoVcnId;
        this.userFullName = whoFullName;
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return "AuditLog [id=" + id + ", when=" + when + ", userVcnId=" + userVcnId + ", userFullName=" + userFullName + ", sourcePath=" + sourcePath
                + ", targetPath=" + targetPath + ", operationType=" + operationType + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((operationType == null) ? 0 : operationType.hashCode());
        result = prime * result + ((sourcePath == null) ? 0 : sourcePath.hashCode());
        result = prime * result + ((targetPath == null) ? 0 : targetPath.hashCode());
        result = prime * result + ((userFullName == null) ? 0 : userFullName.hashCode());
        result = prime * result + ((userVcnId == null) ? 0 : userVcnId.hashCode());
        result = prime * result + ((when == null) ? 0 : when.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AuditLog other = (AuditLog) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (operationType != other.operationType) {
            return false;
        }
        if (sourcePath == null) {
            if (other.sourcePath != null) {
                return false;
            }
        } else if (!sourcePath.equals(other.sourcePath)) {
            return false;
        }
        if (targetPath == null) {
            if (other.targetPath != null) {
                return false;
            }
        } else if (!targetPath.equals(other.targetPath)) {
            return false;
        }
        if (userFullName == null) {
            if (other.userFullName != null) {
                return false;
            }
        } else if (!userFullName.equals(other.userFullName)) {
            return false;
        }
        if (userVcnId == null) {
            if (other.userVcnId != null) {
                return false;
            }
        } else if (!userVcnId.equals(other.userVcnId)) {
            return false;
        }
        if (when == null) {
            if (other.when != null) {
                return false;
            }
        } else if (!when.equals(other.when)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public String getUserVcnId() {
        return userVcnId;
    }

    public void setUserVcnId(String whoVcnId) {
        this.userVcnId = whoVcnId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String whoFullName) {
        this.userFullName = whoFullName;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

}

package com.volvo.phoenix.document.entity;

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
import javax.persistence.Version;

import com.volvo.phoenix.document.datatype.ItemType;
import com.volvo.phoenix.document.datatype.OperationStatus;
import com.volvo.phoenix.document.datatype.OperationType;

@Entity
@Table(name = "CM_OPERATION", schema = EntityMapping.CM_SCHEMA)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Operation {

    @Id
    @SequenceGenerator(sequenceName = "CM_OPERATION_SEQ", name = "CM_OPERATION_SEQ", schema = EntityMapping.CM_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CM_OPERATION_SEQ")
    @Column(name = "ID")
    private Long id;

    @Version
    private Long version;

    @Column(name = "USERNAME")
    private String user;

    @Column(name = "SOURCE_ID")
    private Long sourceId;

    @Column(name = "TARGET_FOLDER_ID")
    private Long targetFolderId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private OperationStatus status;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "OPERATION_TYPE")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getTargetFolderId() {
        return targetFolderId;
    }

    public void setTargetFolderId(Long targetFolderId) {
        this.targetFolderId = targetFolderId;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public abstract ItemType getSourceType();

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

}

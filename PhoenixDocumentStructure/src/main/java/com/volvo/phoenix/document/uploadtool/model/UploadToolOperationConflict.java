package com.volvo.phoenix.document.uploadtool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.volvo.phoenix.document.entity.EntityMapping;

/**
 * Validation conflict Entity
 */
@Entity
@Table(name = "UT_OPERATION_CONFLICT", schema = EntityMapping.PHOENIX_SCHEMA)
public class UploadToolOperationConflict {

    @Id
    @SequenceGenerator(sequenceName = "UT_OPERATION_CONFLICT_SEQ", name = "UT_OPERATION_CONFLICT_SEQ", schema = EntityMapping.PHOENIX_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UT_OPERATION_CONFLICT_SEQ")
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "OPERATION_ID", referencedColumnName = "ID")
    private UploadToolOperation operation;

    @Column(name = "CONFLICT_TYPE")
    @Enumerated(EnumType.STRING)
    private UploadToolConflictType conflict;

    @OneToOne
    @JoinColumn(name = "TREE_NODE_ID", referencedColumnName = "NODE_ID")
    private UploadToolOperationTreeNode treeNode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UploadToolOperation getOperation() {
        return operation;
    }

    public void setOperation(UploadToolOperation operation) {
        this.operation = operation;
    }

    public UploadToolConflictType getConflict() {
        return conflict;
    }

    public void setConflict(UploadToolConflictType conflict) {
        this.conflict = conflict;
    }

    public UploadToolOperationTreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(UploadToolOperationTreeNode treeNode) {
        this.treeNode = treeNode;
    }
}

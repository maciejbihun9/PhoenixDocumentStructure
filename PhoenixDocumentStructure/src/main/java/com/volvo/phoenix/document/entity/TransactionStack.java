package com.volvo.phoenix.document.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.volvo.phoenix.document.datatype.TransactionStackEventType;
import com.volvo.phoenix.document.datatype.TransactionStackSystemType;

/**
 * Transaction Stack ( document change log for indexing in ESE and Solr) record
 */
@Entity
@Table(name = "VT_PHOENIX_CHANGE_LOG", schema = EntityMapping.PHOENIX_SCHEMA)
public class TransactionStack {

    @EmbeddedId
    private TransactionStackPK id;

    @Column(name = "TRANSACTION_TYPE")
    @Enumerated(EnumType.STRING)
    private TransactionStackEventType transactionType;

    @Column(name = "SYSTEM_TYPE")
    @Enumerated(EnumType.STRING)
    private TransactionStackSystemType systemType;


    public TransactionStackPK getId() {
        return id;
    }

    public void setId(TransactionStackPK id) {
        this.id = id;
    }
    
    public Long getDocumentId() {
        return (id != null) ? id.getDocumentId() : null;
    }

    public void setDocumentId(Long documentId) {
        if (id == null) {
            id = new TransactionStackPK();
        }
        id.setDocumentId(documentId);
    }

    public Date getEventDate() {
        return (id != null) ? id.getEventDate() : null;
    }

    public void setEventDate(Date eventDate) {
        if (id == null) {
            id = new TransactionStackPK();
        }
        id.setEventDate(eventDate);
    }

    public TransactionStackEventType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionStackEventType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStackSystemType getSystemType() {
        return systemType;
    }

    public void setSystemType(TransactionStackSystemType systemType) {
        this.systemType = systemType;
    }
    
}

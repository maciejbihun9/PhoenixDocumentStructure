package com.volvo.phoenix.document.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VT_PHOENIX_DOC_STATUS", schema = EntityMapping.PHOENIX_SCHEMA)
public class DocumentStatus {
    
    @Id
    @Column(name = "STATUS")
    private String status;
    @Column(name = "STATUS_DESCRIPTION")
    private String description;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
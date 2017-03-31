package com.volvo.phoenix.document.uploadtool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.volvo.phoenix.document.entity.EntityMapping;

/**
 * Validated Upload Tool document's dynamic attribute (mandatory and optional) value Entity
 */
@Entity
@Table(name = "UT_DOCUMENT_ATTRIBUTE_VALUE", schema = EntityMapping.PHOENIX_SCHEMA)
public class UploadToolDocumentAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private Long id;
    
    @Column(name="ATTRIBUTE_ID")
    private Long attribute;
    
    @Column(name = "VALUE")
    private String value;

    @ManyToOne
    @JoinColumn(name = "DOCUMENT_ID", referencedColumnName = "ID")
    private UploadToolDocument owningDocument;
    
    
    public String getValue() {
        return value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAttribute() {
        return attribute;
    }

    public void setAttribute(Long attribute) {
        this.attribute = attribute;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UploadToolDocument getOwningDocument() {
        return owningDocument;
    }

    public void setOwningDocument(UploadToolDocument owningDocument) {
        this.owningDocument = owningDocument;
    }
    
}

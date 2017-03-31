package com.volvo.phoenix.document.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "VT_PHOENIX_ATTRIBUTE_VALUE", schema = EntityMapping.PHOENIX_SCHEMA)
public class DocumentAttributeValue {

    @EmbeddedId
    private DocumentAttributePK id;

    @Column(name="OBJ_TYPE")
    private String objectType = "O";
    
    @Column(name="INDEX_ID")
    private Long indexId;
    
    @Column(name = "VALUE")
    private String value;

    @ManyToOne
    @JoinColumn(name = "OBJ_ID", referencedColumnName = "OBJ_ID", insertable = false, updatable = false)
    private Document owningDocument;
    
    public DocumentAttributePK getId() {
        return id;
    }

    public void setId(DocumentAttributePK id) {
        this.id = id;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Long getIndexId() {
        return indexId;
    }

    public void setIndexId(Long indexId) {
        this.indexId = indexId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Document getOwningDocument() {
        return owningDocument;
    }

    public void setOwningDocument(Document owningDocument) {
        this.owningDocument = owningDocument;
    }

}

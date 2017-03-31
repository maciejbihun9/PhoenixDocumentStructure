package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DocumentAttributePK implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 3855071786870513137L;

    @Column(name="OBJ_ID")
    private Long document;

    @Column(name="ATTRIBUTE_ID")
    private Long attribute;


    public Long getDocument() {
        return document;
    }
    public void setDocument(Long document) {
        this.document = document;
    }
    public Long getAttribute() {
        return attribute;
    }
    public void setAttribute(Long attribute) {
        this.attribute = attribute;
    }

    public DocumentAttributePK() {
    }

    public DocumentAttributePK(Long document, Long attribute) {
        this.document = document;
        this.attribute = attribute;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return document.hashCode()*31+attribute.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DocumentAttributePK) {
            DocumentAttributePK pk = (DocumentAttributePK)obj;
            return document.equals(pk.getDocument()) && attribute.equals(pk.getAttribute());
        }
        return false;
    }
}

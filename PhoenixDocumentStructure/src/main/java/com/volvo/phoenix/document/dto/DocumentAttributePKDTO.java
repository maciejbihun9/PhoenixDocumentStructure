package com.volvo.phoenix.document.dto;

import java.io.Serializable;

public class DocumentAttributePKDTO implements Serializable {
    private static final long serialVersionUID = 4980145024017225261L;

    private Long documentId;

    private Long attributeId;

    public DocumentAttributePKDTO() {
    }

    public DocumentAttributePKDTO(Long documentId, Long attributeId) {
        super();
        this.documentId = documentId;
        this.attributeId = attributeId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

}

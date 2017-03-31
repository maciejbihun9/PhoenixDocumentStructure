package com.volvo.phoenix.document.entity;

import javax.persistence.Entity;

import com.volvo.phoenix.document.datatype.ItemType;

/**
 * Represents operation on document.
 */
@Entity
public class DocumentOperation extends Operation {

    public Long getSourceDocumenId() {
        return getSourceId();
    }

    public void setSourceDocumenId(Long sourceDocumenId) {
        setSourceId(sourceDocumenId);
    }

    @Override
    public ItemType getSourceType() {
        return ItemType.DOCUMENT;
    }
}

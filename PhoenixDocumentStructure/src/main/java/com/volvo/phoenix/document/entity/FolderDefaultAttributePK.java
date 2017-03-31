package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FolderDefaultAttributePK implements Serializable{

    private static final long serialVersionUID = 3855071786870513137L;

    @Column(name = "FOLDER_ID")
    private Long folderId;

    @Column(name="ATTRIBUTE_ID")
    private Long attributeId;

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

}

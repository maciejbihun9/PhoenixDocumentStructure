package com.volvo.phoenix.document.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "VT_PHOENIX_DEFAULT_ATTRIBUTE", schema = EntityMapping.PHOENIX_SCHEMA)
public class FolderDefaultAttribute {

    @EmbeddedId
    private FolderDefaultAttributePK id;

    @Column(name = "VALUE")
    private String value;

    public FolderDefaultAttributePK getId() {
        if (id == null) {
            id = new FolderDefaultAttributePK();
        }
        return id;
    }

    public void setId(FolderDefaultAttributePK id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

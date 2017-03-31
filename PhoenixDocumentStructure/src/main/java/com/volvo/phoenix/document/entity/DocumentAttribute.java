package com.volvo.phoenix.document.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.volvo.phoenix.document.datatype.AttributeType;

@Entity
@Table(name = "VT_PHOENIX_ATTRIBUTE_VALUE", schema = EntityMapping.PHOENIX_SCHEMA)
public class DocumentAttribute {

    @EmbeddedId
    private DocumentAttributePK id;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTRIBUTE_ID", referencedColumnName = "ATTRIBUTE_ID", insertable = false, updatable = false)
    private AttributeDefinition attribute;

    public AttributeDefinition getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeDefinition attribute) {
        this.attribute = attribute;
    }

    public DocumentAttributePK getId() {
        return id;
    }

    public void setId(DocumentAttributePK id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public String getDictionaryValue() {
        // legacy representation of value or FK of dictionary value
        if (AttributeType.SELECT == attribute.getType() && StringUtils.isNotEmpty(value)) {
            for (DictionaryValue dv : attribute.getDictionary().getValues()) {
                if (value.equals(dv.getId().toString())) {
                    return dv.getValue();
                }
            }
        }

        return null;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

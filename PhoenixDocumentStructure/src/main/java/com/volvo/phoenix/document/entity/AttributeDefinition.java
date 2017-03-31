package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.volvo.phoenix.document.datatype.AttributeType;

@Entity
@Table(name = "VT_PHOENIX_ATTRIBUTE", schema = EntityMapping.PHOENIX_SCHEMA)
public class AttributeDefinition implements Serializable {
    private static final long serialVersionUID = -3432962843554692118L;

    @Id
    @Column(name = "ATTRIBUTE_ID")
    private Long id;

    @Column(name = "ATTRIBUTE_LABEL")
    private String label;
    @Column(name = "ATTRIBUTE_NAME")
    private String name;
    @Column(name = "ATTRIBUTE_TYPE")
    @Enumerated(EnumType.STRING)
    private AttributeType type;
    @Column(name = "ATTRIBUTE_DESCRIPTION")
    private String description;

    @ManyToOne(optional = true)
    @JoinColumn(name = "LOV_ID")
    private Dictionary dictionary;

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}

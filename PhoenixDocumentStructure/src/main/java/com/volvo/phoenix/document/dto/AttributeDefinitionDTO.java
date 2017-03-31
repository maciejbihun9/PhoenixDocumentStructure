package com.volvo.phoenix.document.dto;

import java.io.Serializable;

import com.volvo.phoenix.document.datatype.AttributeType;

public class AttributeDefinitionDTO implements Serializable{

    private static final long serialVersionUID = 1321907988921470370L;

    private Long id;
    private String label;
    private String name;
    private AttributeType type;
    private String description;
    private DictionaryDTO dictionary;

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

    public DictionaryDTO getDictionary() {
        return dictionary;
    }

    public void setDictionary(DictionaryDTO dictionary) {
        this.dictionary = dictionary;
    }

}

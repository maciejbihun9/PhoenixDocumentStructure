package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class DictionaryDTO implements Serializable {

    private static final long serialVersionUID = 84221509834750554L;
    private Long id;
    private String name;
    private String description;
    private List<DictionaryValueDTO> values = Lists.newArrayList();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DictionaryValueDTO> getValues() {
        return values;
    }

    public void setValues(List<DictionaryValueDTO> values) {
        this.values = values;
    }


}

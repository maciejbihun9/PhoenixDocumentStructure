package com.volvo.phoenix.document.dto;

import java.io.Serializable;

public class DictionaryValueDTO implements Serializable {
    private static final long serialVersionUID = 3456018329699289477L;

    private Long id;
    private String value;
    private String description;
    private Integer order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }


}

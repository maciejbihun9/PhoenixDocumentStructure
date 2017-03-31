package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VT_PHOENIX_ATTRIBUTE_LOV_VALUE", schema = EntityMapping.PHOENIX_SCHEMA)
public class DictionaryValue implements Serializable {
    private static final long serialVersionUID = 3456018329699289477L;

    @Id
    @Column(name = "LOV_VALUE_ID")
    private Long id;
    @Column(name = "LOV_VALUE")
    private String value;
    @Column(name = "LOV_DESCRIPTION")
    private String description;
    @Column(name = "LOV_ORDER")
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "LOV_ID", referencedColumnName = "LOV_ID")
    private Dictionary dictionary;

    public DictionaryValue(Long id, String value, String description, Integer order) {
        this.id = id;
        this.value = value;
        this.description = description;
        this.order = order;
    }

    public DictionaryValue() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DictionaryValue other = (DictionaryValue) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (order == null) {
            if (other.order != null) {
                return false;
            }
        } else if (!order.equals(other.order)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DictionaryValue [id=" + id + ", value=" + value + ", description=" + description + ", order=" + order + "]";
    }

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

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
        if (!dictionary.getValues().contains(this)) {
            dictionary.addValue(this);
        }
    }
}

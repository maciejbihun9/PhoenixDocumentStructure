package com.volvo.phoenix.document.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

@Entity
@Table(name = "VT_PHOENIX_ATTRIBUTE_LOV", schema = EntityMapping.PHOENIX_SCHEMA)
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 84221509834750554L;

    @Id
    @Column(name = "LOV_ID")
    private Long id;

    @Column(name = "LOV_NAME")
    private String name;

    @Column(name = "LOV_DESCRIPTION")
    private String description;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "dictionary")
    // @OrderBy("order") // TODO uncomment orderBy, currently it doesnt work with hibernate, instead postload action is used to order dictionary values
    private List<DictionaryValue> values = Lists.newArrayList();

    /**
     * As orderBy doesnt work with hibernate, it is postload action used to sort dictionary values
     */
    @PostLoad
    private void orderDictionaryValues() {
        Function<DictionaryValue, Integer> getOrderFunction = new Function<DictionaryValue, Integer>() {
            @Override
            public Integer apply(DictionaryValue from) {
                return from.getOrder();
            }
        };

        Ordering<DictionaryValue> orderingDictionaryValues = Ordering.natural().onResultOf(getOrderFunction);
        Collections.sort(values, orderingDictionaryValues);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
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
        Dictionary other = (Dictionary) obj;
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
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (values == null) {
            if (other.values != null) {
                return false;
            }
        } else if (!values.equals(other.values)) {
            return false;
        }
        return true;
    }

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

    public List<DictionaryValue> getValues() {
        return values;
    }

    public void setValues(List<DictionaryValue> values) {
        this.values = values;

        for (DictionaryValue dv : values) {
            dv.setDictionary(this);
        }
    }

    public void addValue(DictionaryValue dictionaryValue) {
        this.values.add(dictionaryValue);
    }

}

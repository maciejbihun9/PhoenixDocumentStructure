package com.volvo.phoenix.document.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="VT_PHOENIX_DOMAIN_ATTRIBUTE", schema = EntityMapping.PHOENIX_SCHEMA)
public class DomainAttributeDefinition {
    
    @EmbeddedId
    private DomainAttributeDefinitionPK pk;
  
    @Column(name="MANDATORY")
    private String mandatory;
    @Column(name="SORT_ORDER")
    private Long sortOrder ;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTRIBUTE_ID", referencedColumnName = "ATTRIBUTE_ID", insertable = false, updatable = false)
    private AttributeDefinition attribute;

    public AttributeDefinition getAttribute() {
        return attribute;
    }
    public void setAttribute(AttributeDefinition attribute) {
        this.attribute = attribute;
    }
    public String getMandatory() {
        return mandatory;
    }
    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }
    public Long getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }
    public DomainAttributeDefinitionPK getPk() {
        return pk;
    }
    public void setPk(DomainAttributeDefinitionPK pk) {
        this.pk = pk;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mandatory == null) ? 0 : mandatory.hashCode());
        result = prime * result + ((pk == null) ? 0 : pk.hashCode());
        result = prime * result + ((sortOrder == null) ? 0 : sortOrder.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DomainAttributeDefinition other = (DomainAttributeDefinition) obj;
        if (mandatory == null) {
            if (other.mandatory != null)
                return false;
        } else if (!mandatory.equals(other.mandatory))
            return false;
        if (pk == null) {
            if (other.pk != null)
                return false;
        } else if (!pk.equals(other.pk))
            return false;
        if (sortOrder == null) {
            if (other.sortOrder != null)
                return false;
        } else if (!sortOrder.equals(other.sortOrder))
            return false;
        return true;
    }
}

package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DomainAttributeDefinitionPK implements Serializable{

    @Column(name = "DOMAIN_ID")
    private Long domainId;

    @Column(name = "ATTRIBUTE_ID")
    private Long attributeDefinitionId;
    
    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getAttributeDefinitionId() {
        return attributeDefinitionId;
    }

    public void setAttributeDefinitionId(Long attributeDefinitionId) {
        this.attributeDefinitionId = attributeDefinitionId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributeDefinitionId == null) ? 0 : attributeDefinitionId.hashCode());
        result = prime * result + ((domainId == null) ? 0 : domainId.hashCode());
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
        DomainAttributeDefinitionPK other = (DomainAttributeDefinitionPK) obj;
        if (attributeDefinitionId == null) {
            if (other.attributeDefinitionId != null)
                return false;
        } else if (!attributeDefinitionId.equals(other.attributeDefinitionId))
            return false;
        if (domainId == null) {
            if (other.domainId != null)
                return false;
        } else if (!domainId.equals(other.domainId))
            return false;
        return true;
    }
}

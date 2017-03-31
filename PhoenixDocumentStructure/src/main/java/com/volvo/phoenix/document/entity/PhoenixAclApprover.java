package com.volvo.phoenix.document.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "VT_PHOENIX_ACL_APPROVERS", schema = EntityMapping.PHOENIX_SCHEMA)
public class PhoenixAclApprover {
    
    @EmbeddedId
    private PhoenixAclApproverPK id;

    public PhoenixAclApproverPK getId() {
        if (id == null) {
            id = new PhoenixAclApproverPK();
        }
        return id;
    }

    public void setId(PhoenixAclApproverPK id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

}

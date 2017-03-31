package com.volvo.phoenix.orion.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Access group members
 * 
 */
@Entity
@Table(name = "DM_GROUP_MEMBERS")
public class OrionGroupMember {

    @EmbeddedId
    private OrionGroupMemberPK id;

    public OrionGroupMemberPK getId() {
        return id;
    }

    public void setId(OrionGroupMemberPK id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GroupMember [" + id + "]";
    }
    

}

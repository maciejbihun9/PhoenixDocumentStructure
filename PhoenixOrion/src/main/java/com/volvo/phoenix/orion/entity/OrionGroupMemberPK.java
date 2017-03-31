package com.volvo.phoenix.orion.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * This is primary key for DM_GROUP_MEMBERS
 * 
 */
@Embeddable
public class OrionGroupMemberPK implements Serializable {

    private static final long serialVersionUID = -3697968084386737670L;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
    private OrionGroup orionGroup;
    
    @Column(name="USER_ID")
    private Long userId;
    
    @Column(name="ROLE")
    private String role;

    public OrionGroup getOrionGroup() {
        return orionGroup;
    }

    public void setOrionGroup(OrionGroup orionGroup) {
        this.orionGroup = orionGroup;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "userId=" + userId + ", role=" + role;
    }

}

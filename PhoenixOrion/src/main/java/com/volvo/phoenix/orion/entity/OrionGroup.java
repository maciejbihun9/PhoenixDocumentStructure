package com.volvo.phoenix.orion.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Access group, together with DM_GROUP_MEMBERS, defines the user access group. since the access right defines on the GROUP level in DM_ACL
 * 
 * @author v0cn181
 */
@Entity
@Table(name = "DM_GROUP")
public class OrionGroup {

    /**
     * GROUP_ID
     */
    @Id
    @SequenceGenerator(name = "DM_GROUP_SEQ", sequenceName = "DM_GROUP_ID_SEQ", allocationSize = 28)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DM_GROUP_SEQ")
    @Column(name="GROUP_ID")
    private Long groupId;
    
    /**
     * GROUP_NAME
     */
    @Column(name="GROUP_NAME")
    private String groupName;
    
    @Column(name="OWNER_ID")
    private Long ownerId;
    
    /**
     * Members
     */
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "id.orionGroup")
    private List<OrionGroupMember> members;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<OrionGroupMember> getMembers() {
        if (members == null) {
            members = new ArrayList<OrionGroupMember>();
        }
        return members;
    }

    public void setMembers(List<OrionGroupMember> members) {
        this.members = members;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Group [Id=" + groupId + ", name=" + groupName + ", ownerId=" + ownerId + ", members=" + members + "]";
    }
    
}

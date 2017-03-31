package com.volvo.phoenix.orion.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Orion User information
 * @author v0cn181
 */
@Entity
@Table(name = "DM_USER")
public class OrionUser {
    /**
     * unique user id in numeric format
     */
    @Id
    @SequenceGenerator(name = "DM_USER_SEQ", sequenceName = "DM_USER_ID_SEQ", allocationSize = 28)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DM_USER_SEQ")
    private Long userid;
    
    /**
     * VCN user id, like vcn0xx,bpld2xx
     */
    @Column
    private String username;
    
    /**
     * Orion role id in numeric format
     */
    @Column
    private Long roleId;
    
    /**
     * Orion role name, like EXPORT,APPROVE
     */
    @Column
    private String role;
    
    /**
     * User real name
     */
    @Column
    private String realname;
    
    /**
     * password
     */
    @Column
    private String password;
    
    /**
     * Whether the user is an active user. This is not work anymore, since SSO will authenticate through kerberos and LDAP.
     * But still possible to verfiy it when using spring security in application
     */
    @Column(name = "ACTIVE_USER")
    private String activeUser; 
    
    /**
     * A flag, to signify whether the user should change password before logon.
     */
    @Column(name = "CHANGE_PASSWORD")
    private String changePassword;
    
    /**
     * The access groups user joined, which is defined in DM_GROUP_MEMBERS
     */
    @ManyToMany
    @JoinTable(
        name="DM_GROUP_MEMBERS",
        joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="USERID")},
        inverseJoinColumns={@JoinColumn(name="GROUP_ID", referencedColumnName="GROUP_ID")})
    private List<OrionGroup> groups;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(String activeUser) {
        this.activeUser = activeUser;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }

    public List<OrionGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<OrionGroup> groups) {
        this.groups = groups;
    }

    
}

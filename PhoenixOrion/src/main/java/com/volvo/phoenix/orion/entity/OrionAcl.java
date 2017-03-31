package com.volvo.phoenix.orion.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Definition of ACL with its name, treat as main table of ACL, DM_ACL table is complex relation table betwen
 *  DM_ACL_NAMES , DM_GROUP , DM_STATE and DM_PRIVILEDGE
 *  
 *  @author v0cn181
 */
@Entity
@Table(name="DM_ACL_NAMES")
public class OrionAcl {
    
    /**
     * ACL_ID
     */
    @Id
    @Column(name="ACL_ID")
    private Long id;
    
    /**
     * ACL_NAME
     */
    @Column(name="ACL_NAME")
    private String name;

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
    
}

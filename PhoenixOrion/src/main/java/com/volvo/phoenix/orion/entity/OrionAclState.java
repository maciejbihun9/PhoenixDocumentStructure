package com.volvo.phoenix.orion.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Since in phoenix, the document info class is defined as ACL_STATE. This class is used to get
 *  the state name of corresponding STATE_ID
 *  
 *  @author v0cn181
 */
@Entity
@Table(name="DM_ACL_STATE")
public class OrionAclState {
    /**
     * STATE_ID
     */
    @Id
    @Column(name="STATE_ID")
    private Long id;
    
    /**
     * State name, in phoenix it's info class like
     * 40  Secret
     * 41  Confidential
     * 42  Company
     * 43  Open
     */
    @Column(name="NAME")
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

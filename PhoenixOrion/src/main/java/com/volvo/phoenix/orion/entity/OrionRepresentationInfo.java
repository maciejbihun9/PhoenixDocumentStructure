package com.volvo.phoenix.orion.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Representation info
 * 
 */
@Entity
@Table(name="DM_REP_INFO", schema="DBS")
public class OrionRepresentationInfo {

    @Id
    @Column(name="REP_ID")
    private Long id;
    
    @Column(name="ALIAS_TYPE")
    private String alasType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlasType() {
        return alasType;
    }

    public void setAlasType(String alasType) {
        this.alasType = alasType;
    }
}

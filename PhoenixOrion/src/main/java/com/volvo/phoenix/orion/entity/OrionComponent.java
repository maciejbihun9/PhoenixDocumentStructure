package com.volvo.phoenix.orion.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Document component
 * 
 */
@Entity
@Table(name="DM_COMPONENT", schema="DBS")
public class OrionComponent {

    @Id
    @Column(name="COMP_ID")
    private Long id;

    @Column(name="NAME")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "OBJ_ID", referencedColumnName = "OBJ_ID")
    private OrionDocument document;
    
    @Column(name="STATUS")
    private String status;
    
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "component")
    private List<OrionRepresentation> representations;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrionRepresentation> getRepresentations() {
        return representations;
    }

    public void setRepresentations(List<OrionRepresentation> representations) {
        this.representations = representations;
    }

    public OrionDocument getDocument() {
        return document;
    }

    public void setDocument(OrionDocument document) {
        this.document = document;
    }

 
}

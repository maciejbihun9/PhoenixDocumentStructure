package com.volvo.phoenix.orion.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Representation
 * 
 */
@Entity
@Table(name="DM_REPRESENTATION", schema="DBS")
public class OrionRepresentation {

    @Id
    @Column(name="REP_ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ID", referencedColumnName = "COMP_ID")
    private OrionComponent component;
    
    @OneToOne
    @JoinColumn(name = "REP_ID", referencedColumnName = "REP_ID")
    private OrionRepresentationInfo representationInfo;
    
    @Column(name="TYPE")
    private String type;

    @ManyToMany
    @JoinTable(name = "DM_REPRESENTATION_FILE", schema = "DBS", 
        joinColumns = @JoinColumn(name = "REP_ID", referencedColumnName = "REP_ID"), 
        inverseJoinColumns = @JoinColumn(name = "FILE_ID", referencedColumnName = "FILE_ID"))
    private List<OrionFile> files;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrionComponent getComponent() {
        return component;
    }

    public void setComponent(OrionComponent component) {
        this.component = component;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OrionFile> getFiles() {
        return files;
    }

    public void setFiles(List<OrionFile> files) {
        this.files = files;
    }

    public OrionRepresentationInfo getRepresentationInfo() {
        return representationInfo;
    }

    public void setRepresentationInfo(OrionRepresentationInfo representationInfo) {
        this.representationInfo = representationInfo;
    }

 
}

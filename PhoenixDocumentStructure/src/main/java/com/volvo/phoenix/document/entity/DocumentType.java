package com.volvo.phoenix.document.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "VT_PHOENIX_DOCTYPE", schema = EntityMapping.PHOENIX_SCHEMA)
public class DocumentType {
    
    @Id
    @Column(name = "DOCTYPE_ID")
    private Long id;

    @Column(name = "DOCTYPE_NAME")
    private String name;

    @Column(name = "DOCTYPE_DESCRIPTION")
    private String description;

    @ManyToMany
    @JoinTable(name = "VT_PHOENIX_DOCTYPE_ATTRIBUTE", schema = EntityMapping.PHOENIX_SCHEMA, joinColumns = @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID"), inverseJoinColumns = @JoinColumn(name = "ATTRIBUTE_ID", referencedColumnName = "ATTRIBUTE_ID"))
    private List<AttributeDefinition> attributes;

    @ManyToMany()
    @JoinTable(name = "VT_PHOENIX_FAMILY_DOCTYPE", schema = EntityMapping.PHOENIX_SCHEMA, joinColumns = @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID"), inverseJoinColumns = @JoinColumn(name = "FAMILY_ID", referencedColumnName = "FAMILY_ID"))
    private List<Family> families;

    public List<AttributeDefinition> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDefinition> attributes) {
        this.attributes = attributes;
    }

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

    public List<Family> getFamilies() {
        return families;
    }

    public void setFamilies(List<Family> families) {
        this.families = families;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

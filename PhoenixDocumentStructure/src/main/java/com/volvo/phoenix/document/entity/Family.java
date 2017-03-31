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
@Table(name = "VT_PHOENIX_FAMILY", schema = EntityMapping.PHOENIX_SCHEMA)
public class Family {
    @Id
    @Column(name = "FAMILY_ID")
    private Long id;
    @Column(name = "FAMILY_NAME")
    private String name;

    @ManyToMany()
    @JoinTable(name = "VT_PHOENIX_FAMILY_DOCTYPE", schema = EntityMapping.PHOENIX_SCHEMA, 
        joinColumns = @JoinColumn(name = "FAMILY_ID", referencedColumnName = "FAMILY_ID"),
        inverseJoinColumns = @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID"))
    private List<DocumentType> documentTypes;
                                  
    public Family(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Family() {
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

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }
    public void setDocumentTypes(List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }


}

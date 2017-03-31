package com.volvo.phoenix.document.entity;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Lists;

@Entity
@Table(name = "VT_PHOENIX_DOMAIN", schema = EntityMapping.PHOENIX_SCHEMA)
public class Domain {
    @Id
    @Column(name = "DOMAIN_ID")
    private Long id;
    @Column(name = "DOMAIN_NAME")
    private String name;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "pk.domainId")
    private List<DomainAttributeDefinition> domainAttributes;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "VT_PHOENIX_DOMAIN_DOCTYPE", schema = EntityMapping.PHOENIX_SCHEMA, 
        joinColumns = @JoinColumn(name = "DOMAIN_ID", referencedColumnName = "DOMAIN_ID"), 
        inverseJoinColumns = @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID"))
    private List<DocumentType> documentTypes;

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }
    public void setDocumentTypes(List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }
    /**
     * If you want add new AttributeDefinition please use {@link domainAttributes}
     * @return Unmodifiable collection.
     */
    public List<AttributeDefinition> getAttributes() {
        List<AttributeDefinition> result = Lists.newArrayList();
        for (DomainAttributeDefinition domainAttributeDefinition : domainAttributes) {
            result.add(domainAttributeDefinition.getAttribute());
        }
        return Collections.unmodifiableList(result);
    }

    public List<DomainAttributeDefinition> getDomainAttributes() {
        return domainAttributes;
    }
    public void setDomainAttributes(List<DomainAttributeDefinition> domainAttributes) {
        this.domainAttributes = domainAttributes;
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

}

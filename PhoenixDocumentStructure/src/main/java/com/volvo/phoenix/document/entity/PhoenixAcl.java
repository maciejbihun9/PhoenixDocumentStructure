package com.volvo.phoenix.document.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "VT_PHOENIX_ACL", schema = EntityMapping.PHOENIX_SCHEMA)
public class PhoenixAcl {
    
    @Id
    @Column(name="ACL_ID")
    private Long id;

    @Column(name="PROJECT_NUMBER")
    private String projectNumber;
    
    @ManyToOne
    @JoinColumn(name="DOMAIN_ID",referencedColumnName="DOMAIN_ID")
    private Domain domain;
    
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "id.acl")
    private List<PhoenixAclApprover> workflowApprovers;
    
    @ManyToMany
    @JoinTable(name = "VT_PHOENIX_ACL_DOCTYPE", schema = EntityMapping.PHOENIX_SCHEMA, 
        joinColumns = @JoinColumn(name = "ACL_ID", referencedColumnName = "ACL_ID"),
        inverseJoinColumns = @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID"))
    private List<DocumentType> documentTypes;

    public PhoenixAcl(Long id, Domain domain) {
        super();
        this.id = id;
        this.domain = domain;
    }

    public PhoenixAcl() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public List<PhoenixAclApprover> getWorkflowApprovers() {
        return workflowApprovers;
    }

    public void setWorkflowApprovers(List<PhoenixAclApprover> workflowApprovers) {
        this.workflowApprovers = workflowApprovers;
    }
    public List<DocumentType> getDocumentTypes() {
        if (documentTypes == null) {
            documentTypes = new ArrayList<DocumentType>();
         }
        return documentTypes;
    }

    public void setDocumentTypes(List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

}

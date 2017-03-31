package com.volvo.phoenix.orion.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

/**
 * The main table of Orion to persist Documents
 * 
 * @author v0cn181
 */
@Entity
@Table(name="DM_OBJECT", schema="DBS")
@Transactional
public class OrionDocument {
    /**
     * OBJ_ID
     */
    @Id
    @SequenceGenerator(name = "DM_OBJECT_SEQ", sequenceName = "DM_OBJECT_SEQ", schema="DBS", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DM_OBJECT_SEQ")
    @Column(name="OBJ_ID")
    private Long id;
    
    /**
     * CTX_ID
     */
    @Column(name="CTX_ID")
    private Long ctxId;
    
    /**
     * Registration number in phoenix
     */
    @Column(name="NAME")
    private String name;
    
    /**
     * Version number in phoenix
     */    
    @Column(name="REV")
    private String revision;
    
    @Column(name="OBJ_TYPE")
    private String objectType;
    
    @Column(name="CLASS_NAME")
    private String className;
    
    @Column(name="CUR")
    private String cur;
    
    @Column(name = "OBJ_DATE")
    private Date objectDate;
    
    /**
     * acl info, defines the access to the document
     */
    @ManyToOne
    @JoinColumn(name="ACL_ID",referencedColumnName="ACL_ID")
    private OrionAcl acl;
    
    /**
     * state info of the document, the access to document could be different when the document in different state.
     */
    @ManyToOne
    @JoinColumn(name="STATE_ID",referencedColumnName="STATE_ID")
    private OrionAclState aclState;
    
    @Column(name="INDEX1")
    private String index1;
    
    @Column(name="INDEX4")
    private String index4;
    
    @Column(name="TITLE")
    private String title;
    
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "document")
    private List<OrionComponent> components;

    public Long getId() {
        return id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
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

    public OrionAcl getAcl() {
        return acl;
    }

    public void setAcl(OrionAcl acl) {
        this.acl = acl;
    }

    public OrionAclState getAclState() {
        return aclState;
    }

    public void setAclState(OrionAclState aclState) {
        this.aclState = aclState;
    }

    public Long getCtxId() {
        return ctxId;
    }

    public void setCtxId(Long ctxId) {
        this.ctxId = ctxId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getIndex1() {
        return index1;
    }

    public void setIndex1(String index1) {
        this.index1 = index1;
    }

    public String getIndex4() {
        return index4;
    }

    public void setIndex4(String index4) {
        this.index4 = index4;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getObjectDate() {
        return objectDate;
    }

    public void setObjectDate(Date objectDate) {
        this.objectDate = objectDate;
    }

    public List<OrionComponent> getComponents() {
        return components;
    }

    public void setComponents(List<OrionComponent> components) {
        this.components = components;
    }   

}

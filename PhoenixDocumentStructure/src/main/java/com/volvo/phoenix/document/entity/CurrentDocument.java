package com.volvo.phoenix.document.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entity mapping of current (latest) revision of a document
 */
@Entity
@Table(name = "VT_PHOENIX_DOCUMENT_CURRENT", schema = EntityMapping.PHOENIX_SCHEMA)
public class CurrentDocument {

    @Id
    @Column(name = "OBJ_ID")
    private Long id;
    
    /**
     * Version number in phoenix
     */    
    @Column(name="REV")
    private String revision;
    
    @Column(name="STATE_NAME")
    private String aclStateName;
    
    @Column
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "NODE_ID", referencedColumnName = "NODE_ID")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "DOMAIN_ID", referencedColumnName = "DOMAIN_ID")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "FAMILY_ID", referencedColumnName = "FAMILY_ID")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID")
    private DocumentType type;
    
    @ManyToOne
    @JoinColumn(name = "DOCUMENT_STATUS", referencedColumnName = "STATUS")
    private DocumentStatus status;    

    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "AUTHOR")
    private String author;
    
    @Column(name = "AUTHOR_ID")
    private String authorId;
    
    @Column(name = "ISSUE_DATE")
    private Date issueDate;
    
    @Column(name = "NOTES")
    private String notes;


    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }


    @Transient
    public String getPath() {

        if (folder != null) {
            return folder.getPath() + "\\" + title;
        } else {
            return "\\" + title;
        }
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getAclStateName() {
        return aclStateName;
    }

    public void setAclStateName(String aclStateName) {
        this.aclStateName = aclStateName;
    }

}

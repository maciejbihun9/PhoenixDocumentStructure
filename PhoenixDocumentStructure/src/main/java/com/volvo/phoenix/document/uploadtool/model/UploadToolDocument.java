package com.volvo.phoenix.document.uploadtool.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.EntityMapping;
import com.volvo.phoenix.document.entity.Family;

/**
 * Validated Upload Tool document Aggregate
 */
@Entity
@Table(name = "UT_DOCUMENT", schema = EntityMapping.PHOENIX_SCHEMA)
public class UploadToolDocument {

    @Id
    @SequenceGenerator(sequenceName = "UT_DOCUMENT_SEQ", name = "UT_DOCUMENT_SEQ", schema = EntityMapping.PHOENIX_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UT_DOCUMENT_SEQ")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TREE_NODE_ID", referencedColumnName = "NODE_ID")
    private UploadToolOperationTreeNode treeNode;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REVISION")
    private Long revision;

    @Column(name = "STATE_ID")
    private Long stateId;

    @ManyToOne
    @JoinColumn(name = "FAMILY_ID", referencedColumnName = "FAMILY_ID")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID")
    private DocumentType type;

    @Column(name = "DOCUMENT_STATUS")
    @Enumerated(EnumType.STRING)
    private UploadToolDocumentStatus status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "ISSUER")
    private String issuer;

    @Column(name = "ISSUER_ID")
    private String issuerId;

    @Column(name = "AUTHOR_ID")
    private String authorId;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "ALT_DOC_ID")
    private String altDocId;

    @Column(name = "PROTECT_IN_WORK")
    private Boolean protectInWork = Boolean.FALSE;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "owningDocument")
    private List<UploadToolDocumentAttributeValue> attributesValues;

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
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

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public UploadToolOperationTreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(UploadToolOperationTreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public UploadToolDocumentStatus getStatus() {
        return status;
    }

    public void setStatus(UploadToolDocumentStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAltDocId() {
        return altDocId;
    }

    public void setAltDocId(String altDocId) {
        this.altDocId = altDocId;
    }

    public Boolean getProtectInWork() {
        return protectInWork;
    }

    public void setProtectInWork(Boolean protectInWork) {
        this.protectInWork = protectInWork;
    }

    public List<UploadToolDocumentAttributeValue> getAttributesValues() {
        if (attributesValues == null) {
            attributesValues = new ArrayList<UploadToolDocumentAttributeValue>();
        }
        return attributesValues;
    }

    public void setAttributesValues(List<UploadToolDocumentAttributeValue> attributesValues) {
        this.attributesValues = attributesValues;
    }

}

package com.volvo.phoenix.document.entity;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Transient;

@Entity
@Table(name = "VT_PHOENIX_DOCUMENT", schema = EntityMapping.PHOENIX_SCHEMA)
public class Document {

    @Id
    @Column(name = "OBJ_ID")
    private Long id;

    @Column(name="OBJ_TYPE")
    private String objectType;
    
    @Column(name="INDEX_ID")
    private Long indexId;
    
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
    
    @Column(name = "OBJECT_NO")
    private String objectNo;
    
    @Column(name = "OBJECT_NO_SUFFIX")
    private String objectNoSuffix;
    
    @Column(name = "PERSONAL")
    private String personal;
    
    @Column(name = "FUNCTION_GROUP")
    private Long functionGroup;
    
    @Column(name = "DECISION")
    private String decision;
    
    @Column(name = "GATE")
    private String gate;
    
    @Column(name = "PRODUCT_CLASS")
    private String productClass;
    
    @Column(name = "BRAND")
    private String brand;
    
    @Column(name = "LOGBOOK_MAJOR_VERSION")
    private String logbookMajorVersion;
    
    @Column(name = "TEMPSTORE")
    private String tempstore;
    
    @Column(name = "ALT_DOC_ID")
    private String altDocId;
    
    @Column(name = "DOCUMENT_CONTAINER")
    private String documentContainer;
    
    @Column(name = "CONTENT_STATUS")
    private String contentStatus;
    
    @Column(name = "PROTECT_IN_WORK")
    private String protectInWork;

    @ManyToMany
    @JoinTable(name = "VT_PHOENIX_ATTRIBUTE_VALUE", schema = EntityMapping.PHOENIX_SCHEMA, 
        joinColumns = @JoinColumn(name = "OBJ_ID", referencedColumnName = "OBJ_ID"), 
        inverseJoinColumns = @JoinColumn(name = "ATTRIBUTE_ID", referencedColumnName = "ATTRIBUTE_ID"))
    private List<AttributeDefinition> attributes;

    //TODO Consider to use DocumentAttribute insted of DocumentAttributeValue.
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "owningDocument")
    private List<DocumentAttributeValue> documentAttributes;
    
    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "OBJ_ID", insertable = false, updatable = false)
    private List<DocumentAttribute> cmDocumentAttributes;

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Long getIndexId() {
        return indexId;
    }

    public void setIndexId(Long indexId) {
        this.indexId = indexId;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getProtectInWork() {
        return protectInWork;
    }

    public void setProtectInWork(String protectInWork) {
        this.protectInWork = protectInWork;
    }

    public List<AttributeDefinition> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDefinition> attributes) {
        this.attributes = attributes;
    }


    public List<DocumentAttributeValue> getDocumentAttributes() {
        if (documentAttributes == null) {
            documentAttributes = new ArrayList<DocumentAttributeValue>();
        }
        return documentAttributes;
    }

    public void setDocumentAttributes(List<DocumentAttributeValue> documentAttributes) {
        this.documentAttributes = documentAttributes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
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

    @Transient
    public String getPath() {

        if (folder != null) {
            return folder.getPath() + "\\" + title;
        } else {
            return "\\" + title;
        }
    }

    @Transient
    public Folder getRoot() {

        if (folder != null) {
            return folder.getRoot();
        }
        
        return null;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getObjectNo() {
        return objectNo;
    }

    public void setObjectNo(String objectNo) {
        this.objectNo = objectNo;
    }

    public String getObjectNoSuffix() {
        return objectNoSuffix;
    }

    public void setObjectNoSuffix(String objectNoSuffix) {
        this.objectNoSuffix = objectNoSuffix;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public Long getFunctionGroup() {
        return functionGroup;
    }

    public void setFunctionGroup(Long functionGroup) {
        this.functionGroup = functionGroup;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLogbookMajorVersion() {
        return logbookMajorVersion;
    }

    public void setLogbookMajorVersion(String logbookMajorVersion) {
        this.logbookMajorVersion = logbookMajorVersion;
    }

    public String getTempstore() {
        return tempstore;
    }

    public void setTempstore(String tempstore) {
        this.tempstore = tempstore;
    }

    public String getAltDocId() {
        return altDocId;
    }

    public void setAltDocId(String altDocId) {
        this.altDocId = altDocId;
    }

    public String getDocumentContainer() {
        return documentContainer;
    }

    public void setDocumentContainer(String documentContainer) {
        this.documentContainer = documentContainer;
    }

    public String getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(String contentStatus) {
        this.contentStatus = contentStatus;
    }

    public List<DocumentAttribute> getCmDocumentAttributes() {
        return cmDocumentAttributes;
    }

    public void setCmDocumentAttributes(List<DocumentAttribute> cmDocumentAttributes) {
        this.cmDocumentAttributes = cmDocumentAttributes;
    }
}

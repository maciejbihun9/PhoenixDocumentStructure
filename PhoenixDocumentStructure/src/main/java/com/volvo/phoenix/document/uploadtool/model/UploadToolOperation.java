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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.volvo.phoenix.document.entity.EntityMapping;
import com.volvo.phoenix.document.entity.Folder;

/**
 * Upload tool batch operation information Aggregate
 */
@Entity
@Table(name = "UT_OPERATION", schema = EntityMapping.PHOENIX_SCHEMA)
public class UploadToolOperation {

    @Id
    @SequenceGenerator(sequenceName = "UT_OPERATION_SEQ", name = "UT_OPERATION_SEQ", schema = EntityMapping.PHOENIX_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UT_OPERATION_SEQ")
    @Column(name = "ID")
    private Long id;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @OneToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "FOLDER_ID", referencedColumnName = "NODE_ID")
    private Folder folder;

    @Column(name = "USERNAME")
    private String user;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    
    @Column(name = "FINISH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;

    @Enumerated(EnumType.STRING)
    private UploadToolOperationStatus status;
        
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "operation")
    private List<UploadToolOperationFile> uploadedFiles;
    
    @OneToMany(cascade = { CascadeType.MERGE }, mappedBy = "operation")
    private List<UploadToolOperationTreeNode> uploadedTree;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UploadToolOperationStatus getStatus() {
        return status;
    }

    public void setStatus(UploadToolOperationStatus status) {
        this.status = status;
    }

    public List<UploadToolOperationFile> getUploadedFiles() {
        if (uploadedFiles == null) {
            uploadedFiles = new ArrayList<UploadToolOperationFile>();
        }
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadToolOperationFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public List<UploadToolOperationTreeNode> getUploadedTree() {
        if (uploadedTree == null) {
            uploadedTree = new ArrayList<UploadToolOperationTreeNode>();
        }
        return uploadedTree;
    }

    public void setUploadedTree(List<UploadToolOperationTreeNode> uploadedTree) {
        this.uploadedTree = uploadedTree;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
    
}

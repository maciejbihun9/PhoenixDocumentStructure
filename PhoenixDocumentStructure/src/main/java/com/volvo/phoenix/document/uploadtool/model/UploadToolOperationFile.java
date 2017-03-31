package com.volvo.phoenix.document.uploadtool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import com.volvo.phoenix.document.datatype.MetadataStatus;
import com.volvo.phoenix.document.entity.EntityMapping;

/**
 * Uploaded file information Entity
 */
@Entity
@Table(name = "UT_OPERATION_FILE", schema = EntityMapping.PHOENIX_SCHEMA)
public class UploadToolOperationFile {

    @Id
    @SequenceGenerator(sequenceName = "UT_OPERATION_FILE_SEQ", name = "UT_OPERATION_FILE_SEQ", schema = EntityMapping.PHOENIX_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UT_OPERATION_FILE_SEQ")
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "OPERATION_ID", referencedColumnName = "ID")
    private UploadToolOperation operation;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "FILE_NAME")
    private String fileName;
    
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private UploadToolFileType fileType;
    
    @Column(name = "METADATA_STATUS")
    @Enumerated(EnumType.STRING)
    private MetadataStatus metadataStatus;

    @Column(name = "FILE_SIZE")
    private Long size;    
    
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UploadToolOperation getOperation() {
        return operation;
    }

    public void setOperation(UploadToolOperation operation) {
        this.operation = operation;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public UploadToolFileType getFileType() {
        return fileType;
    }

    public void setFileType(UploadToolFileType fileType) {
        this.fileType = fileType;
    }

    public MetadataStatus getMetadataStatus() {
        return metadataStatus;
    }

    public void setMetadataStatus(MetadataStatus metadataStatus) {
        this.metadataStatus = metadataStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}

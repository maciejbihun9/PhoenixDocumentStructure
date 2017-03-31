package com.volvo.phoenix.document.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VT_PHOENIX_DOC_DEF_ATTRIBUTE", schema = EntityMapping.PHOENIX_SCHEMA)
public class NewDocumentDefaults {

    @Id
    @Column(name = "FOLDER_ID")
    private Long folderId;
    
    @OneToOne
    @JoinColumn(name = "FOLDER_ID", referencedColumnName = "NODE_ID")
    private Folder owningFolder; 
    
    @Column(name = "INFO_CLASS_ID")
    private Long stateId;

    @ManyToOne
    @JoinColumn(name = "DOCUMENT_FAMILY_ID", referencedColumnName = "FAMILY_ID")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "DOCUMENT_TYPE_ID", referencedColumnName = "DOCTYPE_ID")
    private DocumentType type;
    
    @ManyToOne
    @JoinColumn(name = "WORK_STATUS", referencedColumnName = "STATUS")
    private DocumentStatus status;

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Folder getOwningFolder() {
        return owningFolder;
    }

    public void setOwningFolder(Folder owningFolder) {
        this.owningFolder = owningFolder;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
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

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }   

}

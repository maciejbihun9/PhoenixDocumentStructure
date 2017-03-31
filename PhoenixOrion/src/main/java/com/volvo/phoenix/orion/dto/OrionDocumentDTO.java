package com.volvo.phoenix.orion.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrionDocumentDTO {
    
    private Long id;
    private String name;
    private String revision;
    private String objectType;
    private Date objectDate;
    private String index1;
    private String index4;
    private String title;
    private OrionAclDTO acl;
    private OrionAclStateDTO aclState;  
    List<OrionFileDTO> fileList = new ArrayList<OrionFileDTO>();

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
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
    
    /**
     * Name = Document registration number 
     */
    public String getRegistrationNumber() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrionAclDTO getAcl() {
        return acl;
    }

    public void setAcl(OrionAclDTO acl) {
        this.acl = acl;
    }

    public OrionAclStateDTO getAclState() {
        return aclState;
    }

    public void setAclState(OrionAclStateDTO aclState) {
        this.aclState = aclState;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
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

    public List<OrionFileDTO> getFileList() {
        return fileList;
    }

    public void setFileList(List<OrionFileDTO> fileList) {
        this.fileList = fileList;
    }

    public Long getAclId() {
        return acl.getId();
    }

    public Long getStateId() {
        return aclState.getId();
    }

    public boolean isNewVersion() {
        return (this.revision != null) && !"1".equals(this.revision);
    }
}

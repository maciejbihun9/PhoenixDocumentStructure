package com.volvo.phoenix.document.uploadtool.application.dto;

import java.util.ArrayList;
import java.util.List;

import com.volvo.phoenix.document.uploadtool.model.UploadToolNodeType;

public class UploadToolTreeNodeDTO {

    private long id;
    private String name;
    private String absolutePath;
    private UploadToolNodeType nodeType;
    private List<UploadToolTreeNodeDTO> children;
    private long parentId;
    private UploadToolDocumentDTO document;
    private boolean valid;
    private boolean newVersion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nodeText) {
        this.name = nodeText;
    }

    public UploadToolNodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(UploadToolNodeType nodeType) {
        this.nodeType = nodeType;
    }

    public List<UploadToolTreeNodeDTO> getChildren() {
        if (children == null) {
            children = new ArrayList<UploadToolTreeNodeDTO>();
        }
        return children;
    }

    public void setChildren(List<UploadToolTreeNodeDTO> children) {
        this.children = children;
    }

    public UploadToolDocumentDTO getDocument() {
        return document;
    }

    public void setDocument(UploadToolDocumentDTO document) {
        this.document = document;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public String toString() {
        return "UploadToolTreeNodeDTO [id=" + id + ", name=" + name + ", nodeType=" + nodeType + ", children=" + children + ", parentId=" + parentId
                + ", document=" + document + ", valid=" + valid + ", newVersion=" + newVersion + "]";
    }
}

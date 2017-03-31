package com.volvo.phoenix.document.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volvo.phoenix.document.datatype.NodeType;

public class FolderDTO implements Serializable {

    private static final long serialVersionUID = 2597681209485402506L;

    /*
     * For now ignore not much needed fields from Folder entity.
     */
    
    private Long id;
    @JsonIgnore
    private FolderDTO parent;
    @JsonIgnore
    private String text;
    @JsonIgnore
    private NodeType type;
    @JsonIgnore
    private String ownerRealname;
    @JsonIgnore
    private PhoenixAclDTO acl;

    private String path;
    private FolderDTO root;

    public PhoenixAclDTO getAcl() {
        return acl;
    }

    public void setAcl(PhoenixAclDTO acl) {
        this.acl = acl;
    }

    public String getOwnerRealname() {
        return ownerRealname;
    }

    public void setOwnerRealname(String ownerRealname) {
        this.ownerRealname = ownerRealname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FolderDTO getParent() {
        return parent;
    }

    public void setParent(FolderDTO parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setRoot(FolderDTO root) {
        this.root = root;
    }

    public FolderDTO getRoot() {
        return root;
    }
}

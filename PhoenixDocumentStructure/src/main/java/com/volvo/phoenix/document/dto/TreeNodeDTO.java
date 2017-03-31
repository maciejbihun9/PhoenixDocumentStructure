package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.volvo.phoenix.document.datatype.NodeType;

//TODO update javadoc to inform what is TreeNode
public class TreeNodeDTO implements Serializable {

    private static final long serialVersionUID = -4999809865041516277L;

    private Long id;

    private Long parentId;
    
    private String pathId;
    
    private boolean bussinessAdmin;
    /**
     * Name/label which will be prensented on tree.
     */
    private String name;
    /**
     * Path to a document prensented on tab.
     */
    private String path;

    /**
     * Collection on child nodes.
     */
    private List<TreeNodeDTO> children;
    /**
     * URL to be opened on click in personal browser/inbox.
     */
    private String url;

    private Long aclId;

    private String owner;
    
    private String ownerRealname;

    /**
     * TYPE of the item.
     */
    private NodeType type;
    /**
     * date of the item, not available for folder and issue_date for document.
     */
    private String date;

    /**
     * Not available for folder, status of the document.
     */
    private String status;

    /**
     * Not available for folder, family_name of the document.
     */
    private String family;

    private String nodeId;

    private String docType;
    private String docTypeId;
    private String domainId;

    private String infoClass;
    
    private String version;
    
    private Long size;

    public String getInfoClass() {
        return infoClass;
    }

    public void setInfoClass(String infoClass) {
        this.infoClass = infoClass;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String author) {
        this.owner = author;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHasKids() {
        return CollectionUtils.isNotEmpty(children);
    }

    public List<TreeNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeDTO> children) {
        this.children = children;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getAclId() {
        return aclId;
    }

    public void setAclId(Long aclId) {
        this.aclId = aclId;
    }

    public boolean isFolderNode() {
        return NodeType.D != getType();
    }

    public boolean isDocumentNode() {
        return !isFolderNode();
    }

    public String getOwnerRealname() {
        return ownerRealname;
    }

    public void setOwnerRealname(String ownerRealname) {
        this.ownerRealname = ownerRealname;
    }
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    public boolean getBussinessAdmin() {
        return bussinessAdmin;
    }

    public void setBussinessAdmin(boolean bussinessAdmin) {
        this.bussinessAdmin = bussinessAdmin;
    }
    
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}

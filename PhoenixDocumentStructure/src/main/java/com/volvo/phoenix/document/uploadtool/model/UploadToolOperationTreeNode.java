package com.volvo.phoenix.document.uploadtool.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import com.volvo.phoenix.document.entity.EntityMapping;

/**
 * Uploaded tree structure element Entity
 */
@Entity
@Table(name = "UT_OPERATION_TREE", schema = EntityMapping.PHOENIX_SCHEMA)
public class UploadToolOperationTreeNode {

    @ManyToOne
    @JoinColumn(name = "OPERATION_ID", referencedColumnName = "ID")
    private UploadToolOperation operation;

    @Id
    @SequenceGenerator(sequenceName = "UT_OPERATION_TREE_SEQ", name = "UT_OPERATION_TREE_SEQ", schema = EntityMapping.PHOENIX_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UT_OPERATION_TREE_SEQ")
    @Column(name = "NODE_ID")
    private Long nodeId;

    @ManyToOne
    @JoinColumn(name = "NODE_PARENT_ID", referencedColumnName = "NODE_ID")
    private UploadToolOperationTreeNode parentNode;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "NODE_TEXT")
    private String nodeText;

    @Column(name = "NODE_TYPE")
    @Enumerated(EnumType.STRING)
    private UploadToolNodeType nodeType;

    private String rev;

    @Column(name = "IS_VALID")
    private Boolean valid = Boolean.FALSE;
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "parentNode")
    private List<UploadToolOperationTreeNode> childNodes;
    
    @Column(name = "ABSOLUTE_PATH")
    private String absolutePath;
    
    @Column(name = "PHOENIX_OBJ_REF")
    private Long phoenixObjectReference;

    @ElementCollection
    @MapKeyColumn(name = "NAME")
    @Column(name = "VALUE")
    @CollectionTable(name = "UT_DOCUMENT_ATTRIBUTE", joinColumns = @JoinColumn(name = "NODE_ID"))
    private Map<String, String> documentAttributes;

    public UploadToolOperation getOperation() {
        return operation;
    }

    public void setOperation(UploadToolOperation operation) {
        this.operation = operation;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public UploadToolOperationTreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(UploadToolOperationTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getNodeText() {
        return nodeText;
    }

    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
    }

    public UploadToolNodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(UploadToolNodeType nodeType) {
        this.nodeType = nodeType;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public List<UploadToolOperationTreeNode> getChildNodes() {
        if (childNodes == null) {
            childNodes = new ArrayList<UploadToolOperationTreeNode>();
        }
        return childNodes;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public void setChildNodes(List<UploadToolOperationTreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    public Map<String, String> getDocumentAttributes() {
        if (documentAttributes == null) {
            documentAttributes = new HashMap<String, String>();
        }
        return documentAttributes;
    }

    public void setDocumentAttributes(Map<String, String> documentAttributes) {
        this.documentAttributes = documentAttributes;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Long getPhoenixObjectReference() {
        return phoenixObjectReference;
    }

    public void setPhoenixObjectReference(Long phoenixObjectReference) {
        this.phoenixObjectReference = phoenixObjectReference;
    }

    @Override
    public String toString() {
        return "UploadToolOperationTreeNode [nodeId=" + nodeId + ", version=" + version + ", nodeText=" + nodeText + ", nodeType=" + nodeType + ", rev=" + rev
                + "]";
    }
    
}

package com.volvo.phoenix.document.entity;

import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.volvo.phoenix.document.datatype.NodeType;

@Entity
@Table(name = "VT_PHOENIX_TREE", schema = EntityMapping.PHOENIX_SCHEMA)
public class Folder {

    @Id
    @SequenceGenerator(sequenceName = "VT_PHOENIX_TREE_SEQ", name = "treeSeq", schema = EntityMapping.PHOENIX_SCHEMA, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treeSeq")
    @Column(name = "NODE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "NODE_PARENT_ID", referencedColumnName = "NODE_ID")
    private Folder parent;

    @Column(name = "NODE_TEXT")
    private String text;
    
    @Column(name = "NODE_DESCRIPTION")
    private String description;
    
    @Column(name = "NODE_LEVEL")
    private Long nodeLevel;

    @Column(name = "NODE_TYPE")
    @Enumerated(EnumType.STRING)
    private NodeType type;
    
    @Column(name = "OWNER")
    private String owner;

    @Column(name = "OWNER_REALNAME")
    private String ownerRealname;

    @ManyToOne
    @JoinColumn(name = "ACL_ID", referencedColumnName = "ACL_ID")
    private PhoenixAcl acl;
    
    @ManyToOne
    @JoinColumn(name = "PARENT_ACL_ID", referencedColumnName = "ACL_ID")
    private PhoenixAcl parentAcl;
    
    @Column(name = "CREATED")
    private Date created;
    
    @OneToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "NODE_ID", referencedColumnName = "FOLDER_ID")
    private NewDocumentDefaults newDocumentDefaults;
    
    
    public PhoenixAcl getAcl() {
        return acl;
    }

    public NewDocumentDefaults getNewDocumentDefaults() {
        if (type == NodeType.M) {
            return newDocumentDefaults;
        } else {
            if (parent != null) {
                return parent.getNewDocumentDefaults();
            }
        }
        return null;
    }

    public void setNewDocumentDefaults(NewDocumentDefaults newDocumentDefaults) {
        this.newDocumentDefaults = newDocumentDefaults;
    }

    public void setAcl(PhoenixAcl acl) {
        this.acl = acl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
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

    @Transient
    public String getPath() {
        if (parent != null && !parent.equals(this)) {
            return parent.getPath() + "\\" + text;
        } else {
            return "\\" + text;
        }
    }

    @Transient
    public String getPathId() {
        if (parent != null && !parent.equals(this)) {
            return parent.getPathId().toString() + ":" + id.toString();
        } else {
            return ":" + id.toString();
        }
    }

    @Transient
    public Folder getRoot() {
        if (parent != null && !parent.equals(this)) {
            return parent.getRoot();
        } else {
            return this;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(Long nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getOwnerRealname() {
        return ownerRealname;
    }

    public void setOwnerRealname(String ownerRealname) {
        this.ownerRealname = ownerRealname;
    }

    public PhoenixAcl getParentAcl() {
        return parentAcl;
    }

    public void setParentAcl(PhoenixAcl parentAcl) {
        this.parentAcl = parentAcl;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "id=" + id + "\ntext=" + text + "\ntype=" + type + "\nowner=" + ownerRealname + "\nacl=" + acl.getId();
    }

}

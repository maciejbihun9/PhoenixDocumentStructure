package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class PhoenixAclApproverPK implements Serializable {

    private static final long serialVersionUID = -5447646652932245461L;

    @ManyToOne
    @JoinColumn(name = "ACL_ID", referencedColumnName = "ACL_ID")
    private PhoenixAcl acl;

    @Column(name="DOCTYPE_ID")
    private Long doctypeId;
    
    @Column(name="USERNAME")
    private String username;


    public PhoenixAcl getAcl() {
        return acl;
    }

    public void setAcl(PhoenixAcl acl) {
        this.acl = acl;
    }

    public Long getDoctypeId() {
        return doctypeId;
    }

    public void setDoctypeId(Long doctypeId) {
        this.doctypeId = doctypeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "acl=" + acl + ", doctypeId=" + doctypeId + ", username=" + username;
    }


}

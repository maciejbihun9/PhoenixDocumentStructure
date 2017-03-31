package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Counterpart for {@link com.volvo.phoenix.document.entity.AuditLog}.
 * 
 * @author bpld313
 *
 */
public abstract class AuditLogDTO implements Serializable {

    private static final long serialVersionUID = 1952081599993094588L;

    private Long id;
    private Date when;
    private String userVcnId;
    private String userFullName;

    public AuditLogDTO() {
    }

    public AuditLogDTO(Date when, String userVcnId, String userFullName) {
        super();
        this.when = when;
        this.userVcnId = userVcnId;
        this.userFullName = userFullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public String getUserVcnId() {
        return userVcnId;
    }

    public void setUserVcnId(String whoVcnId) {
        this.userVcnId = whoVcnId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String whoFullName) {
        this.userFullName = whoFullName;
    }

}

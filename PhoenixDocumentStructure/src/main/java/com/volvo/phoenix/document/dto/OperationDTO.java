package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.Date;

import com.volvo.phoenix.document.datatype.OperationStatus;
import com.volvo.phoenix.document.datatype.OperationType;

/**
 * DTO corresponding to Operation entity.
 */
public class OperationDTO implements Serializable {

    private static final long serialVersionUID = 3475697602308714597L;

    private Long id;

    private OperationStatus status;
    private OperationType operationType;
    private Date createDate;

    private TreeNodeDTO source;
    private TreeNodeDTO target;

    private boolean success;
    private String message;
    // TODO need to replace with User info after implementing spring security
    private String user;

    @Override
    public String toString() {
        return "OperationDTO [id=" + id + ", source=" + source + ", target=" + target + ", operationType=" + operationType + ", status=" + status
                + ", createDate=" + createDate + ", user=" + user + "]";
    }

    public String getSourcePath() {
        return source.getPath();
    }

    public String getTargetPath() {
        return target.getPath();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TreeNodeDTO getSource() {
        return source;
    }

    public void setSource(TreeNodeDTO source) {
        this.source = source;
    }

    public TreeNodeDTO getTarget() {
        return target;
    }

    public void setTarget(TreeNodeDTO target) {
        this.target = target;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getInfo() {
        return source.getPath() + " -> " + target.getPath();
    }
}

package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RootFolderStatePK implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1600321916384163582L;
    @Column(name="NODE_ID")
    private Long nodeId;
    @Column(name="STATE_ID")
    private Long stateId;
    public Long getNodeId() {
        return nodeId;
    }
    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
    public Long getStateId() {
        return stateId;
    }
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return nodeId.hashCode()*31+stateId.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RootFolderStatePK) {
            RootFolderStatePK pk = (RootFolderStatePK)obj;
            return nodeId.equals(pk.getNodeId()) && stateId.equals(pk.getStateId());
        }
        return false;
    }
}

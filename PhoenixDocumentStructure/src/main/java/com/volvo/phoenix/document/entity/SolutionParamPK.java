package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.volvo.phoenix.document.datatype.SolutionNameType;

@Embeddable
public class SolutionParamPK implements Serializable {

    private static final long serialVersionUID = 3165662184695714408L;

    @Column(name = "OPERATION_ID")
    private Long operationId;
    @Column(name = "SOLUTION")
    @Enumerated(EnumType.STRING)
    private SolutionNameType solution;
    @Column(name = "PARAM_ID")
    private Long paramId;

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public SolutionNameType getSolution() {
        return solution;
    }

    public void setSolution(SolutionNameType solution) {
        this.solution = solution;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((operationId == null) ? 0 : operationId.hashCode());
        result = prime * result + ((paramId == null) ? 0 : paramId.hashCode());
        result = prime * result + ((solution == null) ? 0 : solution.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SolutionParamPK other = (SolutionParamPK) obj;
        if (operationId == null) {
            if (other.operationId != null) {
                return false;
            }
        } else if (!operationId.equals(other.operationId)) {
            return false;
        }
        if (paramId == null) {
            if (other.paramId != null) {
                return false;
            }
        } else if (!paramId.equals(other.paramId)) {
            return false;
        }
        if (solution != other.solution) {
            return false;
        }
        return true;
    }

}

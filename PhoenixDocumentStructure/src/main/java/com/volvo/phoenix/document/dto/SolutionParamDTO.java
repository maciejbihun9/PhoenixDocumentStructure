package com.volvo.phoenix.document.dto;

import java.io.Serializable;

import com.volvo.phoenix.document.datatype.ConflictType;
import com.volvo.phoenix.document.datatype.SolutionNameType;

public class SolutionParamDTO implements Serializable {

    private static final long serialVersionUID = -665387088644546095L;

    private Long operationId;
    private SolutionNameType solution;
    private Long id;
    private String value;

    public SolutionNameType getSolution() {
        return solution;
    }

    public void setSolution(SolutionNameType solution) {
        this.solution = solution;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }
}

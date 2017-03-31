package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.volvo.phoenix.document.datatype.SolutionNameType;

@Entity
@Table(name = "CM_OPERATION_PARAM", schema = EntityMapping.CM_SCHEMA)
public class SolutionParam implements Serializable {

    private static final long serialVersionUID = -1000195506650552686L;
    
    @Id
    @SequenceGenerator(sequenceName = "CM_OPERATION_PARAM_SEQ", name = "CM_OPERATION_PARAM_SEQ", schema = EntityMapping.CM_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CM_OPERATION_PARAM_SEQ")
    @Column(name = "ID")
    private Long id ; 
    
    @Column(name = "OPERATION_ID")
    private Long operationId;

    @Column(name = "SOLUTION")
    @Enumerated(EnumType.STRING)
    private SolutionNameType solution;
    
    @Column(name = "PARAM_ID")
    private Long paramId;

    @Column(name = "PARAM_VALUE")
    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((operationId == null) ? 0 : operationId.hashCode());
        result = prime * result + ((paramId == null) ? 0 : paramId.hashCode());
        result = prime * result + ((solution == null) ? 0 : solution.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SolutionParam other = (SolutionParam) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (operationId == null) {
            if (other.operationId != null)
                return false;
        } else if (!operationId.equals(other.operationId))
            return false;
        if (paramId == null) {
            if (other.paramId != null)
                return false;
        } else if (!paramId.equals(other.paramId))
            return false;
        if (solution != other.solution)
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}

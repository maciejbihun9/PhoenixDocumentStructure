package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ProposedAttribute implements Serializable{

    private static final long serialVersionUID = 1867068618006485825L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTRIBUTE_ID", referencedColumnName = "ATTRIBUTE_ID")
    private AttributeDefinition attribute;

    @Column
    private String value;

}

package com.volvo.phoenix.document.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import com.google.common.collect.Lists;

public class AssignDocTypeToTargetDomain extends ConflictResolution {

    private static final long serialVersionUID = 5295938829861052894L;

    @Column
    private Long documentId;

    /**
     * New optional attributes that don't exist in current document domain, are defined in target domain and user decided to provide values for them.
     */
    @ElementCollection
    private List<ProposedAttribute> optionalAttributes = Lists.newArrayList();

    /**
     * Selected attributes from old document domain that have to be created in target domain.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(table = "CM_DOMAIN_NEW_ATTRIBUTES", name = "TO_BE_CREATED_ATTRIBUTE_ID")
    private List<AttributeDefinition> domainNewAttributes;

}

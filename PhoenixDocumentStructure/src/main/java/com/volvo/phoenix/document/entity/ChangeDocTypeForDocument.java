package com.volvo.phoenix.document.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;

import com.google.common.collect.Lists;

public class ChangeDocTypeForDocument extends ConflictResolution {

    private static final long serialVersionUID = 3034031853438309133L;

    @Column
    private Long documentId;

    @ElementCollection
    private List<ProposedAttribute> obligatoryAttributes = Lists.newArrayList();

}

package com.volvo.phoenix.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Represents proposed by user solution to one of operation conflicts.
 */
// @Entity
@Table(name = "CM_CONFLICT_RESOLUTION", schema = EntityMapping.CM_SCHEMA)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CONFLICT_RESOLUION_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class ConflictResolution implements Serializable {

    private static final long serialVersionUID = 6116166624176356589L;

    @Id
    @SequenceGenerator(sequenceName = "CM_CONFLICT_RESOLUTION_SEQ", name = "CM_CONFLICT_RESOLUTION_SEQ", schema = EntityMapping.CM_SCHEMA)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CM_CONFLICT_RESOLUTION_SEQ")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operation concernsOperation;

}

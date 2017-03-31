package com.volvo.phoenix.document.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volvo.phoenix.document.entity.Domain;

/**
 * 
 */
public interface DomainRepository
extends CrudRepository<Domain, Long>  {
    @Query(value = "SELECT d FROM Domain d left JOIN FETCH d.domainAttributes da left JOIN FETCH  da.attribute where d.id = :id")
    Domain findById(@Param("id") Long id);

    @Query(value = "SELECT d FROM Domain d left JOIN FETCH d.documentTypes where d.id = :id")
    Domain findByIdWithDoctypes(@Param("id") Long id);

}

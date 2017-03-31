package com.volvo.phoenix.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.Family;


public interface DocumentTypeRepository extends CrudRepository<DocumentType, Long> {

    @Query(value = "SELECT d FROM DocumentType d left JOIN FETCH d.attributes where d.id = :docTypeId")
    DocumentType findByIdWithAttributes(@Param("docTypeId") Long docTypeId);
    
    Family findFamiliesById(Long id);


}

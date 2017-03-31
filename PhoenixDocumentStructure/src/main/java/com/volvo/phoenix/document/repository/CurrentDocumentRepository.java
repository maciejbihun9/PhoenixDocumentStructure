package com.volvo.phoenix.document.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.entity.CurrentDocument;

@Transactional
public interface CurrentDocumentRepository extends CrudRepository<CurrentDocument, Long> {
    
    List<CurrentDocument> findByFolder_Id(Long id);
    Page<CurrentDocument> findByFolder_IdAndStatus_StatusNotIn(Long id, List<String> status, Pageable pageable);
   
}
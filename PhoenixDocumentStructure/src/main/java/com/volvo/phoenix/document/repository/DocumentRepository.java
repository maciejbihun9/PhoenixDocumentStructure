package com.volvo.phoenix.document.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.entity.Document;

@Transactional
public interface DocumentRepository extends CrudRepository<Document, Long> {
    
    List<Document> findByFolder_Id(Long id);
    List<Document> findByFolder_IdAndTitleOrderByIdDesc(Long id, String title);
    Page<Document> findByFolder_IdAndStatus_StatusNotIn(Long id, List<String> status, Pageable pageable);
   
}
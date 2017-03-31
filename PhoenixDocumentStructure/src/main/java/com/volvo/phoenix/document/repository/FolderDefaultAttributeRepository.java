package com.volvo.phoenix.document.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.volvo.phoenix.document.entity.FolderDefaultAttribute;

/**
 * 
 */
public interface FolderDefaultAttributeRepository extends  CrudRepository<FolderDefaultAttribute, Long> {
    
   
    List<FolderDefaultAttribute> findById_FolderId(Long folderId);
}

package com.volvo.phoenix.document.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.volvo.phoenix.document.entity.Folder;


public interface FolderRepository extends PagingAndSortingRepository<Folder, Long> {

    List<Folder> findByParentIsNullOrderByTextAsc();

    List<Folder> findByParent_IdOrderByTextAsc(Long id);
    
    List<Folder> findByParent_Id(Long id);
    
    @Query(value = "SELECT f.text FROM Folder f where f.parent.id = :id and f.id != :id")
    List<String> findChildNamesByParentId(@Param("id") Long id);
    
    List<Folder> findByAcl_IdIn(List<Long> acl_Id);

    Page<Folder> findByParentIsNull(Pageable pageable);

    Page<Folder> findByParentId(Long id, Pageable pageable);
    
}

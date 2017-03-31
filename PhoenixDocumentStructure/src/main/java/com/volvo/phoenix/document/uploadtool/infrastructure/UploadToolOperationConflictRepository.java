package com.volvo.phoenix.document.uploadtool.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationConflict;

public interface UploadToolOperationConflictRepository extends JpaRepository<UploadToolOperationConflict, Long>  {
    
    List<UploadToolOperationConflict> findByOperationId(long operationId);
}

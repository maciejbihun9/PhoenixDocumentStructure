package com.volvo.phoenix.document.uploadtool.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;

public interface UploadToolOperationRepository extends JpaRepository<UploadToolOperation, Long>  {}

package com.volvo.phoenix.document.uploadtool.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationFile;

public interface UploadToolOperationFileRepository extends JpaRepository<UploadToolOperationFile, Long>  {}

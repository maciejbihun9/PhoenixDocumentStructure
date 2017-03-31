package com.volvo.phoenix.document.uploadtool.application;

import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationStatus;


/**
 * Manage the lifecycle of Upload tool operations
 */
public interface UploadToolOperationLifecycleService {
    
    UploadToolOperation createOperation(Long folderId);

    void markOperationAsOngoing(Long operationId);

    void closeOperation(Long operationId, UploadToolOperationStatus status);

}

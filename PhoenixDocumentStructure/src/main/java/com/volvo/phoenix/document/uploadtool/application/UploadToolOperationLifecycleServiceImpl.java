package com.volvo.phoenix.document.uploadtool.application;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationRepository;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationStatus;

/**
 * Manage the lifecycle of Upload tool operations
 */
@Service
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class UploadToolOperationLifecycleServiceImpl implements UploadToolOperationLifecycleService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private UploadToolOperationRepository operationRepository;
    
    @Autowired
    private FolderRepository folderRepository;

    
    @Override
    public UploadToolOperation createOperation(Long folderId) {
        UploadToolOperation uto = new UploadToolOperation();
        uto.setCreateDate(new Date());
        uto.setStatus(UploadToolOperationStatus.CREATED);
        uto.setUser(SecurityContextHolder.getContext().getAuthentication().getName());
        uto.setFolder(folderRepository.findOne(folderId));
        return operationRepository.save(uto);
    }

    @Override
    public void markOperationAsOngoing(Long operationId) {
        UploadToolOperation operation  = operationRepository.findOne(operationId);
        operation.setStatus(UploadToolOperationStatus.RUNNING);
        operation.setStartDate(new Date());
        log.info("Apply upload started for operation {}", operationId);
        operationRepository.save(operation);
    }

    @Override
    public void closeOperation(Long operationId, UploadToolOperationStatus status) {
        UploadToolOperation operation = operationRepository.findOne(operationId);
        operation.setStatus(status);
        operation.setFinishDate(new Date());
        log.info("Apply upload finished for operation {} with status {}", operationId, status);
        operationRepository.save(operation);
    }
    
}
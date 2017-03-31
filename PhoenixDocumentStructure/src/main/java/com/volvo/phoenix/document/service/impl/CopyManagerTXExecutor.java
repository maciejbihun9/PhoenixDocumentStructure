package com.volvo.phoenix.document.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.entity.SolutionParam;
import com.volvo.phoenix.document.service.CopyManagerService;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Executes CopyManager service core methods in new transactions
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class CopyManagerTXExecutor {

    @Autowired CopyManagerService service;

    public Long copyDocumentTX(OrionCMContext ctx, long documentId, long targetFolderId, List<SolutionParam> solutions) throws PhoenixDataAccessException, IOException {
        return service.copyDocument(ctx, documentId, targetFolderId, solutions);
    }
    
    public void moveDocumentTX(long documentId, long targetFolderId, List<SolutionParam> solutions) {
        service.moveDocument(documentId, targetFolderId, solutions);
    }
    
    public void copyFolderTX(OrionCMContext ctx, Long sourceId, Long targetFolderId, List<SolutionParam> solutions) throws PhoenixDataAccessException, IOException {
        service.copyFolder(ctx, sourceId, targetFolderId, solutions);
    }
    
    public void moveFolderTX(Long sourceId, Long targetFolderId, List<SolutionParam> solutions) {
        service.moveFolder(sourceId, targetFolderId, solutions);
    }
    
}


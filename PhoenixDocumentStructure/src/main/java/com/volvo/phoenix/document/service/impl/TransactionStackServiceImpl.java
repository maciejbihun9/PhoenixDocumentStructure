package com.volvo.phoenix.document.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.volvo.phoenix.document.datatype.InfoClass;
import com.volvo.phoenix.document.datatype.TransactionStackEventType;
import com.volvo.phoenix.document.datatype.TransactionStackSystemType;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentStatus;
import com.volvo.phoenix.document.entity.TransactionStack;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.TransactionStackRepository;
import com.volvo.phoenix.document.service.TransactionStackService;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.service.OrionDocumentService;

/**
 * Implementation of {@link TransactionStackService}.
 *
 * @author bpl3195
 */
@Service
public class TransactionStackServiceImpl implements TransactionStackService {
    
    private static final String STATUS_VALID         = "VALID";
    private static final String STATUS_WORK          = "WORK";

    @Autowired
    private OrionDocumentService orionDocumentService;

    @Autowired
    private TransactionStackRepository repository;
    
    @Autowired
    private DocumentRepository documentRepository;

    
    @Override
    public void documentCreated(Long documentId) {
        Assert.notNull(documentId, "The parameter 'documentId' cannot be null.");
        
        OrionDocumentDTO orionDocument = orionDocumentService.findDocument(documentId);
        Document document = documentRepository.findOne(documentId);
        
        if (isDocumentLatestValidOrLatestInWorkNotProtected(orionDocument.getRegistrationNumber(), orionDocument.getRevision(), document.getStatus(),  convertToBoolean(document.getProtectInWork()))) {
            if ((orionDocument.getAclState().getId().intValue() != InfoClass.STRICTLY_CONFIDENTIAL.getId())) {
                storeTransactionStackEvent(documentId, TransactionStackEventType.A, TransactionStackSystemType.E);
            }
            storeTransactionStackEvent(documentId, TransactionStackEventType.A, TransactionStackSystemType.S);
        }
    }

    @Override
    public void newVersionCreated(Long newVersionDocumentId, Long previousVersionDocumentId) {
        documentCreated(newVersionDocumentId);

        OrionDocumentDTO prevOrionDocument = orionDocumentService.findDocument(previousVersionDocumentId);

        if ((prevOrionDocument.getAclState().getId().intValue() != InfoClass.STRICTLY_CONFIDENTIAL.getId())) {
            storeTransactionStackEvent(previousVersionDocumentId, TransactionStackEventType.D, TransactionStackSystemType.E);
        }
        storeTransactionStackEvent(previousVersionDocumentId, TransactionStackEventType.D, TransactionStackSystemType.S);
    }
    
    /**
     * {@inheritDoc}.
     */
    @Override
    public void documentCopied(Long documentId) {
        documentCreated(documentId);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void documentMoved(Long documentId) {
        Assert.notNull(documentId, "The parameter 'documentId' cannot be null.");
        
        OrionDocumentDTO orionDocument = orionDocumentService.findDocument(documentId);
        Document document = documentRepository.findOne(documentId);
        
        if (isDocumentLatestValidOrLatestInWorkNotProtected(orionDocument.getRegistrationNumber(), orionDocument.getRevision(), document.getStatus(), convertToBoolean(document.getProtectInWork()))) {
            if (orionDocument.getAclState().getId().intValue() != InfoClass.STRICTLY_CONFIDENTIAL.getId()) {
                storeTransactionStackEvent(documentId, TransactionStackEventType.U, TransactionStackSystemType.E);
            }
            storeTransactionStackEvent(documentId, TransactionStackEventType.U, TransactionStackSystemType.S);
        }
    }

    private boolean convertToBoolean(String protectInWork) {
        return protectInWork != null && protectInWork.equals("Y");
    }

    private boolean isDocumentLatestValidOrLatestInWorkNotProtected(String registrationNumber, String revision, DocumentStatus status, boolean protectInWork) {
        if (status.getStatus().equals(STATUS_VALID) || 
            (status.getStatus().equals(STATUS_WORK) && !protectInWork)) {
            
            String latestRevision = repository.getLatestRevisionValidOrInWorkNotProtected(registrationNumber);
            return (latestRevision == null || revision == null || Long.parseLong(revision) >= Long.parseLong(latestRevision));
        }
        return false;
    }
    
    private void storeTransactionStackEvent(Long documentId, TransactionStackEventType eventType, TransactionStackSystemType systemType) {
        TransactionStack event = new TransactionStack();
        event.setDocumentId(documentId);
        event.setEventDate(new Date());
        event.setTransactionType(eventType);
        event.setSystemType(systemType);
        repository.save(event);
    }

}

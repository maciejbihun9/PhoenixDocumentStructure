package com.volvo.phoenix.document.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.volvo.phoenix.document.entity.DocumentOperation;
import com.volvo.phoenix.document.entity.Operation;

@Service
public class DocumentOperationFactory implements OperationFactory {

    public DocumentOperation createDocumentOperation(long sourceDocumentId, long targetFolderId, String user) {
        DocumentOperation op = new DocumentOperation();
        op.setSourceDocumenId(sourceDocumentId);
        op.setTargetFolderId(targetFolderId);
        op.setUser(user);
        op.setCreateDate(new Date());
        return op;
    }

    @Override
    public Operation createOperation(long sourceId, long targetId, String user) {
        return createDocumentOperation(sourceId, targetId, user);
    }

}

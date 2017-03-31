package com.volvo.phoenix.document.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.volvo.phoenix.document.entity.FolderOperation;
import com.volvo.phoenix.document.entity.Operation;

@Service
public class FolderOperationFactory implements OperationFactory {

    public FolderOperation createFolderOperation(long sourceFolderId, long targetFolderId, String user) {
        FolderOperation op = new FolderOperation();
        op.setSourceFolderId(sourceFolderId);
        op.setTargetFolderId(targetFolderId);
        op.setUser(user);
        op.setCreateDate(new Date());
        return op;
    }

    @Override
    public Operation createOperation(long sourceId, long targetId, String user) {
        return createFolderOperation(sourceId, targetId, user);
    }

}

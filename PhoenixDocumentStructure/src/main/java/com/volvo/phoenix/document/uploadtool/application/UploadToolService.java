package com.volvo.phoenix.document.uploadtool.application;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolDocumentAttributeValueDTO;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolOperationConflictDTO;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolTreeNodeDTO;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationFile;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationTreeNode;

/**
 * Upload tool main application service.
 */
public interface UploadToolService {

    UploadToolOperation createOperation(Long folderId);

    @PreAuthorize("hasPermission(#operationId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperation', 'canModify')")
    UploadToolOperation getOperation(Long operationId);
    
    @PreAuthorize("hasPermission(#operationId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperation', 'canModify')")
    void applyUpload(Long operationId) throws Exception;

    @PreAuthorize("hasPermission(#operationId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperation', 'canModify')")
    String getStoreLocation(Long operationId);

    @PreAuthorize("hasPermission(#operationId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperation', 'canModify')")
    void addUploadedFilesInformation(Long operationId, List<UploadToolOperationFile> uploadedFiles);

    @PreAuthorize("hasPermission(#operationId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperation', 'canModify')")
    void addDocumentTree(Long operationId, List<UploadToolTreeNodeDTO> uploadedDocumentTree);

    void validateDocumentTree(Long operationId);
    
    Boolean checkStatusOfDocumentTree(Long operationId);

    @PreAuthorize("hasPermission(#operationId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperation', 'canModify')")
    List<String> getUploadedZIPFilesWithNoMetadata(Long operationId);

//    @PreAuthorize("hasPermission(#nodeId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperationTreeNode', 'canModify')")
    UploadToolTreeNodeDTO geOperationTreeNodeDTO(long nodeId);

    @PreAuthorize("hasPermission(#operationId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperation', 'canModify')")
    List<UploadToolTreeNodeDTO> getOperationTreeRootNodes(long operationId);

    List<UploadToolOperationTreeNode> getOperationTreeChildNodes(long parentNodeId);

    List<UploadToolOperationConflictDTO> getConflictsForOperation(long parentNodeId);
    
    int getTotalNumberOfDocuments(Long operation);

    List<UploadToolTreeNodeDTO> getDocumentAttachments(long operationId);

    List<UploadToolDocumentAttributeValueDTO> getDocumentOptionalAttrValues(long nodeId);

    List<UploadToolDocumentAttributeValueDTO> getDocumentMandatoryAttrValues(long nodeId);

  

}

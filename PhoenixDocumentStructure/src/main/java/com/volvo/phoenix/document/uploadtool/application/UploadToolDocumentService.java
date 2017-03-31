package com.volvo.phoenix.document.uploadtool.application;

import java.io.IOException;
import java.util.List;

import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.Family;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.PhoenixAcl;
import com.volvo.phoenix.document.uploadtool.application.dto.ValidateDocumentResultDTO;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocument;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationTreeNode;
import com.volvo.phoenix.orion.dto.OrionUserDTO;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Manage validated Upload Tool documents
 */
public interface UploadToolDocumentService {

    ValidateDocumentResultDTO createAndValidateDocument(UploadToolOperation operation, UploadToolOperationTreeNode node, List<Family> families, Domain domain, OrionUserDTO userDTO);
    
    int getUploadToolValidatedDocumentsNumber(Long operationId);
    
    /**
     * Create Phoenix document based on Upload Tool document
     */
    Document createPhoenixDocument(OrionCMContext orionContext, UploadToolOperationTreeNode node, Folder destinationFolder, List<String> attachments) throws PhoenixDataAccessException, IOException;
    
    Folder createPhoenixFolder(UploadToolOperationTreeNode utFolder, Folder parentPnxFolder, PhoenixAcl acl, OrionUserDTO userDTO);
    
    UploadToolDocument getUploadToolDocumentByRootNode(long documentId);
}

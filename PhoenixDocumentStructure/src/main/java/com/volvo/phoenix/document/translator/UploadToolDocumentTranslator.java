package com.volvo.phoenix.document.translator;

import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolDocumentDTO;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocument;

public class UploadToolDocumentTranslator {

    public static UploadToolDocumentDTO toDTO(UploadToolDocument uploadToolDocument){
        
        UploadToolDocumentDTO documentDTO = new UploadToolDocumentDTO();
        documentDTO.setRevision(uploadToolDocument.getRevision() == null ? null : uploadToolDocument.getRevision());
        documentDTO.setName(uploadToolDocument.getName() == null ? null : uploadToolDocument.getName());
        documentDTO.setTitle(uploadToolDocument.getTitle() == null ? null : uploadToolDocument.getTitle());
        documentDTO.setAltDocId(uploadToolDocument.getAltDocId() == null ? null : uploadToolDocument.getAltDocId());
        documentDTO.setAuthor(uploadToolDocument.getAuthor() == null ? null : uploadToolDocument.getAuthor());
        documentDTO.setAuthorId(uploadToolDocument.getAuthorId() == null ? null : uploadToolDocument.getAuthorId());
        documentDTO.setDescription(uploadToolDocument.getDescription() == null ? null : uploadToolDocument.getDescription());
        documentDTO.setIssueDate(uploadToolDocument.getIssueDate() == null ? null : uploadToolDocument.getIssueDate());
        documentDTO.setIssuer(uploadToolDocument.getIssuer() == null ? null : uploadToolDocument.getIssuer());
        documentDTO.setIssuerId(uploadToolDocument.getIssuerId() == null ? null : uploadToolDocument.getIssuerId());
        documentDTO.setNotes(uploadToolDocument.getNotes() == null ? null : uploadToolDocument.getNotes());
        documentDTO.setProtectInWork(uploadToolDocument.getProtectInWork() == null ? null : uploadToolDocument.getProtectInWork());
        documentDTO.setStateId(uploadToolDocument.getStateId() == null ? null : uploadToolDocument.getStateId());
        documentDTO.setStatus(uploadToolDocument.getStatus() == null ? null : uploadToolDocument.getStatus());
        documentDTO.setFamily(uploadToolDocument.getFamily() == null ? null : uploadToolDocument.getFamily().getName());
        documentDTO.setType(uploadToolDocument.getType() == null ? null : uploadToolDocument.getType().getName());
       
        return documentDTO;
    }
    
}

package com.volvo.phoenix.orion.service;

import java.io.File;
import java.util.List;

import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.dto.OrionFileDTO;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Access Orion storage API
 */
public interface OrionStorageService {
    
    OrionAclDTO createAcl(String aclName);

    void checkinNewDocument(OrionDocumentDTO doc, OrionCMContext octxt) throws Exception;

    List<File> exportFiles(String documentName, String revision, List<OrionFileDTO> files, OrionCMContext octxt, String dirName) throws Exception;

    void checkinNewFile(String objectName, String objectRev, Long aclId, Long stateId, File file, String sheetName, OrionCMContext octxt) throws Exception;

}

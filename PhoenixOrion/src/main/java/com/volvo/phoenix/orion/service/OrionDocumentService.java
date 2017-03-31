package com.volvo.phoenix.orion.service;

import java.io.IOException;
import java.util.List;

import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Orion service to handle document related function
 * 
 * @author v0cn181
 */
public interface OrionDocumentService {

    /**
     * find document by id
     * 
     * @param id unique document id(OBJ_ID)
     * @return Orion document information
     */
    OrionDocumentDTO findDocument(Long id);
    
    /**
     * find document name by id
     * 
     * @param id unique document id(OBJ_ID)
     * @return Orion document name
     */
    String getDocumentName(long documentId);
    
    /**
     * find folder size by nodeIds
     * 
     * @param nodeIds
     * @return Orion folder size
     */
    List<Object[]> getEachFolderSizeByNodeId(List<Long> nodeIds);
    
    /**
     * find document size by objIds
     * 
     * @param objIds
     * @return Orion document size
     */
    List<Object[]> getEachDocumentSizeByObjId(List<Long> objIds);
    
    /**
     * find all revisions of certain document by name (registration number)
     * 
     * @param name of document
     * @return Orion documents information
     */
    List<OrionDocumentDTO> findAllDocumentRevisions(String name);

    /**
     * find folder size by nodeIds
     * 
     * @param nodeIds
     * @return Orion folder size
     */
    Long getFoldersSizeByNodeId(List<Long> nodeIds);

    /**
     * find document size by objIds
     * 
     * @param objIds
     * @return Orion document size
     */
    Long getDocumentsSizeByObjId(List<Long> objIds);

    /**
     * update the access right of the document by connect it to a new acl
     * @param docId document id(OBJ_ID)
     * @param aclId new acl_id 
     */
    void updateDocumentAcl(Long docId, Long aclId);

    /**
     * Copy a document with/wo attached files
     * 
     * @param orionContext
     *            Orion Context
     * @param documentDTO
     *            copied document information
     * @return ID of copied document
     * @throws PhoenixDataAccessException 
     * @throws IOException 
     */
    Long copyDocument(OrionCMContext orionContext, OrionDocumentDTO documentDTO) throws PhoenixDataAccessException, IOException;
    
    /**
     * Create a document with/wo attached files
     * 
     * @param orionContext
     *            Orion Context
     * @param documentDTO
     *            copied document information
     * @return ID of copied document
     * @throws PhoenixDataAccessException 
     * @throws IOException 
     */
    Long createDocument(OrionCMContext orionContext, OrionDocumentDTO documentDTO) throws PhoenixDataAccessException, IOException;
    
    Long createNewVersion(OrionCMContext orionContext, OrionDocumentDTO orionDocDTO) throws PhoenixDataAccessException, IOException;

    OrionAclDTO createAcl(String nextAclName);

    OrionDocumentDTO findLatestDocumentRevision(String name);

}

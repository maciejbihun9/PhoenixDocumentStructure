package com.volvo.phoenix.document.service;

import java.io.IOException;
import java.util.List;

import com.volvo.phoenix.document.datatype.ItemType;
import com.volvo.phoenix.document.datatype.OperationType;
import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.DocumentTypeDTO;
import com.volvo.phoenix.document.dto.FamilyDTO;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.dto.SolutionParamDTO;
import com.volvo.phoenix.document.entity.SolutionParam;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Service to plan copy/move operations, check conflicts.
 */
public interface CopyManagerService {

    /**
     * Verifies if operation on document or folder causes any problem described by any of {@link ConflictRule} implementaion classes.
     *
     * @param operation
     * @return list of conflicts or empty list if no conflict is detected
     */
    List<ConflictDTO> checkConflicts(OperationDTO operation);

    /**
     * Gets operation by ID.
     *
     * @param id
     * @return
     */
    OperationDTO findOperation(Long id);

    /**
     * Remove operation from persistent storage. If operaion has not been performed it is also removed from pending operations queue.
     *
     * @param id
     * @return
     */
    Boolean removeOperation(Long id);

    /**
     * Create and persist operaion for copying/moving source document/folder to target folder.
     *
     * @param sourceType
     *            whether source is of document/folder type
     * @param sourceId
     *            source document/folder ID
     * @param targetId
     *            target folder ID to which source document/folder has to be moved/copied
     * @param userVcnId
     *            author that triggers operation
     * @return created operation
     * @throws LockFailedException
     *             if something locks possibility for creating operation i.e. target folder is subject of another ongoing operation
     */
    OperationDTO createOperation(ItemType sourceType, long sourceId, long targetId, String userVcnId) throws LockFailedException;

    /**
     * Get all active (not performed) operations triggered by given user.
     *
     * @param userVcnId
     * @return
     */
    List<OperationDTO> getUserCreatedOperations(String userVcnId);

    void saveSolutionParams(List<SolutionParamDTO> params);

    List<OperationDTO> getOperationsHistory(String user);

    /**
     * Schedule all current user operations that have no conflicts to be performed as copy operations by operations job executor.
     *
     * @return scheduled operaions or empty list if no operation created or all created operations have conflicts
     */
    List<OperationDTO> scheduleCopyOperations();

    /**
     * Schedule requested operation.
     * 
     * @param operationId
     * @param operationType
     */
    void scheduleOperation(Long operationId, OperationType operationType);

    /**
     * Schedule all current user operations that have no conflicts to be performed as move operations by operations job executor.
     *
     * @return scheduled operaions or empty list if no operation created or all created operations have conflicts
     */
    List<OperationDTO> scheduleMoveOperations();

    List<ConflictDTO> checkConflicts();

    /**
     * Move a single document between folders in document tree
     * 
     * @param documentId
     *            id of document to be moved
     * @param targetFolderNodeId
     *            node id of destination folder
     * @param solutions
     *            list of solutions for conflits resolution
     */
    void moveDocument(long documentId, long targetFolderNodeId, List<SolutionParam> solutions);

    /**
     * Copy a single document between folders (latest revision)
     * 
     * @param ctx
     *            Orion context
     * @param documentId
     *            id of latest revision of document to copy
     * @param targetFolderNodeId
     *            node id of destination folder
     * @param solutions
     *            list of solutions for conflits resolution
     * @return id of copied document
     * @throws IOException
     * @throws PhoenixDataAccessException
     */
    Long copyDocument(OrionCMContext ctx, long documentId, long targetFolderNodeId, List<SolutionParam> solutions) throws PhoenixDataAccessException,
            IOException;

    /**
     * Copy a folder with all folder content (documents and subfolders)
     * 
     * @param ctx
     *            Orion context
     * @param sourceId
     *            id of source folder
     * @param targetFolderNodeId
     *            node id of destination folder
     * @param solutions
     *            list of solutions for conflits resolution
     * @throws IOException
     * @throws PhoenixDataAccessException
     */
    void copyFolder(OrionCMContext ctx, Long sourceId, Long targetFolderId, List<SolutionParam> solutions) throws PhoenixDataAccessException, IOException;

    /**
     * Move a folder with all folder content (documents and subfolders)
     * 
     * @param sourceId
     *            id of source folder
     * @param targetFolderNodeId
     *            node id of destination folder
     * @param solutions
     *            list of solutions for conflits resolution
     */
    void moveFolder(Long sourceId, Long targetFolderId, List<SolutionParam> solutions);
    
    FamilyDTO getFamilyForDocumentTypeId(Long documentTypeId);
    
    List<DocumentTypeDTO> getFamilyDocumentTypes(Long familyId, Long operationId);

}

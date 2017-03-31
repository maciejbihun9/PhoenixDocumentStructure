package com.volvo.phoenix.orion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.volvo.phoenix.orion.entity.OrionDocument;



/**
 * To get Orion document information, especially the acl_id and state_id.
 * 
 * @author v0cn181
 */
public interface OrionDocumentRepository extends JpaRepository<OrionDocument, Long> {

    @Query(value="SELECT VT_PHOENIX_DOCUMENT_SEQ.nextval FROM dual", nativeQuery=true)
    Long getNextDocumentRegistrationNumber();

    List<OrionDocument> findByName(String name);
    
    OrionDocument findFirstByNameOrderByRevisionDesc(String name);

    @Query(value="SELECT name FROM DM_OBJECT WHERE obj_id = :documentId", nativeQuery=true)
    String getDocumentName(@Param("documentId") long documentId);
    
    @Query(value=" SELECT T_tree.Id, NVL(SUM(DM_FILE.File_SIZE),0) File_SIZE "
                +"FROM (SELECT CONNECT_BY_ROOT(NODE_ID) Id, NODE_ID FROM VT_PHOENIX_TREE CONNECT BY NODE_PARENT_ID = PRIOR NODE_ID "
                +"      START WITH NODE_ID IN (:nodeIds)) T_tree "
                +" JOIN VT_PHOENIX_DOCUMENT ON VT_PHOENIX_DOCUMENT.NODE_ID = T_tree.NODE_ID "
                +"      AND VT_PHOENIX_DOCUMENT.DOCUMENT_STATUS IN ('VALID','WORK') "
                +" JOIN DM_OBJECT ON DM_OBJECT.OBJ_ID = VT_PHOENIX_DOCUMENT.OBJ_ID "
                +"      AND DM_OBJECT.CUR = 'Y' "
                +" JOIN DM_COMPONENT ON DM_COMPONENT.OBJ_ID = DM_OBJECT.OBJ_ID "
                +" JOIN DM_REPRESENTATION ON DM_REPRESENTATION.ID = DM_COMPONENT.COMP_ID "
                +" JOIN DM_REPRESENTATION_FILE ON DM_REPRESENTATION_FILE.REP_ID = DM_REPRESENTATION.REP_ID "
                +" JOIN DM_FILE ON DM_FILE.FILE_ID = DM_REPRESENTATION_FILE.FILE_ID "
                +"GROUP BY T_tree.Id", nativeQuery=true)
    List<Object[]> getEachFolderSizeByNodeId(@Param("nodeIds") List<Long> nodeIds);
    
    @Query(value="SELECT VT_PHOENIX_DOCUMENT.OBJ_ID Id, NVL(SUM(DM_FILE.File_SIZE),0) File_SIZE  "
                +"FROM  VT_PHOENIX_DOCUMENT "
                +" JOIN DM_OBJECT ON DM_OBJECT.OBJ_ID = VT_PHOENIX_DOCUMENT.OBJ_ID   "
                +"      AND DM_OBJECT.CUR = 'Y' "
                +" JOIN DM_COMPONENT ON DM_COMPONENT.OBJ_ID = DM_OBJECT.OBJ_ID "
                +" JOIN DM_REPRESENTATION ON DM_REPRESENTATION.ID = DM_COMPONENT.COMP_ID  "
                +" JOIN DM_REPRESENTATION_FILE ON DM_REPRESENTATION_FILE.REP_ID = DM_REPRESENTATION.REP_ID "
                +" JOIN DM_FILE ON DM_FILE.FILE_ID = DM_REPRESENTATION_FILE.FILE_ID "
                +"WHERE VT_PHOENIX_DOCUMENT.DOCUMENT_STATUS IN ('VALID','WORK') AND VT_PHOENIX_DOCUMENT.OBJ_ID IN (:objIds) "
                +"GROUP BY VT_PHOENIX_DOCUMENT.OBJ_ID", nativeQuery=true)
    List<Object[]> getEachDocumentSizeByObjId(@Param("objIds") List<Long> objIds);

    @Query(value=" SELECT NVL(SUM(DM_FILE.File_SIZE),0) File_SIZE "
                +"FROM (SELECT CONNECT_BY_ROOT(NODE_ID) Id, NODE_ID FROM VT_PHOENIX_TREE CONNECT BY NODE_PARENT_ID = PRIOR NODE_ID "
                +"      START WITH NODE_ID IN (:nodeIds)) T_tree "
                +" JOIN VT_PHOENIX_DOCUMENT ON VT_PHOENIX_DOCUMENT.NODE_ID = T_tree.NODE_ID "
                +"      AND VT_PHOENIX_DOCUMENT.DOCUMENT_STATUS IN ('VALID','WORK') "
                +" JOIN DM_OBJECT ON DM_OBJECT.OBJ_ID = VT_PHOENIX_DOCUMENT.OBJ_ID "
                +"      AND DM_OBJECT.CUR = 'Y' "
                +" JOIN DM_COMPONENT ON DM_COMPONENT.OBJ_ID = DM_OBJECT.OBJ_ID "
                +" JOIN DM_REPRESENTATION ON DM_REPRESENTATION.ID = DM_COMPONENT.COMP_ID "
                +" JOIN DM_REPRESENTATION_FILE ON DM_REPRESENTATION_FILE.REP_ID = DM_REPRESENTATION.REP_ID "
                +" JOIN DM_FILE ON DM_FILE.FILE_ID = DM_REPRESENTATION_FILE.FILE_ID "
                , nativeQuery=true)
    Long getFoldersSizeByNodeId(@Param("nodeIds") List<Long> nodeIds);

    @Query(value="SELECT NVL(SUM(DM_FILE.File_SIZE),0) File_SIZE  "
                +"FROM  VT_PHOENIX_DOCUMENT "
                +" JOIN DM_OBJECT ON DM_OBJECT.OBJ_ID = VT_PHOENIX_DOCUMENT.OBJ_ID   "
                +"      AND DM_OBJECT.CUR = 'Y' "
                +" JOIN DM_COMPONENT ON DM_COMPONENT.OBJ_ID = DM_OBJECT.OBJ_ID "
                +" JOIN DM_REPRESENTATION ON DM_REPRESENTATION.ID = DM_COMPONENT.COMP_ID  "
                +" JOIN DM_REPRESENTATION_FILE ON DM_REPRESENTATION_FILE.REP_ID = DM_REPRESENTATION.REP_ID "
                +" JOIN DM_FILE ON DM_FILE.FILE_ID = DM_REPRESENTATION_FILE.FILE_ID "
                +"WHERE VT_PHOENIX_DOCUMENT.DOCUMENT_STATUS IN ('VALID','WORK') AND VT_PHOENIX_DOCUMENT.OBJ_ID IN (:objIds) "
                , nativeQuery=true)
    Long getDocumentsSizeByObjId(@Param("objIds") List<Long> objIds);
}

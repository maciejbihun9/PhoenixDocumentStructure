package com.volvo.phoenix.document.uploadtool.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.volvo.phoenix.document.uploadtool.model.UploadToolDocument;

public interface UploadToolDocumentRepository extends JpaRepository<UploadToolDocument, Long> {
    
    @Query(value = "SELECT d FROM UploadToolDocument d where d.treeNode.nodeId = :nodeId")
    UploadToolDocument findDocByTreeNodeId(@Param("nodeId") Long nodeId);
    
    @Query(value="select count(*) from V0PHOENIX.UT_DOCUMENT where tree_node_id in "
            + "(select node_id from ut_operation_tree where operation_id = :operationId)", nativeQuery=true)
    int getValidatedDocumentsNumberForOperation(@Param("operationId") Long operationId);
    
}

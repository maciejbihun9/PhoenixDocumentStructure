package com.volvo.phoenix.document.uploadtool.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.volvo.phoenix.document.uploadtool.model.UploadToolNodeType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationTreeNode;

public interface UploadToolOperationTreeNodeRepository extends JpaRepository<UploadToolOperationTreeNode, Long>  {
    
    List<UploadToolOperationTreeNode> findByOperationIdAndParentNodeIsNull(long operationId);
    
    @Query(value="SELECT * FROM UT_OPERATION_TREE WHERE NODE_PARENT_ID = :node_parent_id and node_type in('S', 'D')", nativeQuery=true)
    List<UploadToolOperationTreeNode> getOperationTreeChildNodes(@Param("node_parent_id") long node_parent_id);
    
    UploadToolOperationTreeNode findByNodeId(long nodeId);
    
    int countByOperationIdAndNodeType(long operationId, UploadToolNodeType nodeType);
    
    @Modifying
    @Query(value="UPDATE UT_OPERATION_TREE SET phoenix_obj_ref = :phoenixObjectRef WHERE NODE_ID = :nodeId", nativeQuery=true)
    void updatePhoenixObjectReference(@Param("nodeId") long nodeId, @Param("phoenixObjectRef") long pnxObjRef);

    @Query(value="SELECT * FROM UT_OPERATION_TREE WHERE NODE_PARENT_ID = :node_parent_id and node_type in('F')", nativeQuery=true)
    List<UploadToolOperationTreeNode> getDocumentAttachments(@Param("node_parent_id") long node_parent_id);
}

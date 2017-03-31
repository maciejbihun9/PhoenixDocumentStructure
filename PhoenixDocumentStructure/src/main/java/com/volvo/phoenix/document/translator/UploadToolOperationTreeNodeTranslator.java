package com.volvo.phoenix.document.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolTreeNodeDTO;
import com.volvo.phoenix.document.uploadtool.model.UploadToolNodeType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationTreeNode;

public class UploadToolOperationTreeNodeTranslator {

    private static final String METADATA_XLSX = "metadata.xlsx";
    private static final String FIRST_VERSION = "1";

    public static List<UploadToolTreeNodeDTO> translateToTreeNodesDtos(List<UploadToolOperationTreeNode> treeNodes) {
        List<UploadToolTreeNodeDTO> treeNodesDTOs = new ArrayList<UploadToolTreeNodeDTO>();
        for (UploadToolOperationTreeNode treeNode : treeNodes) {
            treeNodesDTOs.add(translateToTreeNodeDto(treeNode));
        }
        return treeNodesDTOs;

    }

    public static UploadToolTreeNodeDTO translateToTreeNodeDto(UploadToolOperationTreeNode node) {
        UploadToolTreeNodeDTO dto = new UploadToolTreeNodeDTO();
        dto.setId(node.getNodeId());
        dto.setName(node.getNodeText());
        if (node.getNodeType() == UploadToolNodeType.D && node.getDocumentAttributes().get("Title") != null) {
            dto.setName(node.getDocumentAttributes().get("Title"));
        } else {
            dto.setName(node.getNodeText());
        }
        if (node.getParentNode() != null) {
            dto.setParentId(node.getParentNode().getNodeId());
        }
        dto.setValid(node.getValid());
        dto.setNewVersion(node.getRev() != null && node.getRev().compareTo(FIRST_VERSION) > 0);
        dto.setNodeType(node.getNodeType());
        return dto;
    }

    public static List<UploadToolOperationTreeNode> translateUploadToolTreeNodeDTOList(List<UploadToolTreeNodeDTO> uploadedDocumentTree,
            UploadToolOperationTreeNode parentNode) {
        List<UploadToolOperationTreeNode> result = new ArrayList<UploadToolOperationTreeNode>();
        for (UploadToolTreeNodeDTO nodeDTO : uploadedDocumentTree) {
            // ignore metadata.xlsx nodes
            if (nodeDTO.getName().equalsIgnoreCase(METADATA_XLSX)) {
                continue;
            }
            UploadToolOperationTreeNode node = new UploadToolOperationTreeNode();
            node.setNodeText(nodeDTO.getName());
            node.setAbsolutePath(nodeDTO.getAbsolutePath());
            node.setNodeType(nodeDTO.getNodeType());
            node.setParentNode(parentNode);
            if (nodeDTO.getDocument() != null) {
                addNotEmptyDocumentProperties(nodeDTO.getDocument().getDocumentProperties(), node);
            }
            node.getChildNodes().addAll(translateUploadToolTreeNodeDTOList(nodeDTO.getChildren(), node));
            
            result.add(node);
        }
        return result;
    }

    private static void addNotEmptyDocumentProperties(Map<String, String> properties, UploadToolOperationTreeNode node) {
        Map<String, String> nodeProps = new HashMap<String, String>();
        for (String key : properties.keySet()) {
            if (key != null && !key.isEmpty()) {
                nodeProps.put(key, properties.get(key));
            }
        }
        node.setDocumentAttributes(nodeProps);
    }
    
}

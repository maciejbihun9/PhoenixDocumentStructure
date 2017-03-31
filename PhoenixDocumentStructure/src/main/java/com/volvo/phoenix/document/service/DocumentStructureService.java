package com.volvo.phoenix.document.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.volvo.phoenix.document.dto.PhoenixDocumentDTO;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.dto.TreeNodesDTO;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.PhoenixAcl;
import com.volvo.phoenix.orion.dto.OrionAclDTO;

/**
 * Operations to display documents and folders structure as a tree.
 */
public interface DocumentStructureService {

    TreeNodeDTO folderTree(final Long nodeId);

    TreeNodesDTO getTreeNodes(final Long nodeId, final int page, final int howMany, Sort sort);

    void connectDoctypeToDomain(final Long doctype, final Long domain);

    void changeDocumentDocType(final Long docId, final Long doctype);

    PhoenixAcl createAcl(final OrionAclDTO acl, final Domain domain);

    Folder createFolder(final Folder newFolder);
    
    Document createDocument(PhoenixDocumentDTO documentDTO);

    String getRootfoldersForBusinessAdmin(String userId);
    
    Long getfolderSize(final List<Long> docId, final List<Long> folderId);
}

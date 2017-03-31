package com.volvo.phoenix.document.translator;

import com.volvo.phoenix.document.datatype.ItemType;
import com.volvo.phoenix.document.dto.TreeNodeDTO;

/**
 * Supportive service to resolve node.
 */
public interface NodeResolver {

    TreeNodeDTO resolveDocumentNode(Long documenId);

    TreeNodeDTO resolveFolderNode(Long folderId);

    TreeNodeDTO resolveNode(ItemType sourceType, Long sourceId);

}

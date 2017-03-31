package com.volvo.phoenix.document.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.datatype.ItemType;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.translator.DocumentTranslator;
import com.volvo.phoenix.document.translator.FolderTranslator;
import com.volvo.phoenix.document.translator.NodeResolver;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class NodeResolverImpl implements NodeResolver {

    @Autowired
    private FolderTranslator folderTranslator;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentTranslator documentTranslator;

    @Override
    public TreeNodeDTO resolveDocumentNode(Long documentId) {
        return documentTranslator.translateToTreeNodeDto(documentRepository.findOne(documentId));
    }

    @Override
    public TreeNodeDTO resolveFolderNode(Long folderId) {
        return folderTranslator.translateToTreeNodeDto(folderRepository.findOne(folderId));
    }

    @Override
    public TreeNodeDTO resolveNode(ItemType sourceType, Long sourceId) {
        if (ItemType.DOCUMENT == sourceType) {
            return resolveDocumentNode(sourceId);
        } else if (ItemType.FOLDER == sourceType) {
            return resolveFolderNode(sourceId);
        } else {
            throw new IllegalArgumentException("Unknown item type: " + sourceType);
        }
    }

}

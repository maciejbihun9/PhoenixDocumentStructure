package com.volvo.phoenix.document.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.PhoenixDocumentDTO;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.dto.TreeNodesDTO;
import com.volvo.phoenix.document.entity.CurrentDocument;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.PhoenixAcl;
import com.volvo.phoenix.document.repository.CurrentDocumentRepository;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.DocumentTypeRepository;
import com.volvo.phoenix.document.repository.DomainRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.repository.PhoenixAclRepository;
import com.volvo.phoenix.document.service.DocumentStructureService;
import com.volvo.phoenix.document.translator.DocumentTranslator;
import com.volvo.phoenix.document.translator.FolderTranslator;
import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.service.OrionDocumentService;
import com.volvo.phoenix.orion.service.OrionSecurityService;

@Service
@Transactional
public class DocumentStructureServiceImpl implements DocumentStructureService {

    private static final String ROOT_ID = "treeView";

    @Autowired
    private FolderRepository folderRepo;

    @Autowired
    private DocumentRepository docRepo;
    @Autowired
    private CurrentDocumentRepository currentDocRepo;

    @Autowired
    private PhoenixAclRepository aclRepo;

    @Autowired
    private OrionDocumentService orionDocService;

    @Autowired
    private OrionSecurityService orionSecurityService;

    @Autowired
    private DomainRepository domainRepo;

    @Autowired
    private DocumentTypeRepository doctypeRepo;
    @Autowired
    private DocumentTranslator documentTranslator;
    @Autowired
    private FolderTranslator folderTranslator;

    @Override
    public TreeNodeDTO folderTree(Long nodeId) {
        TreeNodeDTO node = new TreeNodeDTO();

        if (null == nodeId) {
            node.setName(ROOT_ID);
            node.setChildren(folderTranslator.translateToTreeNodeDto(folderRepo.findByParentIsNullOrderByTextAsc()));
        } else {
            node.setId(nodeId);
            node.setChildren(folderTranslator.translateToTreeNodeDto(folderRepo.findByParent_IdOrderByTextAsc(nodeId)));
        }

        return node;
    }

    @Override
    @Transactional
    public void connectDoctypeToDomain(Long doctypeId, Long domainId) {
        Domain domain = domainRepo.findByIdWithDoctypes(domainId);
        DocumentType doctype = doctypeRepo.findOne(doctypeId);
        domain.getDocumentTypes().add(doctype);
        domainRepo.save(domain);
    }

    @Override
    public void changeDocumentDocType(Long docId, Long doctypeId) {
        Document doc = docRepo.findOne(docId);
        DocumentType doctype = doctypeRepo.findOne(doctypeId);
        doc.setType(doctype);
        docRepo.save(doc);
    }

    @Override
    public PhoenixAcl createAcl(OrionAclDTO acl, Domain domain) {
        PhoenixAcl pnxAcl = new PhoenixAcl();
        pnxAcl.setDomain(domain);
        pnxAcl.setId(acl.getId());
        return aclRepo.save(pnxAcl);
    }

    @Override
    public Folder createFolder(Folder newFolder) {
        return folderRepo.save(newFolder);
    }

    @Override
    public TreeNodesDTO getTreeNodes(final Long nodeId, final int selectedPage, final int maxResults, Sort sort) {
        List<TreeNodeDTO> dtoNodes = Lists.newArrayList();
        PageRequest folderPageRequest = new PageRequest(selectedPage, maxResults, new Sort(new Sort.Order(Direction.ASC, "Text")));
        final Page<Folder> pageResult;

        int foldersOnPage = 0;
        int folderTotalPages = 0;
        long documentsCount = 0, foldersCount = 0;

        if (nodeId != null) {
            pageResult = folderRepo.findByParentId(nodeId, folderPageRequest);
            if (pageResult != null) {
                foldersCount = pageResult.getTotalElements();
                foldersOnPage = pageResult.getNumberOfElements();
                folderTotalPages = pageResult.getTotalPages();
            }
            documentsCount = getDocumentsCount(nodeId, sort);
            if (foldersOnPage < maxResults) {
                final int pageNo = (folderTotalPages == 0) ? selectedPage : selectedPage / folderTotalPages;
                dtoNodes.addAll(getDocumentNodes(nodeId, (folderTotalPages == 0 ? pageNo : pageNo % folderTotalPages), maxResults - foldersOnPage, sort));
            }
        } else {
            pageResult = folderRepo.findByParentIsNull(folderPageRequest);
            foldersCount = pageResult.getTotalElements();
        }

        dtoNodes.addAll(0, folderTranslator.translateToTreeNodeDto(pageResult.getContent()));
        
        if (nodeId == null) {
            // get user business admin access
            final String userVcnId = SecurityContextHolder.getContext().getAuthentication().getName();
            String[] nodes = getRootfoldersForBusinessAdmin(userVcnId).split(":");
            dtoNodes = folderTranslator.translateToBussinessAdminTreeNodeDto(dtoNodes, nodes);
        }
        return new TreeNodesDTO(dtoNodes, foldersCount + documentsCount);
    }
    
    private List<TreeNodeDTO> getDocumentNodes(Long nodeId, int page, int maxElements, Sort sort) {
        List<TreeNodeDTO> dtoNodes = Lists.newArrayList();
        Page<CurrentDocument> docs = getPageOfDocuments(nodeId, new PageRequest(page, maxElements, sort));
        if (docs != null) {
            for (CurrentDocument doc : docs) {
                dtoNodes.add(documentTranslator.translateCurrentDocumentToTreeNodeDto(doc));
            }
        }
        return dtoNodes; 
    }
    
    private long getDocumentsCount(Long nodeId, Sort sort) {
        Page<CurrentDocument> docPageResult = getPageOfDocuments(nodeId, new PageRequest(0, 1, sort));
        return (docPageResult != null) ? docPageResult.getTotalElements() : 0;
    }

    private Page<CurrentDocument> getPageOfDocuments(Long nodeId, PageRequest pageRequest) {
        List<String> statuses = Lists.newArrayList("DELETED", "EXPIRED");
        return currentDocRepo.findByFolder_IdAndStatus_StatusNotIn(nodeId, statuses, pageRequest);
    }

    @Override
    public Document createDocument(PhoenixDocumentDTO documentDTO) {
        return docRepo.save(documentTranslator.translateToDocument(documentDTO));
    }

    @Override
    public String getRootfoldersForBusinessAdmin(String userId) {
        String nodes = "";
        List<Long> aclIds = orionSecurityService.getPhoenixAclIdsForBusinessAdministrator(userId);
        List<Folder> folders = folderRepo.findByAcl_IdIn(aclIds);

        for (Folder folder : folders) {
            if (folder.getParent() == null)
                nodes = nodes + ":" + folder.getId();
        }
        return nodes;
    }

    @Override
    public Long getfolderSize(final List<Long> docIds, final List<Long> folderIds) {
        
        long docSize = 0;
        long folderSize = 0;
        
        if(docIds != null && docIds.size()>0){
            docSize = orionDocService.getDocumentsSizeByObjId(docIds);
        }
        if(folderIds != null && folderIds.size()>0){
            folderSize = orionDocService.getFoldersSizeByNodeId(folderIds);
        }
        
        return folderSize + docSize;
    }
}

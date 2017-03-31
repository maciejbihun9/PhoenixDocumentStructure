package com.volvo.phoenix.document.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.volvo.phoenix.document.datatype.ItemType;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.datatype.OperationStatus;
import com.volvo.phoenix.document.datatype.OperationType;
import com.volvo.phoenix.document.datatype.SolutionNameType;
import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.DocumentTypeDTO;
import com.volvo.phoenix.document.dto.FamilyDTO;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.dto.PhoenixDocumentDTO;
import com.volvo.phoenix.document.dto.SolutionParamDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentAttributePK;
import com.volvo.phoenix.document.entity.DocumentAttributeValue;
import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.DomainAttributeDefinition;
import com.volvo.phoenix.document.entity.DomainAttributeDefinitionPK;
import com.volvo.phoenix.document.entity.Family;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.FolderDefaultAttribute;
import com.volvo.phoenix.document.entity.Operation;
import com.volvo.phoenix.document.entity.PhoenixAcl;
import com.volvo.phoenix.document.entity.PhoenixAclApprover;
import com.volvo.phoenix.document.entity.SolutionParam;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.DocumentTypeRepository;
import com.volvo.phoenix.document.repository.DomainRepository;
import com.volvo.phoenix.document.repository.FamilyRepository;
import com.volvo.phoenix.document.repository.FolderDefaultAttributeRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.repository.OperationRepository;
import com.volvo.phoenix.document.repository.PhoenixAclRepository;
import com.volvo.phoenix.document.repository.SolutionParamRepository;
import com.volvo.phoenix.document.service.AuditLogService;
import com.volvo.phoenix.document.service.CopyManagerService;
import com.volvo.phoenix.document.service.DocumentStructureService;
import com.volvo.phoenix.document.service.LockFailedException;
import com.volvo.phoenix.document.service.TransactionStackService;
import com.volvo.phoenix.document.translator.DocumentTranslator;
import com.volvo.phoenix.document.translator.DocumentTypeTranslator;
import com.volvo.phoenix.document.translator.FamilyTranslator;
import com.volvo.phoenix.document.translator.OperationTranslator;
import com.volvo.phoenix.document.translator.SolutionParamTranslator;
import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.service.OrionDocumentService;
import com.volvo.phoenix.orion.service.OrionSecurityService;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Implementation of {@link CopyManagerService}.
 * 
 */
@Service
@Transactional
public class CopyManagerServiceImpl implements CopyManagerService {

    private static final int MAX_FOLDER_SEARCH_DEPTH = 5;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Collection<OperationStatus> lockingStatuses = new HashSet<OperationStatus>();

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OrionDocumentService orionDocumentService;

    @Autowired
    private DocumentStructureService documentService;

    @Autowired
    private PhoenixAclRepository aclRepository;

    @Autowired
    private SolutionParamRepository paramRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private OrionSecurityService orionSecurityService;

    @Autowired
    private TransactionStackService transactionStackService;

    @Autowired
    private DocumentCopyServiceHelper documentCopyServiceHelper;

    @Autowired
    private SolutionParamTranslator solutionParamTranslator;

    @Autowired
    private FamilyTranslator familyTranslator;

    @Autowired
    private OperationTranslator operationTranslator;

    @Autowired
    private DocumentOperationFactory documentOperationFactory;
    @Autowired
    private FolderOperationFactory folderOperationFactory;

    @Autowired
    private DocumentTranslator documentTranslator;

    @Autowired
    private DocumentTypeTranslator documentTypeTranslator;

    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private FolderDefaultAttributeRepository folderDefaultAttributeRepository;

    private Map<ItemType, OperationFactory> operationFactories = Maps.newHashMap();

    @PostConstruct
    public void init() {
        operationFactories.put(ItemType.DOCUMENT, documentOperationFactory);
        operationFactories.put(ItemType.FOLDER, folderOperationFactory);

        lockingStatuses.add(OperationStatus.CREATED);
        lockingStatuses.add(OperationStatus.SCHEDULED);
        lockingStatuses.add(OperationStatus.RUNNING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OperationDTO createOperation(ItemType sourceType, long sourceId, long targetId, String user) throws LockFailedException {
        Operation operation = operationFactories.get(sourceType).createOperation(sourceId, targetId, user);

        String errMsg = checkIfAnyExistingOperationLocksSourceOrTargetFolder(operation);
        if (StringUtils.isNotEmpty(errMsg)) {
            OperationDTO dto = operationTranslator.translateToDto(operation);
            dto.setSuccess(false);
            dto.setMessage(errMsg);
            return dto;
        }

        persistOperation(operation);

        OperationDTO dto = operationTranslator.translateToDto(operation);
        dto.setSuccess(true);

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OperationDTO> scheduleCopyOperations() {
        return scheduleOperations(OperationType.COPY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OperationDTO> scheduleMoveOperations() {
        return scheduleOperations(OperationType.MOVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<ConflictDTO> checkConflicts(final OperationDTO operation) {
        return documentCopyServiceHelper.checkConflicts(operation);
    }

    @Override
    public OperationDTO findOperation(Long id) {
        return operationTranslator.translateToDto(operationRepository.findOne(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Boolean removeOperation(Long id) {
        operationRepository.delete(id);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OperationDTO> getUserCreatedOperations(final String name) {
        final List<Operation> foundOperations = operationRepository.findByStatusAndUserOrderById(OperationStatus.CREATED, name);
        return operationTranslator.translateToDto(foundOperations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSolutionParams(List<SolutionParamDTO> params) {
        paramRepository.save(solutionParamTranslator.translateToEntity(params));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OperationDTO> getOperationsHistory(String user) {
        return operationTranslator.translateToDto(operationRepository.findByStatusNotAndUserOrderByCreateDateDesc(OperationStatus.CREATED, user));
    }

    /**
     * Check existing operation, whether source/target is moving by others.
     *
     * @param source
     * @param target
     * @return
     */
    private String checkIfAnyExistingOperationLocksSourceOrTargetFolder(final Operation op) {

        final List<Operation> sourceOperationsBySource = operationRepository.findByStatusInAndSourceId(lockingStatuses, op.getSourceId());
        if (CollectionUtils.isNotEmpty(sourceOperationsBySource)) {
            return "Source " + (op.getSourceType() == ItemType.FOLDER ? "folder" : "document") + " is locked by " + sourceOperationsBySource.get(0).getUser();
        }

        final List<Operation> targetOperationsBySource = operationRepository.findByStatusInAndTargetFolderId(lockingStatuses, op.getSourceId());
        if (CollectionUtils.isNotEmpty(targetOperationsBySource)) {
            return "Source folder is locked by " + targetOperationsBySource.get(0).getUser();
        }

        final List<Operation> sourceOperationsByTarget = operationRepository.findByStatusInAndSourceId(lockingStatuses, op.getTargetFolderId());
        if (CollectionUtils.isNotEmpty(sourceOperationsByTarget)) {
            return "Target folder is locked by " + sourceOperationsByTarget.get(0).getUser();
        }

        return null;
    }

    private Operation persistOperation(Operation operation) {
        operation.setStatus(OperationStatus.CREATED);
        return operationRepository.save(operation);
    }

    public void scheduleOperation(final Long operationId, final OperationType operaionType) {

        Assert.notNull(operationId, "The 'operationId' parameter cannot be null");
        Assert.notNull(operationId, "The 'operationType' parameter cannot be null");

        final Operation operation = operationRepository.findOne(operationId);
        if (operation != null) {
            operation.setOperationType(operaionType);
            operation.setStatus(OperationStatus.SCHEDULED);
            operationRepository.save(operation);
        }
    }

    private void scheduleOperationToBePerformed(OperationDTO operation, OperationType operaionType) {
        Operation op = operationRepository.findOne(operation.getId());
        op.setOperationType(operaionType);
        op.setStatus(OperationStatus.SCHEDULED);
        operationRepository.save(op);
    }

    private List<OperationDTO> scheduleOperations(OperationType operaionType) {
        List<OperationDTO> scheduledOperations = Lists.newArrayList();
        List<OperationDTO> createdOperations = getUserCreatedOperations(SecurityContextHolder.getContext().getAuthentication().getName());
        for (OperationDTO op : createdOperations) {
            List<ConflictDTO> conflicts = checkConflicts(op);

            if (documentCopyServiceHelper.allConflictsHaveProposedSolutions(conflicts)) {
                scheduleOperationToBePerformed(op, operaionType);
                scheduledOperations.add(op);
            }
        }
        return scheduledOperations;
    }

    @Override
    public List<ConflictDTO> checkConflicts() {

        final long startTime = System.currentTimeMillis();

        final List<ConflictDTO> conflicts = Lists.newArrayList();
        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        final List<OperationDTO> operationDTOs = getUserCreatedOperations(userName);

        for (final OperationDTO operationDTO : operationDTOs) {
            conflicts.addAll(checkConflicts(operationDTO));
        }

        System.out.println(System.currentTimeMillis() - startTime);

        return conflicts;
    }

    @Override
    public void moveDocument(long documentId, long targetFolderId, List<SolutionParam> solutions) {
        Folder targetFolder = folderRepository.findOne(targetFolderId);
        String documentName = orionDocumentService.getDocumentName(documentId);

        List<OrionDocumentDTO> allRevisions = orionDocumentService.findAllDocumentRevisions(documentName);
        for (OrionDocumentDTO revision : allRevisions) {
            Document document = documentRepository.findOne(revision.getId());
            applySolutions(document.getId(), document.getDomain(), targetFolder.getAcl().getDomain(), solutions);
            moveDocumentRevision(document.getId(), targetFolder);
        }
    }

    @Override
    public Long copyDocument(OrionCMContext ctx, long documentId, long targetFolderId, List<SolutionParam> solutions) throws PhoenixDataAccessException, IOException {
        Document document = documentRepository.findOne(documentId);
        OrionDocumentDTO orionDocDTO = orionDocumentService.findDocument(documentId);
        Folder targetFolder = folderRepository.findOne(targetFolderId);
        orionDocDTO.getAcl().setId(targetFolder.getAcl().getId());

        Long copiedDocId = orionDocumentService.copyDocument(ctx, orionDocDTO);

        PhoenixDocumentDTO dto = documentTranslator.translateToPhoenixDTO(document);
        dto.setNode_id(targetFolderId);
        dto.setObjectId(copiedDocId);
        dto.setDomain_id(targetFolder.getAcl().getDomain().getId());

        documentService.createDocument(dto);
        applySolutions(copiedDocId, document.getDomain(), targetFolder.getAcl().getDomain(), solutions);

        transactionStackService.documentCopied(copiedDocId);

        return copiedDocId;
    }

    @Override
    public void copyFolder(OrionCMContext ctx, Long sourceId, Long targetFolderId, List<SolutionParam> solutions) throws PhoenixDataAccessException,
            IOException {
        Folder srcFolder = folderRepository.findOne(sourceId);
        Folder targetFolder = folderRepository.findOne(targetFolderId);

        Folder closestMasterFolder = findClosestMasterFolder(srcFolder);

        PhoenixAcl newAcl = duplicateAcl(srcFolder.getAcl().getId(), srcFolder.getAcl().getDomain());
        Folder copiedFolder = createEmptyFolderCopy(srcFolder, targetFolder, NodeType.M, newAcl);

        copyFolderAttributes(closestMasterFolder, copiedFolder);

        copyFolderContent(srcFolder, copiedFolder, ctx, copiedFolder.getAcl().getDomain(), solutions);
    }

    @Override
    public void moveFolder(Long sourceId, Long targetFolderId, List<SolutionParam> solutions) {
        Folder srcFolder = folderRepository.findOne(sourceId);
        Folder targetFolder = folderRepository.findOne(targetFolderId);
        if (NodeType.M == srcFolder.getType()) {
            srcFolder.setParent(targetFolder);
            folderRepository.save(srcFolder);
        } else {
            moveSelectedSlaveFolder(srcFolder, targetFolder, solutions);
        }
        addTransactionStackEntriesForMovedDocuments(srcFolder);
    }

    private Document applySolutions(long documentId, Domain srcDomain, Domain targetDomain, List<SolutionParam> solutions) {
        Document document = documentRepository.findOne(documentId);
        for (SolutionParam solution : solutions) {
            if (solution.getSolution() == SolutionNameType.CONNECT_DOCUMENT_TYPE_TO_TARGET_DOMAIN) {
                connectDocumentTypeWithTargetDomain(document, srcDomain, targetDomain);
            } else if (solution.getSolution() == SolutionNameType.SELECT_ANOTHER_DOCUMENT_TYPE) {
                changeDocumentTypeForDocument(document, solution);
            } else if (solution.getSolution() == SolutionNameType.DOCUMENT_TYPE_ATTR || solution.getSolution() == SolutionNameType.DOMAIN_ATTR) {
                addOrUpdateUserDefinedAttributeValue(document, solution);
            } else if(solution.getSolution()==SolutionNameType.FAMILY_ATTR){
                changeDocumentFamily(document, solution);
            }
        }
        return documentRepository.save(document);
    }

    private void changeDocumentFamily(Document document, SolutionParam solution) {
        document.setFamily(familyRepository.findOne(solution.getParamId()));
    }

    private void changeDocumentTypeForDocument(Document document, SolutionParam solution) {
        removeExistingAttributesForDocumentType(document);
        document.setType(documentTypeRepository.findByIdWithAttributes(solution.getParamId()));
    }

    private void removeExistingAttributesForDocumentType(Document document) {
        List<DocumentAttributeValue> attributes = document.getDocumentAttributes();
        document.setDocumentAttributes(new ArrayList<DocumentAttributeValue>());
        for (DocumentAttributeValue atr : attributes) {
            if (!isAttributeOfSelectedDocumentType(atr.getId().getAttribute(), document.getType())) {
                document.getDocumentAttributes().add(atr);
            }
        }
    }

    private boolean isAttributeOfSelectedDocumentType(Long attributeId, DocumentType docType) {
        for (AttributeDefinition atr : docType.getAttributes()) {
            if (atr.getId().equals(attributeId)) {
                return true;
            }
        }
        return false;
    }

    private void addOrUpdateUserDefinedAttributeValue(Document document, SolutionParam solution) {
        for (DocumentAttributeValue atr : document.getDocumentAttributes()) {
            if (atr.getId().getAttribute().equals(solution.getParamId())) {
                atr.setValue(solution.getValue());
                return;
            }
        }
        DocumentAttributeValue atr = new DocumentAttributeValue();
        DocumentAttributePK pk = new DocumentAttributePK();
        pk.setAttribute(solution.getParamId());
        pk.setDocument(document.getId());
        atr.setId(pk);
        atr.setOwningDocument(document);
        atr.setIndexId(Long.valueOf(document.getDocumentAttributes().size() + 1));
        atr.setValue(solution.getValue());
        document.getDocumentAttributes().add(atr);
    }

    private void connectDocumentTypeWithTargetDomain(Document document, Domain srcDomain, Domain targetDomain) {
        if (!existDocumentTypeInDomain(document.getType().getId(), targetDomain)) {
            targetDomain.getDocumentTypes().add(document.getType());
            connectDocTypeAttributesWithTargetDomain(document.getType().getAttributes(), srcDomain, targetDomain);
            domainRepository.save(targetDomain);
        }
    }

    private void connectDocTypeAttributesWithTargetDomain(List<AttributeDefinition> docTypeAttributes, Domain srcDomain, Domain targetDomain) {
        Set<Long> attributeIdsToCopy = new HashSet<Long>();
        for (AttributeDefinition attribute : docTypeAttributes) {
            if (!existAttributeInDomain(attribute.getId(), targetDomain.getDomainAttributes())) {
                attributeIdsToCopy.add(attribute.getId());
            }
        }
        copyDomainAttributeDefinitionsToDomain(attributeIdsToCopy , srcDomain , targetDomain);
    }

    private void copyDomainAttributeDefinitionsToDomain(Set<Long> attributeIdsToCopy, Domain srcDomain, Domain targetDomain) {
        if(!attributeIdsToCopy.isEmpty()){
            //find max sortOrder
            //New attributes will be added at the end.
            long order= 0l;
            for (DomainAttributeDefinition targetDomainAttribute : targetDomain.getDomainAttributes()) {
                if(targetDomainAttribute.getSortOrder()>order)
                    order = targetDomainAttribute.getSortOrder();
            }

            for (DomainAttributeDefinition sourceDomainAttDef : srcDomain.getDomainAttributes()) {
                if(attributeIdsToCopy.contains(sourceDomainAttDef.getPk().getAttributeDefinitionId())){
                    copyDomainAttributeDefinitionToDomainUsingSortOrder(targetDomain, order, sourceDomainAttDef);
                    ++order;
                }
            }
        }
    }

    private void copyDomainAttributeDefinitionToDomainUsingSortOrder(Domain targetDomain, long order, DomainAttributeDefinition sourceDomainAttDef) {
        DomainAttributeDefinition copiedAttrDef = new DomainAttributeDefinition();
        DomainAttributeDefinitionPK attrPK = new DomainAttributeDefinitionPK();
        
        attrPK.setAttributeDefinitionId(sourceDomainAttDef.getPk().getAttributeDefinitionId());
        attrPK.setDomainId(targetDomain.getId());
        copiedAttrDef.setPk(attrPK);
        
        copiedAttrDef.setMandatory(sourceDomainAttDef.getMandatory());
        copiedAttrDef.setSortOrder(order);
        targetDomain.getDomainAttributes().add(copiedAttrDef);
    }

    private boolean existAttributeInDomain(Long attributeId, List<DomainAttributeDefinition> domainAttributes) {
        for (DomainAttributeDefinition domainAttribute : domainAttributes) {
            if (domainAttribute.getPk().getAttributeDefinitionId().equals(attributeId)) {
                return true;
            }
        }
        return false;
    }

    private boolean existDocumentTypeInDomain(Long docTypeId, Domain domain) {
        for (DocumentType docType : domain.getDocumentTypes()) {
            if (docType.getId().equals(docTypeId)) {
                return true;
            }
        }
        return false;
    }

    private void moveDocumentRevision(long documentId, Folder targetFolder) {
        Document document = documentRepository.findOne(documentId);

        orionDocumentService.updateDocumentAcl(document.getId(), targetFolder.getAcl().getId());
        document.setFolder(targetFolder);
        document.setDomain(targetFolder.getAcl().getDomain());

        documentRepository.save(document);

        transactionStackService.documentMoved(documentId);
    }

    /**
     * Recursively copy subfolder contents
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void copyFolderContent(Folder srcFolder, Folder targetFolder, OrionCMContext ctx, Domain targetDomain, List<SolutionParam> solutions) throws PhoenixDataAccessException, IOException {
        logger.info("Starting copy folder {} -> {}", srcFolder.getPath(), targetFolder.getPath());
        List<Document> docs = documentRepository.findByFolder_Id(srcFolder.getId());
        for (Document doc : docs) {
            try {
                copyDocument(ctx, doc.getId(), targetFolder.getId(), solutions);
            } catch (PhoenixDataAccessException e) {
                logger.error("Error copying document {} , message: {}", doc.getId(), e.getMessage());
            }
        }

        List<Folder> subFolders = folderRepository.findByParent_Id(srcFolder.getId());
        for (Folder subFolder : subFolders) {
            Folder closestMasterFolder = findClosestMasterFolder(subFolder);
            PhoenixAcl targetAcl = (NodeType.S == subFolder.getType()) ? targetFolder.getAcl() : duplicateAcl(srcFolder.getAcl().getId(), targetDomain);

            Folder copiedFolder = createEmptyFolderCopy(subFolder, targetFolder, subFolder.getType(), targetAcl);
            if (NodeType.M == copiedFolder.getType()) {
                copyFolderAttributes(closestMasterFolder, copiedFolder);
            }
            copyFolderContent(subFolder, copiedFolder, ctx, targetDomain, solutions);
        }
    }

    /**
     * Recursively add transaction stack triggers for each document in moved folder and subfolders
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void addTransactionStackEntriesForMovedDocuments(Folder folder) {
        List<Document> docs = documentRepository.findByFolder_Id(folder.getId());
        for (Document doc : docs) {
            transactionStackService.documentMoved(doc.getId());
        }

        List<Folder> subFolders = folderRepository.findByParent_Id(folder.getId());
        for (Folder subFolder : subFolders) {
            addTransactionStackEntriesForMovedDocuments(subFolder);
        }
    }

    private Folder createEmptyFolderCopy(Folder src, Folder target, NodeType nodeType, PhoenixAcl phoenixAcl) {
        Folder node = new Folder();
        node.setType(nodeType);
        node.setCreated(new Date());
        node.setNodeLevel(src.getNodeLevel());
        node.setDescription(src.getDescription());
        node.setText(src.getText());
        node.setOwner(src.getOwner());
        node.setOwnerRealname(src.getOwnerRealname());
        node.setParent(target);
        node.setAcl(phoenixAcl);
        node.setParentAcl(target.getAcl());
        return documentService.createFolder(node);
    }

    private void moveSelectedSlaveFolder(Folder srcFolder, Folder targetFolder, List<SolutionParam> solutions) {
        Folder convertedFolder = convertToMasterFolder(srcFolder, targetFolder, solutions);

        List<Document> docs = documentRepository.findByFolder_Id(srcFolder.getId());
        for (Document doc : docs) {
            moveDocument(doc.getId(), convertedFolder.getId(), solutions);
        }

        List<Folder> subFolders = folderRepository.findByParent_Id(srcFolder.getId());
        for (Folder subFolder : subFolders) {
            if (NodeType.S == subFolder.getType()) {
                subFolder.setAcl(convertedFolder.getAcl());
                folderRepository.save(subFolder);
            }
        }
    }

    private Folder convertToMasterFolder(Folder srcFolder, Folder targetFolder, List<SolutionParam> solutions) {
        PhoenixAcl phoenixAcl = duplicateAcl(srcFolder.getAcl().getId(), srcFolder.getParent().getAcl().getDomain());
        srcFolder.setAcl(phoenixAcl);
        Folder closestMasterFolder = findClosestMasterFolder(srcFolder);

        copyFolderAttributes(closestMasterFolder, srcFolder);

        srcFolder.setType(NodeType.M);
        srcFolder.setParent(targetFolder);
        return folderRepository.save(srcFolder);
    }

    private PhoenixAcl duplicateAcl(Long srcAclId, Domain targetDomain) {
        OrionAclDTO acl = orionDocumentService.createAcl(aclRepository.getNextAclName());
        orionSecurityService.duplicateAcl(srcAclId, acl.getId());

        PhoenixAcl phoenixAcl = new PhoenixAcl();
        phoenixAcl.setId(acl.getId());
        phoenixAcl.setDomain(targetDomain);
        return aclRepository.save(phoenixAcl);
    }

    private Folder findClosestMasterFolder(Folder srcFolder) {
        Folder f = srcFolder;
        int searchDepth = 1;
        while (f.getType() != NodeType.M && searchDepth++ < MAX_FOLDER_SEARCH_DEPTH) {
            f = f.getParent();
        };
        return (f.getType() == NodeType.M) ? f : null;
    }

    private void copyFolderAttributes(Folder fromFolder, Folder toFolder) {
        copyParentFolderWorkflowApprovers(fromFolder, toFolder);
        copyParentFolderDefaultValuesForDomainAttributes(fromFolder, toFolder);
    }

    private void copyParentFolderWorkflowApprovers(Folder fromFolder, Folder toFolder) {
        toFolder.getAcl().getDocumentTypes().addAll(fromFolder.getAcl().getDocumentTypes());

        if (fromFolder.getAcl().getWorkflowApprovers() != null) {
            List<PhoenixAclApprover> workflowApprovers = new ArrayList<PhoenixAclApprover>();
            for (PhoenixAclApprover approver : fromFolder.getAcl().getWorkflowApprovers()) {
                PhoenixAclApprover newApprover = new PhoenixAclApprover();
                newApprover.getId().setAcl(toFolder.getAcl());
                newApprover.getId().setDoctypeId(approver.getId().getDoctypeId());
                newApprover.getId().setUsername(approver.getId().getUsername());
                workflowApprovers.add(newApprover);
            }
            toFolder.getAcl().setWorkflowApprovers(workflowApprovers);
        }
    }

    private void copyParentFolderDefaultValuesForDomainAttributes(Folder fromFolder, Folder toFolder) {
        List<FolderDefaultAttribute> attributes = folderDefaultAttributeRepository.findById_FolderId(fromFolder.getId());
        for (FolderDefaultAttribute atr : attributes) {
            FolderDefaultAttribute newAtr = new FolderDefaultAttribute();
            newAtr.getId().setFolderId(toFolder.getId());
            newAtr.getId().setAttributeId(atr.getId().getAttributeId());
            newAtr.setValue(atr.getValue());
            folderDefaultAttributeRepository.save(newAtr);
        }
    }

    @Override
    public FamilyDTO getFamilyForDocumentTypeId(Long documentTypeId) {
        final DocumentType doctype = documentTypeRepository.findOne(documentTypeId);
        for (final Family family : doctype.getFamilies()) {
            return familyTranslator.translateToDto(family);
        }
        return null;
    }

    @Override
    public List<DocumentTypeDTO> getFamilyDocumentTypes(final Long familyId, final Long operationId) {

        Assert.notNull(familyId, "The 'familyId' cannot be null");
        Assert.notNull(operationId, "The 'operationId' cannot be null");

        final List<DocumentTypeDTO> documentTypeDTOs = new ArrayList<DocumentTypeDTO>();
        final Operation operation = operationRepository.findOne(operationId);

        if (operation != null) {
            final Folder targetFolder = folderRepository.findOne(operation.getTargetFolderId());
            final Domain targetDomain = targetFolder.getAcl().getDomain();
            final List<DocumentType> domainDocumentTypes = targetDomain.getDocumentTypes();
            final Family family = familyRepository.findOne(familyId);
            final List<DocumentType> familyDocumentTypes = family.getDocumentTypes();

            familyDocumentTypes.retainAll(domainDocumentTypes);

            for (final DocumentType documentType : familyDocumentTypes) {
                documentTypeDTOs.add(documentTypeTranslator.translateToDto(documentType));
            }

            Collections.sort(documentTypeDTOs, new Comparator<DocumentTypeDTO>() {
                @Override
                public int compare(DocumentTypeDTO o1, DocumentTypeDTO o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        return documentTypeDTOs;
    }

}

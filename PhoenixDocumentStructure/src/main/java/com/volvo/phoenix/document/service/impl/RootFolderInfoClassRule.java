package com.volvo.phoenix.document.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.volvo.phoenix.document.datatype.ConflictType;
import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.dto.RootFolderInfoClassConflictDTO;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.RootFolderState;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.repository.RootFolderStateRepository;
import com.volvo.phoenix.document.service.ConflictRule;
import com.volvo.phoenix.orion.dto.OrionAclStateDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.service.OrionDocumentService;

/**
 * Checks if Info Class of moved document exist in target Root Folder. If not then it means confict.
 */
@Component
@Transactional(propagation = Propagation.SUPPORTS)
public class RootFolderInfoClassRule implements ConflictRule {

    private final byte ruleOrder = 2;
    
    @Autowired
    private OrionDocumentService orionDocService;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private RootFolderStateRepository rootFolderStateRepository;

    //Some ACLState names has other name in DB and other in Phoenix. Some kind of mapping is needed. 
    private Map<Long , String> stateId2DisplayStateName= new HashMap<Long, String>();
    
    public RootFolderInfoClassRule() {
        stateId2DisplayStateName.put(40l, "Strictly Confidential");
        stateId2DisplayStateName.put(42l, "Internal");
    }
    
    
    /**
     * {@inheritDoc} Checks if source document/folder state is compliant with target folder.
     * 
     */
    @Override
    public ConflictDTO check(final OperationDTO operationDTO) {

        Assert.notNull(operationDTO, "The 'operationDTO' parameter cannot be null");

        final Folder targetFolder = folderRepository.findOne(Long.valueOf(operationDTO.getTarget().getId()));
        Assert.notNull(targetFolder, "The 'targetFolder' cannot be null. OperationDTO has not existing folder.");

        final Long targetRootFolderId = targetFolder.getRoot().getId();
        final List<RootFolderState> targetRootFolderStates = rootFolderStateRepository.findById_NodeId(targetRootFolderId);

        switch (operationDTO.getSource().getType()) {
        case M:
        case S:
            final Folder sourceFolder = folderRepository.findOne(operationDTO.getSource().getId());
            final String missingState = checkIfSourceFolderDocumentStatesAreCompliantWithTargetRootFolderStates(sourceFolder, targetRootFolderStates);
            if (null != missingState) {
                return new RootFolderInfoClassConflictDTO(missingState, operationDTO);
            }
            break;
        case D:
            final OrionDocumentDTO orionDocument = orionDocService.findDocument(operationDTO.getSource().getId());
            if (!isDocumentAclStateCompliantWithRootFolderStates(targetRootFolderStates, orionDocument)) {
                return new RootFolderInfoClassConflictDTO(getDisplayNameForACLState(orionDocument.getAclState()), operationDTO);
            }
            break;
        default:
            throw new IllegalArgumentException(String.format("Unknown type of Folder/Document: %s", operationDTO.getSource().getType()));
        }
        return null;
    }

    /**
     * PHOENIX-2205 Some ACLState names has other name in DB and other in Phoenix. Some kind of mapping is needed. 
     * @param aclState
     * @return
     */
    private String getDisplayNameForACLState(OrionAclStateDTO aclState) {
        String result = aclState.getName();
        if(stateId2DisplayStateName.containsKey(aclState.getId())) {
            result = stateId2DisplayStateName.get(aclState.getId());
        }
        return result;
    }

    /**
     * Checks if all documents in source folder are complaint with target root folder states.
     * 
     * @param sourceFolder
     * @param states
     *            target root folder states
     * @return null if source folder documents states are compliant with target root folder states, otherwise returns name of missing state
     */
    private String checkIfSourceFolderDocumentStatesAreCompliantWithTargetRootFolderStates(final Folder sourceFolder, final List<RootFolderState> states) {

        final Long sourceRootFolderId = sourceFolder.getRoot().getId();
        final List<RootFolderState> sourceRootFolderStates = rootFolderStateRepository.findById_NodeId(sourceRootFolderId);
        if (states.retainAll(sourceRootFolderStates)) {
            return null;
        }

        final List<Document> sourceFolderDocs = documentRepository.findByFolder_Id(sourceFolder.getId());
        for (final Document sourceDoc : sourceFolderDocs) {
            final OrionDocumentDTO orionSourceDoc = orionDocService.findDocument(sourceDoc.getId());
            if (!isDocumentAclStateCompliantWithRootFolderStates(states, orionSourceDoc)) {
                return orionSourceDoc.getAclState().getName();
            }
        }
        
        final List<Folder> sourceSubfolders = folderRepository.findByParent_Id(sourceFolder.getId());
        for (final Folder folder : sourceSubfolders) {
            final String result = checkIfSourceFolderDocumentStatesAreCompliantWithTargetRootFolderStates(folder, states);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private boolean isDocumentAclStateCompliantWithRootFolderStates(final List<RootFolderState> states, final OrionDocumentDTO doc) {

        for (final RootFolderState state : states) {
            if (state.getId().getStateId().equals(doc.getAclState().getId())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public byte getOrderInConflictRuleChain() {
        return ruleOrder;
    }
}

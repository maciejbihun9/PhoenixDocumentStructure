package com.volvo.phoenix.document.uploadtool.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.datatype.MetadataStatus;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.DictionaryValue;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.Family;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.PhoenixAcl;
import com.volvo.phoenix.document.repository.FamilyRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.translator.UploadToolOperationConflictTranslator;
import com.volvo.phoenix.document.translator.UploadToolOperationTreeNodeTranslator;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolDocumentAttributeValueDTO;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolOperationConflictDTO;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolTreeNodeDTO;
import com.volvo.phoenix.document.uploadtool.application.dto.ValidateDocumentResultDTO;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolDocumentRepository;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationConflictRepository;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationFileRepository;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationRepository;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationTreeNodeRepository;
import com.volvo.phoenix.document.uploadtool.model.UploadToolConflictType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocument;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocumentAttributeValue;
import com.volvo.phoenix.document.uploadtool.model.UploadToolFileType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolNodeType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationConflict;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationFile;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationStatus;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationTreeNode;
import com.volvo.phoenix.orion.dto.OrionUserDTO;
import com.volvo.phoenix.orion.service.OrionSecurityService;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Upload tool main application service.
 */
@Service
@Transactional
public class UploadToolServiceImpl implements UploadToolService {

    private static final String ORION_VAULT = "app_phoenix";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UploadToolDocumentService uploadToolDocumentService;
    
    @Autowired
    private UploadToolOperationLifecycleService operationLifecycleService;
    
    @Autowired
    private OrionSecurityService orionSecurityService;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private UploadToolOperationRepository operationRepository;

    @Autowired
    private UploadToolOperationFileRepository operationFileRepository;

    @Autowired
    private UploadToolOperationTreeNodeRepository treeNodeRepository;

    @Autowired
    private UploadToolOperationConflictRepository conflictRepository;
    
    @Autowired
    private UploadToolDocumentRepository uploadToolDocumentRepository;
    

    @Override
    public UploadToolOperation createOperation(Long folderId) {
        return operationLifecycleService.createOperation(folderId);
    }

    @Override
    public String getStoreLocation(Long operationId) {
        return System.getProperty("TEMP_DIR") + "/ut-" + operationId;
    }

    @Override
    public UploadToolOperation getOperation(Long operationId) {
        return operationRepository.findOne(operationId);
    }

    @Override
    public void addUploadedFilesInformation(Long operationId, List<UploadToolOperationFile> uploadedFiles) {
        UploadToolOperation operation = operationRepository.findOne(operationId);

        for (UploadToolOperationFile operationFile : uploadedFiles) {
            operationFile.setOperation(operation);
            operationFileRepository.save(operationFile);
        }
        operation.getUploadedFiles().addAll(uploadedFiles);

        operationRepository.save(operation);
    }

    @Override
    public void addDocumentTree(Long operationId, List<UploadToolTreeNodeDTO> uploadedDocumentTree) {
        UploadToolOperation operation = operationRepository.findOne(operationId);
        List<UploadToolOperationTreeNode> uploadedDocumentStructure 
            = UploadToolOperationTreeNodeTranslator.translateUploadToolTreeNodeDTOList(uploadedDocumentTree, null);
        addTreeToOperation(operation, uploadedDocumentStructure);
        operationRepository.save(operation);
    }

    @Override
    public void validateDocumentTree(Long operationId) {
        UploadToolOperation operation = operationRepository.findOne(operationId);
        List<Family> families = familyRepository.findAll();
        Domain domain = operation.getFolder().getAcl().getDomain();
        OrionUserDTO userDTO = orionSecurityService.findUserByUserName(operation.getUser());

        if (checkForConflictingFolders(operation)) {
            for (UploadToolOperationTreeNode node : operation.getUploadedTree()) {
                if (node.getNodeType() == UploadToolNodeType.D) {
                    ValidateDocumentResultDTO result = uploadToolDocumentService.createAndValidateDocument(operation, node, families, domain, userDTO);
                    node.setValid(result.isValid());
                    node.setRev(result.getRevision());
                }
            }

            for (UploadToolOperationTreeNode node : operation.getUploadedTree()) {
                if (node.getNodeType() == UploadToolNodeType.S) {
                    node.setValid(isSlaveFolderValid(node));
                }
            }
        }
    }
    public Boolean checkStatusOfDocumentTree(Long operationId) {
        UploadToolOperation operation = operationRepository.findOne(operationId);
        for (UploadToolOperationTreeNode node : operation.getUploadedTree()) {
            if ((node.getNodeType() == UploadToolNodeType.S || node.getNodeType() == UploadToolNodeType.D) && !node.getValid()) {
                return true;
            }
        }
        return false;
        
    }

    private boolean checkForConflictingFolders(UploadToolOperation operation) {
        List<String> subFolderNames = folderRepository.findChildNamesByParentId(operation.getFolder().getId());

        for (UploadToolOperationTreeNode node : operation.getUploadedTree()) {
            if (node.getNodeType() == UploadToolNodeType.S && node.getParentNode() == null) {
                if (subFolderNames.contains(node.getNodeText())) {
                    storeFolderConflict(operation, node);
                    return false;
                }
            }
        }
        return true;
    }

    private void storeFolderConflict(UploadToolOperation operation, UploadToolOperationTreeNode node) {
        log.warn("A folder with name {} already exists under destination folder {}", node.getNodeText(), operation.getFolder().getText());
        UploadToolOperationConflict conflict = new UploadToolOperationConflict();
        conflict.setConflict(UploadToolConflictType.FOLDER_NAME);
        conflict.setTreeNode(node);
        conflict.setOperation(operation);
        conflictRepository.save(conflict);
    }

    private Boolean isSlaveFolderValid(UploadToolOperationTreeNode folderNode) {
        boolean valid = true;
        for (UploadToolOperationTreeNode node : folderNode.getChildNodes()) {
            if (node.getNodeType() == UploadToolNodeType.D) {
                valid &= node.getValid();
            } else if (node.getNodeType() == UploadToolNodeType.S) {
                valid &= isSlaveFolderValid(node);
            }
        }
        return valid;
    }

    private void addTreeToOperation(UploadToolOperation operation, List<UploadToolOperationTreeNode> uploadedDocumentStructure) {
        for (UploadToolOperationTreeNode node : uploadedDocumentStructure) {
            operation.getUploadedTree().add(node);
            node.setOperation(operation);
            addTreeToOperation(operation, node.getChildNodes());
        }
    }

    @Override
    public List<String> getUploadedZIPFilesWithNoMetadata(Long operationId) {
        List<String> result = new ArrayList<String>();
        if (checkStatusOfDocumentTree(operationId)) {
            UploadToolOperation operation = operationRepository.findOne(operationId);
            for (UploadToolOperationFile f : operation.getUploadedFiles()) {
                if (f.getFileType() == UploadToolFileType.Z && !f.getMetadataStatus().equals(MetadataStatus.METADATA_OK)) {
                    result.add(f.getFileName());
                }
            }

            return result;
        }
        return result;
    }

    @Override
    public UploadToolTreeNodeDTO geOperationTreeNodeDTO(long nodeId) {
        UploadToolOperationTreeNode uto = treeNodeRepository.findByNodeId(nodeId);
        UploadToolTreeNodeDTO dto = UploadToolOperationTreeNodeTranslator.translateToTreeNodeDto(uto);
        dto.setChildren(UploadToolOperationTreeNodeTranslator.translateToTreeNodesDtos(uto.getChildNodes()));
        return dto;
    }

    @Override
    public List<UploadToolTreeNodeDTO> getOperationTreeRootNodes(long operationId) {
        return UploadToolOperationTreeNodeTranslator.translateToTreeNodesDtos(treeNodeRepository.findByOperationIdAndParentNodeIsNull(operationId));
    }

    @Override
    public List<UploadToolOperationTreeNode> getOperationTreeChildNodes(long parentNodeId) {
        return treeNodeRepository.getOperationTreeChildNodes(parentNodeId);
    }
    

    @Override
    public List<UploadToolOperationConflictDTO> getConflictsForOperation(long parentNodeId) {
        return UploadToolOperationConflictTranslator.listToDTO(conflictRepository.findByOperationId(parentNodeId));
    }

    @Override
    public int getTotalNumberOfDocuments(Long operationId) {
        return treeNodeRepository.countByOperationIdAndNodeType(operationId, UploadToolNodeType.D);
    }

    @Override
    public void applyUpload(Long operationId) throws PhoenixDataAccessException, IOException {
        UploadToolOperation operation = operationRepository.findOne(operationId);
        OrionCMContext orionContext = new OrionCMContext(operation.getUser(), ORION_VAULT);
        OrionUserDTO userDTO = orionSecurityService.findUserByUserName(operation.getUser());
        operationLifecycleService.markOperationAsOngoing(operationId);
        
        createRootStructure(orionContext, operation, userDTO);
        
        operationLifecycleService.closeOperation(operationId, UploadToolOperationStatus.SUCCEEDED);
    }
    
    @Override
    public List<UploadToolTreeNodeDTO> getDocumentAttachments(long parentNodeId){
        return UploadToolOperationTreeNodeTranslator.translateToTreeNodesDtos(treeNodeRepository.getDocumentAttachments(parentNodeId));
    }
    
    @Override
    public List<UploadToolDocumentAttributeValueDTO> getDocumentOptionalAttrValues(long nodeId) {

        UploadToolDocument doc = uploadToolDocumentRepository.findDocByTreeNodeId(nodeId);   
    
        UploadToolOperationTreeNode node = doc.getTreeNode();
        Domain domain = node.getOperation().getFolder().getAcl().getDomain();
        List<UploadToolDocumentAttributeValueDTO> atrList = new ArrayList<UploadToolDocumentAttributeValueDTO>();

        if (domain.getAttributes() != null) {
            for (AttributeDefinition atr : domain.getAttributes()) {
                if (atr.getLabel() != null) {
                    for (UploadToolDocumentAttributeValue documentAttributeValue : doc.getAttributesValues()) {
                        if (documentAttributeValue.getAttribute().equals(atr.getId())) {
                         
                            atrList.add(formatValueForPhoenixAttribute(documentAttributeValue.getValue(), atr));
                         
                        }
                    }
                }
            }
        }
        return atrList;
    }
    
    @Override
    public List<UploadToolDocumentAttributeValueDTO> getDocumentMandatoryAttrValues(long nodeId) {
        List<UploadToolDocumentAttributeValueDTO> atrList = new ArrayList<UploadToolDocumentAttributeValueDTO>();
       
        UploadToolDocument doc = uploadToolDocumentRepository.findDocByTreeNodeId(nodeId);   
    
        if (doc.getType() != null && doc.getType().getAttributes() != null) {
            for (AttributeDefinition atr : doc.getType().getAttributes()) {
                if (atr.getLabel() != null) {

                    String value = doc.getTreeNode().getDocumentAttributes().get(atr.getLabel());
                    if (value != null) {
                        
                        atrList.add(formatValueForPhoenixAttribute(value, atr));
                    }
                }
            }
        }
           return atrList;
    }
    

    private UploadToolDocumentAttributeValueDTO formatValueForPhoenixAttribute(String value, AttributeDefinition atr) {
        UploadToolDocumentAttributeValueDTO atrValue = new UploadToolDocumentAttributeValueDTO();
        atrValue.setType(atr.getType().toString());
        atrValue.setLabel(atr.getLabel());
        atrValue.setName(atr.getName());
        atrValue.setAttrId(atr.getId());
        
        switch (atr.getType()) {
        case CHECKBOX:
            atrValue.setValue(value == null 
                    ? null : value.toUpperCase().startsWith("Y") 
                            ? "Y" : null);
            break;
        case SELECT:
            atrValue.setSelectedDictionary(atr.getDictionary().getValues().toString());
            for (DictionaryValue lovValue : atr.getDictionary().getValues()) {
                if (lovValue.getValue() != null && lovValue.getValue().equals(value)) {
                    atrValue.setValue(lovValue.getValue());
                    atrValue.setSelectedId(lovValue.getId().toString());
                }
            }
            break;
        default:
            break;

        }
        return atrValue;
    }

    private void createRootStructure(OrionCMContext orionContext, UploadToolOperation operation, OrionUserDTO userDTO) throws PhoenixDataAccessException, IOException {
        List<UploadToolOperationTreeNode> rootNodes = getRootNodes(operation.getUploadedTree());
        sortDocumentNodesByRev(rootNodes);
        
        for (UploadToolOperationTreeNode node : operation.getUploadedTree()) {
            if (node.getParentNode() == null) {
                if (node.getNodeType() == UploadToolNodeType.S) {
                    createSubStructure(orionContext, node, operation.getFolder(), operation.getFolder().getAcl(), userDTO);
                } else if (node.getNodeType() == UploadToolNodeType.D) {

                    createPhoenixDocument(orionContext, operation.getFolder(), node);

                }
            }    
        }
    }
    
    private void createSubStructure(OrionCMContext orionContext, UploadToolOperationTreeNode uploadToolFolder, Folder parentPnxFolder, PhoenixAcl acl, OrionUserDTO userDTO) throws IOException {
        Folder createdFolder = uploadToolDocumentService.createPhoenixFolder(uploadToolFolder, parentPnxFolder, acl, userDTO);

        List<UploadToolOperationTreeNode> childNodes = uploadToolFolder.getChildNodes();
        sortDocumentNodesByRev(childNodes);
        

        for (UploadToolOperationTreeNode node : childNodes) {
            if (node.getNodeType() == UploadToolNodeType.S) {
                createSubStructure(orionContext, node, createdFolder, acl, userDTO);
            } else if (node.getNodeType() == UploadToolNodeType.D) {

                createPhoenixDocument(orionContext, createdFolder, node);

            }
        }
    }


    private void createPhoenixDocument(OrionCMContext orionContext, Folder createdFolder, UploadToolOperationTreeNode node) throws IOException {
        try {
            uploadToolDocumentService.createPhoenixDocument(orionContext, node, createdFolder, createAttachmentList(node));
        } catch (PhoenixDataAccessException e) {
            log.error(e.getMessage());
        }
    }
    
    private List<UploadToolOperationTreeNode> getRootNodes(List<UploadToolOperationTreeNode> allNodes) {
        List<UploadToolOperationTreeNode> result = new ArrayList<UploadToolOperationTreeNode>();
        for (UploadToolOperationTreeNode node : allNodes) {
            if (node.getParentNode() == null) {
                result.add(node);
            }    
        }
        return result;
    }

    private void sortDocumentNodesByRev(List<UploadToolOperationTreeNode> childNodes) {
        Collections.sort(childNodes, new Comparator<UploadToolOperationTreeNode>() {
            @Override
            public int compare(UploadToolOperationTreeNode o1, UploadToolOperationTreeNode o2) {
                if (o1.getNodeType() == UploadToolNodeType.D && o2.getNodeType() == UploadToolNodeType.D &&
                    o1.getRev() != null && o2.getRev() != null) {
                    return Integer.parseInt(o1.getRev()) - Integer.parseInt(o2.getRev());
                }
                return 0;
            }
        });
    }


    private List<String> createAttachmentList(UploadToolOperationTreeNode documentNode) {
        List<String> attachments = new ArrayList<String>();
        for (UploadToolOperationTreeNode node : documentNode.getChildNodes()) {
            attachments.add(node.getAbsolutePath());
        }
        return attachments;

    }
    
}


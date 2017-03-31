package com.volvo.phoenix.document.service.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.datatype.SolutionNameType;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.FolderDefaultAttribute;
import com.volvo.phoenix.document.entity.Operation;
import com.volvo.phoenix.document.entity.SolutionParam;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.FolderDefaultAttributeRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.repository.OperationRepository;
import com.volvo.phoenix.document.repository.SolutionParamRepository;
import com.volvo.phoenix.document.service.CopyManagerService;
import com.volvo.phoenix.orion.entity.OrionDocument;
import com.volvo.phoenix.orion.repository.OrionAclRepository;
import com.volvo.phoenix.orion.repository.OrionDocumentRepository;
import com.volvo.phoenix.orion.service.OrionSecurityService;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.util.OrionCMContext;
import com.volvo.phoenix.orion.util.SqlUtils;


public class CopyManagerServiceImplIntegrationTest extends AbstractTransactionalTestCase {

    private static final String CREATE_TEST_DATA = "./src/test/resources/createDocumentAndTreeTestData.sql";
    
    @Autowired
    private SqlUtils sqlUtils;
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;
    @Autowired
    private CopyManagerService copyManagerService;
    @Autowired
    private OrionSecurityService orionSecurityService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderDefaultAttributeRepository folderDefaultAttributeRepository;
    @Autowired
    private OrionDocumentRepository orionDocumentRepository;
    @Autowired
    private OrionAclRepository orionAclRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private SolutionParamRepository solutionParamRepository;
    
    @Before
    public void setupTestData() throws IOException, SQLException {
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});     
    }
    
    @Test
    public void shouldMoveDocumentAndUpdateACL() throws IOException, SQLException {
        // given
        long operationId = -1l;
        long previousRevDocId   = -1L;
        long targetFolderACLId  = -61L;

        Operation operation = operationRepository.findOne(operationId);        
        long documentId         = operation.getSourceId();
        long targetFolderNodeId = operation.getTargetFolderId();        
        
        Document document = documentRepository.findOne(documentId);
        OrionDocument orionDocument = orionDocumentRepository.findOne(documentId);
        assertThat(orionDocument).isNotNull();        
        assertThat(document).isNotNull();
        
        // when
        copyManagerService.moveDocument(documentId, targetFolderNodeId, new ArrayList<SolutionParam>());

        // then all versions shall be moved to destination folder
        orionDocument = orionDocumentRepository.findOne(documentId);
        document = documentRepository.findOne(documentId);
        
        assertThat(document.getFolder().getId().longValue()).isEqualTo(targetFolderNodeId);
        assertThat(orionDocument.getAcl().getId().longValue()).isEqualTo(targetFolderACLId);
        
        orionDocument = orionDocumentRepository.findOne(previousRevDocId);
        document = documentRepository.findOne(previousRevDocId);
        
        assertThat(document.getFolder().getId().longValue()).isEqualTo(targetFolderNodeId);
        assertThat(orionDocument.getAcl().getId().longValue()).isEqualTo(targetFolderACLId);
    }
    
    @Test
    public void shouldApplySolutionConnectDocTypeToDomainWhenDocumentMoved() throws IOException, SQLException {
        // given
        long operationId = -2L;
        long targetDomainId = -12L;
        
        
        Operation operation = operationRepository.findOne(operationId);
        
        long documentId     = operation.getSourceId();
        long targetFolderId = operation.getTargetFolderId();
        
        OrionDocument orionDocument = orionDocumentRepository.findOne(documentId);
        Document document = documentRepository.findOne(documentId);
      
        assertThat(document.getDomain().getDomainAttributes().size()).isEqualTo(3);
        assertThat(orionDocument).isNotNull();        
        assertThat(document.getDocumentAttributes().size()).isEqualTo(3);
        
        List<SolutionParam> solutions = solutionParamRepository.findByOperationId(operation.getId());

        // when
        copyManagerService.moveDocument(documentId, targetFolderId, solutions);

        // then document shall be moved to destination folder, 
        // then target domain shall have the source document type connected
        // then target domain shall have the source document type attributes connected
        document = documentRepository.findOne(documentId);
        
        assertThat(document.getFolder().getId().longValue()).isEqualTo(targetFolderId);
        assertThat(document.getDomain().getId()).isEqualTo(targetDomainId);
        assertThat(document.getDomain().getDocumentTypes().size()).isEqualTo(1);
        assertThat(document.getDomain().getDomainAttributes().size()).isEqualTo(4);
        
        assertThat(document.getDocumentAttributes().size()).isEqualTo(4);
        
        //Assertions.assertThat(document.getDocumentAttributes()).extracting("id").extracting("attribute").contains(-51L,-52L,-53L,-54L);
        //Assertions.assertThat(document.getDocumentAttributes()).extracting("value").contains(null,"-93", null, "-92");
    }
    
    @Test
    public void shouldApplySolutionSelectAnotherDocTypeWhenDocumentMoved() throws IOException, SQLException {
        // given
        
        long operationId=-3;
        long targetFamilyId = 3l;
        
        Operation operation = operationRepository.findOne(operationId);
        long documentId     =  operation.getSourceId();
        long targetFolderId = operation.getTargetFolderId();
        
        OrionDocument orionDocument = orionDocumentRepository.findOne(documentId);
        Document document = documentRepository.findOne(documentId);
      
        assertThat(document.getDomain().getDomainAttributes().size()).isEqualTo(3);
        assertThat(orionDocument).isNotNull();        
        assertThat(document.getDocumentAttributes().size()).isEqualTo(3);
        
        List<SolutionParam> solutions = solutionParamRepository.findByOperationId(operation.getId());
        
        // when
        copyManagerService.moveDocument(documentId, targetFolderId, solutions);

        // then document shall be moved to destination folder, 
        // then target domain shall have the source document type connected
        // then target domain shall have the source document type attributes connected
        document = documentRepository.findOne(documentId);
        
        assertThat(document.getFolder().getId().longValue()).isEqualTo(targetFolderId);
        assertThat(document.getType().getId()).isEqualTo(-32L);
        assertThat(document.getDocumentAttributes().size()).isEqualTo(3);
        assertThat(document.getFamily().getId()).isEqualTo(targetFamilyId);
        //Assertions.assertThat(document.getDocumentAttributes()).extracting("id").extracting("attribute").contains(-52L,-53L,-54L);
        //Assertions.assertThat(document.getDocumentAttributes()).extracting("value").contains("-91", null, "-92");
    }

    @Test
    public void shouldMoveSlaveFolderConvertToMaster() throws IOException, SQLException {
        // given
        
        long operationId = -4;
        
        
        Operation operation = operationRepository.findOne(operationId);
        
        long sourceFolderId    = operation.getSourceId();
        long sourceParentId    = -5L;
        long sourceParentAclId = -65L;
        long targetFolderId    =  operation.getTargetFolderId();

        Folder folder = folderRepository.findOne(sourceFolderId);
        assertThat(folder.getType()).isEqualTo(NodeType.S);
        List<FolderDefaultAttribute> parentAttributes = folderDefaultAttributeRepository.findById_FolderId(sourceParentId);
        assertThat(folder.getParent().getAcl().getWorkflowApprovers().size()).isEqualTo(4);
        assertThat(folder.getParent().getAcl().getDocumentTypes().size()).isEqualTo(2);
        
        List<Object[]> sourceACLMembers = orionAclRepository.findAclMembersByAclId(sourceParentAclId);
        assertThat(sourceACLMembers.size()).isEqualTo(3);
        
        List<Long> sourceParentFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(sourceParentAclId);
        assertThat(sourceParentFolderAdministators.size()).isEqualTo(2);
        
        // when
        copyManagerService.moveFolder(sourceFolderId, targetFolderId,  solutionParamRepository.findByOperationId(operation.getId()));

        // then folder shall be moved and become master
        folder = folderRepository.findOne(sourceFolderId);
        List<FolderDefaultAttribute> copiedAttributes = folderDefaultAttributeRepository.findById_FolderId(sourceFolderId);
        parentAttributes = folderDefaultAttributeRepository.findById_FolderId(sourceParentId);
        
        assertThat(folder.getType()).isEqualTo(NodeType.M);
        assertThat(folder.getParent().getId()).isEqualTo(targetFolderId);
        assertThat(copiedAttributes.size()).isGreaterThan(0);
        assertThat(copiedAttributes.size()).isEqualTo(parentAttributes.size());
        assertThat(folder.getAcl().getDocumentTypes().size()).isEqualTo(2);
        assertThat(folder.getAcl().getWorkflowApprovers().size()).isEqualTo(4);

        
        Folder oldParent = folderRepository.findOne(sourceParentId);
        assertThat(oldParent.getAcl().getWorkflowApprovers().size()).isEqualTo(4);
        assertThat(folder.getAcl().getId()).isNotEqualTo(oldParent.getAcl().getId());
        
        List<Object[]> targetACLMembers = orionAclRepository.findAclMembersByAclId(folder.getAcl().getId());
        assertThat(targetACLMembers.size()).isEqualTo(3);
        
        sourceParentFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(sourceParentAclId);
        assertThat(sourceParentFolderAdministators.size()).isEqualTo(2);
        
        List<Long> targetFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(folder.getAcl().getId());
        assertThat(targetFolderAdministators.size()).isEqualTo(2);
    }
    
    @Test
    public void shouldCopySlaveFolderConvertToMaster() throws IOException, SQLException, PhoenixDataAccessException {
        // given
        OrionCMContext ctx = new OrionCMContext("dummyUser", "dummyDB");
        long operationId = -4;
        
        Operation operation = operationRepository.findOne(operationId);
        
        
        long sourceFolderId    = operation.getSourceId();
        long sourceParentId    = -5L;
        long sourceParentAclId = -65L;
        long targetFolderId    = operation.getTargetFolderId();

        Folder folder = folderRepository.findOne(sourceFolderId);
        List<FolderDefaultAttribute> parentAttributes = folderDefaultAttributeRepository.findById_FolderId(sourceParentId);
        assertThat(parentAttributes.size()).isGreaterThan(0);
        assertThat(folder.getParent().getAcl().getWorkflowApprovers().size()).isEqualTo(4);
        assertThat(folder.getParent().getAcl().getDocumentTypes().size()).isEqualTo(2);
        
        List<Object[]> sourceACLMembers = orionAclRepository.findAclMembersByAclId(sourceParentAclId);
        assertThat(sourceACLMembers.size()).isEqualTo(3);
        
        List<Long> sourceParentFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(sourceParentAclId);
        assertThat(sourceParentFolderAdministators.size()).isEqualTo(2);
        
        // when
        copyManagerService.copyFolder(ctx, sourceFolderId, targetFolderId, solutionParamRepository.findByOperationId(operation.getId()));

        // then folder shall be copied and become master
        List<Folder> folders = folderRepository.findByParent_Id(targetFolderId);
        assertThat(folders.size()).isEqualTo(1);
        folder = folders.get(0);
        
        List<FolderDefaultAttribute> copiedAttributes = folderDefaultAttributeRepository.findById_FolderId(folder.getId());
        parentAttributes = folderDefaultAttributeRepository.findById_FolderId(sourceParentId);
        
        assertThat(folder.getId()).isNotEqualTo(sourceFolderId);
        assertThat(folder.getType()).isEqualTo(NodeType.M);
        assertThat(folder.getParent().getId()).isEqualTo(targetFolderId);
        assertThat(copiedAttributes.size()).isEqualTo(parentAttributes.size());
        assertThat(folder.getAcl().getWorkflowApprovers().size()).isEqualTo(4);
        assertThat(folder.getAcl().getDocumentTypes().size()).isEqualTo(2);
        
        Folder oldParent = folderRepository.findOne(sourceParentId);
        assertThat(oldParent.getAcl().getWorkflowApprovers().size()).isEqualTo(4);
        assertThat(folder.getAcl().getId()).isNotEqualTo(oldParent.getAcl().getId());
        
        List<Object[]> targetACLMembers = orionAclRepository.findAclMembersByAclId(folder.getAcl().getId());
        assertThat(targetACLMembers.size()).isEqualTo(3);
        
        sourceParentFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(sourceParentAclId);
        assertThat(sourceParentFolderAdministators.size()).isEqualTo(2);
        
        List<Long> targetFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(folder.getAcl().getId());
        assertThat(targetFolderAdministators.size()).isEqualTo(2);
        
        List<Folder> copiedSubFolders = folderRepository.findByParent_Id(folder.getId());
        assertThat(copiedSubFolders.size()).isEqualTo(1);
        assertThat(copiedSubFolders.get(0).getType()).isEqualTo(NodeType.M);
        assertThat(copiedSubFolders.get(0).getId()).isNotEqualTo(-511L);
        
        Long subFolderLevel3Id = copiedSubFolders.get(0).getId();
        copiedSubFolders = folderRepository.findByParent_Id(subFolderLevel3Id);
        assertThat(copiedSubFolders.size()).isEqualTo(1);
        assertThat(copiedSubFolders.get(0).getType()).isEqualTo(NodeType.S);
        assertThat(copiedSubFolders.get(0).getId()).isNotEqualTo(-5111L);
    }
    
    @Test
    public void shouldCopyMasterFolder() throws IOException, SQLException, PhoenixDataAccessException {
        // given
        OrionCMContext ctx = new OrionCMContext("dummyUser", "dummyDB");
        
        long operationID= -5;
        
        Operation operation = operationRepository.findOne(operationID);
        
        long sourceFolderId = operation.getSourceId();
        long sourceAclId    = -651L;
        long targetFolderId = operation.getTargetFolderId();
        
        Folder folder = folderRepository.findOne(sourceFolderId);
        assertThat(folder.getType()).isEqualTo(NodeType.M);
        List<FolderDefaultAttribute> sourceAttributes = folderDefaultAttributeRepository.findById_FolderId(sourceFolderId);
        assertThat(sourceAttributes.size()).isGreaterThan(0);
        assertThat(folder.getParent().getAcl().getWorkflowApprovers().size()).isEqualTo(4);
        assertThat(folder.getParent().getAcl().getDocumentTypes().size()).isEqualTo(2);
        
        List<Object[]> sourceACLMembers = orionAclRepository.findAclMembersByAclId(sourceAclId);
        assertThat(sourceACLMembers.size()).isEqualTo(3);
        
        List<Long> sourceFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(sourceAclId);
        assertThat(sourceFolderAdministators.size()).isEqualTo(2);
        
        // when
        copyManagerService.copyFolder(ctx, sourceFolderId, targetFolderId, solutionParamRepository.findByOperationId(operation.getId()));

        // then folder shall be copied and become master
        List<Folder> folders = folderRepository.findByParent_Id(targetFolderId);
        assertThat(folders.size()).isEqualTo(1);
        folder = folders.get(0);
        
        List<FolderDefaultAttribute> copiedAttributes = folderDefaultAttributeRepository.findById_FolderId(folder.getId());
        sourceAttributes = folderDefaultAttributeRepository.findById_FolderId(sourceFolderId);
        
        assertThat(folder.getId()).isNotEqualTo(sourceFolderId);
        assertThat(folder.getType()).isEqualTo(NodeType.M);
        assertThat(folder.getParent().getId()).isEqualTo(targetFolderId);
        assertThat(copiedAttributes.size()).isEqualTo(sourceAttributes.size());
        assertThat(folder.getAcl().getWorkflowApprovers().size()).isEqualTo(1);
        assertThat(folder.getAcl().getDocumentTypes().size()).isEqualTo(1);
        
        List<Object[]> targetACLMembers = orionAclRepository.findAclMembersByAclId(folder.getAcl().getId());
        assertThat(targetACLMembers.size()).isEqualTo(3);
        
        sourceFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(sourceAclId);
        assertThat(sourceFolderAdministators.size()).isEqualTo(2);
        
        List<Long> targetFolderAdministators = orionSecurityService.getPhoenixAdministratorsForACL(folder.getAcl().getId());
        assertThat(targetFolderAdministators.size()).isEqualTo(2);
        
        List<Folder> copiedSubFolders = folderRepository.findByParent_Id(folder.getId());
        assertThat(copiedSubFolders.size()).isEqualTo(1);
        assertThat(copiedSubFolders.get(0).getType()).isEqualTo(NodeType.S);
        assertThat(copiedSubFolders.get(0).getId()).isNotEqualTo(-5111L);
    }
    
    private SolutionParam createSolutionParam(SolutionNameType solutionType, Long paramId, String value) {
        SolutionParam solution = new SolutionParam();
        solution.setSolution(solutionType);
        solution.setParamId(paramId);
        solution.setValue(value);
        return solution;
    }
    
}

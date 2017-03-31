package com.volvo.phoenix.document.uploadtool.application;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.datatype.InfoClass;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolDocumentAttributeValueDTO;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolDocumentDTO;
import com.volvo.phoenix.document.uploadtool.application.dto.UploadToolTreeNodeDTO;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolDocumentRepository;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationRepository;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationTreeNodeRepository;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocument;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocumentStatus;
import com.volvo.phoenix.document.uploadtool.model.UploadToolFileType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolNodeType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationFile;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationStatus;
import com.volvo.phoenix.orion.util.SqlUtils;

public class UploadToolServiceIntegrationTest extends AbstractTransactionalTestCase {
    
    private static final String CREATE_TEST_DATA = "./src/test/resources/createUploadToolOperationTestData2.sql";
    
    @Autowired
    private SqlUtils sqlUtils;
    
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;
    
    @Autowired
    private UploadToolService uploadToolService;
    
    @Autowired
    private UploadToolOperationRepository repository;
    
    @Autowired
    private UploadToolDocumentRepository uploadToolDocumentRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private UploadToolOperationTreeNodeRepository treeNodeRepository;
    

    @Ignore
    @Test
    public void shouldAddFileInformationToOperation() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        List<UploadToolOperationFile> uploadedFiles = createTestUploadedFilesList();
        
        // when
        uploadToolService.addUploadedFilesInformation(-1L, uploadedFiles);

        // then
        UploadToolOperation operation = repository.findOne(-1L);
        assertThat(operation.getUploadedFiles().size()).isEqualTo(4);
        assertThat(operation.getUploadedFiles().get(2).getFileName()).isEqualTo("TestFile3.zip");
        assertThat(operation.getUploadedFiles().get(2).getId()).isNotNull();
    }
    
    /**
     * Must be ignored unless any documents are checked in with Orion, due to non-transactional nature of Orion API
     * @throws Exception 
     */
    @Test @Ignore
    public void shouldApplyUpload() throws Exception {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        
        // when
        uploadToolService.applyUpload(-2L);

        // then verify the tree structure updated with references to created folders in Phoenix
        UploadToolOperation operation = uploadToolService.getOperation(-2L);
        assertThat(operation.getStatus()).isEqualTo(UploadToolOperationStatus.SUCCEEDED);
        assertThat(operation.getUploadedTree().size()).isEqualTo(5);
        assertThat(operation.getUploadedTree().get(0).getNodeText()).isEqualTo("Marine");
        
        Long createdMarineSlaveFolderId = treeNodeRepository.findOne(operation.getUploadedTree().get(0).getNodeId()).getPhoenixObjectReference();
        assertThat(createdMarineSlaveFolderId).isNotNull();
        
        // then 1st Slave Folder shall get created in Phoenix
        Folder createdMarineSlaveFolder = folderRepository.findOne(createdMarineSlaveFolderId);
        assertThat(createdMarineSlaveFolder).isNotNull();
        assertThat(createdMarineSlaveFolder.getText()).isEqualTo("Marine");
        assertThat(createdMarineSlaveFolder.getDescription()).isEqualTo("Marine");
        assertThat(createdMarineSlaveFolder.getOwner()).isEqualTo("bpl3195");
        assertThat(createdMarineSlaveFolder.getParent().getId()).isEqualTo(operation.getFolder().getId());
        assertThat(createdMarineSlaveFolder.getAcl().getId()).isEqualTo(operation.getFolder().getAcl().getId());
        
        assertThat(operation.getUploadedTree().get(1).getNodeText()).isEqualTo("Document under Marine");
        Long created1stPhoenixDocumentId = treeNodeRepository.findOne(operation.getUploadedTree().get(1).getNodeId()).getPhoenixObjectReference(); 
        assertThat(created1stPhoenixDocumentId).isNotNull();
        
        Document created1stPhoenixDocument = documentRepository.findOne(created1stPhoenixDocumentId);
        assertThat(created1stPhoenixDocument).isNotNull();
        assertThat(created1stPhoenixDocument.getTitle()).isEqualTo("Document under Marine");
        
        assertThat(operation.getUploadedTree().get(3).getNodeText()).isEqualTo("Folder under Marine");
 
        Long created2ndSlaveFolderId = treeNodeRepository.findOne(operation.getUploadedTree().get(3).getNodeId()).getPhoenixObjectReference(); 
        assertThat(created2ndSlaveFolderId).isNotNull();   
        
        // then 2nd Slave Folder shall get created in Phoenix
        Folder created2ndSlaveFolder = folderRepository.findOne(created2ndSlaveFolderId);
        assertThat(created2ndSlaveFolder).isNotNull();
        assertThat(created2ndSlaveFolder.getText()).isEqualTo("Folder under Marine");
        assertThat(created2ndSlaveFolder.getParent().getId()).isEqualTo(createdMarineSlaveFolder.getId());
        assertThat(created2ndSlaveFolder.getAcl().getId()).isEqualTo(operation.getFolder().getAcl().getId());
        
        assertThat(operation.getUploadedTree().get(4).getNodeText()).isEqualTo("Document under Folder under Marine");
        Long created2ndPhoenixDocumentId = treeNodeRepository.findOne(operation.getUploadedTree().get(4).getNodeId()).getPhoenixObjectReference(); 
        assertThat(created2ndPhoenixDocumentId).isNotNull();
        
        Document created2ndPhoenixDocument = documentRepository.findOne(created2ndPhoenixDocumentId);
        assertThat(created2ndPhoenixDocument).isNotNull();
        assertThat(created2ndPhoenixDocument.getTitle()).isEqualTo("Document under Folder under Marine");
    }
    
    @Test
    public void shouldAddDocumentTree() throws IOException, SQLException, ParseException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        List<UploadToolTreeNodeDTO> uploadedDocumentTree = createTestDocumentTree();
        
        // when
        uploadToolService.addDocumentTree(-1L, uploadedDocumentTree);

        // then verify the tree structure
        UploadToolOperation operation = uploadToolService.getOperation(-1L);
        assertThat(operation.getUploadedTree().size()).isEqualTo(8);
        assertThat(operation.getUploadedTree().get(5).getNodeText()).isEqualTo("Marine");
        assertThat(operation.getUploadedTree().get(5).getChildNodes().get(0).getParentNode()).isEqualTo(operation.getUploadedTree().get(5));
        assertThat(operation.getUploadedTree().get(6).getNodeText()).isEqualTo("Document under Marine folder");
        assertThat(operation.getUploadedTree().get(6).getDocumentAttributes().size()).isEqualTo(2);
        assertThat(operation.getUploadedTree().get(6).getChildNodes().get(0).getParentNode()).isEqualTo(operation.getUploadedTree().get(6));
        assertThat(operation.getUploadedTree().get(7).getNodeText()).isEqualTo("TestPlan.docx");
    }
    
    /**
     * To run the following test you need the transaction to be commited, after execution of domain methods.
     * 
     * To execute the test just once on your local environment, please comment out the annotation @Transactional(propagation = Propagation.REQUIRES_NEW) 
     * in UploadToolDocumentServiceImpl.java
     */
    @Test @Ignore
    public void shouldValidateDocumentTree() throws IOException, SQLException, ParseException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});

        // when
        uploadToolService.validateDocumentTree(-1L);
        
        // then verify the test document having metadata specified for all mandatory attributes
        UploadToolDocument doc = uploadToolDocumentRepository.findDocByTreeNodeId(-1001L);
        
        assertThat(doc.getTitle()).isEqualTo("Approve testing 3");
        assertThat(doc.getRevision()).isEqualTo(1);
        assertThat(doc.getName()).isEqualTo("-50239949");
        assertThat(doc.getAltDocId()).isEqualTo("50239949");
        assertThat(doc.getIssueDate().compareTo(new SimpleDateFormat("yyyy-MM-dd").parse("2015-03-17"))).isEqualTo(0);
        assertThat(doc.getStatus()).isEqualTo(UploadToolDocumentStatus.WORK);
        assertThat(doc.getProtectInWork()).isFalse();
        assertThat(doc.getAuthor()).isEqualTo("Krzysztof Kocik");
        assertThat(doc.getAuthorId()).isEqualTo("bpl3195");
        assertThat(doc.getIssuer()).isEqualTo("Jan Nowak");
        assertThat(doc.getIssuerId()).isNullOrEmpty();
        assertThat(doc.getDescription()).isEqualTo("Test description");
        assertThat(doc.getNotes()).isEqualTo("Test notes");

        assertThat(doc.getStateId()).isEqualTo(InfoClass.STRICTLY_CONFIDENTIAL.getId());
        assertThat(doc.getType().getId()).isEqualTo(-31);
        assertThat(doc.getFamily().getId()).isEqualTo(-61);
        assertThat(doc.getAttributesValues().size()).isEqualTo(4);
        assertThat(doc.getAttributesValues().get(0).getAttribute()).isEqualTo(-51);
        assertThat(doc.getAttributesValues().get(0).getOwningDocument()).isNotNull();
        assertThat(doc.getAttributesValues().get(0).getValue()).isEqualTo("Feature");
        assertThat(doc.getAttributesValues().get(1).getAttribute()).isEqualTo(-52);
        assertThat(doc.getAttributesValues().get(1).getValue()).isEqualTo("Y");
        assertThat(doc.getAttributesValues().get(3).getAttribute()).isEqualTo(-54);
        assertThat(doc.getAttributesValues().get(3).getValue()).isEqualTo("1500");
        assertThat(doc.getAttributesValues().get(2).getAttribute()).isEqualTo(-53);
        assertThat(doc.getAttributesValues().get(2).getValue()).isEqualTo("-2");
        
        assertThat(doc.getTreeNode().getValid()).isTrue();
        assertThat(doc.getTreeNode().getParentNode().getValid()).isTrue();
        
        // then verify the test document having only Registration number specified and it was existing in Phoenix
        doc = uploadToolDocumentRepository.findDocByTreeNodeId(-102L);
        
        assertThat(doc.getTitle()).isEqualTo("Existing doc in Phoenix");
        assertThat(doc.getRevision()).isEqualTo(3);
        assertThat(doc.getName()).isEqualTo("-502000");
        assertThat(doc.getAltDocId()).isNull();
        assertThat(doc.getStatus()).isEqualTo(UploadToolDocumentStatus.WORK);
        assertThat(doc.getProtectInWork()).isFalse();

        assertThat(doc.getStateId()).isEqualTo(InfoClass.INTERNAL.getId());
        assertThat(doc.getType().getId()).isEqualTo(-31);
        assertThat(doc.getFamily().getId()).isEqualTo(-61);
        assertThat(doc.getAttributesValues().size()).isEqualTo(2);
        assertThat(doc.getAttributesValues().get(0).getAttribute()).isEqualTo(-51);
        assertThat(doc.getAttributesValues().get(0).getOwningDocument()).isNotNull();
        assertThat(doc.getAttributesValues().get(0).getValue()).isEqualTo("Existing atr value");
        assertThat(doc.getAttributesValues().get(1).getAttribute()).isEqualTo(-52);
        assertThat(doc.getAttributesValues().get(1).getValue()).isEqualTo("Y");
        
        assertThat(doc.getTreeNode().getValid()).isTrue();
        
        // then verify the test document having no parsed metadata for which New Document Defaults were applied
        doc = uploadToolDocumentRepository.findDocByTreeNodeId(-103L);
        
        assertThat(doc.getTitle()).isEqualTo("Document with no metadata");
        assertThat(doc.getStatus()).isEqualTo(UploadToolDocumentStatus.WORK);
        assertThat(doc.getStateId()).isEqualTo(InfoClass.INTERNAL.getId());
        assertThat(doc.getType().getId()).isEqualTo(-31);
        assertThat(doc.getFamily().getId()).isEqualTo(-61);
        assertThat(doc.getAttributesValues().size()).isEqualTo(0);
        
        assertThat(doc.getTreeNode().getValid()).isFalse();
    }
    /**
     * To run the following test you need the transaction to be commited, after execution of domain methods.
     * 
     * To execute the test just once on your local environment, please comment out the annotation @Transactional(propagation = Propagation.REQUIRES_NEW) 
     * in UploadToolDocumentServiceImpl.java
     */
    @Test @Ignore
    public void shouldValidatetMandatoryAttrValues() throws IOException, SQLException, ParseException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});

        // when
        uploadToolService.validateDocumentTree(-1L);
        
        // then verify the test document having metadata specified for all mandatory attributes
         List<UploadToolDocumentAttributeValueDTO> mandatoryAttrValues = uploadToolService.getDocumentMandatoryAttrValues(-1001L);
         assertThat(mandatoryAttrValues.size()).isEqualTo(2);
         assertThat(mandatoryAttrValues.get(0).getLabel()).isEqualTo("Test text attribute");
      
         assertThat(mandatoryAttrValues.get(0).getType()).isEqualTo("TEXT");
         assertThat(mandatoryAttrValues.get(0).getValue()).isEqualTo("Feature");
         
         assertThat(mandatoryAttrValues.get(1).getLabel()).isEqualTo("Test checkbox attribute");
      
         assertThat(mandatoryAttrValues.get(1).getType()).isEqualTo("CHECKBOX");
         assertThat(mandatoryAttrValues.get(1).getValue()).isEqualTo("Y");
    }
    /**
     * To run the following test you need the transaction to be commited, after execution of domain methods.
     * 
     * To execute the test just once on your local environment, please comment out the annotation @Transactional(propagation = Propagation.REQUIRES_NEW) 
     * in UploadToolDocumentServiceImpl.java
     */
    @Test @Ignore
    public void shouldValidatetOptionalAttrValues() throws IOException, SQLException, ParseException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});

        // when
        uploadToolService.validateDocumentTree(-1L);
        
        // then verify the test document having metadata specified for all mandatory attributes
         List<UploadToolDocumentAttributeValueDTO> optionalAttrValues = uploadToolService.getDocumentOptionalAttrValues(-1001L);
         assertThat(optionalAttrValues.size()).isEqualTo(6);
         
         
         assertThat(optionalAttrValues.get(0).getLabel()).isEqualTo("Test text attribute");
      
         assertThat(optionalAttrValues.get(0).getType()).isEqualTo("TEXT");
         assertThat(optionalAttrValues.get(0).getValue()).isEqualTo("Feature");
         
         assertThat(optionalAttrValues.get(1).getLabel()).isEqualTo("Test text attribute");
      
         assertThat(optionalAttrValues.get(1).getType()).isEqualTo("TEXT");
         assertThat(optionalAttrValues.get(1).getValue()).isEqualTo("Feature");
    }
    
    
    
    private UploadToolDocumentDTO createTestDocumentWithAttributes() {
        UploadToolDocumentDTO doc = new UploadToolDocumentDTO();
        doc.addDocumentProperty("Registration number", "50239949");
        doc.addDocumentProperty("Alternative number", "50239949");
        return doc;
    }
    
 
    @Test
    public void shouldFindUploadedZIPFilesWithNoMetadata() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        
        // when
        List<String> filesWithNoMetadata = uploadToolService.getUploadedZIPFilesWithNoMetadata(-1L);

        // then
        assertThat(filesWithNoMetadata.size()).isEqualTo(1);
        assertThat(filesWithNoMetadata.get(0)).isEqualTo("Test1.zip");
    }
    
    private List<UploadToolTreeNodeDTO> createTestDocumentTree() {
        List<UploadToolTreeNodeDTO> result = new ArrayList<UploadToolTreeNodeDTO>();
        
        UploadToolTreeNodeDTO slaveFolder = new UploadToolTreeNodeDTO();
        slaveFolder.setName("Marine");
        slaveFolder.setNodeType(UploadToolNodeType.S);
       
        UploadToolTreeNodeDTO documentNode = new UploadToolTreeNodeDTO();
        documentNode.setName("Document under Marine folder");
        documentNode.setNodeType(UploadToolNodeType.D);
        documentNode.setDocument(createTestDocumentWithAttributes());
        
        UploadToolTreeNodeDTO attachment = new UploadToolTreeNodeDTO();
        attachment.setName("TestPlan.docx");
        attachment.setNodeType(UploadToolNodeType.F);   
        
        documentNode.getChildren().add(attachment);
        slaveFolder.getChildren().add(documentNode);
        result.add(slaveFolder);
        
        return result;
    }

    private List<UploadToolOperationFile> createTestUploadedFilesList() {
        List<UploadToolOperationFile> fileInfoList = new ArrayList<UploadToolOperationFile>();
        
        UploadToolOperationFile file = new UploadToolOperationFile();
        file.setFileName("TestFile3.zip");
        file.setFileType(UploadToolFileType.Z);
    //    file.setHasMetadata(true);
        
        
        fileInfoList.add(file);
        
        UploadToolOperationFile file1 = new UploadToolOperationFile();
        file1.setFileName("TestFile4.xlsx");
        file1.setFileType(UploadToolFileType.D);
     //   file1.setHasMetadata(false);
        
        fileInfoList.add(file1);
        
        return fileInfoList;
    }

}

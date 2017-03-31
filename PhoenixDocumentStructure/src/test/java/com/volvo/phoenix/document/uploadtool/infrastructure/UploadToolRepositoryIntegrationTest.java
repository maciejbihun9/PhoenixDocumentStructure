package com.volvo.phoenix.document.uploadtool.infrastructure;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocument;
import com.volvo.phoenix.document.uploadtool.model.UploadToolDocumentAttributeValue;
import com.volvo.phoenix.document.uploadtool.model.UploadToolFileType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolNodeType;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationFile;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationStatus;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperationTreeNode;
import com.volvo.phoenix.orion.util.SqlUtils;

public class UploadToolRepositoryIntegrationTest extends AbstractTransactionalTestCase {

    private static final String CREATE_TEST_DATA = "./src/test/resources/createUploadToolOperationTestData.sql";

    @Autowired
    private SqlUtils sqlUtils;

    @Autowired
    @Named("dataSource")
    private DataSource dataSource;

    @Autowired
    private UploadToolOperationRepository operationRepository;

    @Autowired
    private UploadToolDocumentRepository documentRepository;

    @Test
    public void shouldAddFileInformationToOperation() throws IOException, SQLException, InterruptedException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});

        UploadToolOperation operation = operationRepository.findOne(-1L);
        assertThat(operation).isNotNull();

        UploadToolOperationFile file = new UploadToolOperationFile();
        file.setFileName("TestFile3.zip");
        file.setFileType(UploadToolFileType.Z);
        operation.getUploadedFiles().add(file);

        // when
        operation.getUploadedFiles().size();
        operation = operationRepository.save(operation);

        // then
        assertThat(operation).isNotNull();
        assertThat(operation.getUploadedFiles().size()).isEqualTo(3);
        assertThat(operation.getUploadedFiles().get(2).getFileName()).isEqualTo("TestFile3.zip");
        assertThat(operation.getUploadedFiles().get(2).getId()).isNotNull();
    }
    
    @Test
    public void shouldAddSlaveFolderAndADocumentToTree() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        UploadToolOperation operation = operationRepository.findOne(-1L);
        assertThat(operation).isNotNull();
        assertThat(operation.getUploadedTree().size()).isEqualTo(3);

        // when
        UploadToolOperationTreeNode slaveFolder = new UploadToolOperationTreeNode();
        slaveFolder.setNodeText("Industri");
        slaveFolder.setNodeType(UploadToolNodeType.S);

        UploadToolOperationTreeNode docNode = new UploadToolOperationTreeNode();
        docNode.setNodeText("Document under Industri slave folder");
        docNode.setNodeType(UploadToolNodeType.D);
        docNode.setParentNode(slaveFolder);

        slaveFolder.getChildNodes().add(docNode);

        operation.getUploadedTree().add(slaveFolder); // add 4th node
        operation.getUploadedTree().add(docNode); // add 5th node

        operation = operationRepository.save(operation);
        operation = operationRepository.findOne(-1L);
        // then
        assertThat(operation.getUploadedTree().size()).isEqualTo(5);
        assertThat(operation.getUploadedTree().get(3).getNodeType()).isEqualTo(UploadToolNodeType.S);
        assertThat(operation.getUploadedTree().get(3).getNodeId()).isNotNull();
        assertThat(operation.getUploadedTree().get(3).getNodeText()).isEqualTo("Industri");
        assertThat(operation.getUploadedTree().get(3).getChildNodes().get(0).getNodeType()).isEqualTo(UploadToolNodeType.D);
        // then parent and child information must match
        assertThat(operation.getUploadedTree().get(3).getChildNodes().get(0).getParentNode()).isEqualTo(operation.getUploadedTree().get(3));
        assertThat(operation.getUploadedTree().get(4).getNodeId()).isNotNull();
    }
    
    @Test
    public void shouldFindUploadToolOperation() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});

        // when
        UploadToolOperation operation = operationRepository.findOne(-1L);

        // then
        assertThat(operation).isNotNull();
        assertThat(operation.getStatus()).isEqualTo(UploadToolOperationStatus.CREATED);
        assertThat(operation.getUploadedFiles().size()).isEqualTo(2);
        assertThat(operation.getUploadedFiles().get(1).getFileType()).isEqualTo(UploadToolFileType.D);
        assertThat(operation.getUploadedTree().size()).isEqualTo(3);
        assertThat(operation.getUploadedTree().get(2).getNodeType()).isEqualTo(UploadToolNodeType.F);
        assertThat(operation.getUploadedTree().get(2).getParentNode().getNodeType()).isEqualTo(UploadToolNodeType.D);
        assertThat(operation.getUploadedTree().get(1).getParentNode().getNodeType()).isEqualTo(UploadToolNodeType.S);
        assertThat(operation.getUploadedTree().get(1).getChildNodes().size()).isEqualTo(1);
        assertThat(operation.getUploadedTree().get(1).getDocumentAttributes().size()).isEqualTo(1);
    }
    
    @Test
    public void shoulGetUploadToolDocumentWithAllDocumentAttributes() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        // when
        UploadToolDocument uploadToolDocument = documentRepository.findOne(-1L);
        List<UploadToolDocumentAttributeValue> uploadToolDocumentAttributesValues = uploadToolDocument.getAttributesValues();

        // then
        assertThat(uploadToolDocument.getFamily()).isNotNull();
        assertThat(uploadToolDocument.getFamily().getName()).isEqualTo("FAMILY_TEST_NAME");
        assertThat(uploadToolDocumentAttributesValues.size()).isEqualTo(3);
        assertThat(uploadToolDocumentAttributesValues.get(2).getValue()).isEqualTo("value_3");
    }

}

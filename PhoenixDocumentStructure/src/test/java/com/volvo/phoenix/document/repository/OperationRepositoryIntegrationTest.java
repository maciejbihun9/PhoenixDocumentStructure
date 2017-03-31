package com.volvo.phoenix.document.repository;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.datatype.ItemType;
import com.volvo.phoenix.document.datatype.OperationStatus;
import com.volvo.phoenix.document.datatype.OperationType;
import com.volvo.phoenix.document.entity.DocumentOperation;
import com.volvo.phoenix.document.entity.Operation;
import com.volvo.phoenix.orion.util.SqlUtils;


public class OperationRepositoryIntegrationTest extends AbstractTransactionalTestCase {

    private static final String CREATE_TEST_DATA = "./src/test/resources/createCMOperationTestData.sql";
    
    @Autowired
    private SqlUtils sqlUtils;
    
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;
    
    @Inject
    private OperationRepository operationRepository;

    @Test
    public void shouldCreateAndPersistOperation() {
        // given
        DocumentOperation docOperation = new DocumentOperation();
        docOperation.setCreateDate(new Date());
        docOperation.setOperationType(OperationType.COPY);
        docOperation.setSourceDocumenId(-2L);
        docOperation.setTargetFolderId(-3L);
        docOperation.setStatus(OperationStatus.CREATED);
        docOperation.setUser("dummyUser");

        // when
        operationRepository.save(docOperation);

        Operation foundOperation = operationRepository.findOne(docOperation.getId());

        // then
        assertThat(operationRepository.exists(docOperation.getId())).isTrue();
        assertThat(foundOperation).isNotNull();
        assertThat(foundOperation).isInstanceOf(DocumentOperation.class);
        assertThat(foundOperation.getOperationType()).isEqualTo(docOperation.getOperationType());

    }
    
    @Test
    public void shouldFindOperationAndCastToProperOperationType() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        Long documentOperationIDinDB = Long.valueOf(-1);
        // when
        Operation foundOperation = operationRepository.findOne(documentOperationIDinDB);

        // then
        assertThat(foundOperation).isNotNull();
        assertThat(foundOperation).isInstanceOf(DocumentOperation.class);
        assertThat(foundOperation.getSourceType()).isEqualTo(ItemType.DOCUMENT);
    }
}

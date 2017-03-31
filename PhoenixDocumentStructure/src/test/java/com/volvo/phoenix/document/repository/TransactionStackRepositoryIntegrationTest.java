package com.volvo.phoenix.document.repository;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.datatype.TransactionStackEventType;
import com.volvo.phoenix.document.datatype.TransactionStackSystemType;
import com.volvo.phoenix.document.entity.TransactionStack;
import com.volvo.phoenix.orion.util.SqlUtils;


public class TransactionStackRepositoryIntegrationTest extends AbstractTransactionalTestCase {

    private static final String CREATE_TEST_DATA = "./src/test/resources/createTransactionStackTestData.sql";
    
    @Autowired
    private SqlUtils sqlUtils;
    
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;
    
    @Inject
    private TransactionStackRepository transactionStackRepository;

    @Test
    public void shouldCreateAndPersistTransactionStackEntry() {
        // given
        TransactionStack entry = new TransactionStack();
        entry.setDocumentId(-2L);
        entry.setEventDate(new Date());
        entry.setTransactionType(TransactionStackEventType.A);
        entry.setSystemType(TransactionStackSystemType.S);
        
        // when
        transactionStackRepository.save(entry);

        List<TransactionStack> foundEntries = transactionStackRepository.findById_DocumentId(-2L);

        // then
        assertThat(foundEntries).isNotNull();
        assertThat(foundEntries.size()).isEqualTo(1);
        assertThat(foundEntries.get(0).getDocumentId()).isEqualTo(-2L);
        assertThat(foundEntries.get(0).getTransactionType()).isEqualTo(TransactionStackEventType.A);
        assertThat(foundEntries.get(0).getSystemType()).isEqualTo(TransactionStackSystemType.S);
        
    }
    
    @Test
    public void shouldFindTransactionStackEntry() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        
        // when
        List<TransactionStack> foundEntries = transactionStackRepository.findById_DocumentId(-1L);

        // then
        assertThat(foundEntries).isNotNull();
        assertThat(foundEntries.size()).isEqualTo(1);
        assertThat(foundEntries.get(0).getDocumentId()).isEqualTo(-1L);
        assertThat(foundEntries.get(0).getTransactionType()).isEqualTo(TransactionStackEventType.U);
        assertThat(foundEntries.get(0).getSystemType()).isEqualTo(TransactionStackSystemType.E);
    }
}

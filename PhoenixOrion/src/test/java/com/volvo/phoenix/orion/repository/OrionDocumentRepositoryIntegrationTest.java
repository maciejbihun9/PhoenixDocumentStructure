package com.volvo.phoenix.orion.repository;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;

import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.orion.entity.OrionAcl;
import com.volvo.phoenix.orion.entity.OrionDocument;
import com.volvo.phoenix.orion.util.SqlUtils;

public class OrionDocumentRepositoryIntegrationTest extends AbstractTransactionalTestCase { 
    
    private static final String CREATE_TEST_DATA = "./src/test/resources/createOrionDocumentTestData.sql";
    
    @Autowired
    private SqlUtils sqlUtils;
    
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;
    
    @Autowired
    private OrionDocumentRepository repository;
    
    @Autowired
    private OrionAclRepository aclRepository;
    
    @Before
    public void setupTestData() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});     
    }
    
    @Test
    public void shouldFindComponentsByDocumentId() throws IOException, SQLException {
        // when
        OrionDocument doc = repository.findOne(-1001L);
        
        //then 
        assertThat(doc).isNotNull();
        assertThat(doc.getComponents().size()).isEqualTo(1);
        assertThat(doc.getComponents().get(0).getRepresentations().get(0).getRepresentationInfo().getAlasType()).isEqualTo("PDF");
        assertThat(doc.getComponents().get(0).getRepresentations().get(0).getFiles().get(0).getInputName()).isEqualTo("testFile.pdf");
    }
    
    @Test
    public void shouldFindDocumentNameByDocumentId() throws IOException, SQLException {
        // when
        String name = repository.getDocumentName(-1001L);
        
        //then 
        assertThat(name).isEqualTo("Copy manager test document");
    }
    
    @Test
    public void shouldFindLatestDocumentVersion() throws IOException, SQLException {
        // when
        OrionDocument doc = repository.findFirstByNameOrderByRevisionDesc("Test document 2 revs");
        
        //then 
        assertThat(doc).isNotNull();
        assertThat(doc.getId()).isEqualTo(-1003);
        assertThat(doc.getRevision()).isEqualTo("2");
    }
    
    @Test
    public void shouldCreateNewACL() throws IOException, SQLException {
        // given new ACL with manually assigned new ID
        OrionAcl acl = new OrionAcl();
        acl.setName("test");
        acl.setId(-1L);
        
        // when
        acl = aclRepository.save(acl);
        
        //then 
        assertThat(acl.getId()).isNotNull();
    }

}

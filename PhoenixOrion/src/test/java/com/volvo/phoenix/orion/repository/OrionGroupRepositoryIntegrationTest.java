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
import com.volvo.phoenix.orion.entity.OrionGroup;
import com.volvo.phoenix.orion.util.SqlUtils;

public class OrionGroupRepositoryIntegrationTest extends AbstractTransactionalTestCase { 
    
    private static final String CREATE_TEST_DATA = "./src/test/resources/createOrionDocumentTestData.sql";
    
    @Autowired
    private SqlUtils sqlUtils;
    
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;
    
    @Autowired
    private OrionGroupRepository repository;
    
    @Before
    public void setupTestData() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});     
    }
    
    @Test
    public void testFindByUsernameIgnoreCase() {
        //given
        Long testGroupId = -1011L;
        
        //when
        OrionGroup group = repository.findOne(testGroupId);
        
        // then
        assertThat(group).isNotNull();
        assertThat(group.getOwnerId()).isEqualTo(-1);
        assertThat(group.getMembers()).isNotNull();
        assertThat(group.getMembers().size()).isEqualTo(2);
        //assertThat(group.getMembers().get(0).getId().getUserId()).isEqualTo(-1031);
    }


}

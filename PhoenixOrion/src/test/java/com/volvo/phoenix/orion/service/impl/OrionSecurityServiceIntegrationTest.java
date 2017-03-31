package com.volvo.phoenix.orion.service.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.orion.repository.OrionAclRepository;
import com.volvo.phoenix.orion.repository.OrionGroupRepository;
import com.volvo.phoenix.orion.service.OrionSecurityService;
import com.volvo.phoenix.orion.util.SqlUtils;

public class OrionSecurityServiceIntegrationTest extends AbstractTransactionalTestCase { 
    
    private static final String CREATE_TEST_DATA = "./src/test/resources/createOrionDocumentTestData.sql";
    
    @Autowired
    private SqlUtils sqlUtils;
    
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;
    
    @Autowired
    private OrionSecurityService orionSecurityService;
    
    @Autowired
    private OrionAclRepository orionAclRepository;
    
    @Autowired
    private OrionGroupRepository orionGroupRepository;

    @Before
    public void setupTestData() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});     
    }
    
    @Test
    public void shouldFindACLAdministrators() throws IOException, SQLException {
        // given
        long aclId = -1001L;
        // when
        List<Long> administrators = orionSecurityService.getPhoenixAdministratorsForACL(aclId);
        
        //then 
        assertThat(administrators).isNotNull();
        assertThat(administrators.size()).isEqualTo(2);
        assertThat(administrators.get(0).longValue()).isEqualTo(-1032L);
        assertThat(administrators.get(1).longValue()).isEqualTo(-1031L);
    }
    
//    @Test
//    public void shouldCreateAndDuplicateACL() {
//        // given
//        long sourceAclId = -1001L;
//        
//        List<Object []> aclMembers = orionAclRepository.findAclMembersByAclId(sourceAclId);
//        assertThat(aclMembers).isNotNull();
//        assertThat(aclMembers.size()).isEqualTo(2);
//       
//        // when
//        OrionAclDTO targetAcl = orionSecurityService.createAcl("TestACL_");
//        assertThat(targetAcl).isNotNull();
//        Long targetAclId = targetAcl.getId();
//        assertThat(targetAclId).isNotNull();
//        
//        orionSecurityService.duplicateAcl(sourceAclId, targetAclId);
//        
//        //then all records in DM_ACL belonging to source ACL should be copied
//        List<Object []> targetAclMembers = orionAclRepository.findAclMembersByAclId(targetAclId);
//        assertThat(targetAclMembers).isNotNull();
//        assertThat(targetAclMembers.size()).isEqualTo(2);
//        
//        // and group PNX001ADM should be copied to new group <acl_name> + "ADM"
//        List<OrionGroup> groups = orionGroupRepository.findByGroupName("TestACL_ADM");
//        assertThat(groups).isNotNull();
//        assertThat(groups.size()).isEqualTo(1);
//        assertThat(groups.get(0).getMembers().size()).isEqualTo(2);
//    }
    
}

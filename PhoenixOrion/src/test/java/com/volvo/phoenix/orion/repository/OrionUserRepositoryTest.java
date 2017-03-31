package com.volvo.phoenix.orion.repository;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.orion.entity.OrionUser;

public class OrionUserRepositoryTest extends AbstractTransactionalTestCase { //PhoenixDbUnitTestCase {

    @Autowired
    private OrionUserRepository userRepo;
    
    @Test
    public void testFindByUsernameIgnoreCase() {
        OrionUser user = userRepo.findByUsernameIgnoreCase("v0cn181");
        assertNotNull(user);
    }

//    @Override
//    protected IDataSet getDataSet() throws Exception {
//        final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
//        builder.setColumnSensing(true);
//        return builder.build(this.getClass().getClassLoader().getResourceAsStream("dataset/users.xml"));
//    }

}

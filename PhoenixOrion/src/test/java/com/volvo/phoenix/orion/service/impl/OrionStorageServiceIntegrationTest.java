package com.volvo.phoenix.orion.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.orion.dto.OrionFileDTO;
import com.volvo.phoenix.orion.service.OrionStorageService;
import com.volvo.phoenix.orion.util.OrionCMContext;

@Ignore
public class OrionStorageServiceIntegrationTest extends AbstractTransactionalTestCase { 
    
    
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;
    
    @Autowired
    private OrionStorageService storageService;
    
    @Before
    public void setUp() {
        System.setProperty("ORION_API_CONFIG_FILE", "src/test/resources/OrionAPIConfig.properties");
    }
    
    @Test
    public void shouldFindDocumentNameByDocumentId() throws Exception {
        //given
        String documentName = "50239046";
        String revision = "2";
        String dirName = "export";
//        File tmpDirectory = new File(dirName);
//        tmpDirectory.mkdir();
        OrionCMContext octxt = new OrionCMContext("v0ipcp", "v0ipcp99", "app_phoenix");
        List<OrionFileDTO> files = new ArrayList<OrionFileDTO>();
        OrionFileDTO f = new OrionFileDTO();
        f.setAliasType("SVGZ");
        f.setInputName("ra_activemq.xml");
        f.setSheetName("page1");
        files.add(f);
                
        // when
        storageService.exportFiles(documentName, revision, files, octxt, dirName);
        
        //then 

    }
    

}

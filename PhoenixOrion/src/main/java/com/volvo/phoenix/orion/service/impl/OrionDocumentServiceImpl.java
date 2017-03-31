package com.volvo.phoenix.orion.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.dto.OrionFileDTO;
import com.volvo.phoenix.orion.entity.OrionAcl;
import com.volvo.phoenix.orion.entity.OrionDocument;
import com.volvo.phoenix.orion.repository.OrionAclRepository;
import com.volvo.phoenix.orion.repository.OrionDocumentRepository;
import com.volvo.phoenix.orion.service.OrionDocumentService;
import com.volvo.phoenix.orion.service.OrionStorageService;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;
import com.volvo.phoenix.orion.translator.OrionDocumentDTOEntityTranslator;
import com.volvo.phoenix.orion.util.OrionCMContext;

/**
 * Implementation of {@link OrionDocumentService}
 * 
 * @see OrionDocumentService
 * 
 */
@Service
@Transactional(propagation=Propagation.SUPPORTS)
public class OrionDocumentServiceImpl implements OrionDocumentService {
    
    private static final String NEW_REVISION = "1";
    private static final String EXPORT_FOLDER_PREFIX = "/copymgr-";
    private static final String COMPONENT_NAME_PREFIX = "page";
    private static final long PHOENIX_REG_NUMBER_BASE = 50000000;

    @Autowired
    private OrionStorageService storageService;
    
    @Autowired
    private OrionDocumentRepository orionDocumentRepository;
    
    @Autowired
    private OrionAclRepository orionAclRepository;
    
    @Autowired
    private OrionDocumentDTOEntityTranslator translator;

    @Override
    public OrionDocumentDTO findDocument(Long id) {
        OrionDocument orionDocument = orionDocumentRepository.findOne(id);
        return new OrionDocumentDTOEntityTranslator().toDTO(orionDocument);
    }
    
    @Override
    public String getDocumentName(long documentId) {
        return orionDocumentRepository.getDocumentName(documentId);
    }
    
    @Override
    public List<Object[]> getEachFolderSizeByNodeId(List<Long> nodeIds) {
        return orionDocumentRepository.getEachFolderSizeByNodeId(nodeIds);
    }
    
    @Override
    public List<Object[]> getEachDocumentSizeByObjId(List<Long> ObjIds) {
        return orionDocumentRepository.getEachDocumentSizeByObjId(ObjIds);
    }
    
    @Override
    public Long getFoldersSizeByNodeId(List<Long> nodeIds) {
        return orionDocumentRepository.getFoldersSizeByNodeId(nodeIds);
    }
    
    @Override
    public Long getDocumentsSizeByObjId(List<Long> ObjIds) {
        return orionDocumentRepository.getDocumentsSizeByObjId(ObjIds);
    }
    
    @Override
    public List<OrionDocumentDTO> findAllDocumentRevisions(String name) {
        List<OrionDocument> documents = orionDocumentRepository.findByName(name);
        List<OrionDocumentDTO> documentDTOs = new ArrayList<OrionDocumentDTO>();
        for (OrionDocument document : documents) {
            documentDTOs.add(new OrionDocumentDTOEntityTranslator().toDTO(document));
        }
        return documentDTOs;
    }
    
    @Override
    public OrionDocumentDTO findLatestDocumentRevision(String name) {
        OrionDocument doc = orionDocumentRepository.findFirstByNameOrderByRevisionDesc(name);
        if (doc != null) {
            return new OrionDocumentDTOEntityTranslator().toDTO(doc);
        }
        return null;
    }

    @Override
    public void updateDocumentAcl(Long docId, Long aclId) {
        OrionDocument doc = orionDocumentRepository.findOne(docId);
        OrionAcl acl = orionAclRepository.findOne(aclId);
        doc.setAcl(acl);
        orionDocumentRepository.save(doc);
    }
    
    @Override
    public OrionAclDTO createAcl(String aclName) {
        return storageService.createAcl(aclName);
    }

    
    @Override
    public Long copyDocument(OrionCMContext ctx, OrionDocumentDTO dto) throws PhoenixDataAccessException, IOException {
        String registrationNumber = createRegistrationNumber();

        copyDocument(dto, registrationNumber, ctx);
        
        List<OrionDocument> copies = orionDocumentRepository.findByName(registrationNumber);
        if (copies.size() != 1) {
            throw new PhoenixDataAccessException("Number of copied revisions: " + copies.size() + " is different than 1");
        }
        
        return copies.get(0).getId();
    }
    
    private void copyDocument(OrionDocumentDTO dto, String newName, OrionCMContext ctx) throws PhoenixDataAccessException, IOException {
        String tmpDirectoryName = System.getProperty("TEMP_DIR") + EXPORT_FOLDER_PREFIX + new Date().getTime();
        File tmpDirectory = new File(tmpDirectoryName);
        tmpDirectory.mkdir();

        try {
            List<File> files = storageService.exportFiles(dto.getName(), dto.getRevision(), dto.getFileList(), ctx, tmpDirectoryName);
            dto.setName(newName);
            dto.setRevision(NEW_REVISION);

            storageService.checkinNewDocument(dto, ctx);
 
            int i = 1;
            for (File file : files) {
                storageService.checkinNewFile(dto.getName(), dto.getRevision(), dto.getAclId(), dto.getStateId(), file, COMPONENT_NAME_PREFIX + i++, ctx);
                file.delete();
            }
        } catch (Exception e) {
            throw new PhoenixDataAccessException(e);
        }
        tmpDirectory.delete();
    }
    
    /**
     *  Create a registration number (name)
     */
    private String createRegistrationNumber() {
         return String.valueOf(PHOENIX_REG_NUMBER_BASE + orionDocumentRepository.getNextDocumentRegistrationNumber());
    }

    @Override
    public Long createDocument(OrionCMContext ctx, OrionDocumentDTO dto) throws PhoenixDataAccessException, IOException {
        String newRegistrationNumber = createRegistrationNumber();
        dto.setName(newRegistrationNumber);
        return createRevisionBase(ctx, dto, newRegistrationNumber);
    }
    
    @Override
    public Long createNewVersion(OrionCMContext ctx, OrionDocumentDTO dto) throws PhoenixDataAccessException, IOException {
        String docRegistrationNumber = dto.getName(); 
        OrionDocument doc = orionDocumentRepository.findFirstByNameOrderByRevisionDesc(docRegistrationNumber);
        if (doc == null) {
            throw new PhoenixDataAccessException("Document with registration number: " + docRegistrationNumber + " not found to create new version");
        }
        return createRevisionBase(ctx, dto, docRegistrationNumber);
    }

    
    private Long createRevisionBase(OrionCMContext ctx, OrionDocumentDTO dto, String registrationNumber) throws PhoenixDataAccessException {
        try {
            storageService.checkinNewDocument(dto, ctx);
            
            int i = 1;
            for (OrionFileDTO fileDTO : dto.getFileList()) {
                File file = new File(fileDTO.getInputName());
                storageService.checkinNewFile(dto.getName(), dto.getRevision(), dto.getAclId(), dto.getStateId(), file, COMPONENT_NAME_PREFIX + i++, ctx);
            }
        } catch (Exception e) {
            throw new PhoenixDataAccessException(e);
        }
        
        OrionDocument doc = orionDocumentRepository.findFirstByNameOrderByRevisionDesc(registrationNumber);
        if (doc != null) {
            return doc.getId();
        }
        return null;
    }
    
}

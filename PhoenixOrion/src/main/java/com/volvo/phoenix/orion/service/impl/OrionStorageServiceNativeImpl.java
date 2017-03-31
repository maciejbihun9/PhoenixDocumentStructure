package com.volvo.phoenix.orion.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.formtek.orion.api.common.ConfigManager;
import com.formtek.orion.api.exception.DBException;
import com.formtek.orion.api.exception.InvalidInputException;
import com.formtek.orion.api.item.DocAttributeType;
import com.formtek.orion.api.item.DocCheckoutMode;
import com.formtek.orion.api.item.DocComponent;
import com.formtek.orion.api.item.DocDocument;
import com.formtek.orion.api.item.DocNameComponent;
import com.formtek.orion.api.item.DocRevision;
import com.formtek.orion.api.utils.Define;
import com.formtek.orion.api.utils.FTKAttribute;
import com.formtek.orion.api.utils.FTKAttributeType;
import com.formtek.orion.api.utils.FTKValue;
import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;
import com.volvo.phoenix.orion.dto.OrionFileDTO;
import com.volvo.phoenix.orion.entity.OrionAcl;
import com.volvo.phoenix.orion.repository.OrionAclRepository;
import com.volvo.phoenix.orion.service.OrionStorageService;
import com.volvo.phoenix.orion.translator.OrionAclDTOEntityTranslator;
import com.volvo.phoenix.orion.util.FileConverter;
import com.volvo.phoenix.orion.util.OrionCMContext;
import com.volvo.phoenix.orion.util.StreamCachingFileCustomDataSource;

/**
 * Access Orion storage through native API
 */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED)
public class OrionStorageServiceNativeImpl implements OrionStorageService {

	private static final String PNX_ORION_CLASS_NAME = "Phoenix";
	private static final String ORION_DOC_CHECKIN_OPERATION = "R";
	private static final String NO_COLUMN_NAME = "";
	private static final String NO_COLUMN_VALUE = "";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private OrionAclRepository orionAclRepository;
    
    
    @Override
    public OrionAclDTO createAcl(String aclName) {
        OrionAcl acl = new OrionAcl();
        acl.setName(aclName);
        acl.setId(orionAclRepository.getNextAvailableACLId());
        return OrionAclDTOEntityTranslator.toDTO(orionAclRepository.save(acl));
    }
	
    @Override
    public void checkinNewDocument(OrionDocumentDTO doc, OrionCMContext ctx) throws DBException, InvalidInputException, Exception {
        initializeContext(ctx);

        DocDocument docDocument = createDocDocument(doc.getName(), doc.getRevision(), ctx);
        setDocumentFieldAttributes(docDocument, doc);

        docDocument.checkin(ORION_DOC_CHECKIN_OPERATION);

        commitAndCloseContext(ctx);
    }
    
    @Override
    public void checkinNewFile(String objectName, String objectRev, Long aclId, Long stateId, File file, String sheetName, OrionCMContext ctx) throws Exception {
        initializeContext(ctx);
        
        DocDocument docDocument = createDocDocument(objectName, objectRev, ctx);
        DocComponent docComponent = createDocComponent(sheetName, ctx, docDocument);
        
        StreamCachingFileCustomDataSource dataSource = new StreamCachingFileCustomDataSource(file);
        DataHandler datahandler = new DataHandler(dataSource);
        List<DataHandler> fileList = new ArrayList<DataHandler>();
        fileList.add(datahandler);

        docComponent.setFileList(fileList);

        setComponentFieldAttributes(docComponent, file, aclId, stateId);
        
        docComponent.checkin(Define.OP_CHECKIN_KW);
        
        dataSource.closeStreams();
        commitAndCloseContext(ctx);
    }
    
    @Override
    public List<File> exportFiles(String documentName, String revision, List<OrionFileDTO> files, OrionCMContext ctx, String dirName) throws Exception {
        initializeContext(ctx);
        
        DocDocument docDocument = createDocDocument(documentName, revision, ctx);
        
        for (OrionFileDTO fileBean : files) {
           FileOutputStream fos = new FileOutputStream(new File(dirName + "/" + fileBean.getInputName()));
           DocComponent docComponent = createDocComponent(fileBean.getSheetName(), ctx, docDocument);

           try {
               docComponent.streamCheckout(DocCheckoutMode.EXPT, fileBean.getAliasType(), fos);
           } catch (Exception e) {
               logger.error("Error exporting file {} of document {} , message:" + e.getMessage(), fileBean.getInputName(), documentName);
           }
        }
        commitAndCloseContext(ctx);
        return Arrays.asList(new File(dirName).listFiles());
    }
    
    private DocComponent createDocComponent(String sheetName, OrionCMContext ctx, DocDocument docDocument) throws Exception {
        DocNameComponent[] sheet= new DocNameComponent[1];
        sheet[0] = new DocNameComponent(1, NO_COLUMN_NAME, sheetName);
        
        DocRevision sheetDocRev = new DocRevision();
        DocNameComponent sheetRev = new DocNameComponent(1, NO_COLUMN_NAME, NO_COLUMN_VALUE);
        sheetDocRev.setNameComponent(sheetRev, DocAttributeType.REV);
        
        DocNameComponent sheetAuxRev = new DocNameComponent(1, NO_COLUMN_NAME, NO_COLUMN_VALUE);
        sheetDocRev.setNameComponent(sheetAuxRev, DocAttributeType.AUX_REV);
        
        DocComponent docComponent = new DocComponent(sheet, docDocument, PNX_ORION_CLASS_NAME, ctx.getDbManager(), sheetDocRev);
        docComponent.setEnvironment(ctx.getEnvironment());
        docComponent.setObjectId("");
        docComponent.setCurrent(true);
        docComponent.setObjectType(Define.COMPONENT);
        
        return docComponent;
    }

    private DocDocument createDocDocument(String name, String revision, OrionCMContext ctx) throws Exception {
        DocNameComponent[] docName = new DocNameComponent[1];
        docName[0] = new DocNameComponent(1, NO_COLUMN_NAME, name);

        DocRevision docRev = new DocRevision();
        DocNameComponent rev = new DocNameComponent(1, NO_COLUMN_NAME, revision);
        docRev.setNameComponent(rev, DocAttributeType.REV);

        DocNameComponent auxrev = new DocNameComponent(1, NO_COLUMN_NAME, NO_COLUMN_VALUE);
        docRev.setNameComponent(auxrev, DocAttributeType.AUX_REV);

        DocDocument docDocument = new DocDocument(docName, PNX_ORION_CLASS_NAME, ctx.getDbManager(), docRev);
        docDocument.setEnvironment(ctx.getEnvironment());
        docDocument.setCurrent(true);
        docDocument.setObjectType(Define.OBJECT);
        docDocument.setObjectId("");
        
        return docDocument;
    }
    
    private void setDocumentFieldAttributes(DocDocument docDocument, OrionDocumentDTO doc) {
        List<FTKAttribute> attr = new ArrayList<FTKAttribute>();

        attr.add(new FTKAttribute(Define.FTK_ATTRIBUTE_INDEX, createIndex("I", String.valueOf(doc.getAcl().getId()), "1", "I", "ACL_ID", "DM_OBJECT")));
        attr.add(new FTKAttribute(Define.FTK_ATTRIBUTE_INDEX, createIndex("I", checkValue(doc.getIndex1(), "%NVL"), "1", "C", "INDEX1", "DM_OBJECT")));
        attr.add(new FTKAttribute(Define.FTK_ATTRIBUTE_INDEX, createIndex("I", checkValue(doc.getIndex4(), "%NVL"), "1", "C", "INDEX4","DM_OBJECT")));
        attr.add(new FTKAttribute(Define.FTK_ATTRIBUTE_INDEX, createIndex("I", String.valueOf(doc.getAclState().getId()), "1","I", "STATE_ID",  "DM_OBJECT")));
        
        FTKAttribute[] allAttributes = attr.toArray(new FTKAttribute[0]);
        docDocument.setAttributes(allAttributes);
    }
    
    private void setComponentFieldAttributes(DocComponent comp, File file, Long aclId, Long stateId) {
        FTKAttribute indexes[] = new FTKAttribute[2];
        indexes[0] = new FTKAttribute(Define.FTK_ATTRIBUTE_INDEX, createIndex("I", String.valueOf(aclId),   "1", "I", "ACL_ID", "DM_COMPONENT"));
        indexes[1] = new FTKAttribute(Define.FTK_ATTRIBUTE_INDEX, createIndex("I", String.valueOf(stateId), "1", "I", "STATE_ID","DM_COMPONENT"));
        
        comp.setAttributes(indexes);
        
        FileConverter fileConverter = new FileConverter();
        String mediaTypeOrion = fileConverter.getTypeFromFileExt(FilenameUtils.getExtension(file.getName()));
        
        FTKAttribute[][] fileIndexes = new FTKAttribute[1][0];
        FTKAttribute[] fi = {
                new FTKAttribute(Define.FILE_INFO_FILE_NAME,        new FTKValue(FTKAttributeType.STRING_TYPE, file.getName())),
                new FTKAttribute(Define.FILE_INFO_FILE_PATH,        new FTKValue(FTKAttributeType.STRING_TYPE, file.getParent())),
                new FTKAttribute(Define.FILE_INFO_FILE_EXTENSION,   new FTKValue(FTKAttributeType.STRING_TYPE, "%NVL")),
                new FTKAttribute(Define.FILE_INFO_FILE_REP_TYPE,    new FTKValue(FTKAttributeType.STRING_TYPE, mediaTypeOrion)),
                new FTKAttribute(Define.FILE_INFO_FILE_VOLUME,      new FTKValue(FTKAttributeType.STRING_TYPE, "%NVL")),
                new FTKAttribute(Define.FILE_INFO_FILE_LAUNCH_TYPE, new FTKValue(FTKAttributeType.STRING_TYPE, "Y")),
                new FTKAttribute(Define.FILE_INFO_FILE_APPLICATION, new FTKValue(FTKAttributeType.STRING_TYPE, "%NVL")) };

        fileIndexes[0] = fi;
        comp.setFileAttribute(fileIndexes);
    }

    private FTKValue createIndex(String indexOps, String indexValue, String indexId, String indexType, String columnName, String tableName) {
        Hashtable<String, String> indexValues = new Hashtable<String, String>();
        indexValues.put("INDEX_OPS", indexOps);
        indexValues.put("INDEX_VALUE", indexValue);
        indexValues.put("INDEX_ID", indexId);
        indexValues.put("INDEX_TYPE", indexType);
        indexValues.put("COLUMN_NAME", columnName);
        indexValues.put("TABLE_NAME", tableName);
        return new FTKValue(FTKAttributeType.HASHTABLE_TYPE, indexValues);
    }
    
    private void initializeContext(OrionCMContext ctx) throws Exception {
        ctx.getDbManager().getConnection();
    }
    
    private void commitAndCloseContext(OrionCMContext ctx) throws Exception {
        if (Boolean.parseBoolean(ConfigManager.getConnectionParameter("DISABLE_TRANSACTIONS"))) {
            ctx.getDbManager().executeCommit();
        }
        ctx.getDbManager().releaseConnection();
    }

    private String checkValue(String value, String def) {
        return !StringUtils.isEmpty(value) ? value : def;
    }

}

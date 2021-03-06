/* Repository data */

INSERT INTO DM_ACL_NAMES (ACL_ID, ACL_NAME) VALUES (-65, 'PNX001');

INSERT INTO VT_PHOENIX_ATTRIBUTE_LOV (LOV_ID) VALUES (-1);

INSERT INTO VT_PHOENIX_ATTRIBUTE_LOV_VALUE (LOV_ID, LOV_VALUE_ID, LOV_VALUE, LOV_ORDER) VALUES (-1, -1, 'Selection 1', 1);
INSERT INTO VT_PHOENIX_ATTRIBUTE_LOV_VALUE (LOV_ID, LOV_VALUE_ID, LOV_VALUE, LOV_ORDER) VALUES (-1, -2, 'Selection 2', 2);

INSERT INTO VT_PHOENIX_ATTRIBUTE (ATTRIBUTE_ID, ATTRIBUTE_LABEL, ATTRIBUTE_NAME, ATTRIBUTE_TYPE) VALUES (-51, 'Test text attribute','Test text attribute1', 'TEXT');
INSERT INTO VT_PHOENIX_ATTRIBUTE (ATTRIBUTE_ID, ATTRIBUTE_LABEL, ATTRIBUTE_NAME, ATTRIBUTE_TYPE) VALUES (-52, 'Test checkbox attribute','Test text attribute2', 'CHECKBOX');
INSERT INTO VT_PHOENIX_ATTRIBUTE (ATTRIBUTE_ID, ATTRIBUTE_LABEL, ATTRIBUTE_NAME, ATTRIBUTE_TYPE, LOV_ID) VALUES (-53, 'Test select attribute','Test text attribute3', 'SELECT', -1);
INSERT INTO VT_PHOENIX_ATTRIBUTE (ATTRIBUTE_ID, ATTRIBUTE_LABEL, ATTRIBUTE_NAME, ATTRIBUTE_TYPE) VALUES (-54, 'Test numeric attribute','Test text attribute4', 'NUMERIC');

INSERT INTO VT_PHOENIX_DOCTYPE (DOCTYPE_ID, DOCTYPE_DESCRIPTION ) VALUES (-31, 'Test document type');
INSERT INTO VT_PHOENIX_DOCTYPE (DOCTYPE_ID) VALUES (-32);

INSERT INTO VT_PHOENIX_DOCTYPE_ATTRIBUTE (ATTRIBUTE_ID, DOCTYPE_ID) VALUES (-51, -31);
INSERT INTO VT_PHOENIX_DOCTYPE_ATTRIBUTE (ATTRIBUTE_ID, DOCTYPE_ID) VALUES (-52, -31);


INSERT INTO VT_PHOENIX_DOMAIN (DOMAIN_ID, DOMAIN_NAME) VALUES (-11, 'Test domain');
INSERT INTO VT_PHOENIX_DOMAIN_ATTRIBUTE( ATTRIBUTE_ID, DOMAIN_ID, MANDATORY, SORT_ORDER)  VALUES (-51, -11,'N', 1);
INSERT INTO VT_PHOENIX_DOMAIN_ATTRIBUTE( ATTRIBUTE_ID, DOMAIN_ID, MANDATORY, SORT_ORDER)  VALUES (-52, -11,'N', 1);

INSERT INTO VT_PHOENIX_ACL (ACL_ID, DOMAIN_ID) VALUES (-61, -11);
INSERT INTO VT_PHOENIX_ACL_DOCTYPE (ACL_ID, DOCTYPE_ID) VALUES (-61, -31);

INSERT INTO VT_PHOENIX_DOMAIN_DOCTYPE (DOMAIN_ID, DOCTYPE_ID) VALUES (-11, -31);

INSERT INTO VT_PHOENIX_FAMILY (FAMILY_ID, FAMILY_NAME) VALUES (-61, 'Test family');

INSERT INTO VT_PHOENIX_FAMILY_DOCTYPE (FAMILY_ID, DOCTYPE_ID) VALUES (-61, -31);

INSERT INTO VT_PHOENIX_DOMAIN_ATTRIBUTE (ATTRIBUTE_ID, DOMAIN_ID , SORT_ORDER , MANDATORY ) VALUES (-53, -11 , 1, 'Y');
INSERT INTO VT_PHOENIX_DOMAIN_ATTRIBUTE (ATTRIBUTE_ID, DOMAIN_ID , SORT_ORDER , MANDATORY ) VALUES (-54, -11 , 2, 'Y');

INSERT INTO VT_PHOENIX_TREE (NODE_ID, NODE_PARENT_ID, NODE_TEXT, NODE_TYPE, ACL_ID) VALUES (-501, -501 , 'Master folder', 'M', -61);

INSERT INTO VT_PHOENIX_DOC_DEF_ATTRIBUTE (FOLDER_ID, DOCUMENT_FAMILY_ID, DOCUMENT_TYPE_ID, INFO_CLASS_ID, WORK_STATUS)
	VALUES (-501, -61, -31, 42, 'VALID');

/* Existing documents in Phoenix test data */

INSERT INTO DM_OBJECT (CTX_ID, OBJ_ID, OBJ_TYPE, NAME, REV, CUR,  STATUS, ACL_ID, STATE_ID, CLASS_NAME)
	VALUES (-1, -1002, 'O', '-502000' , 1, 'N', 'S', -65, 42, 'Phoenix');
INSERT INTO DM_OBJECT (CTX_ID, OBJ_ID, OBJ_TYPE, NAME, REV, CUR,  STATUS, ACL_ID, STATE_ID, CLASS_NAME)
	VALUES (-1, -1003, 'O', '-502000' , 2, 'Y', 'S', -65, 42, 'Phoenix');

INSERT INTO VT_PHOENIX_DOCUMENT (OBJ_ID, OBJ_TYPE, INDEX_ID, NODE_ID, TITLE, DOCUMENT_STATUS, FAMILY_ID, DOCTYPE_ID, DOMAIN_ID, PROTECT_IN_WORK) 
	VALUES (-1003, 'O', '1', -501, 'Existing doc in Phoenix', 'VALID',-61, -31, -11, 'N');	
	
INSERT INTO VT_PHOENIX_ATTRIBUTE_VALUE (OBJ_ID, OBJ_TYPE, INDEX_ID, ATTRIBUTE_ID, VALUE) VALUES (-1003, 'O', 1, -51, 'Existing atr value');
INSERT INTO VT_PHOENIX_ATTRIBUTE_VALUE (OBJ_ID, OBJ_TYPE, INDEX_ID, ATTRIBUTE_ID, VALUE) VALUES (-1003, 'O', 2, -52, 'Y');
	
/* Upload Tool transaction data */

INSERT INTO UT_OPERATION (ID, VERSION, USERNAME, FOLDER_ID, CREATE_DATE, STATUS) 
	VALUES (-1, 1, 'bpl3195', -501, TO_DATE('2016-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'CREATED');

INSERT INTO UT_OPERATION_FILE (ID, OPERATION_ID, VERSION, FILE_NAME, TYPE, METADATA_STATUS) 
	VALUES (-11, -1, 1, 'Test1.zip', 'Z', 'NO_METADATA');
	
INSERT INTO UT_OPERATION_FILE (ID, OPERATION_ID, VERSION, FILE_NAME, TYPE, METADATA_STATUS) 
	VALUES (-12, -1, 1, 'Test.docx', 'D', 'NO_METADATA');	
	
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) 
	VALUES (-1, -101, null, 1, 'Marine', 'S', null);
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) 
	VALUES (-1, -1001, -101, 1, 'Migrated Park Document', 'D', 1);	
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) 
	VALUES (-1, -10001, -1001, 1, 'parkDocument.docx', 'F', null);
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) 
	VALUES (-1, -102, null, 1, 'Document already existing in system', 'D', null);
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) 
	VALUES (-1, -103, null, 1, 'Document with no metadata', 'D', null);
	
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-102, 'Registration number', '-502000');	
	

INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Registration number', '-50239949');	
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Alternative number', '50239949');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Version date', '2015-03-17');

INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Document status', 'Valid'); 

INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Protect In-Work', 'No');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Title', 'Approve testing 3');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Author name', 'Krzysztof Kocik, bpl3195');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Issuer name', 'Jan Nowak');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Description', 'Test description');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Notes', 'Test notes');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Version', '1');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Info class', 'Strictly Confidential');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Document family', 'Test family');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Document type', 'Test document type');
        
/* mandatory document type attributes for type = Test document type */
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Test text attribute', 'Feature');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Test checkbox attribute', 'Yes');
        
/* optional domain attributes for domain = Test domain */
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Test numeric attribute', '1500');
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Test select attribute', 'Selection 2');


/* Apply Upload test data */

INSERT INTO UT_OPERATION (ID, VERSION, USERNAME, FOLDER_ID, CREATE_DATE, STATUS) 
	VALUES (-2, 1, 'bpl3195', -501, TO_DATE('2016-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'CREATED');

	
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) VALUES (-2, -201, null, 1, 'Marine', 'S', null);
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) VALUES (-2, -2001, -201, 1, 'Document under Marine', 'D', 1);	
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) VALUES (-2, -20001, -2001, 1, 'parkDocument.docx', 'F', null);
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) VALUES (-2, -2002, -201, 1, 'Folder under Marine', 'S', null);
INSERT INTO UT_OPERATION_TREE (OPERATION_ID, NODE_ID, NODE_PARENT_ID, VERSION, NODE_TEXT, NODE_TYPE, REV) VALUES (-2, -2012, -2002, 1, 'Document under Folder under Marine', 'D', 1);
	

INSERT INTO UT_DOCUMENT (ID, TREE_NODE_ID, TITLE, NAME, REVISION, STATE_ID, FAMILY_ID, DOCTYPE_ID, DOCUMENT_STATUS,
      DESCRIPTION, AUTHOR, AUTHOR_ID, ISSUER, ISSUER_ID, ISSUE_DATE, NOTES, ALT_DOC_ID, PROTECT_IN_WORK) VALUES
						(-21, -2001, 'Document under Marine', null, 1, 40,  -61, -31, 'VALID', 
	'Desc', 'Krzysztof Kocik', 'bpl3195', 'Krzysztof Kocik', 'bpl3195', TO_DATE('2016-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'notes', null, null);

INSERT INTO UT_DOCUMENT (ID, TREE_NODE_ID, TITLE, NAME, REVISION, STATE_ID, FAMILY_ID, DOCTYPE_ID, DOCUMENT_STATUS,
      DESCRIPTION, AUTHOR, AUTHOR_ID, ISSUER, ISSUER_ID, ISSUE_DATE, NOTES, ALT_DOC_ID, PROTECT_IN_WORK) VALUES
						(-23, -2012, 'Document under Folder under Marine', null, 1, 40,  -61, -31, 'VALID', 
	'Desc', 'Krzysztof Kocik', 'bpl3195', 'Krzysztof Kocik', 'bpl3195', TO_DATE('2016-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'notes', null, null);

	

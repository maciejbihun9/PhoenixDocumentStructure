INSERT INTO UT_OPERATION (ID, VERSION, USERNAME, FOLDER_ID, CREATE_DATE, STATUS) 
	VALUES (-1, 1, 'bpl3195', 431, TO_DATE('2016-09-05 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'CREATED');

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
	
INSERT INTO UT_DOCUMENT_ATTRIBUTE (NODE_ID, NAME, VALUE) VALUES (-1001, 'Author', 'Krzysztof Kocik, bpl3195');

/* MACIEK DATA*/

INSERT INTO VT_PHOENIX_FAMILY (FAMILY_ID,FAMILY_NAME) 
	VALUES (-5, 'FAMILY_TEST_NAME');
	

INSERT INTO UT_DOCUMENT (ID, AUTHOR, DOCUMENT_STATUS, REVISION, STATE_ID, FAMILY_ID) 
	VALUES (-1, 'MACIEJ BIHUN', 'VALID', 12, 14, -5);
	
/*Add attributes values*/
	
INSERT INTO VT_PHOENIX_ATTRIBUTE (ATTRIBUTE_ID, ATTRIBUTE_NAME) 
	VALUES (-1, 'TEST_ATTRIBUT');
	
INSERT INTO VT_PHOENIX_ATTRIBUTE (ATTRIBUTE_ID, ATTRIBUTE_NAME) 
	VALUES (-2, 'TEST_ATTRIBUT2');
	
INSERT INTO VT_PHOENIX_ATTRIBUTE (ATTRIBUTE_ID, ATTRIBUTE_NAME) 
	VALUES (-3, 'TEST_ATTRIBUT3');
	
INSERT INTO UT_DOCUMENT_ATTRIBUTE_VALUE (ID, DOCUMENT_ID, ATTRIBUTE_ID, VALUE) 
	VALUES (-11, -1, -1, 'value_1');
	

INSERT INTO UT_DOCUMENT_ATTRIBUTE_VALUE (ID, DOCUMENT_ID, ATTRIBUTE_ID, VALUE) 
	VALUES (-12, -1, -2, 'value_2');

	
INSERT INTO UT_DOCUMENT_ATTRIBUTE_VALUE (ID, DOCUMENT_ID, ATTRIBUTE_ID, VALUE) 
	VALUES (-13, -1, -3, 'value_3');
	
	
	
	

	

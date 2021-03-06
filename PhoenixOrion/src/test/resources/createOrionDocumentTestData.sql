INSERT INTO DM_USER (USERNAME, USERID, ROLE, ROLEID, DIRECTORY, REALNAME) VALUES ('Test user 1', -1031, 'APPROVE', 2, '/tmp', 'test user1');
INSERT INTO DM_USER (USERNAME, USERID, ROLE, ROLEID, DIRECTORY, REALNAME) VALUES ('Test user 2', -1032, 'APPROVE', 2, '/tmp', 'test user2');

INSERT INTO DM_GROUP (GROUP_ID, GROUP_NAME, OWNER_ID) VALUES (-1011, 'PNX001ADM', -1);
INSERT INTO DM_GROUP (GROUP_ID, GROUP_NAME, OWNER_ID) VALUES (-1012, 'PNX001SCW', -1);

INSERT INTO DM_GROUP_MEMBERS (GROUP_ID, USER_ID, ROLE) VALUES (-1011, -1031, 'Mem');
INSERT INTO DM_GROUP_MEMBERS (GROUP_ID, USER_ID, ROLE) VALUES (-1011, -1032, 'Mem');
INSERT INTO DM_GROUP_MEMBERS (GROUP_ID, USER_ID, ROLE) VALUES (-1012, -1031, 'Mem');
INSERT INTO DM_GROUP_MEMBERS (GROUP_ID, USER_ID, ROLE) VALUES (-1012, -1032, 'Mem');

INSERT INTO DM_ACL (ACL_ID, GROUP_ID, PRIV_ID, STATE_ID) VALUES (-1001, -1011, 15, 42);
INSERT INTO DM_ACL (ACL_ID, GROUP_ID, PRIV_ID, STATE_ID) VALUES (-1001, -1011, 15, 41);

INSERT INTO DM_ACL_NAMES (ACL_ID, ACL_NAME) VALUES (-1001, 'PNX001');

INSERT INTO DM_OBJECT (CTX_ID, OBJ_ID, OBJ_TYPE, NAME, REV, CUR,  STATUS, ACL_ID, STATE_ID, CLASS_NAME)
	VALUES (-1, -1001, 'O', 'Copy manager test document' , 1, 'Y', 'S', -1001, 42, 'Phoenix');
	
INSERT INTO DM_OBJECT (CTX_ID, OBJ_ID, OBJ_TYPE, NAME, REV, CUR,  STATUS, ACL_ID, STATE_ID, CLASS_NAME)
	VALUES (-1, -1002, 'O', 'Test document 2 revs' , 1, 'N', 'S', -1001, 42, 'Phoenix');
INSERT INTO DM_OBJECT (CTX_ID, OBJ_ID, OBJ_TYPE, NAME, REV, CUR,  STATUS, ACL_ID, STATE_ID, CLASS_NAME)
	VALUES (-1, -1003, 'O', 'Test document 2 revs' , 2, 'Y', 'S', -1001, 42, 'Phoenix');

INSERT INTO DM_COMPONENT (COMP_ID, OBJ_ID, NAME, STATUS) VALUES (-1011, -1001, 'page1', 'S');

INSERT INTO DM_REPRESENTATION (ID, REP_ID, TYPE) VALUES (-1011, -1021, 'C');
  
INSERT INTO DM_REP_INFO (REP_ID, ALIAS_TYPE) VALUES (-1021, 'PDF');
	
INSERT INTO DM_FILE (FILE_ID, INPUT_NAME, RETRIEVAL_ID, VOL_GRP_ID) VALUES (-1031, 'testFile.pdf', '1/testFile.pdf', 6);
	
INSERT INTO DM_REPRESENTATION_FILE (FILE_ID, REP_ID) VALUES (-1031, -1021);

	



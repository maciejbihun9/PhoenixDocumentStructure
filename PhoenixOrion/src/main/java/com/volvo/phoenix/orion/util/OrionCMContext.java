package com.volvo.phoenix.orion.util;

import com.formtek.orion.api.common.AuthenticationManager;
import com.formtek.orion.api.common.DBManager;
import com.formtek.orion.api.common.LoginManager;
import com.formtek.orion.api.commondatatype.ConnectionDetails;
import com.formtek.orion.api.exception.DBException;
import com.formtek.orion.api.exception.InvalidInputException;
import com.formtek.orion.api.exception.OrionServiceException;
import com.formtek.orion.api.utils.Environment;
import com.volvo.phoenix.orion.service.PhoenixDataAccessException;

public class OrionCMContext {

    private String userName;
    private String password = "not set";
    private String dbName;
    private String locale = "US_ENGLISH";
    private DBManager dbManager;
    private LoginManager loginManager;

    public OrionCMContext(String username, String password, String dbName) {
        this.userName = username;
        this.password = password;
        this.dbName = dbName;
    }
    
    public OrionCMContext(String username, String dbName) {
        this.userName = username;
        this.dbName = dbName;
    }

    public void initDBManager() throws PhoenixDataAccessException {
        ConnectionDetails cd = new ConnectionDetails();
        cd.setLocale(locale);
        cd.setUserName(userName);
        cd.setPassword(password);
        cd.setVaultName(dbName);

        cd.setAuthenticationManager(new CMSSOAuthenticationManager());

        loginManager = new LoginManager();
        try {
            dbManager = loginManager.login(cd);
        } catch (Exception e) {
            throw new PhoenixDataAccessException(e);
        }
    }

    public Environment getEnvironment() throws PhoenixDataAccessException {
        if (loginManager == null) {
            initDBManager();
        }
        return loginManager.getEnvironment();
    }

    public DBManager getDbManager() throws Exception {
        if (dbManager == null) {
            initDBManager();
        }
        return dbManager;
    }

    class CMSSOAuthenticationManager extends AuthenticationManager {
        @Override
        public boolean validateUserForLogin(ConnectionDetails paramConnectionDetails, DBManager paramDBManager) throws InvalidInputException, DBException,
                OrionServiceException {
            return true;
        }
    }

}

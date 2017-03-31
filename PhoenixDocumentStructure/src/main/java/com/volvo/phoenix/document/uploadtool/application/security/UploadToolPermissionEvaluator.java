package com.volvo.phoenix.document.uploadtool.application.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.volvo.phoenix.document.security.DomainPermissionEvaluator;
import com.volvo.phoenix.document.uploadtool.infrastructure.UploadToolOperationRepository;
import com.volvo.phoenix.document.uploadtool.model.UploadToolOperation;

@Service
public class UploadToolPermissionEvaluator implements DomainPermissionEvaluator {

    private final Logger LOGGER = LoggerFactory.getLogger(UploadToolPermissionEvaluator.class);
    
    @Autowired
    private UploadToolOperationRepository uploadToolOperationRepository;


    /**
     *  Example expression for securying a method.
     *  @PreAuthorize("hasPermission(#operation, 'canModify')")
     */    
    @Override
    public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
        if (domainObject == null) {
            return false;
        }
        return checkPermission(authentication, (UploadToolOperation) domainObject, permission);
    }

    /**
     *  Example expression for securying a method.
     *  @PreAuthorize("hasPermission(#operationId, 'com.volvo.phoenix.document.uploadtool.model.UploadToolOperation', 'canModify')")
     */     
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        UploadToolOperation domainObject = uploadToolOperationRepository.findOne(Long.parseLong(targetId.toString()));
        return checkPermission(authentication, domainObject, permission); 
    }
    
    
    private boolean checkPermission(Authentication authentication, UploadToolOperation operation, Object permission) {
        boolean hasAccess = false;
        
        if (authentication != null || operation != null || !StringUtils.isEmpty(permission)) {
            String loggedUser = authentication.getName();
            
            if (loggedUser != null) {
                if ("canModify".equals(permission)) {
                    hasAccess = isOperationCreatedForUser(operation, loggedUser);
                } else {
                    // more authorization logic here...
                    hasAccess = true;
                }
            }
        }

        LOGGER.debug((hasAccess ? "Accepting" : "Denying") + " authentication:" + authentication.getName() + " permission '" + permission
                + "' on object " + operation);
        return hasAccess;
    }

    private boolean isOperationCreatedForUser(UploadToolOperation operation, String loggedUser) {
        return operation.getUser().equals(loggedUser);
    }

    @Override
    public boolean supports(Class<?> targetDomainClass) {
        return UploadToolOperation.class.isAssignableFrom(targetDomainClass);
    }
}

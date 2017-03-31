package com.volvo.phoenix.document.security;

import org.springframework.security.access.PermissionEvaluator;

/**
 * Strategy used in expression evaluation to determine whether a user has a permission or permissions
 * for a given domain object.
 *
 */
public interface DomainPermissionEvaluator extends PermissionEvaluator {
    
    boolean supports(Class<?> targetDomainClass);

}

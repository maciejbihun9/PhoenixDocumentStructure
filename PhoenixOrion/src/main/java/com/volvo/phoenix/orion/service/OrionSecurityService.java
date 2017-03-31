package com.volvo.phoenix.orion.service;

import java.util.List;

import com.volvo.phoenix.orion.dto.OrionUserDTO;

/**
 * This is designed to handle Orion security related to function.
 * 
 * @author v0cn181
 */
public interface OrionSecurityService {


    void duplicateAcl(Long id, Long id2);

    /**
     * Obtain list of Phoenix administrator ids for an ACL
     * @param aclId
     * @return
     */
    List<Long> getPhoenixAdministratorsForACL(long aclId);
    
    /**
     * Obtain list of ACL for Phoenix business administrator
     * @param username
     * @return
     */
    List<Long> getPhoenixAclIdsForBusinessAdministrator(String username);
    
    /**
     * Provide OrionUser details
     * @param userName
     * @return
     */
    OrionUserDTO findUserByUserName(String userName);
}

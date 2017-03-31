package com.volvo.phoenix.orion.repository;

import org.springframework.data.repository.CrudRepository;

import com.volvo.phoenix.orion.entity.OrionUser;

/**
 * standard spring data repository for Orion user to provide basic CRUD actions
 * 
 * @author v0cn181
 */
public interface OrionUserRepository extends CrudRepository<OrionUser, Long>{
    /**
     * to load user information and connected access groups by username case insensitive 
     * 
     * @param username username
     * @return user information with access groups
     */
    public OrionUser findByUsernameIgnoreCase(String username);
}


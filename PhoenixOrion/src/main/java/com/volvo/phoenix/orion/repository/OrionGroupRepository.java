package com.volvo.phoenix.orion.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.volvo.phoenix.orion.entity.OrionGroup;

/**
 * standard spring data repository for Orion group to provide basic CRUD actions
 * 
 * @author v0cn181
 */
public interface OrionGroupRepository extends CrudRepository<OrionGroup, Long> {

    List<OrionGroup> findByGroupName(String groupName);

}

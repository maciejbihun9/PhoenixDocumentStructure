package com.volvo.phoenix.document.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volvo.phoenix.document.entity.PhoenixAcl;

/**
 * 
 */
public interface PhoenixAclRepository
extends CrudRepository<PhoenixAcl, Long>  {

    @Query(value="SELECT 'PNX' || VT_PHOENIX_ACL_NAME_SEQ.nextval  FROM dual",nativeQuery=true)
    String getNextAclName();
}

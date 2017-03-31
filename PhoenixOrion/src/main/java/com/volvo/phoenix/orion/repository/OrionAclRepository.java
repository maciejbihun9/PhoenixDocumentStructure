package com.volvo.phoenix.orion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volvo.phoenix.orion.entity.OrionAcl;

/**
 * Designed to fetch or persist acl info, so far it's only used to fetch acl information.
 * 
 * @author v0cn181
 */
public interface OrionAclRepository extends CrudRepository<OrionAcl, Long> {
    
    @Query(value="select DM_ACL_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Long getNextAvailableACLId();

    @Query(value="SELECT GROUP_ID, PRIV_ID, STATE_ID FROM DM_ACL where ACL_ID = :srcId", nativeQuery = true)
    List<Object []> findAclMembersByAclId(@Param("srcId") Long srcId);

    @Modifying
    @Query(value="insert into DM_ACL (ACL_ID, GROUP_ID, PRIV_ID, STATE_ID) VALUES (:acl_id, :group_id, :priv_id, :state_id)", nativeQuery = true)
    void saveAclMember(@Param("acl_id") Long acl_id, @Param("group_id") Long group_id, @Param("priv_id") Long priv_id, @Param("state_id") Object state_id);
    
    @Modifying
    @Query(value="insert into DM_ACL (ACL_ID, GROUP_ID, PRIV_ID, STATE_ID) VALUES (:acl_id, :group_id, :priv_id, null)", nativeQuery = true)
    void saveAclMember(@Param("acl_id") Long acl_id, @Param("group_id") Long group_id, @Param("priv_id") Long priv_id);
    
    @Query(value="SELECT distinct acl.ACL_ID FROM DM_ACL acl ,DM_GROUP g, DM_GROUP_MEMBERS gm, DM_USER u WHERE acl.group_id = g.group_id AND gm.group_id = g.group_id AND gm.user_id = u.userid AND u.username = :username AND g.group_name like '%BADM'", nativeQuery = true)
    List<Object []> findAclIdByUserName(@Param("username") String username);

}

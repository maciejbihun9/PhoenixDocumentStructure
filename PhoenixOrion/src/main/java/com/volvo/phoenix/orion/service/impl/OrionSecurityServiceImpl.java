package com.volvo.phoenix.orion.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volvo.phoenix.orion.dto.OrionAclMember;
import com.volvo.phoenix.orion.dto.OrionUserDTO;
import com.volvo.phoenix.orion.entity.OrionAcl;
import com.volvo.phoenix.orion.entity.OrionGroup;
import com.volvo.phoenix.orion.entity.OrionGroupMember;
import com.volvo.phoenix.orion.entity.OrionGroupMemberPK;
import com.volvo.phoenix.orion.entity.OrionUser;
import com.volvo.phoenix.orion.repository.OrionAclRepository;
import com.volvo.phoenix.orion.repository.OrionGroupRepository;
import com.volvo.phoenix.orion.repository.OrionUserRepository;
import com.volvo.phoenix.orion.service.OrionSecurityService;

/**
 * Implementation of {@link OrionSecurityService}
 * 
 * @author v0cn181
 */
@Service
public class OrionSecurityServiceImpl implements OrionSecurityService {

    private static final String GROUP_ADMIN_SUFFIX = "ADM";

    private static final int GROUP_ID_IDX = 0;
    private static final int PRIV_ID_IDX = 1;
    private static final int STATE_ID_IDX = 2;

    @PersistenceContext(unitName = "PhoenixOrionPU")
    protected EntityManager entityManager;

    @Autowired
    private OrionAclRepository orionAclRepository;

    @Autowired
    private OrionGroupRepository orionGroupRepository;

    @Autowired
    private OrionUserRepository orionUserRepository;

    @Override
    public void duplicateAcl(Long srcId, Long targetId) {
        String sourceAclName = orionAclRepository.findOne(srcId).getName();
        String targetAclName = orionAclRepository.findOne(targetId).getName();

        List<OrionAclMember> aclMembers = translateToAclMembers(orionAclRepository.findAclMembersByAclId(srcId));
        for (OrionAclMember member : aclMembers) {
            OrionAclMember newMember = new OrionAclMember();
            OrionGroup sourceGroup = orionGroupRepository.findOne(member.getGroup_id());

            if (sourceGroup.getGroupName().startsWith(sourceAclName)) {
                String targetGroupName = sourceGroup.getGroupName().replace(sourceAclName, targetAclName);
                List<OrionGroup> targetGroups = orionGroupRepository.findByGroupName(targetGroupName);
                OrionGroup targetGroup;
                if (targetGroups.size() > 0) {
                    targetGroup = targetGroups.get(0);
                } else {
                    targetGroup = new OrionGroup();
                    targetGroup.setGroupName(targetGroupName);
                    targetGroup.setOwnerId(sourceGroup.getOwnerId());
                    cloneGroupMembers(sourceGroup, targetGroup);
                    targetGroup = orionGroupRepository.save(targetGroup);
                }

                newMember.setGroup_id(targetGroup.getGroupId());
            } else {
                newMember.setGroup_id(member.getGroup_id());
            }

            newMember.setPriv_id(member.getPriv_id());
            newMember.setState_id(member.getState_id());

            entityManager.flush();
            if (newMember.getState_id() == null) {
                orionAclRepository.saveAclMember(targetId, newMember.getGroup_id(), newMember.getPriv_id());
            } else {
                orionAclRepository.saveAclMember(targetId, newMember.getGroup_id(), newMember.getPriv_id(), newMember.getState_id());
            }
        }
    }

    private void cloneGroupMembers(OrionGroup sourceGroup, OrionGroup targetGroup) {
        for (OrionGroupMember member : sourceGroup.getMembers()) {
            OrionGroupMember newMember = new OrionGroupMember();
            OrionGroupMemberPK newMemberPK = new OrionGroupMemberPK();
            newMemberPK.setOrionGroup(targetGroup);
            newMemberPK.setUserId(member.getId().getUserId());
            newMemberPK.setRole(member.getId().getRole());
            newMember.setId(newMemberPK);
            targetGroup.getMembers().add(newMember);
        }
    }

    private List<OrionAclMember> translateToAclMembers(List<Object[]> aclMembersResultSet) {
        List<OrionAclMember> aclMembers = new ArrayList<OrionAclMember>();
        for (Object[] row : aclMembersResultSet) {
            OrionAclMember member = new OrionAclMember();
            member.setGroup_id(((BigDecimal) row[GROUP_ID_IDX]).longValueExact());
            member.setPriv_id(((BigDecimal) row[PRIV_ID_IDX]).longValueExact());
            if (row[STATE_ID_IDX] != null) {
                member.setState_id(((BigDecimal) row[STATE_ID_IDX]).longValueExact());
            }
            aclMembers.add(member);
        }
        return aclMembers;
    }

    @Override
    public List<Long> getPhoenixAdministratorsForACL(long aclId) {
        OrionAcl orionAcl = orionAclRepository.findOne(aclId);
        String adminGroupName = orionAcl.getName() + GROUP_ADMIN_SUFFIX;

        List<OrionGroup> orionGroups = orionGroupRepository.findByGroupName(adminGroupName);
        List<Long> result = new ArrayList<Long>();
        if (orionGroups.size() == 1) {
            for (OrionGroupMember member : orionGroups.get(0).getMembers()) {
                result.add(member.getId().getUserId());
            }
        }
        return result;
    }
    
    @Override
    public OrionUserDTO findUserByUserName(String userName) {
        OrionUser orionUser = orionUserRepository.findByUsernameIgnoreCase(userName);
        if (orionUser != null) {
            return new OrionUserDTO(userName, orionUser.getRealname());
        }
        return null;
    }

    public List<Long> getPhoenixAclIdsForBusinessAdministrator(String username) {
        List<Long> orionAcls = translateToLongs(orionAclRepository.findAclIdByUserName(username));
        return orionAcls;
    }

    private List<Long> translateToLongs(List<Object[]> aclsResultSet) {
        List<Long> results = new ArrayList<Long>();
        if (aclsResultSet.size() > 0) {
            for (Object aclResultSet : aclsResultSet.toArray()) {
                results.add(((BigDecimal) aclResultSet).longValueExact());
            }
        }
        return results;
    }


}

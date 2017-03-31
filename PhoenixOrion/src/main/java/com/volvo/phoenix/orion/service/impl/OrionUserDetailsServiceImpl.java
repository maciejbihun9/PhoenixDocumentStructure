package com.volvo.phoenix.orion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.volvo.phoenix.orion.entity.OrionGroup;
import com.volvo.phoenix.orion.entity.OrionUser;
import com.volvo.phoenix.orion.repository.OrionUserRepository;

/**
 * A custom {@link UserDetailsService} where user information is retrieved from a JPA repository.
 */
@Service("orionUserDetailsServiceBean")
@Transactional(readOnly = true)
public class OrionUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private OrionUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails user = null;
        if (StringUtils.hasText(username)) {
            OrionUser dbUser = userRepo.findByUsernameIgnoreCase(username);
            if (dbUser != null) {
                List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
                boolean isPhoenixAdmin = false;
                boolean isGroupAdmin = false;
                boolean isBusinessAdmin = false;
                // not used anymore
                // boolean isProjectAdmin = false;
                if (dbUser.getGroups() != null) {
                    for (OrionGroup role : dbUser.getGroups()) {
                        roles.add(new SimpleGrantedAuthority("ROLE_" + role.getGroupName()));
                        if (!isPhoenixAdmin && "Phoenix.adm".equals(role.getGroupName())) {
                            isPhoenixAdmin = true;
                        } else if (!isGroupAdmin && "Phoenix.group.adm".equals(role.getGroupName())) {
                            isGroupAdmin = true;
                        } else if (!isBusinessAdmin && role.getGroupName().endsWith("BADM")) {
                            isBusinessAdmin = true;
                        }
                    }

                }
                roles.add(new SimpleGrantedAuthority("ROLE_USER"));
                if (isPhoenixAdmin) {
                    roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }
                if (isGroupAdmin) {
                    roles.add(new SimpleGrantedAuthority("ROLE_GROUP_ADMIN"));
                }
                if (isBusinessAdmin) {
                    roles.add(new SimpleGrantedAuthority("ROLE_BUSINESS_ADMIN"));
                }
                user = new org.springframework.security.core.userdetails.User(username, dbUser.getPassword(), "Y".equals(dbUser.getActiveUser()), true, true,
                                                                              true, roles);
            }
        }
        return user;
    }

}

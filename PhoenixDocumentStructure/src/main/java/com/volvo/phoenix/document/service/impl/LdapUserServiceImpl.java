package com.volvo.phoenix.document.service.impl;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import com.volvo.phoenix.document.service.LdapUserService;

@Service
public class LdapUserServiceImpl implements LdapUserService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public List<String> getUserEmails(final String userId) {

        if (userId == null || userId.isEmpty()) {
            return null;
        }

        return ldapTemplate.search("DC=vcn,DC=ds,DC=volvo,DC=net", "(&(objectClass=User)(objectCategory=Person)(cn=" + userId + "))",
                                   new AttributesMapper<String>() {
                                       public String mapFromAttributes(Attributes attrs) throws NamingException {
                                           return attrs.get("mail").get().toString();
                                       }
                                   });
    }
    
}

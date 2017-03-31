package com.volvo.phoenix.document.service;

import java.util.List;


public interface LdapUserService {

    List<String> getUserEmails(String userId);
}

package com.volvo.phoenix.orion.dto;

public class OrionUserDTO {

    private String username;
    private String realname;

    public OrionUserDTO(String username, String realname) {
        super();
        this.username = username;
        this.realname = realname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

}

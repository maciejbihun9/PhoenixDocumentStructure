package com.volvo.phoenix.document.dto;

import java.io.Serializable;

public class PhoenixAclDTO implements Serializable {

    private static final long serialVersionUID = -29881680124765365L;

    private Long id;

    private DomainDTO domain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomainDTO getDomain() {
        return domain;
    }

    public void setDomain(DomainDTO domain) {
        this.domain = domain;
    }

}

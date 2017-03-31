package com.volvo.phoenix.document.translator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.volvo.phoenix.document.dto.PhoenixAclDTO;
import com.volvo.phoenix.document.entity.PhoenixAcl;

@Component
@Transactional(propagation = Propagation.SUPPORTS)
public class PhoenixAclTranslator {

    @Autowired
    private DomainTranslator domainTranslator;

    public PhoenixAclDTO translateToDto(PhoenixAcl entity) {
        PhoenixAclDTO dto = new PhoenixAclDTO();

        dto.setDomain(domainTranslator.translateToDto(entity.getDomain()));
        dto.setId(entity.getId());

        return dto;
    }

}

package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.AttributeDefinitionDTO;
import com.volvo.phoenix.document.dto.DomainDTO;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.DomainAttributeDefinition;

@Component
public class DomainTranslator {

    @Autowired
    private AttributeDefinitionTranslator phoenixAttributeTranslator;
    @Autowired
    private DocumentTypeTranslator documentTypeTranslator;

    public DomainDTO translateToDto(Domain entity) {
        DomainDTO dto = new DomainDTO();

        dto.setAttributes(phoenixAttributeTranslator.translateToDto(entity.getAttributes()));
        //dto.setDocumentTypes(documentTypeTranslator.translateToDto(entity.getDocumentTypes()));
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }
}

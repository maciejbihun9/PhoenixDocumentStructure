package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.DocumentAttributeDTO;
import com.volvo.phoenix.document.dto.DocumentAttributePKDTO;
import com.volvo.phoenix.document.entity.DocumentAttribute;
import com.volvo.phoenix.document.entity.DocumentAttributePK;

@Component
public class DocumentAttributeTranslator {

    private AttributeDefinitionTranslator attributeDefinitionTranslator;

    @Autowired
    public void setAttributeDefinitionTranslator(AttributeDefinitionTranslator phoenixAttributeTranslator) {
        this.attributeDefinitionTranslator = phoenixAttributeTranslator;
    }

    public DocumentAttributeDTO translateToDto(DocumentAttribute entity) {
        DocumentAttributeDTO dto = new DocumentAttributeDTO();
        dto.setId(translatePKToDto(entity.getId()));
        dto.setValue(entity.getValue());
        dto.setDictionaryValue(entity.getDictionaryValue());
        dto.setAttribute(attributeDefinitionTranslator.translateToDto(entity.getAttribute()));

        return dto;
    }


    private DocumentAttributePKDTO translatePKToDto(DocumentAttributePK pk) {
        return new DocumentAttributePKDTO(pk.getDocument(), pk.getAttribute());
    }

    public List<DocumentAttribute> translateToEntity(List<DocumentAttributeDTO> dtos) {
        List<DocumentAttribute> entities = Lists.newArrayList();

        for (DocumentAttributeDTO dto : dtos) {
            entities.add(translateToEntity(dto));
        }
        return entities;
    }

    public DocumentAttribute translateToEntity(DocumentAttributeDTO dto) {
        DocumentAttribute entity = new DocumentAttribute();
        entity.setId(translatePKToEntity(dto.getId()));
        entity.setValue(dto.getValue());
        entity.setAttribute(attributeDefinitionTranslator.translateToEntity(dto.getAttribute()));

        return entity;
    }

    private DocumentAttributePK translatePKToEntity(DocumentAttributePKDTO pkDto) {
        return new DocumentAttributePK(pkDto.getDocumentId(), pkDto.getAttributeId());
    }

    public List<DocumentAttributeDTO> translateToDto(List<DocumentAttribute> entities) {
        List<DocumentAttributeDTO> dtos = Lists.newArrayList();

        for (DocumentAttribute documentAttribute : entities) {
            dtos.add(translateToDto(documentAttribute));
        }

        return dtos;
    }

}

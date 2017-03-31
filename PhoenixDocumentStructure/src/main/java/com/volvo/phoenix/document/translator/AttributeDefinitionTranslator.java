package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.AttributeDefinitionDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;

@Component
@Transactional(propagation = Propagation.SUPPORTS)
public class AttributeDefinitionTranslator {

    @Autowired
    private DictionaryTranslator dictionaryTranslator;

    public void setDictionaryTranslator(DictionaryTranslator dictionaryTranslator) {
        this.dictionaryTranslator = dictionaryTranslator;
    }

    public AttributeDefinitionDTO translateToDto(final AttributeDefinition attributeDefinition) {

        final AttributeDefinitionDTO attributeDefinitionDTO = new AttributeDefinitionDTO();

        attributeDefinitionDTO.setDescription(attributeDefinition.getDescription());
        attributeDefinitionDTO.setId(attributeDefinition.getId());
        attributeDefinitionDTO.setLabel(attributeDefinition.getLabel());
        attributeDefinitionDTO.setName(attributeDefinition.getName());
        attributeDefinitionDTO.setType(attributeDefinition.getType());
        attributeDefinitionDTO.setDictionary(dictionaryTranslator.translateToDto(attributeDefinition.getDictionary()));

        return attributeDefinitionDTO;
    }

    public AttributeDefinition translateToEntity(AttributeDefinitionDTO dto) {
        AttributeDefinition entity = new AttributeDefinition();

        entity.setDescription(dto.getDescription());
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());
        entity.setName(dto.getName());
        entity.setType(dto.getType());

        return entity;
    }

    public List<AttributeDefinitionDTO> translateToDto(final List<AttributeDefinition> attributeDefinitions) {

        final List<AttributeDefinitionDTO> attributeDefinitionDTOs = Lists.newArrayList();

        if (attributeDefinitions != null) {
            for (final AttributeDefinition attributeDefinition : attributeDefinitions) {
                attributeDefinitionDTOs.add(translateToDto(attributeDefinition));
            }
        }

        return attributeDefinitionDTOs;
    }

}

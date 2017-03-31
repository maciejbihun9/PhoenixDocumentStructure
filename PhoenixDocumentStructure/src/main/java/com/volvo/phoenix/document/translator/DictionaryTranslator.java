package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.AttributeDefinitionDTO;
import com.volvo.phoenix.document.dto.DictionaryDTO;
import com.volvo.phoenix.document.dto.DictionaryValueDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.Dictionary;
import com.volvo.phoenix.document.entity.DictionaryValue;

@Component
@Transactional(propagation = Propagation.SUPPORTS)
public class DictionaryTranslator {

    public DictionaryDTO translateToDto(final Dictionary dictionary) {
        
        if (dictionary == null) {
            return new DictionaryDTO();
        }
        
        final DictionaryDTO dictionaryDTO = new DictionaryDTO();

        dictionaryDTO.setDescription(dictionary.getDescription());
        dictionaryDTO.setId(dictionary.getId());
        dictionaryDTO.setName(dictionary.getName());
        dictionaryDTO.setValues(translateToDto(dictionary.getValues()));

        return dictionaryDTO;
    }

    private List<DictionaryValueDTO> translateToDto(final List<DictionaryValue> dictionaryValues) {
        
        final List<DictionaryValueDTO> dictionaryValueDTOs = Lists.newArrayList();
        for (final DictionaryValue dictionaryValue : dictionaryValues) {
            dictionaryValueDTOs.add(translateToDto(dictionaryValue));
        }

        return dictionaryValueDTOs;
    }

    private DictionaryValueDTO translateToDto(final DictionaryValue dictionaryValue) {
        
        final DictionaryValueDTO dictionaryValueDTO = new DictionaryValueDTO();

        dictionaryValueDTO.setDescription(dictionaryValue.getDescription());
        dictionaryValueDTO.setId(dictionaryValue.getId());
        dictionaryValueDTO.setOrder(dictionaryValue.getOrder());
        dictionaryValueDTO.setValue(dictionaryValue.getValue());

        return dictionaryValueDTO;
    }

//    public AttributeDefinition translateToEntity(AttributeDefinitionDTO dto) {
//        AttributeDefinition entity = new AttributeDefinition();
//
//        entity.setDescription(dto.getDescription());
//        entity.setId(dto.getId());
//        entity.setLabel(dto.getLabel());
//        entity.setName(dto.getName());
//        entity.setType(dto.getType());
//
//        return entity;
//    }

}

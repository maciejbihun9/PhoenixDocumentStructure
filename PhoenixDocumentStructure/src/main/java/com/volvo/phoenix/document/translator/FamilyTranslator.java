package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.FamilyDTO;
import com.volvo.phoenix.document.entity.Family;

@Component
public class FamilyTranslator {

    @Autowired
    private DocumentTypeTranslator documentTypeTranslator;

    public FamilyDTO translateToDto(final Family family) {

        final FamilyDTO familyDTO = new FamilyDTO();
        familyDTO.setId(family.getId());
        familyDTO.setName(family.getName());
        //familyDTO.setDocumentTypes(documentTypeTranslator.translateToDto(family.getDocumentTypes()));

        return familyDTO;
    }
    
    public List<FamilyDTO> translateToDto(final List<Family> families) {
        
        final List<FamilyDTO> familDTOs = Lists.newArrayList();

        for (final Family family : families) {
            familDTOs.add(translateToDto(family));
        }

        return familDTOs;
    }

}

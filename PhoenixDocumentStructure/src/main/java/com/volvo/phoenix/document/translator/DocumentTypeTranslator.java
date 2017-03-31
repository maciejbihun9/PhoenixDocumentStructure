package com.volvo.phoenix.document.translator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.dto.DocumentTypeDTO;
import com.volvo.phoenix.document.entity.DocumentType;

@Component
public class DocumentTypeTranslator {

    @Autowired
    private AttributeDefinitionTranslator phoenixAttributeTranslator;
    
    @Autowired
    private FamilyTranslator familyTranslator;

    public DocumentTypeDTO translateToDto(final DocumentType documentType) {

        final DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        //documentTypeDTO.setAttributes(phoenixAttributeTranslator.translateToDto(documentType.getAttributes()));
        documentTypeDTO.setId(documentType.getId());
        documentTypeDTO.setName(documentType.getName());
        documentTypeDTO.setFamilies(familyTranslator.translateToDto(documentType.getFamilies()));

        return documentTypeDTO;
    }

    public List<DocumentTypeDTO> translateToDto(final List<DocumentType> documentTypes) {

        final List<DocumentTypeDTO> documentTypeDTOs = Lists.newArrayList();

        for (final DocumentType documentType : documentTypes) {
            documentTypeDTOs.add(translateToDto(documentType));
        }

        return documentTypeDTOs;
    }

}

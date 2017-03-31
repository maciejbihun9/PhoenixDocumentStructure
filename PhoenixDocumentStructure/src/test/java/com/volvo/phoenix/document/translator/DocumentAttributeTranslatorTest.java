package com.volvo.phoenix.document.translator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.volvo.phoenix.document.dto.DocumentAttributeDTO;
import com.volvo.phoenix.document.entity.DocumentAttribute;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DocumentAttributeTranslatorTest {

    private AttributeDefinitionTranslator attributeDefinitionTranslator = new AttributeDefinitionTranslator();

    private DocumentAttributeTranslator documentAttributeTranslator = new DocumentAttributeTranslator();

    private TranslatorTestObjectsFactory translatorTestObjectsFactory = new TranslatorTestObjectsFactory();

    private DictionaryTranslator dictionaryTranslator = new DictionaryTranslator();


    @Before
    public void setup() {
        documentAttributeTranslator.setAttributeDefinitionTranslator(attributeDefinitionTranslator);
        attributeDefinitionTranslator.setDictionaryTranslator(dictionaryTranslator);
    }

    @Test
    public void shouldTranslateToDto() {
        // given
        DocumentAttribute entity = translatorTestObjectsFactory.createEntityDocumentAttribute();

        // when
        DocumentAttributeDTO dto = documentAttributeTranslator.translateToDto(entity);

        // then
        assertThat(dto.getId().getAttributeId()).isEqualTo(entity.getId().getAttribute());
        assertThat(dto.getId().getDocumentId()).isEqualTo(entity.getId().getDocument());
        assertThat(dto.getValue()).isEqualTo(entity.getValue());

        assertThat(dto.getAttribute().getDescription()).isEqualTo(entity.getAttribute().getDescription());
        assertThat(dto.getAttribute().getId()).isEqualTo(entity.getAttribute().getId());
        assertThat(dto.getAttribute().getLabel()).isEqualTo(entity.getAttribute().getLabel());
        assertThat(dto.getAttribute().getName()).isEqualTo(entity.getAttribute().getName());
        assertThat(dto.getAttribute().getType()).isEqualTo(entity.getAttribute().getType());
    }

    @Test
    public void shouldTranslateToEntity() {
        // given
        DocumentAttributeDTO dto = translatorTestObjectsFactory.createDtoDocumentAttribute();

        // when
        DocumentAttribute entity = documentAttributeTranslator.translateToEntity(dto);

        // then
        assertThat(entity.getId().getAttribute()).isEqualTo(dto.getId().getAttributeId());
        assertThat(entity.getId().getDocument()).isEqualTo(dto.getId().getDocumentId());
        assertThat(entity.getValue()).isEqualTo(dto.getValue());

        assertThat(entity.getAttribute().getDescription()).isEqualTo(dto.getAttribute().getDescription());
        assertThat(entity.getAttribute().getId()).isEqualTo(dto.getAttribute().getId());
        assertThat(entity.getAttribute().getLabel()).isEqualTo(dto.getAttribute().getLabel());
        assertThat(entity.getAttribute().getName()).isEqualTo(dto.getAttribute().getName());
        assertThat(entity.getAttribute().getType()).isEqualTo(dto.getAttribute().getType());
    }


}

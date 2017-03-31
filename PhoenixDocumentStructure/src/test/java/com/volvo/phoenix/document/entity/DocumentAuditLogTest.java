package com.volvo.phoenix.document.entity;

import java.util.List;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DocumentAuditLogTest {

    @Test
    public void shouldReturnProeprlyParsedSourceMandatoryAttributesAndValues() {
        // given
        String attributes = prepareAttributesString();

        DocumentAuditLog log = new DocumentAuditLog();
        log.setSourceMandatoryAttributes(attributes);

        // when
        List<DocumentAttributeAuditLog> attributesAsList = log.getSourceMandatoryAttributesAsList();

        // then
        assertAttributes(attributesAsList);
    }

    @Test
    public void shouldReturnProeprlyParsedTargetMandatoryAttributesAndValues() {
        // given
        String attributes = prepareAttributesString();

        DocumentAuditLog log = new DocumentAuditLog();
        log.setTargetMandatoryAttributes(attributes);

        // when
        List<DocumentAttributeAuditLog> attributesAsList = log.getTargetMandatoryAttributesAsList();

        // then
        assertAttributes(attributesAsList);
    }

    @Test
    public void shouldReturnProeprlyParsedSourceOptionlAttributesAndValues() {
        // given
        String toMandatoryAttributes = prepareAttributesString();

        DocumentAuditLog log = new DocumentAuditLog();
        log.setSourceOptionalAttributes(toMandatoryAttributes);

        // when
        List<DocumentAttributeAuditLog> attributesAsList = log.getSourceOptionalAttributesAsList();

        // then
        assertAttributes(attributesAsList);
    }

    @Test
    public void shouldReturnProeprlyParsedTargetOptionlAttributesAndValues() {
        // given
        String toMandatoryAttributes = prepareAttributesString();

        DocumentAuditLog log = new DocumentAuditLog();
        log.setTargetOptionalAttributes(toMandatoryAttributes);

        // when
        List<DocumentAttributeAuditLog> attributesAsList = log.getTargetOptionalAttributesAsList();

        // then
        assertAttributes(attributesAsList);
    }

    private void assertAttributes(List<DocumentAttributeAuditLog> attributesAsList) {
        assertThat(attributesAsList.size()).isEqualTo(2);
        assertThat(attributesAsList.get(0).getName()).isEqualTo("attr1");
        assertThat(attributesAsList.get(0).getValue()).isEqualTo("val1");
        assertThat(attributesAsList.get(1).getName()).isEqualTo("attr2");
        assertThat(attributesAsList.get(1).getValue()).isEqualTo("val2");
    }

    private String prepareAttributesString() {
        String toMandatoryAttributes = String.format("attr1 %s val1 %s attr2 %s val2", DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR,
                DocumentAuditLog.ATTRIBUTES_SEPARATOR, DocumentAuditLog.ATTRIBUTE_VALUE_SEPARATOR);
        return toMandatoryAttributes;
    }

}

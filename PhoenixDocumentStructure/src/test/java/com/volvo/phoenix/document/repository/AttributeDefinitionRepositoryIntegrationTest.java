package com.volvo.phoenix.document.repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.Dictionary;
import com.volvo.phoenix.document.entity.DictionaryValue;
import com.volvo.phoenix.orion.util.SqlUtils;

public class AttributeDefinitionRepositoryIntegrationTest extends AbstractTransactionalTestCase {

    private static final String CREATE_TEST_DATA = "./src/test/resources/createAttributeDefinitionTestData.sql";
    @Autowired
    private AttributeDefinitionRepository attributeDefinitionRepository;
    @Autowired
    private SqlUtils sqlUtils;
    @Autowired
    @Named("dataSource")
    private DataSource dataSource;


    @Test
    public void shouldFindAttributeDefinitions() throws IOException, SQLException {
        // given
        sqlUtils.executeSql(dataSource, CREATE_TEST_DATA, new Object[] {});
        List<Long> attributeIds = Lists.newArrayList(-1L, -2L, -3L);
        Long attributeWithDictionaryId = -3L;
        Dictionary dictionary = prepareExpectedDictionary();

        // when
        List<AttributeDefinition> attributes = Lists.newArrayList(attributeDefinitionRepository.findAll(attributeIds));
        AttributeDefinition attrWithDictionary = attributeDefinitionRepository.findOne(attributeWithDictionaryId);

        // then
        Assertions.assertThat(attributes).isNotNull();
        Assertions.assertThat(attributes.size()).isEqualTo(3);

        Assertions.assertThat(attrWithDictionary.getDictionary().getId()).isEqualTo(dictionary.getId());
        Assertions.assertThat(Lists.newArrayList(attrWithDictionary.getDictionary().getValues())).isEqualTo(dictionary.getValues());

    }

    private Dictionary prepareExpectedDictionary() {
        Dictionary dictionary = new Dictionary();

        DictionaryValue v1 = new DictionaryValue(-1L, "val1", "Val1", 1);
        DictionaryValue v2 = new DictionaryValue(-2L, "val2", "Val2", 2);
        DictionaryValue v3 = new DictionaryValue(-3L, "val3", "Val3", 3);
        DictionaryValue v4 = new DictionaryValue(-4L, "val4", "Val4", 4);
        List<DictionaryValue> values = Lists.newArrayList(v1, v2, v3, v4);

        dictionary.setId(-1L);
        dictionary.setName("Dictionary1");
        dictionary.setDescription("Dictionary1");
        dictionary.setValues(values);

        return dictionary;
    }
}

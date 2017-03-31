package com.volvo.phoenix.document.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.volvo.jvs.test.AbstractTransactionalTestCase;
import com.volvo.phoenix.document.entity.Dictionary;
import com.volvo.phoenix.document.entity.DictionaryValue;

public class DictionaryRepositoryIntegrationTest extends AbstractTransactionalTestCase {

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @PersistenceContext(unitName = "PhoenixDocumentStructurePU")
    private EntityManager em;

    @Test
    public void shouldCreateAndFindDictionary() {
        // given
        Long dictionaryId = -12L;
        DictionaryValue dv1 = new DictionaryValue(-1L, "value1", "desc1", 2);
        DictionaryValue dv2 = new DictionaryValue(-2L, "value2", "desc2", 1);
        DictionaryValue dv3 = new DictionaryValue(-3L, "value3", "desc3", 4);
        DictionaryValue dv4 = new DictionaryValue(-4L, "value4", "desc4", 3);

        prepareDictionary(dictionaryId, dv1, dv2, dv3, dv4);

        // when
        Dictionary dictionary = dictionaryRepository.findOne(dictionaryId);

        // then
        Assertions.assertThat(dictionary).isNotNull();
        Assertions.assertThat(dictionary.getId()).isEqualTo(dictionaryId);

        // check values order
        System.out.println(dictionary.getValues());

        Assertions.assertThat(dictionary.getValues().get(0)).isEqualTo(dv2);
        Assertions.assertThat(dictionary.getValues().get(1)).isEqualTo(dv1);
        Assertions.assertThat(dictionary.getValues().get(2)).isEqualTo(dv4);
        Assertions.assertThat(dictionary.getValues().get(3)).isEqualTo(dv3);
    }

    private void prepareDictionary(Long dictionaryId, DictionaryValue... values) {

        List<DictionaryValue> dictionaryValues = Lists.newArrayList(values);

        Dictionary dictionary = new Dictionary();
        dictionary.setId(dictionaryId);
        dictionary.setDescription("description");
        dictionary.setName("Test dictionary");
        dictionary.setValues(dictionaryValues);

        dictionaryRepository.save(dictionary);

        em.flush();
        em.clear();
    }
}

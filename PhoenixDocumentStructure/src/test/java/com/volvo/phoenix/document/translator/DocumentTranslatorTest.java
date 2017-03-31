package com.volvo.phoenix.document.translator;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.dto.PhoenixDocumentDTO;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.entity.Document;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTranslatorTest {

    private DocumentTranslator documentTranslator = new DocumentTranslator();

    private TranslatorTestObjectsFactory translatorTestObjectsFactory = new TranslatorTestObjectsFactory();

    @Test
    public void shouldTranslateToTreeNodeDto() {
        // given
        Document entity = translatorTestObjectsFactory.createEntityDocument();
        // when
        TreeNodeDTO dto = documentTranslator.translateToTreeNodeDto(entity);
        // then
        Assertions.assertThat(dto.getId()).isEqualTo(entity.getId());
        Assertions.assertThat(dto.getName()).isEqualTo(entity.getTitle());
        Assertions.assertThat(dto.getType()).isEqualTo(NodeType.D);
        Assertions.assertThat(dto.getPath()).isEqualTo(entity.getPath());
        Assertions.assertThat(dto.getFamily()).isEqualTo(entity.getFamily().getName());
        Assertions.assertThat(dto.getDocType()).isEqualTo(entity.getType().getName());
        Assertions.assertThat(dto.getOwner()).isEqualTo(entity.getAuthor());
    }

    @Test
    public void shouldTranslateToPhoenixDocumentDto() {
        // given
        Document entity = translatorTestObjectsFactory.createEntityDocument();
        
        // when
        PhoenixDocumentDTO dto = documentTranslator.translateToPhoenixDTO(entity);
        
        // then
        Assertions.assertThat(dto.getdescription()).isNotEmpty();
        Assertions.assertThat(dto.getissuer()).isNotEmpty();
        Assertions.assertThat(dto.getissuer_id()).isNotEmpty();
        Assertions.assertThat(dto.getauthor_id()).isNotEmpty();
        Assertions.assertThat(dto.getauthor()).isNotEmpty();
        Assertions.assertThat(dto.getAttributeValues()).isNotEmpty();
        Assertions.assertThat(dto.getissue_date()).isNotNull();
        Assertions.assertThat(dto.getnotes()).isNotEmpty();
        Assertions.assertThat(dto.getpersonal()).isNotEmpty();
        Assertions.assertThat(dto.getobject_no()).isNotEmpty();
        Assertions.assertThat(dto.getobject_no_suffix()).isNotEmpty();
        Assertions.assertThat(dto.getdecision()).isNotEmpty();
        Assertions.assertThat(dto.getfunction_group()).isNotNull();
        Assertions.assertThat(dto.getgate()).isNotEmpty();
        Assertions.assertThat(dto.getproduct_class()).isNotEmpty();
        Assertions.assertThat(dto.getlogbookmajorversion()).isNotEmpty();
        Assertions.assertThat(dto.gettempstore()).isNotEmpty();
        Assertions.assertThat(dto.getbrand()).isNotEmpty();
        Assertions.assertThat(dto.gettitle()).isNotEmpty();
        Assertions.assertThat(dto.getdocument_status()).isNotEmpty();
        Assertions.assertThat(dto.getcontent_status()).isNotEmpty();
        Assertions.assertThat(dto.getnode_id()).isNotNull();
        Assertions.assertThat(dto.getfamily_id()).isNotNull();
        Assertions.assertThat(dto.getdomain_id()).isNotNull();
        Assertions.assertThat(dto.getalt_doc_id()).isNotEmpty();
        Assertions.assertThat(dto.getdocumentcontainer()).isNotEmpty();
        Assertions.assertThat(dto.getdoctype_id()).isNotNull();
        Assertions.assertThat(dto.getProtectInWork()).isNotEmpty();
    }    
}

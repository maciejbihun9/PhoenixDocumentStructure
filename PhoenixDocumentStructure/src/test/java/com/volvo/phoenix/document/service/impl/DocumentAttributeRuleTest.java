package com.volvo.phoenix.document.service.impl;

import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.dto.AttributeDefinitionDTO;
import com.volvo.phoenix.document.dto.ConflictDTO;
import com.volvo.phoenix.document.dto.DocumentAttributeConflictDTO;
import com.volvo.phoenix.document.dto.OperationDTO;
import com.volvo.phoenix.document.dto.TreeNodeDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentAttribute;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.DomainAttributeDefinition;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.PhoenixAcl;
import com.volvo.phoenix.document.repository.DocumentRepository;
import com.volvo.phoenix.document.repository.DomainRepository;
import com.volvo.phoenix.document.repository.FolderRepository;
import com.volvo.phoenix.document.translator.AttributeDefinitionTranslator;

@RunWith(MockitoJUnitRunner.class)
public class DocumentAttributeRuleTest {

    @Mock
    private OperationDTO operation;
    @Mock
    private FolderRepository folderRepository;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private Folder targetFolder;
    @Mock
    private Document sourceDocument;
    @Mock
    private Domain sourceDomain;
    @Mock
    private DomainRepository domainRepository;

    @Mock
    private AttributeDefinitionTranslator attributeDefinitionTranslator;

    @InjectMocks
    private DocumentAttributeRule rule;

    @Mock
    private PhoenixAcl targetFolderAcl;
    @Mock
    private Domain targetDomain;
    @Mock
    private DocumentAttribute docAttribute;
    @Mock
    private AttributeDefinitionDTO attributeDefDto1;
    @Mock
    private AttributeDefinitionDTO attributeDefDto2;

    @Test
    public void shouldReturnNullIfFolderOperation() {
        // given
        TreeNodeDTO node = Mockito.mock(TreeNodeDTO.class);
        Mockito.when(operation.getSource()).thenReturn(node);
        Mockito.when(node.getType()).thenReturn(NodeType.M);

        // when
        ConflictDTO conflict = rule.check(operation);

        // then
        Assertions.assertThat(conflict).isNull();
    }

    @Test
    public void shouldReturnNullIfDocumentOperationAndSourceAndTargetDomainsAreTheSame() {
        // given
        TreeNodeDTO sourceNode = Mockito.mock(TreeNodeDTO.class);
        TreeNodeDTO targetNode = Mockito.mock(TreeNodeDTO.class);
        Long targetFolderId = -55L;
        Long sourceDocumentId = -66L;

        Mockito.when(operation.getSource()).thenReturn(sourceNode);
        Mockito.when(operation.getTarget()).thenReturn(targetNode);
        Mockito.when(targetNode.getId()).thenReturn(targetFolderId);
        Mockito.when(sourceNode.getType()).thenReturn(NodeType.D);
        Mockito.when(sourceNode.getId()).thenReturn(sourceDocumentId);
        Mockito.when(folderRepository.findOne(Mockito.anyLong())).thenReturn(targetFolder);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(sourceDocument.getDomain()).thenReturn(sourceDomain);
        Long sourceDomainId = -23L;
        Mockito.when(sourceDomain.getId()).thenReturn(sourceDomainId);
        Mockito.when(targetFolder.getAcl()).thenReturn(targetFolderAcl);
        Mockito.when(targetFolderAcl.getDomain()).thenReturn(targetDomain);
        Long targetDomainId = sourceDomainId;
        Mockito.when(targetDomain.getId()).thenReturn(targetDomainId);

        // when
        ConflictDTO conflict = rule.check(operation);

        // then
        Assertions.assertThat(conflict).isNull();
    }

    @Test @Ignore
    public void shouldReturnConflictObject() {
        // given
        TreeNodeDTO sourceNode = Mockito.mock(TreeNodeDTO.class);
        TreeNodeDTO targetNode = Mockito.mock(TreeNodeDTO.class);
        Long targetFolderId = -55L;
        Long sourceDocumentId = -66L;

        Mockito.when(operation.getSource()).thenReturn(sourceNode);
        Mockito.when(operation.getTarget()).thenReturn(targetNode);
        Mockito.when(targetNode.getId()).thenReturn(targetFolderId);
        Mockito.when(sourceNode.getType()).thenReturn(NodeType.D);
        Mockito.when(sourceNode.getId()).thenReturn(sourceDocumentId);
        Mockito.when(folderRepository.findOne(Mockito.anyLong())).thenReturn(targetFolder);
        Mockito.when(documentRepository.findOne(Mockito.anyLong())).thenReturn(sourceDocument);
        Mockito.when(sourceDocument.getDomain()).thenReturn(sourceDomain);
        Long sourceDomainId = -23L;
        Mockito.when(sourceDomain.getId()).thenReturn(sourceDomainId);
        Mockito.when(targetFolder.getAcl()).thenReturn(targetFolderAcl);
        Mockito.when(targetFolderAcl.getDomain()).thenReturn(targetDomain);
        Long targetDomainId = -32L;
        Mockito.when(targetDomain.getId()).thenReturn(targetDomainId);

        Mockito.when(domainRepository.findById(sourceDomainId)).thenReturn(sourceDomain);
        Mockito.when(domainRepository.findById(targetDomainId)).thenReturn(targetDomain);

        List<DomainAttributeDefinition> sourceDomainAttributes = preparePheonixAttributes("sAttr1", -1L);
        List<DomainAttributeDefinition> targetDomainAttributes = preparePheonixAttributes("tAttr1", -2L);

        Mockito.when(sourceDomain.getDomainAttributes()).thenReturn(sourceDomainAttributes);
        Mockito.when(targetDomain.getDomainAttributes()).thenReturn(targetDomainAttributes);
        List<AttributeDefinitionDTO> attrs1 = Lists.newArrayList(attributeDefDto1);
        List<AttributeDefinitionDTO> attrs2 = Lists.newArrayList(attributeDefDto2);
        Mockito.when(attributeDefinitionTranslator.translateToDto(Mockito.anyListOf(AttributeDefinition.class))).thenReturn(attrs1, attrs2);

        List<AttributeDefinitionDTO> expectedNotFoundAttributes = Lists.newArrayList(attributeDefDto1);
        List<AttributeDefinitionDTO> expectedNewAttributes = Lists.newArrayList(attributeDefDto2);

        // when
        ConflictDTO conflict = rule.check(operation);

        // then
        Assertions.assertThat(conflict).isNotNull();
        Assertions.assertThat(conflict).isInstanceOf(DocumentAttributeConflictDTO.class);
//        Assertions.assertThat(((DocumentAttributeConflictDTO) conflict).getNotFoundAttrs()).isEqualTo(expectedNotFoundAttributes);
//        Assertions.assertThat(((DocumentAttributeConflictDTO) conflict).getNewAttributes()).isEqualTo(expectedNewAttributes);
    }

    private List<DomainAttributeDefinition> preparePheonixAttributes(Object... namesIds) {
        List<DomainAttributeDefinition> attrs = Lists.newArrayList();

        for (int i = 0; i < namesIds.length; i += 2) {
            attrs.add(preparePhoenixAttribute((String) namesIds[i], (Long) namesIds[i + 1]));
        }

        return attrs;
    }

    private DomainAttributeDefinition preparePhoenixAttribute(String name, Long id) {
        AttributeDefinition attr = new AttributeDefinition();
        attr.setName(name);
        attr.setId(id);
        
        DomainAttributeDefinition mock = Mockito.mock(DomainAttributeDefinition.class);
        Mockito.when(mock.getAttribute()).thenReturn(attr);
        return mock;
    }

}

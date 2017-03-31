package com.volvo.phoenix.document.translator;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.datatype.AttributeType;
import com.volvo.phoenix.document.datatype.NodeType;
import com.volvo.phoenix.document.dto.AttributeDefinitionDTO;
import com.volvo.phoenix.document.dto.DictionaryDTO;
import com.volvo.phoenix.document.dto.DictionaryValueDTO;
import com.volvo.phoenix.document.dto.DocumentAttributeDTO;
import com.volvo.phoenix.document.dto.DocumentAttributePKDTO;
import com.volvo.phoenix.document.entity.AttributeDefinition;
import com.volvo.phoenix.document.entity.Dictionary;
import com.volvo.phoenix.document.entity.DictionaryValue;
import com.volvo.phoenix.document.entity.Document;
import com.volvo.phoenix.document.entity.DocumentAttribute;
import com.volvo.phoenix.document.entity.DocumentAttributePK;
import com.volvo.phoenix.document.entity.DocumentAttributeValue;
import com.volvo.phoenix.document.entity.DocumentStatus;
import com.volvo.phoenix.document.entity.DocumentType;
import com.volvo.phoenix.document.entity.Domain;
import com.volvo.phoenix.document.entity.DomainAttributeDefinition;
import com.volvo.phoenix.document.entity.DomainAttributeDefinitionPK;
import com.volvo.phoenix.document.entity.Family;
import com.volvo.phoenix.document.entity.Folder;
import com.volvo.phoenix.document.entity.PhoenixAcl;
import com.volvo.phoenix.orion.dto.OrionAclDTO;
import com.volvo.phoenix.orion.dto.OrionAclStateDTO;
import com.volvo.phoenix.orion.dto.OrionDocumentDTO;

/**
 * Objects factory for test purpose.
 */
public class TranslatorTestObjectsFactory {

    public DocumentAttribute createEntityDocumentAttribute() {
        Dictionary dictionary = createDictionary();
        AttributeDefinition phoenixAttribute = createEntityPhoenixAttribute(dictionary);

        DocumentAttribute entity = new DocumentAttribute();
        DocumentAttributePK pk = new DocumentAttributePK(getRandomLong(), getRandomLong());
        entity.setId(pk);
        entity.setValue("some value");
        entity.setAttribute(phoenixAttribute);
        return entity;
    }

    public AttributeDefinition createEntityPhoenixAttribute(Dictionary dictionary) {

        AttributeDefinition phoenixAttribute = new AttributeDefinition();
        phoenixAttribute.setDescription("description");
        phoenixAttribute.setId(getRandomLong());
        phoenixAttribute.setLabel("label");
        phoenixAttribute.setDictionary(dictionary);
        phoenixAttribute.setName("name");
        phoenixAttribute.setType(AttributeType.TEXT);
        return phoenixAttribute;
    }

    public Dictionary createDictionary() {
        DictionaryValue dv = new DictionaryValue(-2L, "dictionary value", "value desc", 1);
        Dictionary dictionary = new Dictionary();
        dictionary.setDescription("Dictionary desc");
        dictionary.setId(-100L);
        dictionary.setName("Dictionary name");
        dictionary.setValues(Lists.newArrayList(dv));

        return dictionary;
    }

    private Long getRandomLong() {
        return (long) (Math.random() * 10000);
    }

    public DocumentAttributeDTO createDtoDocumentAttribute() {
        DocumentAttributeDTO dto = new DocumentAttributeDTO();

        AttributeDefinitionDTO attribute = createDtoAttributeDefinition();

        DocumentAttributePKDTO pk = new DocumentAttributePKDTO(getRandomLong(), getRandomLong());

        dto.setAttribute(attribute);
        dto.setId(pk);
        dto.setValue("some value");

        return dto;
    }

    public AttributeDefinitionDTO createDtoAttributeDefinition() {
        AttributeDefinitionDTO attribute = new AttributeDefinitionDTO();
        attribute.setDescription("description");
        attribute.setId(1L);
        attribute.setLabel("description");
        attribute.setDictionary(createDtoDictionary());
        attribute.setName("name");
        attribute.setType(AttributeType.TEXT);
        return attribute;
    }

    private DictionaryDTO createDtoDictionary() {
        DictionaryDTO dictionary = new DictionaryDTO();
        dictionary.setDescription("description");
        dictionary.setId(getRandomLong());
        dictionary.setName("dictionary name");
        dictionary.setValues(Lists.newArrayList(crateDtoDictionaryValue(), crateDtoDictionaryValue()));

        return dictionary;
    }

    private DictionaryValueDTO crateDtoDictionaryValue() {
        DictionaryValueDTO dv = new DictionaryValueDTO();
        Long id = getRandomLong();

        dv.setDescription("description" + id);
        dv.setId(id);
        dv.setOrder(1);
        dv.setValue("value" + id);

        return dv;
    }

    public Document createEntityDocument() {
        Document entity = new Document();

        Dictionary dictionary = createDictionary();
        List<AttributeDefinition> attributes = Lists.newArrayList(createEntityPhoenixAttribute(dictionary));
        entity.setAttributes(attributes);
        
        List<DocumentAttributeValue> attributeValues = Lists.newArrayList(createEntityDocumentAttributeValue());
        entity.setDocumentAttributes(attributeValues);

        entity.setAuthor("author");
        entity.setDomain(createEntityDomain());
        entity.setFamily(createEntityFamily());
        entity.setFolder(createEntityFolder());
        entity.setId(1L);
        entity.setTitle("title");
        entity.setType(createEntityDocumentType());
        entity.setDescription("description");
        entity.setIssuer("issuer");
        entity.setIssuerId("issuerId");
        entity.setIssueDate(new Date());
        entity.setAuthorId("authorId");
        entity.setIssueDate(new Date());
        entity.setNotes("notes");
        entity.setObjectNo("objectNo");
        entity.setObjectNoSuffix("objectNoSuffix");
        entity.setPersonal("personal");
        entity.setFunctionGroup(-1L);
        entity.setDecision("decision");
        entity.setGate("gate");
        entity.setProductClass("productClass");
        entity.setBrand("brand");
        
        DocumentStatus docStatus = new DocumentStatus();
        docStatus.setStatus("VALID");
        entity.setStatus(docStatus);
        entity.setLogbookMajorVersion("logbookMajorVersion");
        entity.setTempstore("tempstore");
        entity.setContentStatus("VALID");
        entity.setAltDocId("altDocId");
        entity.setDocumentContainer("documentContainer");
        entity.setProtectInWork("N");

        return entity;
    }

    private DocumentAttributeValue createEntityDocumentAttributeValue() {
        DocumentAttributeValue attr = new DocumentAttributeValue();
        DocumentAttributePK pk = new DocumentAttributePK();
        pk.setAttribute(-1L);
        pk.setDocument(-1L);
        attr.setId(pk);
        attr.setValue("value");
        return attr;
    }

    private DocumentType createEntityDocumentType() {
        DocumentType documentType = new DocumentType();

        List<AttributeDefinition> attributes = Lists.newArrayList(createEntityPhoenixAttribute(createDictionary()));
        documentType.setAttributes(attributes);
        documentType.setId(getRandomLong());
        documentType.setName("documentType name");

        return documentType;
    }

    private Folder createEntityFolder() {
        Folder folder = new Folder();

        folder.setAcl(createEntityPhoenixAcl());
        folder.setId(getRandomLong());
        folder.setOwnerRealname("folder owner");
        folder.setParent(createEntityParentFolder());
        folder.setText("folder text");
        folder.setType(NodeType.S);

        return folder;
    }

    private Folder createEntityParentFolder() {
        Folder folder = new Folder();

        folder.setAcl(createEntityPhoenixAcl());
        folder.setId(getRandomLong());
        folder.setOwnerRealname("parent folder owner");
        folder.setParent(null);
        folder.setText("parent folder text");
        folder.setType(NodeType.M);

        return folder;
    }

    private PhoenixAcl createEntityPhoenixAcl() {
        return new PhoenixAcl(getRandomLong(), createEntityDomain());
    }

    private Family createEntityFamily() {
        return new Family(getRandomLong(), "Family name");
    }

    private Domain createEntityDomain() {
        Domain domain = new Domain();

        domain.setId(getRandomLong());
        List<DomainAttributeDefinition> attributes = Lists.newArrayList(createDomainAttributeDefinition(domain , createEntityPhoenixAttribute(createDictionary())));
        List<DocumentType> documentTypes = Lists.newArrayList(createEntityDocumentType());

        domain.setDomainAttributes(attributes);
        domain.setDocumentTypes(documentTypes);
        domain.setName("domain name");

        return domain;
    }

    private DomainAttributeDefinition createDomainAttributeDefinition(Domain domain, AttributeDefinition attribute) {
        DomainAttributeDefinition domainAttrDef = new DomainAttributeDefinition();
        DomainAttributeDefinitionPK pk = new DomainAttributeDefinitionPK();
        pk.setAttributeDefinitionId(attribute.getId());
        pk.setDomainId(domain.getId());
        domainAttrDef.setPk(pk);
        domainAttrDef.setMandatory("N");
        domainAttrDef.setSortOrder(1l);
        domainAttrDef.setAttribute(attribute);
        return domainAttrDef;
    }

    public OrionDocumentDTO createDtoOrionDocument() {
        OrionDocumentDTO dto = new OrionDocumentDTO();
        dto.setId(-12345L);
        dto.setObjectType("O");
        dto.setName("567890");
        dto.setRevision("2");
        OrionAclStateDTO aclState = new OrionAclStateDTO();
        aclState.setId(42L);
        aclState.setName("INTERNAL");
        dto.setAclState(aclState);
        
        OrionAclDTO aclDTO = new OrionAclDTO();
        aclDTO.setId(-1001L);
        aclDTO.setName("ACL -1001");
        dto.setAcl(aclDTO);
        dto.setIndex1("index1");
        dto.setIndex4("index4");
        dto.setObjectDate(new Date());
        dto.setTitle("title");
        return dto;
    }

}

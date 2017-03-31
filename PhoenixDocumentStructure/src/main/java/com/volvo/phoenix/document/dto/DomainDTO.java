package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

public class DomainDTO implements Serializable {

    private static final long serialVersionUID = 8088427622792719691L;

    private Long id;
    private String name;
    private List<AttributeDefinitionDTO> attributes;
    //private List<DocumentTypeDTO> documentTypes;

//    public List<DocumentTypeDTO> getDocumentTypes() {
//        return documentTypes;
//    }
//
//    public void setDocumentTypes(List<DocumentTypeDTO> documentTypes) {
//        this.documentTypes = documentTypes;
//    }

    public List<AttributeDefinitionDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDefinitionDTO> attributes) {
        this.attributes = attributes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

public class DocumentTypeDTO implements Serializable {

    private static final long serialVersionUID = 3041360538753457306L;

    private Long id;
    private String name;
    private List<FamilyDTO> families;

    //private List<AttributeDefinitionDTO> attributes;

    //public List<AttributeDefinitionDTO> getAttributes() {
    //    return attributes;
   // }

    //public void setAttributes(List<AttributeDefinitionDTO> attributes) {
    //    this.attributes = attributes;
   // }

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

    public List<FamilyDTO> getFamilies() {
        return families;
    }

    public void setFamilies(List<FamilyDTO> families) {
        this.families = families;
    }

}

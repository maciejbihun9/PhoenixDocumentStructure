package com.volvo.phoenix.document.uploadtool.application.dto;

public class UploadToolDocumentAttributeValueDTO {

    private String label;
    private String value;
    private String type;
    private String name;
    private Long attrId;
    private String selectedId;
    private String selectedDictionary;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String attributeName) {
        this.label = attributeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAttrId() {
        return attrId;
    }

    public void setAttrId(Long id) {
        this.attrId = id;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public String getSelectedDictionary() {
        return selectedDictionary;
    }

    public void setSelectedDictionary(String selectedDictionary) {
        this.selectedDictionary = selectedDictionary;
    }
}

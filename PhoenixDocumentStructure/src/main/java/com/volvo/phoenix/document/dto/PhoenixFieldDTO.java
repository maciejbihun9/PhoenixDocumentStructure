package com.volvo.phoenix.document.dto;

/**
 * This bean contains information about dynamic metadata fields connected to a doc type
 */
public class PhoenixFieldDTO {

    private Long field_id;
    private String value = "";
    private String objectType;
    private Long indexId;

    public Long getFieldId() {
        return field_id;
    }

    public String getValue() {
        return value;
    }

    public void setFieldId(Long field_id) {
        this.field_id = field_id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Long getIndexId() {
        return indexId;
    }

    public void setIndexId(Long indexId) {
        this.indexId = indexId;
    }

}
package com.volvo.phoenix.document.dto;

import java.io.Serializable;

/**
 * Contains information about attributes assigned to the document. The attributes points to a domain. 
 * NOTE: attributes defined by the domain are treated as OPTIONAL. 
 */
public class DocumentAttributeDTO implements Serializable {

    private static final long serialVersionUID = 7113999251783574990L;
    private DocumentAttributePKDTO id;
    /*
     * Holds atribute value assigned to the document.
     * In case of SELECT attribute type the value store id related to the position in the dictionary.
     * Otherwise it contains the value not id to the value.
     */
    private String value;
    /*
     * Dictionary value is available only for SELECT attribute type.
     * Otherwise the dictionaryValue is equal null.
     */
    private String dictionaryValue;
    
    /*
     * Stores information about atribute itself.
     */
    private AttributeDefinitionDTO attribute;

    
    public DocumentAttributePKDTO getId() {
        return id;
    }

    public void setId(DocumentAttributePKDTO id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDictionaryValue() {
        return dictionaryValue;
    }

    public void setDictionaryValue(String dictionaryValue) {
        this.dictionaryValue = dictionaryValue;
    }

    public AttributeDefinitionDTO getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeDefinitionDTO attribute) {
        this.attribute = attribute;
    }

}

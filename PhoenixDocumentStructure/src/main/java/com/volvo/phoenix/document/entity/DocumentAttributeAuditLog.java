package com.volvo.phoenix.document.entity;

import java.io.Serializable;

/**
 * Represents audit trail document attribute and its value.
 * 
 * @author bpld313
 *
 */
public class DocumentAttributeAuditLog implements Serializable {

	private static final long serialVersionUID = 217568398051043589L;

	private String name;
	private String value;
	
	
	public DocumentAttributeAuditLog() {
	}
	
	public DocumentAttributeAuditLog(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DocumentAttributeAuditLog other = (DocumentAttributeAuditLog) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

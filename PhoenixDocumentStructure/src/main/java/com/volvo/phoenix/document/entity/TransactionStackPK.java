package com.volvo.phoenix.document.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TransactionStackPK implements Serializable {

    private static final long serialVersionUID = -4128392824535387972L;

    @Column(name = "OBJ_ID")
    private Long documentId;

    @Column(name = "EVENT_DATE")
    private Date eventDate;

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((documentId == null) ? 0 : documentId.hashCode());
        result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }    
        if (obj == null) {
            return false;
        }    
        if (getClass() != obj.getClass()) {
            return false;
        }   
        TransactionStackPK other = (TransactionStackPK) obj;
        if (documentId == null) {
            if (other.documentId != null) {
                return false;
            }    
        } else if (!documentId.equals(other.documentId)) {
            return false;
        }    
        if (eventDate == null) {
            if (other.eventDate != null) {
                return false;
            }    
        } else if (!eventDate.equals(other.eventDate)) {
            return false;
        }    
        return true;
    } 

}

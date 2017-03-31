package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

public class FamilyDTO implements Serializable {

    private static final long serialVersionUID = -1690709211006234635L;
    
    private Long id;
    private String name;
    private List<DocumentTypeDTO> documentTypes;
    
    
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
    
    public List<DocumentTypeDTO> getDocumentTypes() {
        return documentTypes;
    }
    
    public void setDocumentTypes(List<DocumentTypeDTO> documentTypes) {
        this.documentTypes = documentTypes;
    }
    /**
     * {@inheritDoc}
     */
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    /**
     * {@inheritDoc}
     */
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FamilyDTO)) {
            return false;
        }
        FamilyDTO other = (FamilyDTO) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    

}

package com.volvo.phoenix.document.dto;

import java.util.List;

import com.volvo.phoenix.document.datatype.ConflictLevel;
import com.volvo.phoenix.document.datatype.ConflictType;
import com.volvo.phoenix.document.entity.DocumentAttribute;

public class DocumentTypeConflictDTO extends ConflictDTO {

    private static final long serialVersionUID = -1919434404151027145L;

    private DocumentTypeDTO sourceDocumentTypeDTO;
    private DomainDTO targetDomainDTO;
    //private List<DocumentTypeDTO> targetDocumentTypeDTOs;
    private List<FamilyDTO> targetFamilies;
    private List<DocumentAttributeDTO> sourceDocumentTypeAttributes;
    //private String solution;
    //private List<DocumentAttributeDTO> attributes;
    //private DocumentTypeDTO newDoctype;
    //private boolean attributeSaved;

    public DocumentTypeConflictDTO(final OperationDTO operationDTO, final List<DocumentAttributeDTO> sourceDocumentTypeAttributes, final DocumentTypeDTO sourceDocumentTypeDTO, final DomainDTO targetDomainDTO, final List<FamilyDTO> targetFamilies) { // final List<DocumentTypeDTO> targetDocumentTypeDTOs) {
        this.setOperation(operationDTO);
        this.sourceDocumentTypeAttributes = sourceDocumentTypeAttributes;
        this.sourceDocumentTypeDTO = sourceDocumentTypeDTO;
        this.targetDomainDTO = targetDomainDTO;
        this.targetFamilies = targetFamilies;
    //    this.targetDocumentTypeDTOs = targetDocumentTypeDTOs;
    }

//    public DocumentTypeDTO getNewDoctype() {
//        return newDoctype;
//    }

//    public void setNewDoctype(DocumentTypeDTO newDoctype) {
//        this.newDoctype = newDoctype;
//    }

//    public boolean isAttributeSaved() {
//        return attributeSaved;
//    }
//
//    public void setAttributeSaved(boolean attributeSaved) {
//        this.attributeSaved = attributeSaved;
//    }
//
//    public String getSolution() {
//        return solution;
//    }
//
//    public void setSolution(String solution) {
//        this.solution = solution;
//    }

//    public List<DocumentAttributeDTO> getAttributes() {
//        return attributes;
//    }
//
//    public void setAttributes(List<DocumentAttributeDTO> attributes) {
//        this.attributes = attributes;
//    }

//    public List<DocumentTypeDTO> getDoctypes() {
//        return targetDocumentTypeDTOs;
//    }
//
//    public void setDoctypes(List<DocumentTypeDTO> doctypes) {
//        this.targetDocumentTypeDTOs = doctypes;
//    }
//
//    public DocumentTypeDTO getDoctype() {
//        return sourceDocumentTypeDTO;
//    }
//
//    public void setDoctype(DocumentTypeDTO doctype) {
//        this.sourceDocumentTypeDTO = doctype;
//    }
//
//    public DomainDTO getDomain() {
//        return targetDomainDTO;
//    }
//
//    public void setDomain(DomainDTO domain) {
//        this.targetDomainDTO = domain;
//    }


    public DocumentTypeDTO getSourceDocumentTypeDTO() {
        return sourceDocumentTypeDTO;
    }

    public void setSourceDocumentTypeDTO(DocumentTypeDTO sourceDocumentTypeDTO) {
        this.sourceDocumentTypeDTO = sourceDocumentTypeDTO;
    }

    public DomainDTO getTargetDomainDTO() {
        return targetDomainDTO;
    }

    public void setTargetDomainDTO(DomainDTO targetDomainDTO) {
        this.targetDomainDTO = targetDomainDTO;
    }

//    public List<DocumentTypeDTO> getTargetDocumentTypeDTOs() {
//        return targetDocumentTypeDTOs;
//    }
//
//    public void setTargetDocumentTypeDTOs(List<DocumentTypeDTO> targetDocumentTypeDTOs) {
//        this.targetDocumentTypeDTOs = targetDocumentTypeDTOs;
//    }

    @Override
    public ConflictType getType() {
        return ConflictType.TYPE;
    }

    @Override
    public ConflictLevel getLevel() {
        return ConflictLevel.ERROR;
    }

    public List<DocumentAttributeDTO> getSourceDocumentTypeAttributes() {
        return sourceDocumentTypeAttributes;
    }

    public void setSourceDocumentTypeAttributes(List<DocumentAttributeDTO> sourceDocumentTypeAttributes) {
        this.sourceDocumentTypeAttributes = sourceDocumentTypeAttributes;
    }

    public List<FamilyDTO> getTargetFamilies() {
        return targetFamilies;
    }

    public void setTargetFamilies(List<FamilyDTO> targetFamilies) {
        this.targetFamilies = targetFamilies;
    }

}

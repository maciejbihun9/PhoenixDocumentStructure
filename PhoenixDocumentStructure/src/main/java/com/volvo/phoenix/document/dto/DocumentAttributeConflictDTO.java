package com.volvo.phoenix.document.dto;

import java.util.List;

import com.volvo.phoenix.document.datatype.ConflictLevel;
import com.volvo.phoenix.document.datatype.ConflictType;

public class DocumentAttributeConflictDTO extends ConflictDTO {

    private static final long serialVersionUID = 1052133949584763139L;
    private List<AttributeDefinitionDTO> targetDomainAttributes;
    private List<DocumentAttributeDTO> sourceDocumentAttributes;
    private List<Long> lostSourceDocumentAttributes;

    public DocumentAttributeConflictDTO() {
    }

    public DocumentAttributeConflictDTO(final List<DocumentAttributeDTO> sourceDocumentAttributes, final List<AttributeDefinitionDTO> targetDomainAttributes,
            final List<Long> lostSourceDocumentAttributes) {
        super();
        this.sourceDocumentAttributes = sourceDocumentAttributes;
        this.targetDomainAttributes = targetDomainAttributes;
        this.lostSourceDocumentAttributes = lostSourceDocumentAttributes;
    }

    public DocumentAttributeConflictDTO(final List<DocumentAttributeDTO> sourceDocumentAttributes, final List<AttributeDefinitionDTO> targetDomainAttributes,
            final OperationDTO operationDTO, final List<Long> lostSourceDocumentAttributes) {
        super();
        this.sourceDocumentAttributes = sourceDocumentAttributes;
        this.targetDomainAttributes = targetDomainAttributes;
        this.lostSourceDocumentAttributes = lostSourceDocumentAttributes;
        setOperation(operationDTO);
    }

    public boolean targetDomainContainsSourceDocumentAttribute(final AttributeDefinitionDTO documentAttribute) {
        return targetDomainAttributes.contains(documentAttribute);
    }

    @Override
    public ConflictType getType() {
        return ConflictType.ATTRIBUTE;
    }

    @Override
    public ConflictLevel getLevel() {
        return ConflictLevel.INFO;
    }

    public List<AttributeDefinitionDTO> getTargetDomainAttributes() {
        return targetDomainAttributes;
    }

    public void setTargetDomainAttributes(List<AttributeDefinitionDTO> targetDomainAttributes) {
        this.targetDomainAttributes = targetDomainAttributes;
    }

    public List<DocumentAttributeDTO> getSourceDocumentAttributes() {
        return sourceDocumentAttributes;
    }

    public void setSourceDocumentAttributes(List<DocumentAttributeDTO> sourceDocumentAttributes) {
        this.sourceDocumentAttributes = sourceDocumentAttributes;
    }

    public List<Long> getLostSourceDocumentAttributes() {
        return lostSourceDocumentAttributes;
    }

    public void setLostSourceDocumentAttributes(List<Long> lostSourceDocumentAttributes) {
        this.lostSourceDocumentAttributes = lostSourceDocumentAttributes;
    }

}

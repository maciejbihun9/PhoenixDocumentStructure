package com.volvo.phoenix.document.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.volvo.phoenix.document.datatype.OperationType;

/**
 * Represents single entry in audit log for history of operations on document.
 * 
 * @author bpld313
 *
 */
@Entity
public class DocumentAuditLog extends AuditLog {

    private static final long serialVersionUID = 1L;
    public static final String ATTRIBUTES_SEPARATOR = "<ATTR-SEP>";
    public static final String ATTRIBUTE_VALUE_SEPARATOR = "=";

    @Column(name = "from_domain")
    private String sourceDomain;
    @Column(name = "from_doc_type")
    private String sourceDocType;
    @Column(name = "from_mandatory_attributes")
    private String sourceMandatoryAttributes;
    @Column(name = "from_optional_attributes")
    private String sourceOptionalAttributes;
    @Column(name = "to_domain")
    private String targetDomain;
    @Column(name = "to_doc_type")
    private String targetDocType;
    @Column(name = "to_mandatory_attributes")
    private String targetMandatoryAttributes;
    @Column(name = "to_optional_attributes")
    private String targetOptionalAttributes;

    public DocumentAuditLog() {
    }

    public DocumentAuditLog(Date when, String whoVcnId, String whoFullName, String sourcePath, String targetPath, OperationType operationType) {
        super(when, whoVcnId, whoFullName, sourcePath, targetPath, operationType);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((sourceDocType == null) ? 0 : sourceDocType.hashCode());
        result = prime * result + ((sourceDomain == null) ? 0 : sourceDomain.hashCode());
        result = prime * result + ((sourceMandatoryAttributes == null) ? 0 : sourceMandatoryAttributes.hashCode());
        result = prime * result + ((sourceOptionalAttributes == null) ? 0 : sourceOptionalAttributes.hashCode());
        result = prime * result + ((targetDocType == null) ? 0 : targetDocType.hashCode());
        result = prime * result + ((targetDomain == null) ? 0 : targetDomain.hashCode());
        result = prime * result + ((targetMandatoryAttributes == null) ? 0 : targetMandatoryAttributes.hashCode());
        result = prime * result + ((targetOptionalAttributes == null) ? 0 : targetOptionalAttributes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DocumentAuditLog other = (DocumentAuditLog) obj;
        if (sourceDocType == null) {
            if (other.sourceDocType != null) {
                return false;
            }
        } else if (!sourceDocType.equals(other.sourceDocType)) {
            return false;
        }
        if (sourceDomain == null) {
            if (other.sourceDomain != null) {
                return false;
            }
        } else if (!sourceDomain.equals(other.sourceDomain)) {
            return false;
        }
        if (sourceMandatoryAttributes == null) {
            if (other.sourceMandatoryAttributes != null) {
                return false;
            }
        } else if (!sourceMandatoryAttributes.equals(other.sourceMandatoryAttributes)) {
            return false;
        }
        if (sourceOptionalAttributes == null) {
            if (other.sourceOptionalAttributes != null) {
                return false;
            }
        } else if (!sourceOptionalAttributes.equals(other.sourceOptionalAttributes)) {
            return false;
        }
        if (targetDocType == null) {
            if (other.targetDocType != null) {
                return false;
            }
        } else if (!targetDocType.equals(other.targetDocType)) {
            return false;
        }
        if (targetDomain == null) {
            if (other.targetDomain != null) {
                return false;
            }
        } else if (!targetDomain.equals(other.targetDomain)) {
            return false;
        }
        if (targetMandatoryAttributes == null) {
            if (other.targetMandatoryAttributes != null) {
                return false;
            }
        } else if (!targetMandatoryAttributes.equals(other.targetMandatoryAttributes)) {
            return false;
        }
        if (targetOptionalAttributes == null) {
            if (other.targetOptionalAttributes != null) {
                return false;
            }
        } else if (!targetOptionalAttributes.equals(other.targetOptionalAttributes)) {
            return false;
        }
        return true;
    }

    public String getSourceDomain() {
        return sourceDomain;
    }

    public void setSourceDomain(String fromDomain) {
        this.sourceDomain = fromDomain;
    }

    public String getSourceDocType() {
        return sourceDocType;
    }

    public void setSourceDocType(String fromDocType) {
        this.sourceDocType = fromDocType;
    }

    public String getSourceMandatoryAttributes() {
        return sourceMandatoryAttributes;
    }

    public void setSourceMandatoryAttributes(String fromMandatoryAttributes) {
        this.sourceMandatoryAttributes = fromMandatoryAttributes;
    }

    public String getSourceOptionalAttributes() {
        return sourceOptionalAttributes;
    }

    public void setSourceOptionalAttributes(String fromOptionalAttributes) {
        this.sourceOptionalAttributes = fromOptionalAttributes;
    }

    public String getTargetDomain() {
        return targetDomain;
    }

    public void setTargetDomain(String toDomain) {
        this.targetDomain = toDomain;
    }

    public String getTargetDocType() {
        return targetDocType;
    }

    public void setTargetDocType(String toDocType) {
        this.targetDocType = toDocType;
    }

    public String getTargetMandatoryAttributes() {
        return targetMandatoryAttributes;
    }

    public void setTargetMandatoryAttributes(String toMandatoryAttributes) {
        this.targetMandatoryAttributes = toMandatoryAttributes;
    }

    public String getTargetOptionalAttributes() {
        return targetOptionalAttributes;
    }

    public void setTargetOptionalAttributes(String toOptionalAttributes) {
        this.targetOptionalAttributes = toOptionalAttributes;
    }

    private List<DocumentAttributeAuditLog> splitAttributes(final String attributes) {
        final List<DocumentAttributeAuditLog> attrs = Lists.newArrayList();

        if (StringUtils.isEmpty(attributes)) {
            return attrs;
        }

        final String[] attrList = attributes.split(ATTRIBUTES_SEPARATOR);
        for (final String attr : attrList) {
            if (attr.contains(ATTRIBUTE_VALUE_SEPARATOR)) {
                final String[] attrValue = StringUtils.split(attr.trim(), ATTRIBUTE_VALUE_SEPARATOR);
                if (attrValue.length == 2) {
                    attrs.add(new DocumentAttributeAuditLog(attrValue[0].trim(), attrValue[1].trim()));
                } else if (attrValue.length == 1) {
                    attrs.add(new DocumentAttributeAuditLog(attrValue[0].trim(), ""));
                }
            }
        }

        return attrs;
    }

    public List<DocumentAttributeAuditLog> getSourceMandatoryAttributesAsList() {
        return splitAttributes(sourceMandatoryAttributes);
    }

    public List<DocumentAttributeAuditLog> getSourceOptionalAttributesAsList() {
        return splitAttributes(sourceOptionalAttributes);
    }

    public List<DocumentAttributeAuditLog> getTargetMandatoryAttributesAsList() {
        return splitAttributes(targetMandatoryAttributes);
    }

    public List<DocumentAttributeAuditLog> getTargetOptionalAttributesAsList() {
        return splitAttributes(targetOptionalAttributes);
    }

}

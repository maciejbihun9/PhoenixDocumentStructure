package com.volvo.phoenix.document.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class contains metadata fields found in vt_phoenix_document, and represents metadata fields used for all types of documents in Phoenix.
 */
public class PhoenixDocumentDTO {

    private Long objectId;
    private String object_no;
    private String object_no_suffix;
    private String decision;
    private Long function_group;
    private String gate;
    private String product_class;
    private String brand;
    private String protectInWork;
    private String objectType;
    private Long indexId;
    private Long node_id;
    private Long domain_id;
    private Long doctype_id;
    private Long family_id;
    private String description;
    private String title;
    private String issuer;
    private String issuer_id;
    private Date issue_date;
    private String author;
    private String author_id;
    private String personal;
    private String notes;
    private String logbookmajorversion;
    private String tempstore;
    private String document_status;
    private String content_status;
    private String alt_doc_id;
    private String documentcontainer;
    private List<PhoenixFieldDTO> attributesValues = new ArrayList<PhoenixFieldDTO>();

    public String getdescription() {
        return description;
    }

    public String getissuer() {
        return issuer;
    }

    public String getissuer_id() {
        return issuer_id;
    }

    public String getauthor_id() {
        return author_id;
    }

    public String getauthor() {
        return author;
    }

    public List<PhoenixFieldDTO> getAttributeValues() {
        return attributesValues;
    }

    public Date getissue_date() {
        return issue_date;
    }

    public String getnotes() {
        return notes;
    }

    public String getpersonal() {
        return personal;
    }

    public String getobject_no() {
        return object_no;
    }

    public String getobject_no_suffix() {
        return object_no_suffix;
    }

    public String getdecision() {
        return decision;
    }

    public Long getfunction_group() {
        return function_group;
    }

    public String getgate() {
        return gate;
    }

    public String getproduct_class() {
        return product_class;
    }

    public String getlogbookmajorversion() {
        return logbookmajorversion;
    }

    public String gettempstore() {
        return tempstore;
    }

    public String getbrand() {
        return brand;
    }

    public String gettitle() {
        return title;
    }

    public String getdocument_status() {
        return document_status;
    }

    public String getcontent_status() {
        return content_status;
    }

    public Long getnode_id() {
        return node_id;
    }

    public Long getfamily_id() {
        return family_id;
    }

    public Long getdomain_id() {
        return domain_id;
    }

    public String getalt_doc_id() {
        return alt_doc_id;
    }

    public String getdocumentcontainer() {
        return documentcontainer;
    }

    public Long getdoctype_id() {
        return doctype_id;
    }

    public String getProtectInWork() {
        return protectInWork;
    }

    public void setObject_no(String object_no) {
        this.object_no = object_no;
    }

    public void setObject_no_suffix(String object_no_suffix) {
        this.object_no_suffix = object_no_suffix;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setFunction_group(Long function_group) {
        this.function_group = function_group;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public void setProduct_class(String product_class) {
        this.product_class = product_class;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setNode_id(Long node_id) {
        this.node_id = node_id;
    }

    public void setDomain_id(Long domain_id) {
        this.domain_id = domain_id;
    }

    public void setDoctype_id(Long doctype_id) {
        this.doctype_id = doctype_id;
    }

    public void setFamily_id(Long family_id) {
        this.family_id = family_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setIssuer_id(String issuer_id) {
        this.issuer_id = issuer_id;
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setLogbookmajorversion(String logbookmajorversion) {
        this.logbookmajorversion = logbookmajorversion;
    }

    public void setTempstore(String tempstore) {
        this.tempstore = tempstore;
    }

    public void setDocument_status(String document_status) {
        this.document_status = document_status;
    }

    public void setContent_status(String content_status) {
        this.content_status = content_status;
    }

    public void setAlt_doc_id(String alt_doc_id) {
        this.alt_doc_id = alt_doc_id;
    }

    public void setDocumentcontainer(String documentcontainer) {
        this.documentcontainer = documentcontainer;
    }

    public void setAttributesValues(List<PhoenixFieldDTO> attributesValues) {
        this.attributesValues = attributesValues;
    }

    public void setProtectInWork(String protectInWork) {
        this.protectInWork = protectInWork;
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

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

}

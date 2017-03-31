package com.volvo.phoenix.document.service;

/**
 * Service calculates and stores document modification events for document indexing with ESE (Eureka) engine and Solr
 */
public interface TransactionStackService {
    
    /**
     * Calculate and store events related to creation of a new document
     * 
     * @param documentId
     *            id of document
     */    
    void documentCreated(Long documentId);
    
    /**
     * Calculate and store events related to creation of a new version of existing document
     * 
     * @param documentId
     *            id of document
     */      
    void newVersionCreated(Long newVersionDocumentId, Long previousVersionDocumentId);

    /**
     * Calculate and store events related to copy of a document to new location
     * 
     * @param documentId
     *            id of document
     */
    void documentCopied(Long documentId);

    /**
     * Calculate and store events related to move of a document to new location
     * 
     * @param documentId
     *            id of document
     */
    void documentMoved(Long documentId);
 
}

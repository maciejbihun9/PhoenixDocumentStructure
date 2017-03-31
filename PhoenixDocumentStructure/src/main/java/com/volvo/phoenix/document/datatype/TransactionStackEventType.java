package com.volvo.phoenix.document.datatype;

/**
 * Represents type of operation to be performed on indexing system
 * 
 * A - add document to the index , U - update document, D - delete document from index
 */
public enum TransactionStackEventType {
    A, U, D
}

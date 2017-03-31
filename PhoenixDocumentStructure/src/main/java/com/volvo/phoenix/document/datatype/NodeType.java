package com.volvo.phoenix.document.datatype;

/**
 * Node type:
 * 
 * M master folder S slave folder D document
 */
public enum NodeType {
    M, S, D; // TODO check if we can replace enum values with full name - consider that this enum is mapped as string representation to existing table, which
             // possibly use letters shorcuts in other places!!!
}

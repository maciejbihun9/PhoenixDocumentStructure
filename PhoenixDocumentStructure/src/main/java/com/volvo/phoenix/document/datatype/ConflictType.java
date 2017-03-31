package com.volvo.phoenix.document.datatype;

/**
 * Enumeration of conflict types.
 */
public enum ConflictType {
    ATTRIBUTE, ROOT, APP, TYPE;

    public String getName() {
        return name();
    }
}

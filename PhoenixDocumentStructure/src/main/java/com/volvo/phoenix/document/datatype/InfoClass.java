package com.volvo.phoenix.document.datatype;


/**
 * Document security classification
 * 
 * @author bpl3195
 */
public enum InfoClass {
    
    OPEN (43, "Open"), 
    INTERNAL (42, "Internal"), 
    CONFIDENTIAL (41, "Confidential"), 
    STRICTLY_CONFIDENTIAL (40, "Strictly Confidential");

    private final int id;
    private final String name;
    
    InfoClass(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public static InfoClass getInfoClass(final String name) {
        for (final InfoClass state : values()) {
            if (state.name.equalsIgnoreCase(name)) {
                return state;
            }
        }
        return null;
    }
    
    public static InfoClass getInfoClass(final int id) {
        for (final InfoClass state : values()) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }
}

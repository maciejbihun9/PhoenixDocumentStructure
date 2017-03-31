package com.volvo.phoenix.document.datatype;

public enum MetadataStatus {

    METADATA_OK("There were no problems detected"),
    NO_METADATA("Do not constain metadata"),
    NO_TAB_CALLED_METADATA("Metadata tab do not exists"),
    BAD_EXCEL_VERSION("Excel can not be parsed");
    
    private final String description;
    
    private MetadataStatus(String description){
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
}

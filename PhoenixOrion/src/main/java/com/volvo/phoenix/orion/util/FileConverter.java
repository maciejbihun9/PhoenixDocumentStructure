package com.volvo.phoenix.orion.util;

import java.util.HashMap;


public class FileConverter {
    
    private HashMap<String, String> mediaTypes = new HashMap<String, String>();

    /**
     * Construct new <code>FileConverter</code> intance
     */
    public FileConverter(){
        mediaTypes.put("AVI","");
        mediaTypes.put("MPEG","");
        mediaTypes.put("MOV","");
        mediaTypes.put("WMF","");
        mediaTypes.put("SWF","");
        mediaTypes.put("WMA","");
        mediaTypes.put("WMV","");
    }
    
    /**
     * Determines Orion representation type based on file extension using 
     * <code>orionservlet.types</code> descriptor.
     * @param extension file extension which representation should be found for
     * @return Orion representation type
     */
    public String getTypeFromFileExt(String extension) {
        if (extension == null) {
            return "TEXT";
        }
        extension = extension.toLowerCase();
        DocumentType dt = DocumentTypes.getInstance().getDocumentTypeByExtension(extension);

        if (dt != null) {
            return dt.getTdmType();
        } else if (isMediaType(extension.toUpperCase())) {
            return "MPEG";
        } else {
            return "TEXT";
        }
    }
    
    private boolean isMediaType(String ext){
        return mediaTypes.containsKey(ext);
    }
}

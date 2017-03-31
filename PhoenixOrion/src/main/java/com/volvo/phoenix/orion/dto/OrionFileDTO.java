package com.volvo.phoenix.orion.dto;


public class OrionFileDTO {
   
	private String sheetName;
	private String aliasType;
	private String inputName;

	
    public String getSheetName() {
        return sheetName;
    }

    public String getAliasType() {
        return aliasType;
    }

    public String getInputName() {
        return inputName;
    }

    public void setSheetName(String sheet_name) {
        this.sheetName = sheet_name;
    }

    public void setAliasType(String alias_type) {
        this.aliasType = alias_type;
    }

    public void setInputName(String input_name) {
        this.inputName = input_name;
    }

}

package com.volvo.phoenix.orion.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * File detail
 * 
 */
@Entity
@Table(name = "DM_FILE")
public class OrionFile {

    @Id
    @Column(name="FILE_ID")
    private Long id;

    @Column(name="INPUT_NAME")
    private String inputName;

    @Column(name="FILE_SIZE")
    private Long fileSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

}

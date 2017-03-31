package com.volvo.phoenix.document.entity;

import javax.persistence.Entity;

import com.volvo.phoenix.document.datatype.ItemType;

/**
 * Represents operation on folder.
 */
@Entity
public class FolderOperation extends Operation {

    public Long getSourceFolderId() {
        return getSourceId();
    }

    public void setSourceFolderId(Long sourceFolderId) {
        setSourceId(sourceFolderId);
    }

    @Override
    public ItemType getSourceType() {
        return ItemType.FOLDER;
    }
}

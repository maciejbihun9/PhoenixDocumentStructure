package com.volvo.phoenix.document.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "VT_PHOENIX_ROOTNODE_STATES", schema = EntityMapping.PHOENIX_SCHEMA)
public class RootFolderState {

    @EmbeddedId
    RootFolderStatePK id;
    @Column(name = "LINK")
    private String link;
    @Column(name = "RANK")
    private String rank;
    @Column(name = "AVAIlABLE_IN_SEARCH")
    private String availableInSearch;

    public RootFolderStatePK getId() {
        return id;
    }

    public void setId(RootFolderStatePK id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAvailableInSearch() {
        return availableInSearch;
    }

    public void setAvailableInSearch(String availableInSearch) {
        this.availableInSearch = availableInSearch;
    }
}

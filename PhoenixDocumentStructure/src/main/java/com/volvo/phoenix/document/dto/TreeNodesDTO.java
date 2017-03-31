package com.volvo.phoenix.document.dto;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class TreeNodesDTO implements Serializable {

    private static final long serialVersionUID = -9156034877630728565L;

    private List<TreeNodeDTO> treeNodes = Lists.newArrayList();
    private long totalCount;

    public TreeNodesDTO() {
    }

    public TreeNodesDTO(final List<TreeNodeDTO> treeNodes, final long totalCount) {

        super();

        this.treeNodes = treeNodes;
        this.totalCount = totalCount;
    }

    /**
     * @return the treeNodes
     */
    public List<TreeNodeDTO> getTreeNodes() {
        return this.treeNodes;
    }

    /**
     * @param treeNodes
     *            the treeNodes to set
     */
    public void setTreeNodes(final List<TreeNodeDTO> treeNodes) {
        this.treeNodes = treeNodes;
    }

    /**
     * @return the totalCount
     */
    public long getTotalCount() {
        return this.totalCount;
    }

    /**
     * @param totalCount
     *            the totalCount to set
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

}

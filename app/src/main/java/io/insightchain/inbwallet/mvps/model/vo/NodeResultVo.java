package io.insightchain.inbwallet.mvps.model.vo;

import java.util.List;

public class NodeResultVo {

    private List<NodeVo> items;
    private int totalCount;
    private int totalPages;

    public List<NodeVo> getItems() {
        return items;
    }

    public void setItems(List<NodeVo> items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "NodeResultVo{" +
                "items=" + items +
                ", totalCount=" + totalCount +
                ", totalPages=" + totalPages +
                '}';
    }
}

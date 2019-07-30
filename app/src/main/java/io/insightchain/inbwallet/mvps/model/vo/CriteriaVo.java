package io.insightchain.inbwallet.mvps.model.vo;

public class CriteriaVo {

    /**
     * limit : 5
     * page : 1
     * block : null
     * hash : null
     * address : 0x1ae5dc22a80205cdfb9da7ed24e929174f9b95a0
     * offSet : 0
     */

    private int limit;
    private int page;
    private Object block;
    private Object hash;
    private String address;
    private int offSet;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Object getBlock() {
        return block;
    }

    public void setBlock(Object block) {
        this.block = block;
    }

    public Object getHash() {
        return hash;
    }

    public void setHash(Object hash) {
        this.hash = hash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    @Override
    public String toString() {
        return "CriteriaBean{" +
                "limit=" + limit +
                ", page=" + page +
                ", block=" + block +
                ", hash=" + hash +
                ", address='" + address + '\'' +
                ", offSet=" + offSet +
                '}';
    }
}

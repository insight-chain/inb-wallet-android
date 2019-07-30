package io.insightchain.inbwallet.mvps.model.vo;

import java.util.List;

public class TransactionRecordVo {

    /**
     * totalCount : 2
     * items : [{"blockNumber":"3876","timestamp":"2019-07-04 20:56:53","hash":"0xa575016a0d90519367324bf580d79d1c8defc96811270ee65f9e04c7d549df35","blockHash":"0x34853f25117ddf4b39b86816fbd32225a6830db30fe7e0a6ca87b2825c3fee2b","from":"0x1ae5dc22a80205cdfb9da7ed24e929174f9b95a0","to":"0x1000000000000000000000000000000000000000","amount":0.7744,"input":"mortgageNet","bindwith":"231","type":2,"direction":1,"status":"1"},{"blockNumber":"2192","timestamp":"2019-07-04 19:43:07","hash":"0x3ec4a85c23f14996d17b523fce05d36e86b6e4cc68a51d08e3aa74b8b9a61015","blockHash":"0x7b2880cda5c27375d6cad652f81bcccbbbeeca189393a7656c03199f4c384bf3","from":"0xdd19ce1c57f102b902809aa47619336f692410dd","to":"0x1ae5dc22a80205cdfb9da7ed24e929174f9b95a0","amount":7.8211,"input":"","bindwith":"440","type":1,"direction":2,"status":"1"}]
     * criteria : {"limit":5,"page":1,"block":null,"hash":null,"address":"0x1ae5dc22a80205cdfb9da7ed24e929174f9b95a0","offSet":0}
     * totalPages : 1
     */

    private int totalCount;
    private CriteriaVo criteria;
    private int totalPages;
    private List<TransactionResultVo> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public CriteriaVo getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaVo criteria) {
        this.criteria = criteria;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<TransactionResultVo> getItems() {
        return items;
    }

    public void setItems(List<TransactionResultVo> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "TransactionRecordVo{" +
                "totalCount=" + totalCount +
                ", criteria=" + criteria +
                ", totalPages=" + totalPages +
                ", items=" + items +
                '}';
    }
}

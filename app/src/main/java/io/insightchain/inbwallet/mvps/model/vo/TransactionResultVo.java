package io.insightchain.inbwallet.mvps.model.vo;

import java.io.Serializable;

public class TransactionResultVo implements Serializable {

    /**
     * blockNumber : 3876
     * timestamp : 2019-07-04 20:56:53
     * hash : 0xa575016a0d90519367324bf580d79d1c8defc96811270ee65f9e04c7d549df35
     * blockHash : 0x34853f25117ddf4b39b86816fbd32225a6830db30fe7e0a6ca87b2825c3fee2b
     * from : 0x1ae5dc22a80205cdfb9da7ed24e929174f9b95a0
     * to : 0x1000000000000000000000000000000000000000
     * amount : 0.7744
     * input : mortgageNet
     * bindwith : 231
     * type : 2
     * direction : 1
     * status : 1
     */

    private String blockNumber;
    private String timestamp;
    private String hash;
    private String blockHash;
    private String from;
    private String to;
    private Double amount;
    private String input;
    private String bindwith;
    private int type;//1.交易 2.抵押 3.解抵押 4.投票
    private int direction;
    private String status;

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getBindwith() {
        return bindwith;
    }

    public void setBindwith(String bindwith) {
        this.bindwith = bindwith;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TransactionResultVo{" +
                "blockNumber='" + blockNumber + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", hash='" + hash + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                ", input='" + input + '\'' +
                ", bindwith='" + bindwith + '\'' +
                ", type=" + type +
                ", direction=" + direction +
                ", status='" + status + '\'' +
                '}';
    }
}

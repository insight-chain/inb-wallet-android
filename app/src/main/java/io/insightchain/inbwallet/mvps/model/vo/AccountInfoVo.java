package io.insightchain.inbwallet.mvps.model.vo;

import java.math.BigInteger;

public class AccountInfoVo {


    /**
     * balance : 1.0000000002767919E14
     * used : 259383950
     * usableness : 0
     * mortgagte : 1442.1173
     * nonce : 904
     */

    private BigInteger balance;
    private int used;
    private int usableness;
    private String mortgagte;
    private BigInteger nonce;

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getUsableness() {
        return usableness;
    }

    public void setUsableness(int usableness) {
        this.usableness = usableness;
    }

    public String getMortgagte() {
        return mortgagte;
    }

    public void setMortgagte(String mortgagte) {
        this.mortgagte = mortgagte;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "balance=" + balance +
                ", used=" + used +
                ", usableness=" + usableness +
                ", mortgagte='" + mortgagte + '\'' +
                ", nonce=" + nonce +
                '}';
    }
}

package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;
import java.util.Objects;

public class AccountInfo {

    private BigInteger Balance;
    private String CodeHash;
    private BigInteger Nonce;
    private Resources Resources;
    private String Root;

    public AccountInfo() {

    }

    public AccountInfo(BigInteger balance, String codeHash, BigInteger nonce, org.web3j.protocol.core.methods.response.Resources resources, String root) {
        Balance = balance;
        CodeHash = codeHash;
        Nonce = nonce;
        Resources = resources;
        Root = root;
    }

    public BigInteger getBalance() {
        return Balance;
    }

    public void setBalance(BigInteger balance) {
        Balance = balance;
    }

    public String getCodeHash() {
        return CodeHash;
    }

    public void setCodeHash(String codeHash) {
        CodeHash = codeHash;
    }

    public BigInteger getNonce() {
        return Nonce;
    }

    public void setNonce(BigInteger nonce) {
        Nonce = nonce;
    }

    public org.web3j.protocol.core.methods.response.Resources getResources() {
        return Resources;
    }

    public void setResources(org.web3j.protocol.core.methods.response.Resources resources) {
        Resources = resources;
    }

    public String getRoot() {
        return Root;
    }

    public void setRoot(String root) {
        Root = root;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "Balance=" + Balance +
                ", CodeHash='" + CodeHash + '\'' +
                ", Nonce=" + Nonce +
                ", Resources=" + Resources +
                ", Root='" + Root + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountInfo that = (AccountInfo) o;
        return Objects.equals(Balance, that.Balance) &&
                Objects.equals(CodeHash, that.CodeHash) &&
                Objects.equals(Nonce, that.Nonce) &&
                Objects.equals(Resources, that.Resources) &&
                Objects.equals(Root, that.Root);
    }

}

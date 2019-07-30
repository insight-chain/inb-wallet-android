package org.web3j.crypto;

import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * Transaction class used for signing transactions locally.<br>
 * For the specification, refer to p4 of the <a href="http://gavwood.com/paper.pdf">
 * yellow paper</a>.
 */
public class RawTransaction {

    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private BigInteger value;
    private String data;
    //private Payment payment;
//    private String resourcePayer;
//    private  byte vp;
//    private  byte[] rp;
//    private  byte[] sp;

    protected RawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                           BigInteger value, String data) {
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.to = to;
        this.value = value;
        if (data != null) {
            this.data = Numeric.cleanHexPrefix(data);
        }
//        this.payment = new Payment();
//        this.resourcePayer = "";
//        this.vp = new Byte("0");
//        this.rp = new byte[]{};
//        this.sp = new byte[]{};
    }

    public static RawTransaction createContractTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value,
            String init) {

        return new RawTransaction(nonce, gasPrice, gasLimit, "", value, init);
    }

    public static RawTransaction createEtherTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value) {

        return new RawTransaction(nonce, gasPrice, gasLimit, to, value, "");

    }

    public static RawTransaction createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data) {
        return createTransaction(nonce, gasPrice, gasLimit, to, BigInteger.ZERO, data);
    }

    public static RawTransaction createTransaction(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value, String data) {

        return new RawTransaction(nonce, gasPrice, gasLimit, to, value, data);
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public String getTo() {
        return to;
    }

    public BigInteger getValue() {
        return value;
    }

    public String getData() {
        return data;
    }

//    public Payment getPayment() {
//        return payment;
//    }

//    public String getResourcePayer() {
//        return resourcePayer;
//    }
//
//    public byte getVp() {
//        return vp;
//    }
//
//    public byte[] getRp() {
//        return rp;
//    }
//
//    public byte[] getSp() {
//        return sp;
//    }
}

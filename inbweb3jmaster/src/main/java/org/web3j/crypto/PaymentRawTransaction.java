package org.web3j.crypto;

import java.math.BigInteger;
import java.security.SignatureException;

public class PaymentRawTransaction extends RawTransaction {

    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;

    private Payment payment;

    public PaymentRawTransaction(BigInteger nonce, BigInteger gasPrice,
                                 BigInteger gasLimit, String to, BigInteger value, String data,
                                 Payment payment) {
        super(nonce, gasPrice, gasLimit, to, value, data);
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    public static PaymentRawTransaction createTransaction2(
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
            BigInteger value, String data,Payment payment) {

        return new PaymentRawTransaction(nonce, gasPrice, gasLimit, to, value, data,payment);
    }
}

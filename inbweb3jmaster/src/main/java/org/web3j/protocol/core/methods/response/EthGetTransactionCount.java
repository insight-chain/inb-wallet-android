package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_getTransactionCount.
 */
public class EthGetTransactionCount extends Response<String> {
    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}

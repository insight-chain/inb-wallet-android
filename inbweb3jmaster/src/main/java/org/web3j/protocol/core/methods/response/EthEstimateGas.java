package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_estimateGas.
 */
public class EthEstimateGas extends Response<String> {
    public BigInteger getAmountUsed() {
        return Numeric.decodeQuantity(getResult());
    }
}

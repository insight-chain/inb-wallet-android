package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_hashrate.
 */
public class EthHashrate extends Response<String> {
    public BigInteger getHashrate() {
        return Numeric.decodeQuantity(getResult());
    }
}

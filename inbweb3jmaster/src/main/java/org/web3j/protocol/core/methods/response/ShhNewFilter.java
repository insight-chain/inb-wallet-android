package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * shh_newFilter.
 */
public class ShhNewFilter extends Response<String> {

    public BigInteger getFilterId() {
        return Numeric.decodeQuantity(getResult());
    }
}

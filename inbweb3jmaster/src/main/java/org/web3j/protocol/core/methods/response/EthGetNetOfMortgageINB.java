package org.web3j.protocol.core.methods.response;

import android.util.Log;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_getNetOfMortgageINB.
 */
public class EthGetNetOfMortgageINB extends Response<String> {
    public BigInteger getNetofMortgageINB() {
        String result = getResult();
        Log.e("EthGetNetOfMortgageINB","getResult()="+result);
        return Numeric.decodeQuantity(result);
    }
}

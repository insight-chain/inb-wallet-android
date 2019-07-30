package org.web3j.protocol.core.methods.response;

import android.util.Log;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_getNet.
 */
public class EthGetNet extends Response<String> {
    public BigInteger getNet() {
        String result = getResult();
        Log.e("EthGetNet","getResult()="+result);
        return Numeric.decodeQuantity(result);
    }
}

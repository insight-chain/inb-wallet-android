package org.web3j.ens;

import android.util.Log;

import org.web3j.tx.ChainId;

/**
 * ENS registry contract addresses.
 */
public class Contracts {

    public static final String MAINNET = "0x314159265dd8dbb310642f98f50c066173c1259b";
    public static final String ROPSTEN = "0x112234455c3a32fd11230c42e7bccd4a84e02010";
    public static final String RINKEBY = "0xe7410170f87102df0055eb195163a03b7f2bff4a";

    public static String resolveRegistryContract(String chainId) {
        Log.e("ChainId", "chainId = "+chainId);
        if(chainId.equalsIgnoreCase("891")){
            return "";
        }
        switch (Byte.valueOf(chainId)) {
            case ChainId.MAINNET:
                return MAINNET;
            case ChainId.ROPSTEN:
                return ROPSTEN;
            case ChainId.RINKEBY:
                return RINKEBY;

            default:
                throw new EnsResolutionException(
                        "Unable to resolve ENS registry contract for network id: " + chainId);
        }
    }
}

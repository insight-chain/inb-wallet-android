package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

import java.util.List;

/**
 * eth_accounts.
 */
public class EthAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}

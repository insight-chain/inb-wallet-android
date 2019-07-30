package org.web3j.protocol.admin.methods.response;

import org.web3j.protocol.core.Response;

import java.util.List;

/**
 * personal_listAccounts.
 */
public class PersonalListAccounts extends Response<List<String>> {
    public List<String> getAccountIds() {
        return getResult();
    }
}

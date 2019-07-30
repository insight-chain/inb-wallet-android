package org.web3j.protocol.core.methods.response;

import android.util.Log;

import org.web3j.protocol.core.Response;
import org.web3j.protocol.exceptions.TransactionException;

/**
 * eth_sendTransaction.
 */
public class EthSendTransaction extends Response<String>{
    public String getTransactionHash() throws TransactionException {
        if(getResult() == null){
            Log.e("aaa",getError().getMessage());
            if(getError()!=null && getError().getMessage()!=null && getError().getMessage().equals("over AuableNet number")){
                throw new TransactionException("交易失败，请先抵押资源！");
            }else
            throw new TransactionException("交易失败！");
        }
        return getResult();
    }
}

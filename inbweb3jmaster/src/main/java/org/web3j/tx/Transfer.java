package org.web3j.tx;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Class for performing Ether transactions on the Ethereum blockchain.
 */
public class Transfer extends ManagedTransaction {

    // This is the cost to send Ether between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);

    public Transfer(Web3j web3j, TransactionManager transactionManager) {
        super(web3j, transactionManager);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     *
     * @return the transaction receipt
     * @throws ExecutionException if the computation threw an
     *                            exception
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting
     * @throws TransactionException if the transaction was not mined while waiting
     */
    private TransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit)
            throws IOException, InterruptedException,
            TransactionException {

        BigInteger gasPrice = requestCurrentGasPrice();
        return send(toAddress, value, unit, gasPrice, GAS_LIMIT);
    }

    private TransactionReceipt send(
            String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice,
            BigInteger gasLimit) throws IOException, InterruptedException,
            TransactionException {

        BigDecimal weiValue = Convert.toWei(value, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: " + value + " " + unit.toString()
                            + " = " + weiValue + " Wei");
        }

        String resolvedAddress = ensResolver.resolve(toAddress);
        return send(resolvedAddress, "", weiValue.toBigIntegerExact(), gasPrice, gasLimit);
    }

    public static RemoteCall<TransactionReceipt> sendFunds(
            final Web3j web3j, final Credentials credentials,
            final String toAddress, final BigDecimal value, final Convert.Unit unit)
            throws InterruptedException,
            IOException, TransactionException {

        final TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

        return new RemoteCall<TransactionReceipt>(new Callable<TransactionReceipt>() {
            @Override
            public TransactionReceipt call() throws Exception {
                return new Transfer(web3j, transactionManager).send(toAddress, value, unit);
            }
        });
    }

    /**
     * Execute the provided function as a transaction asynchronously. This is intended for one-off
     * fund transfers. For multiple, create an instance.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     *
     * @return {@link RemoteCall} containing executing transaction
     */
    public RemoteCall<TransactionReceipt> sendFunds(
            final String toAddress, final BigDecimal value, final Convert.Unit unit) {
        return new RemoteCall<TransactionReceipt>(new Callable<TransactionReceipt>() {
            @Override
            public TransactionReceipt call() throws Exception {
                return Transfer.this.send(toAddress, value, unit);
            }
        });
    }

    public RemoteCall<TransactionReceipt> sendFunds(
            final String toAddress, final BigDecimal value, final Convert.Unit unit,
            final BigInteger gasPrice, final BigInteger gasLimit) {
        return new RemoteCall<TransactionReceipt>(new Callable<TransactionReceipt>() {
            @Override
            public TransactionReceipt call() throws Exception {
                return Transfer.this.send(toAddress, value, unit, gasPrice, gasLimit);
            }
        });
    }
}

package org.web3j.tx.gas;

import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.math.BigInteger;

public class DefaultGasProvider extends StaticGasProvider {
    public static final BigInteger GAS_LIMIT = Contract.GAS_LIMIT;
    public static final BigInteger GAS_PRICE = ManagedTransaction.GAS_PRICE;

    public DefaultGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }
}

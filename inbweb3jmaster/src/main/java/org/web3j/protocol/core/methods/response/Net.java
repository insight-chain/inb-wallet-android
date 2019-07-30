package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

public class Net {
    private BigInteger MortgagteINB;
    private BigInteger Usableness;
    private BigInteger Used;

    public Net() {

    }

    public Net(BigInteger mortgagteINB, BigInteger usableness, BigInteger used) {
        MortgagteINB = mortgagteINB;
        Usableness = usableness;
        Used = used;
    }

    public BigInteger getMortgagteINB() {
        return MortgagteINB;
    }

    public void setMortgagteINB(BigInteger mortgagteINB) {
        MortgagteINB = mortgagteINB;
    }

    public BigInteger getUsableness() {
        return Usableness;
    }

    public void setUsableness(BigInteger usableness) {
        Usableness = usableness;
    }

    public BigInteger getUsed() {
        return Used;
    }

    public void setUsed(BigInteger used) {
        Used = used;
    }

    @Override
    public String toString() {
        return "Net{" +
                "MortgagteINB=" + MortgagteINB +
                ", Usableness=" + Usableness +
                ", Used=" + Used +
                '}';
    }
}

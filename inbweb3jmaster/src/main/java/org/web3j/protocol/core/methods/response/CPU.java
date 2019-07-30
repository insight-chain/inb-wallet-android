package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

public class CPU {
    private BigInteger MortgagteINB;
    private BigInteger Usableness;
    private BigInteger Used;

    public CPU() {

    }

    public CPU(BigInteger mortgagteINB, BigInteger usableness, BigInteger used) {
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
        return "CPU{" +
                "MortgagteINB=" + MortgagteINB +
                ", Usableness=" + Usableness +
                ", Used=" + Used +
                '}';
    }
}

package io.insightchain.inbwallet.mvps.model.vo;

import java.math.BigInteger;

public class CPUResourceVo {

    private int totalNumber;
    private int currentNumber;
    private BigInteger mortgage;

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    public BigInteger getMortgage() {
        return mortgage;
    }

    public void setMortgage(BigInteger mortgage) {
        this.mortgage = mortgage;
    }
}

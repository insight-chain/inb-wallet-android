package org.web3j.protocol.core.methods.response;

public class Resources {
    private String Date;
    private Net NET;
    private CPU CPU;

    public Resources(){

    }

    public Resources(String date, Net NET, org.web3j.protocol.core.methods.response.CPU CPU) {
        Date = date;
        this.NET = NET;
        this.CPU = CPU;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Net getNET() {
        return NET;
    }

    public void setNET(Net NET) {
        this.NET = NET;
    }

    public org.web3j.protocol.core.methods.response.CPU getCPU() {
        return CPU;
    }

    public void setCPU(org.web3j.protocol.core.methods.response.CPU CPU) {
        this.CPU = CPU;
    }

    @Override
    public String toString() {
        return "Resources{" +
                "Date='" + Date + '\'' +
                ", NET=" + NET +
                ", CPU=" + CPU +
                '}';
    }
}

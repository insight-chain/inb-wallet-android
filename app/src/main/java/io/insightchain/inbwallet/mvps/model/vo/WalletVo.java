package io.insightchain.inbwallet.mvps.model.vo;
import io.insightchain.inbwallet.wallet.ETHWallet;

public class WalletVo {
    private ETHWallet wallet;
    private String walletName;
    private String balance;
    private CPUResourceVo cpuResourceVo;
    private String address;
    private long id;
    private String rmbNumber;
    private String netTotal;
    private String netCanUsed;
    private String mortgageInbNumber;
    private boolean isNote;
    private NodeVo nodeVo;

    public WalletVo(ETHWallet wallet){
        this.wallet = wallet;
        this.walletName = wallet.getName();
//        this.address = getChecksumAddress();
        this.address = wallet.getAddress();
        this.id = wallet.getId();
    }

    public long getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public ETHWallet getWallet() {
        return wallet;
    }

    public String getWalletName() {
        return walletName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRmbNumber() {
        return rmbNumber;
    }

    public void setRmbNumber(String rmbNumber) {
        this.rmbNumber = rmbNumber;
    }

    public CPUResourceVo getCpuResourceVo() {
        return cpuResourceVo;
    }

    public void setCpuResourceVo(CPUResourceVo cpuResourceVo) {
        this.cpuResourceVo = cpuResourceVo;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getNetCanUsed() {
        return netCanUsed;
    }

    public void setNetCanUsed(String netCanUsed) {
        this.netCanUsed = netCanUsed;
    }

    public String getMortgageInbNumber() {
        return mortgageInbNumber;
    }

    public void setMortgageInbNumber(String mortgageInbNumber) {
        this.mortgageInbNumber = mortgageInbNumber;
    }

    public boolean isNote() {
        return isNote;
    }

    public void setNote(boolean note) {
        isNote = note;
    }

    public NodeVo getNodeVo() {
        return nodeVo;
    }

    public void setNodeVo(NodeVo nodeVo) {
        this.nodeVo = nodeVo;
    }

    @Override
    public String toString() {
        return "WalletVo{" +
                "wallet=" + wallet +
                ", walletName='" + walletName + '\'' +
                ", balance='" + balance + '\'' +
                ", cpuResourceVo=" + cpuResourceVo +
                ", address='" + address + '\'' +
                ", id=" + id +
                ", rmbNumber='" + rmbNumber + '\'' +
                ", netTotal='" + netTotal + '\'' +
                ", netCanUsed='" + netCanUsed + '\'' +
                ", mortgageInbNumber='" + mortgageInbNumber + '\'' +
                ", isNote=" + isNote +
                ", nodeVo=" + nodeVo +
                '}';
    }
}

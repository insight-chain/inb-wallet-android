package io.insightchain.inbwallet.wallet;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 钱包bean对象
 * Created by dwq on 2018/3/20/020.
 * e-mail:lomapa@163.com
 */
@Entity
public class ETHWallet {
    @Id(autoincrement = true)
    private Long id;

    private String address;
    private String name;
    private String password;
    private String passwordHint;
    private String keystorePath;
    private String mnemonic;
    private boolean isCurrent;
    private boolean isBackup;
    private Long createTime;

    @Generated(hash = 1907724336)
    public ETHWallet(Long id, String address, String name, String password, String passwordHint,
            String keystorePath, String mnemonic, boolean isCurrent, boolean isBackup,
            Long createTime) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.password = password;
        this.passwordHint = passwordHint;
        this.keystorePath = keystorePath;
        this.mnemonic = mnemonic;
        this.isCurrent = isCurrent;
        this.isBackup = isBackup;
        this.createTime = createTime;
    }

    @Generated(hash = 1963897189)
    public ETHWallet() {
    }

//    @Generated(hash = 1331046581)
//    public ETHWallet(Long id, String address, String name, String password, String passwordHint,
//                     Long createTime, String keystorePath, String mnemonic, boolean isCurrent,
//                     boolean isBackup) {
//        this.id = id;
//        this.address = address;
//        this.name = name;
//        this.password = password;
//        this.passwordHint = passwordHint;
//        this.keystorePath = keystorePath;
//        this.mnemonic = mnemonic;
//        this.isCurrent = isCurrent;
//        this.isBackup = isBackup;
//        this.createTime = createTime;
////        this.createTime = System.currentTimeMillis() / 1000;
//    }
//
//    @Generated(hash = 1963897189)
//    public ETHWallet() {
//    }

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public boolean getIsCurrent() {
        return this.isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public boolean getIsBackup() {
        return this.isBackup;
    }

    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ETHWallet{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", passwordHint='" + passwordHint + '\'' +
                ", keystorePath='" + keystorePath + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", isCurrent=" + isCurrent +
                ", isBackup=" + isBackup +
                ", createTime=" + createTime +
                '}';
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

}

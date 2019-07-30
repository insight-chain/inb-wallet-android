package io.insightchain.inbwallet.wallet.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.wallet.ETHWallet;
import io.insightchain.wallettest.gen.ETHWalletDao;

/**
 * Created by dwq on 2018/3/21/021.
 * e-mail:lomapa@163.com
 */

public class WalletDaoUtils {
    public static ETHWalletDao ethWalletDao = BaseApplication.getsInstance().getDaoSession().getETHWalletDao();

    /**
     * 插入新创建钱包
     *
     * @param ethWallet 新创建钱包
     */
    public static void insertNewWallet(ETHWallet ethWallet) {
        updateCurrent(-1);
        ethWallet.setCurrent(true);
        ethWalletDao.insert(ethWallet);
    }

    /**
     * 更新选中钱包
     *
     * @param id 钱包ID
     */
    public static ETHWallet updateCurrent(long id) {
//        List<ETHWallet> ethWallets = ethWalletDao.loadAll();
        ETHWallet currentWallet = null;
        for (ETHWallet ethwallet : ethWallets) {
            if (id != -1 && ethwallet.getId() == id) {
                ethwallet.setCurrent(true);
                currentWallet = ethwallet;
            } else {
                ethwallet.setCurrent(false);
            }
            ethWalletDao.update(ethwallet);
        }
        return currentWallet;
    }

    /**
     * 获取当前钱包
     *
     * @return 钱包对象
     */
    public static ETHWallet getCurrent() {
        List<ETHWallet> ethWallets = ethWalletDao.loadAll();
        for (ETHWallet ethwallet : ethWallets) {
            if (ethwallet.isCurrent()) {
                ethwallet.setCurrent(true);
                return ethwallet;
            }
        }
        return null;
    }

    private static List<ETHWallet> ethWallets = new ArrayList<>();
    public static boolean hasWallet;

    /**
     * 查询所有钱包
     */
    public static List<ETHWallet> loadAll() {
        ethWallets = ethWalletDao.loadAll();
        if(ethWallets.size()!=0){
            hasWallet = true;
        }
        return ethWallets;
    }

    public static List<ETHWallet> getEthWallets() {
        return loadAll();
    }

    /**
     * 检查钱包名称是否存在
     *
     * @param name
     * @return
     */
    public static boolean walletNameChecking(String name) {
        for (ETHWallet ethWallet : ethWallets
                ) {
            if (TextUtils.equals(ethWallet.getName(), name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置isBackup为已备份
     *
     * @param walletId 钱包Id
     */
    public static void setIsBackup(long walletId) {
        ETHWallet ethWallet = ethWalletDao.load(walletId);
        ethWallet.setIsBackup(true);
        ethWalletDao.update(ethWallet);
    }

    /**
     * 以助记词检查钱包是否存在
     *
     * @param mnemonic
     * @return
     */
    public static boolean checkRepeatByMenmonic(String mnemonic) {
        for (ETHWallet ethWallet : ethWallets
                ) {
            if (TextUtils.equals(ethWallet.getMnemonic().trim(), mnemonic.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkRepeatByKeystore(String keystore) {
        return false;
    }

    /**
     * 修改钱包名称
     *
     * @param walletId
     * @param name
     */
    public static void updateWalletName(long walletId, String name) {
        ETHWallet wallet = ethWalletDao.load(walletId);
        wallet.setName(name);
        ethWalletDao.update(wallet);
    }

    public static void setCurrentAfterDelete() {
        List<ETHWallet> ethWallets = ethWalletDao.loadAll();
        if (ethWallets != null && ethWallets.size() > 0) {
            ETHWallet ethWallet = ethWallets.get(0);
            ethWallet.setCurrent(true);
            ethWalletDao.update(ethWallet);
        }
    }
}

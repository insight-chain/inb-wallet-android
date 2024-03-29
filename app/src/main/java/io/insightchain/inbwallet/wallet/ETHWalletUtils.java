package io.insightchain.inbwallet.wallet;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import io.insightchain.inbwallet.wallet.utils.AppFilePath;
import io.insightchain.inbwallet.wallet.utils.Md5Utils;
import io.insightchain.inbwallet.wallet.utils.WalletDaoUtils;


/**
 * 以太坊钱包创建工具类
 * Created by dwq on 2018/3/15/015.
 * e-mail:lomapa@163.com
 */

public class ETHWalletUtils {

    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    /**
     * 随机
     */
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();
    private Credentials credentials;
    /**
     * 通用的以太坊基于bip44协议的助记词路径 （imtoken jaxx Metamask myetherwallet）
     */
    public static String ETH_JAXX_TYPE = "m/44'/60'/0'/0/0";
    public static String ETH_LEDGER_TYPE = "m/44'/60'/0'/0";
    public static String ETH_CUSTOM_TYPE = "m/44'/60'/1'/0/0";

    private static String privateKey;

    /**
     * 创建助记词，并通过助记词创建钱包
     *
     * @param walletName
     * @param pwd
     * @return
     */
    public static ETHWallet generateMnemonic(String walletName, String pwd, String passwordHint) {
        String[] pathArray = ETH_JAXX_TYPE.split("/");
        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;

        DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase, creationTimeSeconds);
        return generateWalletByMnemonic(walletName, ds, pathArray, pwd, passwordHint);
    }

    /**
     * 通过导入助记词，导入钱包
     *
     * @param path 路径
     * @param list 助记词
     * @param pwd  密码
     * @return
     */
    public static ETHWallet importMnemonic(String walletName, String path, List<String> list, String pwd, String passwordHint) {
        if (!path.startsWith("m") && !path.startsWith("M")) {
            //参数非法
            return null;
        }
        String[] pathArray = path.split("/");
        if (pathArray.length <= 1) {
            //内容不对
            return null;
        }
        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;
        DeterministicSeed ds = new DeterministicSeed(list, null, passphrase, creationTimeSeconds);
        return generateWalletByMnemonic(TextUtils.isEmpty(walletName)?generateNewWalletName():walletName, ds, pathArray, pwd, passwordHint);
    }

    @NonNull
    private static String generateNewWalletName() {
        char letter1 = (char) (int) (Math.random() * 26 + 97);
        char letter2 = (char) (int) (Math.random() * 26 + 97);
        String walletName = String.valueOf(letter1) + String.valueOf(letter2) + "-新钱包";
        while (WalletDaoUtils.walletNameChecking(walletName)) {
            letter1 = (char) (int) (Math.random() * 26 + 97);
            letter2 = (char) (int) (Math.random() * 26 + 97);
            walletName = String.valueOf(letter1) + String.valueOf(letter2) + "-新钱包";
        }
        return walletName;
    }

    /**
     * @param walletName 钱包名称
     * @param ds         助记词加密种子
     * @param pathArray  助记词标准
     * @param pwd        密码
     * @return
     */
    @Nullable
    public static ETHWallet generateWalletByMnemonic(String walletName, DeterministicSeed ds,
                                                     String[] pathArray, String pwd, String passwordHint) {
        //种子
        byte[] seedBytes = ds.getSeedBytes();
        Log.e("ETHWalletUtils",Arrays.toString(seedBytes));
//        System.out.println(Arrays.toString(seedBytes));
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
//        System.out.println(Arrays.toString(mnemonic.toArray()));
        Log.e("ETHWalletUtils",Arrays.toString(mnemonic.toArray()));
        if (seedBytes == null)
            return null;
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0,
                        pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }

        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
        ETHWallet ethWallet = generateWallet(walletName, pwd, passwordHint,keyPair);
        if (ethWallet != null) {
            ethWallet.setMnemonic(convertMnemonicList(mnemonic));
        }
        return ethWallet;
    }

    private static String convertMnemonicList(List<String> mnemonics) {
        StringBuilder sb = new StringBuilder();
        for (String mnemonic : mnemonics
                ) {
            sb.append(mnemonic);
            sb.append(" ");
        }
        return sb.toString();
    }

    @Nullable
    private static ETHWallet generateWallet(String walletName, String pwd, String passwordHint, ECKeyPair ecKeyPair) {
        WalletFile walletFile;
        try {
            walletFile = Wallet.create(pwd, ecKeyPair, 1024, 1); // WalletUtils. .generateNewWalletFile();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//        BigInteger publicKey = ecKeyPair.getPublicKey();
//        BigInteger priKey = ecKeyPair.getPrivateKey();
//        String s = publicKey.toString();
        privateKey = Numeric.toHexStringNoPrefixZeroPadded(ecKeyPair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
//        Log.e("ETHWalletUtils", "publicKey = " + s);
//        Log.e("ETHWalletUtils", "priKey = " + priKey.toString());
//        Log.e("ETHWalletUtils", "privateKey = " + privateKey);
        String wallet_dir = AppFilePath.Wallet_DIR;
        Log.e("ETHWalletUtils", "wallet_dir = " + wallet_dir);
//        LogUtils.i("ETHWalletUtils", "wallet_dir = " + wallet_dir);
//        String keystorePath = "keystore_" + walletName + ".json";
        File destination = new File(wallet_dir, "keystore_" + walletName + ".json");

        //目录不存在则创建目录，创建不了则报错
        if (!createParentDir(destination)) {
            return null;
        }
        try {
            objectMapper.writeValue(destination, walletFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        ETHWallet ethWallet = new ETHWallet();
        ethWallet.setName(walletName);
        ethWallet.setAddress(Keys.toChecksumAddress(walletFile.getAddress()));
        ethWallet.setKeystorePath(destination.getAbsolutePath());
        ethWallet.setPassword(Md5Utils.md5(pwd));
        ethWallet.setPasswordHint(passwordHint);
        ethWallet.setCreateTime(System.currentTimeMillis() / 1000);
//        DateUtil.getUTCTime()
        return ethWallet;
    }

    /**
     * 通过keystore.json文件导入钱包
     *
     * @param keystore 原json文件
     * @param pwd      json文件密码
     * @return
     */
    public static ETHWallet loadWalletByKeystore(String keystore, String pwd, String passwordHint) {
        Credentials credentials = null;
        try {
            WalletFile walletFile = null;
            walletFile = objectMapper.readValue(keystore, WalletFile.class);

//            WalletFile walletFile = new Gson().fromJson(keystore, WalletFile.class);
            credentials = Credentials.create(Wallet.decrypt(pwd, walletFile));
        } catch (IOException e) {
            e.printStackTrace();
//            LogUtils.e("ETHWalletUtils", e.toString());
        } catch (CipherException e) {
//            LogUtils.e("ETHWalletUtils", e.toString());
//            ToastUtils.showToast(R.string.load_wallet_by_official_wallet_keystore_input_tip);
            e.printStackTrace();
        }
        if (credentials != null) {
            return generateWallet(generateNewWalletName(), pwd, passwordHint,credentials.getEcKeyPair());
        }
        return null;
    }

    /**
     * 通过明文私钥导入钱包
     *
     * @param privateKey
     * @param pwd
     * @return
     */
    public static ETHWallet loadWalletByPrivateKey(String walletName ,String privateKey, String pwd, String passwordHint) {
        Credentials credentials = null;
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        return generateWallet(TextUtils.isEmpty(walletName)?generateNewWalletName():walletName, pwd, passwordHint, ecKeyPair);
    }


    private static boolean createParentDir(File file) {
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            System.out.println("目标文件所在目录不存在，准备创建");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        return true;
    }

    /**
     * 修改钱包密码
     *
     * @param walletId
     * @param walletName
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public static ETHWallet modifyPassword(long walletId, String walletName, String oldPassword, String newPassword) {
        ETHWallet ethWallet = WalletDaoUtils.ethWalletDao.load(walletId);
        Credentials credentials = null;
        ECKeyPair keypair = null;
        try {
            credentials = WalletUtils.loadCredentials(oldPassword, ethWallet.getKeystorePath());
            keypair = credentials.getEcKeyPair();
            File destinationDirectory = new File(AppFilePath.Wallet_DIR, "keystore_" + walletName + ".json");
//            WalletUtils.generateWalletFile(newPassword, keypair, destinationDirectory, true);
            WalletFile walletFile = Wallet.create(newPassword, keypair, 1024, 1);
//            String fileName = getWalletFileName(walletFile);
//            File destination = new File(destinationDirectory, fileName);
            objectMapper.writeValue(destinationDirectory, walletFile);
            ethWallet.setPassword(Md5Utils.md5(newPassword));
            WalletDaoUtils.ethWalletDao.update(ethWallet);
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ethWallet;
    }

    /**
     * 导出明文私钥
     *
     * @param walletId 钱包Id
     * @param pwd      钱包密码
     * @return
     */
    public static String derivePrivateKey(long walletId, String pwd) {
        ETHWallet ethWallet = WalletDaoUtils.ethWalletDao.load(walletId);
        Credentials credentials;
        ECKeyPair keypair;
        String privateKey = null;
        try {
            credentials = WalletUtils.loadCredentials(pwd, ethWallet.getKeystorePath());
            keypair = credentials.getEcKeyPair();
            privateKey = Numeric.toHexStringNoPrefixZeroPadded(keypair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 导出keystore文件
     *
     * @param walletId
     * @param pwd
     * @return
     */
    public static String deriveKeystore(long walletId, String pwd) {
        ETHWallet ethWallet = WalletDaoUtils.ethWalletDao.load(walletId);
        String keystore = null;
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(new File(ethWallet.getKeystorePath()), WalletFile.class);
            keystore = objectMapper.writeValueAsString(walletFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keystore;
    }

    /**
     * 删除钱包
     *
     * @param walletId
     * @return
     */
    public static boolean deleteWallet(long walletId) {
        ETHWallet ethWallet = WalletDaoUtils.ethWalletDao.load(walletId);
        if (deleteFile(ethWallet.getKeystorePath())) {
            WalletDaoUtils.ethWalletDao.deleteByKey(walletId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
//                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
//                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
//            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    public static boolean verifyPassword(long walletId, String password){
        ETHWallet ethWallet = WalletDaoUtils.ethWalletDao.load(walletId);
        return ethWallet.getPassword().equals(Md5Utils.md5(password));
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void clear() {
        ETHWalletUtils.privateKey = null;
    }

    /**
     * 用于扫描WalletFile，并生成相应的Wallet
     */
//    public static void scanWallets(){
//        try {
//            String wallet_dir = AppFilePath.Wallet_DIR;
////        LogUtils.i("ETHWalletUtils", "wallet_dir = " + wallet_dir);
////        String keystorePath = "keystore_" + walletName + ".json";
//            File walletDir = new File(wallet_dir);
//            if (walletDir.exists() && walletDir.listFiles().length > 0) {
//                File[] files = walletDir.listFiles();
//                for(File file:files){
//                    WalletFile walletFile = objectMapper.readValue(file, WalletFile.class);
//
//                    keystoreMap.put(walletFile.getId(),walletFile);
//                    ETHWallet ethWallet = new ETHWallet();
//                    ethWallet.setName(walletName);
//                    ethWallet.setAddress(Keys.toChecksumAddress(walletFile.getAddress()));
//                    ethWallet.setKeystorePath(destination.getAbsolutePath());
//                    ethWallet.setPassword(Md5Utils.md5(pwd));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}

package io.insightchain.inbwallet.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by lijilong on 05/02.
 * Encypt utils
 */
public class AppEncryptUtils {
    private static final String PARAMETER_SIGN_KEY_NAME = "s";

    private static final String AES_IV = "BDC15E1365E4430A";
    private static final byte[] AES_IV_BYTES = AES_IV.getBytes();

    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCT+KUq1Dg7BHEF18R2vaKeh9HoJIYzX1OWl1StaiEVrjA4cTHnZHWMf6sBZO9nR+qsNTOBU6qp1JU5m6OjtxWQ3ZiQUbCCdT/Y4PMQX60GMzHBe5wds5FiBisCpjsHB8uzwXg8pm9hHoBwuwMyAlX6922QuTnzWlmHwGpTrnaypwIDAQAB";

    private static final byte[] PUBLIC_KEY_BYTES = Base64.decode(PUBLIC_KEY.getBytes(), Base64.NO_WRAP);

    /**
     * 利用HmacSHA1的摘要算法
     *
     * @param data 需要摘要的字符串
     * @param key
     * @return 摘要以后的bytes的hex字符串，小写字母
     */
    public static String hash(String data, String key) {
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key)) {
            return null;
        }
        try {
            return HexUtils.encodeHexStr(EncryptUtils.hash(data.getBytes("UTF-8"), key.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 利用AES加密信息
     *
     * @param data 需要加密的字符串
     * @param key  .bytes的长度必须为16
     * @return 加密以后的bytes的base64
     */
    public static String encryptByAES(String data, String key) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            return new String(Base64.encode(EncryptUtils.encryptByAES(data.getBytes("UTF-8"), key.getBytes("UTF-8"), AES_IV_BYTES), Base64.NO_WRAP), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 利用AES解密信息
     *
     * @param data 加密以后的bytes的base64
     * @param key  .bytes的长度必须为16
     * @return 解密以后的字符串
     */
    public static String decryptByAES(String data, String key) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            return new String(EncryptUtils.decryptByAES(Base64.decode(data.getBytes("UTF-8"), Base64.NO_WRAP), key.getBytes("UTF-8"), AES_IV_BYTES), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 利用RSA加密信息，data不能大于117个bytes
     *
     * @param data 需要加密的字符串
     * @return 加密以后的bytes的base64
     */
    public static String encryptByRSA(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            return new String(Base64.encode(EncryptUtils.encryptByRSA(data.getBytes("UTF-8"), PUBLIC_KEY_BYTES), Base64.NO_WRAP), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得数字签名，算法 = RSA(Hash(data, key))
     *
     * @param data 原值
     * @param key  hash密码
     * @return
     */
    public static String sign(String data, String key) {
        String signed = null;
        if (data != null && !TextUtils.isEmpty(key)) {
            signed = encryptByRSA(hash(data, key));
        }
        return signed;
    }

    /**
     * 检查某个应用是否存在
     * @param context
     * @param uri
     * @return
     */
    private boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}

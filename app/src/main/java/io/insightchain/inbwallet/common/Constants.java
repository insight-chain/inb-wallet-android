package io.insightchain.inbwallet.common;

public class Constants {

    public static String HOST_URL = "https://api-ropsten.etherscan.io/";//公链地址
    public static String HOST_INSIGHT_URL = "https://api-ropsten.etherscan.io/";//用于检查升级的地址
    public static String HOST_BROWSER_URL = "https://api-ropsten.etherscan.io/";//用于调用的浏览器后台地址

    //正式Json地址
    public static String Json_URL = "http://file.inbhome.com/insight/config/test.json";
    //请求Header
    public static String FLAVOR = "";

    public static final String AES_IV = "BDC15E1365E4430A";

    public static final String HEX_PREFIX = "0x";
    public static final int PERMISSION_WRITE_FILE_CODE = 2;
    public static final String PACKAGE_NAME = "WalletTest";
    public static final String IMAGE_FILE_HEADER = "WalletTest";
    public static final String UPDATE_APK_PATH = "update_apk_path";
    public static final int VOTE_MAX_NUMBER = 30;
    public static final String MORTGAGE_LIMIT = "100000";
}

package io.insightchain.inbwallet.common;

/**
 * Created by lijilong on 03/27.
 */

public class LocalData {
    private volatile static LocalData instance = null;

    private String token = null;
    private String etoken = null;
    private String key = null;
    private String ekey = null;
    private String serverKey = null;
    private String eserverKey = null;
    private boolean isLogin = false;
    //经度
    private Double currentLongitude;
    //维度
    private Double currentLatitude;

    private Long diffMills = 0L;//差异时间 = 系统时间 - 手机系统时间

    private boolean isNonomallKill;//判断是否被系统杀死，杀死重启app。 true---为非杀死
    private String wxCode;
    private boolean isLogout;//是否登出状态（默认 false  没有登出）

    private LocalData() {
    }

    public static LocalData getInstance() {
        if (instance == null) {
            synchronized (LocalData.class) {
                if (instance == null) {
                    instance = new LocalData();
                }
            }
        }
        return instance;
    }


}

package io.insightchain.inbwallet.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import java.util.ArrayList;
import java.util.List;

import io.insightchain.inbwallet.mvps.model.vo.WalletVo;
import io.insightchain.inbwallet.utils.SharedPreferencesUtils;
import io.insightchain.inbwallet.wallet.ETHWallet;
import io.insightchain.inbwallet.wallet.utils.AppFilePath;
import io.insightchain.inbwallet.wallet.utils.WalletDaoUtils;
import io.insightchain.wallettest.gen.DaoMaster;
import io.insightchain.wallettest.gen.DaoSession;


public class BaseApplication extends MultiDexApplication {

    public List<Activity> activityList = new ArrayList<>();
    public static BaseApplication instance;
    public static List<String> mnemonicWordList;
    private static long currentWalletID; //将其存在sharedPreference中，
    private static ETHWallet currentWallet;
    private static WalletVo currentWalletVo;

    private static SharedPreferences sp;

    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static BaseApplication getsInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //各种初始化，比如第三方等都放入init方法
        init();
        //注册activity生命周期监听回调
        registerActivityLifecycleCallbacks(lifeCallBacks);
        getConfig();
        initGreenDao();
    }

    private void init(){
        sp = SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "config");
        AppFilePath.init(this);
    }

    ActivityLifecycleCallbacks lifeCallBacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //检查是否要修改应用语言
//            CommonUtils.checkAppLanguage(activity);
            activityList.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityList.remove(activity);
        }
    };

    public void endApp() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public static List<String> getMnemonicWordList() {
        if(mnemonicWordList == null) {
            mnemonicWordList = new ArrayList<>();
            mnemonicWordList.add("tomorrow");
            mnemonicWordList.add("cupboard");
            mnemonicWordList.add("fine");
            mnemonicWordList.add("enforce");
            mnemonicWordList.add("umbrella");
            mnemonicWordList.add("weather");
            mnemonicWordList.add("describe");
            mnemonicWordList.add("surprise");
            mnemonicWordList.add("disagree");
            mnemonicWordList.add("achieve");
            mnemonicWordList.add("member");
            mnemonicWordList.add("victory");
        }
        return mnemonicWordList;
    }

    public static long getCurrentWalletID() {
        return currentWalletID;
    }

    public static void setCurrentWalletID(long currentWalletID) {
        BaseApplication.currentWalletID = currentWalletID;
        sp.edit().putLong("currentWalletID",currentWalletID).apply();
    }

    public static ETHWallet getCurrentWallet() {
        return currentWallet;
    }

    public static void setCurrentWallet(ETHWallet currentWallet) {
        BaseApplication.currentWallet = currentWallet;
        WalletDaoUtils.updateCurrent(currentWallet.getId());
        setCurrentWalletID(currentWallet.getId());
        setCurrentWalletVo(new WalletVo(currentWallet));
    }

    public static WalletVo getCurrentWalletVo() {
        return currentWalletVo;
    }

    public static void setCurrentWalletVo(WalletVo currentWalletVo) {
        BaseApplication.currentWalletVo = currentWalletVo;
    }

    private void getConfig(){
        currentWalletID = sp.getLong("currentWalletID",-1L);
    }

    private void initGreenDao() {
        //创建数据库表
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "wallet", null);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
    }
}

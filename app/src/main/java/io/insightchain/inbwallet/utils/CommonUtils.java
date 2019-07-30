package io.insightchain.inbwallet.utils;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.mvps.view.activity.MainActivity;

/**
 * Created by lijilong on 03/09.
 */

public class CommonUtils {

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null) {
            return null;
        }
        return info.getMacAddress();
    }

    public static String getBluetoothMac() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.getAddress();
//        return "123";
    }

    public static String getWifiMac(Context context) {
        String macSerial = getLocalMacAddress(context);
        if (!TextUtils.isEmpty(macSerial)) {
            return macSerial;
        }
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);


            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static String getAppFlavor(Application application) {
        String flavor = "Insight";
        try {
            PackageManager pm = application.getPackageManager();

            ApplicationInfo ai = pm.getApplicationInfo(
                    application.getPackageName(),
                    PackageManager.GET_META_DATA);

            if (ai != null) {
                flavor = ai.metaData.getString("flavor");
                if (flavor != null) {
                    Constants.FLAVOR = flavor;
                }
            }
        } catch (Exception e) {
            Log.e("getAppFloavor", "" + e.getMessage());
        }
        return flavor;
    }

    /**
     * 设置应用内语言
     *
     * @param context
     * @param language chineses zh Locale.SIMPLIFIED_CHINESE
     *                 english en Locale.ENGLISH
     *                 japanese ja Locale.JAPANESE
     *                 korean ko Locale.KOREAN
     *                 russian ru
     */
    public static void alterAppLanguage(Context context, String language) {
        if (context == null || TextUtils.isEmpty(language)) {
            return;
        }
        Locale Local_Russian = new Locale("RU", "ru", "");//自定义俄语
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(new Locale(language));
        Locale locale;
        switch (language) {
            case "zh":
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case "en":
                locale = Locale.ENGLISH;
                break;
            case "ja":
                locale = Locale.JAPANESE;
                break;
            case "ko":
                locale = Locale.KOREAN;
                break;
            case "ru":
                locale = Local_Russian;
                break;
            default:
                locale = Locale.SIMPLIFIED_CHINESE;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            Log.e("N", "language=" + config.getLocales().get(0).getLanguage());
        } else {
            config.locale = locale;
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            resources.updateConfiguration(config, displayMetrics);
            Log.e("<N", "language=" + config.locale.getLanguage());
        }
        Log.e("languageutils", "language=" + locale.getLanguage());
        SharedPreferences sp = SharedPreferencesUtils.getSharedPreferences(context, "config");
        sp.edit().putString("language", locale.getLanguage()).apply();
        Intent it = new Intent(context, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.e("languageUtils", "language=" + sp.getString("language", "--"));
        context.startActivity(it);

    }

    public static boolean checkAppLanguage(Context context) {
        SharedPreferences sp = SharedPreferencesUtils.getSharedPreferences(context, "config");
        String local = sp.getString("language", null);
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        Log.e("locale.language=", locale.getLanguage());
        if (locale.getLanguage().equals(local)) {
            return false;
        } else {
            alterAppLanguage(context, local);
            return true;
        }
    }

    //复制内容到剪切板
    public static void copyToClipboard(Context context, String msg) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建普通字符型ClipData
        ClipData clipData = ClipData.newPlainText(context.getResources().getString(R.string.wallet), msg);
        //将ClipData内容放到系统剪切板里
        cm.setPrimaryClip(clipData);
    }

    //从剪切板获取内容粘贴
    public static String getContentFromClipboard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        String resultString = "";
        // 检查剪贴板是否有内容
        if (!cm.hasPrimaryClip()) {
            return null;
        } else {
            ClipData clipData = cm.getPrimaryClip();
            int count = clipData.getItemCount();

            for (int i = 0; i < count; ++i) {

                ClipData.Item item = clipData.getItemAt(i);
                CharSequence str = item
                        .coerceToText(context);
                resultString += str;
            }

        }
        return resultString;
    }

    public static String getPlatformNameFromType(int type) {
        switch (type) {
            case 1:
                return "ETH";
            case 2:
                return "BTC";
            case 3:
                return "NEO";
            case 4:
                return "EOS";
            default:
                return "未知类型";
        }
    }

    public static Map<String, Object> removeNullObjectForMap(Map<String, Object> map) {
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Object value = map.get(key);
            if (value == null) {
                iterator.remove();
            }
        }
        return map;
    }

    /**
     * 检查权限
     *
     * @param permission
     * @param resultCode
     */
    public static boolean checkSelfPermission(Activity activity, String permission, int resultCode) {
        Log.e("rrt", "build.sdk= " + Build.VERSION.SDK + "," + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, resultCode);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * 检查权限
     *
     * @param permission
     * @param resultCode
     */
    public static boolean checkSelfPermissions(Activity activity, String[] permission, int resultCode) {
        Log.e("rrt", "build.sdk= " + Build.VERSION.SDK + "," + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionlist = new ArrayList<String>();
            for (int i = 0; i < permission.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionlist.add(permission[i]);
                }
            }
            if (!permissionlist.isEmpty()) {
                String[] permissionLast=permissionlist.toArray(new String[permissionlist.size()]);
                ActivityCompat.requestPermissions(activity,permissionLast, resultCode);
                return false;
            }
        }
        return true;
    }

    public static void hideSoftInputMethod(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //获取键盘显示状态
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        // 如果软键盘已经显示，则隐藏，反之则显示
        //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        // 隐藏软键盘
        //imm.hideSoftInputFromWindow(view, InputMethodManager.HIDE_NOT_ALWAYS);
        // 强制显示软键盘
        //imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
        // 强制隐藏软键盘
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Bitmap loadBitmapFromView(View v) {
        v.setDrawingCacheEnabled(true);
        v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        v.setDrawingCacheBackgroundColor(Color.WHITE);
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    public static boolean valideEmail(String str) {
//        String regex_email = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String regex_email = "[\\w\\.\\-\\_]+@([\\w\\-]+\\.)+[\\w\\-]+";
        return Pattern.matches(regex_email, str);
    }
}

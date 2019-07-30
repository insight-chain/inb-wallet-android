package io.insightchain.inbwallet.utils;

/**
 * Created by lijilong on 03/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 屏幕工具类，涉及到屏幕宽度、高度、密度比、(像素、dp、sp)之间的转换等。
 */
public class ScreenUtils {
    private static final String TAG = "ScreenUtils";
    public static float screenWidthDpi;
    public static float screenHeightDpi;
    public static float screenWidthPx;
    public static float screenHeightPx;
    public static float densityDpi;
    public static float density;

    private ScreenUtils(){}
    public static void initSettings(float screenWidthPx, float screenHeightPx,
                                    float density, DisplayMetrics displayMetrics) {
        ScreenUtils.screenWidthDpi = screenWidthPx / density;
        ScreenUtils.screenHeightDpi = screenHeightPx / density;
        ScreenUtils.screenWidthPx = screenWidthPx;
        ScreenUtils.screenHeightPx = screenHeightPx;
        ScreenUtils.densityDpi = displayMetrics.densityDpi;
        ScreenUtils.density = density;
    }

    /**
     * 获取屏幕宽度，单位为px
     * @param context   应用程序上下文
     * @return 屏幕宽度，单位px
     */
    public static int getScreenWidth(Context context){
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度，单位为px
     * @param context   应用程序上下文
     * @return 屏幕高度，单位px
     */
    public static int getScreenHeight(Context context){
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 获取系统dp尺寸密度值
     * @param context   应用程序上下文
     * @return
     */
    public static float getDensity(Context context){
        return getDisplayMetrics(context).density;
    }

    /**
     * 获取系统字体sp密度值
     * @param context   应用程序上下文
     * @return
     */
    public static float getScaledDensity(Context context){
        return getDisplayMetrics(context).scaledDensity;
    }

    /**
     * dip转换为px大小
     * @param context   应用程序上下文
     * @param dpValue   dp值
     * @return  转换后的px值
     */
    public static int dp2px(Context context, int dpValue){
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    /**
     * px转换为dp值
     * @param context   应用程序上下文
     * @param pxValue   px值
     * @return  转换后的dp值
     */
    public static int px2dp(Context context, int pxValue){
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    /**
     * sp转换为px
     * @param context   应用程序上下文
     * @param spValue   sp值
     * @return      转换后的px值
     */
    public static int sp2px(Context context, int spValue){
        return (int) (spValue * getScaledDensity(context) + 0.5f);
    }

    /**
     * px转换为sp
     * @param context   应用程序上下文
     * @param pxValue   px值
     * @return  转换后的sp值
     */
    public static int px2sp(Context context, int pxValue){
        return (int) (pxValue / getScaledDensity(context) + 0.5f);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context){
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height);
        decorView.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(bmp, 0, statusHeight, width, height - statusHeight);
        decorView.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取DisplayMetrics对象
     * @param context   应用程序上下文
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置状态栏颜色,状态栏占用空间
     *
     * @param activity
     * @param colorId  颜色资源id
     */
    public static void tryShowStatusBar(Activity activity, int colorId) {
        int color = ContextCompat.getColor(activity, colorId);
        //设置 paddingTop
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            //根布局添加占位状态栏
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, lp);
        }
    }

    /**
     * 获取是否存在NavigationBar
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }
    /**
     * 获取虚拟功能键高度
     * @param context
     * @return
     */
    public static int getVirtualBarHeigh(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

//    public static boolean isNavigationBarShow(Context context){
//        if(Constants.DEVICE_BRAND.contains("xiaomi") || OSUtils.isMIUI()){
//            if(Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0){//true 是手势，有虚拟键是 false
//                //开启手势，不显示虚拟键
//                return false;
//            }else{
//                return true;
//            }
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            Display display = windowManager.getDefaultDisplay();
//            Point size = new Point();
//            Point realSize = new Point();
//            display.getSize(size);
//            display.getRealSize(realSize);
//            boolean  result  = realSize.y!=size.y;
//            return realSize.y!=size.y;
//        }else {
//            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
//            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
//            if(menu || back) {
//                return false;
//            }else {
//                return true;
//            }
//        }
//    }

}

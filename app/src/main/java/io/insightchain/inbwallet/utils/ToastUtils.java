package io.insightchain.inbwallet.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lijilong on 04/19.
 */

public class ToastUtils {
    private static Toast toast;
    private static Context context;

    private ToastUtils() {
        throw new AssertionError();
    }

    public static void showToast(int resId) {
        showToast(context, context.getResources().getString(resId));
    }

    public static void showToast(int resId, int duration) {
        showToast(context, context.getResources().getString(resId), duration);
    }

    public static void showToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int duration, Object... args) {
        showToast(context, String.format(text, args), duration);
    }

    public static void showToast(Context context, CharSequence text, int duration) {

        if (toast == null) {
            toast = Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }
}

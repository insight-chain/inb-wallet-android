package io.insightchain.inbwallet.mvps.view.widget;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import io.insightchain.inbwallet.R;

public class CustomPopupWindow extends PopupWindow {

    private View view;
    public CustomPopupWindow(Context context, int layoutResource){
        view = LayoutInflater.from(context).inflate(layoutResource,null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置状态栏也包括
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(this, true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        setFocusable(false);
        setOutsideTouchable(true);
        //设置状态栏不包括，测试可以防止弹出框挡住底部虚拟键
        setClippingEnabled(true);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键，没什么用
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setAnimationStyle(R.style.alphIntoPopupAnim);
    }


    public void setSingleClickListener(int[] idResources, OnSingleClickListener listener){
        for(int idResource:idResources){
            View v = getContentView().findViewById(idResource);
            v.setOnClickListener(v1 -> {
                if(listener!=null) {
                    listener.onSingleClick(v1);
                }
            });
        }
    }

//    public void setCancleAble(boolean cancleAble){
//        if (cancleAble){
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CustomPopupWindow.this.dismiss();
//                }
//            });
//        }else{
//            view.setOnClickListener(null);
//        }
//    }

    public interface OnSingleClickListener{
        void onSingleClick(View v);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}

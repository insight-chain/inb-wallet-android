package io.insightchain.inbwallet.base.mvp;

import android.view.View;

/**
 * create by NiPengyuan
 * time:2019/3/27
 */
public interface BaseMvpView {
    /**
     * 显示loading框
     */
    void showLoading();

    /**
     * 隐藏loading框
     */
    void hideLoading();

    void toast(CharSequence s);

    void toast(int id);

    void toastLong(CharSequence s);

    void toastLong(int id);


    /**
     * 显示空数据布局
     */
    void showNullLayout();

    /**
     * 隐藏空数据布局
     */
    void hideNullLayout();

    /**
     * 显示异常布局
     * @param listener
     */
    void showErrorLayout(View.OnClickListener listener);

    void hideErrorLayout();

    void showServerError(int errorCode, String errorDesc);

    void showSuccess(boolean isSuccess);
}

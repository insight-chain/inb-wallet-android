package io.insightchain.inbwallet.mvps.presenter;

import io.insightchain.inbwallet.base.mvp.BaseMvpView;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;

public interface SettingView extends BaseMvpView {

    void showNewestToast();
    void showPopWindow(CustomPopupWindow popupWindow);
}

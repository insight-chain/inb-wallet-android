package io.insightchain.inbwallet.mvps.presenter;

import io.insightchain.inbwallet.base.mvp.BaseMvpView;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;

public interface PasswordPopView extends BaseMvpView {
    void showPasswordPopWindow(CustomPopupWindow popupWindow);
    void confirmClicked(String passwordText, int type);

}

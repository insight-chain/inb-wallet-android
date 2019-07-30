package io.insightchain.inbwallet.mvps.presenter;

import android.widget.EditText;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;

public class PasswordPopPresenter extends BasePresenter<PasswordPopView> {

    public void showPasswordPop(int type){
        CustomPopupWindow popupWindow = new CustomPopupWindow(mContext, R.layout.layout_popup_input_password);
        EditText passwordEditText = popupWindow.getView().findViewById(R.id.et_password_popup_input_password);
//                    popupWindow.setCancleAble(true);

        popupWindow.setSingleClickListener(new int[]{R.id.btn_confirm_popup_input_password, R.id.iv_close_popup},
                v -> {
                    switch (v.getId()){
                        case R.id.btn_confirm_popup_input_password:
                            mView.confirmClicked(passwordEditText.getText().toString(),type);
                            popupWindow.dismiss();
                            break;
                        case R.id.iv_close_popup:
                            popupWindow.dismiss();
                            break;
                    }
                });
        popupWindow.setFocusable(true);
        mView.showPasswordPopWindow(popupWindow);
    }

}

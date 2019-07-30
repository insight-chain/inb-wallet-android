package io.insightchain.inbwallet.mvps.view.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.presenter.DeleteWalletPresenter;
import io.insightchain.inbwallet.mvps.presenter.DeleteWalletView;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.CommonUtils;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = DeleteWalletPresenter.class)
public class WalletDetailActivity extends BaseMvpActivity implements DeleteWalletView {

    @BindView(R.id.topbar_wallet_datail_activity)
    TopBarCommon topBar;
    @BindView(R.id.tv_account_name)
    TextView walletNameText;
    @BindView(R.id.tv_wallet_address)
    TextView walletAddressText;
    @BindView(R.id.tv_create_time)
    TextView createTimeText;
    @BindView(R.id.root_view_activity_wallet_detail)
    View mRootView;

    @PresenterVariable
    DeleteWalletPresenter presenter;
    private CustomPopupWindow popupWindow;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    public void initView() {
        initTopBar();
//        Log.e(TAG,BaseApplication.getCurrentWallet());
        walletNameText.setText(BaseApplication.getCurrentWallet().getName());
        walletAddressText.setText(BaseApplication.getCurrentWalletVo().getAddress());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(BaseApplication.getCurrentWallet().getCreateTime() * 1000);
        createTimeText.setText(dateString);
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.wallet_detail_title));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
        topBar.top_bar_right_layout.setVisibility(View.VISIBLE);
        topBar.setRightView(null,R.mipmap.icon_delete);
    }

    @OnClick({R.id.top_bar_left_layout, R.id.ll_modify_wallet_password_layout,R.id.ll_export_private_key_layout,
            R.id.top_bar_right_layout, R.id.iv_copy_address})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
            case R.id.ll_modify_wallet_password_layout:
                Intent intent = new Intent(this,ModifyPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_export_private_key_layout:
                showPasswordPopWindow(0);
                break;
            case R.id.top_bar_right_layout:
                showPasswordPopWindow(1);
                break;
            case R.id.iv_copy_address:
                CommonUtils.copyToClipboard(this, walletAddressText.getText().toString());
                ToastUtils.showToast(context, "已复制到剪贴板");
                break;
            default:
                break;
        }
    }

    @Override
    public void sendEvent() {
        Log.e(TAG,"sendEvent");
        SimpleMessageEvent event = new SimpleMessageEvent();
        event.setTarget("MainActivity");
        event.setObj(0);
        EventBus.getDefault().post(event);
        startActivity(new Intent(context,MainActivity.class));
        finish();
    }

    @Override
    public void jumpBacksUp(String password) {
        Intent intent = new Intent(context,BacksUpWalletActivity.class);
        intent.putExtra("password",password);
        startActivity(intent);
    }

    @Override
    public void dismissPop() {
        if(popupWindow!=null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    private void showPasswordPopWindow(int type){
        //弹窗输入密码
//                if(popupWindow==null) {
        popupWindow = new CustomPopupWindow(context, R.layout.layout_popup_input_password);
        EditText passwordEditText = popupWindow.getView().findViewById(R.id.et_password_popup_input_password);
//                    popupWindow.setCancleAble(true);

        popupWindow.setSingleClickListener(new int[]{R.id.btn_confirm_popup_input_password, R.id.iv_close_popup},
                v -> {
                    switch (v.getId()){
                        case R.id.btn_confirm_popup_input_password:
                            if(TextUtils.isEmpty(passwordEditText.getText())){
                                ToastUtils.showToast(context, "请输入密码");
                                return;
                            }
                            presenter.verifyPassword(passwordEditText.getText().toString().trim(),type);

//                                showLoading();
//                                if(BaseApplication.getCurrentWallet().getKeystore().verifyPassword(passwordEditText.getText().toString().trim())){
//                                    Intent intent = new Intent(context,BacksUpWalletActivity.class);
//                                    intent.putExtra("password",passwordEditText.getText().toString().trim());
//                                    startActivity(intent);
//                                    hideLoading();
//                                }else{
//                                    ToastUtils.showToast(context, "密码不正确");
//                                }
                            break;
                        case R.id.iv_close_popup:
                            popupWindow.dismiss();
                            break;
                    }
                });
        popupWindow.setFocusable(true);
//                }
        popupWindow.showAtLocation(mRootView,Gravity.CENTER, 0, 0);
    }
}

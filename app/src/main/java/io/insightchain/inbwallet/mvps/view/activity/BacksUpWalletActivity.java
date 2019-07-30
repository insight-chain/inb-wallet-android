package io.insightchain.inbwallet.mvps.view.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.CommonUtils;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.ETHWallet;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BacksUpWalletActivity extends BaseMvpActivity {

    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    @BindView(R.id.tv_wallet_private_key)
    TextView privateKeyText;
    @BindView(R.id.root_view_activity_backs_up_wallet)
    View mRootView;
    @BindView(R.id.tv_account_name)
    TextView walletNameText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_backs_up_wallet;
    }

    @Override
    public void initView() {
        initTopBar();
        ETHWallet wallet = BaseApplication.getCurrentWallet();
        topBar.post(() -> {
            CustomPopupWindow popupWindow = new CustomPopupWindow(context,R.layout.layout_popup_not_shoot_screen);
            popupWindow.setSingleClickListener(new int[]{R.id.tv_I_know, R.id.iv_close_popup}, v -> {
                switch (v.getId()){
                    case R.id.tv_I_know:
                    case R.id.iv_close_popup:
                        popupWindow.dismiss();
                        break;
                }
            });
            popupWindow.showAtLocation(mRootView, Gravity.CENTER,0,0);
        });

        walletNameText.setText(wallet.getName());
        Observable.create((ObservableOnSubscribe<String>) e -> {
            String priKey = ETHWalletUtils.derivePrivateKey(wallet.getId(), getIntent().getStringExtra("password"));
            e.onNext(priKey);
            e.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(str -> privateKeyText.setText(str));
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.backs_up_wallet));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.top_bar_right_layout.setVisibility(View.INVISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
    }

    @OnClick({R.id.top_bar_left_layout, R.id.iv_copy_address})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
            case R.id.iv_copy_address:

                CommonUtils.copyToClipboard(this, privateKeyText.getText().toString());
                ToastUtils.showToast(context, "已复制到剪贴板");
                break;
        }
    }
}

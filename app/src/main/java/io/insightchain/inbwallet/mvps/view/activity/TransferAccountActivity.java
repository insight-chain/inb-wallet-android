package io.insightchain.inbwallet.mvps.view.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.presenter.TransferAccountPresenter;
import io.insightchain.inbwallet.mvps.presenter.TransferAccountView;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.qrcode.CaptureActivity;
import io.insightchain.inbwallet.qrcode.Intents;
import io.insightchain.inbwallet.utils.CommonUtils;
import io.insightchain.inbwallet.utils.PermissionsUtil;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;

@CreatePresenter(presenter = TransferAccountPresenter.class)
public class TransferAccountActivity extends BaseMvpActivity implements TextWatcher, TransferAccountView {

    @BindView(R.id.topbar_common)
    TopBarCommon topBar;

    @BindView(R.id.et_amount)
    EditText amountEditText;
    @BindView(R.id.tv_can_used_number)
    TextView canUsedNumberText;
    @BindView(R.id.et_receive_account)
    EditText acctoutEditText;
    @BindView(R.id.btn_send_transaction)
    TextView sendButton;
    @BindView(R.id.et_remarks_information)
    EditText remarkEditText;
    @BindView(R.id.root_view_activity_transfer_account)
    View mRootView;
    @BindView(R.id.iv_clear_remarks_information)
    ImageView clearImage;

    @PresenterVariable
    TransferAccountPresenter presenter;

    private CustomPopupWindow popupWindow;
    String balance = "";
    public static final int REQUEST_SCAN_QRCODE = 0X11;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_account;
    }

    @Override
    public void initView() {
        initTopBar();
        balance = BaseApplication.getCurrentWalletVo().getBalance();
        canUsedNumberText.setText(balance+" INB");
        acctoutEditText.addTextChangedListener(this);
        amountEditText.addTextChangedListener(this);
        remarkEditText.addTextChangedListener(this);
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.transfer_accounts_wallet_fragment));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.top_bar_right_layout.setVisibility(View.VISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
        topBar.setRightView(getString(R.string.receive_coins),0);
    }

    @OnClick({R.id.top_bar_left_layout, R.id.top_bar_right_layout, R.id.iv_scan_et_receive_account,
            R.id.iv_paste_et_receive_account, R.id.tv_all_et_amount, R.id.btn_send_transaction, R.id.iv_clear_remarks_information})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
            case R.id.top_bar_right_layout:
                startActivity(new Intent(context, ReceiveCoinsActivity.class));
                break;
            case R.id.iv_scan_et_receive_account:
                if (!PermissionsUtil.getInstance(context).hasPermission(Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PermissionsUtil.CAMERA_PERMISSION_CODE);
                }else {
                    Intent scanIntent = new Intent(context, CaptureActivity.class);
                    scanIntent.setAction(Intents.Scan.ACTION);
                    startActivityForResult(scanIntent, REQUEST_SCAN_QRCODE);
                }
//                    Intent scanIntent = new Intent(context, CaptureActivity.class);
//                    scanIntent.setAction(Intents.Scan.ACTION);
//                    startActivityForResult(scanIntent, REQUEST_SCAN_QRCODE);
//                }
                break;
            case R.id.iv_paste_et_receive_account:
                String pasteStr = CommonUtils.getContentFromClipboard(context);
                acctoutEditText.setText(pasteStr);
                acctoutEditText.setSelection(pasteStr.length());
                break;
            case R.id.tv_all_et_amount:
                amountEditText.setText(balance);
                amountEditText.setSelection(balance.length());
                break;
            case R.id.btn_send_transaction:
                if(new BigDecimal(amountEditText.getText().toString().trim()).compareTo(new BigDecimal(balance)) > 0 ){
                    ToastUtils.showToast(context, "没有那么多钱！");
                    return;
                }
                //TODO 显示密码输入框，获取密码
                //弹窗输入密码
//                if(popupWindow==null) {
                popupWindow = new CustomPopupWindow(context, R.layout.layout_popup_input_password);
                EditText passwordEditText = popupWindow.getView().findViewById(R.id.et_password_popup_input_password);
//                    popupWindow.setCancleAble(true);

                popupWindow.setSingleClickListener(new int[]{R.id.btn_confirm_popup_input_password, R.id.iv_close_popup},
                        new CustomPopupWindow.OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                switch (v.getId()){
                                    case R.id.btn_confirm_popup_input_password:
                                        if(TextUtils.isEmpty(passwordEditText.getText())){
                                            ToastUtils.showToast(context, "请输入密码");
                                            return;
                                        }
                                        showLoading();
                                        if(ETHWalletUtils.verifyPassword(BaseApplication.getCurrentWalletID(),passwordEditText.getText().toString().trim())){
//                                            Intent intent = new Intent(context,BacksUpWalletActivity.class);
//                                            intent.putExtra("password",passwordEditText.getText().toString().trim());
//                                            startActivity(intent);
                                            presenter.sendTransaction(acctoutEditText.getText().toString().trim(),
                                                    amountEditText.getText().toString().trim(),
                                                    passwordEditText.getText().toString().trim(),remarkEditText.getText().toString().trim());
//                                            presenter.sendRawTransaction(acctoutEditText.getText().toString().trim(),
//                                                    amountEditText.getText().toString().trim(),
//                                                    passwordEditText.getText().toString().trim(),remarkEditText.getText().toString().trim());
                                            popupWindow.dismiss();
                                        }else{
                                            ToastUtils.showToast(context, "密码不正确");
                                            hideLoading();
                                        }
                                        break;
                                    case R.id.iv_close_popup:
                                        popupWindow.dismiss();
                                        break;
                                }
                            }
                        });
                popupWindow.setFocusable(true);
//                }
                popupWindow.showAtLocation(mRootView,Gravity.CENTER, 0, 0);

                break;
            case R.id.iv_clear_remarks_information:
                remarkEditText.setText("");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            String result = data.getExtras().getString("codedContent");
            acctoutEditText.setText(result);  //显示扫描二维码得到的数据
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(amountEditText.getText()) && !TextUtils.isEmpty(acctoutEditText.getText())){
            sendButton.setEnabled(true);
        }else{
            sendButton.setEnabled(false);
        }
        if(TextUtils.isEmpty(remarkEditText.getText())){
            clearImage.setVisibility(View.GONE);
        }else{
            clearImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void sendEvent() {
        SimpleMessageEvent event = new SimpleMessageEvent();
        event.setTarget("MainActivity");
        event.setObj(0);
        EventBus.getDefault().post(event);
        startActivity(new Intent(context,MainActivity.class));
        finish();
    }
}

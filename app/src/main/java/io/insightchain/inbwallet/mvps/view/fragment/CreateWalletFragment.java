package io.insightchain.inbwallet.mvps.view.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.presenter.CreateWalletPresenter;
import io.insightchain.inbwallet.mvps.presenter.CreateWalletView;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = CreateWalletPresenter.class)
public class CreateWalletFragment extends BaseMvpFragment implements CreateWalletView,TextWatcher {

    @BindView(R.id.cb_agree_tips)
    CheckBox agreeCheckBox;
    @BindView(R.id.btn_create_wallet)
    TextView createWalletButton;

    @BindView(R.id.et_identity_name)
    EditText walletNameEditText;
    @BindView(R.id.et_wallet_password)
    EditText passwordEditText;
    @BindView(R.id.et_password_tip)
    EditText passwordHintEditText;
    @BindView(R.id.cb_see_password)
    CheckBox passwordHideCheckBox;
    @BindView(R.id.iv_clear)
    ImageView clearImage;

    @PresenterVariable
    CreateWalletPresenter createWalletPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_wallet;
    }

    @Override
    protected void init() {
        agreeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> createWalletButton.setEnabled(isChecked));

        passwordHideCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                //如果选中，显示密码
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                //否则隐藏密码
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            if(!TextUtils.isEmpty(passwordEditText.getText())) {
                passwordEditText.setSelection(passwordEditText.getText().toString().length());
            }
        });
        passwordHintEditText.addTextChangedListener(this);
    }

    public static CreateWalletFragment newInstance(){
//        CreateWalletFragment fragment = new CreateWalletFragment();
        return new CreateWalletFragment();
    }

    @OnClick({R.id.btn_create_wallet, R.id.iv_clear})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_create_wallet:
                if(TextUtils.isEmpty(walletNameEditText.getText())){
                    ToastUtils.showToast(mContext,"请输入钱包名称！");
                    return;
                }
                if(TextUtils.isEmpty(passwordEditText.getText())){
                    ToastUtils.showToast(mContext,"请输入密码！");
                    return;
                }
                createWalletPresenter.createWallet(walletNameEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim(),passwordHintEditText.getText().toString().trim());
                break;
            case R.id.iv_clear:
                passwordHintEditText.setText("");
                break;
        }
    }

    @Override
    public void sendEvent() {
        SimpleMessageEvent event = new SimpleMessageEvent();
        event.setTarget("CreateWalletActivity");
        event.setObj(0);
        EventBus.getDefault().post(event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(TextUtils.isEmpty(passwordHintEditText.getText())){
            clearImage.setVisibility(View.GONE);
        }else{
            clearImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

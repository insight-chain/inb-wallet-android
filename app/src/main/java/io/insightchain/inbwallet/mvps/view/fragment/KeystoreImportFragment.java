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
import io.insightchain.inbwallet.mvps.presenter.ImportWalletPresenter;
import io.insightchain.inbwallet.mvps.presenter.ImportWalletView;
import io.insightchain.inbwallet.mvps.view.activity.CreateWalletActivity;
import io.insightchain.inbwallet.mvps.view.activity.MainActivity;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = ImportWalletPresenter.class)
public class KeystoreImportFragment extends BaseMvpFragment implements ImportWalletView {

    @BindView(R.id.cb_see_password_mnemonic_word)
    CheckBox passwordHideCheckBox;
    @BindView(R.id.cb_agree_tips_mnemonic_word)
    CheckBox agreeCheckBox;
    @BindView(R.id.et_wallet_password_mnemonic_word)
    EditText walletPasswordEditText;
    @BindView(R.id.et_password_tip_mnemonic_word)
    EditText passwordTipEditText;
    @BindView(R.id.et_mnemonic_word)
    EditText mnemonicWordEditText;
    @BindView(R.id.btn_import_wallet_mnemonic_word)
    TextView importButton;
    @BindView(R.id.iv_clear_mnemonic_word)
    ImageView clearImage;
    @BindView(R.id.et_identity_name)
    EditText walletNameEditText;

    @PresenterVariable
    ImportWalletPresenter importWalletPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mnemonic_word_import;
    }

    @Override
    protected void init() {
        setListenter();
    }

    public static KeystoreImportFragment newInstance(){
//        MnemonicWordImportFragment fragment = new MnemonicWordImportFragment();
        return new KeystoreImportFragment();
    }

    private void setListenter(){
        passwordHideCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                //如果选中，显示密码
                walletPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                //否则隐藏密码
                walletPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            if(!TextUtils.isEmpty(walletPasswordEditText.getText())) {
                walletPasswordEditText.setSelection(walletPasswordEditText.getText().toString().length());
            }
        });
        agreeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                importButton.setEnabled(true);
            }else{
                importButton.setEnabled(false);
            }
        });
        passwordTipEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(passwordTipEditText.getText())){
                    clearImage.setVisibility(View.GONE);
                }else{
                    clearImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_clear_mnemonic_word, R.id.btn_import_wallet_mnemonic_word, R.id.btn_create_wallet_mnemonic_word, R.id.tv_linked_tips_mnemonic})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_clear_mnemonic_word:
                passwordTipEditText.setText("");
                break;
            case R.id.btn_create_wallet_mnemonic_word:
                startActivity(CreateWalletActivity.class);
                break;
            case R.id.btn_import_wallet_mnemonic_word:
                if(TextUtils.isEmpty(mnemonicWordEditText.getText())){
                    ToastUtils.showToast(mContext,"请输入助记次");
                    return;
                }
                if(TextUtils.isEmpty(walletPasswordEditText.getText())){
                    ToastUtils.showToast(mContext,"请输入密码");
                    return;
                }
                if(TextUtils.isEmpty(walletNameEditText.getText())){
                    ToastUtils.showToast(mContext,"请输入钱包名称");
                    return;
                }

                importWalletPresenter.importWalletByMnemonic(walletNameEditText.getText().toString(),
                        mnemonicWordEditText.getText().toString(),
                        walletPasswordEditText.getText().toString(),
                        passwordTipEditText.getText().toString());
                break;
            case R.id.tv_linked_tips_mnemonic:
                break;
            default:
                break;
        }
    }

    @Override
    public void sendEvent() {
        SimpleMessageEvent event = new SimpleMessageEvent();
        event.setTarget("MainActivity");
        event.setObj(0);
        EventBus.getDefault().post(event);
        startActivityFinish(MainActivity.class);
    }
}

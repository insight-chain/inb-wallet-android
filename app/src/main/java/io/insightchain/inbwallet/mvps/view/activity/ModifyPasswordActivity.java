package io.insightchain.inbwallet.mvps.view.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.presenter.ModifyPasswordPresenter;
import io.insightchain.inbwallet.mvps.presenter.ModifyPasswordView;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = ModifyPasswordPresenter.class)
public class ModifyPasswordActivity extends BaseMvpActivity implements ModifyPasswordView,
        CompoundButton.OnCheckedChangeListener,TextWatcher {

    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    @BindView(R.id.et_old_password)
    EditText oldPasswordEditText;
    @BindView(R.id.et_new_password)
    EditText newPasswordEditText;
    @BindView(R.id.et_confirm_new_password)
    EditText confirmPasswordEditText;
    @BindView(R.id.cb_old_password)
    CheckBox oldPasswordCheckBox;
    @BindView(R.id.cb_new_password)
    CheckBox newPasswordCheckBox;
    @BindView(R.id.cb_confirm_new_password)
    CheckBox confirmPasswordCheckBox;
    @BindView(R.id.btn_done_activity_modify_password)
    TextView doneButton;
    @PresenterVariable
    ModifyPasswordPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    public void initView() {
        initTopBar();
        setListener();
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.modify_password));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
    }

    @OnClick({R.id.top_bar_left_layout, R.id.btn_done_activity_modify_password})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
            case R.id.btn_done_activity_modify_password:
                //判断新密码和重复的是否一致
                if(newPasswordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
                    //修改密码
                    presenter.modifyPassword(oldPasswordEditText.getText().toString(), newPasswordEditText.getText().toString());
                }else{
                    ToastUtils.showToast(context, "新密码两次输入不一致！");
                }

                break;
        }
    }

    @Override
    public void modifySuccess() {
        ToastUtils.showToast(context, "修改密码成功！");
        SimpleMessageEvent event = new SimpleMessageEvent();
        event.setTarget("MainActivity");
        event.setObj(0);
        EventBus.getDefault().post(event);
        startActivity(new Intent(context,MainActivity.class));
        finish();
    }

    @Override
    public void passwordWrong() {
        ToastUtils.showToast(context, "原密码错误！");
    }

    private void setListener(){
        newPasswordEditText.addTextChangedListener(this);
        oldPasswordEditText.addTextChangedListener(this);
        confirmPasswordEditText.addTextChangedListener(this);
        oldPasswordCheckBox.setOnCheckedChangeListener(this);
        newPasswordCheckBox.setOnCheckedChangeListener(this);
        confirmPasswordCheckBox.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        newPasswordCheckBox.setChecked(isChecked);
        oldPasswordCheckBox.setChecked(isChecked);
        confirmPasswordCheckBox.setChecked(isChecked);
        if(isChecked){
            //如果选中，显示密码
            oldPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            newPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            confirmPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            //否则隐藏密码
            oldPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            newPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            confirmPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        if(!TextUtils.isEmpty(oldPasswordEditText.getText())) {
            oldPasswordEditText.setSelection(oldPasswordEditText.getText().toString().length());
        }
        if(!TextUtils.isEmpty(newPasswordEditText.getText())) {
            newPasswordEditText.setSelection(newPasswordEditText.getText().toString().length());
        }
        if(!TextUtils.isEmpty(confirmPasswordEditText.getText())) {
            confirmPasswordEditText.setSelection(confirmPasswordEditText.getText().toString().length());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if( !TextUtils.isEmpty(oldPasswordEditText.getText()) && !TextUtils.isEmpty(newPasswordEditText.getText())
                && !TextUtils.isEmpty(confirmPasswordEditText.getText())){
            doneButton.setEnabled(true);
        }else{
            doneButton.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }



}

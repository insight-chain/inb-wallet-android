package io.insightchain.inbwallet.mvps.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.presenter.PasswordPopPresenter;
import io.insightchain.inbwallet.mvps.presenter.PasswordPopView;
import io.insightchain.inbwallet.mvps.presenter.ResourcePresenter;
import io.insightchain.inbwallet.mvps.presenter.ResourceView;
import io.insightchain.inbwallet.mvps.presenter.VerifyPasswordPresenter;
import io.insightchain.inbwallet.mvps.presenter.VerifyPasswordView;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = {ResourcePresenter.class, PasswordPopPresenter.class, VerifyPasswordPresenter.class})
public class ResourceActivity extends BaseMvpActivity implements TextWatcher,ResourceView,VerifyPasswordView,PasswordPopView {

    @BindView(R.id.rg_tab)
    RadioGroup radioGroup;
    @BindView(R.id.rb_mortgage)
    RadioButton mortgageRadioButton;
    @BindView(R.id.rb_redeem)
    RadioButton redeemRadioButton;
    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    @BindView(R.id.ll_net_redeem_layout)
    LinearLayout netRedeemLayout;
    @BindView(R.id.ll_net_mortgage_layout)
    LinearLayout netMortgageLayout;
    @BindView(R.id.iv_clear_net_mortgage)
    ImageView clearNetMortgageImage;
    @BindView(R.id.iv_clear_net_redeem)
    ImageView clearNetRedeemImage;
    @BindView(R.id.et_net_mortgage)
    EditText netMortgageEditText;
    @BindView(R.id.et_net_redeem)
    EditText netRedeemEditText;
    @BindView(R.id.tv_left_net_number)
    TextView leftNetText;
    @BindView(R.id.tv_total_net_number)
    TextView totalNetText;
    @BindView(R.id.tv_mortgage_number)
    TextView mortgageNumberText;
    @BindView(R.id.tv_can_redeem_number)
    TextView canRedeemNumberText;

    @BindView(R.id.root_view_activity_resource)
    View mRootView;

    @PresenterVariable
    ResourcePresenter presenter;
    @PresenterVariable
    PasswordPopPresenter passwordPopPresenter;
    @PresenterVariable
    VerifyPasswordPresenter verifyPasswordPresenter;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_resource;
    }

    @Override
    public void initView() {
        initTopBar();
        leftNetText.setText(BaseApplication.getCurrentWalletVo().getNetCanUsed());
        totalNetText.setText(BaseApplication.getCurrentWalletVo().getNetTotal());
        mortgageNumberText.setText(BaseApplication.getCurrentWalletVo().getMortgageInbNumber()+"INB");
        canRedeemNumberText.setText(BaseApplication.getCurrentWalletVo().getMortgageInbNumber()+"INB");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_bottom_line_blue);
            drawable.setBounds(0,0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
            switch (checkedId){
                case R.id.rb_mortgage:
                    mortgageRadioButton.setCompoundDrawables(null,null,null, drawable);
                    redeemRadioButton.setCompoundDrawables(null,null,null,null);
                    netMortgageLayout.setVisibility(View.VISIBLE);
                    netRedeemLayout.setVisibility(View.GONE);
                    if(imm!=null)
                    imm.hideSoftInputFromWindow(netRedeemEditText.getWindowToken(), 0); //强制隐藏键盘
                    break;
                case R.id.rb_redeem:
                    redeemRadioButton.setCompoundDrawables(null,null,null, drawable);
                    mortgageRadioButton.setCompoundDrawables(null,null,null,null);
                    netMortgageLayout.setVisibility(View.GONE);
                    netRedeemLayout.setVisibility(View.VISIBLE);
                    if(imm!=null)
                    imm.hideSoftInputFromWindow(netMortgageEditText.getWindowToken(), 0); //强制隐藏键盘
                    break;
            }
        });
        netMortgageEditText.addTextChangedListener(this);
        netRedeemEditText.addTextChangedListener(this);
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.resource));
        topBar.setLeftView(null, R.mipmap.icon_back_arrow);
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.top_bar_left_layout, R.id.iv_clear_net_mortgage, R.id.iv_clear_net_redeem
            ,R.id.tv_confirm_cpu_mortgage, R.id.tv_confirm_cpu_redeem})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
            case R.id.iv_clear_net_mortgage:
                netMortgageEditText.setText("");
                break;
            case R.id.iv_clear_net_redeem:
                netRedeemEditText.setText("");
                break;
            case R.id.tv_confirm_cpu_mortgage:
                passwordPopPresenter.showPasswordPop(0);
//                presenter.confirmClick(0,netMortgageEditText.getText().toString());
                break;
            case R.id.tv_confirm_cpu_redeem:
                passwordPopPresenter.showPasswordPop(1);
//                presenter.confirmClick(1,netRedeemEditText.getText().toString());
                break;
            default:
                break;
        }
    }

//    @Override
//    public void showPopWindow(CustomPopupWindow popupWindow){
//        popupWindow.showAtLocation(mRootView,Gravity.CENTER, 0, 0);
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(TextUtils.isEmpty(netMortgageEditText.getText())){
            clearNetMortgageImage.setVisibility(View.GONE);
        }else{
            clearNetMortgageImage.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(netRedeemEditText.getText())){
            clearNetRedeemImage.setVisibility(View.GONE);
        }else{
            clearNetRedeemImage.setVisibility(View.VISIBLE);
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

    @Override
    public void passwordRight(String password, int type) {
        if(type == 0) {
            presenter.mortgageNet(netMortgageEditText.getText().toString(), password);
        }else if(type == 1){
            presenter.unMortgageNet(netRedeemEditText.getText().toString(), password);
        }
    }

    @Override
    public void passwordWrong() {
        ToastUtils.showToast(context, "密码不正确");
    }

    @Override
    public void passwordEmpty() {
        ToastUtils.showToast(context, "请输入密码");
    }

    @Override
    public void showPasswordPopWindow(CustomPopupWindow popupWindow) {
        popupWindow.showAtLocation(mRootView,Gravity.CENTER, 0, 0);
    }

    @Override
    public void confirmClicked(String passwordText, int type) {
        verifyPasswordPresenter.verifyPassword(passwordText,type);
    }
}

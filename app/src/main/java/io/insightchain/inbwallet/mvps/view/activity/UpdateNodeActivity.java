package io.insightchain.inbwallet.mvps.view.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;
import io.insightchain.inbwallet.mvps.presenter.PasswordPopPresenter;
import io.insightchain.inbwallet.mvps.presenter.PasswordPopView;
import io.insightchain.inbwallet.mvps.presenter.SuperNodePresenter;
import io.insightchain.inbwallet.mvps.presenter.SuperNodeView;
import io.insightchain.inbwallet.mvps.presenter.VerifyPasswordPresenter;
import io.insightchain.inbwallet.mvps.presenter.VerifyPasswordView;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = {SuperNodePresenter.class, VerifyPasswordPresenter.class, PasswordPopPresenter.class})
public class UpdateNodeActivity extends BaseMvpActivity implements SuperNodeView,VerifyPasswordView, PasswordPopView {

    @BindView(R.id.et_head_img_url)
    EditText headImageUrlEditText;
    @BindView(R.id.et_node_name)
    EditText nodeNameEditText;
    @BindView(R.id.et_node_intro)
    EditText nodeIntroEditText;
    @BindView(R.id.et_node_ip_address)
    EditText IPAddressEditText;
    @BindView(R.id.et_node_port)
    EditText portEditText;
    @BindView(R.id.et_we_chat_number)
    EditText wechatNumberEditText;
    @BindView(R.id.et_email_address)
    EditText emailEditText;
    @BindView(R.id.et_city)
    EditText cityEditText;
    @BindView(R.id.et_country)
    EditText countryEditText;

    @BindView(R.id.root_view_activity_update_node)
    View mRootView;
    @BindView(R.id.topbar_common)
    TopBarCommon topBar;

    private String IPAddress, portNumber, nodeName, headImageUrl, nodeIntro, emailAddress, city, country, homePage;

    @PresenterVariable
    SuperNodePresenter superNodePresenter;
    @PresenterVariable
    VerifyPasswordPresenter verifyPasswordPresenter;
    @PresenterVariable
    PasswordPopPresenter passwordPopPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_node;
    }

    @Override
    public void initView() {
        initTopBar();
        if(BaseApplication.getCurrentWalletVo().isNote()){
            NodeVo nodeVo = BaseApplication.getCurrentWalletVo().getNodeVo();
            headImageUrlEditText.setText(nodeVo.getImage()!=null ? nodeVo.getImage() : "");
            nodeNameEditText.setText(nodeVo.getName()!=null ? nodeVo.getName() : "");
            nodeIntroEditText.setText(nodeVo.getIntro()!=null ? nodeVo.getIntro() : "");
            IPAddressEditText.setText(nodeVo.getHost()!=null ? nodeVo.getHost() : "");
            portEditText.setText(nodeVo.getPort()!=null ? nodeVo.getPort() : "");
            emailEditText.setText(nodeVo.getEmail()!=null ? nodeVo.getEmail() : "");
            cityEditText.setText(nodeVo.getCity()!=null ? nodeVo.getCity() : "");
            countryEditText.setText(nodeVo.getCountry()!=null ? nodeVo.getCountry() : "");
        }
    }

    @OnClick({R.id.btn_update_node ,R.id.top_bar_left_layout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_update_node:

                verifyEditText();
                passwordPopPresenter.showPasswordPop(0);

                break;
            case R.id.top_bar_left_layout:
                finish();
                break;
        }

    }

    private void verifyEditText(){
        nodeName = TextUtils.isEmpty(nodeNameEditText.getText()) ? "" : nodeNameEditText.getText().toString().trim();
        IPAddress = TextUtils.isEmpty(IPAddressEditText.getText()) ? "" : IPAddressEditText.getText().toString().trim();
        portNumber = TextUtils.isEmpty(portEditText.getText()) ? "" : portEditText.getText().toString().trim();
        headImageUrl = TextUtils.isEmpty(headImageUrlEditText.getText()) ? "" : headImageUrlEditText.getText().toString().trim();
        nodeIntro = TextUtils.isEmpty(nodeIntroEditText.getText()) ? "" : nodeIntroEditText.getText().toString().trim();
        emailAddress = TextUtils.isEmpty(emailEditText.getText()) ? "" : emailEditText.getText().toString().trim();
        city = TextUtils.isEmpty(cityEditText.getText()) ? "" : cityEditText.getText().toString().trim();
        country = TextUtils.isEmpty(countryEditText.getText()) ? "" : countryEditText.getText().toString().trim();
        homePage = TextUtils.isEmpty(wechatNumberEditText.getText()) ? "" : wechatNumberEditText.getText().toString().trim();
    }

    @Override
    public void showPasswordPopWindow(CustomPopupWindow popupWindow) {
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void confirmClicked(String passwordText, int type) {
        verifyPasswordPresenter.verifyPassword(passwordText, type);
    }

    @Override
    public void passwordRight(String password, int type) {
        String content = "inb|1|event|declare|e486016a2a5f701464252f6c9edabc4ef47f5ebe20bc6682c8d91f96300867a827155bea289de308273c6b763dad7bbdef5dd0df32829b049597a37210c2deb9" +
                "~" + IPAddress +
                "~" + portNumber +
                "~" + nodeName +
                "~" + city +
                "~" + country +
                "~" + headImageUrl +
                "~" + homePage +
                "~" + emailAddress +
                "~" + "intro/" + nodeIntro;
        Log.e(TAG,content);
        superNodePresenter.registerSuperNode(content, password);
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
    public void showSuccessRegist() {
        ToastUtils.showToast(context, "注册成功！");
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.update_note_information));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
    }
}

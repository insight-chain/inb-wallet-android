package io.insightchain.inbwallet.mvps.view.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.presenter.SettingPresenter;
import io.insightchain.inbwallet.mvps.presenter.SettingView;
import io.insightchain.inbwallet.mvps.view.activity.AboutUsActivity;
import io.insightchain.inbwallet.mvps.view.activity.SystemSettingActivity;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.APKVersionCodeUtils;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = SettingPresenter.class)
public class SettingFragment extends BaseMvpFragment implements SettingView {
    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    @BindView(R.id.tv_version_code)
    TextView versionText;

    @PresenterVariable
    SettingPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void init() {
        initTopBar();

        versionText.setText(APKVersionCodeUtils.getVerName(mContext));


    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.setting));
        topBar.top_bar_left_layout.setVisibility(View.INVISIBLE);
        topBar.top_bar_right_layout.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.ll_about_us_layout, R.id.ll_system_setting_layout, R.id.ll_check_update_layout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_about_us_layout:
                startActivity(AboutUsActivity.class);
                break;
            case R.id.ll_system_setting_layout:
                startActivity(SystemSettingActivity.class);
                break;
            case R.id.ll_check_update_layout:

                //TODO 在点击检查更新的时候通过接口获取版本号，和versionCode做对比判断是否需要升级，如果需要升级，弹出对话框，如果不需要，弹出Toast
                presenter.checkVersion();
                break;
        }
    }

    @Override
    public void showNewestToast() {
        ToastUtils.showToast(mContext, "当前已是最新版本");
    }

    @Override
    public void showPopWindow(CustomPopupWindow popupWindow) {
        popupWindow.showAtLocation(mRootView, Gravity.CENTER,0,0);
    }

}

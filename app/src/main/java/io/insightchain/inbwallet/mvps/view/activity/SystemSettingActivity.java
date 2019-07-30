package io.insightchain.inbwallet.mvps.view.activity;

import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;

public class SystemSettingActivity extends BaseMvpActivity {
    @BindView(R.id.topbar_common)
    TopBarCommon topBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_setting;
    }

    @Override
    public void initView() {
        initTopBar();
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.system_setting));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.setLeftView(null, R.mipmap.icon_back_arrow);
        topBar.top_bar_right_layout.setVisibility(View.GONE);
    }

    @OnClick({R.id.top_bar_left_layout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
        }
    }
}

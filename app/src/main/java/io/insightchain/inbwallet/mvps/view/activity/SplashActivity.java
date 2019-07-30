package io.insightchain.inbwallet.mvps.view.activity;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import butterknife.BindView;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.presenter.SplashPresenter;
import io.insightchain.inbwallet.mvps.presenter.SplashView;
import io.insightchain.inbwallet.utils.ScreenUtils;

@CreatePresenter(presenter = SplashPresenter.class)
public class SplashActivity extends BaseMvpActivity implements SplashView {

    @BindView(R.id.root_view_activity_splash)
    View mRootView;

    @PresenterVariable
    SplashPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        initConstants();
        Log.e(TAG,"presenter.getUrl()");
        presenter.getUrl();

    }

    private void initConstants(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        ScreenUtils.initSettings(dm.widthPixels, dm.heightPixels, this.getResources().getDisplayMetrics().density, dm);
    }

    @Override
    public void jumpMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

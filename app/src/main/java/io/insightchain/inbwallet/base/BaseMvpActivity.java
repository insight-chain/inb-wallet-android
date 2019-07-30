package io.insightchain.inbwallet.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.mvp.BaseMvpView;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterDispatch;
import io.insightchain.inbwallet.base.mvp.PresenterProviders;
import io.insightchain.inbwallet.utils.ScreenUtils;

/**
 * create by NiPengyuan
 * time:2019/3/27
 */

public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements BaseMvpView {

    private PresenterProviders mPresenterProviders;
    private PresenterDispatch mPresenterDispatch;
    private Activity activity;
    protected Context context = this;
    protected ProgressDialog progress;
    protected boolean hasBus = false;
    protected View noDataStubView;
    protected View sysErrStubView;
    protected Unbinder unbinder;
    protected boolean isBelowStatus = true;
    /**
     * TAG，动态生成类名
     */
    protected String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        TAG = this.getClass().getSimpleName();
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);

        mPresenterDispatch.attachView(this, this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
        if (isBelowStatus ) {
            ScreenUtils.tryShowStatusBar(this, R.color.colorPrimary);
        }
        activity = this;
        unbinder = ButterKnife.bind(activity);
        setStatusBarFullTransparent();
        beforeInitView();
        initView();
        afterInitView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDispatch.onSaveInstanceState(outState);
    }

    protected abstract int getLayoutId();
    public abstract void initView();

    protected void beforeInitView(){

    }
    protected void afterInitView(){

    }


    protected P getPresenter() {
        return mPresenterProviders.getPresenter(0);
    }

    public PresenterProviders getPresenterProviders() {
        return mPresenterProviders;
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
    }

//    @Override
    public void showLoading() {
        if(progress==null)
            progress = new ProgressDialog(this);
        progress.show();
    }

//    @Override
    public void hideLoading() {
        if(progress!=null && progress.isShowing()){
            progress.dismiss();
        }
    }

    @Override
    public void toast(CharSequence s) {

    }

    @Override
    public void toast(int id) {

    }

    @Override
    public void toastLong(CharSequence s) {

    }

    @Override
    public void toastLong(int id) {

    }

    @Override
    public void showNullLayout() {

    }

    @Override
    public void hideNullLayout() {

    }

    @Override
    public void showErrorLayout(View.OnClickListener listener) {

    }

    @Override
    public void hideErrorLayout() {

    }

    @Override
    public void showServerError(int errorCode, String errorDesc) {

    }

    @Override
    public void showSuccess(boolean isSuccess) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterDispatch.detachView();
        if (hasBus) {
            EventBus.getDefault().unregister(this);
        }
        if(unbinder != null)
            unbinder.unbind();
        if(progress!=null){
            progress.cancel();
        }
    }

    /*** 全透状态栏 */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            //21表示5.0W
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//虚拟键盘也透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected void forbidScrennShot(){
        //禁止截屏的方法
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }
}

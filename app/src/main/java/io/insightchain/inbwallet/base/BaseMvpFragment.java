package io.insightchain.inbwallet.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.insightchain.inbwallet.base.mvp.BaseMvpView;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterDispatch;
import io.insightchain.inbwallet.base.mvp.PresenterProviders;

/**
 * create by NiPengyuan
 * time:2019/3/27
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends Fragment implements BaseMvpView {
    protected View mRootView;
    protected LayoutInflater inflater;
    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;
    //标志位 fragment是否可见
    protected boolean isVisible;
    protected ProgressDialog progress;
    protected Context mContext;
    protected Activity mActivity;

    private PresenterProviders mPresenterProviders;
    private PresenterDispatch mPresenterDispatch;

    private boolean hasBus = false;
    protected Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        } else {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            mActivity = getActivity();
            mContext = mActivity;
            this.inflater = inflater;
        }
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);

        mPresenterDispatch.attachView(getActivity(), this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
        isPrepared = true;
        init();
        lazyLoad();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDispatch.onSaveInstanceState(outState);
    }

    protected P getPresenter() {
        return mPresenterProviders.getPresenter(0);
    }

    public PresenterProviders getPresenterProviders() {
        return mPresenterProviders;
    }

    /**
     * 获取布局
     */
    public abstract
    @LayoutRes
    int getLayoutId();


    /**
     * 初始化
     */
    protected abstract void init();


    /**
     * 懒加载
     */
    private void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        lazyLoadData();
        isPrepared = false;
    }

    /**
     * 懒加载
     */
    protected void lazyLoadData() {

    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

//    @Override
    public void showLoading() {
        if(progress==null)
            progress = new ProgressDialog(mContext);
        progress.show();
    }

//    @Override
    public void hideLoading() {
        if(progress!=null){
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
    public void onDestroyView() {
        super.onDestroyView();
        mPresenterDispatch.detachView();
        if(unbinder != null)
            unbinder.unbind();
        if (hasBus) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDetach() {
        this.mActivity = null;
        super.onDetach();
    }

    protected Intent getIntent() {
        return getActivity().getIntent();
    }

    protected Intent getIntent(Class<?> cls) {
        return new Intent(mContext, cls);
    }

    protected Intent getIntent(Class<?> cls, int flags) {
        Intent intent = getIntent(cls);
        intent.setFlags(flags);
        return intent;
    }

    protected void startActivity(Class<?> cls) {
        startActivity(getIntent(cls));
    }

    protected void startActivity(Class<?> cls, int flags) {
        startActivity(getIntent(cls, flags));
    }

    protected void startActivityFinish(Class<?> cls) {
        startActivity(cls);
        finish();
    }

    protected void startActivityFinish(Class<?> cls, int flags) {
        startActivity(cls, flags);
        finish();
    }

    protected void finish() {
        getActivity().finish();
    }
}

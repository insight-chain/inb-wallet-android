package io.insightchain.inbwallet.base.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;

/**
 * create by NiPengyuan
 * time:2019/3/27
 */
public class BasePresenter <V>  {

    protected Context mContext;
    protected V mView;
    protected CompositeDisposable compositeDisposable;
    protected String TAG = getClass().getSimpleName();

    protected void onCleared() {

    }

    public void attachView(Context context, V view) {
        this.mContext = context;
        this.mView = view;
    }

    public void detachView() {
        this.mView = null;
    }

    public boolean isAttachView() {
        return this.mView != null;
    }

    public void onCreatePresenter(@Nullable Bundle savedState) {
        compositeDisposable = new CompositeDisposable();
    }

    public void onDestroyPresenter() {
        this.mContext = null;
        if(compositeDisposable!=null && !compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
        detachView();
    }

    public void onSaveInstanceState(Bundle outState) {

    }
}

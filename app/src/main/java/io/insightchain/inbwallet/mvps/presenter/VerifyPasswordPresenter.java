package io.insightchain.inbwallet.mvps.presenter;

import android.text.TextUtils;
import android.util.Log;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VerifyPasswordPresenter extends BasePresenter<VerifyPasswordView> {

    public void verifyPassword(String password,int type){
        Log.e(TAG,"mView.showLoading()");
        mView.showLoading();
        if(TextUtils.isEmpty(password)){
            mView.passwordEmpty();
            mView.hideLoading();
            return;
        }
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            boolean passwordRight = ETHWalletUtils.verifyPassword(BaseApplication.getCurrentWalletID(),password);
//                BaseApplication.getCurrentWallet().getKeystore().verifyPassword(password);
            e.onNext(passwordRight);
            e.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.e(TAG,"mView.showLoading()");
                        mView.hideLoading();
                        if (aBoolean){
                            mView.passwordRight(password,type);
                        }else{
                            mView.passwordWrong();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

}

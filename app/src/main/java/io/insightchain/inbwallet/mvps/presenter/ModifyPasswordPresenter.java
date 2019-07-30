package io.insightchain.inbwallet.mvps.presenter;

import android.util.Log;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.ETHWallet;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ModifyPasswordPresenter extends BasePresenter<ModifyPasswordView> {

    public void modifyPassword(String oldPassword, String newPassword){
        mView.showLoading();
        //验证密码是否正确
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            boolean isRightPassword = ETHWalletUtils.verifyPassword(BaseApplication.getCurrentWalletID(), oldPassword);
            Log.e("ModifyPasswordPresenter","isRightPassword = "+isRightPassword);
            if(isRightPassword) {
                ETHWallet wallet = ETHWalletUtils.modifyPassword(BaseApplication.getCurrentWalletID(), BaseApplication.getCurrentWallet().getName(), oldPassword, newPassword);
                BaseApplication.setCurrentWallet(wallet);
            }
            e.onNext(isRightPassword);
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
                mView.hideLoading();
                if (aBoolean){
                    mView.modifySuccess();
                }else{
                    mView.passwordWrong();
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                e.printStackTrace();
                ToastUtils.showToast(mContext,e.getMessage());
            }

            @Override
            public void onComplete() {
//                mView.hideLoading();
            }
        });
    }

}

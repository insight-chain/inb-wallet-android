package io.insightchain.inbwallet.mvps.presenter;


import android.util.Log;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.ETHWallet;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;
import io.insightchain.inbwallet.wallet.utils.WalletDaoUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateWalletPresenter extends BasePresenter<CreateWalletView> {

    public void createWallet(String walletName, String password, String passwordHint){
        mView.showLoading();
        Observable.create((ObservableOnSubscribe<ETHWallet>) e -> {
            ETHWallet wallet = ETHWalletUtils.generateMnemonic(walletName,password,passwordHint);
            // 将新创建的钱包添加到数据库中，并选中新钱包

            Log.e("aaa","wallet = "+wallet);

//            ETHWalletUtils.derivePrivateKey(wallet.getId(),password);
//                WalletManager.exportPrivateKey(BaseApplication.getCurrentWallet(),password);
            e.onNext(wallet);
            e.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ETHWallet>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ETHWallet wallet) {
                        WalletDaoUtils.insertNewWallet(wallet);
                        BaseApplication.setCurrentWallet(wallet);
                        mView.hideLoading();
                        mView.sendEvent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        e.printStackTrace();
                        ToastUtils.showToast(mContext,e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                });


    }

}

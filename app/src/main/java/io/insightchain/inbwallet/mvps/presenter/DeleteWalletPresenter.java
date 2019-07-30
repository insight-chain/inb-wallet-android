package io.insightchain.inbwallet.mvps.presenter;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DeleteWalletPresenter extends BasePresenter<DeleteWalletView> {

    public void verifyPassword(String password, int type){
        mView.showLoading();
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            boolean rightPassword = ETHWalletUtils.verifyPassword(BaseApplication.getCurrentWalletID(),password);
            e.onNext(rightPassword);
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
                        if (aBoolean){
                            if (type == 0){
                                mView.hideLoading();
                                mView.dismissPop();
                                mView.jumpBacksUp(password);
                            }else{
                                deletWallet();
                            }
                        }else{
                            ToastUtils.showToast(mContext, "密码不正确！");
                            mView.dismissPop();
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        mView.dismissPop();
                        e.printStackTrace();
                        ToastUtils.showToast(mContext,e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void deletWallet(){

        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            ETHWalletUtils.deleteWallet(BaseApplication.getCurrentWalletID());
            e.onNext(true);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.hideLoading();
                        mView.dismissPop();
//                        if (aBoolean){
                        BaseApplication.setCurrentWalletID(-1L);
                        mView.sendEvent();
//                        }else{
//                            ToastUtils.showToast(mContext, "密码不正确！");
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        mView.dismissPop();
                        e.printStackTrace();
                        ToastUtils.showToast(mContext, "删除过程出现问题，删除失败！");
//                        ToastUtils.showToast(mContext,e.getMessage());
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                        mView.dismissPop();
                    }
                });


    }
}

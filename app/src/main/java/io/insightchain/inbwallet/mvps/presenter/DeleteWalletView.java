package io.insightchain.inbwallet.mvps.presenter;


import io.insightchain.inbwallet.base.mvp.BaseMvpView;

public interface DeleteWalletView extends BaseMvpView {
    void sendEvent();
    void jumpBacksUp(String password);
    void dismissPop();

}

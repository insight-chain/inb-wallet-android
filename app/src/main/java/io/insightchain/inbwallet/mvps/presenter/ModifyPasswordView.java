package io.insightchain.inbwallet.mvps.presenter;


import io.insightchain.inbwallet.base.mvp.BaseMvpView;

public interface ModifyPasswordView extends BaseMvpView {

    void modifySuccess();
    void passwordWrong();

}

package io.insightchain.inbwallet.mvps.presenter;

import io.insightchain.inbwallet.base.mvp.BaseMvpView;

public interface VerifyPasswordView extends BaseMvpView {

    void passwordRight(String password, int type);
    void passwordWrong();
    void passwordEmpty();

}

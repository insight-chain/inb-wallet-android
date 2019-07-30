package io.insightchain.inbwallet.mvps.presenter;

import io.insightchain.inbwallet.base.mvp.BaseMvpView;

public interface WalletView extends BaseMvpView {
    void showBalance(String balance);
    void showRMBBalance(String rmbStr);
    void showZeroBalance();
    void showResource(String netCurrent, String netTotal, String mortgageInbNumber);
}

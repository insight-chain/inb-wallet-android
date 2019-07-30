package io.insightchain.inbwallet.mvps.presenter;

import android.graphics.Bitmap;

import io.insightchain.inbwallet.base.mvp.BaseMvpView;

public interface ReceiveCoinsView extends BaseMvpView {

    void setQrImage(Bitmap bitmap);
//    void sendBroadcast(File file);
}

package io.insightchain.inbwallet.mvps.presenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.utils.HexUtils;
import io.insightchain.inbwallet.utils.ScreenUtils;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.utils.ZxUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static io.insightchain.inbwallet.common.Constants.PACKAGE_NAME;
import static io.insightchain.inbwallet.common.Constants.PERMISSION_WRITE_FILE_CODE;

public class ReceiveCoinsPresenter extends BasePresenter<ReceiveCoinsView> {

    Bitmap qrBitmap;

    public void saveImage(){

        mView.showLoading();
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+PACKAGE_NAME);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, "insight" + System.currentTimeMillis() + ".png");
                FileOutputStream fos = null;

                fos = new FileOutputStream(file);
                fos.write(byteArray, 0, byteArray.length);
                fos.flush();
                fos.close();
//                    bitmap.recycle();
//                mView.sendBroadcast(file);
                sendBroadcast(file);
                e.onNext(true);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean){
                            mView.hideLoading();
                            ToastUtils.showToast(mContext,"图片保存成功！");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        ToastUtils.showToast(mContext,"图片保存失败！");
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

    public void getQrImage(String address){
        mView.showLoading();
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                qrBitmap = ZxUtils.createQrCode(ScreenUtils.dp2px(mContext,195),
                        ScreenUtils.dp2px(mContext,195),
                        HexUtils.prependHexPrefix(address));
                e.onNext( qrBitmap !=null);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            mView.setQrImage(qrBitmap);
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showToast(mContext, "生成二维码过程出错！");
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 请求手机弹出权限框后，用户点击是否授权的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_WRITE_FILE_CODE) {
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //授权允许
                saveImage();
            } else {
                ToastUtils.showToast(mContext,"写入权限被拒绝！");
            }
            return;
        }
    }

    @Override
    public void onDestroyPresenter() {
        super.onDestroyPresenter();
        if(qrBitmap!=null && !qrBitmap.isRecycled()) {
            qrBitmap.recycle();
            qrBitmap = null;
        }
    }

    public void sendBroadcast(File file) {
        //用广播通知相册进行更新相册
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        mContext.sendBroadcast(intent);
    }

}

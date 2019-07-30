package io.insightchain.inbwallet.mvps.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.sql.Timestamp;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.common.TimestampAdapter;
import io.insightchain.inbwallet.mvps.http.ApiServiceHelper;
import io.insightchain.inbwallet.mvps.model.vo.AppVersionVo;
import io.insightchain.inbwallet.mvps.model.vo.HttpResult;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.service.DownloadService;
import io.insightchain.inbwallet.utils.APKVersionCodeUtils;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingPresenter extends BasePresenter<SettingView> {

    public void checkVersion(){
        ApiServiceHelper.getApiService("http://192.168.1.211:8080/inb-api-version/").getWalletVersion(4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<JsonObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(HttpResult<JsonObject> httpResult) {
                        if (httpResult.getCode() == 2000) {
                            Gson gon = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampAdapter()).create();
                            AppVersionVo appVersionVo = gon.fromJson(httpResult.getData(), AppVersionVo.class);
                            if(appVersionVo.getVersionCode()==(APKVersionCodeUtils.getVersionCode(mContext))){
                                //已是最新
                                mView.showNewestToast();
                            }else{
                                showUpdatePop(appVersionVo.getDownloadUrl(),appVersionVo.getVersionName(),appVersionVo.getReleaseNote());
                            }

                        }else{
                            ToastUtils.showToast(mContext, httpResult.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private ProgressBar progressBar;
    private TextView percentText;
    private CustomPopupWindow popupWindow;
    public void showUpdatePop(String downloadUrl,String versionName,String description) {
        if(popupWindow == null) {
            popupWindow = new CustomPopupWindow(mContext, R.layout.layout_popup_update_version);
            progressBar = popupWindow.getView().findViewById(R.id.progress_bar);
            percentText = popupWindow.getView().findViewById(R.id.tv_percent_number);
            TextView versionNameText = popupWindow.getView().findViewById(R.id.tv_version_name);
            TextView describeText = popupWindow.getView().findViewById(R.id.update_describe);
            versionNameText.setText(versionName);
            describeText.setText(description);
            popupWindow.setSingleClickListener(new int[]{R.id.update_btn,R.id.iv_close}, v -> {
                switch (v.getId()){
                    case R.id.update_btn:
                        //TODO 启动升级
                        showNotification(downloadUrl);
                        v.setVisibility(View.GONE);
                        popupWindow.getView().findViewById(R.id.rl_progress_layout).setVisibility(View.VISIBLE);
                        break;
                    case R.id.iv_close:
                        popupWindow.dismiss();
                        break;
                }
            });
        }
        mView.showPopWindow(popupWindow);
    }

    public void showNotification(String downloadUrl){
        Intent myIntent = new Intent(mContext, DownloadService.class);
        myIntent.putExtra(Constants.UPDATE_APK_PATH, downloadUrl);
        mContext.startService(myIntent);
        mContext.bindService(myIntent,conn,Context.BIND_AUTO_CREATE);
        ToastUtils.showToast(mContext, mContext.getString(R.string.update_tip));
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            DownloadService downloadService = ((DownloadService.DownloadBinder)service).getService();
            if(downloadService!=null){
                //注册回调接口来接收下载进度的变化
                downloadService.setOnDownloadProgressChangeListener((currentProgress, max) -> {
//                    showDownloadProgress(currentProgress, max);
                    Observable.create(new ObservableOnSubscribe<Boolean>() {
                        @Override
                        public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                            e.onNext(true);
                            e.onComplete();
                        }
                    }).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    compositeDisposable.add(d);
                                }

                                @Override
                                public void onNext(Boolean aBoolean) {
                                    if(progressBar!=null && percentText!=null) {
                                        if(max!=0) {
                                            progressBar.setMax(max);
                                            progressBar.setProgress(currentProgress);
                                            percentText.setText(currentProgress + "%");
                                        }else{
                                            progressBar.setVisibility(View.GONE);
                                            percentText.setText("下载失败");
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                    Log.e("CustomPopupWindow","currentProgress = "+currentProgress+",max = "+max);
                });
            }
        }
    };

}

package io.insightchain.inbwallet.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.mvps.http.ApiServiceHelper;
import io.insightchain.inbwallet.mvps.http.DownloadInterceptor;
import io.insightchain.inbwallet.utils.InterfaceUtils;
import io.insightchain.inbwallet.utils.SDCardUtils;
import io.insightchain.inbwallet.utils.StorageUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    NotificationManager notificationManager;
    Notification myNotify;
    private String urlStr;
    private static final int NOTIFICATION_ID = 0;
    private NotificationCompat.Builder mBuilder;
    private OnDownloadProgressChangeListener onDownloadProgressChangeListener;
    public boolean isDownloading;

    public void setOnDownloadProgressChangeListener(OnDownloadProgressChangeListener onDownloadProgressChangeListener) {
        this.onDownloadProgressChangeListener = onDownloadProgressChangeListener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        myNotify = new NotificationCompat.Builder(this)
                .setTicker(getString(R.string.found_new_version_and_click_update))
                .setContentTitle(getString(R.string.found_new_version_and_click_update))
                .setSmallIcon(this.getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).build();
        myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
        notificationManager.notify(NOTIFICATION_ID, myNotify);
    }

    public class DownloadBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        urlStr = null;
        myNotify = null;
        notificationManager = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        urlStr = intent.getStringExtra(Constants.UPDATE_APK_PATH);
        if (!isDownloading)
            init();
//        installAPk(new File(SDCardUtils.getDataCachePath(this), "insight_android_v1.1.0_build_1100.apk"));

        return super.onStartCommand(intent, flags, startId);
    }

    public void init() {
        Log.e(TAG, "init()");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        String appName = getString(getApplicationInfo().labelRes);
        mBuilder.setContentTitle(appName + getString(R.string.upgrade)).setSmallIcon(R.mipmap.ic_launcher);
        File dir = StorageUtils.getCacheDirectory(this);
        final String apkName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
        final File apkFile = new File(dir, apkName);
        apkFileStr = apkFile.toString();
        Log.e("DownloadService", "path=" + apkFile.getPath());
        Log.e("DownloadService", "path=" + apkFile.getAbsolutePath());

        final InterfaceUtils.DownloadListener downloadListener = new InterfaceUtils.DownloadListener() {
            @Override
            public void onStartDownload() {
                Log.e("downloadListener", "onStartDownload");
                isDownloading = true;
            }

            @Override
            public void onProgress(int progress) {
                updateProgress(progress);
            }

            @Override
            public void onFinishDownload() {
                Log.e("downloadListener", "onFinishDownload");

            }

            @Override
            public void onFail(String errorInfo) {
                downloadFailed();
                Log.e("downloadListener", "errorInfo=" + errorInfo);

            }
        };
        Log.e(TAG, "urlStr" + urlStr);
        DownloadInterceptor interceptor = new DownloadInterceptor(downloadListener);
        ApiServiceHelper.getDownloadApiService(interceptor).downloadFile(urlStr)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody response) throws Exception {
                        SDCardUtils.writeFile(response.byteStream(), apkFile.getAbsolutePath(), downloadListener);
                        return true;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.e("downloadService", "onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.e("downloadService", "onCompleted");
                        isDownloading = false;
                        installAPk(apkFile);
                    }
                });
/*
        DataProcess.requestDownloadApk(this, false, urlStr, false, null, new DataProcess.RequestDownloadCallback() {
            @Override
            public void onProgress(int progress) {
                updateProgress(progress);
            }

            @Override
            public void successfullyCallback(File file) {
                installAPk(file);
                LocalData.getInstance().setDownloading(false);
                notificationManager.cancel(NOTIFICATION_ID);
            }

            @Override
            public void errorCallback(String result) {
                if (result != null) {
                    UIUtils.showTip(DownloadService.this, false, result);
                }
                LocalData.getInstance().setDownloading(false);
                downloadFailed();
            }
        }, apkFile);*/

    }

    int mProgress;

    private void updateProgress(int progress) {
        //"正在下载:" + progress + "%"
        if (onDownloadProgressChangeListener != null) {
            Log.e(TAG, "onDownloadProgressChangeListener!=null");
            onDownloadProgressChangeListener.onDownloadChanged(progress, 100);
        } else {
            Log.e(TAG, "onDownloadProgressChangeListener==null");
        }
        mProgress = progress;
        mBuilder.setContentText(this.getString(R.string.update_download_progress, progress)).setProgress(100, progress, false);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, getIntent(), PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingintent);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void downloadFailed() {
        myNotify.flags = Notification.FLAG_AUTO_CANCEL;
        mBuilder.setContentText(getString(R.string.one_file_zero_done));
        mBuilder.setContentTitle(getString(R.string.download_failed));
        Log.e(TAG, "downloadFailed");
        if (onDownloadProgressChangeListener != null)
            onDownloadProgressChangeListener.onDownloadChanged(0, 0);
        mBuilder.setProgress(0, 0, false);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setAutoCancel(true);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, getIntent(), PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingintent);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void installAPk(File apkFile) {
        Log.e(TAG, "installApk progress=" + mProgress);
        if (mProgress != 100)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            //清单文件中配置的authorities
            Log.e(TAG, StorageUtils.getCacheDirectory(this).getPath());
            data = FileProvider.getUriForFile(this, "io.insightchain.fileProvider", apkFile);
            // 给目标应用一个临时授权
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//重点！！
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//重点！！！
            data = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        notificationManager.cancel(NOTIFICATION_ID);

    }

    String apkFileStr;

    public Intent getIntent() {
        if (mProgress != 100)
            return new Intent();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
        try {
            String[] command = {"chmod", "777", apkFileStr};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            //清单文件中配置的authorities
            Log.e(TAG, StorageUtils.getCacheDirectory(this).getPath());
            data = FileProvider.getUriForFile(this, "io.insightchain.fileProvider", new File(apkFileStr));
            // 给目标应用一个临时授权
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//重点！！
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//重点！！！
            data = Uri.fromFile(new File(apkFileStr));
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
    public interface OnDownloadProgressChangeListener {
        void onDownloadChanged(int currentProgress, int max);
    }
}

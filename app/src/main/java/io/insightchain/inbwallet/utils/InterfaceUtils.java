package io.insightchain.inbwallet.utils;

import android.view.View;

/**
 * Created by lijilong on 05/16.
 */

public class InterfaceUtils {
    public interface OnClickListener {
        void onClick(View view, String msg);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**下载回调*/
    public interface DownloadListener {

        void onStartDownload();

        void onProgress(int progress);

        void onFinishDownload();

        void onFail(String errorInfo);
    }
    /**微信分享*/
    public interface ShareListener {
        void onShareDone(int errCode);
    }

}

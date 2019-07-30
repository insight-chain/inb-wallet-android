package io.insightchain.inbwallet.mvps.http;


import java.io.IOException;

import io.insightchain.inbwallet.utils.InterfaceUtils;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by lijilong on 06/06.
 */

public class DownloadInterceptor implements Interceptor {
    InterfaceUtils.DownloadListener downloadListener;

    public DownloadInterceptor(InterfaceUtils.DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new DownloadResponseBody(response.body(), downloadListener)).build();
    }
}

package io.insightchain.inbwallet.mvps.http;

import android.util.Log;

import java.io.IOException;

import io.insightchain.inbwallet.utils.InterfaceUtils;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by lijilong on 06/06.
 * 带进度条，下载请求体
 */

public class DownloadResponseBody extends ResponseBody {
    private ResponseBody responseBody;

    private InterfaceUtils.DownloadListener downloadListener;

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, InterfaceUtils.DownloadListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Log.e("download", "read: " + (int) (totalBytesRead * 100 / responseBody.contentLength()) + "totalRead=" + totalBytesRead + ",total=" + responseBody.contentLength());
                if (null != downloadListener) {
                    if (bytesRead != -1) {
                        downloadListener.onProgress((int) (totalBytesRead * 100 / responseBody.contentLength()));
                    }

                }
                return bytesRead;
            }
        };

    }
}

package io.insightchain.inbwallet.mvps.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.mvps.model.vo.UrlVo;
import io.insightchain.inbwallet.wallet.Network;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashPresenter extends BasePresenter<SplashView> {


    public void getUrl(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constants.Json_URL + "?v=" + System.currentTimeMillis())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                Log.e(TAG, "onFailure");
                //TODO 获取地址失败提示
                Constants.HOST_URL = Network.MAINNET;
                mView.jumpMain();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String jsonStr = response.body().string();
                    Log.e(TAG, jsonStr);
                    JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
                    JsonObject object = jsonObject.getAsJsonObject("inbUrl");
                    String jStr = object.toString();
                    Log.e(TAG, jStr);
                    Gson gson = new Gson();
                    UrlVo vo = gson.fromJson(jStr, UrlVo.class);
                    if (vo.getApi() != null && vo.getApi().size() != 0) {
                        Constants.HOST_INSIGHT_URL = vo.getApi().get(0);
                    }
                    if (vo.getExplorer() != null && vo.getExplorer().size() != 0) {
                        Constants.HOST_BROWSER_URL = vo.getExplorer().get(0);
                    }
                    if (vo.getChain() != null && vo.getChain().size() != 0) {
//                        Constants.HOST_URL = vo.getChain().get(0);
                        Constants.HOST_URL = Network.MAINNET;
                    }
                    mView.jumpMain();
                } catch (Exception e) {
                    //TODO 异常提示
                    e.printStackTrace();
                    Constants.HOST_URL = Network.MAINNET;
                    mView.jumpMain();
                }
            }
        });
    }
}

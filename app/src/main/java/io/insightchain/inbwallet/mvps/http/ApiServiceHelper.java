package io.insightchain.inbwallet.mvps.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.common.TimestampAdapter;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by lijilong on 04/28.
 */

public class ApiServiceHelper {
    //传进来请求参数，是因为有多个基本请求参数用拦截器添加，避免api方法中书写过多参数，而且“加密需要所有参数”,同时为了查看方便，没有将所有值在拦截器中添加，关键参数在方法中申明
    public static ApiService getApiService(String baseUrl) {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampAdapter())
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Map<String, String> headers = getHeaders(BaseApplication.getInstance().getApplicationContext());
                    Request original = chain.request();
                    boolean isPOST = "POST".equals(original.method());
                    //添加请求头参数
                    Request.Builder requestBuild = original.newBuilder();
                    for (Iterator<String> iterator = headers.keySet().iterator(); iterator.hasNext(); ) {
                        String key = iterator.next();
                        String value = headers.get(key);
                        requestBuild.addHeader(key, value);
                    }
                    requestBuild.removeHeader("User-Agent")
                            .addHeader("User-Agent", getUserAgent());
                    requestBuild.addHeader("Connection", "close");
//                        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
//                            requestBuild.addHeader("Connection", "close");
//                        }
                    //以下方法能够添加请求体参数
                    FormBody formBody;
                    try {
                        formBody = (FormBody) original.body();
                    } catch (Exception e) {
                        formBody = null;
                    }
                    Map<String, String> queryParam = new HashMap<String, String>();
                    Set<String> keys = original.url().queryParameterNames();
                    String value;
                    for (String key : keys) {
                        value = original.url().queryParameter(key);
                        queryParam.put(key, value);
                    }
                    if (isPOST && formBody != null && formBody.size() > 0) {
                        for (int i = 0; i < formBody.size(); i++) {
                            queryParam.put(formBody.name(i), formBody.value(i));
                        }
                    }
                    String sign = getSign(queryParam);
                    if (!TextUtils.isEmpty(sign)) {
                        requestBuild.addHeader("s", sign);
                        Log.e("sign", sign);
                    }
                    return chain.proceed(requestBuild.build());
                }).retryOnConnectionFailure(false)
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }

    public static ApiService getApiService(String baseUrl,int seconds) {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampAdapter())
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(seconds, TimeUnit.SECONDS)
                .readTimeout(seconds, TimeUnit.SECONDS)
                .writeTimeout(seconds, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Map<String, String> headers = getHeaders(BaseApplication.getInstance().getApplicationContext());
                        Request original = chain.request();
                        boolean isPOST = "POST".equals(original.method());
                        //添加请求头参数
                        Request.Builder requestBuild = original.newBuilder();
                        for (Iterator<String> iterator = headers.keySet().iterator(); iterator.hasNext(); ) {
                            String key = iterator.next();
                            String value = headers.get(key);
                            requestBuild.addHeader(key, value);
                        }
                        requestBuild.removeHeader("User-Agent")
                                .addHeader("User-Agent", getUserAgent());
                        requestBuild.addHeader("Connection", "close");
//                        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
//                            requestBuild.addHeader("Connection", "close");
//                        }
                        //以下方法能够添加请求体参数
                        FormBody formBody;
                        try {
                            formBody = (FormBody) original.body();
                        } catch (Exception e) {
                            formBody = null;
                        }
                        Map<String, String> queryParam = new HashMap<String, String>();
                        Set<String> keys = original.url().queryParameterNames();
                        String value;
                        for (String key : keys) {
                            value = original.url().queryParameter(key);
                            queryParam.put(key, value);
                        }
                        if (isPOST && formBody != null && formBody.size() > 0) {
                            for (int i = 0; i < formBody.size(); i++) {
                                queryParam.put(formBody.name(i), formBody.value(i));
                            }
                        }
                        String sign = getSign(queryParam);
                        if (!TextUtils.isEmpty(sign)) {
                            requestBuild.addHeader("s", sign);
                            Log.e("sign", sign);
                        }
                        return chain.proceed(requestBuild.build());
                    }
                }).retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }

    public static ApiService getApiServiceCommon(String baseUrl) {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampAdapter())
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Map<String, String> headers = getHeaders(BaseApplication.getInstance().getApplicationContext());
                        Request original = chain.request();
                        boolean isPOST = "POST".equals(original.method());
                        //添加请求头参数
                        Request.Builder requestBuild = original.newBuilder();
                        for (Iterator<String> iterator = headers.keySet().iterator(); iterator.hasNext(); ) {
                            String key = iterator.next();
                            String value = headers.get(key);
                            requestBuild.addHeader(key, value);
                        }
                        requestBuild.removeHeader("User-Agent")
                                .addHeader("User-Agent", getUserAgent());
                        requestBuild.addHeader("Connection", "close");
//                        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
//                            requestBuild.addHeader("Connection", "close");
//                        }
                        //以下方法能够添加请求体参数
                        FormBody formBody = (FormBody) original.body();
                        Map<String, String> queryParam = new HashMap<String, String>();
                        Set<String> keys = original.url().queryParameterNames();
                        String value;
                        for (String key : keys) {
                            value = original.url().queryParameter(key);
                            queryParam.put(key, value);
                        }
                        if (isPOST && formBody != null && formBody.size() > 0) {
                            for (int i = 0; i < formBody.size(); i++) {
                                queryParam.put(formBody.name(i), formBody.value(i));
                            }
                        }
                        String sign = getSign(queryParam);
                        if (!TextUtils.isEmpty(sign)) {
                            requestBuild.addHeader("s", sign);
                            Log.e("sign", sign);
                        }
                        return chain.proceed(requestBuild.build());
                    }
                }).retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }

    //下载文件用的Retrofit
    public static ApiService getDownloadApiService(DownloadInterceptor interceptor) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HOST_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }

    //获取请求要加的头数据，该数据还要处理下，去除空数据，还要加入对url参数加密后的一个参数
    public static Map<String, String> getHeaders(Context context) {
//        LocalData localData = LocalData.getInstance();
        Map<String, String> headers = new HashMap<>();
//        headers.put("lang", Locale.getDefault().toString());
//        headers.put("deviceUuid", Constants.DEVICE_ID);
//        headers.put("randomUuid", Constants.RANDOM_UUID);
//        headers.put("model", Constants.OS_MODEL);
//        headers.put("deviceBrand", Constants.DEVICE_BRAND);
//        headers.put("device", Constants.DEVICE);
//        headers.put("osVersion", Constants.OS_VERSION);
//        headers.put("appVersion", Constants.APP_VERSION);
//        headers.put("appVersionCode", Constants.APP_VERSION_CODE + "");
//        headers.put("flavor", Constants.FLAVOR);
//        headers.put("sn", Constants.SN);
//        headers.put("phoneNumber", Constants.PHONE_NUMBER);
//        headers.put("sdkInt", Constants.SDK_INT);
//        headers.put("display", Constants.DISPLAY);
//        headers.put("incremental", Constants.INCREMENTAL);
//        headers.put("screenWidth", "" + ScreenUtils.screenWidthPx);
//        headers.put("screenHeight", "" + ScreenUtils.screenHeightPx);
//        headers.put("density", "" + ScreenUtils.density);
//        headers.put("densityDpi", "" + ScreenUtils.densityDpi);
//        headers.put("androidId", Constants.ANDROID_ID);
//        headers.put("mac", Constants.MAC);
//        headers.put("bluetoothMac", Constants.BLUETOOTH_MAC);
//        if (localData.getCurrentLatitude() != null) {
//            headers.put("la", localData.getCurrentLatitude().toString());
//        } else {
//            SharedPreferences sp = SharedPreferencesUtils.getSharedPreferences(context, "encrypt");
//            Float latitude = sp.getFloat("latitude", 0);
//            headers.put("la", latitude.toString());
//        }
//        if (localData.getCurrentLongitude() != null) {
//            headers.put("lo", localData.getCurrentLongitude().toString());
//        } else {
//            SharedPreferences sp = SharedPreferencesUtils.getSharedPreferences(context, "encrypt");
//            Float longitude = sp.getFloat("longitude", 0);
//            headers.put("lo", longitude.toString());
//        }
//        if (!TextUtils.isEmpty(LocalData.getInstance().getEkey())) {
//            headers.put("m1", LocalData.getInstance().getEkey());
//        }
//        if (!TextUtils.isEmpty(LocalData.getInstance().getEserverKey())) {
//            headers.put("m2", LocalData.getInstance().getEserverKey());
//        }
//        if (!TextUtils.isEmpty(Constants.DEVICE_ID)) {
//            headers.put("s1", AppEncryptUtils.sign(Constants.DEVICE_ID, LocalData.getInstance().getKey()));
//        } else if (!TextUtils.isEmpty(Constants.RANDOM_UUID)) {
//            headers.put("s1", AppEncryptUtils.sign(Constants.RANDOM_UUID, LocalData.getInstance().getKey()));
//        }
//        if (localData.isLogin()) {
//            if (!TextUtils.isEmpty(localData.getEtoken())) {
//                headers.put("etoken", localData.getEtoken());
//            } else {
//                SharedPreferences sp = SharedPreferencesUtils.getSharedPreferences(BaseApplication.getInstance().getApplicationContext(), "config");
//                String etoken = sp.getString("eToken", null);
//                if (!TextUtils.isEmpty(etoken)) {
//                    headers.put("etoken", etoken);
//                    LocalData.getInstance().setEtoken(etoken);
//                }
//            }
//        }

        for (Iterator<String> iterator = headers.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = headers.get(key);
            if (TextUtils.isEmpty(value)) {
                iterator.remove();
            }

        }
        return headers;

    }

    public static String getSign(Map<String, String> original) {
        if (original.size() == 0)
            return null;
        Map<String, String> param = original;
        List<String> keys = new ArrayList<>(param.keySet());
        Collections.sort(keys);
        String signStr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = param.get(key);
            signStr += key + "=" + value;
        }
        if (TextUtils.isEmpty(signStr))
            return null;
//        return AppEncryptUtils.sign(signStr, LocalData.getInstance().getKey());
        return "";

    }

    public static String getUserAgent() {
//        return "insight/" + Constants.APP_VERSION + ";"
//                + "/Android/" + Constants.OS_VERSION + ";"
//                + Constants.DEVICE_BRAND + "/" + Constants.OS_MODEL;
        return "";
    }

    public static RequestBody toRequestBody(String value) {
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), value);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

}

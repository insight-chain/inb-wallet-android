package io.insightchain.inbwallet.mvps.http;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.insightchain.inbwallet.mvps.model.vo.AccountInfoVo;
import io.insightchain.inbwallet.mvps.model.vo.HttpResult;
import io.insightchain.inbwallet.mvps.model.vo.NodeResultVo;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;
import io.insightchain.inbwallet.mvps.model.vo.TestVo;
import io.insightchain.inbwallet.mvps.model.vo.TransactionRecordVo;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by lijilong on 03/06.
 */

public interface ApiService {
//    @GET("/student/mobileRegister")
//    Observable<HttpResult<UserEntity>> login(@Query("phone") String phone, @Query("password") String psw);

    @GET("getPicUrl")
    Observable<TestVo> getPicUrl(@Query("url") String url);

    /**
     * @param address 钱包地址
     * @param page 分页，
     * @param limit 每页显示
     * @return
     */
    @GET("v1/account/search/transfers")
    Observable<TransactionRecordVo> getTrasactionRecord(@Query("address") String address, @Query("page") int page, @Query("limit") int limit);

    @GET("/v1/account/search")
    Observable<AccountInfoVo> getAccountInfo(@Query("address") String address);

    @GET("/v1/node/info")
    Observable<NodeResultVo> getNodeList(@Query("page") int page, @Query("limit") int limit);

    @GET("api")
    Observable<JSONObject> sendTransaction(@Query("module") String proxy, @Query("action") String eth_sendRawTransaction, @Query("hex") String hex);

    @GET("coin/inb")
    Observable<HttpResult<JsonObject>> getCoinPrice();

    @GET("/v1/account/node/info")
    Observable<NodeVo> checkNode(@Query("address") String address);

    /**
     * @param appType 3 walletIOS,4 walletAndroid ,5 CZZ_Wallet_IOS,6 CZZ_wallet_android
     * @return
     */
    @GET("wallet/version")
    Observable<HttpResult<JsonObject>> getWalletVersion(@Query("appType") Integer appType);

    //下载文件 大文件下载要加这个注解（Streaming会实时下载字节码而不是将整个文件加入到内存中），不然会OOM
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);
}

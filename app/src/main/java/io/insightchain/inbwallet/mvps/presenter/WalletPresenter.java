package io.insightchain.inbwallet.mvps.presenter;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.common.TimestampAdapter;
import io.insightchain.inbwallet.mvps.http.ApiServiceHelper;
import io.insightchain.inbwallet.mvps.model.vo.CoinPriceVo;
import io.insightchain.inbwallet.mvps.model.vo.HttpResult;
import io.insightchain.inbwallet.utils.NumberUtils;
import io.insightchain.inbwallet.utils.PermissionsUtil;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletPresenter extends BasePresenter<WalletView> {

    private Web3j mWeb3j = Web3jFactory.build(new HttpService(Constants.HOST_URL));

    public void updateBalance(String mAddress){
        mView.showLoading();
        Observable.create((ObservableOnSubscribe<String>) e -> {
//            BigInteger balance = mWeb3j.ethGetBalance(mAddress,
//                    DefaultBlockParameterName.LATEST).send().getBalance();
//                AccountInfo accountInfo = mWeb3j.ethGetAccountInfo(mAddress).send().getAccountInfo();
            Object object = mWeb3j.ethGetAccountInfo(mAddress).send().getAccountInfo();
            String beginStr = object.toString();
            Log.e(TAG,beginStr);
            e.onNext(beginStr);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String beginStr) {

                        String balanceStr = beginStr.substring(beginStr.indexOf("Balance=")+8,
                                beginStr.indexOf(",",beginStr.indexOf("Balance=")+8));
                        Log.e(TAG,balanceStr);
                        BigDecimal bigDecimal = Convert.fromWei(TextUtils.isEmpty(balanceStr)||balanceStr.equalsIgnoreCase("null")?"0":balanceStr, Convert.Unit.ETHER);
                        String balanceString = bigDecimal.setScale(4, RoundingMode.FLOOR).toPlainString();
                        Log.e(TAG,balanceString);
                        //获取到余额
                        BaseApplication.getCurrentWalletVo().setBalance(balanceString);
                        mView.showBalance(balanceString);

                        String netStr = beginStr.substring(beginStr.indexOf("NET={")+5,
                                beginStr.indexOf("}",beginStr.indexOf("NET={")+5));
                        Log.e(TAG,netStr);
                        int endIndex;
                        if(netStr.substring(netStr.indexOf("Used=")+5).contains(",")){
                            endIndex = netStr.indexOf(",",netStr.indexOf("Used=")+5);
                        }else{
                            endIndex = netStr.length();
                        }
                        String usedNet = netStr.substring(netStr.indexOf("Used=")+5,
                                endIndex);//
                        Log.e(TAG, "usedNet = "+usedNet);
                        if(netStr.substring(netStr.indexOf("Usableness=")+11).contains(",")){
                            endIndex = netStr.indexOf(",",netStr.indexOf("Usableness=")+11);
                        }else{
                            endIndex = netStr.length();
                        }
                        String unUsedNet = netStr.substring(netStr.indexOf("Usableness=")+11,
                                endIndex);//
                        Log.e(TAG, "unUsedNet = "+unUsedNet);
                        if(netStr.substring(netStr.indexOf("MortgagteINB=")+13).contains(",")){
                            endIndex = netStr.indexOf(",",netStr.indexOf("MortgagteINB=")+13);
                        }else{
                            endIndex = netStr.length();
                        }
                        String mortgagteINB = netStr.substring(netStr.indexOf("MortgagteINB=")+13,
                                endIndex);//
                        //设置抵押INB金额
                        BigDecimal mortgageInb = Convert.fromWei(TextUtils.isEmpty(mortgagteINB)||mortgagteINB.equalsIgnoreCase("null")?"0":mortgagteINB, Convert.Unit.ETHER);
                        String mortgageInbNumber = mortgageInb.setScale(4, RoundingMode.FLOOR).toPlainString();
                        Log.e(TAG,mortgageInbNumber);
                        BaseApplication.getCurrentWalletVo().setMortgageInbNumber(mortgageInbNumber);
                        //设置可使用NET值
                        String usedNetString = "";
                        BigDecimal usedNetDecimal = Convert.fromWei(TextUtils.isEmpty(unUsedNet)||unUsedNet.equalsIgnoreCase("null")?"0":unUsedNet, Convert.Unit.WEI);
                        if(unUsedNet.length()>0 && unUsedNet.length()<=3){
                            usedNetString = usedNetDecimal.setScale(3, RoundingMode.FLOOR).toPlainString()+"byte";
                        }else if(unUsedNet.length()>3 && unUsedNet.length()<=6){
//                            usedNetString = NumberUtils.formatNumberNoComma(usedNetDecimal/1024,3)+"KB";
                            usedNetString = usedNetDecimal.divide(new BigDecimal(1024)).setScale(3, RoundingMode.FLOOR).toPlainString()+"KB";
                        }else if(unUsedNet.length()>6 && unUsedNet.length()<=9){
                            usedNetString = usedNetDecimal.divide(new BigDecimal(1024*1024)).setScale(3, RoundingMode.FLOOR).toPlainString()+"MB";
                        }else if(unUsedNet.length()>9 && unUsedNet.length()<=12){
                            usedNetString = usedNetDecimal.divide(new BigDecimal(1024*1024*1024)).setScale(3, RoundingMode.FLOOR).toPlainString()+"GB";
                        }else if(unUsedNet.length()>12){
                            usedNetString = usedNetDecimal.divide(new BigDecimal(1024*1024*1024*1024)).setScale(3, RoundingMode.FLOOR).toPlainString()+"TB";
                        }
                        Log.e(TAG,usedNetString);
                        BaseApplication.getCurrentWalletVo().setNetCanUsed(usedNetString);
                        //设置全部NET值
                        String totalNetString = "";
                        BigDecimal totalNetDecimal = usedNetDecimal.add(Convert.fromWei(TextUtils.isEmpty(usedNet)||usedNet.equalsIgnoreCase("null")?"0":usedNet, Convert.Unit.WEI));
                        Log.e(TAG,"totalNetDecimal = "+totalNetDecimal.toPlainString());
                        if(totalNetDecimal.toPlainString().length()>0 && totalNetDecimal.toPlainString().length()<=3){
                            totalNetString = totalNetDecimal.setScale(3, RoundingMode.FLOOR).toPlainString()+"byte";
                        }else if(totalNetDecimal.toPlainString().length()>3 && totalNetDecimal.toPlainString().length()<=6){
                            totalNetString = totalNetDecimal.divide(new BigDecimal(1024)).setScale(3, RoundingMode.FLOOR).toPlainString()+"KB";
                        }else if(totalNetDecimal.toPlainString().length()>6 && totalNetDecimal.toPlainString().length()<=9){
                            totalNetString = totalNetDecimal.divide(new BigDecimal(1024*1024)).setScale(3, RoundingMode.FLOOR).toPlainString()+"MB";
                        }else if(totalNetDecimal.toPlainString().length()>9 && totalNetDecimal.toPlainString().length()<=12){
                            totalNetString = totalNetDecimal.divide(new BigDecimal(1024*1024*1024)).setScale(3, RoundingMode.FLOOR).toPlainString()+"GB";
                        }else if(totalNetDecimal.toPlainString().length()>12){
                            totalNetString = totalNetDecimal.divide(new BigDecimal(1024*1024*1024*1024)).setScale(3, RoundingMode.FLOOR).toPlainString()+"TB";
                        }
                        Log.e(TAG,totalNetString);
                        BaseApplication.getCurrentWalletVo().setNetTotal(totalNetString);
                        mView.showResource(usedNetString, totalNetString, mortgageInbNumber);

                        ApiServiceHelper.getApiService("http://apia61.insightchain.io/").getCoinPrice()
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
                                            JsonObject jsonObj = httpResult.getData();
                                            Gson gon = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampAdapter()).create();
                                            CoinPriceVo coinPriceVo = gon.fromJson(jsonObj, CoinPriceVo.class);
                                            Log.e(TAG,coinPriceVo.toString());
                                            float balanceNumber = Float.parseFloat(balanceString);
                                            double rmbNumber = balanceNumber * coinPriceVo.getCoinCnyPrice();
                                            String rmbString = NumberUtils.formatNumberNoComma(rmbNumber,4);
                                            BaseApplication.getCurrentWalletVo().setRmbNumber(rmbString);
                                            mView.showRMBBalance(rmbString);
                                        }else{
                                            ToastUtils.showToast(mContext, httpResult.getMessage());
                                        }

                                        mView.hideLoading();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mView.hideLoading();
                                        mView.showZeroBalance();
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onComplete() {
//                                        mView.hideLoading();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        mView.showZeroBalance();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });

    }

    public void requestStoragePermission(Activity activity){
//        CommonUtils.checkSelfPermission(this, PERMISSION_READ_PHONE_STATE, PERMISSION_READ_PHONE_STATE_CODE)
        if (!PermissionsUtil.getInstance(mContext).hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                || !PermissionsUtil.getInstance(mContext).hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !PermissionsUtil.getInstance(mContext).hasPermission(Manifest.permission.CAMERA)) {
//            PermissionsUtil.getInstance(mContext).showRequestPermissionsInfoAlertDialog(PermissionsUtil.READ_WRITE_EXTERNAL_PERMISSION_CODE);
//            ActivityCompat.requestPermissions(activity, new String[]{}, PermissionsUtil.READ_WRITE_EXTERNAL_PERMISSION_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PermissionsUtil.READ_WRITE_EXTERNAL_PERMISSION_CODE);
        }
    }

    public void requestCameraPermission(Activity activity){
        if (!PermissionsUtil.getInstance(mContext).hasPermission(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PermissionsUtil.CAMERA_PERMISSION_CODE);
        }
    }

}

package io.insightchain.inbwallet.mvps.presenter;

import android.util.Log;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.mvps.http.ApiServiceHelper;
import io.insightchain.inbwallet.mvps.model.vo.TransactionRecordVo;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TransactionRecordPresenter extends BasePresenter<TransactionRecordView> {

    private Web3j mWeb3j = Web3jFactory.build(new HttpService(Constants.HOST_URL));
    private int totalCount;
    private boolean isFirstRequest = true;

    public void getTransactionRecord(int pageNum){
        mView.showLoading();
        if(!isFirstRequest){
             if(pageNum >totalCount) {
                 mView.showLastLine();
                 mView.hideLoading();
                 return;
             }
        }
        ApiServiceHelper.getApiService("http://192.168.1.191:8383/").getTrasactionRecord(BaseApplication.getCurrentWallet().getAddress(),pageNum,20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TransactionRecordVo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(TransactionRecordVo transactionRecordVo) {
                        mView.hideLoading();
                        isFirstRequest = false;
                        totalCount = transactionRecordVo.getTotalCount();
                        if(totalCount>0){
                            mView.getList(transactionRecordVo.getItems());
                        }else{
                            ToastUtils.showToast(mContext, "没有找到交易记录！");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoading();
                        ToastUtils.showToast(mContext, "查询失败！");
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }


    public void getTransactionResult(){
        Observable.create((ObservableOnSubscribe<List<EthBlock.TransactionResult>>) e -> {
            BigInteger bigInteger = mWeb3j.ethBlockNumber().send().getBlockNumber();
            Log.e(TAG, bigInteger.toString());

            List<EthBlock.TransactionResult> results = new ArrayList<>();
            e.onNext(results);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EthBlock.TransactionResult>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<EthBlock.TransactionResult> results) {
//                        mView.getList(results);

//                        recordAdapter = new TransactionRecordAdapter(mContext,results,recyclerView);
//                        recyclerView.setAdapter(recordAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}

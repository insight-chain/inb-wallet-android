package io.insightchain.inbwallet.mvps.presenter;

import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ResourcePresenter extends BasePresenter<ResourceView> {

    private Web3j mWeb3j = Web3jFactory.build(new HttpService(Constants.HOST_URL));

    public void mortgageNet(String valueStr, String password){
        mView.showLoading();
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            BigInteger transactionCount = mWeb3j.ethGetTransactionCount(BaseApplication.getCurrentWalletVo().getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
            BigInteger gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();

            BigInteger gasLimit = new BigInteger("200000");
            BigDecimal value = Convert.toWei(valueStr, Convert.Unit.ETHER);
            String hexRemarkStr = Numeric.toHexString("mortgageNet".getBytes());
            //创建交易
            RawTransaction etherTransaction = RawTransaction.createTransaction(transactionCount, gasPrice, gasLimit, "0x596bf60c2feceabbc4bd95682d9d959d7cd75382", value.toBigInteger(),hexRemarkStr);
            Credentials credentials = WalletUtils.loadCredentials(password, BaseApplication.getCurrentWallet().getKeystorePath());
            byte[] signMessage = TransactionEncoder.signMessage(etherTransaction, credentials);
            String transactionHash = mWeb3j.ethMortgageNet(Numeric.toHexString(signMessage)).send().getTransactionHash();
            e.onNext(true);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        //TODO 交易成功
                        ToastUtils.showToast(mContext, "抵押成功！");
                        mView.sendEvent();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        ToastUtils.showToast(mContext, "抵押失败！");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

    public void unMortgageNet(String valueStr, String password){
        mView.showLoading();
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            BigInteger transactionCount = mWeb3j.ethGetTransactionCount(BaseApplication.getCurrentWalletVo().getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
            BigInteger gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();

            BigInteger gasLimit = new BigInteger("200000");
            BigDecimal value = Convert.toWei(valueStr, Convert.Unit.ETHER);
            String hexRemarkStr = Numeric.toHexString("unmortgageNet".getBytes());
            //创建交易
            RawTransaction etherTransaction = RawTransaction.createTransaction(transactionCount, gasPrice, gasLimit, "0x596bf60c2feceabbc4bd95682d9d959d7cd75382", value.toBigInteger(),hexRemarkStr);
            Credentials credentials = WalletUtils.loadCredentials(password, BaseApplication.getCurrentWallet().getKeystorePath());
            byte[] signMessage = TransactionEncoder.signMessage(etherTransaction, credentials);
            String transactionHash = mWeb3j.ethUnMortgageNet(Numeric.toHexString(signMessage)).send().getTransactionHash();
            Log.e("ResourcePresenter","transactionHash = "+transactionHash);
            e.onNext(true);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        //TODO 交易成功
                        ToastUtils.showToast(mContext, "解抵押成功！");
                        mView.sendEvent();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        ToastUtils.showToast(mContext, "解抵押失败！");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }


}

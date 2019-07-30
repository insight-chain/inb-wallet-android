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
import java.util.List;

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

public class VotePresenter extends BasePresenter<VoteView> {

    private Web3j mWeb3j = Web3jFactory.build(new HttpService(Constants.HOST_URL));

    public void sendVote(List<String> addressList, String password){

        mView.showLoading();
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            Log.e(TAG, BaseApplication.getCurrentWalletVo().getAddress());
            BigInteger transactionCount = mWeb3j.ethGetTransactionCount(BaseApplication.getCurrentWalletVo().getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
            BigInteger gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();

            Log.e(TAG, "run: " + transactionCount + ", " + gasPrice);
            BigInteger gasLimit = new BigInteger("200000");
            BigDecimal value = Convert.toWei("0", Convert.Unit.ETHER);
            Log.e(TAG, "gasLimit = " + gasLimit);
            Log.e(TAG, "run: value wei = " + value.toPlainString());
            String remarkStr = "candidates:";
            for(String addressStr:addressList){
                remarkStr += addressStr;
                if(addressList.indexOf(addressStr) != addressList.size()-1){
                    remarkStr += ",";
                }
            }
            Log.e(TAG, "remarkStr = " + remarkStr);
            String hexRemarkStr = Numeric.toHexString(remarkStr.getBytes());
            Log.e(TAG, "hexRemarkStr = " + hexRemarkStr);
            //创建交易
            RawTransaction etherTransaction = RawTransaction.createTransaction(transactionCount, gasPrice, gasLimit, "0x596bf60c2feceabbc4bd95682d9d959d7cd75382", value.toBigInteger(),hexRemarkStr);
            Credentials credentials = WalletUtils.loadCredentials(password, BaseApplication.getCurrentWallet().getKeystorePath());
            byte[] signMessage = TransactionEncoder.signMessage(etherTransaction, credentials);
            Log.e(TAG,Numeric.toHexString(signMessage));
            String transactionHash = mWeb3j.ethSendRawVote(Numeric.toHexString(signMessage)).send().getTransactionHash();
            Log.e(TAG,"transactionHash = "+transactionHash);
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
                        ToastUtils.showToast(mContext, "交易成功！");
//                        mView.sendEvent();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        ToastUtils.showToast(mContext, "异常！");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

}

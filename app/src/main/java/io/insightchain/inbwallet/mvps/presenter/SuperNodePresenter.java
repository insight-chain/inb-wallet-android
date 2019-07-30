package io.insightchain.inbwallet.mvps.presenter;

import android.text.TextUtils;
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

public class SuperNodePresenter extends BasePresenter<SuperNodeView> {

    private Web3j mWeb3j = Web3jFactory.build(new HttpService(Constants.HOST_URL));

    public void registerSuperNode(String content, String password){
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
            Log.e(TAG, "toStr = "+BaseApplication.getCurrentWalletVo().getAddress());
            String hexRemarkStr = "";
            if(!TextUtils.isEmpty(content)) {
                hexRemarkStr = Numeric.toHexString(content.getBytes());
            }
            //创建交易
            RawTransaction etherTransaction = RawTransaction.createTransaction(transactionCount, gasPrice, gasLimit, BaseApplication.getCurrentWalletVo().getAddress(), value.toBigInteger(),hexRemarkStr);
            Credentials credentials = WalletUtils.loadCredentials(password, BaseApplication.getCurrentWallet().getKeystorePath());
            byte[] signMessage = TransactionEncoder.signMessage(etherTransaction, credentials);

            String transactionHash = mWeb3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).send().getTransactionHash();
            Log.e("TransferPresenter","transactionHash = "+transactionHash);
            Log.e("TransferPresenter","Numeric.toHexString(signMessage) = "+Numeric.toHexString(signMessage));
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
                        mView.showSuccessRegist();
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

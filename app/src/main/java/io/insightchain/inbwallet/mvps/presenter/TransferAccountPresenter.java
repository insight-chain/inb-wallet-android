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

import java.io.IOException;
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

public class TransferAccountPresenter extends BasePresenter<TransferAccountView> {

    private Web3j mWeb3j = Web3jFactory.build(new HttpService(Constants.HOST_URL));

    public void sendTransaction(String toStr, String valueStr, String password, String remarkStr){
        mView.showLoading();
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            Log.e("TransferPresenter", BaseApplication.getCurrentWalletVo().getAddress());
            BigInteger transactionCount = mWeb3j.ethGetTransactionCount(BaseApplication.getCurrentWalletVo().getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
            BigInteger gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();

            Log.e("TransferPresenter", "run: " + transactionCount + ", " + gasPrice);
            BigInteger gasLimit = new BigInteger("200000");
            BigDecimal value = Convert.toWei(valueStr, Convert.Unit.ETHER);
            Log.e("TransferPresenter", "gasLimit = " + gasLimit);
            Log.e("TransferPresenter", "run: value wei = " + value.toPlainString());
            Log.e("TransferPresenter", "toStr = "+toStr);
            String hexRemarkStr = "";

            if(!TextUtils.isEmpty(remarkStr)) {
//                    hexRemarkStr = NumericUtil.prependHexPrefix(HexUtils.convertStringToUTF8(remarkStr));
                hexRemarkStr = Numeric.toHexString(remarkStr.getBytes());
            }
            //创建交易
//            Transaction transaction = new Transaction(BaseApplication.getCurrentWalletVo().getAddress(),transactionCount, gasPrice, gasLimit, toStr, value.toBigInteger(),hexRemarkStr);
//            mWeb3j.ethSendTransaction(transaction);
                    RawTransaction etherTransaction = RawTransaction.createTransaction(transactionCount, gasPrice, gasLimit, toStr, value.toBigInteger(),hexRemarkStr);
//            Payment payment = new Payment(null,new Byte("0"),new byte[]{},new byte[]{});
//                    PaymentRawTransaction etherTransaction = PaymentRawTransaction.createTransaction2(transactionCount, gasPrice, gasLimit, toStr, value.toBigInteger(),hexRemarkStr,payment);

//            EthereumTransaction tran = new EthereumTransaction(transactionCount, gasPrice, gasLimit, toStr, value.toBigInteger(), hexRemarkStr);
//                EthereumTransaction tran = new EthereumTransaction(transactionCount, BigInteger.valueOf(20000000000L),
//                        BigInteger.valueOf(21000), toStr, value.toBigInteger(), "0xE4B88AE6B5B7");
//            TxSignResult result = tran.signTransaction(Integer.toString(ChainId.ETHEREUM_ROPSTEN), password, BaseApplication.getCurrentWallet());
//            String signedTx = result.getSignedTx(); // This is the signature result which you need to broadcast.
//            String txHash = result.getTxHash(); // This is txHash which you can use for locating your transaction record
            Credentials credentials = WalletUtils.loadCredentials(password, BaseApplication.getCurrentWallet().getKeystorePath());
            byte[] signMessage = TransactionEncoder.signMessage(etherTransaction, credentials);


            String transactionHash = mWeb3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).send().getTransactionHash();
//            Log.e("TransferPresenter", "signedTx = "+signedTx+",txHash = "+txHash);
                Log.e("TransferPresenter","transactionHash = "+transactionHash);
                Log.e("TransferPresenter","Numeric.toHexString(signMessage) = "+Numeric.toHexString(signMessage));
//                Admin admin = AdminFactory.build(new HttpService(Network.ROPSTEN));
//                Request<?, EthTransaction> ethTransactionRequest = admin.ethGetTransactionByHash(txHash);
//                ethTransactionRequest.
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
                        mView.sendEvent();
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

    public void sendRawTransaction(String toStr, String valueStr, String password, String remarkStr){
        mView.showLoading();
        Observable.create((ObservableOnSubscribe<String>) e -> {
            Log.e("TransferPresenter", BaseApplication.getCurrentWalletVo().getAddress());
            BigInteger transactionCount = mWeb3j.ethGetTransactionCount(BaseApplication.getCurrentWalletVo().getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
            BigInteger gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();

            Log.e("TransferPresenter", "run: " + transactionCount + ", " + gasPrice);
            BigInteger gasLimit = new BigInteger("200000");
            BigDecimal value = Convert.toWei(valueStr, Convert.Unit.ETHER);
            Log.e("TransferPresenter", "gasLimit = " + gasLimit);
            Log.e("TransferPresenter", "run: value wei = " + value.toPlainString());
            Log.e("TransferPresenter", "toStr = "+toStr);
            String hexRemarkStr = "";

            if(!TextUtils.isEmpty(remarkStr)) {
//                    hexRemarkStr = NumericUtil.prependHexPrefix(HexUtils.convertStringToUTF8(remarkStr));
                hexRemarkStr = Numeric.toHexString(remarkStr.getBytes());
            }
            //创建交易
            RawTransaction etherTransaction = RawTransaction.createTransaction(transactionCount, gasPrice, gasLimit, toStr, value.toBigInteger(),hexRemarkStr);

//            EthereumTransaction tran = new EthereumTransaction(transactionCount, gasPrice, gasLimit, toStr, value.toBigInteger(), hexRemarkStr);
//                EthereumTransaction tran = new EthereumTransaction(transactionCount, BigInteger.valueOf(20000000000L),
//                        BigInteger.valueOf(21000), toStr, value.toBigInteger(), "0xE4B88AE6B5B7");
//            TxSignResult result = tran.signTransaction(Integer.toString(ChainId.ETHEREUM_ROPSTEN), password, BaseApplication.getCurrentWallet());
//            String signedTx = result.getSignedTx(); // This is the signature result which you need to broadcast.
//            String txHash = result.getTxHash(); // This is txHash which you can use for locating your transaction record
            Credentials credentials = WalletUtils.loadCredentials(password, BaseApplication.getCurrentWallet().getKeystorePath());
            byte[] signMessage = TransactionEncoder.signMessage(etherTransaction, credentials);

//            String transactionHash = mWeb3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).send().getTransactionHash();
//            Log.e("TransferPresenter", "signedTx = "+signedTx+",txHash = "+txHash);
//            Log.e("TransferPresenter","transactionHash = "+transactionHash);
//                Admin admin = AdminFactory.build(new HttpService(Network.ROPSTEN));
//                Request<?, EthTransaction> ethTransactionRequest = admin.ethGetTransactionByHash(txHash);
//                ethTransactionRequest.
            e.onNext(Numeric.toHexString(signMessage));
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String aBoolean) {
                        //TODO 交易成功
                        Log.e("TransferPresenter",aBoolean);
//                        ApiServiceHelper.getApiService().sendTransaction("proxy","eth_sendRawTransaction",aBoolean)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Observer<JSONObject>() {
//                                    @Override
//                                    public void onSubscribe(Disposable d) {
//
//                                    }
//
//                                    @Override
//                                    public void onNext(JSONObject jsonObject) {
//                                        ToastUtils.showToast(mContext, "交易成功！");
//                                        mView.sendEvent();
//                                        mView.hideLoading();
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//
//                                    }
//                                });

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

    /**
     * 发送账户内所有eth
     * @param from 持有地址
     * @param to 目标地址
     * @return 交易hash
     * @throws IOException
     */
    public void sendAllEth(String from, String to, String password, String remarkStr) throws IOException {
        mView.showLoading();
        Observable.create((ObservableOnSubscribe<String>) e -> {

            BigInteger nonce = mWeb3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
            BigInteger gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();
            BigInteger gasLimit = BigInteger.valueOf(21000L);
            BigInteger balance = mWeb3j.ethGetBalance(from, DefaultBlockParameterName.PENDING).send().getBalance();

            if (balance.compareTo(gasPrice.multiply(gasLimit)) <= 0) {
                e.onNext("");
                e.onComplete();
                return;
            }

            BigInteger amount = balance.subtract(gasPrice.multiply(gasLimit));
            String hexRemarkStr = "";
            if (!TextUtils.isEmpty(remarkStr)) {
                hexRemarkStr = Numeric.toHexString(remarkStr.getBytes());
            }
            RawTransaction transaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, amount, hexRemarkStr);
            Credentials credentials = WalletUtils.loadCredentials(password, BaseApplication.getCurrentWallet().getKeystorePath());
            byte[] signMessage = TransactionEncoder.signMessage(transaction, credentials);
            String transactionHash = mWeb3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).send().getTransactionHash();
            e.onNext(transactionHash);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String str) {
                        //TODO 交易成功
                        if(TextUtils.isEmpty(str)){

                        }else {
                            ToastUtils.showToast(mContext, "交易成功！");
                            mView.sendEvent();
                        }
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

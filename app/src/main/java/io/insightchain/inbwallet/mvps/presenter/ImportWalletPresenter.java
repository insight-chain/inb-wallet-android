package io.insightchain.inbwallet.mvps.presenter;

import java.util.Arrays;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.ETHWallet;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;
import io.insightchain.inbwallet.wallet.utils.WalletDaoUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImportWalletPresenter extends BasePresenter<ImportWalletView> {

    private String ethType = ETHWalletUtils.ETH_JAXX_TYPE;

    public void importWalletByMnemonic(String walletName, String mnemonicWord, String password, String passwordHint){
        mView.showLoading();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                //导入钱包时，首先知道是导入的哪种钱包，然后知道是用哪种方式导入。这里eth为例，需要一个默认钱包名称,判断同类型钱包有几个
                ETHWallet ethWallet = ETHWalletUtils.importMnemonic(walletName, ethType
                        , Arrays.asList(mnemonicWord.split(" ")), password, passwordHint);
                if (ethWallet != null) {
                    WalletDaoUtils.insertNewWallet(ethWallet);
                }
//                String defaultWalletName = "Wallet-" + (WalletManager.getWallets().size()+1);
////                Metadata metadata = new Metadata(ChainType.ETHEREUM, Network.MAINNET, defaultWalletName, passwordHint);
//                Metadata metadata = new Metadata(ChainType.ETHEREUM, Network.ROPSTEN, defaultWalletName, passwordHint);
//                Wallet wallet = WalletManager.importWalletFromMnemonic(metadata, mnemonicWord, "m/44'/60'/0'/0/0", password, false);
                BaseApplication.setCurrentWallet(ethWallet);
                e.onNext(0);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mView.hideLoading();
                        mView.sendEvent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        ToastUtils.showToast(mContext, "导入失败！");
//                        if (e.getMessage().equals(Messages.MNEMONIC_INVALID_LENGTH)){
//                            ToastUtils.showToast(mContext, "助记词长度错误！");
//                        }else if(e.getMessage().equals(Messages.MNEMONIC_BAD_WORD)){
//                            ToastUtils.showToast(mContext, "助记词错误！");
//                        }else{//Messages.MNEMONIC_CHECKSUM
//                            ToastUtils.showToast(mContext, "助记词验证错误!");
//                        }
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

    public void importWalletByPrivateKey(String walletName, String privateKey, String password, String passwordHint){
        mView.showLoading();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                ETHWallet ethWallet = ETHWalletUtils.loadWalletByPrivateKey(walletName, privateKey, password, passwordHint);
                if (ethWallet != null) {
                    WalletDaoUtils.insertNewWallet(ethWallet);
                }
                //导入钱包时，首先知道是导入的哪种钱包，然后知道是用哪种方式导入。这里eth为例，需要一个默认钱包名称,判断同类型钱包有几个
//                String defaultWalletName = "Wallet-" + (WalletManager.getWallets().size()+1);
////                Metadata metadata = new Metadata(ChainType.ETHEREUM, Network.MAINNET, defaultWalletName, passwordHint);
//                Metadata metadata = new Metadata(ChainType.ETHEREUM, Network.ROPSTEN, defaultWalletName, passwordHint);
//                Wallet wallet = WalletManager.importWalletFromPrivateKey(metadata,  privateKey, password, false);
                BaseApplication.setCurrentWallet(ethWallet);
                e.onNext(0);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Integer o) {
                        mView.hideLoading();
                        mView.sendEvent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        e.printStackTrace();
                        ToastUtils.showToast(mContext,e.getMessage());
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

}

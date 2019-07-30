package io.insightchain.inbwallet.mvps.presenter;

import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.mvps.http.ApiServiceHelper;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CheckNotePresenter extends BasePresenter<CheckNoteView> {

    public void checkNote(String address){

        ApiServiceHelper.getApiService("http://192.168.1.191:8383/").checkNode(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NodeVo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(NodeVo nodeVo) {
                        if(nodeVo != null && nodeVo.getId() !=null){
                            BaseApplication.getCurrentWalletVo().setNote(true);
                            BaseApplication.getCurrentWalletVo().setNodeVo(nodeVo);
                        }else{
                            BaseApplication.getCurrentWalletVo().setNote(false);
                            BaseApplication.getCurrentWalletVo().setNodeVo(null);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showToast(mContext, "异常");
                        BaseApplication.getCurrentWalletVo().setNote(false);
                        BaseApplication.getCurrentWalletVo().setNodeVo(null);
//                        mView.checkNoteResult(false, null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

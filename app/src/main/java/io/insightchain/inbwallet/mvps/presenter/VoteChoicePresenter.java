package io.insightchain.inbwallet.mvps.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.mvp.BasePresenter;
import io.insightchain.inbwallet.mvps.http.ApiServiceHelper;
import io.insightchain.inbwallet.mvps.model.vo.NodeResultVo;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;
import io.insightchain.inbwallet.mvps.view.activity.VoteActivity;
import io.insightchain.inbwallet.mvps.view.adapter.VoteConfirmAdapter;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VoteChoicePresenter extends BasePresenter<VoteChoiceView> {

    CustomPopupWindow popupWindow;

    public void getNodeList(){
        ApiServiceHelper.getApiService("http://192.168.1.191:8383/").getNodeList(1,200)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NodeResultVo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(NodeResultVo nodeResultVo) {
                        Log.e(TAG,nodeResultVo.toString());
                        if(nodeResultVo!=null && nodeResultVo.getItems()!=null){
                            mView.showList(nodeResultVo.getItems());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"异常");
                        ToastUtils.showToast(mContext,"获取数据异常");
                        mView.showList(null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showSelectedPop(ArrayList<NodeVo> selectedList){
        if(popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }else{
            popupWindow = new CustomPopupWindow(mContext, R.layout.layout_popup_node_select);
            RecyclerView recyclerView = popupWindow.getView().findViewById(R.id.recycler_view_node_select);
            initRecyclerView(recyclerView);
            VoteConfirmAdapter adapter = new VoteConfirmAdapter(mContext, selectedList, recyclerView);
            recyclerView.setAdapter(adapter);
            popupWindow.setSingleClickListener(new int[]{R.id.tv_clear_node, R.id.view_other_popup_node_select,
                    R.id.rl_show_selected_layout_popup_node_select, R.id.view_confirm_vote_layout_popup_node_select}, v12 -> {
                switch (v12.getId()) {
                    case R.id.view_other_popup_node_select:
                        popupWindow.dismiss();
                        break;
                    case R.id.tv_clear_node:
                        //清空recyclerView
//                        selectedList.clear();

                        mView.updateSelectedList(null);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.view_confirm_vote_layout_popup_node_select:
                        jumpToVoteConfirm(selectedList);
                        popupWindow.dismiss();
                        break;
                    case R.id.rl_show_selected_layout_popup_node_select:
                        popupWindow.dismiss();
                        break;
                }
            });
//                }
            adapter.setOnClickListener(position -> {
                selectedList.remove(position);
                mView.updateSelectedList(selectedList);
                adapter.notifyDataSetChanged();
            });
            mView.showPopwindow(popupWindow);
        }

    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    public void jumpToVoteConfirm(ArrayList<NodeVo> selectedList){
        Intent intent = new Intent(mContext, VoteActivity.class);
        intent.putExtra("voteConfirm",1);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("selectedList",selectedList);
        intent.putExtra("bundle", bundle);
        mContext.startActivity(intent);
    }
}

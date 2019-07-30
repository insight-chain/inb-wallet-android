package io.insightchain.inbwallet.mvps.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.TransactionResultVo;
import io.insightchain.inbwallet.mvps.presenter.TransactionRecordPresenter;
import io.insightchain.inbwallet.mvps.presenter.TransactionRecordView;
import io.insightchain.inbwallet.mvps.view.adapter.TransactionRecordAdapter;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.InterfaceUtils;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = TransactionRecordPresenter.class)
public class TransactionRecordActivity extends BaseMvpActivity implements TransactionRecordView {

    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    @BindView(R.id.recycle_view_transaction_record_activity)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_frame_activity_transaction_record)
    PtrClassicFrameLayout ptrFrame;

    @PresenterVariable
    TransactionRecordPresenter presenter;

    private TransactionRecordAdapter recordAdapter;
    private int pageNum = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_record;
    }

    @Override
    public void initView() {
        initTopBar();
        initRecyclerView(recyclerView);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNum = 1;
                presenter.getTransactionRecord(pageNum);
                frame.refreshComplete();
            }
        });
        recordAdapter = new TransactionRecordAdapter(context,transactionResultVos,recyclerView);
        recyclerView.setAdapter(recordAdapter);
        recordAdapter.setOnClickListener(position -> {
            Intent intent = new Intent(context,TransactionDetailActivity.class);
            intent.putExtra("resultVo",transactionResultVos.get(position));
            startActivity(intent);
        });
        recordAdapter.setOnLoadMoreListener(new InterfaceUtils.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNum++;
                presenter.getTransactionRecord(pageNum);
            }
        });
        presenter.getTransactionRecord(pageNum);
        presenter.getTransactionResult();
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.transaction_history));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.top_bar_right_layout.setVisibility(View.INVISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
    }

    public void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    @OnClick({R.id.top_bar_left_layout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
            default:
                break;
        }
    }

    private List<TransactionResultVo> transactionResultVos = new ArrayList<>();
    @Override
    public void getList(List<TransactionResultVo> results) {
        Log.e(TAG, "getList");
        if(pageNum == 1){
            transactionResultVos.clear();
        }
        transactionResultVos.addAll(results);
        recordAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLastLine() {
        ToastUtils.showToast(context,"已是最后一页");
    }
}

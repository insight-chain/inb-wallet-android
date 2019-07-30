package io.insightchain.inbwallet.mvps.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.mvps.view.fragment.VoteChoiceFragment;
import io.insightchain.inbwallet.mvps.view.fragment.VoteConfirmFragment;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;

public class VoteActivity extends BaseMvpActivity {

    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    @BindView(R.id.container_activity_vote)
    FrameLayout containerLayout;

    VoteChoiceFragment voteChoiceFragment;
    VoteConfirmFragment voteConfirmFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vote;
    }

    @Override
    public void initView() {
        initTopBar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int voteConfirm = getIntent().getIntExtra("voteConfirm",-1);
        if(voteConfirm == -1){
            voteChoiceFragment = VoteChoiceFragment.newInstanse();
            transaction.replace(R.id.container_activity_vote, voteChoiceFragment);
        }else{
            voteConfirmFragment = VoteConfirmFragment.newInstanse();
            voteConfirmFragment.setArguments(getIntent().getBundleExtra("bundle"));
            transaction.replace(R.id.container_activity_vote, voteConfirmFragment);
        }
        transaction.commit();
//        EventBus.getDefault().register(this);
//        hasBus = true;
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.vote_wallet_fragment));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
    }

    @OnClick({R.id.top_bar_left_layout})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.top_bar_left_layout:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void changeFragment(SimpleMessageEvent event){
////        Log.e(TAG,"changeFragment");
//        if(!event.getTarget().equals(TAG))
//            return;
////        Log.e(TAG,"收到点击");
//
//
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("addressList",(ArrayList<String>) event.getObj());
//        voteConfirmFragment.setArguments(bundle);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container_activity_vote, voteConfirmFragment);
//        transaction.commit();
//    }


}

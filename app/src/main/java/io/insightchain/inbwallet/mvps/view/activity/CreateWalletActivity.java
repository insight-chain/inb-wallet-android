package io.insightchain.inbwallet.mvps.view.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.view.fragment.BacksUpHintFragment;
import io.insightchain.inbwallet.mvps.view.fragment.CreateWalletFragment;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;

public class CreateWalletActivity extends BaseMvpActivity {
    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    CreateWalletFragment createWalletFragment;
    BacksUpHintFragment backsUpHintFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_wallet;
    }

    @Override
    public void initView() {
        initTopBar();
        createWalletFragment = CreateWalletFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container_activity_create_wallet, createWalletFragment);
        transaction.commit();
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.create_wallet));
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.top_bar_left_layout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!hasBus) {
            EventBus.getDefault().register(this);
            hasBus = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeFragment(SimpleMessageEvent event){
        if(!event.getTarget().equals(TAG))
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if ((int)event.getObj() == 0) {
            backsUpHintFragment = BacksUpHintFragment.newInstance();
            transaction.replace(R.id.fl_container_activity_create_wallet, backsUpHintFragment);
            transaction.commit();
        }else if((int)event.getObj() == 1){
            Intent intent = new Intent(context, BacksUpWhenCreateActivity.class);
            startActivity(intent);
        }
    }
}

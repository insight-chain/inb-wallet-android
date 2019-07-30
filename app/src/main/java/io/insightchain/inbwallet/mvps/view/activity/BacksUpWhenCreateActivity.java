package io.insightchain.inbwallet.mvps.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.view.fragment.BacksUpMnemonicFragment;
import io.insightchain.inbwallet.mvps.view.fragment.BacksUpShowFragment;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;

public class BacksUpWhenCreateActivity extends BaseMvpActivity {
    @BindView(R.id.topbar_common)
    TopBarCommon topBar;

    @BindView(R.id.fl_container)
    FrameLayout container;

    BacksUpMnemonicFragment backsUpMnemonicFragment;
    BacksUpShowFragment backsUpShowFragment;

//    @BindView(R.id.root_view_activity_backs_up_when_create)
//    View mRootView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_backs_up_when_create;
    }

    @Override
    public void initView() {
        initTopBar();
        backsUpShowFragment = BacksUpShowFragment.newInstanse();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, backsUpShowFragment);
        transaction.commit();


    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.backs_up_wallet));
        topBar.top_bar_left_layout.setVisibility(View.INVISIBLE);
//        topBar.setLeftView(null, R.mipmap.icon_back_arrow);
    }

    @Override
    protected void onResume() {
        super.onResume();

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

            backsUpShowFragment = BacksUpShowFragment.newInstanse();
            transaction.replace(R.id.fl_container, backsUpShowFragment);
            transaction.commit();
        }else if((int)event.getObj() == 1){
            backsUpMnemonicFragment = BacksUpMnemonicFragment.newInstanse();
            transaction.replace(R.id.fl_container, backsUpMnemonicFragment);
            transaction.commit();
        }


    }


}

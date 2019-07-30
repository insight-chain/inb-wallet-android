package io.insightchain.inbwallet.mvps.view.fragment;

import android.view.View;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;

public class BacksUpHintFragment extends BaseMvpFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_backs_up_hint;
    }

    @Override
    protected void init() {

    }

    @OnClick({R.id.btn_backs_up})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_backs_up:
                SimpleMessageEvent event = new SimpleMessageEvent();
                event.setObj(1);
                event.setTarget("CreateWalletActivity");
                EventBus.getDefault().post(event);
                break;
        }
    }

    public static BacksUpHintFragment newInstance(){
//        BacksUpHintFragment fragment = new BacksUpHintFragment();
        return new BacksUpHintFragment();
    }
}

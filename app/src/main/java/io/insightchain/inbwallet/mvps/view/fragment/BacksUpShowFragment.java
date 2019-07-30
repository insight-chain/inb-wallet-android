package io.insightchain.inbwallet.mvps.view.fragment;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.view.adapter.ShowGridViewAdapter;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;

public class BacksUpShowFragment extends BaseMvpFragment {

    @BindView(R.id.gv_fragment_backs_up_show)
    GridView showGridView;
    @BindView(R.id.tv_wallet_private_key)
    TextView privateKeyText;

    ShowGridViewAdapter showGridViewAdapter;
    List<String> mnemonicList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_backs_up_show;
    }

    @Override
    protected void init() {
        mnemonicList = Arrays.asList(BaseApplication.getCurrentWallet().getMnemonic().split(" "));
        privateKeyText.setText(ETHWalletUtils.getPrivateKey());
        showGridViewAdapter = new ShowGridViewAdapter(mContext, mnemonicList);
        showGridView.setAdapter(showGridViewAdapter);
    }

    public static BacksUpShowFragment newInstanse(){
        BacksUpShowFragment fragment = new BacksUpShowFragment();
        return fragment;
    }

    @OnClick({R.id.btn_next_step})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_next_step:
                SimpleMessageEvent event = new SimpleMessageEvent();
                event.setObj(1);
                event.setTarget("BacksUpWhenCreateActivity");
                EventBus.getDefault().post(event);
                break;
        }
    }

}

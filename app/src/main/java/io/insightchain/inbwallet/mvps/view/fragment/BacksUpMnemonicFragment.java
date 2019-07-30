package io.insightchain.inbwallet.mvps.view.fragment;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.view.activity.MainActivity;
import io.insightchain.inbwallet.mvps.view.adapter.HoldGridViewAdapter;
import io.insightchain.inbwallet.mvps.view.adapter.SelectedGridViewAdapter;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.ETHWalletUtils;

public class BacksUpMnemonicFragment extends BaseMvpFragment {

    @BindView(R.id.gv_selected_fragment_backs_up_mnemonic)
    GridView selectedGridView;
    @BindView(R.id.gv_hold_fragment_backs_up_mnemonic)
    GridView holdGridView;
    @BindView(R.id.btn_done_fragment_backs_up_mnemonic)
    TextView doneButton;

    HoldGridViewAdapter holdGridViewAdapter;
    SelectedGridViewAdapter selectedGridViewAdapter;
    List<String> mnemonicWordList;
    List<String> selectedWordList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_backs_up_mnemonic;
    }

    @Override
    protected void init() {
        mnemonicWordList = new ArrayList<>();
        mnemonicWordList.addAll(Arrays.asList(BaseApplication.getCurrentWallet().getMnemonic().split(" ")));
        Collections.shuffle(mnemonicWordList);
        selectedWordList = new ArrayList<>();
        holdGridViewAdapter = new HoldGridViewAdapter(mContext, mnemonicWordList);

        holdGridView.setAdapter(holdGridViewAdapter);
        selectedGridViewAdapter = new SelectedGridViewAdapter(mContext, selectedWordList);
        selectedGridView.setAdapter(selectedGridViewAdapter);
        holdGridViewAdapter.setOnCheckedListener(new HoldGridViewAdapter.OnCheckedListener() {
            @Override
            public void onChecked(int position, boolean isChecked) {
                if(isChecked) {
                    selectedWordList.add(mnemonicWordList.get(position));
                    selectedGridViewAdapter.notifyDataSetChanged();
                    if(selectedWordList.size() == mnemonicWordList.size()){
                        doneButton.setEnabled(true);
                    }
                }
            }
        });

        selectedGridViewAdapter.setOnClickListener(new SelectedGridViewAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                holdGridViewAdapter.setCheckBoxEnable(mnemonicWordList.indexOf(selectedWordList.get(position)));
                selectedWordList.remove(position);
                selectedGridViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public static BacksUpMnemonicFragment newInstanse(){
        BacksUpMnemonicFragment fragment = new BacksUpMnemonicFragment();
        return fragment;
    }

    private String TAG = getClass().getSimpleName();
    @OnClick({R.id.btn_done_fragment_backs_up_mnemonic})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_done_fragment_backs_up_mnemonic:
                //验证助记次

                if(equalsList(selectedWordList,Arrays.asList(BaseApplication.getCurrentWallet().getMnemonic().split(" ")))){
                    Log.e(TAG,"order is right");
                }else{
                    //顺序不正确
                    Log.e(TAG,"order is wrong");
                    ToastUtils.showToast(mContext,"助记词顺序错误，请重新确认!");
                    return;
                }
                ETHWalletUtils.clear();
                SimpleMessageEvent event = new SimpleMessageEvent();
                event.setTarget("MainActivity");
                event.setObj(0);
                EventBus.getDefault().post(event);
                startActivity(MainActivity.class);
                finish();
                break;
        }
    }

    private String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(" ");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    private boolean equalsList(List<String> firstList, List<String> secondList){
        return listToString(firstList).equals(listToString(secondList));
    }
}

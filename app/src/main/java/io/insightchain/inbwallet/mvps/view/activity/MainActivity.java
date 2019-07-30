package io.insightchain.inbwallet.mvps.view.activity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.presenter.SettingPresenter;
import io.insightchain.inbwallet.mvps.presenter.SettingView;
import io.insightchain.inbwallet.mvps.view.fragment.SettingFragment;
import io.insightchain.inbwallet.mvps.view.fragment.WalletFragment;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.mvps.view.widget.FragmentTabHost;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.utils.WalletDaoUtils;


@CreatePresenter(presenter = SettingPresenter.class)
public class MainActivity extends BaseMvpActivity implements SettingView {

    @PresenterVariable
    SettingPresenter presenter;

    private FragmentTabHost mTabHost;
    private RelativeLayout rootView;
    private Class<?> mTabFragmentArray[] ;
    private int mTabTextArray[] = {R.string.wallet ,/*R.string.discovery,*/R.string.setting};
    private int mTabTagArray[] = {R.string.wallet_tag, /*R.string.discovery_tag,*/ R.string.mine_tag};
    private int mTabImageArray[] = {R.drawable.selector_tab_view_main_wallet, /*R.drawable.selector_tab_view_main_discovery,*/ R.drawable.selector_tab_view_main_setting};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        mTabFragmentArray = new Class[]{WalletFragment.class, /*DiscoveryFragment.class, */SettingFragment.class};

        initTabHost();
        WalletDaoUtils.loadAll();
//        presenter.checkVersion();
    }


    private void initTabHost() {
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.container);
        int count = mTabFragmentArray.length;
        for (int i = 0; i < count; i++) {
            View tabView = getTabItemView(i);
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(mTabTagArray[i])).setIndicator(tabView);
            mTabHost.addTab(tabSpec, mTabFragmentArray[i], null);
        }
        TabWidget tabWidget = mTabHost.getTabWidget();
        tabWidget.setDividerDrawable(null);
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            tabWidget.getChildAt(i).setOnClickListener(new OnHomeClickListener(i));
        }
        this.mTabHost.setCurrentTab(mCurrentTabIndex);

    }

    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab_view_main, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_layout_tab_view_main);
        TextView textView = (TextView) view.findViewById(R.id.tv_layout_tab_view_main);
        imageView.setImageResource(mTabImageArray[index]);
        textView.setText(mTabTextArray[index]);
        return view;
    }

    class OnHomeClickListener implements View.OnClickListener {
        private int tabId = 0;

        OnHomeClickListener(int tabId) {
            this.tabId = tabId;
        }

        @Override
        public void onClick(View v) {
            Log.e("onHomeClick", "tagId=" + tabId);
            switch (tabId) {
//                case TAB_INDEX_MINE:
//                    break;
            }
            mTabHost.setCurrentTab(tabId);
            mCurrentTabIndex = tabId;

        }
    }

    private static final int TAB_INDEX_WALLET = 0;
    private static final int TAB_INDEX_SETTING = 1;
    private int mCurrentTabIndex = TAB_INDEX_WALLET;

    long mLatestPressBackTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLatestPressBackTime < 1500) {
            finish();
            System.exit(0);
        } else {
            mLatestPressBackTime = System.currentTimeMillis();
            Toast.makeText(this, getResources().getString(R.string.again_press_logout), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(!hasBus) {
//            EventBus.getDefault().register(this);
//            hasBus = true;
//        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void changeFragment(SimpleMessageEvent event){
//        Log.e(TAG,"changeFragment");
//        if(!event.getTarget().equals(TAG))
//            return;
//        if((int)event.getObj() == 0) {
//
//        }
//    }

    @Override
    public void showNewestToast() {
        ToastUtils.showToast(context, "当前已是最新版本");
    }

    @Override
    public void showPopWindow(CustomPopupWindow popupWindow) {
        popupWindow.showAtLocation(rootView, Gravity.CENTER,0,0);
    }
}

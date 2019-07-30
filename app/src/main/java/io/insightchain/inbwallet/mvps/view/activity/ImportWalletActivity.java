package io.insightchain.inbwallet.mvps.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.mvps.view.adapter.CommonFragmentAdapter;
import io.insightchain.inbwallet.mvps.view.fragment.KeystoreImportFragment;
import io.insightchain.inbwallet.mvps.view.fragment.MnemonicWordImportFragment;
import io.insightchain.inbwallet.mvps.view.fragment.PrivateKeyImportFragment;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.mvps.view.widget.smarttablayout.SmartTabLayout;

public class ImportWalletActivity extends BaseMvpActivity {

    @BindView(R.id.tab_layout_import_wallet_activity)
    SmartTabLayout tabLayout;
    @BindView(R.id.viewpager_import_wallet_activity)
    ViewPager viewPager;
    @BindView(R.id.topbar_common)
    TopBarCommon topBar;

    List<Fragment> mFragmentList = new ArrayList<>();
    List<String> mFragmentTitleList = new ArrayList<>();
    CommonFragmentAdapter mCommonFragmentAdapter;
    PrivateKeyImportFragment mPrivateKeyImportFra;
    MnemonicWordImportFragment mMnemonicWordImportFra;
    KeystoreImportFragment keystoreImportFra;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_wallet;
    }

    @Override
    public void initView() {
        initTopBar();
        initFragmentPager();

    }

    private void initTopBar(){
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
//        topBar.top_bar_right_layout.setVisibility(View.VISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
//        topBar.setRightView(null,R.mipmap.icon_scan_topbar);
        topBar.setTitle("");
    }

    private void initFragmentPager(){
        mPrivateKeyImportFra = PrivateKeyImportFragment.newInstance();
        mMnemonicWordImportFra = MnemonicWordImportFragment.newInstance();
//        keystoreImportFra = KeystoreImportFragment.newInstance();
        mFragmentList.add(mPrivateKeyImportFra);
        mFragmentList.add(mMnemonicWordImportFra);
//        mFragmentList.add(keystoreImportFra);
        mFragmentTitleList.add(getString(R.string.private_key));
        mFragmentTitleList.add(getString(R.string.mnemonic_word));
//        mFragmentTitleList.add("keystore");
        mCommonFragmentAdapter = new CommonFragmentAdapter(getSupportFragmentManager(), mFragmentList, mFragmentTitleList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(mCommonFragmentAdapter);
        tabLayout.setCustomTabView(R.layout.smartlayout_text_layout, R.id.custom_text);
        tabLayout.setViewPager(viewPager);
        tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                if(i==1){
//                    topBar.top_bar_right_layout.setVisibility(View.GONE);
//
//                }else{
//                    topBar.top_bar_right_layout.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @OnClick({R.id.top_bar_left_layout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
            default:
                break;
        }
    }
}

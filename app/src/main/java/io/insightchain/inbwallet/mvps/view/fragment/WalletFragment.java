package io.insightchain.inbwallet.mvps.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.common.Constants;
import io.insightchain.inbwallet.mvps.model.vo.SimpleMessageEvent;
import io.insightchain.inbwallet.mvps.presenter.CheckNotePresenter;
import io.insightchain.inbwallet.mvps.presenter.CheckNoteView;
import io.insightchain.inbwallet.mvps.presenter.PasswordPopPresenter;
import io.insightchain.inbwallet.mvps.presenter.PasswordPopView;
import io.insightchain.inbwallet.mvps.presenter.VerifyPasswordPresenter;
import io.insightchain.inbwallet.mvps.presenter.VerifyPasswordView;
import io.insightchain.inbwallet.mvps.presenter.WalletPresenter;
import io.insightchain.inbwallet.mvps.presenter.WalletView;
import io.insightchain.inbwallet.mvps.view.activity.BacksUpWalletActivity;
import io.insightchain.inbwallet.mvps.view.activity.CreateWalletActivity;
import io.insightchain.inbwallet.mvps.view.activity.ImportWalletActivity;
import io.insightchain.inbwallet.mvps.view.activity.ReceiveCoinsActivity;
import io.insightchain.inbwallet.mvps.view.activity.ResourceActivity;
import io.insightchain.inbwallet.mvps.view.activity.TransactionRecordActivity;
import io.insightchain.inbwallet.mvps.view.activity.TransferAccountActivity;
import io.insightchain.inbwallet.mvps.view.activity.UpdateNodeActivity;
import io.insightchain.inbwallet.mvps.view.activity.VoteActivity;
import io.insightchain.inbwallet.mvps.view.activity.WalletDetailActivity;
import io.insightchain.inbwallet.mvps.view.adapter.AddAccountPopRecyclerAdapter;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.qrcode.CaptureActivity;
import io.insightchain.inbwallet.qrcode.Intents;
import io.insightchain.inbwallet.utils.PermissionsUtil;
import io.insightchain.inbwallet.utils.ToastUtils;
import io.insightchain.inbwallet.wallet.ETHWallet;
import io.insightchain.inbwallet.wallet.utils.WalletDaoUtils;

@CreatePresenter(presenter = {WalletPresenter.class, VerifyPasswordPresenter.class, PasswordPopPresenter.class, CheckNotePresenter.class})
public class WalletFragment extends BaseMvpFragment implements WalletView, VerifyPasswordView, PasswordPopView, CheckNoteView {

    @BindView(R.id.tv_name_fragment_wallet)
    TextView walletNameText;
    @BindView(R.id.tv_coins_number)
    TextView coinsNumberText;
    @BindView(R.id.tv_rmb_number)
    TextView rmbNumberText;
    @BindView(R.id.cb_see_status)
    CheckBox eyesCheckBox;
    @BindView(R.id.tv_left_NET)
    TextView leftNETText;
    @BindView(R.id.tv_total_NET)
    TextView totalNETText;
    @BindView(R.id.view_blue_progress)
    View blueProgressView;
    @BindView(R.id.view_gray_progress)
    View grayProgressView;
    @BindView(R.id.tv_pawn_number)
    TextView mortgageInbText;

    @BindView(R.id.ll_wallet_layout)
    LinearLayout walletLayout;
    @BindView(R.id.ll_welcome_layout)
    LinearLayout welcomeLayout;
    @BindView(R.id.ptr_frame_fragment_wallet)
    PtrClassicFrameLayout ptrFrameLayout;
    @BindView(R.id.scroll_view_fragment_wallet)
    ScrollView scrollView;

    CustomPopupWindow popupWindow;
    private boolean hasWallet = false;
//    private float ratio = 2.12f;
    @PresenterVariable
    WalletPresenter walletPresenter;
    @PresenterVariable
    VerifyPasswordPresenter verifyPasswordPresenter;
    @PresenterVariable
    PasswordPopPresenter passwordPopPresenter;
    @PresenterVariable
    CheckNotePresenter checkNotePresenter;

    private String rmbNumber;
    private String coinsNumber;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected void init() {
        hasWallet = WalletDaoUtils.hasWallet;
        setLayout();
        EventBus.getDefault().register(this);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                setLayout();
                frame.refreshComplete();
            }
        });
        eyesCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            coinsNumberText.setText(isChecked? coinsNumber:"****");
            rmbNumberText.setText(isChecked? rmbNumber:"****");
        });
        walletPresenter.requestStoragePermission(getActivity());


    }

//    public static final int REQUEST_SCAN_QRCODE = 0X11;
//    String[] persions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @OnClick({R.id.rl_wallet_detail_layout, R.id.ll_transfer_accounts_layout, R.id.ll_scan_code_layout,
            R.id.ll_transaction_record_layout, R.id.ll_vote_layout, R.id.ll_backups_layout,
            R.id.ll_receive_money_layout, R.id.rl_cpu_layout, R.id.iv_select_account, R.id.tv_name_fragment_wallet,
            R.id.ll_create_wallet_layout, R.id.ll_import_wallet_layout, R.id.tv_update_node_information})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_wallet_detail_layout:
                startActivity(WalletDetailActivity.class);
                break;
            case R.id.ll_transfer_accounts_layout:
                startActivity(TransferAccountActivity.class);
                break;
            case R.id.ll_receive_money_layout:
                startActivity(ReceiveCoinsActivity.class);
                break;
            case R.id.ll_backups_layout:
                passwordPopPresenter.showPasswordPop(0);
//                //弹窗输入密码
////                if(popupWindow==null) {
//                    popupWindow = new CustomPopupWindow(mContext, R.layout.layout_popup_input_password);
//                    EditText passwordEditText = popupWindow.getView().findViewById(R.id.et_password_popup_input_password);
//                    popupWindow.setSingleClickListener(new int[]{R.id.btn_confirm_popup_input_password, R.id.iv_close_popup},
//                            v1 -> {
//                                switch (v1.getId()){
//                                    case R.id.btn_confirm_popup_input_password:
//                                        verifyPasswordPresenter.verifyPassword(passwordEditText.getText().toString().trim());
//                                        break;
//                                    case R.id.iv_close_popup:
//                                        popupWindow.dismiss();
//                                        break;
//                                }
//                            });
//                    popupWindow.setFocusable(true);
//                }

                break;
            case R.id.ll_transaction_record_layout:
                startActivity(TransactionRecordActivity.class);
                break;
            case R.id.ll_vote_layout:
//                List<String> addressList = new ArrayList<>();
//                addressList.add("07a822194f7a55f822e49ba9e7161d4800aa9639");
//                votePresenter.sendVote(addressList,"nini1234");
                startActivity(VoteActivity.class);
                break;
            case R.id.rl_cpu_layout:
                startActivity(ResourceActivity.class);
                break;
            case R.id.iv_select_account:
            case R.id.tv_name_fragment_wallet:
//                if(popupWindow==null) {
                    popupWindow = new CustomPopupWindow(mContext, R.layout.layout_popup_add_account);

                    popupWindow.setSingleClickListener(new int[]{R.id.iv_retraction_view_add_account, R.id.btn_add_account_popup_window, R.id.view_popup_add_account}, v12 -> {
                        switch (v12.getId()){
                            case R.id.iv_retraction_view_add_account:
                            case R.id.view_popup_add_account:
                                popupWindow.dismiss();
                                break;
                            case R.id.btn_add_account_popup_window:
                                ToastUtils.showToast(mContext, "增加账号");
                                startActivity(ImportWalletActivity.class);
                                popupWindow.dismiss();
                                break;
                        }
                    });
//                }
                RecyclerView recyclerView = popupWindow.getView().findViewById(R.id.recycler_view_add_account);
                initRecyclerView(recyclerView);
                AddAccountPopRecyclerAdapter recyclerAdapter = new AddAccountPopRecyclerAdapter(mContext,walletList,recyclerView);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.setOnClickListener((v13, position) -> {
                    if(!walletList.get(position).getId().equals(currentWallet.getId())){
                        currentWallet = walletList.get(position);
                        setCurrentWallet();

                        walletPresenter.updateBalance(BaseApplication.getCurrentWalletVo().getAddress());
                        checkNotePresenter.checkNote(BaseApplication.getCurrentWalletVo().getAddress());
//                        walletPresenter.getAccountInfo(BaseApplication.getCurrentWalletVo().getAddress());
                        recyclerAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(mRootView,Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_scan_code_layout:
//                if (CommonUtils.checkSelfPermissions(getActivity(), persions, 103)) {
                if (!PermissionsUtil.getInstance(mContext).hasPermission(Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, PermissionsUtil.CAMERA_PERMISSION_CODE);
                }else {
                    Intent scanIntent = new Intent(getContext(), CaptureActivity.class);
                    scanIntent.setAction(Intents.Scan.ACTION);
                    startActivity(scanIntent);
                }
//                    startActivityForResult(scanIntent, REQUEST_SCAN_QRCODE);
//                }
//                String content = "inb:1:event:declare:e486016a2a5f701464252f6c9edabc4ef47f5ebe20bc6682c8d91f96300867a827155bea289de308273c6b763dad7bbdef5dd0df32829b049597a37210c2deb9" +
//                        "~192.168.1.107" +
//                        "~30002" +
//                        "~安卓测试1" +
//                        "~China" +
//                        "~北京" +
//                        "~https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562835098717&di=4980c41e32b1c83f7c982699f1d00f14&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D319190465%2C3717476812%26fm%3D214%26gp%3D0.jpg" +
//                        "~www.baid222u.com" +
//                        "~ghyin22sight@insig222ht.io" +
//                        "~name/lgggg-age/200-jobs/bug-describe/这是一首无聊的小情歌，啦啦啦啦啦啦啦啦";
//                superNodePresenter.registerSuperNode(content,"nini1234");
                break;
            case R.id.ll_create_wallet_layout:
                startActivity(CreateWalletActivity.class);
                break;
            case R.id.ll_import_wallet_layout:
                startActivity(ImportWalletActivity.class);
                break;
            case R.id.tv_update_node_information:
                //判断是否有足够的抵押
                if(new BigDecimal(BaseApplication.getCurrentWalletVo().getBalance()).
                        compareTo(new BigDecimal(Constants.MORTGAGE_LIMIT)) < 0){
                    ToastUtils.showToast(mContext, "你的抵押数量不够，无法申请成为节点！");
                    return;
                }
                startActivity(UpdateNodeActivity.class);
                break;
            default:
                break;
        }

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
////        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
////        if (intentResult != null) {
////            if (intentResult.getContents() == null) {
////
////            } else {
////                // ScanResult 为获取到的字符串
////                String ScanResult = intentResult.getContents();
//////                tvResult.setText(ScanResult);
////            }
////        } else {
//            super.onActivityResult(requestCode, resultCode, data);
////        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.e("walletFragment", "requestCode = "+requestCode);
//        switch (requestCode) {
//            case 101:
////                Intent scanIntent = new Intent(getContext(), CaptureActivity.class);
////                scanIntent.setAction(Intents.Scan.ACTION);
////                startActivityForResult(scanIntent, REQUEST_SCAN_QRCODE);
//                break;
//
//            case 103:
//                for (int i = 0; i < grantResults.length; i++) {
//                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//
//                        Log.e("WalletFragment", " grantResults["+i+"]="+ grantResults[i]);
//                    }
//                }
//                break;
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeFragment(SimpleMessageEvent event){
        Log.e("walletFragment",event.getTarget());
        if(!event.getTarget().equals("MainActivity"))
            return;
        if((int)event.getObj() == 0) {
            hasWallet = true;
            setLayout();
        }
    }

    private void setLayout(){
        if(hasWallet){
            welcomeLayout.setVisibility(View.GONE);
            walletLayout.setVisibility(View.VISIBLE);
            initView();
        }else{
            welcomeLayout.setVisibility(View.VISIBLE);
            walletLayout.setVisibility(View.GONE);
        }
    }

    private List<ETHWallet> walletList;
    private ETHWallet currentWallet;

    private void initView(){
        walletList = WalletDaoUtils.getEthWallets();
        if(BaseApplication.getCurrentWalletID()!= -1L){
            try {
                currentWallet = WalletDaoUtils.updateCurrent(BaseApplication.getCurrentWalletID());
//                currentWallet = WalletManager.mustFindWalletById(BaseApplication.getCurrentWalletID());
            }catch (Exception e){
                currentWallet = walletList.get(0);
            }
        }else {
            currentWallet = walletList.get(0);
        }
        setCurrentWallet();
        walletPresenter.updateBalance(BaseApplication.getCurrentWalletVo().getAddress());
        checkNotePresenter.checkNote(BaseApplication.getCurrentWalletVo().getAddress());
    }

    private void setCurrentWallet(){
        BaseApplication.setCurrentWallet(currentWallet);
        walletNameText.setText(currentWallet.getName());
//        walletNameText.setText(BaseApplication.getCurrentWalletVo().getAddress());
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void showBalance(String balance) {
        coinsNumber = balance;
//        rmbNumber = rmbStr;
        coinsNumberText.setText(eyesCheckBox.isChecked()? coinsNumber:"****");
//        rmbNumberText.setText(eyesCheckBox.isChecked()? rmbNumber:"****");
    }

    @Override
    public void showRMBBalance(String rmbStr) {
        rmbNumber = rmbStr;
        rmbNumberText.setText(eyesCheckBox.isChecked()? rmbNumber:"****");
    }

    @Override
    public void showZeroBalance() {
        coinsNumberText.setText(eyesCheckBox.isChecked()? "0.0000":"****");
        rmbNumberText.setText(eyesCheckBox.isChecked()? "0.0000":"****");
    }

    @Override
    public void passwordRight(String password,int type) {

        Intent intent = new Intent(getContext(),BacksUpWalletActivity.class);
        intent.putExtra("password",password);
        startActivity(intent);

    }

    @Override
    public void passwordWrong() {
        ToastUtils.showToast(mContext, "密码不正确");
    }

    @Override
    public void passwordEmpty() {
        ToastUtils.showToast(mContext, "请输入密码");
    }

    @Override
    public void showResource(String netCurrent, String netTotal, String mortgageInbNumber) {
        leftNETText.setText(netCurrent);
        totalNETText.setText(netTotal);
        mortgageInbText.setText(mortgageInbNumber+"INB");
    }

    @Override
    public void showPasswordPopWindow(CustomPopupWindow popupWindow) {
        popupWindow.showAtLocation(mRootView,Gravity.CENTER, 0, 0);
    }

    @Override
    public void confirmClicked(String passwordText,int type) {
        verifyPasswordPresenter.verifyPassword(passwordText,type);
    }

}

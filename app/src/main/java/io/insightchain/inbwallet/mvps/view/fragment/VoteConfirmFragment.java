package io.insightchain.inbwallet.mvps.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;
import io.insightchain.inbwallet.mvps.presenter.PasswordPopPresenter;
import io.insightchain.inbwallet.mvps.presenter.PasswordPopView;
import io.insightchain.inbwallet.mvps.presenter.ResourcePresenter;
import io.insightchain.inbwallet.mvps.presenter.ResourceView;
import io.insightchain.inbwallet.mvps.presenter.VerifyPasswordPresenter;
import io.insightchain.inbwallet.mvps.presenter.VerifyPasswordView;
import io.insightchain.inbwallet.mvps.presenter.VotePresenter;
import io.insightchain.inbwallet.mvps.presenter.VoteView;
import io.insightchain.inbwallet.mvps.view.adapter.VoteConfirmAdapter;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.utils.ToastUtils;

import static io.insightchain.inbwallet.common.Constants.VOTE_MAX_NUMBER;

@CreatePresenter(presenter = {VotePresenter.class,VerifyPasswordPresenter.class,PasswordPopPresenter.class,ResourcePresenter.class})
public class VoteConfirmFragment extends BaseMvpFragment implements VoteView,VerifyPasswordView,PasswordPopView,ResourceView {

    @BindView(R.id.tv_resource_number)
    TextView resourceNumberText;
    @BindView(R.id.tv_account_balance)
    TextView balanceText;
    @BindView(R.id.tv_select_node_point)
    TextView selectNodeText;
    @BindView(R.id.et_net_resource)
    EditText netEditText;
    @BindView(R.id.tv_total_vote_number)
    TextView totalVoteNumberText;
    @BindView(R.id.recycler_view_fragment_vote_confirm)
    RecyclerView recyclerView;

    @PresenterVariable
    VotePresenter votePresenter;
    @PresenterVariable
    VerifyPasswordPresenter verifyPasswordPresenter;
    @PresenterVariable
    PasswordPopPresenter passwordPopPresenter;
    @PresenterVariable
    ResourcePresenter resourcePresenter;

    private ArrayList<String> addressList;
    private ArrayList<NodeVo> selectedList;

    private VoteConfirmAdapter adapter;
    private final int VOTE_TYPE = 0;
    private final int MORTGAGE_TYPE = 1;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vote_confirm;
    }

    @Override
    protected void init() {

//        resourceNumberText.setText(BaseApplication.getCurrentWalletVo().getMortgageInbNumber());
//        balanceText.setText(BaseApplication.getCurrentWalletVo().getBalance()+"INB");

        addressList = new ArrayList<>();
        selectedList = getArguments().getParcelableArrayList("selectedList");
        setLayout();

        initRecyclerView(recyclerView);
        adapter = new VoteConfirmAdapter(mContext, selectedList, recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(position -> {
            selectedList.remove(position);
            setLayout();
            adapter.notifyDataSetChanged();
        });
    }

    public static VoteConfirmFragment newInstanse(){
        VoteConfirmFragment fragment = new VoteConfirmFragment();
        return fragment;
    }

    @OnClick({R.id.tv_confirm_vote, R.id.iv_add_resource})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_confirm_vote:
                passwordPopPresenter.showPasswordPop(VOTE_TYPE);

                break;
            case R.id.iv_add_resource:
                if(TextUtils.isEmpty(netEditText.getText())){
                    ToastUtils.showToast(mContext, "请先输入新增抵押的INB数量！");
                    return;
                }
                if(new BigDecimal(netEditText.getText().toString().trim())
                        .compareTo(new BigDecimal(BaseApplication.getCurrentWalletVo().getBalance()))
                        > 0){
                    ToastUtils.showToast(mContext, "抵押的数字已超过余额，请重新输入！");
                    return;
                }
                passwordPopPresenter.showPasswordPop(MORTGAGE_TYPE);
                break;
        }
    }

    @Override
    public void showPasswordPopWindow(CustomPopupWindow popupWindow) {
//        //获取需要在其上方显示的控件的位置信息
//        int[] location = new int[2];
//        v.getLocationOnScreen(location);
//        popupWindow.showAsDropDown(mRootView);
        popupWindow.showAtLocation(mRootView,Gravity.BOTTOM,0,0);
    }

    @Override
    public void confirmClicked(String passwordText, int type) {
        verifyPasswordPresenter.verifyPassword(passwordText,type);
    }

    @Override
    public void passwordRight(String password, int type) {
        if(type == VOTE_TYPE) {
            for(NodeVo nodeVo:selectedList){
                addressList.add(nodeVo.getAddress());
            }
            votePresenter.sendVote(addressList, password);
        }else if(type == MORTGAGE_TYPE){
            resourcePresenter.mortgageNet(netEditText.getText().toString().trim(), password);
        }
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
    public void sendEvent() {
        //TODO ?? 抵押成功后调用查询余额？
        BigDecimal newMortgageNumber = new BigDecimal(BaseApplication.getCurrentWalletVo().getMortgageInbNumber())
                .add(new BigDecimal(netEditText.getText().toString().trim()));
        BaseApplication.getCurrentWalletVo().setMortgageInbNumber(newMortgageNumber.toPlainString());
        BigDecimal newBalance = new BigDecimal(BaseApplication.getCurrentWalletVo().getBalance())
                .subtract(new BigDecimal(netEditText.getText().toString().trim()));
        BaseApplication.getCurrentWalletVo().setBalance(newBalance.toPlainString());
        setLayout();//更新票数
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    private void setLayout(){
        resourceNumberText.setText(BaseApplication.getCurrentWalletVo().getMortgageInbNumber());
        balanceText.setText(BaseApplication.getCurrentWalletVo().getBalance()+"INB");
        selectNodeText.setText(selectedList.size()+"/"+VOTE_MAX_NUMBER);
        BigDecimal totalVoteNumber = new BigDecimal(selectedList.size()).multiply(new BigDecimal(BaseApplication.getCurrentWalletVo().getMortgageInbNumber()));
        totalVoteNumberText.setText(totalVoteNumber.toPlainString());
    }
}

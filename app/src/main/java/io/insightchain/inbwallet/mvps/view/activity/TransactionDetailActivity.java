package io.insightchain.inbwallet.mvps.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpActivity;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.mvps.model.vo.TransactionResultVo;
import io.insightchain.inbwallet.mvps.presenter.ReceiveCoinsPresenter;
import io.insightchain.inbwallet.mvps.presenter.ReceiveCoinsView;
import io.insightchain.inbwallet.mvps.view.widget.TopBarCommon;
import io.insightchain.inbwallet.utils.CommonUtils;
import io.insightchain.inbwallet.utils.ToastUtils;

@CreatePresenter(presenter = ReceiveCoinsPresenter.class)
public class TransactionDetailActivity extends BaseMvpActivity implements ReceiveCoinsView {
    @BindView(R.id.topbar_common)
    TopBarCommon topBar;
    @BindView(R.id.ll_middle_layout)
    LinearLayout middleLayout;
    @BindView(R.id.ll_vote_middle_layout)
    LinearLayout voteMiddleLayout;
    @BindView(R.id.tv_type)
    TextView typeText;
    @BindView(R.id.tv_transaction_number)
    TextView transactionNumberText;
    @BindView(R.id.tv_wasted_resource_hint)
    TextView wastedResourceHintText;
    @BindView(R.id.tv_send_account)
    TextView sendAccountText;
    @BindView(R.id.tv_receive_account)
    TextView receiveAccountText;
    @BindView(R.id.tv_block_hash)
    TextView blockHashText;
    @BindView(R.id.tv_transaction_time)
    TextView transactionTimeText;
    @BindView(R.id.tv_trade_number)
    TextView tradeNumberText;

    @BindView(R.id.tv_node_name)
    TextView nodeNameText;
    @BindView(R.id.iv_trade_number)
    ImageView trageNumberImage;

    @PresenterVariable
    ReceiveCoinsPresenter receiveCoinsPresenter;

    private final int TYPE_TRANSFER = 1;
    private final int TYPE_MORTGAGE = 2 ;
    private final int TYPE_UNMORTGAGE = 3 ;
    private final int TYPE_VOTE = 4;
//    int type = 0;
    private TransactionResultVo resultVo;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_detail;
    }

    @Override
    public void initView() {
        initTopBar();
        Intent intent = getIntent();
        resultVo = (TransactionResultVo) intent.getSerializableExtra("resultVo");

        if(resultVo.getType() == TYPE_TRANSFER){
            middleLayout.setVisibility(View.VISIBLE);
            voteMiddleLayout.setVisibility(View.GONE);
            if(resultVo.getDirection() == 1){//1是转账
                typeText.setText(getString(R.string.transaction_succeed));
                transactionNumberText.setText("-"+resultVo.getAmount()+" INB");
            }else if(resultVo.getDirection() == 2){//2收款
                typeText.setText(getString(R.string.receive_succeed));
                transactionNumberText.setText("+"+resultVo.getAmount()+" INB");
            }

        }else if(resultVo.getType() == TYPE_MORTGAGE){
            middleLayout.setVisibility(View.VISIBLE);
            voteMiddleLayout.setVisibility(View.GONE);
            typeText.setText(getString(R.string.mortgage_CPU_NET));
            transactionNumberText.setText("-"+resultVo.getAmount()+" INB");
            wastedResourceHintText.setText(getString(R.string.mortgage_detail));
        }else if(resultVo.getType() == TYPE_UNMORTGAGE){
            middleLayout.setVisibility(View.VISIBLE);
            voteMiddleLayout.setVisibility(View.GONE);
            typeText.setText(getString(R.string.unmortgage_CPU_NET));
            transactionNumberText.setText("+"+resultVo.getAmount()+" INB");
            wastedResourceHintText.setText(getString(R.string.mortgage_detail));
        }else if(resultVo.getType() == TYPE_VOTE){

            middleLayout.setVisibility(View.GONE);
            voteMiddleLayout.setVisibility(View.VISIBLE);
            typeText.setText(getString(R.string.node_vote));
            transactionNumberText.setVisibility(View.GONE);
            nodeNameText.setText(resultVo.getInput() != null
                    ? resultVo.getInput().substring(resultVo.getInput().indexOf("candidates:")+11)
                    : "");
        }
//        BigDecimal bigDecimal = Convert.fromWei(resultVo.getValue(), Convert.Unit.ETHER);
//        String valueString = bigDecimal.setScale(4, RoundingMode.FLOOR).toPlainString();
//        transactionNumberText.setText(valueString);

        sendAccountText.setText(resultVo.getFrom() != null ? resultVo.getFrom() : "");
        receiveAccountText.setText(resultVo.getTo() != null ? resultVo.getTo() : "");
        blockHashText.setText(resultVo.getBlockHash() != null ? resultVo.getBlockHash() : "");
//        long time = Long.parseLong(resultVo.getTimeStamp());
//        transactionTimeText.setText(""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date( time * 1000)));
        transactionTimeText.setText(resultVo.getTimestamp() != null ? resultVo.getTimestamp() : "");
        tradeNumberText.setText(resultVo.getHash() != null ? resultVo.getHash() : "");

        receiveCoinsPresenter.getQrImage(resultVo.getHash()!=null ? resultVo.getHash() : "");
    }

    private void initTopBar(){
        topBar.setTitle(getString(R.string.transaction_detail));
        topBar.top_bar_left_layout.setVisibility(View.VISIBLE);
        topBar.top_bar_right_layout.setVisibility(View.INVISIBLE);
        topBar.setLeftView(null,R.mipmap.icon_back_arrow);
    }

    @OnClick({R.id.top_bar_left_layout ,R.id.iv_copy_block_number, R.id.iv_copy_send_account, R.id.iv_copy_receive_account})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_left_layout:
                finish();
                break;
            case R.id.iv_copy_send_account:
                CommonUtils.copyToClipboard(this, resultVo.getFrom());
                ToastUtils.showToast(context, "已复制到剪贴板");
                break;
            case R.id.iv_copy_receive_account:
                CommonUtils.copyToClipboard(this, resultVo.getTo());
                ToastUtils.showToast(context, "已复制到剪贴板");
                break;
            case R.id.iv_copy_block_number:
                CommonUtils.copyToClipboard(this, resultVo.getBlockNumber());
                ToastUtils.showToast(context, "已复制到剪贴板");
                break;
            default:
                break;
        }
    }

    @Override
    public void setQrImage(Bitmap bitmap) {
        trageNumberImage.setImageBitmap(bitmap);
    }

//    @Override
//    public void sendBroadcast(File file) {
//
//    }
}

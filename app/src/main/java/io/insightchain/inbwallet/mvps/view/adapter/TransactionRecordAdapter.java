package io.insightchain.inbwallet.mvps.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.RecyclerCommonAdapter;
import io.insightchain.inbwallet.base.RecyclerViewHolder;
import io.insightchain.inbwallet.mvps.model.vo.TransactionResultVo;

public class TransactionRecordAdapter extends RecyclerCommonAdapter<TransactionResultVo> {

    private Context context;
    private List<TransactionResultVo> datas;
    private OnClickListener onClickListener;

    public TransactionRecordAdapter(Context context, List<TransactionResultVo> datas, RecyclerView recyclerView){
        super(context, R.layout.item_recycler_view_transaction_record, datas, recyclerView);
        this.context = context;
        this.datas = datas;
    }
    @Override
    public void convert(RecyclerViewHolder holder, TransactionResultVo transactionResult) {

    }

    @Override
    public void convert(RecyclerViewHolder holder, TransactionResultVo transactionResult, int position) {
        Log.e("sss",transactionResult.toString());
        if (getItemViewType(position) == VIEW_ITEM) {
            //如果地址和from一致，那么证明是转账，如果地址和to一致，那么证明是收款
            switch (transactionResult.getType()){
                case 1://交易
                    if(transactionResult.getFrom().equalsIgnoreCase(BaseApplication.getCurrentWallet().getAddress())) {
                        holder.setText(R.id.tv_transaction_account, transactionResult.getTo());
                        holder.setText(R.id.tv_transaction_status,context.getResources().getString(R.string.transfer_accounts_wallet_fragment));
                        holder.setText(R.id.tv_transaction_number, "-"+transactionResult.getAmount()+" INB");
                        transactionResult.setDirection(1);
                    }else if(transactionResult.getTo().equalsIgnoreCase(BaseApplication.getCurrentWallet().getAddress())){
                        holder.setText(R.id.tv_transaction_account, transactionResult.getFrom());
                        holder.setText(R.id.tv_transaction_status,context.getResources().getString(R.string.receive_money_wallet_fragment));
                        holder.setText(R.id.tv_transaction_number, "+"+transactionResult.getAmount()+" INB");
                        transactionResult.setDirection(2);
                    }
                    holder.setBackgroundRes(R.id.tv_transaction_status,R.drawable.shape_rectangle_3574fa_2dp);
                    holder.setTextColor(R.id.tv_transaction_status,context.getResources().getColor(R.color.c_3574fa));
                    break;
                case 2://抵押
                    holder.setText(R.id.tv_transaction_account, context.getResources().getString(R.string.resource_mortgage));
                    holder.setText(R.id.tv_transaction_status,context.getResources().getString(R.string.resource));
                    holder.setBackgroundRes(R.id.tv_transaction_status,R.drawable.shape_rectangle_f5a523_2dp);
                    holder.setTextColor(R.id.tv_transaction_status,context.getResources().getColor(R.color.c_f5a623));
                    holder.setText(R.id.tv_transaction_number, "-"+transactionResult.getAmount()+" INB");
                    break;
                case 3://解抵押
                    holder.setText(R.id.tv_transaction_account, context.getResources().getString(R.string.resource_unmortgage));
                    holder.setText(R.id.tv_transaction_status,context.getResources().getString(R.string.resource));
                    holder.setBackgroundRes(R.id.tv_transaction_status,R.drawable.shape_rectangle_f5a523_2dp);
                    holder.setTextColor(R.id.tv_transaction_status,context.getResources().getColor(R.color.c_f5a623));
                    holder.setText(R.id.tv_transaction_number, "+"+transactionResult.getAmount()+" INB");
                    break;
                case 4://投票
                    holder.setText(R.id.tv_transaction_account, context.getResources().getString(R.string.vote_wallet_fragment));
                    holder.setText(R.id.tv_transaction_status,context.getResources().getString(R.string.other));
                    holder.setBackgroundRes(R.id.tv_transaction_status,R.drawable.shape_rectangle_3574fa_2dp);
                    holder.setTextColor(R.id.tv_transaction_status,context.getResources().getColor(R.color.c_3574fa));
                    holder.setText(R.id.tv_transaction_number, "-"+transactionResult.getAmount()+" INB");
                    break;
            }

            holder.setText(R.id.tv_transaction_time, transactionResult.getTimestamp());
            holder.setText(R.id.tv_remarks_information, transactionResult.getInput());

            holder.setOnClickListener(R.id.root_view_item_recycler_view_transaction_record, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(position);
                    }
                }
            });
        }else if (getItemViewType(position) == VIEW_LOAD_MORE) {
            if (hasMoreData) {
                holder.setVisible(R.id.loading_layout, true);
                holder.setVisible(R.id.no_more_data_layout, false);
            } else {
                holder.setVisible(R.id.loading_layout, false);
                holder.setVisible(R.id.no_more_data_layout, true);
            }
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClick(int position);
    }

}

package io.insightchain.inbwallet.mvps.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import java.util.List;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseApplication;
import io.insightchain.inbwallet.base.RecyclerCommonAdapter;
import io.insightchain.inbwallet.base.RecyclerViewHolder;
import io.insightchain.inbwallet.wallet.ETHWallet;

/**
 * Created by NiPengyuan on 05/09.
 */
public class AddAccountPopRecyclerAdapter extends RecyclerCommonAdapter<ETHWallet> {

    private Context context;
    private OnClickListener onClickListener;


    public AddAccountPopRecyclerAdapter(Context context, List<ETHWallet> datas, RecyclerView recyclerView) {
        super(context, R.layout.item_recycler_view_pop_add_account, datas, recyclerView);
        this.context = context;
    }

    @Override
    public void convert(RecyclerViewHolder holder, ETHWallet wallet) {

    }

    @Override
    public void convert(RecyclerViewHolder holder, ETHWallet wallet, int position) {
        if (getItemViewType(position) == VIEW_ITEM) {
            holder.setText(R.id.tv_account_name, wallet.getName());
            holder.setText(R.id.tv_account_address, wallet.getAddress());
            //多币种需要判断货币种类，从而改变icon。目前单一INB
//            holder.setImageResource(R.id.iv_circle_head,)
            CheckBox checkBox = holder.getView(R.id.cb_item_add_account);
            if(BaseApplication.getCurrentWalletID() == wallet.getId()){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            holder.setOnClickListener(R.id.root_view_item_recycler_view_pop_add_account, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener!=null && BaseApplication.getCurrentWalletID() != wallet.getId()){
                        onClickListener.onClick(v,position);
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
        void onClick(View v, int position);
    }
}

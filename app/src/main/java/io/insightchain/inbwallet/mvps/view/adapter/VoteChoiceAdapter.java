package io.insightchain.inbwallet.mvps.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.RecyclerCommonAdapter;
import io.insightchain.inbwallet.base.RecyclerViewHolder;
import io.insightchain.inbwallet.common.GlideCircleTransform;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;
import io.insightchain.inbwallet.utils.NumberUtils;

public class VoteChoiceAdapter extends RecyclerCommonAdapter<NodeVo> {

    private Context context;
    private List<NodeVo> datas;
    private OnClickListener onClickListener;

    public VoteChoiceAdapter(Context context, List<NodeVo> datas, RecyclerView recyclerView){
        super(context, R.layout.item_recycle_view_vote_choice, datas, recyclerView);
        this.context = context;
        this.datas = datas;
    }
    @Override
    public void convert(RecyclerViewHolder holder, NodeVo nodeVo) {

    }

    @Override
    public void convert(RecyclerViewHolder holder, NodeVo nodeVo, int position) {
        Log.e("sss",nodeVo.toString());
        if (getItemViewType(position) == VIEW_ITEM) {

            holder.setText(R.id.tv_node_name,nodeVo.getName()!=null ? nodeVo.getName():"");
            holder.setText(R.id.tv_node_address, nodeVo.getAddress()!=null ? nodeVo.getAddress():"");
            holder.setText(R.id.tv_description, "");
            holder.setText(R.id.tv_right_rate, NumberUtils.formatNumberNoComma(nodeVo.getVoteRatio()*100,2)+"%");
            holder.setText(R.id.tv_location, nodeVo.getCity()!=null ? nodeVo.getCity():"");
            holder.setText(R.id.tv_description, nodeVo.getIntro()!=null ? nodeVo.getIntro():"");
            holder.setText(R.id.tv_status_item_vote_choice, nodeVo.isChecked() ?
                    context.getString(R.string.selected_wallet_fragment):
                    context.getString(R.string.vote_wallet_fragment));

            ImageView headImage = holder.getView(R.id.iv_node_head_pic);
            if (nodeVo.getImage() != null) {
                Glide.with(context).load(nodeVo.getImage()).error(R.mipmap.icon_default_head_img_circle_gray)
                        .transform(new GlideCircleTransform(context)).into(headImage);
            }
            CheckBox checkBox = holder.getView(R.id.cb_select);
            checkBox.setChecked(nodeVo.isChecked());
            holder.setOnClickListener(R.id.root_view_item_recycler_view_vote_choice, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodeVo.setChecked(!nodeVo.isChecked());
                    checkBox.setChecked(nodeVo.isChecked());
                    holder.setText(R.id.tv_status_item_vote_choice, nodeVo.isChecked() ?
                            context.getString(R.string.selected_wallet_fragment):
                            context.getString(R.string.vote_wallet_fragment));
                    if(onCheckChangedListner!=null){
                        onCheckChangedListner.onCheckChanged(checkBox,nodeVo.isChecked(),position);
                    }
                }
            });
//            CheckBox checkBox = holder.getView(R.id.cb_select);


//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if(onCheckChangedListner!=null){
//                        onCheckChangedListner.onCheckChanged(buttonView,isChecked,position);
//                    }
//                }
//            });

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

    private onCheckChangedListner onCheckChangedListner;

    public void setOnCheckChangedListner(VoteChoiceAdapter.onCheckChangedListner onCheckChangedListner) {
        this.onCheckChangedListner = onCheckChangedListner;
    }

    public interface onCheckChangedListner{
        void onCheckChanged(CompoundButton buttonView, boolean isChecked, int position);
    }

}

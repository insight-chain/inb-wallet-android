package io.insightchain.inbwallet.mvps.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.RecyclerCommonAdapter;
import io.insightchain.inbwallet.base.RecyclerViewHolder;
import io.insightchain.inbwallet.common.GlideCircleTransform;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;

public class VoteConfirmAdapter extends RecyclerCommonAdapter<NodeVo> {

    private Context context;
    private List<NodeVo> datas;
    private OnClickListener onClickListener;

    public VoteConfirmAdapter(Context context, List<NodeVo> datas, RecyclerView recyclerView){
        super(context, R.layout.item_recycler_view_fragment_vote_confirm, datas, recyclerView);
        this.context = context;
        this.datas = datas;
    }
    @Override
    public void convert(RecyclerViewHolder holder, NodeVo nodeVo) {

    }

    @Override
    public void convert(RecyclerViewHolder holder, NodeVo nodeVo, int position) {
        Log.e("VoteConfirmAdapter",nodeVo.toString());
        if (getItemViewType(position) == VIEW_ITEM) {
            ImageView headImage = holder.getView(R.id.iv_head);
            if (nodeVo.getImage() != null) {
                Glide.with(context).load(nodeVo.getImage()).error(R.mipmap.icon_default_head_img_circle_gray)
                        .transform(new GlideCircleTransform(context)).into(headImage);
            }

            holder.setText(R.id.tv_node_name, nodeVo.getName()!=null ? nodeVo.getName():"");
            holder.setText(R.id.tv_node_address, nodeVo.getAddress()!=null ? nodeVo.getAddress():"");
            holder.setOnClickListener(R.id.iv_subtraction, v -> {
                if(onClickListener != null){
                    onClickListener.onClick(position);
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
}

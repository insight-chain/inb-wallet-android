package io.insightchain.inbwallet.mvps.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.insightchain.inbwallet.R;

public class SelectedGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> mnemonicList;
    private OnClickListener onClickListener;

    public SelectedGridViewAdapter(Context context, List<String> mnemonicList){
        this.context = context;
        this.mnemonicList = mnemonicList;
    }

    @Override
    public int getCount() {
        return mnemonicList.size();
    }

    @Override
    public Object getItem(int position) {
        return mnemonicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_selected, parent,false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.tv_selected_word);
            holder.imageView = convertView.findViewById(R.id.iv_delete_item_gridview_selected);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mnemonicList.get(position));
        if(position == mnemonicList.size()-1){
            holder.imageView.setVisibility(View.VISIBLE);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.onClick(position);
                }
            }
        });
        return convertView;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;
    }

    public interface OnClickListener{
        void onClick(int position);
    }
}

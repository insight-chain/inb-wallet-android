package io.insightchain.inbwallet.mvps.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.insightchain.inbwallet.R;

public class ShowGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> mnemonicList;

    public ShowGridViewAdapter(Context context, List<String> mnemonicList){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_show, parent,false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.tv_item_gridview_show);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mnemonicList.get(position));
        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }
}

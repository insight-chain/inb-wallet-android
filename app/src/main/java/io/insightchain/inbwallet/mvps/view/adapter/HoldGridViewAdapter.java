package io.insightchain.inbwallet.mvps.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.List;

import io.insightchain.inbwallet.R;

public class HoldGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> mnemonicList;
    private OnCheckedListener onCheckedListener;
    private int modifyPosition;

    public HoldGridViewAdapter(Context context, List<String> mnemonicList){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_hold_fragment_backs_up_mnemonic, parent,false);
            holder = new ViewHolder();
            holder.checkbox = convertView.findViewById(R.id.checkbox_item_gridview_hold);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkbox.setText(mnemonicList.get(position));
        if(modifyPosition == position){
            holder.checkbox.setEnabled(true);
            holder.checkbox.setChecked(false);
        }
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(onCheckedListener!=null){
                if (isChecked){
                    buttonView.setEnabled(false);
                    onCheckedListener.onChecked(position,isChecked);
                }
            }
        });

        return convertView;
    }

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    public void setCheckBoxEnable(int position){
        modifyPosition = position;
        notifyDataSetChanged();
    }

    class ViewHolder{
        CheckBox checkbox;
    }

    public interface OnCheckedListener{
        void onChecked(int position, boolean isChecked);
    }
}

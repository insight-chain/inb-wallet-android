package io.insightchain.inbwallet.mvps.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lijilong on 03/09.
 */

public abstract class TopBar extends RelativeLayout {
    protected Context mContext;

    protected View mRootView;

    public View status_bar;
    public View top_bar_line;

    public ViewGroup top_bar_main_layout;

    public ViewGroup top_bar_left_layout;
    public ViewGroup top_bar_right_layout;

    public ViewGroup top_bar_title_layout;

    public TextView top_bar_title_text;

    private int width;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TopBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        mContext = context;
    }

    protected abstract void init();

    public void setBackgroundColor(int i) {
        this.mRootView.setBackgroundColor(i);
    }

    public void setTitle(String title) {
        this.top_bar_title_text.setText(title);
    }

    public void setBackgroundAlpha(float f) {
        if (mRootView != null && mRootView.getBackground() != null) {
            this.mRootView.getBackground().mutate().setAlpha((int) (f * 255));
        }
    }


}


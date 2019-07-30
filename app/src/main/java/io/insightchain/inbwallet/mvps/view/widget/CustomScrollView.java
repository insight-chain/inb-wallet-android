package io.insightchain.inbwallet.mvps.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private float xDistance, yDistance, xLast, yLast;
    private int scaledTouchSlop;

    public CustomScrollView(Context context) {
        super(context, null);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                Log.e("SiberiaDante", "xDistance ：" + xDistance + "---yDistance:" + yDistance);
                return !(((xDistance >= yDistance) || yDistance < scaledTouchSlop) || (xDistance < 10 && yDistance < 10));
//            case MotionEvent.ACTION_UP:
//                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}

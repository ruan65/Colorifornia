package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.BaseActivity;

public class FrameLayoutWithAim extends FrameLayout {

    private ImageView aim;
    private BaseActivity ctx;

    public FrameLayoutWithAim(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = (BaseActivity) context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                if (aim != null) {
                    aim.setX(ev.getX() - aim.getWidth() / 2);
                    aim.setY(ev.getY() - aim.getHeight() / 2);
                    aim.setImageResource(ctx.isWhiteText()
                            ? R.drawable.ic_aim_white
                            : R.drawable.ic_aim_black);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setAim(ImageView aim) {
        this.aim = aim;
    }
}

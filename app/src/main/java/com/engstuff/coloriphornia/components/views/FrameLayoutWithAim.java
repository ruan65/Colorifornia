package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FrameLayoutWithAim extends FrameLayout {

    private ImageView aim;

    public FrameLayoutWithAim(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (aim != null) {
                aim.setX(ev.getX());
                aim.setY(ev.getY());
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setAim(ImageView aim) {
        this.aim = aim;
    }
}

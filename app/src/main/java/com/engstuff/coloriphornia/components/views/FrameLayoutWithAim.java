package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.BaseActivity;

public class FrameLayoutWithAim extends FrameLayout {

    private ImageView aim;
    private BaseActivity ctx;

    private float aimX, aimY;

    private GestureDetector gestureDetector =
            new GestureDetector(ctx, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent ev) {

                    if (aim != null) {
                        aimX = ev.getX() - aim.getWidth() / 2;
                        aimY = ev.getY() - aim.getHeight() / 2;

                        replaceAim(aimX, aimY);
                    }
                    return super.onSingleTapUp(ev);
                }
            });

    void replaceAim(float x, float y) {
        aim.setX(x);
        aim.setY(y);
        aim.setImageResource(ctx.isWhiteText()
                ? R.drawable.ic_target_w
                : R.drawable.ic_target_b);
    }

    public FrameLayoutWithAim(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = (BaseActivity) context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        gestureDetector.onTouchEvent(ev);

        return super.onInterceptTouchEvent(ev);
    }

    public void setAim(ImageView aim) {
        this.aim = aim;
    }

    public float getAimX() {
        return aimX;
    }

    public float getAimY() {
        return aimY;
    }
}

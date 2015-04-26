package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.BaseColorActivity;

public class FrameLayoutWithAim extends FrameLayout {

    private ImageView aim;
    private BaseColorActivity ctx;
    private ZoomableImageView ziv;

    private float aimX, aimY;

    private GestureDetector gestureDetector =
            new GestureDetector(ctx, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent ev) {

                    float evX = ev.getX();
                    float evY = ev.getY();

                    aimX = evX - aim.getWidth() / 2;
                    aimY = evY - aim.getHeight() / 2;

                    if (ziv != null) {
                        // for some magic reasons it works without subtracting half of aim
                        ziv.changeColor((int) evX, (int) evY);
                        ziv.setAimCoords(evX, evY);
                    }

                    if (aim != null) replaceAim(aimX, aimY);

                    return super.onSingleTapUp(ev);
                }
            });

    public void replaceAim(float x, float y) {
        aim.setX(x);
        aim.setY(y);
        changeAimColor(ctx.isWhiteText());
    }

    public void changeAimColor(boolean white) {
        aim.setImageResource(white ? R.drawable.ic_target_w
                : R.drawable.ic_target_b);
    }

    public FrameLayoutWithAim(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = (BaseColorActivity) context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        gestureDetector.onTouchEvent(ev);

        return super.onInterceptTouchEvent(ev);
    }

    public void setAim(ImageView aim) {
        this.aim = aim;
    }

    public void setZiv(ZoomableImageView ziv) {
        this.ziv = ziv;
    }
}
package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.interfaces.ColorControlChangeListener;

public class SeekBarButton extends ImageButton implements
        View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    public static final int INTERVAL = 150;

    private SeekBar seekBar;
//    private ColorControlChangeListener colorChangeListener;
    private boolean incrementer;

    private Handler mHandler = new Handler();

    private Runnable autoIncDecSeekBar = new Runnable() {

        @Override
        public void run() {

            stepSeekBar();
            mHandler.postDelayed(autoIncDecSeekBar, INTERVAL);
        }
    };

    public SeekBarButton(Context context) {
        super(context);
    }

    public SeekBarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void stepSeekBar() {

//        colorChangeListener.onColorControlStartTracking();

        int prs = seekBar.getProgress();

        seekBar.setProgress(incrementer ? (prs < 255 ? ++prs : prs) : (prs > 0 ? --prs : prs));

//        colorChangeListener.onColorControlChange(prs, seekBar.getId());
//        colorChangeListener.onColorControlStopTracking();
    }

    public void init(SeekBar sb, boolean inc) {

        seekBar = sb;
//        colorChangeListener = listener;
        incrementer = inc;
        setOnClickListener(this);
        setOnLongClickListener(this);
        setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {

        stepSeekBar();
    }

    @Override
    public boolean onLongClick(View v) {

        mHandler.postDelayed(autoIncDecSeekBar, INTERVAL);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && mHandler != null) {
            mHandler.removeCallbacks(autoIncDecSeekBar);
        }
        return super.onTouchEvent(event);
    }
}
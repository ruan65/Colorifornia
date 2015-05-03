package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;

public class SeekBarMinusPlus extends LinearLayout {

    private SeekBar seekBar;
    private SeekBarButton minus, plus;
    private Resources res;

    public SeekBarMinusPlus(Context context) {
        super(context);
    }

    public SeekBarMinusPlus(Context context, AttributeSet attrs) {
        super(context, attrs);

        res = context.getResources();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.seek_bar_minus_plus, this, true);

        seekBar = (SeekBar) getChildAt(1);
        minus = (SeekBarButton) getChildAt(0);
        plus = (SeekBarButton) getChildAt(2);

    }

    public void init(SeekBar.OnSeekBarChangeListener sbl, int tag) {

        seekBar.setOnSeekBarChangeListener(sbl);
        seekBar.setTag(tag);
        minus.init(seekBar, false);
        plus.init(seekBar, true);

        switch (tag) {

            case Cv.RED:
                seekBar.setProgress(Cv.INIT_RED);
                seekBar.setProgressDrawable(res.getDrawable(R.drawable.progress_red));
                seekBar.setThumb(res.getDrawable(R.drawable.thumb_r));
                break;

            case Cv.GREEN:
                seekBar.setProgress(Cv.INIT_GREEN);
                seekBar.setProgressDrawable(res.getDrawable(R.drawable.progress_green));
                seekBar.setThumb(res.getDrawable(R.drawable.thumb_g));
                break;

            case Cv.BLUE:
                seekBar.setProgress(Cv.INIT_BLUE);
                seekBar.setProgressDrawable(res.getDrawable(R.drawable.progress_blue));
                seekBar.setThumb(res.getDrawable(R.drawable.thumb_b));
                break;

            case Cv.ALPHA:
                seekBar.setProgress(Cv.INIT_ALPHA);
                seekBar.setProgressDrawable(res.getDrawable(R.drawable.progress));
                seekBar.setThumb(res.getDrawable(R.drawable.thumb));
                break;
        }
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }
}

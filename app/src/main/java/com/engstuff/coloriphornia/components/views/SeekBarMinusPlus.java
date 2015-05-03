package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.interfaces.ColorControlChangeListener;

public class SeekBarMinusPlus extends LinearLayout {

    private SeekBar seekBar;
    private SeekBarButton minus, plus;

    public SeekBarMinusPlus(Context context) {
        super(context);
    }

    public SeekBarMinusPlus(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.seek_bar_minus_plus, this, true);

        seekBar = (SeekBar) getChildAt(1);
        minus = (SeekBarButton) getChildAt(0);
        plus = (SeekBarButton) getChildAt(2);

    }

    public void init(SeekBar.OnSeekBarChangeListener sbl, ColorControlChangeListener ccl, int tag) {

        seekBar.setOnSeekBarChangeListener(sbl);
        seekBar.setTag(tag);
        minus.init(seekBar, ccl, false);
        plus.init(seekBar, ccl, true);
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }
}

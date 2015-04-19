package com.engstuff.coloriphornia.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FullScreenColorCC extends FullScreenColorC {

    @InjectView(R.id.tv_color_params_1) TextView tv1;
    @InjectView(R.id.tv_color_params_2) TextView tv2;

    @InjectView(R.id.layout_full_screen_color_1) RelativeLayout rl1;
    @InjectView(R.id.layout_full_screen_color_2) RelativeLayout rl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_cc);

        ButterKnife.inject(this);

        Intent intent = getIntent();

        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);

        // set color 1
        setColor(intent,
                Cv.EXTRA_MESSAGE_COLOR_1, Cv.EXTRA_MESSAGE_TEXT_COLOR_1,
                rl1, tv1);

        // set color 2
        setColor(intent,
                Cv.EXTRA_MESSAGE_COLOR_2, Cv.EXTRA_MESSAGE_TEXT_COLOR_2,
                rl2, tv2);
    }

    private void setColor(Intent intent, String extra1, String extra2,
                          RelativeLayout rl, TextView tv) {

        String hexColor = intent.getStringExtra(extra1);

        int backColor = (int) Long.parseLong(hexColor.substring(1), 16);

        rl.setBackgroundColor(backColor);

        int textColor1 =
                intent.getBooleanExtra(extra2, false)
                ? Color.WHITE
                : Color.BLACK;

        tv.setTextColor(textColor1);
        tv.setText(composeInfo(hexColor));
    }
}

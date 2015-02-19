package com.engstuff.coloriphornia.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;


public class FullScreenColorCC extends Activity {

    private TextView tv1;
    private RelativeLayout rl1, rl2;
    private Button btnBack1, btnBack2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_cc);

        rl1 = (RelativeLayout) findViewById(R.id.layout_full_screen_color_1);
        rl2 = (RelativeLayout) findViewById(R.id.layout_full_screen_color_2);

        Intent intent = getIntent();

        String[] colorMessage = intent.getStringArrayExtra(ColorC.EXTRA_MESSAGE_COLOR_1);

        int backColor = (int) Long.parseLong(colorMessage[1].substring(1), 16);

        rl1.setBackgroundColor(backColor);

        tv1 = (TextView) findViewById(R.id.tv_color_params_1);

        int textColor1 = intent.getBooleanExtra(ColorC.EXTRA_MESSAGE_TEXT_COLOR_1, false)
                ? Color.WHITE
                : Color.BLACK;

        tv1.setTextColor(textColor1);
        tv1.setText(colorMessage[0] + "\n" + colorMessage[1]);

        btnBack1 = (Button) findViewById(R.id.btnBack1);
        btnBack1.setBackgroundColor(backColor);
        btnBack1.setTextColor(textColor1);

    }

    public void backClick(View v) {
        this.finish();
    }

}

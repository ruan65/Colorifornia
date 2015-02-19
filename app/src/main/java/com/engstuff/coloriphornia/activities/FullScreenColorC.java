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


public class FullScreenColorC extends Activity {

    private TextView tv;
    private RelativeLayout rl;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_c);

        rl = (RelativeLayout) findViewById(R.id.layout_full_screen_color);

        Intent intent = getIntent();

        String[] colorMessage = intent.getStringArrayExtra(ColorC.EXTRA_MESSAGE_COLOR_1);

        int backColor = (int) Long.parseLong(colorMessage[1].substring(1), 16);

        rl.setBackgroundColor(backColor);

        tv = (TextView) findViewById(R.id.tv_color_params);

        int textColor = intent.getBooleanExtra(ColorC.EXTRA_MESSAGE_TEXT_COLOR_1, false)
                ? Color.WHITE
                : Color.BLACK;

        tv.setTextColor(textColor);
        tv.setText(colorMessage[0] + "\n" + colorMessage[1]);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setBackgroundColor(backColor);
        btnBack.setTextColor(textColor);

    }

    public void backClick(View v) {
        this.finish();
    }

}

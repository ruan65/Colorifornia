package com.engstuff.coloriphornia.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.helpers.ColorParams;


public class FullScreenColorC extends Activity implements OnClickListener {

    private TextView tv;
    protected LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_c);

        container = (LinearLayout) findViewById(R.id.layout_full_screen_color);

        Intent intent = getIntent();

        String hexString = intent.getStringExtra(Cv.EXTRA_MESSAGE_COLOR_1);

        int backColor = (int) Long.parseLong(hexString.substring(1), 16);

        container.setBackgroundColor(backColor);
        container.setOnClickListener(this);

        tv = (TextView) findViewById(R.id.tv_color_params);

        int textColor = intent.getBooleanExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, false)
                ? Color.WHITE
                : Color.BLACK;

        tv.setTextColor(textColor);
        tv.setText(ColorParams.composeInfo(hexString));
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_l, R.anim.slide_out_l);
    }

    @Override
    public void onClick(View v) {
        finish();
    }





    
}

package com.engstuff.coloriphornia.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.helpers.ColorParams;


public class FullScreenColorC extends Activity implements OnClickListener {

    private CardView cv;
    private TextView tv;
    protected RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_c);

        container = (RelativeLayout) findViewById(R.id.layout_full_screen_color);

        Intent intent = getIntent();

        String hexString = intent.getStringExtra(Cv.EXTRA_MESSAGE_COLOR_1);

        int backColor = (int) Long.parseLong(hexString.substring(1), 16);
        int backCardColor = (int) Long.parseLong(hexString.substring(3), 16);

        container.setBackgroundColor(backColor);
        container.setOnClickListener(this);

        cv = (CardView) findViewById(R.id.card_view_full_c);
        cv.setCardBackgroundColor(backCardColor);

        tv = (TextView) findViewById(R.id.tv_color_params);

        int textColor = intent.getBooleanExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, false)
                ? Color.WHITE
                : Color.BLACK;

        tv.setBackgroundColor(backCardColor);
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

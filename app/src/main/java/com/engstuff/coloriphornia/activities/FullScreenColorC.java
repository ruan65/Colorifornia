package com.engstuff.coloriphornia.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.helpers.ColorParams;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FullScreenColorC extends Activity implements OnClickListener {

    @InjectView(R.id.layout_full_screen_color) RelativeLayout container;
    @InjectView(R.id.card_view_full_c) CardView cv;
    @InjectView(R.id.tv_color_params) TextView tv;
    @InjectView(R.id.show_info_full_c) ImageButton showInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_c);

        ButterKnife.inject(this);

        Intent intent = getIntent();

        String hexString = intent.getStringExtra(Cv.EXTRA_MESSAGE_COLOR_1);

        int backColor = (int) Long.parseLong(hexString.substring(1), 16);
        int backCardColor = (int) Long.parseLong(hexString.substring(3), 16);

        container.setBackgroundColor(backColor);
        container.setOnClickListener(this);

        cv.setCardBackgroundColor(backCardColor);

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

    @OnClick(R.id.show_info_full_c)
    public void showInfo() {
        cv.setVisibility(View.VISIBLE);
        showInfo.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.close_info_full_c_card)
    public void closeInfo() {
        cv.setVisibility(View.INVISIBLE);
        showInfo.setVisibility(View.VISIBLE);
    }



    
}

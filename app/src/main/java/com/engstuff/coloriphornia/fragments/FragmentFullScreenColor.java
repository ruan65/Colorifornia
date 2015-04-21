package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.ColorParams;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentFullScreenColor extends Fragment implements View.OnClickListener {

    Activity activity;
    String hexString;
    boolean whiteText;

    @InjectView(R.id.card_view_full_c)
    CardView cv;
    @InjectView(R.id.tv_color_params)
    TextView tv;
    @InjectView(R.id.show_info_full_c)
    ImageView showInfo;
    @InjectView(R.id.close_info_full_c_card)
    ImageView closeInfo;
    @InjectView(R.id.send_info_full_c)
    ImageView sendInfo;
    @InjectView(R.id.layout_full_screen_color_fragment)
    RelativeLayout layout;

    public FragmentFullScreenColor() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RelativeLayout root = (RelativeLayout) inflater
                .inflate(R.layout.fragment_full_screen_color, container, false);

        ButterKnife.inject(this, root);


        boolean whiteText = false;
        try {
            whiteText = ColorParams.blackOrWhiteText(hexString);
        } catch (Exception ignore) {
        }

        showInfo.setImageResource(
                whiteText
                        ? R.drawable.ic_add_circle_outline_white_36dp
                        : R.drawable.ic_add_circle_outline_black_36dp);

        closeInfo.setImageResource(
                whiteText
                        ? R.drawable.ic_remove_circle_outline_white_36dp
                        : R.drawable.ic_remove_circle_outline_black_36dp);

        sendInfo.setImageResource(
                whiteText
                        ? R.drawable.ic_send_white_36dp
                        : R.drawable.ic_send_black_36dp);

        int backColor = (int) Long.parseLong(hexString.substring(1), 16);
        int backCardColor = (int) Long.parseLong(hexString.substring(3), 16);

        container.setBackgroundColor(backColor);

        cv.setCardBackgroundColor(backCardColor);

        int textColor = whiteText ? Color.WHITE : Color.BLACK;

        tv.setTextColor(textColor);
        tv.setText(ColorParams.composeInfo(hexString));

        layout.setOnClickListener(this);

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        activity.finish();
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

    @OnClick(R.id.send_info_full_c)
    public void sendInfo() {
        AppHelper.fireShareIntent(activity, ColorParams.composeInfoHTML(hexString));
    }

    public void setHexString(String hexString) {
        this.hexString = hexString;
    }

    public void setWhiteText(boolean whiteText) {
        this.whiteText = whiteText;
    }
}

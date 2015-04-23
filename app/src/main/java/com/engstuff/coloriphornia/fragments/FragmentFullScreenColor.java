package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.interfaces.OnFlingListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class FragmentFullScreenColor extends Fragment {

    Activity activity;
    String hexString;
    Animation hideAnim, btnFadeInAnim, showAnim, btnFadeOutAnim;
    boolean whiteText;

    GestureDetector mGestureDetector;

    @InjectView(R.id.card_view_full_c) CardView cv;

    @InjectView(R.id.tv_color_params) TextView tv;

    @InjectView(R.id.show_info_full_c) ImageView showInfo;

    @InjectView(R.id.close_info_full_c_card)ImageView closeInfo;

    @InjectView(R.id.send_info_full_c) ImageView sendInfo;

    public FragmentFullScreenColor() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGestureDetector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                ((OnFlingListener) activity).onFling(true);

                return true;
            }
        });

        setRetainInstance(true);

        RelativeLayout root = (RelativeLayout) inflater
                .inflate(R.layout.fragment_full_screen_color, container, false);

        ButterKnife.inject(this, root);

        hideAnim = AnimationUtils.loadAnimation(activity, R.anim.info_close);
        showAnim = AnimationUtils.loadAnimation(activity, R.anim.info_open);
        btnFadeOutAnim = AnimationUtils.loadAnimation(activity, R.anim.btn_fadeout);
        btnFadeInAnim = AnimationUtils.loadAnimation(activity, R.anim.btn_fadein);

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

        hexString = ColorParams.replaceNotValidHexForZeroColor(hexString);

        int backColor = (int) Long.parseLong(hexString.substring(1), 16);
        int backCardColor = (int) Long.parseLong(hexString.substring(3), 16);

        container.setBackgroundColor(backColor);

        cv.setCardBackgroundColor(backCardColor);

        int textColor = whiteText ? Color.WHITE : Color.BLACK;

        tv.setTextColor(textColor);
        tv.setText(ColorParams.composeInfo(hexString));

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

    @OnClick(R.id.show_info_full_c)
    public void showInfo() {

        cv.setVisibility(View.VISIBLE);
        showAnim.cancel();
        cv.startAnimation(showAnim);

        showInfo.setVisibility(View.INVISIBLE);
        btnFadeOutAnim.cancel();
        showInfo.startAnimation(btnFadeOutAnim);
    }

    @OnClick(R.id.close_info_full_c_card)
    public void closeInfo() {

        cv.setVisibility(View.INVISIBLE);
        hideAnim.cancel();
        cv.startAnimation(hideAnim);

        showInfo.setVisibility(View.VISIBLE);
        btnFadeInAnim.cancel();
        showInfo.startAnimation(btnFadeInAnim);
    }

    @OnClick(R.id.send_info_full_c)
    public void sendInfo() {
        AppHelper.fireShareIntent(activity, ColorParams.composeInfoHTML(hexString));
    }

    @OnClick(R.id.layout_full_screen_color_fragment)
    public void close() {
        activity.finish();
    }

    @OnTouch(R.id.layout_full_screen_color_fragment)
    public boolean nextFavoriteColor(MotionEvent ev) {

        return mGestureDetector.onTouchEvent(ev);
    }

    public void setHexString(String hexString) {
        this.hexString = hexString;
    }

    public void setWhiteText(boolean whiteText) {
        this.whiteText = whiteText;
    }
}

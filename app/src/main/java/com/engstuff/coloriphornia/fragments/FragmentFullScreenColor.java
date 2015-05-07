package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
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
import com.engstuff.coloriphornia.interfaces.HideInfoListener;
import com.engstuff.coloriphornia.interfaces.OnFlingListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class FragmentFullScreenColor extends Fragment {

    private Activity activity;
    private String hexString;
    private int textColor = -1;

    private Animation hideAnim, btnFadeInAnim, showAnim, btnFadeOutAnim;

    private final GestureDetector mGestureDetector = new GestureDetector(activity,
            new GestureDetector.SimpleOnGestureListener() {

        private static final int DISTANCE_THRESHOLD = 100;
        private static final int VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();

            if (Math.abs(distanceX) > Math.abs(distanceY)
                    && Math.abs(distanceX) > DISTANCE_THRESHOLD
                    && Math.abs(velocityX) > VELOCITY_THRESHOLD) {

                ((OnFlingListener) activity).onFling(distanceX < 0);

                return true;
            }
            return false;
        }
    });

    @InjectView(R.id.card_view_full_c) CardView cardWidgetForInfo;

    @InjectView(R.id.tv_color_params) TextView infoText;

    @InjectView(R.id.show_info_full_c) ImageView showInfo;

    @InjectView(R.id.close_info_full_c_card)ImageView closeInfo;

    @InjectView(R.id.send_info_full_c) ImageView sendInfo;

    public FragmentFullScreenColor() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        cardWidgetForInfo.setCardBackgroundColor(backCardColor);

        if (textColor == -1) {
            textColor = whiteText ? Color.WHITE : Color.BLACK;
        }

        infoText.setTextColor(textColor);
        infoText.setText(Html.fromHtml(ColorParams.composeInfoHTML(hexString, textColor)));

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

        cardWidgetForInfo.setVisibility(View.VISIBLE);
        showAnim.cancel();
        cardWidgetForInfo.startAnimation(showAnim);

        showInfo.setVisibility(View.INVISIBLE);
        btnFadeOutAnim.cancel();
        showInfo.startAnimation(btnFadeOutAnim);

        if (activity instanceof HideInfoListener) {
            ((HideInfoListener) activity).onHideInfoInvoked(false);
        }
    }

    @OnClick(R.id.close_info_full_c_card)
    public void closeInfo() {

        cardWidgetForInfo.setVisibility(View.GONE);
        hideAnim.cancel();
        cardWidgetForInfo.startAnimation(hideAnim);

        showInfo.setVisibility(View.VISIBLE);
        btnFadeInAnim.cancel();
        showInfo.startAnimation(btnFadeInAnim);

        if (activity instanceof HideInfoListener) {
            ((HideInfoListener) activity).onHideInfoInvoked(true);
        }
    }

    @OnClick(R.id.send_info_full_c)
    public void sendInfo() {
        AppHelper.fireShareIntent(activity, ColorParams.composeInfoHTML(hexString, textColor));
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

    public void setTextColor(int color) {
        textColor = color;
    }
}
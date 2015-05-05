package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.BaseColorActivity;
import com.engstuff.coloriphornia.activities.FavoriteColorsActivity;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.interfaces.ColorBoxEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class FragmentColorBox extends Fragment {

    private ColorBoxEventListener colorBoxEventListener;

    Activity activity;

    GestureOverlayView gestureLayer;
    GestureDetector gestureDetector;
    GestureLibrary gestureLibrary;

    Animation likeAnim;

    @InjectView(R.id.color_box_layout) RelativeLayout layout;

    @InjectView(R.id.btn_color_info) ImageView info;

    @InjectView(R.id.like) ImageView like;

    private String hexColorParams;

    int colorHex;
    int alpha, r, g, b; // alpha, red, green, blue

    int width;
    int height;

    private boolean whiteText;

    public FragmentColorBox() {
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().setInterpolator(new DecelerateInterpolator())
                            .scaleX(.7f).scaleY(.7f);
                    break;
                case MotionEvent.ACTION_UP:
                    v.animate().setInterpolator(new OvershootInterpolator(10f))
                            .scaleX(1f).scaleY(1f);
                    break;
            }
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gestureLayer = (GestureOverlayView) inflater
                .inflate(R.layout.fragment_color_box, container, false);

        ButterKnife.inject(this, gestureLayer);

        gestureLibrary = GestureLibraries.fromRawResource(activity, R.raw.gestures);
        gestureLibrary.load();

        info.animate().setDuration(300);
        info.setOnTouchListener(touchListener);

        like.animate().setDuration(300);
        like.setOnTouchListener(touchListener);

        likeAnim = AnimationUtils.loadAnimation(activity, R.anim.like);

        gestureLayer.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {

            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

                ArrayList<Prediction> recognized = gestureLibrary.recognize(gesture);

                if (recognized.size() > 0) {

                    Prediction prediction = recognized.get(0);

                    if (prediction.score > 1.5 && prediction.name.equals(Cv.G_YES)) {
                        performColorSave();
                    } else if (prediction.score > 1.5
                            && (prediction.name.equals(Cv.G_NEXT) || prediction.name.equals(Cv.G_PREV))) {
                        infoClick();
                    }
                }
            }
        });

        gestureDetector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                performColorSave();
                return true;
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, FavoriteColorsActivity.class));
            }
        });
        layout.removeView(like);

        return gestureLayer;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
        try {
            colorBoxEventListener = (ColorBoxEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ColorClickedListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        colorBoxEventListener = null;
        ButterKnife.reset(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeColor();
    }

    void performColorSave() {
        colorClicked();
        ((BaseColorActivity) activity).saveColorToPrefs();
        likeColor();
    }

    public void likeColor() {

        if (layout.findViewById(R.id.like) != null) {
            layout.removeView(like);
        }
        like.setImageResource(ColorParams.blackOrWhiteText(alpha, r, g, b)
                ? R.drawable.ic_loyalty_white_24dp
                : R.drawable.ic_loyalty_black_24dp);
        layout.addView(like);
        likeAnim.cancel();
        like.startAnimation(likeAnim);
    }

    public void changeColor() {

        colorHex = ColorParams.composeHex(alpha, r, g, b);
        hexColorParams = ColorParams.makeHexInfo(colorHex);

        if (layout != null) {

            layout.setBackgroundColor(colorHex);
            //noinspection deprecation
            layout.setAlpha(alpha);
        }

        boolean whiteAgain = ColorParams.blackOrWhiteText(alpha, r, g, b);

        if (whiteText != whiteAgain && colorBoxEventListener != null) {

            colorBoxEventListener.onTextColorChanged();
            whiteText = whiteAgain;
        } else if (colorBoxEventListener != null) {

            colorBoxEventListener.onTextColorChanged();
        }
    }

    public FragmentColorBox setColorParams() {

        try {
            ColorControlAbstractFragment fragmentControl = ((BaseColorActivity) getActivity()).getFragmentControl();
            alpha = fragmentControl.getAlpha();
            r = fragmentControl.getR();
            g = fragmentControl.getG();
            b = fragmentControl.getB();
        } catch (Exception ignoreNpeIfSlowDevice) {
        }
        return this;
    }

    public FragmentColorBox setColorParams(int a, int r, int g, int b) {
        alpha = a;
        this.r = r;
        this.g = g;
        this.b = b;
        return this;
    }

    public FragmentColorBox setColorParams(int alpha, int color) {

        setColorParams(alpha, Color.red(color), Color.green(color), Color.blue(color));
        return this;
    }

    public FragmentColorBox setColorParams(String hexARGB) {

        int[] argb = ColorParams.hexStringToARGB(hexARGB);

        setColorParams(argb[0], argb[1], argb[2], argb[3]);
        return this;
    }

    public FragmentColorBox setColorParams(int r, int g, int b) {

        alpha = 255;
        this.r = r;
        this.g = g;
        this.b = b;

        return this;
    }

    @OnClick(R.id.color_box_layout)
    public void colorClicked() {
        colorBoxEventListener.onColorClicked(FragmentColorBox.this);
    }

    /**
     * I need this hack for fragment swapping while saving color to favorites
     */
    @OnLongClick(R.id.color_box_layout)
    public boolean colorLongClicked() {
        colorClicked();
        return false;
    }

    @OnTouch(R.id.color_box_layout)
    public boolean colorTouched(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @OnClick(R.id.btn_color_info)
    public void infoClick() {
        colorBoxEventListener.onInfoClicked(FragmentColorBox.this);
    }

    public boolean isWhiteText() {
        return whiteText;
    }

    public ImageView getInfo() {
        return info;
    }

    public ImageView getLike() {
        return like;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public String getHexColorParams() {
        return hexColorParams;
    }

    public int getColorHex() {
        return colorHex;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getColor() {
        return ColorParams.composeHex(alpha, r, g, b);
    }
}
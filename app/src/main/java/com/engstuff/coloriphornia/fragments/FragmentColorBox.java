package com.engstuff.coloriphornia.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.BaseColorActivity;
import com.engstuff.coloriphornia.helpers.ColorParams;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsInt;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.writeToPrefs;

public class FragmentColorBox extends Fragment {

    public interface ColorBoxEventListener {

        void onColorClicked(FragmentColorBox colorBox);

        void onInfoClicked(FragmentColorBox colorBox);

        void onTextColorChanged(boolean white);
    }

    private ColorBoxEventListener colorBoxEventListener;

    Activity ctx;

    View layout;

    @InjectView(R.id.btn_color_info)
    ImageView info;

    private String rgbColorParams;
    private String hexColorParams;

    int colorHex;
    int alpha, r, g, b; // alpha, red, green, blue

    int width;
    int height;

    private boolean whiteText;

    public FragmentColorBox() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx = getActivity();

        layout = inflater.inflate(R.layout.fragment_color_box, container, false);

        ButterKnife.inject(this, layout);

        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    public void changeColor() {

        colorHex = ColorParams.composeHex(alpha, r, g, b);
        rgbColorParams = ColorParams.makeArgbInfo(alpha, r, g, b);
        hexColorParams = ColorParams.makeHexInfo(colorHex);
        layout.setBackgroundColor(colorHex);
        //noinspection deprecation
        layout.setAlpha(alpha);

        boolean whiteAgain = ColorParams.blackOrWhiteText(r, g, b);

        if (whiteText != whiteAgain) {
            colorBoxEventListener.onTextColorChanged(whiteAgain);
            whiteText = whiteAgain;
        } else colorBoxEventListener.onTextColorChanged(whiteText);
    }

    public FragmentColorBox setColorParams() {

        FragmentSeekBarsControl fragmentControl = ((BaseColorActivity) getActivity()).getFragmentControl();
        alpha = fragmentControl.getAlpha();
        r = fragmentControl.getR();
        g = fragmentControl.getG();
        b = fragmentControl.getB();
        return this;
    }

    public FragmentColorBox setColorParams(int a, int r, int g, int b) {
        alpha = a;
        this.r = r;
        this.g = g;
        this.b = b;
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

    public String getRgbColorParams() {
        return rgbColorParams;
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
}

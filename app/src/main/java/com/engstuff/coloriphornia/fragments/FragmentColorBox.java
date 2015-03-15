package com.engstuff.coloriphornia.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.ColorC;
import com.engstuff.coloriphornia.helpers.HexColorFrom4parts;

public class FragmentColorBox extends Fragment {

    public interface ColorBoxEventListener {

        void onColorClicked(FragmentColorBox colorBox);
        void onColorLongClicked(FragmentColorBox colorBox);
        void onTextColorChanged(boolean white);
    }

    private ColorBoxEventListener colorBoxEventListener;

    private ImageView iv;

    private String rgbColorParams;
    private String hexColorParams;

    int colorHex;
    int alpha, r, g, b; // alpha, red, green, blue

    int width;
    int height;

    private boolean whiteText;

    public FragmentColorBox() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_color_box, container, false);

        iv = (ImageView) rootView.findViewById(R.id.colorView);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorBoxEventListener.onColorClicked(FragmentColorBox.this);
            }
        });

        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                colorBoxEventListener.onColorLongClicked(FragmentColorBox.this);
                return true;
            }
        });
        return rootView;
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
    public void onDetach() {
        super.onDetach();
        colorBoxEventListener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (((ViewGroup) getView().getParent()).getId() == R.id.color_box_container2) {
            setColorParams(255, 60, 130,200).changeColor();
        } else if (((ViewGroup) getView().getParent()).getId() == R.id.color_box_container){
            setColorParams().changeColor();
        }
    }

    public void changeColor() {

        colorHex = HexColorFrom4parts.composeHex(alpha, r, g, b);
        rgbColorParams = "\u03b1: " + alpha + " r:" + r + " g:" + g + " b:" + b;
        hexColorParams = "#" + Integer.toHexString(colorHex);
        iv.setBackgroundColor(colorHex);
        //noinspection deprecation
        iv.setAlpha(alpha);

        boolean whiteAgain = blackOrWhiteText(r, g, b);

        if (whiteText != whiteAgain) {
            colorBoxEventListener.onTextColorChanged(whiteAgain);
        }

        whiteText = whiteAgain;
    }

    public FragmentColorBox setColorParams() {

        FragmentSeekBarsControl fragmentControl = ((ColorC) getActivity()).getFragmentControl();
        alpha = fragmentControl.getAlpha();
        r = fragmentControl.getR();
        g = fragmentControl.getG();
        b = fragmentControl.getB();
        return this;
    }

    public FragmentColorBox setColorParams(int a, int r, int g, int b) {
        alpha = a; this.r = r; this.g = g; this.b = b;
        return this;
    }

    public FragmentColorBox setColorParams(int r, int g, int b) {
        alpha = 255; this.r = r; this.g = g; this.b = b;
        return this;
    }

    private boolean blackOrWhiteText(int r, int g, int b) {
        return  (r + g + b > 480 || g > 200) ? false : true;
    }

    public boolean isWhiteText() {
        return whiteText;
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

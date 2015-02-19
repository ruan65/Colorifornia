package com.engstuff.coloriphornia.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
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
    }

    private ColorBoxEventListener colorBoxEventListener;

    private FragmentSeekBarsControl fragmentControl;

    private ImageView iv;

    private String rgbColorParams;
    private String hexColorParams;

    int colorHex;
    int alpha, r, g, b; // alpha, red, green, blue

    private Bitmap currentBitmap;
    private Canvas mCanvas;

    int width;
    int height;

    private boolean whiteText = true;

    public FragmentColorBox() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentControl = ((ColorC) getActivity()).getFragmentControl();

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

        // This one needed since iv.getWidth(); gives O otherwise
        view.post(new Runnable() {
            @Override
            public void run() {

                width = iv.getWidth();
                height = iv.getHeight();

                currentBitmap = Bitmap
                        .createBitmap(width, height, Bitmap.Config.ARGB_8888);

                mCanvas = new Canvas(currentBitmap);
                if (((ViewGroup) getView().getParent()).getId() == R.id.color_box_container2) {
                    setColorParams(255, 60, 130,200).changeColor();
                } else {
                    setColorParams().changeColor();
                }
            }
        });
    }


    public void changeColor() {

        colorHex = HexColorFrom4parts.composeHex(alpha, r, g, b);
        mCanvas.drawRGB(r, g, b);

        Paint textPainter = prepareTextPainter(r, g, b);

        rgbColorParams = "\u03b1: " + alpha + " r:" + r + " g:" + g + " b:" + b;
        hexColorParams = "#" + Integer.toHexString(colorHex);

        mCanvas.drawText(rgbColorParams, 10, 40, textPainter);
        mCanvas.drawText(hexColorParams, 10, 80, textPainter);
        mCanvas.drawText("Hold to go full screen", 10, 180, textPainter);


        iv.setImageBitmap(currentBitmap);
        //noinspection deprecation
        iv.setAlpha(alpha);
    }

    public FragmentColorBox setColorParams() {
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

    private Paint prepareTextPainter(int r, int g, int b) {

        Paint tp = new Paint();

        if (r + g + b > 480 || g > 200) {

            tp.setColor(Color.BLACK);
            whiteText = false;

        } else {

            tp.setColor(Color.WHITE);
            whiteText = true;
        }

        tp.setTextSize(20);

        return tp;
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

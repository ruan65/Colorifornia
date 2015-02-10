package com.engstuff.coloriphornia.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.R;

public class ColorC extends BaseActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {


    private Bitmap mBitmap;
    private Canvas mCanvas;
    private ImageView iv;

    private SeekBar sbAlfa, sbRed, sbGreen, sbBlue;

    private int alpha, r, g, b; // alpha, red, green, blue
    private int colorHex;
    private Paint tp;

    public final static String EXTRA_MESSAGE_COLOR = "color_parameters";
    public final static String EXTRA_MESSAGE_TEXT_COLOR = "text_color_parameters";

    private String rgbColorParams;
    private String hexColorParams;
    private boolean white = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iv = (ImageView) findViewById(R.id.colorView);
        iv.setOnClickListener(this);

        sbAlfa = (SeekBar) findViewById(R.id.sbAlfa);
        sbRed = (SeekBar) findViewById(R.id.sbRed);
        sbGreen = (SeekBar) findViewById(R.id.sbGreen);
        sbBlue = (SeekBar) findViewById(R.id.sbBlue);

        sbAlfa.setOnSeekBarChangeListener(this);
        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);

        alpha = getResources().getInteger(R.integer.sbMax);
        r = g = b = getResources().getInteger(R.integer.sbProgress);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.color_c;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        int w = iv.getWidth();
        int h = iv.getHeight();

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        tp = new Paint();

        changeColor();
    }

    @SuppressWarnings("deprecation")
    private void changeColor() {

        colorHex = HexColorFrom4parts.composeHex(alpha, r, g, b);

        mCanvas.drawRGB(r, g, b);

        if ((r > 160 && g > 160 && b > 160) || (r > 200 && g > 200)) {
            tp.setColor(Color.BLACK);
            white = false;
        } else {
            tp.setColor(Color.WHITE);
            white = true;
        }

        tp.setTextSize(30);

        rgbColorParams = "\u03b1: " + alpha + " r:" + r + " g:" + g + " b:" + b;
        hexColorParams = "#" + Integer.toHexString(colorHex);

        mCanvas.drawText(rgbColorParams, 10, 40, tp);
        mCanvas.drawText(hexColorParams, 10, 80, tp);
        mCanvas.drawText("Full screen \u21aa", 10, 180, tp);

        iv.setImageBitmap(mBitmap);
        iv.setAlpha(alpha);

    }

    @Override
    public void onClick(View v) {

        String[] colorParams = {
                rgbColorParams,
                hexColorParams
        };

        Intent i = new Intent(this, FullScreenColor.class);

        i.putExtra(EXTRA_MESSAGE_COLOR, colorParams);
        i.putExtra(EXTRA_MESSAGE_TEXT_COLOR, white);

        startActivity(i);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()) {
            case R.id.sbAlfa:
                alpha = progress;
                break;
            case R.id.sbRed:
                r = progress;
                break;
            case R.id.sbGreen:
                g = progress;
                break;
            case R.id.sbBlue:
                b = progress;
                break;
            default:
                break;
        }
        changeColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

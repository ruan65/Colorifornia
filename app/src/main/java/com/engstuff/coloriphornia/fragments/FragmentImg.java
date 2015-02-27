package com.engstuff.coloriphornia.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.helpers.ZoomableImageView;

public class FragmentImg extends Fragment implements View.OnTouchListener {



    public interface ImageGetColorListener {

        void onPickColor();
    }

    private ImageGetColorListener imageGetColorListener;

    private ImageView iv;

    private int r, g, b;

    Bitmap bitmap;

    public FragmentImg() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        iv = new ImageView(getActivity());

        iv = new ZoomableImageView(getActivity());

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bitten_apple);

        iv.setImageBitmap(bitmap);

//        iv.setOnTouchListener(this);

        return iv;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            imageGetColorListener = (ImageGetColorListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ImageGetColorListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        imageGetColorListener = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            int pixel = bitmap.getPixel(x, y);

            r = Color.red(pixel);
            g = Color.green(pixel);
            b = Color.blue(pixel);

            Log.d("ml", "pixel = " + pixel);
            Log.d("ml", "x = " + event.getX() + ", y = " + event.getY());
            Log.d("ml", "x int = " + x + ", y int = " + y);


            Log.d("ml", "rgb: " + r + " " + g + " " + b);

            imageGetColorListener.onPickColor();
        }
        return false;
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

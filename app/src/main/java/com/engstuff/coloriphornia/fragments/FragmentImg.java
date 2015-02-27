package com.engstuff.coloriphornia.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.helpers.ZoomableImageView;
import com.engstuff.coloriphornia.interfaces.ImageGetColorListener;

public class FragmentImg extends Fragment {

    private ZoomableImageView ziv;

    Bitmap bitmap;

    public FragmentImg() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ziv = new ZoomableImageView(getActivity());

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bitten_apple);

        ziv.setImageBitmap(bitmap);

        return ziv;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            imageGetColorListener = (ImageGetColorListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement ImageGetColorListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        imageGetColorListener = null;
//    }

    public int getR() {
        return ziv.getR();
    }

    public int getG() {
        return ziv.getG();
    }

    public int getB() {
        return ziv.getB();
    }
}

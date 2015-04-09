package com.engstuff.coloriphornia.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.engstuff.coloriphornia.R;

public class RoundColorControlFragment extends ColorControlAbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_round_control, container, false);

        Log.d("ml", "rootView : " + rootView + " " + rootView.getId());
        return rootView;
    }

    @Override
    public void setControls(int alpha, int r, int g, int b) {

    }
}

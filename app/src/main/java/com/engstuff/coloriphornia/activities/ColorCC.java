package com.engstuff.coloriphornia.activities;

import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;

public class ColorCC extends ColorC {

    FragmentColorBox fragmentColorBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentColorBox2 = new FragmentColorBox();

        getFragmentManager().beginTransaction()
                .add(R.id.color_box_container2, fragmentColorBox2)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.color_cc;
    }

}

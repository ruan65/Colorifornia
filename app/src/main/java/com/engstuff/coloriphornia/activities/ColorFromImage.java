package com.engstuff.coloriphornia.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.FragmentImg;
import com.engstuff.coloriphornia.interfaces.ImageGetColorListener;

public class ColorFromImage extends BaseActivity implements ImageGetColorListener {

    protected FragmentImg fragmentImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentImg = new FragmentImg();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction
                .add(R.id.img_container, fragmentImg)
                .add(R.id.color_box_container_color_from_image, fragmentColorBox)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_color_from_image;
    }


    @Override
    public void onPickColor() {

        fragmentColorBox.setColorParams(
                fragmentImg.getR(),
                fragmentImg.getG(),
                fragmentImg.getB()
        ).changeColor();
    }


}

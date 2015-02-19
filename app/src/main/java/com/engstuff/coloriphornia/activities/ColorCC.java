package com.engstuff.coloriphornia.activities;

import android.content.Intent;
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
    public void onColorLongClicked(FragmentColorBox color) {

        changeFragment(color);

        Intent i = new Intent(this, FullScreenColorCC.class);

        String[] colorParams = {
                color.getRgbColorParams(),
                color.getHexColorParams()
        };

        i.putExtra(EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(EXTRA_MESSAGE_TEXT_COLOR_1, color.isWhiteText());

        int fragmentId = color.getId() == R.id.color_box_container
                ? R.id.color_box_container2 : R.id.color_box_container;

        color = (FragmentColorBox) getFragmentManager().findFragmentById(fragmentId);

        String[] colorParams2 = {
                color.getRgbColorParams(),
                color.getHexColorParams()
        };

        i.putExtra(EXTRA_MESSAGE_COLOR_2, colorParams2);
        i.putExtra(EXTRA_MESSAGE_TEXT_COLOR_2, color.isWhiteText());

        startActivity(i);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.color_cc;
    }

}

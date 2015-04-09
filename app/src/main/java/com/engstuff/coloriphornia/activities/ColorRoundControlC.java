package com.engstuff.coloriphornia.activities;

import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.RoundColorControlFragment;

public class ColorRoundControlC extends BaseColorActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentControl = new RoundColorControlFragment();

        getFragmentManager().beginTransaction()

                .add(R.id.color_control_container_round, fragmentControl)
                .add(R.id.color_box_container_round, fragmentColorBox)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_color_round_control;
    }
}

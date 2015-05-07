package com.engstuff.coloriphornia.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentFullScreenColor;
import com.engstuff.coloriphornia.interfaces.OnFlingListener;

public class FullScreenColorCC extends Activity implements OnFlingListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_cc);

        Intent intent = getIntent();

        FragmentFullScreenColor fragment1 = new FragmentFullScreenColor();
        FragmentFullScreenColor fragment2 = new FragmentFullScreenColor();

        fragment1.setHexString(intent.getStringExtra(Cv.EXTRA_MESSAGE_COLOR_1));
        fragment2.setHexString(intent.getStringExtra(Cv.EXTRA_MESSAGE_COLOR_2));

        getFragmentManager().beginTransaction()
                .add(R.id.frame_full_screen_color_1, fragment1)
                .add(R.id.frame_full_screen_color_2, fragment2)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_l, R.anim.slide_out_l);
    }

    @Override
    public void onFling(boolean next) {
        // ignore
    }
}

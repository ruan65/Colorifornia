package com.engstuff.coloriphornia.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentFullScreenColor;
import com.engstuff.coloriphornia.interfaces.OnFlingListener;


public class FullScreenColorC extends Activity implements OnFlingListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_c);

        Intent intent = getIntent();

        FragmentFullScreenColor fragment = new FragmentFullScreenColor();

        fragment.setHexString(intent.getStringExtra(Cv.EXTRA_MESSAGE_COLOR_1));

        fragment.setWhiteText(intent.getBooleanExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, false));

        getFragmentManager().beginTransaction()
                .add(R.id.frame_for_full_screen_color_fragment, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_l, R.anim.slide_out_l);
    }


    @Override
    public void onFling(boolean next) {

    }
}

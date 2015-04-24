package com.engstuff.coloriphornia.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentFullScreenColor;
import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.helpers.PrefsHelper;
import com.engstuff.coloriphornia.interfaces.OnFlingListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FullScreenColorC extends Activity implements OnFlingListener {

    boolean calledFromFavorites;
    private List<String> savedColorsSet;
    String startedColor;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_color_c);

        Intent intent = getIntent();

        startedColor = intent.getStringExtra(Cv.EXTRA_MESSAGE_COLOR_1);

        calledFromFavorites = intent.getBooleanExtra(Cv.CALLED_FROM_FAVORITES, false);

        if (calledFromFavorites) {
            savedColorsSet = new ArrayList<>(
                    PrefsHelper.readFromPrefsAll(this, Cv.SAVED_COLORS).keySet());
            position = savedColorsSet.indexOf(startedColor);
        }

        getFragmentManager().beginTransaction()
                .add(R.id.frame_for_full_screen_color_fragment, prepareFragment(startedColor))
                .commit();
    }

    private FragmentFullScreenColor prepareFragment(String hex) {

        FragmentFullScreenColor fragment = new FragmentFullScreenColor();

        fragment.setHexString(hex);

        fragment.setWhiteText(ColorParams.blackOrWhiteText(hex));

        return fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_l, R.anim.slide_out_l);
    }

    @Override
    public void onFling(boolean next) {

        if (calledFromFavorites && savedColorsSet.size() > 0 && position != -1) {

            String hex = next
                    ? ( position < savedColorsSet.size() - 1 ? savedColorsSet.get(++position)
                                                           : savedColorsSet.get(position = 0) )
                    : position > 0 ? savedColorsSet.get(--position)
                                   : savedColorsSet.get(position = savedColorsSet.size() - 1);

            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_for_full_screen_color_fragment, prepareFragment(hex))
                    .commit();
        }
    }
}

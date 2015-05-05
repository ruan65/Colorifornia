package com.engstuff.coloriphornia.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.SeekBarsColorControlFragment;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public class FontAndBackgroundActivity extends BaseColorActivity {

    private TextView text;
    private boolean tuneColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        text = (TextView) findViewById(R.id.font_color);

        fragmentControl = new SeekBarsColorControlFragment();

        getFragmentManager().beginTransaction()

                .add(R.id.color_control_container, fragmentControl)
                .add(R.id.color_box_container, fragmentColorBox)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        AppHelper.setColorToColorBox(this, Cv.LAST_BACkGROUND, fragmentControl, currentColorBox);
        AppHelper.setLikesAndInfo(this, currentColorBox);

        text.setTextColor(PrefsHelper.readFromPrefsInt(
                this, Cv.IMAGE_FRAGMENT_RETAINED, Cv.LAST_COLOR_FONT));

        unlockInfo = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, Cv.LAST_BACkGROUND,
                fragmentColorBox.getHexColorParams());

        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, Cv.LAST_COLOR_FONT,
                text.getCurrentTextColor());

        unlockInfo = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean rv = super.onCreateOptionsMenu(menu);
        tuneTextIcon.setVisible(true);
        return rv;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.tune_text) {

            tuneColor = !tuneColor;

            tuneTextIcon.setIcon(tuneColor
                    ? R.drawable.ic_format_color_fill_white_36dp
                    : R.drawable.ic_text_format_white_36dp);

            if (tuneColor) {

            } else {
//                fragmentControl.setControls(currentColorBox.getColor());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColorControlChange(int p, int id) {

        if (tuneColor) {
            text.setTextColor(fragmentControl.getColor());
        } else {
            super.onColorControlChange(p, id);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_font_and_background;
    }
}

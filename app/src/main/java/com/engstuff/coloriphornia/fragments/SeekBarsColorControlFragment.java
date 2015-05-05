package com.engstuff.coloriphornia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.BaseColorActivity;
import com.engstuff.coloriphornia.activities.FontAndBackgroundActivity;
import com.engstuff.coloriphornia.components.views.SeekBarMinusPlus;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

import java.lang.ref.WeakReference;
import java.util.List;

public class SeekBarsColorControlFragment extends ColorControlAbstractFragment
        implements SeekBar.OnSeekBarChangeListener {

    private MenuItem openAlpha;
    private BaseColorActivity act;

    boolean alphaMode;

    private SeekBarMinusPlus sbRed, sbGreen, sbBlue, sbAlpha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seek_bars_control, container, false);

        act = (BaseColorActivity) getActivity();

        sbRed = (SeekBarMinusPlus) rootView.findViewById(R.id.sb_c_red);
        sbRed.init(this, Cv.RED);

        sbGreen = (SeekBarMinusPlus) rootView.findViewById(R.id.sb_c_green);
        sbGreen.init(this, Cv.GREEN);

        sbBlue = (SeekBarMinusPlus) rootView.findViewById(R.id.sb_c_blue);
        sbBlue.init(this, Cv.BLUE);

        sbAlpha = (SeekBarMinusPlus) rootView.findViewById(R.id.sb_c_alpha);
        sbAlpha.init(this, Cv.ALPHA);

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        alphaMode = PrefsHelper.readFromPrefsBoolean(act, act.getString(R.string.prefs_alpha_mode));

        if (openAlpha != null) showAlpha(alphaMode);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        openAlpha = menu.findItem(R.id.open_alpha);
        openAlpha.setVisible(true);
        openAlpha.setIcon(alphaMode ? R.drawable.ic_blur_off_white_36dp
                : R.drawable.ic_blur_on_white_36dp);
        showAlpha(alphaMode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.open_alpha) {

            showAlpha(sbAlpha.getVisibility() == View.GONE);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void makeCurrentColorsOpaque() {

        List<WeakReference<Fragment>> fragments = act.getAllAttachedFragments();

        for (WeakReference<Fragment> f : fragments) {

            Fragment fragment = f.get();

            if (fragment.getClass().equals(FragmentColorBox.class)) {
                ((FragmentColorBox) fragment).setAlpha(255);
                ((FragmentColorBox) fragment).changeColor();
            }
        }
        sbAlpha.getSeekBar().setProgress(255);
        act.getProgress().setVisibility(View.INVISIBLE);

        if (act.getClass().equals(FontAndBackgroundActivity.class)) {

            ((FontAndBackgroundActivity) act).setTextColorOpaque();
        }
    }

    protected void showAlpha(boolean show) {

        FrameLayout fl = act.getColorControlContainer();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fl.getLayoutParams();

        layoutParams.weight = getResources().getInteger(show ? R.integer.control_weight
                : R.integer.control_weight_low);

        fl.setLayoutParams(layoutParams);

        sbAlpha.setVisibility(show ? View.VISIBLE : View.GONE);

        openAlpha.setIcon(show ? R.drawable.ic_blur_off_white_36dp
                : R.drawable.ic_blur_on_white_36dp);

        PrefsHelper.writeToPrefsDefault(act, act.getString(R.string.prefs_alpha_mode), alphaMode = show);

        if (!show) makeCurrentColorsOpaque();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        int id = (int) seekBar.getTag();

        switch (id) {
            case Cv.ALPHA:
                alpha = progress;
                break;
            case Cv.RED:
                r = progress;
                break;
            case Cv.GREEN:
                g = progress;
                break;
            case Cv.BLUE:
                b = progress;
                break;
        }
        colorChangeListener.onColorControlChange(progress, id);
    }

    public void setControls(int a, int r, int g, int b) {

        sbAlpha.getSeekBar().setProgress(a);
        sbRed.getSeekBar().setProgress(r);
        sbGreen.getSeekBar().setProgress(g);
        sbBlue.getSeekBar().setProgress(b);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        colorChangeListener.onColorControlStartTracking();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        colorChangeListener.onColorControlStopTracking();
    }
}
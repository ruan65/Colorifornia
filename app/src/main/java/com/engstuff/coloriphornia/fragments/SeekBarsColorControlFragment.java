package com.engstuff.coloriphornia.fragments;

import android.app.Fragment;
import android.os.Bundle;
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
import com.engstuff.coloriphornia.components.views.SeekBarMinusPlus;
import com.engstuff.coloriphornia.data.Cv;

import java.lang.ref.WeakReference;
import java.util.List;

public class SeekBarsColorControlFragment extends ColorControlAbstractFragment
        implements SeekBar.OnSeekBarChangeListener {

    private MenuItem openAlpha;
    private BaseColorActivity activity;

    private SeekBarMinusPlus sbRed, sbGreen, sbBlue, sbAlpha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seek_bars_control, container, false);

        activity = (BaseColorActivity) getActivity();

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        openAlpha = menu.findItem(R.id.open_alpha);
        openAlpha.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        FrameLayout fl = activity.getColorControlContainer();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fl.getLayoutParams();

        if (item.getItemId() == R.id.open_alpha) {

            if (sbAlpha.getVisibility() == View.GONE) {

                layoutParams.weight = getResources().getInteger(R.integer.control_weight);
                fl.setLayoutParams(layoutParams);

                sbAlpha.setVisibility(View.VISIBLE);

                openAlpha.setIcon(R.drawable.ic_blur_off_white_36dp);
            }
            else {
                sbAlpha.setVisibility(View.GONE);

                List<WeakReference<Fragment>> fragments = activity.getAllAttachedFragments();

                for (WeakReference<Fragment> f : fragments) {

                    Fragment fragment = f.get();

                    if (fragment.getClass().equals(FragmentColorBox.class)) {
                        ((FragmentColorBox) fragment).setAlpha(255);
                        ((FragmentColorBox) fragment).changeColor();
                    }
                }
                sbAlpha.getSeekBar().setProgress(255);
                activity.getProgress().setVisibility(View.INVISIBLE);
                layoutParams.weight = getResources().getInteger(R.integer.control_weight_low);
                fl.setLayoutParams(layoutParams);

                openAlpha.setIcon(R.drawable.ic_blur_on_white_36dp);
            }
        }
        return super.onOptionsItemSelected(item);
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
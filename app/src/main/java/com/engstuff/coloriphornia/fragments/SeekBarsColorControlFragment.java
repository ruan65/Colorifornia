package com.engstuff.coloriphornia.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.BaseColorActivity;
import com.engstuff.coloriphornia.components.views.SeekBarButton;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class SeekBarsColorControlFragment extends ColorControlAbstractFragment
        implements SeekBar.OnSeekBarChangeListener {

    private SeekBar sbAlpha, sbRed, sbGreen, sbBlue;
    private MenuItem openAlpha;
    private RelativeLayout alphaFrame;
    private BaseColorActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seek_bars_control, container, false);

        activity = (BaseColorActivity) getActivity();

        sbAlpha = (SeekBar) rootView.findViewById(R.id.sbAlpha);
        sbRed = (SeekBar) rootView.findViewById(R.id.sbRed);
        sbRed = (SeekBar) rootView.findViewById(R.id.sbRed);
        sbGreen = (SeekBar) rootView.findViewById(R.id.sbGreen);
        sbBlue = (SeekBar) rootView.findViewById(R.id.sbBlue);

        sbAlpha.setOnSeekBarChangeListener(this);
        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);

        SeekBarButton mAlpha = (SeekBarButton) rootView.findViewById(R.id.minus_alpha);
        SeekBarButton mRed = (SeekBarButton) rootView.findViewById(R.id.minus_red);
        SeekBarButton mGreen = (SeekBarButton) rootView.findViewById(R.id.minus_green);
        SeekBarButton mBlue = (SeekBarButton) rootView.findViewById(R.id.minus_blue);

        SeekBarButton pAlpha = (SeekBarButton) rootView.findViewById(R.id.plus_alpha);
        SeekBarButton pRed = (SeekBarButton) rootView.findViewById(R.id.plus_red);
        SeekBarButton pGreen = (SeekBarButton) rootView.findViewById(R.id.plus_green);
        SeekBarButton pBlue = (SeekBarButton) rootView.findViewById(R.id.plus_blue);

        mAlpha.init(sbAlpha, colorChangeListener, false);
        mRed.init(sbRed, colorChangeListener, false);
        mGreen.init(sbGreen, colorChangeListener, false);
        mBlue.init(sbBlue, colorChangeListener, false);

        pAlpha.init(sbAlpha, colorChangeListener, true);
        pRed.init(sbRed, colorChangeListener, true);
        pGreen.init(sbGreen, colorChangeListener, true);
        pBlue.init(sbBlue, colorChangeListener, true);

        alpha = sbAlpha.getProgress();
        r = sbRed.getProgress(); g = sbGreen.getProgress(); b = sbBlue.getProgress();

        setHasOptionsMenu(true);

        alphaFrame = (RelativeLayout) rootView.findViewById(R.id.seek_frame_alpha);

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
            if (alphaFrame.getVisibility() == View.GONE) {

                layoutParams.weight = getResources().getInteger(R.integer.control_weight);
                fl.setLayoutParams(layoutParams);

                alphaFrame.setVisibility(View.VISIBLE);

                openAlpha.setIcon(R.drawable.ic_blur_off_white_36dp);
            }
            else {
                alphaFrame.setVisibility(View.GONE);

                List<WeakReference<Fragment>> fragments = activity.getAllAttachedFragments();

                for (WeakReference<Fragment> f : fragments) {

                    Fragment fragment = f.get();

                    if (fragment.getClass().equals(FragmentColorBox.class)) {
                        ((FragmentColorBox) fragment).setAlpha(255);
                        ((FragmentColorBox) fragment).changeColor();
                    }
                }
                sbAlpha.setProgress(255);
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

        int id = seekBar.getId();

        switch (id) {
            case R.id.sbAlpha:
                alpha = progress;
                break;
            case R.id.sbRed:
                r = progress;
                break;
            case R.id.sbGreen:
                g = progress;
                break;
            case R.id.sbBlue:
                b = progress;
                break;
        }
        colorChangeListener.onColorControlChange(progress, id);
    }

    public void setControls(int a, int r, int g, int b) {
        sbAlpha.setProgress(a);
        sbRed.setProgress(r);
        sbGreen.setProgress(g);
        sbBlue.setProgress(b);
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
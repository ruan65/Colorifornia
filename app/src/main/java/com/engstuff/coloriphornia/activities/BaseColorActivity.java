package com.engstuff.coloriphornia.activities;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.ColorControlAbstractFragment;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.interfaces.ColorBoxEventListener;
import com.engstuff.coloriphornia.interfaces.ColorControlChangeListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.writeToPrefs;

public abstract class BaseColorActivity extends MockUpActivity implements
        ColorBoxEventListener,
        ColorControlChangeListener {

    protected ColorControlAbstractFragment fragmentControl;
    protected FragmentColorBox fragmentColorBox;
    protected FragmentColorBox currentColorBox;

    protected boolean unlockInfo;
    protected TextView progress;

    protected List<WeakReference<Fragment>> allAttachedFragments = new ArrayList<>();

    private FrameLayout colorControlContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentColorBox = currentColorBox = new FragmentColorBox();

        colorControlContainer = (FrameLayout) findViewById(R.id.color_control_container);

        progress = (TextView) findViewById(R.id.sb_progress);
    }

    @Override
    public void onResume() {
        super.onResume();

        registerForContextMenu(fragmentColorBox.getView());
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        allAttachedFragments.add(new WeakReference<>(fragment));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            /**
             * This two calls I need to unset "like" tag after color deleting from favorites
             */
            for (WeakReference<Fragment> ref : allAttachedFragments) {

                Fragment f = ref.get();

                if (f.getClass().equals(FragmentColorBox.class)) {
                    AppHelper.unsetLike((FragmentColorBox) f);
                    AppHelper.setLike(this, (FragmentColorBox) f);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        boolean rv = super.onCreateOptionsMenu(menu);
        sendIcon.setVisible(true);
        return rv;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_color_box, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ctx_menu_save_color:

                saveColorToPrefs();

                currentColorBox.likeColor();

                return true;

            case R.id.ctx_menu_share_color:

                AppHelper.fireShareIntent(this, composeEmailBody(true, textColor));

                return true;
        }
        return super.onContextItemSelected(item);
    }

    protected String composeEmailBody(boolean calledFromContextMenu, int fontColor) {

        StringBuilder result = new StringBuilder(getString(R.string.email_body_header));

        if (!calledFromContextMenu) {

            for (WeakReference<Fragment> ref : allAttachedFragments) {

                Fragment f = ref.get();

                if (f.getClass().equals(FragmentColorBox.class)) {

                    result.append("<p>" + ColorParams.composeInfoHTML(
                            ((FragmentColorBox) f).getHexColorParams(), fontColor) + "</p>");
                }
            }
        } else {
            result.append("<p>" + ColorParams.composeInfoHTML(
                    currentColorBox.getHexColorParams(), fontColor) + "</p>");
        }
        return result.toString();
    }

    protected void switchInfo(int p, int id) {

        progress.setVisibility(View.VISIBLE);

        String val = String.valueOf(p);

        switch (id) {

            case Cv.ALPHA:
                progress.setTextColor(Color.BLACK);
                progress.setText(getString(R.string.alpha) + val);
                break;
            case Cv.RED:
                progress.setTextColor(Color.RED);
                progress.setText(getString(R.string.red) + val);
                break;
            case Cv.GREEN:
                progress.setTextColor(getResources().getColor(R.color.dk_green));
                progress.setText(getString(R.string.green) + val);
                break;
            case Cv.BLUE:
                progress.setTextColor(Color.BLUE);
                progress.setText(getString(R.string.blue) + val);
                break;
        }
    }

    protected void animInfoAndGone() {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(800);
        progress.setAnimation(anim);
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onColorControlChange(int p, int id) {

        currentColorBox
                .setColorParams()
                .changeColor();

        if (unlockInfo) {
            switchInfo(p, id);
            animInfoAndGone();
        }
    }

    @Override
    public void onColorControlChange(int progress, int color, int alpha, float[] hsv) {

        currentColorBox
                .setColorParams(alpha, color)
                .changeColor();
    }

    @Override
    public void onColorControlStartTracking() {

        AppHelper.unsetLike(currentColorBox);
    }

    @Override
    public void onColorControlStopTracking() {

        AppHelper.setLike(this, currentColorBox);
    }

    @Override
    public void onColorClicked(FragmentColorBox box) {
        changeFragment(box);
    }

    @Override
    public void onTextColorChanged() {
        if (null != currentColorBox) AppHelper.setInfoIcon(currentColorBox);
    }

    protected void changeFragment(FragmentColorBox box) {
    }

    @Override
    public void onInfoClicked(FragmentColorBox box) {

        changeFragment(box);

        AppHelper.startFullColorC(this, box.getHexColorParams());
    }

    public boolean isWhiteText() {
        return currentColorBox.isWhiteText();
    }

    public ColorControlAbstractFragment getFragmentControl() {
        return fragmentControl;
    }

    public void saveColorToPrefs() {

        String hexColorParams = currentColorBox.getHexColorParams();
        int colorHex = currentColorBox.getColorHex();

        writeToPrefs(activity, Cv.SAVED_COLORS, hexColorParams, colorHex);
    }

    public FrameLayout getColorControlContainer() {
        return colorControlContainer;
    }

    public List<WeakReference<Fragment>> getAllAttachedFragments() {
        return allAttachedFragments;
    }

    public TextView getProgress() {
        return progress;
    }
}
package com.engstuff.coloriphornia.activities;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.FragmentSeekBarsControl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsInt;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.writeToPrefs;

public abstract class BaseColorActivity extends MockUpActivity
        implements FragmentColorBox.ColorBoxEventListener, FragmentSeekBarsControl.ColorControlChangeListener {

    protected FragmentSeekBarsControl fragmentControl;
    protected FragmentColorBox fragmentColorBox;
    protected FragmentColorBox currentColorBox;

    List<WeakReference<Fragment>> allAttachedFragments = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentControl = new FragmentSeekBarsControl();
        fragmentColorBox = currentColorBox = new FragmentColorBox();
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_color_box, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ctx_menu_save_color:
                saveColorToPrefs();
                return true;

            case R.id.ctx_menu_share_color:

                return true;
        }
        return super.onContextItemSelected(item);
    }

    protected String composeEmailBody() {
        StringBuilder result = new StringBuilder()
                .append("<h3>Colors chosen via \"Colorifornia\" mobile app: </h3>");

        for (WeakReference<Fragment> ref : allAttachedFragments) {
            Fragment f = ref.get();
            if (f.getClass().equals(FragmentColorBox.class)) {
                result.append("<p>" + ((FragmentColorBox) f).getHexColorParams() + "</p>");
            }
        }

        return result.toString();
    }

    @Override
    public void onColorControlChange() {
        currentColorBox
                .setColorParams()
                .changeColor();
    }

    @Override
    public void onColorClicked(FragmentColorBox color) {
        changeFragment(color);
    }

    @Override
    public void onTextColorChanged(boolean white) {
        currentColorBox.getInfo().setImageResource(
                white ? R.drawable.ic_info_white
                      : R.drawable.ic_info_black);
    }

    protected void changeFragment(FragmentColorBox color) {
    }

    @Override
    public void onInfoClicked(FragmentColorBox color) {

        changeFragment(color);

        String[] colorParams = {
                color.getRgbColorParams(),
                color.getHexColorParams()
        };

        Intent i = new Intent(this, FullScreenColorC.class);

        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, fragmentColorBox.isWhiteText());

        startActivity(i);
    }

    public boolean isWhiteText() {
        return currentColorBox.isWhiteText();
    }

    public FragmentSeekBarsControl getFragmentControl() {
        return fragmentControl;
    }

    protected void saveColorToPrefs() {

        String hexColorParams = currentColorBox.getHexColorParams();
        int colorHex = currentColorBox.getColorHex();

        writeToPrefs(ctx, Cv.SAVED_COLORS, hexColorParams, colorHex);

        if (readFromPrefsInt(ctx, Cv.SAVED_COLORS, hexColorParams) == colorHex) {

            Toast toast = new Toast(ctx);

            TextView view = (TextView)
                    getLayoutInflater().inflate(R.layout.toast_custom, null);

            view.setBackgroundColor(colorHex);

            view.setTextColor(isWhiteText() ? Color.WHITE : Color.BLACK);
            view.setText("This color's been saved\n\n            " + hexColorParams);

            toast.setView(view);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }
}

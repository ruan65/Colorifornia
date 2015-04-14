package com.engstuff.coloriphornia.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

import java.util.ArrayList;

public class FavoriteColorsActivity extends MockUpActivity {

    ArrayList<FavoriteColor> fColorsList = new ArrayList<>();

    int gridSize;

    boolean modeColorsOperation;

    FavoritesAdapter fAdapter;
    GridView lvFavorites;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lvFavorites = (GridView) findViewById(R.id.favorite_colors);

        gridSize = calculateGridSize();

        lvFavorites.setColumnWidth(gridSize);

        refreshData();

        fAdapter = new FavoritesAdapter(this, fColorsList);
        lvFavorites.setAdapter(fAdapter);

        lvFavorites.setOnItemClickListener(viewModeOnClickListener);
        lvFavorites.setOnItemLongClickListener(getOnItemLongClickListener());
    }

    private AdapterView.OnItemLongClickListener getOnItemLongClickListener() {

        return new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (!modeColorsOperation) {

                    setColorOperationsMode();
                }
                checkColor(view, position, true);
                return true;
            }
        };
    }

    AdapterView.OnItemClickListener viewModeOnClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            FavoriteColor c = fColorsList.get(position);

            AppHelper.startFullColorC(FavoriteColorsActivity.this,
                    ColorParams.makeArgbInfo(c.hexString), c.hexString);
        }
    };

    AdapterView.OnItemClickListener operationModeOnClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (fColorsList.get(position).isChecked()) {

                checkColor(view, position, false);

                boolean allUnchecked = true;

                for (FavoriteColor fc : fColorsList) {

                    if (fc.isChecked()) {
                        allUnchecked = false;
                        break;
                    }
                }

                if (allUnchecked) {

                    setViewMode();
                }

            } else {
                checkColor(view, position, true);
            }
        }
    };

    private void setColorOperationsMode() {

        modeColorsOperation = true;

        lvFavorites.setOnItemClickListener(operationModeOnClickListener);

        checkModeIcon.setVisible(false);
        binIcon.setVisible(true);
    }

    private void setViewMode() {

        modeColorsOperation = false;

        lvFavorites.setOnItemClickListener(viewModeOnClickListener);

        checkModeIcon.setVisible(true);
        binIcon.setVisible(false);
    }

    private void checkColor(View view, int position, boolean check) {

        view.findViewById(R.id.check_favorite).setVisibility(check ? View.VISIBLE : View.GONE);

        fColorsList.get(position).setChecked(check);
    }

    private void refreshData() {

        fColorsList.clear();

        String[] ss = PrefsHelper.readFromPrefsAllToArray(this, Cv.SAVED_COLORS);

        for (String s : ss) {
            fColorsList.add(new FavoriteColor(s));
        }
    }

    int calculateGridSize() {
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        return gridSize = (int) (p.x / 3.3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
        fAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = super.onCreateOptionsMenu(menu);
        checkModeIcon.setVisible(true);
        return retValue;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.bin:

                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Discard checked")
                        .setMessage("All checked colors will be deleted saved colors list!?")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for (FavoriteColor fc : fColorsList) {
                                    if (fc.isChecked()) {

                                        PrefsHelper.erasePrefs(activity, Cv.SAVED_COLORS, fc.hexString);
                                    }
                                }
                                activity.recreate();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ignore
                            }
                        })
                        .show();
                break;

            case R.id.check_mode:

                setColorOperationsMode();

                for (int i = 0; i < fColorsList.size(); i++) {
                    lvFavorites.getChildAt(i).performClick();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_favorite_colors;
    }

    @Override
    protected String composeEmailBody(boolean calledFromContextMenu) {
        return "";
    }

    /**
     * Custom adapter
     */
    private class FavoritesAdapter extends ArrayAdapter<FavoriteColor> {

        public FavoritesAdapter(Context context, ArrayList<FavoriteColor> data) {
            super(context, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RelativeLayout frame = (RelativeLayout) convertView;

            if (frame == null) {
                frame = (RelativeLayout) getLayoutInflater()
                        .inflate(R.layout.favorite_color_item, null);
            }
            FavoriteColor color = fColorsList.get(position);

            frame.setMinimumHeight(gridSize);
            frame.setMinimumWidth(gridSize);
            frame.findViewById(R.id.check_favorite).setVisibility(
                    color.isChecked() ? View.VISIBLE : View.GONE);
            frame.setBackgroundColor(color.color);

            return frame;
        }
    }

    private class FavoriteColor {

        String hexString;
        int color;
        boolean checked;

        private FavoriteColor(String h) {
            hexString = h;
            color = (int) Long.parseLong(hexString.substring(1), 16);
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isChecked() {
            return checked;
        }
    }
}

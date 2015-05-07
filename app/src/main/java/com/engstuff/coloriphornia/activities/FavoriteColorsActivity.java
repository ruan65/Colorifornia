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
import android.widget.Toast;

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

    private boolean allUnchecked = true;

    FavoritesAdapter fAdapter;
    GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gridView = (GridView) findViewById(R.id.favorite_colors);

        gridSize = calculateGridSize();

        gridView.setColumnWidth(gridSize);

        refreshData();

        fAdapter = new FavoritesAdapter(this, fColorsList);
        gridView.setAdapter(fAdapter);

        gridView.setOnItemClickListener(viewModeOnClickListener);
        gridView.setOnItemLongClickListener(getOnItemLongClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        modeColorsOperation = false;

        if (checkModeIcon != null) {
            checkCurrentMode();
        }

        refreshData();
        fAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = super.onCreateOptionsMenu(menu);

        checkCurrentMode(); // this steps I need to prevent menu changes while open/close nav drawer
        return retValue;
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

            AppHelper.startFullColorC(FavoriteColorsActivity.this, c.hexString, true);
        }
    };

    AdapterView.OnItemClickListener operationModeOnClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (fColorsList.get(position).isChecked()) {

                checkColor(view, position, false);

                allUnchecked = areAllUnchecked();

                if (allUnchecked) {

                    setViewMode();
                }

            } else {
                checkColor(view, position, true);
            }
        }
    };

    private boolean areAllUnchecked() {

        for (FavoriteColor fc : fColorsList) {

            if (fc.isChecked()) {

                return false;
            }
        }
        return true;
    }

    private void setColorOperationsMode() {

        modeColorsOperation = true;

        gridView.setOnItemClickListener(operationModeOnClickListener);

        checkModeIcon.setVisible(false);
        binIcon.setVisible(true);
        undoIcon.setVisible(true);
        sendIcon.setVisible(true);
    }

    private void setViewMode() {

        modeColorsOperation = false;

        checkAllColors(false);

        gridView.setOnItemClickListener(viewModeOnClickListener);

        checkModeIcon.setVisible(true);
        binIcon.setVisible(false);
        undoIcon.setVisible(false);
        sendIcon.setVisible(false);
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

    private void checkCurrentMode() {

        if (modeColorsOperation) {
            setColorOperationsMode();
        } else {
            setViewMode();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.bin:

                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Discard checked")
                        .setMessage(
                                fColorsList.size() > 0 && !areAllUnchecked()
                                        ? "All checked colors will be erased. Proceed?"
                                        : "An empty list. Nothing to delete."
                        )
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for (FavoriteColor fc : fColorsList) {
                                    if (fc.isChecked()) {

                                        PrefsHelper.erasePrefs(activity, Cv.SAVED_COLORS, fc.hexString);
                                    }
                                }
                                refreshData();
                                fAdapter.notifyDataSetChanged();
                                setViewMode();
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

            case R.id.send:

                if (areAllUnchecked()) {
                    Toast.makeText(this, getString(R.string.email_error_no_selected),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;

            case R.id.check_mode:

                setColorOperationsMode();
                checkAllColors(true);
                break;

            case R.id.undo:

                checkAllColors(false);
                setViewMode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void checkAllColors(boolean checkOrUncheckThatIsTheQuestion) {

        allUnchecked = !checkOrUncheckThatIsTheQuestion;

        for (FavoriteColor fc : fColorsList) {
            fc.setChecked(checkOrUncheckThatIsTheQuestion);
        }
        fAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_favorite_colors;
    }

    @Override
    protected String composeEmailBody(boolean calledFromContextMenu, int fontColor) {

        StringBuilder result = new StringBuilder(getString(R.string.email_body_header));

        for (FavoriteColor fc : fColorsList) {

            if (fc.isChecked()) {

                result.append("<p>" + ColorParams.composeInfoHTML(fc.hexString, fontColor) + "</p>");
            }
        }
        return result.toString();
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
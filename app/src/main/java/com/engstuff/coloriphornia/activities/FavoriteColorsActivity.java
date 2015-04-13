package com.engstuff.coloriphornia.activities;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
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

        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FavoriteColor c = fColorsList.get(position);
                AppHelper.startFullColorC(FavoriteColorsActivity.this,
                        ColorParams.makeArgbInfo(c.hexString), c.hexString);
            }
        });
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
    protected int getLayoutResource() {
        return R.layout.activity_favorite_colors;
    }

    @Override
    protected String composeEmailBody(boolean calledFromContextMenu) {
        return "";
    }

    /**
     *  Custom adapter
     */
    private class FavoritesAdapter extends ArrayAdapter<FavoriteColor> {

        public FavoritesAdapter(Context context, ArrayList<FavoriteColor> data) {
            super(context, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RelativeLayout frame = (RelativeLayout) convertView;
            View checkMark;

            FavoriteColor fc = fColorsList.get(position);

            if (frame == null) {
                frame = (RelativeLayout) getLayoutInflater()
                        .inflate(R.layout.favorite_color_item, null);
            }

            checkMark = frame.findViewById(R.id.check_favorite);
//            checkMark.setVisibility(View.GONE);

            frame.setMinimumHeight(gridSize);
            frame.setMinimumWidth(gridSize);
            frame.setBackgroundColor(fc.color);

            return frame;
        }
    }

    private class FavoriteColor {

        String hexString;
        int color;

        private FavoriteColor(String h) {
            hexString = h;
            color = (int) Long.parseLong(hexString.substring(1), 16);
        }
    }
}

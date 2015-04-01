package com.engstuff.coloriphornia.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
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

        String[] ss = PrefsHelper.readFromPrefsAllToArray(this, Cv.SAVED_COLORS);

        for (String s : ss) {
            fColorsList.add(new FavoriteColor(s));
        }

        fAdapter = new FavoritesAdapter(this, fColorsList);
        lvFavorites.setAdapter(fAdapter);

        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FullScreenColorC.class);
                startActivity(intent);
            }
        });
    }

    int calculateGridSize() {
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        return gridSize = (int) (p.x / 3.1);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_favorite_colors;
    }

    @Override
    protected String composeEmailBody() {
        return "";
    }

    private class FavoritesAdapter extends ArrayAdapter<FavoriteColor> {

        public FavoritesAdapter(Context context, ArrayList<FavoriteColor> data) {
            super(context, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            FavoriteColor fc = fColorsList.get(position);

            if (v == null) {
                v = new View(getApplicationContext());
            }
            v.setMinimumHeight(gridSize);
            v.setMinimumWidth(gridSize);
            v.setBackgroundColor(fc.color);

            return v;
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

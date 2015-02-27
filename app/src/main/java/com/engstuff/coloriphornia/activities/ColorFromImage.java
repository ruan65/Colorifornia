package com.engstuff.coloriphornia.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.FragmentImg;
import com.engstuff.coloriphornia.interfaces.ImageGetColorListener;

public class ColorFromImage extends BaseActivity implements ImageGetColorListener,
        FragmentColorBox.ColorBoxEventListener {

    protected FragmentImg fragmentImg;
    protected FragmentColorBox fragmentColorBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentImg = new FragmentImg();
        fragmentColorBox = new FragmentColorBox();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction
                .add(R.id.img_container, fragmentImg)
                .add(R.id.color_box_container_color_from_image, fragmentColorBox)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_color_from_image;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_color_from_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPickColor() {

        fragmentColorBox.setColorParams(
                fragmentImg.getR(),
                fragmentImg.getG(),
                fragmentImg.getB()
        ).changeColor();
    }

    @Override
    public void onColorClicked(FragmentColorBox colorBox) {
        // ignore
    }

    @Override
    public void onColorLongClicked(FragmentColorBox color) {

        String[] colorParams = {
                color.getRgbColorParams(),
                color.getHexColorParams()
        };

        Intent i = new Intent(this, FullScreenColorC.class);

        i.putExtra(ColorC.EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(ColorC.EXTRA_MESSAGE_TEXT_COLOR_1, fragmentColorBox.isWhiteText());

        startActivity(i);
    }
}

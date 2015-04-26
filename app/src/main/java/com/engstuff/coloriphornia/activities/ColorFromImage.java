package com.engstuff.coloriphornia.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentImg;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.PrefsHelper;
import com.engstuff.coloriphornia.interfaces.ImageGetColorListener;

public class ColorFromImage extends BaseColorActivity
        implements ImageGetColorListener {

    private static final int GALLERY_INTENT_CALLED = 0xbaaa;
    private static final int GALLERY_KITKAT_INTENT_CALLED = 0xbeee;

    protected FragmentImg fragmentImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();

        fragmentImg = (FragmentImg) fragmentManager.findFragmentByTag(Cv.IMAGE_FRAGMENT_RETAINED);

        fragmentImg = fragmentImg == null ? new FragmentImg() : fragmentImg;


        fragmentManager.beginTransaction()

                .add(R.id.img_container, fragmentImg, Cv.IMAGE_FRAGMENT_RETAINED)
                .add(R.id.color_box_container_color_from_image, fragmentColorBox)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        boolean rv = super.onCreateOptionsMenu(menu);

        openPhotoIcon.setVisible(true);

        return rv;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.open_image:

                Intent intent = new Intent();
                intent.setType("image/jpeg");

                if (Build.VERSION.SDK_INT < 19) {

                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            getResources().getString(R.string.select_picture)), GALLERY_INTENT_CALLED);
                } else {

                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        String stringUriCurrentImage = PrefsHelper.readFromPrefsString(this,
                Cv.PREFS_RETAIN, Cv.CURRENT_IMAGE);
        String currentHex = PrefsHelper.readFromPrefsString(this,
                Cv.PREFS_RETAIN, Cv.CURRENT_COLOR_IMG);

        float x = PrefsHelper.readFromPrefsInt(this, Cv.PREFS_RETAIN, Cv.AIM_X);
        float y = PrefsHelper.readFromPrefsInt(this, Cv.PREFS_RETAIN, Cv.AIM_Y);

        if (!"".equals(stringUriCurrentImage)) {
            fragmentImg.putBitmap(Uri.parse(stringUriCurrentImage));
        } else {
            fragmentImg.getZiv().setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.triangles));
        }

        if (!"".equals(currentHex)) {
            fragmentColorBox.setColorParams(currentHex).changeColor();
        } else fragmentColorBox.setColorParams(255, 130, 58).changeColor();

        if (x != 0 && y != 0) {
            fragmentImg.getAim().setX(x);
            fragmentImg.getAim().setY(y);
        }
        AppHelper.setLikesAndInfo(this, fragmentColorBox);
    }

    @Override
    public void onPause() {
        super.onPause();
        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, Cv.CURRENT_COLOR_IMG, currentColorBox.getHexColorParams());
        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, Cv.AIM_X, (int) fragmentImg.getAim().getX());
        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, Cv.AIM_Y, (int) fragmentImg.getAim().getY());
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || null == data) return;

        Uri uri = null;

        switch (requestCode) {
            case GALLERY_INTENT_CALLED:
                uri = data.getData();
                break;

            case GALLERY_KITKAT_INTENT_CALLED:
                uri = data.getData();
                final int takeFlags = data.getFlags() &
                        (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(uri, takeFlags);
                break;
        }
        fragmentImg.putBitmap(uri);

        PrefsHelper.writeToPrefs(this,
                Cv.PREFS_RETAIN, Cv.CURRENT_IMAGE, uri.toString());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_color_from_image;
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
    public void onFirstFingerDown() {

        AppHelper.unsetLike(fragmentColorBox);
    }

    @Override
    public void onLastFingerUp() {
        AppHelper.setLikesAndInfo(this, fragmentColorBox);
    }

    @Override
    public void onTextColorChanged() {
        super.onTextColorChanged();
        fragmentImg.getAim().setImageResource(
                fragmentColorBox.isWhiteText()
                        ? R.drawable.ic_target_w
                        : R.drawable.ic_target_b);
    }
}
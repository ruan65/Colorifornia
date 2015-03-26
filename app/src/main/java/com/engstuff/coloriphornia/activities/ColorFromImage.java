package com.engstuff.coloriphornia.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.FragmentImg;
import com.engstuff.coloriphornia.helpers.PrefsHelper;
import com.engstuff.coloriphornia.interfaces.ImageGetColorListener;
import com.software.shell.fab.ActionButton;

import static android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT;

public class ColorFromImage extends BaseActivity
        implements ImageGetColorListener, View.OnClickListener {

    private static final int GALLERY_INTENT_CALLED = 0xbaaa;
    private static final int GALLERY_KITKAT_INTENT_CALLED = 0xbeee;

    protected FragmentImg fragmentImg;
    protected ActionButton aBtn;
    private FrameLayout imgContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgContainer = (FrameLayout) findViewById(R.id.img_container);

        fragmentImg = new FragmentImg();

        getFragmentManager().beginTransaction()

                .add(R.id.img_container, fragmentImg)
                .add(R.id.color_box_container_color_from_image, fragmentColorBox)
                .commit();

        aBtn = (ActionButton) findViewById(R.id.action_button);
        aBtn.setOnClickListener(this);
        aBtn.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String stringUriCurrentImage = PrefsHelper.readFromPrefsString(this,
                Cv.PREFS_RETAIN, Cv.CURRENT_IMAGE);
        if (!"".equals(stringUriCurrentImage)) {
            fragmentImg.putBitmap(Uri.parse(stringUriCurrentImage));
        } else {
            fragmentImg.getZiv().setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.triangles));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {

            RelativeLayout.LayoutParams lp =
                    new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

            lp.setMargins(imgContainer.getWidth() - aBtn.getWidth() - 10,
                    imgContainer.getHeight() + aBtn.getHeight() / 3,
                    0, 0);

            aBtn.setLayoutParams(lp);
            aBtn.show();

        } else aBtn.hide();
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
    public void onTextColorChanged(boolean white) {
        fragmentImg.getAim().setImageResource(!fragmentColorBox.isWhiteText()
                ? R.drawable.ic_target_w
                : R.drawable.ic_target_b);
    }

    @Override
    public void onClick(View v) {

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
    }

    @Override
    public void onColorLongClicked(FragmentColorBox color) {
        super.onColorLongClicked(color);
    }
}

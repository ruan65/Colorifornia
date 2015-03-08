package com.engstuff.coloriphornia.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.FragmentImg;
import com.engstuff.coloriphornia.interfaces.ImageGetColorListener;

public class ColorFromImage extends BaseActivity
        implements ImageGetColorListener, View.OnClickListener {

    private static final int GALLERY_INTENT_CALLED = 0xa;
    private static final int GALLERY_KITKAT_INTENT_CALLED = 0xb;

    protected FragmentImg fragmentImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentImg = new FragmentImg();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction
                .add(R.id.img_container, fragmentImg)
                .add(R.id.color_box_container_color_from_image, fragmentColorBox)
                .commit();

        Button btnGetImage = (Button) findViewById(R.id.btn_get_image);
        btnGetImage.setOnClickListener(this);
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
}

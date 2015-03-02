package com.engstuff.coloriphornia.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.components.views.ZoomableImageView;
import com.engstuff.coloriphornia.helpers.ImageHelper;
import com.engstuff.coloriphornia.helpers.Logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FragmentImg extends Fragment {

    private ZoomableImageView ziv;

    Activity ctx;

    public FragmentImg() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ctx = getActivity();
        ziv = new ZoomableImageView(ctx);

        ziv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bitten_apple));

        return ziv;
    }

    public void resetBitmap(Uri uri) {

        Logging.logMemory();

        try {
            int pxx = 300, pxy = 300;

            String path = ImageHelper.getRealImagePath(ctx, uri);

            Logging.log("path: " + path);

            File file = new File(path);

            Logging.log(file.getAbsolutePath());

            Bitmap bmp = ImageHelper.decodeSampledBitmapFromResource(
                    file.getAbsolutePath(), pxx, pxy, Bitmap.Config.RGB_565);

            Logging.log(String.format("Required size = %sx%s, bitmap size = %sx%s, byteCount = %sK",
                    pxx, pxy, bmp.getWidth(), bmp.getHeight(), bmp.getByteCount() / 1024));

            ziv.setImageBitmap(bmp);
            ziv.invalidate();
        } catch (Exception e) {

            Toast.makeText(ctx, ctx.getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
            Logging.log("error: " + e);
        }

        Logging.logMemory();
    }


    public int getR() {
        return ziv.getR();
    }

    public int getG() {
        return ziv.getG();
    }

    public int getB() {
        return ziv.getB();
    }
}

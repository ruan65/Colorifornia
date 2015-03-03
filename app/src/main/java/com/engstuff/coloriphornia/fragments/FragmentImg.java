package com.engstuff.coloriphornia.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.ColorFromImage;
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
        return ziv;
    }

    public void putBitmap(Uri uri) {

        Logging.logMemory();


        try {
            new GetImageFromGallery().execute(uri);

        } catch (Exception e) {

            Toast.makeText(ctx, ctx.getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
            Logging.log("error: " + e);
        }

        Logging.logMemory();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ziv.getDrawable() == null) {
            ctx.findViewById(R.id.btn_get_image).performClick();
        }
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

    private class GetImageFromGallery extends AsyncTask<Uri, Void, Bitmap> {

        int pxx, pxy;
        @Override
        protected Bitmap doInBackground(Uri... uris) {
            pxx = pxy = (int) ctx.getResources().getDimension(R.dimen.bitmap_size);

            String path = ImageHelper.getRealImagePath(ctx, uris[0]);

            Logging.log("path: " + path);

            File file = new File(path);

            Logging.log(file.getAbsolutePath());

            return ImageHelper.decodeSampledBitmapFromResource(
                    file.getAbsolutePath(), pxx, pxy, Bitmap.Config.RGB_565);
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            Logging.log(String.format("Required size = %sx%s, bitmap size = %sx%s, byteCount = %sK",
                    pxx, pxy, bmp.getWidth(), bmp.getHeight(), bmp.getByteCount() / 1024));

            ziv.setImageBitmap(bmp);
            ziv.invalidate();
        }
    }
}

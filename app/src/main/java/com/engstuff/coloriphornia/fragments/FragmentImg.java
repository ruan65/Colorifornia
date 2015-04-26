package com.engstuff.coloriphornia.fragments;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.BaseColorActivity;
import com.engstuff.coloriphornia.components.views.FrameLayoutWithAim;
import com.engstuff.coloriphornia.components.views.ZoomableImageView;
import com.engstuff.coloriphornia.helpers.ImageHelper;
import com.engstuff.coloriphornia.helpers.Logging;

import java.io.File;

import static android.view.ViewGroup.LayoutParams;

public class FragmentImg extends Fragment {

    private ZoomableImageView ziv;
    private ImageView aim;
    private FrameLayoutWithAim frame;

    BaseColorActivity ctx;
    int pxx, pxy;

    public FragmentImg() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ctx = (BaseColorActivity) getActivity();

        pxx = pxy = (int) ctx.getResources().getDimension(R.dimen.bitmap_size);

        frame = (FrameLayoutWithAim) inflater.inflate(R.layout.fragment_img, container, false);

        ziv = new ZoomableImageView(ctx);

        aim = createAimImageView();

        frame.setAim(aim);
        frame.setZiv(ziv);
        frame.addView(ziv);
        frame.addView(aim);

        return frame;
    }

    private ImageView createAimImageView() {

        DisplayMetrics display = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(display);

        ImageView aim = new ImageView(ctx);
        aim.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        aim.setImageResource(ctx.isWhiteText() ? R.drawable.ic_target_w
                : R.drawable.ic_target_b);
        aim.setX(display.heightPixels / display.scaledDensity / 3f);
        aim.setY((display.widthPixels / display.scaledDensity / 1.8f));

        return aim;
    }

    public void putBitmap(Uri uri) {

        try {
            new GetImageFromGallery().execute(uri);

        } catch (Exception e) {

            Toast.makeText(ctx, ctx.getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
            Logging.log("error: " + e.getMessage());
        }
    }

    public ImageView getAim() {
        return aim;
    }

    public ZoomableImageView getZiv() {
        return ziv;
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


        @Override
        protected Bitmap doInBackground(Uri... uris) {

            try {
                String path = ImageHelper.getRealImagePath(ctx, uris[0]);

                File file = new File(path);

                return ImageHelper.decodeSampledBitmapFromResource(
                        file.getAbsolutePath(), pxx, pxy, Bitmap.Config.RGB_565);
            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            ziv.setImageBitmap(bmp);
        }
    }
}
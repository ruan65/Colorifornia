package com.engstuff.coloriphornia.fragments;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.engstuff.coloriphornia.activities.BaseActivity;
import com.engstuff.coloriphornia.components.views.FrameLayoutWithAim;
import com.engstuff.coloriphornia.components.views.ZoomableImageView;
import com.engstuff.coloriphornia.helpers.ImageHelper;
import com.engstuff.coloriphornia.helpers.Logging;
import com.software.shell.fab.ActionButton;

import java.io.File;

import static android.view.ViewGroup.LayoutParams;

public class FragmentImg extends Fragment {

    private ZoomableImageView ziv;
    private ImageView aim;
    private FrameLayoutWithAim frame;

    BaseActivity ctx;
    int pxx, pxy;

    public FragmentImg() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ctx = (BaseActivity) getActivity();

        pxx = pxy = (int) ctx.getResources().getDimension(R.dimen.bitmap_size);

        frame = (FrameLayoutWithAim) inflater.inflate(R.layout.fragment_img, container, false);

        ziv = new ZoomableImageView(ctx);

        aim = createAimImageView();

        frame.setAim(aim);
        frame.setZiv(ziv);
        frame.addView(ziv);
        frame.addView(aim);

//        Bitmap bm = ImageHelper.decodeSampledBitmapFromResource()
//
//        ziv.setImageBitmap();

        return frame;
    }

    private ImageView createAimImageView() {

        DisplayMetrics display = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(display);

        ImageView aim = new ImageView(ctx);
        aim.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        aim.setImageResource(ctx.isWhiteText() ? R.drawable.ic_target_w
                : R.drawable.ic_target_b);
        aim.setX(display.heightPixels / display.scaledDensity / 2);
        aim.setY((float) (display.widthPixels * 0.62 / display.scaledDensity / 2));

        return aim;
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

            String path = ImageHelper.getRealImagePath(ctx, uris[0]);

            File file = new File(path);

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

    public ImageView getAim() {
        return aim;
    }
}

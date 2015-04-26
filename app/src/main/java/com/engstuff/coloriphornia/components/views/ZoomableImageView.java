package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.interfaces.ImageGetColorListener;

public class ZoomableImageView extends ImageView implements View.OnTouchListener {

    private Matrix matrix = new Matrix();

    private ImageGetColorListener imageGetColorListener;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int CLICK = 3;

    private int mode = NONE;

    private PointF last = new PointF();
    private PointF start = new PointF();

    private int r, g, b;

    private float minScale = 1f;
    private float maxScale = 1f;
    private float[] m;

    private float redundantXSpace, redundantYSpace;
    private float width, height;
    private float saveScale = 1f;
    private float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

    private float aimX, aimY;

    private ScaleGestureDetector mScaleDetector;

    public ZoomableImageView(final Context context) {
        super(context);

        maxScale = context.getResources().getInteger(R.integer.matrix_max_scale);
        imageGetColorListener = (ImageGetColorListener) context;

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        matrix.setTranslate(1f, 1f);
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        m = new float[9];

        setOnTouchListener(this);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (null != bm) {
            bmWidth = bm.getWidth();
            bmHeight = bm.getHeight();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus) {
            setRGB(getWidth() / 2, getHeight() / 2, getCurrentBitmap(this));
        }
    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        mScaleDetector.onTouchEvent(event);

        matrix.getValues(m);

        float x = m[Matrix.MTRANS_X];
        float y = m[Matrix.MTRANS_Y];

        float eventX = event.getX();
        float eventY = event.getY();

        PointF curr = new PointF(eventX, eventY);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                last.set(eventX, eventY);
                start.set(last);
                mode = DRAG;
                imageGetColorListener.onFirstFingerDown();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                last.set(eventX, eventY);
                start.set(last);
                mode = ZOOM;
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == ZOOM || (mode == DRAG && saveScale > minScale)) {

                    float deltaX = curr.x - last.x;
                    float deltaY = curr.y - last.y;
                    float scaleWidth = Math.round(origWidth * saveScale);
                    float scaleHeight = Math.round(origHeight * saveScale);

                    if (scaleWidth < width) {
                        deltaX = 0;
                        if (y + deltaY > 0)
                            deltaY = -y;
                        else if (y + deltaY < -bottom)
                            deltaY = -(y + bottom);
                    } else if (scaleHeight < height) {
                        deltaY = 0;
                        if (x + deltaX > 0)
                            deltaX = -x;
                        else if (x + deltaX < -right)
                            deltaX = -(x + right);
                    } else {
                        if (x + deltaX > 0)
                            deltaX = -x;
                        else if (x + deltaX < -right)
                            deltaX = -(x + right);

                        if (y + deltaY > 0)
                            deltaY = -y;
                        else if (y + deltaY < -bottom)
                            deltaY = -(y + bottom);
                    }
                    matrix.postTranslate(deltaX, deltaY);
                    last.set(curr.x, curr.y);
                }
                changeColor((int) aimX, (int) aimY);
                break;

            case MotionEvent.ACTION_UP:

                mode = NONE;
                int xDiff = (int) Math.abs(curr.x - start.x);
                int yDiff = (int) Math.abs(curr.y - start.y);
                if (xDiff < CLICK && yDiff < CLICK) {
                    performClick();
                }
                imageGetColorListener.onLastFingerUp();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }
        setImageMatrix(matrix);
        invalidate();
        return true;
    }

    public void changeColor(int x, int y) {
        setRGB(x, y, getCurrentBitmap(this));
        imageGetColorListener.onPickColor();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;

            saveScale *= mScaleFactor;

            if (saveScale > maxScale) {

                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;

            } else if (saveScale < minScale) {

                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }
            right = width * saveScale - width - (2 * redundantXSpace * saveScale);
            bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);

            if (origWidth * saveScale <= width || origHeight * saveScale <= height) {

                matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);

                if (mScaleFactor < 1) {

                    matrix.getValues(m);

                    float x = m[Matrix.MTRANS_X];
                    float y = m[Matrix.MTRANS_Y];

                    if (mScaleFactor < 1) {

                        if (Math.round(origWidth * saveScale) < width) {

                            if (y < -bottom)

                                matrix.postTranslate(0, -(y + bottom));

                            else if (y > 0)

                                matrix.postTranslate(0, -y);
                        } else {

                            if (x < -right)

                                matrix.postTranslate(-(x + right), 0);

                            else if (x > 0)

                                matrix.postTranslate(-x, 0);
                        }
                    }
                }
            } else {

                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                matrix.getValues(m);

                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];

                if (mScaleFactor < 1) {

                    if (x < -right)

                        matrix.postTranslate(-(x + right), 0);
                    else if (x > 0)

                        matrix.postTranslate(-x, 0);
                    if (y < -bottom)

                        matrix.postTranslate(0, -(y + bottom));
                    else if (y > 0)
                        matrix.postTranslate(0, -y);
                }
            }
            return true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        //Fit to screen.
        float scale;
        float scaleX = width / bmWidth;
        float scaleY = height / bmHeight;

        scale = Math.min(scaleX, scaleY);
        matrix.setScale(scale, scale);
        setImageMatrix(matrix);
        saveScale = 1f;

        // Center the image
        redundantYSpace = height - (scale * bmHeight);
        redundantXSpace = width - (scale * bmWidth);
        redundantYSpace /= 2;
        redundantXSpace /= 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = width - 2 * redundantXSpace;
        origHeight = height - 2 * redundantYSpace;
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        setImageMatrix(matrix);
    }

    private Bitmap getCurrentBitmap(ImageView iv) {

        iv.setDrawingCacheEnabled(true);

        iv.buildDrawingCache(true);

        Bitmap bitmap = Bitmap.createBitmap(iv.getDrawingCache());

        iv.setDrawingCacheEnabled(false);

        return bitmap;
    }

    private void setRGB(int x, int y, Bitmap bitmap) {

        try {
            int pixel = bitmap.getPixel(x, y);

            r = Color.red(pixel);
            g = Color.green(pixel);
            b = Color.blue(pixel);
        } catch (IllegalArgumentException justSkipThisTime) {
        }

    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public void setAimCoords(float x, float y) {
        aimX = x;
        aimY = y;
    }
}
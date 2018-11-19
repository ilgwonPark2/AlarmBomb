package com.example.ilgwon.alarmbomb.module_decibel_meter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.ilgwon.alarmbomb.R;

public class DecibelMeter extends View {

    private float scaleWidth, scaleHeight;
    private int newWidth, newHeight;
    private Matrix mMatrix = new Matrix();
    private Bitmap indicatorBitmap;
    private Paint paint = new Paint();
    static final long ANIMATION_INTERVAL = 20;

    public DecibelMeter(Context context) {
        super(context);
    }

    public DecibelMeter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * init method.
     */
    private void init() {
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noise_index);
        int bitmapWidth = myBitmap.getWidth();
        int bitmapHeight = myBitmap.getHeight();
        newWidth = getWidth();
        newHeight = getHeight();
        // Get the zoom ratio
        scaleWidth = ((float) newWidth) / (float) bitmapWidth;
        scaleHeight = ((float) newHeight) / (float) bitmapHeight;
        //  Set the scale of mMatrix
        mMatrix.postScale(scaleWidth, scaleHeight);
        //  Get the same and background width and height of the pointer map bitmap
        indicatorBitmap = Bitmap.createBitmap(myBitmap, 0, 0, bitmapWidth, bitmapHeight, mMatrix, true);
        //  init paint
        paint = new Paint();
        paint.setTextSize(44);
        paint.setAntiAlias(true);  //Anti-aliasing
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
    }

    public void refresh() {
        postInvalidateDelayed(ANIMATION_INTERVAL); //Sub-thread refresh view
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * onDraw Callback
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (indicatorBitmap == null) {
            init();
        }
        //  Draw the current decibel on the meter.
        //  The relative position of the sheet
        mMatrix.setRotate(getAngle(DecibelData.dbCount), newWidth / 2, newHeight * 215 / 460);
        //  draw the bitmap, paint
        canvas.drawBitmap(indicatorBitmap, mMatrix, paint);
        //  draw the current decibel to the meter
        canvas.drawText((int) DecibelData.dbCount + " DB", newWidth / 2, newHeight * 36 / 46, paint);
    }

    /**
     * getting Angle
     */
    private float getAngle(float db) {
        // returning angle, referenced formula in stackoverflow.
        return (db - 85) * 5 / 3;
    }

}

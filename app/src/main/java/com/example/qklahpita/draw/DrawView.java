package com.example.qklahpita.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Lenovo-PC on 2/1/2018.
 */

public class DrawView extends View {
    private Canvas canvas;
    private Path path;
    private Paint paint;

    private Bitmap bitmap;

    @SuppressLint("ResourceAsColor")
    public DrawView(Context context, Bitmap bitmap) {
        super(context);

        canvas = new Canvas();
        path = new Path();

        paint = new Paint();
        paint.setColor(DrawActivity.checkColor);
        paint.setStrokeWidth(DrawActivity.checkSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        this.bitmap = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
        }
        canvas = new Canvas(bitmap);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                paint.setColor(DrawActivity.checkColor);
                paint.setStrokeWidth(DrawActivity.checkSize);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());

                break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, paint);
                path.reset();
                break;
        }

        invalidate();
        return true;
    }
}

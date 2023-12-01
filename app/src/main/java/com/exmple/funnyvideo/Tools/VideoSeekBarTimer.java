package com.exmple.funnyvideo.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

import com.exmple.funnyvideo.R;

public class VideoSeekBarTimer extends AppCompatImageView {
    boolean aBoolean1;
    int anInt;
    int anInt1;
    int anInt11;
    int anInt12;
    int anInt13;
    int anInt14;
    int anInt2;
    int anInt3;
    int anInt4;
    int anInt5;
    int anInt7;
    int anInt8;
    int anInt9;
    Bitmap bitmap1;
    int dimensionPixelOffset;
    Paint paint1;
    String time;

    public VideoSeekBarTimer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.anInt = 100;
        this.paint1 = new Paint();
        this.anInt2 = 3;
        this.anInt3 = 15;
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.time_seekbar_line);
        this.dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.default_margin);
    }

    public VideoSeekBarTimer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.anInt = 100;
        this.paint1 = new Paint();
        this.anInt2 = 3;
        this.anInt3 = 15;
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.time_seekbar_line);
        this.dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.default_margin);
    }

    public VideoSeekBarTimer(Context context) {
        super(context);
        this.anInt = 100;
        this.paint1 = new Paint();
        this.anInt2 = 3;
        this.anInt3 = 15;
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.time_seekbar_line);
        this.dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.default_margin);
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (isInEditMode()) {
            return;
        }
        a();
    }

    private void a() {
        this.anInt9 = (getHeight() / 2) - (this.bitmap1.getHeight() / 2);
        this.anInt7 = this.bitmap1.getWidth() / 2;
        if (this.anInt12 == 0 || this.anInt14 == 0) {
            this.anInt12 = this.dimensionPixelOffset;
            this.anInt14 = getWidth() - this.dimensionPixelOffset;
        }
        this.anInt4 = a(this.anInt3) - (this.dimensionPixelOffset * 2);
        this.anInt5 = (getHeight() / 2) - this.anInt2;
        this.anInt1 = (getHeight() / 2) + this.anInt2;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.aBoolean1) {
            canvas.drawBitmap(this.bitmap1, this.anInt8 - this.anInt7, 0.0f, this.paint1);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getContext().getResources().getColor(R.color.white));
//            canvas.drawRoundRect(new RectF(this.anInt8 - this.bitmap1.getWidth(), 0.0f, this.anInt8 + this.bitmap1.getWidth(), 40.0f), 50.0f, 50.0f, paint);
//            paint.setColor(ViewCompat.MEASURED_STATE_MASK);
//            paint.setTextSize(25.0f);
            Rect rect = new Rect();
            paint.getTextBounds(String.valueOf(this.time), 0, String.valueOf(this.time).length(), rect);
//            canvas.drawText(String.valueOf(this.time), this.anInt8 - (paint.measureText(String.valueOf(this.time)) / 2.0f), 40 - (rect.height() / 2), paint);
        }
    }

    private void b() {
        int i = this.anInt12;
        int i2 = this.dimensionPixelOffset;
        if (i < i2) {
            this.anInt12 = i2;
        }
        if (this.anInt14 < i2) {
            this.anInt14 = i2;
        }
        if (this.anInt12 > getWidth() - this.dimensionPixelOffset) {
            this.anInt12 = getWidth() - this.dimensionPixelOffset;
        }
        if (this.anInt14 > getWidth() - this.dimensionPixelOffset) {
            this.anInt14 = getWidth() - this.dimensionPixelOffset;
        }
        invalidate();
    }

    private int a(int i) {
        int i2 = this.dimensionPixelOffset;
        return ((int) (((getWidth() - (i2 * 2.0d)) / this.anInt) * i)) + i2;
    }

    public void setLeftProgress(int i) {
        if (i < this.anInt13 - this.anInt3) {
            this.anInt12 = a(i);
        }
        b();
    }

    public void setRightProgress(int i) {
        if (i > this.anInt11 + this.anInt3) {
            this.anInt14 = a(i);
        }
        b();
    }

    public int getLeftProgress() {
        return this.anInt11;
    }

    public int getRightProgress() {
        return this.anInt13;
    }

    public void videoPlayingProgress(int i) {
        this.aBoolean1 = true;
        this.anInt8 = a(i);
        invalidate();
    }

    public void videoPlayingTime(String str) {
        this.aBoolean1 = true;
        this.time = str;
        invalidate();
    }

    public void removeVideoStatusThumb() {
        this.aBoolean1 = false;
        invalidate();
    }

    public void setMaxValue(int i) {
        this.anInt = i;
    }

    public void setProgressMinDiff(int i) {
        this.anInt3 = i;
        this.anInt4 = a(i);
    }
}

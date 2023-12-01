package com.exmple.funnyvideo.ReverseVideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import com.exmple.funnyvideo.R;

public class VideoSliceSeekBar5 extends AppCompatImageView {
    private boolean aBoolean;
    private boolean aBoolean1;
    private int anInt;
    private int anInt1;
    private int anInt10;
    private int anInt11;
    private int anInt12;
    private int anInt13;
    private int anInt14;
    private int anInt15;
    private int anInt2;
    private int anInt3;
    private int anInt4;
    private int anInt5;
    private int anInt6;
    private int anInt7;
    private int anInt8;
    private int anInt9;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private int color;
    private int color1;
    private Bitmap decodeResource;
    private int dimensionPixelOffset;
    private Paint paint;
    private Paint paint1;
    private SeekBarChangeListener seekBarChangeListener;
    private String time;


    public interface SeekBarChangeListener {
        void SeekBarValueChanged(int i, int i2);
    }

    public VideoSliceSeekBar5(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.video_slice_seekbar5_right);
        this.anInt = 100;
        this.paint = new Paint();
        this.paint1 = new Paint();
        this.color = getResources().getColor(R.color.black_transparent);
        this.anInt2 = 3;
        this.anInt3 = 15;
        this.color1 = getResources().getColor(R.color.yellow);
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.time_seekbar_line);
        this.dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.default_margin);
        this.decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.video_slice_seekbar5_left);
    }

    public VideoSliceSeekBar5(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.video_slice_seekbar5_right);
        this.anInt = 100;
        this.paint = new Paint();
        this.paint1 = new Paint();
        this.color = getResources().getColor(R.color.black_transparent);
        this.anInt2 = 3;
        this.anInt3 = 15;
        this.color1 = getResources().getColor(R.color.yellow);
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.time_seekbar_line);
        this.dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.default_margin);
        this.decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.video_slice_seekbar5_left);
    }

    public VideoSliceSeekBar5(Context context) {
        super(context);
        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.video_slice_seekbar5_right);
        this.anInt = 100;
        this.paint = new Paint();
        this.paint1 = new Paint();
        this.color = getResources().getColor(R.color.black_transparent);
        this.anInt2 = 3;
        this.anInt3 = 15;
        this.color1 = getResources().getColor(R.color.yellow);
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.time_seekbar_line);
        this.dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.default_margin);
        this.decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.video_slice_seekbar5_left);
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
        if (this.decodeResource.getHeight() > getHeight()) {
            getLayoutParams().height = this.decodeResource.getHeight();
        }
        this.anInt15 = (getHeight() / 2) - (this.decodeResource.getHeight() / 2);
        this.anInt9 = (getHeight() / 2) - (this.bitmap1.getHeight() / 2);
        this.anInt10 = this.decodeResource.getWidth() / 2;
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

    public void setSeekBarChangeListener(SeekBarChangeListener seekBarChangeListener) {
        this.seekBarChangeListener = seekBarChangeListener;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(this.color);
        int height = (getHeight() / 2) - (this.decodeResource.getHeight() / 2);
        int height2 = (getHeight() / 2) + (this.decodeResource.getHeight() / 2);
        canvas.drawRect(new Rect(this.dimensionPixelOffset, height, this.anInt12, height2), this.paint);
        canvas.drawRect(new Rect(this.anInt14, height, getWidth() - this.dimensionPixelOffset, height2), this.paint);
        if (this.aBoolean) {
            return;
        }
        canvas.drawBitmap(this.decodeResource, this.anInt12 - this.anInt10, this.anInt15, this.paint1);
        canvas.drawBitmap(this.bitmap, this.anInt14 - this.anInt10, this.anInt15, this.paint1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.aBoolean) {
            int x = (int) motionEvent.getX();
            int action = motionEvent.getAction();
            if (action == 0) {
                int i = this.anInt12;
                int i2 = this.anInt10;
                if ((x < i - i2 || x > i + i2) && x >= i - i2) {
                    int i3 = this.anInt14;
                    if ((x >= i3 - i2 && x <= i3 + i2) || x > i3 + i2) {
                        this.anInt6 = 2;
                    } else if ((x - i) + i2 >= (i3 - i2) - x && (x - i) + i2 > (i3 - i2) - x) {
                        this.anInt6 = 2;
                    } else {
                        this.anInt6 = 1;
                    }
                } else {
                    this.anInt6 = 1;
                }
            } else if (action == 1) {
                this.anInt6 = 0;
            } else if (action == 2) {
                int i4 = this.anInt12;
                int i5 = this.anInt10;
                if ((x <= i4 + i5 + 0 && this.anInt6 == 2) || (x >= (this.anInt14 - i5) + 0 && this.anInt6 == 1)) {
                    this.anInt6 = 0;
                }
                int i6 = this.anInt6;
                if (i6 != 1 && i6 == 2) {
                    this.anInt14 = x;
                } else {
                    this.anInt12 = x;
                }
            }
            b();
        }
        return true;
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
        if (this.seekBarChangeListener != null) {
            c();
            this.seekBarChangeListener.SeekBarValueChanged(this.anInt11, this.anInt13);
        }
    }

    private void c() {
        int i = this.anInt * (this.anInt12 - this.dimensionPixelOffset);
        int width = getWidth();
        int i2 = this.dimensionPixelOffset;
        this.anInt11 = i / (width - (i2 * 2));
        this.anInt13 = (this.anInt * (this.anInt14 - i2)) / (getWidth() - (this.dimensionPixelOffset * 2));
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

    public int getSelectedThumb() {
        return this.anInt6;
    }

    public int getLeftProgress() {
        return this.anInt11;
    }

    public int getRightProgress() {
        return this.anInt13;
    }

    public void setProgress(int i, int i2) {
        if (i2 - i > this.anInt3) {
            this.anInt12 = a(i);
            this.anInt14 = a(i2);
        }
        b();
    }

    public void videoPlayingProgress(int i) {
        this.aBoolean1 = true;
        this.anInt8 = a(i);
        invalidate();
    }

    public void videoPlayingTime(String str, int i) {
        this.aBoolean1 = true;
        this.time = str;
        invalidate();
    }

    public void removeVideoStatusThumb() {
        this.aBoolean1 = false;
        invalidate();
    }

    public void setSliceBlocked(boolean z) {
        this.aBoolean = z;
        invalidate();
    }

    public void setMaxValue(int i) {
        this.anInt = i;
    }

    public void setProgressMinDiff(int i) {
        this.anInt3 = i;
        this.anInt4 = a(i);
    }

    public void setProgressHeight(int i) {
        this.anInt2 /= 2;
        invalidate();
    }

    public void setProgressColor(int i) {
        this.color = i;
        invalidate();
    }

    public void setSecondaryProgressColor(int i) {
        this.color1 = i;
        invalidate();
    }

    public void setThumbSlice(Bitmap bitmap) {
        this.decodeResource = bitmap;
        a();
    }

    public void setThumbCurrentVideoPosition(Bitmap bitmap) {
        this.bitmap1 = bitmap;
        a();
    }

    public void setThumbPadding(int i) {
        this.dimensionPixelOffset = i;
        invalidate();
    }
}

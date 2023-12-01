package com.exmple.funnyvideo.Tools;


public class VideoMetadata {
    private long durationMs;
    private int height;
    private int width;

    public float aspectRatio() {
        int i = this.height;
        if (i == 0) {
            return 0.0f;
        }
        return (this.width * 1.0f) / i;
    }

    public long getDurationMs() {
        return this.durationMs;
    }

    public boolean isCorrupt() {
        return this.durationMs <= 0 || this.width <= 0 || this.height <= 0;
    }

    public boolean isCorruptDimensions() {
        return this.width <= 0 || this.height <= 0;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public void setDurationMs(long j) {
        this.durationMs = j;
    }
}

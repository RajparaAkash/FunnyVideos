package com.exmple.funnyvideo.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MRetriever {

    private Context context;
    private String currentPreparedSource;
    private final int size;
    private final ExecutorService threaspoolExecutor;
    private HashMap<Integer, Future> tasks = new HashMap<>();
    private final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

    public MRetriever(Context context, int i, ExecutorService executorService) {
        this.context = context;
        this.size = i;
        this.threaspoolExecutor = executorService;
    }

    public void frameAt(final String str, final long j, final FetchCallback<Bitmap> fetchCallback, int i) {
        Future future = this.tasks.get(Integer.valueOf(i));
        if (future != null) {
            future.cancel(false);
        }
        this.tasks.put(Integer.valueOf(i), this.threaspoolExecutor.submit(new Runnable() { // from class: com.video.editor.cutter.video.maker.offline.UtilsAndAdapters.MRetriever.1
            @Override
            public void run() {
                if (!str.equals(MRetriever.this.currentPreparedSource)) {
                    MRetriever.this.mediaMetadataRetriever.setDataSource(MRetriever.this.context, Uri.parse(str));
                }
                fetchCallback.onSuccess(MRetriever.this.getScaledFrameAt(j * 1000, 2));
                MRetriever.this.currentPreparedSource = str;
            }
        }));
    }

    public void setSource(String str) {
        this.mediaMetadataRetriever.setDataSource(this.context, Uri.parse(str));
    }

    public Bitmap getScaledFrameAt(long j, int i) {
        int i2;
        int height;
        Bitmap frameAtTime = this.mediaMetadataRetriever.getFrameAtTime(j, i);
        try {
            if (frameAtTime.getHeight() > frameAtTime.getWidth()) {
                height = this.size;
                i2 = (int) (frameAtTime.getWidth() * ((height * 1.0f) / frameAtTime.getHeight()));
            } else {
                i2 = this.size;
                height = (int) (frameAtTime.getHeight() * ((i2 * 1.0f) / frameAtTime.getWidth()));
            }
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(frameAtTime, i2, height, false);
            frameAtTime.recycle();
            return createScaledBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return frameAtTime;
        }
    }

    public void release() throws IOException {
        this.mediaMetadataRetriever.release();
    }
}

package com.exmple.funnyvideo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.exmple.funnyvideo.Tools.FileHelper;
import com.exmple.funnyvideo.Tools.ImageLoader;
import com.exmple.funnyvideo.Tools.MRetriever;
import com.exmple.funnyvideo.Tools.MediaHelper;
import com.exmple.funnyvideo.Tools.VideoMetadata;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MediaRetrieverAdapter extends RecyclerView.Adapter<MediaRetrieverAdapter.Holder> {
    File cacheDir;
    private Context context;
    int count;
    private int frameDuration;
    private final int frameSize;
    private final ImageLoader imageLoader;
    String mediaId;
    private MRetriever mediaMetadataRetriever;
    private final String mediaUri;
    private ExecutorService threaspoolExecutor;
    VideoMetadata videoMetadata;
    private Handler handler = new Handler();
    private HashMap<Integer, Future> tasks = new HashMap<>();

    public MediaRetrieverAdapter(Context context, String str, int i, int i2, ImageLoader imageLoader) {
        this.context = context;
        this.mediaUri = str;
        this.frameDuration = i;
        this.frameSize = i2;
        this.imageLoader = imageLoader;
        VideoMetadata videoMetadata = new VideoMetadata();
        this.videoMetadata = videoMetadata;
        if (i != 0) {
            MediaHelper.getVideoMets(context, str, videoMetadata);
            this.count = (int) (this.videoMetadata.getDurationMs() / i);
            this.mediaId = str.substring(str.lastIndexOf(47) + 1);
            File file = new File(context.getCacheDir(), ".thumbs");
            this.cacheDir = file;
            file.mkdir();
            this.threaspoolExecutor = Executors.newFixedThreadPool(1);
            try {
                MRetriever mRetriever = new MRetriever(this.context, i2, Executors.newFixedThreadPool(1));
                this.mediaMetadataRetriever = mRetriever;
                mRetriever.setSource(str);
            } catch (Exception unused) {
            }
        }
    }

    private int getIdentifier(int i) {
        return (int) ((this.videoMetadata.getDurationMs() * i) / this.count);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ImageView imageView = new ImageView(viewGroup.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int i2 = this.frameSize;
        imageView.setLayoutParams(new ViewGroup.LayoutParams(i2, i2));
        return new Holder(imageView);
    }

    @Override
    public void onBindViewHolder(Holder holder, @SuppressLint("RecyclerView") final int i) {
        Future future = this.tasks.get(Integer.valueOf(holder.hashCode()));
        if (future != null) {
            future.cancel(false);
        }
        final File cachedFile = FileHelper.getCachedFile(this.cacheDir, this.mediaId, getIdentifier(i));
        this.imageLoader.load(cachedFile, (ImageView) holder.itemView);
        if (cachedFile.exists()) {
            return;
        }
        this.tasks.put(Integer.valueOf(holder.hashCode()), this.threaspoolExecutor.submit(new Runnable() { // from class: com.video.editor.cutter.video.maker.offline.UtilsAndAdapters.MediaRetrieverAdapter.1
            @Override
            public void run() {
                Bitmap scaledFrameAt = MediaRetrieverAdapter.this.mediaMetadataRetriever.getScaledFrameAt(((long) (i * MediaRetrieverAdapter.this.frameDuration)) * 1000, 2);
                if (scaledFrameAt != null) {
                    FileHelper.saveBitmapToFile(cachedFile, scaledFrameAt);
                    scaledFrameAt.recycle();
                    MediaRetrieverAdapter.this.handler.post(new Runnable() { // from class: com.video.editor.cutter.video.maker.offline.UtilsAndAdapters.MediaRetrieverAdapter.1.1
                        @Override
                        public void run() {
                            MediaRetrieverAdapter.this.notifyItemChanged(i);
                        }
                    });
                }
            }
        }));
    }

    @Override
    public int getItemCount() {
        return this.count;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public Holder(View view) {
            super(view);
        }
    }
}

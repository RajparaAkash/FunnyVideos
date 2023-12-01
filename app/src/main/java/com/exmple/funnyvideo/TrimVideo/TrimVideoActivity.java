package com.exmple.funnyvideo.TrimVideo;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.exmple.funnyvideo.Tools.VideoSeekBarTimer;
import com.exmple.funnyvideo.Adapter.MediaRetrieverAdapter;
import com.exmple.funnyvideo.R;
import com.exmple.funnyvideo.MyUtils.UtilCommand;
import com.exmple.funnyvideo.Model.VideoPlayerState;
import com.exmple.funnyvideo.Activity.VideoPreviewActivity;
import com.exmple.funnyvideo.Tools.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressLint({"WrongConstant"})
public class TrimVideoActivity extends AppCompatActivity implements MediaScannerConnectionClient {


    ImageView imagePlayPause;
    VideoSliceSeekBar1 videoSliceSeekBar;
    VideoSeekBarTimer seekbarTimer;
    RecyclerView rvVideoFrame;
    TextView left_pointer;
    TextView right_pointer;
    TextView totalDuration;
    VideoView trimVideoView;

    public static Context AppContext = null;
    static final boolean myBoolean = true;
    MediaScannerConnection mediaScannerConnection;
    int myInt1 = 0;
    int myInt2 = 0;

    private String string = "";
    private String videoPath;
    public String finalVideoPath;

    public VideoPlayerState videoPlayerState = new VideoPlayerState();
    private myHandler handler = new myHandler();

    private class myHandler extends Handler {
        private boolean aBoolean;
        private Runnable aRunnable;

        private myHandler() {
            this.aBoolean = false;
            this.aRunnable = new Runnable() {
                public void run() {
                    myMethod();
                }
            };
        }

        public void myMethod() {
            if (!this.aBoolean) {
                this.aBoolean = TrimVideoActivity.myBoolean;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.aBoolean = false;
            videoSliceSeekBar.videoPlayingProgress(trimVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingProgress(trimVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingTime(UtilCommand.milliSecondsToTimer(trimVideoView.getCurrentPosition()));

            if (!trimVideoView.isPlaying() || trimVideoView.getCurrentPosition() >= videoSliceSeekBar.getRightProgress()) {
                if (trimVideoView.isPlaying()) {
                    trimVideoView.pause();
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
                videoSliceSeekBar.setSliceBlocked(false);
                videoSliceSeekBar.removeVideoStatusThumb();
                seekbarTimer.removeVideoStatusThumb();
                return;
            }
            postDelayed(aRunnable, 50);
        }
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_trim_video);

        rvVideoFrame = (RecyclerView) findViewById(R.id.rvVideoFrame);
        rvVideoFrame.setLayoutManager(new LinearLayoutManager(this, 0, false));

        findViewById(R.id.back_img).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.downloadVideo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trimVideoView.isPlaying()) {
                    trimVideoView.pause();
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
                createNewVideoFile();
            }
        });
        if (myBoolean) {
            AppContext = this;
            imagePlayPause = (ImageView) findViewById(R.id.imagePlayPause);
            videoSliceSeekBar = (VideoSliceSeekBar1) findViewById(R.id.videoSliceSeekBar);
            seekbarTimer = (VideoSeekBarTimer) findViewById(R.id.seekbarTimer);
            left_pointer = (TextView) findViewById(R.id.left_pointer);
            right_pointer = (TextView) findViewById(R.id.right_pointer);
            totalDuration = (TextView) findViewById(R.id.totalDuration);
            trimVideoView = (VideoView) findViewById(R.id.trimVideoView);

            trimVideoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (trimVideoView.isPlaying()) {
                        trimVideoView.pause();
                        videoSliceSeekBar.setSliceBlocked(false);
                        imagePlayPause.setVisibility(View.VISIBLE);
                        imagePlayPause.setImageResource(R.drawable.video_play_img);
                        videoSliceSeekBar.removeVideoStatusThumb();
                        seekbarTimer.removeVideoStatusThumb();
                        return;
                    }
                    trimVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
                    trimVideoView.start();
                    videoSliceSeekBar.videoPlayingProgress(videoSliceSeekBar.getLeftProgress());
                    imagePlayPause.setVisibility(View.INVISIBLE);
                    handler.myMethod();
                }
            });

            videoPath = getIntent().getStringExtra("videofilename");
            if (videoPath == null) {
                finish();
            }

            trimVideoView.setVideoPath(videoPath);
            trimVideoView.seekTo(100);
            prepared();
            trimVideoView.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
            });
            return;
        }
        throw new AssertionError();
    }


    public void nextGo() {
        File file = new File(finalVideoPath);
        MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                // now visible in gallery
            }
        });
        Intent intent = new Intent(getApplicationContext(), VideoPreviewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("song", finalVideoPath);
        startActivity(intent);
        finish();
    }

    private void createNewVideoFile() {
        String valueOf = String.valueOf(myInt2);
        String.valueOf(myInt1);
        String valueOf2 = String.valueOf(myInt1 - myInt2);
        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsoluteFile());
        sb.append("/");
        sb.append(getResources().getString(R.string.MainFolderName));
        sb.append("/");
        sb.append(getResources().getString(R.string.VideoCutter));
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsoluteFile());
        sb2.append("/");
        sb2.append(getResources().getString(R.string.MainFolderName));
        sb2.append("/");
        sb2.append(getResources().getString(R.string.VideoCutter));
        sb2.append("/videocutter");
        sb2.append(format);
        sb2.append(".mp4");

        finalVideoPath = "/storage/emulated/0/Movies" + sb2;
        String[] strArr = new String[]{"-ss", valueOf, "-y", "-i", videoPath, "-t", valueOf2, "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", finalVideoPath};

        videoDownload(strArr, finalVideoPath);
    }


    private void videoDownload(String[] strArr, final String str) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        String ffmpegCommand = UtilCommand.main(strArr);
        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {

            @Override
            public void apply(final long executionId, final int returnCode) {

                Config.printLastCommandOutput(Log.INFO);

                progressDialog.dismiss();
                if (returnCode == RETURN_CODE_SUCCESS) {
                    progressDialog.dismiss();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(finalVideoPath)));
                    TrimVideoActivity.this.sendBroadcast(intent);
                    nextGo();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    try {
                        new File(str).delete();
                        TrimVideoActivity.this.deleteFromGallery(str);
                        Toast.makeText(TrimVideoActivity.this, "Error Creating Video", 0).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    try {
                        new File(str).delete();
                        TrimVideoActivity.this.deleteFromGallery(str);
                        Toast.makeText(TrimVideoActivity.this, "Error Creating Video", 0).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        });

        getWindow().clearFlags(16);
    }

    ImageLoader picassoLoader = new ImageLoader() {
        @Override
        public void load(File file, ImageView imageView) {
            if (file == null || !file.exists()) {
                imageView.setImageDrawable(new ColorDrawable(-3355444));
            } else {
                Picasso.get().load(Uri.fromFile(file)).placeholder(new ColorDrawable(-3355444)).into(imageView);
            }
        }
    };

    private void prepared() {
        trimVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {

                if (trimVideoView.getDuration() / 1000 <= 13 && trimVideoView.getDuration() / 1000 >= 6) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(TrimVideoActivity.this,
                            videoPath, 1000, 160, picassoLoader));

                } else if (trimVideoView.getDuration() / 1000 <= 5 && trimVideoView.getDuration() / 1000 >= 3) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(TrimVideoActivity.this,
                            videoPath, 500, 160, picassoLoader));

                } else if (trimVideoView.getDuration() / 1000 <= 2 && trimVideoView.getDuration() / 1000 >= 1) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(TrimVideoActivity.this,
                            videoPath, 100, 160, picassoLoader));

                } else {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(TrimVideoActivity.this,
                            videoPath, 2000, 160, picassoLoader));
                }

                videoSliceSeekBar.setSeekBarChangeListener(new VideoSliceSeekBar1.SeekBarChangeListener() {
                    public void SeekBarValueChanged(int i, int i2) {
                        if (videoSliceSeekBar.getSelectedThumb() == 1) {
                            trimVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
                        }

                        left_pointer.setText(formatTimeUnit((long) i));
                        right_pointer.setText(formatTimeUnit((long) i2));
                        videoPlayerState.setStart(i);
                        videoPlayerState.setStop(i2);
                        myInt2 = i / 1000;
                        myInt1 = i2 / 1000;

                        StringBuilder sb = new StringBuilder();
                        sb.append("Total : ");
                        sb.append(String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((myInt1 - myInt2) / 3600), Integer.valueOf(((myInt1 - myInt2) % 3600) / 60), Integer.valueOf((myInt1 - myInt2) % 60)}));
                        totalDuration.setText(sb.toString());
                    }
                });
                videoSliceSeekBar.setMaxValue(mediaPlayer.getDuration());
                seekbarTimer.setMaxValue(mediaPlayer.getDuration());
                videoSliceSeekBar.setLeftProgress(0);
                seekbarTimer.setLeftProgress(0);
                videoSliceSeekBar.setRightProgress(mediaPlayer.getDuration());
                seekbarTimer.setRightProgress(mediaPlayer.getDuration());
                videoSliceSeekBar.setProgressMinDiff(0);
                seekbarTimer.setProgressMinDiff(0);
            }
        });
    }

    public String formatTimeUnit(long j2) throws ParseException {
        return String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j2)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)))});
    }

    public void onMediaScannerConnected() {
        this.mediaScannerConnection.scanFile(this.string, "video/*");
    }

    public void onScanCompleted(String str, Uri uri) {
        this.mediaScannerConnection.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                new File(str).delete();
                refreshGallery(str);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        query.close();
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

package com.exmple.funnyvideo.CompressVideo;

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
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.exmple.funnyvideo.Activity.VideoPreviewActivity;
import com.exmple.funnyvideo.Adapter.MediaRetrieverAdapter;
import com.exmple.funnyvideo.Model.VideoPlayerState;
import com.exmple.funnyvideo.MyUtils.UtilCommand;
import com.exmple.funnyvideo.R;
import com.exmple.funnyvideo.Tools.StaticMethods;
import com.exmple.funnyvideo.Tools.ImageLoader;
import com.exmple.funnyvideo.Tools.VideoSeekBarTimer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CompressVideoActivity extends AppCompatActivity {

    VideoSliceSeekBar6 videoSliceSeekBar;
    VideoSeekBarTimer seekbarTimer;
    RecyclerView rvVideoFrame;

    public VideoView compressVideoView;
    public ImageView imagePlayPause;
    public SeekBar seekBar;
    public TextView videoSizeTxt;
    public TextView videoFormatTxt;
    public TextView seekBarPerTxt;
    TextView left_pointer;
    TextView totalDuration;
    TextView right_pointer;

    int myInt1 = 0;
    int myInt2 = 0;
    public VideoPlayerState videoPlayerState = new VideoPlayerState();

    private String valueOfPer = "9000k";
    static final boolean myBol1 = true;
    public int LIST_COLUMN_SIZE = 4;
    public int MP_DURATION;
    boolean myBol2 = myBol1;

    public boolean isInFront = myBol1;

    public String oldVideoPath;
    public String newVideoPath;

    public int maxtime;
    public int mintime;

    public ProgressDialog prgDialog;
    WakeLock wakeLock;
    public int totalVideoDuration = 0;
    private StateObserver stateObserver = new StateObserver();
    private String ssad;

    @SuppressLint({"HandlerLeak"})
    public class StateObserver extends Handler {
        private boolean aBoolean = false;
        private Runnable aRunnable = new Runnable() {
            public void run() {
                myMethod();
            }
        };

        public void myMethod() {
            if (!this.aBoolean) {
                this.aBoolean = CompressVideoActivity.myBol1;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.aBoolean = false;
            videoSliceSeekBar.videoPlayingProgress(compressVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingProgress(compressVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingTime(UtilCommand.milliSecondsToTimer(compressVideoView.getCurrentPosition()));

            if (!compressVideoView.isPlaying() || compressVideoView.getCurrentPosition() >= videoSliceSeekBar.getRightProgress()) {
                if (compressVideoView.isPlaying()) {
                    compressVideoView.pause();
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setBackgroundResource(R.drawable.video_play_img);
                }
                if (!compressVideoView.isPlaying()) {
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setBackgroundResource(R.drawable.video_play_img);
                    return;
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
        setContentView(R.layout.activity_compress_video);

        findViewById(R.id.back_img).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.downloadVideo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (compressVideoView.isPlaying()) {
                        imagePlayPause.setVisibility(View.VISIBLE);
                        imagePlayPause.setBackgroundResource(R.drawable.video_play_img);
                        compressVideoView.pause();
                    }
                } catch (Exception unused) {
                }
                executeCompressCommand();
            }
        });

        if (myBol1) {
            this.myBol2 = myBol1;
            copyCreate();
            return;
        }
        throw new AssertionError();
    }

    public void nextGo() {
        Intent intent = new Intent(getApplicationContext(), VideoPreviewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("song", newVideoPath);
        startActivity(intent);
        finish();
    }

    @SuppressLint("InvalidWakeLockTag")
    public void copyCreate() {
        this.isInFront = myBol1;
        this.LIST_COLUMN_SIZE = disPlaySize() / 100;
        this.totalVideoDuration = 0;
        this.wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(6, "VideoMerge");
        if (!this.wakeLock.isHeld()) {
            this.wakeLock.acquire();
        }

        oldVideoPath = getIntent().getStringExtra("videofilename");

        rvVideoFrame = (RecyclerView) findViewById(R.id.rvVideoFrame);
        rvVideoFrame.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        videoSliceSeekBar = (VideoSliceSeekBar6) findViewById(R.id.videoSliceSeekBar);
        seekbarTimer = (VideoSeekBarTimer) findViewById(R.id.seekbarTimer);
        compressVideoView = (VideoView) findViewById(R.id.compressVideoView);
        imagePlayPause = (ImageView) findViewById(R.id.imagePlayPause);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        videoSizeTxt = (TextView) findViewById(R.id.videoSizeTxt);
        videoFormatTxt = (TextView) findViewById(R.id.videoFormatTxt);
        seekBarPerTxt = (TextView) findViewById(R.id.seekBarPerTxt);

        left_pointer = (TextView) findViewById(R.id.left_pointer);
        totalDuration = (TextView) findViewById(R.id.totalDuration);
        right_pointer = (TextView) findViewById(R.id.right_pointer);

        size_Format(oldVideoPath);

        runOnUiThread(new Runnable() {
            public void run() {
                CompressVideoActivity.this.prgDialog = ProgressDialog.show(CompressVideoActivity.this, "", "Please Wait...", CompressVideoActivity.myBol1);
            }
        });

        VideoSeekBar();

        compressVideoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (compressVideoView.isPlaying()) {
                    compressVideoView.pause();
                    videoSliceSeekBar.setSliceBlocked(false);
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setBackgroundResource(R.drawable.video_play_img);
                    videoSliceSeekBar.removeVideoStatusThumb();
                    seekbarTimer.removeVideoStatusThumb();
                    return;
                }
                compressVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
                compressVideoView.start();
                videoSliceSeekBar.videoPlayingProgress(videoSliceSeekBar.getLeftProgress());
                imagePlayPause.setVisibility(View.INVISIBLE);
                stateObserver.myMethod();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekBarPerTxt.setText(progress + " %");

                if (progress >= 0 && progress <= 10) {
                    valueOfPer = "1000k";
                } else if (progress >= 11 && progress <= 20) {
                    valueOfPer = "2000k";
                } else if (progress >= 21 && progress <= 30) {
                    valueOfPer = "3000k";
                } else if (progress >= 31 && progress <= 40) {
                    valueOfPer = "4000k";
                } else if (progress >= 41 && progress <= 50) {
                    valueOfPer = "5000k";
                } else if (progress >= 51 && progress <= 60) {
                    valueOfPer = "6000k";
                } else if (progress >= 61 && progress <= 70) {
                    valueOfPer = "7000k";
                } else if (progress >= 71 && progress <= 80) {
                    valueOfPer = "8000k";
                } else if (progress >= 81 && progress <= 90) {
                    valueOfPer = "9000k";
                } else if (progress >= 91 && progress <= 100) {
                    valueOfPer = "10000k";
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void executeCompressCommand() {
        String[] strArr;
        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsoluteFile());
        sb.append("/");
        sb.append(getResources().getString(R.string.MainFolderName));
        sb.append("/");
        sb.append(getResources().getString(R.string.VideoCompressor));
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsoluteFile());
        sb2.append("/");
        sb2.append(getResources().getString(R.string.MainFolderName));
        sb2.append("/");
        sb2.append(getResources().getString(R.string.VideoCompressor));
        sb2.append("/videocompressor");
        sb2.append(format);
        sb2.append(".mp4");
        this.newVideoPath = sb2.toString();
        StringBuilder hdcd = new StringBuilder();
        hdcd.append("" + width);
        hdcd.append("x");
        hdcd.append("" + height);
        ssad = hdcd.toString();

        String valueOf = String.valueOf(myInt2);
        String valueOf2 = String.valueOf(myInt1 - myInt2);

        strArr = new String[]{"-ss", valueOf, "-y", "-i", oldVideoPath,
                "-t", valueOf2, "-s", ssad, "-r", "25", "-vcodec", "mpeg4", "-b:v",
                valueOfPer, "-b:a", "48000", "-ac", "2", "-ar", "22050", newVideoPath};

        a(strArr, newVideoPath);
    }

    private void a(String[] strArr, final String str) {
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
                    intent.setData(Uri.fromFile(new File(newVideoPath)));
                    CompressVideoActivity.this.sendBroadcast(intent);
                    nextGo();

                } else if (returnCode == RETURN_CODE_CANCEL) {

                    try {
                        new File(str).delete();
                        CompressVideoActivity.this.deleteFromGallery(str);
                        Toast.makeText(CompressVideoActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {

                    try {
                        new File(str).delete();
                        CompressVideoActivity.this.deleteFromGallery(str);
                        Toast.makeText(CompressVideoActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        });
    }

    private void size_Format(String str) {
        TextView textView = this.videoSizeTxt;
        StringBuilder sb = new StringBuilder();
        sb.append("Total Size ");
        sb.append("(" + StaticMethods.sizeInMBbyFilepath(str) + ")");
        textView.setText(sb.toString());
        TextView textView2 = this.videoFormatTxt;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Video Format ");
        sb2.append("(" + StaticMethods.FormatofVideo(str) + ")");
        textView2.setText(sb2.toString());
    }

    private int width;
    private int height;

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

    public void VideoSeekBar() {
        compressVideoView.setVideoURI(Uri.parse(oldVideoPath));
        compressVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {

                if (compressVideoView.getDuration() / 1000 <= 13 && compressVideoView.getDuration() / 1000 >= 6) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(CompressVideoActivity.this,
                            oldVideoPath, 1000, 160, picassoLoader));

                } else if (compressVideoView.getDuration() / 1000 <= 5 && compressVideoView.getDuration() / 1000 >= 3) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(CompressVideoActivity.this,
                            oldVideoPath, 500, 160, picassoLoader));

                } else if (compressVideoView.getDuration() / 1000 <= 2 && compressVideoView.getDuration() / 1000 >= 1) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(CompressVideoActivity.this,
                            oldVideoPath, 100, 160, picassoLoader));

                } else {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(CompressVideoActivity.this,
                            oldVideoPath, 2000, 160, picassoLoader));
                }

                videoSliceSeekBar.setSeekBarChangeListener(new VideoSliceSeekBar6.SeekBarChangeListener() {
                    public void SeekBarValueChanged(int i, int i2) {
                        if (videoSliceSeekBar.getSelectedThumb() == 1) {
                            compressVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
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

                if (myBol2) {
                    CompressVideoActivity.this.mintime = 0;
                    CompressVideoActivity.this.maxtime = mediaPlayer.getDuration();
                    CompressVideoActivity.this.MP_DURATION = mediaPlayer.getDuration();
                    CompressVideoActivity.this.seekLayout(0, CompressVideoActivity.this.MP_DURATION);
                    compressVideoView.start();
                    compressVideoView.pause();
                    compressVideoView.seekTo(100);
                    return;
                }
                CompressVideoActivity.this.seekLayout(CompressVideoActivity.this.mintime, CompressVideoActivity.this.maxtime);
                compressVideoView.start();
                compressVideoView.pause();
                compressVideoView.seekTo(CompressVideoActivity.this.mintime);
            }
        });
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(oldVideoPath);
        width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

        compressVideoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                CompressVideoActivity.this.prgDialog.dismiss();
                return false;
            }
        });
    }

    public String formatTimeUnit(long j2) throws ParseException {
        return String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j2)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)))});
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        this.totalVideoDuration = 0;
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        super.onDestroy();
    }

    public void seekLayout(int i2, int i3) {
        this.prgDialog.dismiss();
    }

    private int disPlaySize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i2 = displayMetrics.heightPixels;
        int i3 = displayMetrics.widthPixels;
        return i3 <= i2 ? i3 : i2;
    }

    public void onPause() {
        super.onPause();
        this.myBol2 = false;
        try {
            if (compressVideoView.isPlaying()) {
                imagePlayPause.setVisibility(View.VISIBLE);
                imagePlayPause.setBackgroundResource(R.drawable.video_play_img);
                compressVideoView.pause();
            }
        } catch (Exception unused) {
        }
        this.isInFront = false;
    }


    public void onRestart() {
        super.onRestart();
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
    public void onResume() {
        super.onResume();
        this.isInFront = myBol1;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

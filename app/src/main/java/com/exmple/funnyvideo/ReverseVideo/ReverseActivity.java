package com.exmple.funnyvideo.ReverseVideo;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.exmple.funnyvideo.Tools.ImageLoader;
import com.exmple.funnyvideo.Tools.VideoSeekBarTimer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReverseActivity extends AppCompatActivity {

    ImageView imagePlayPause;
    VideoSliceSeekBar5 videoSliceSeekBar;
    VideoSeekBarTimer seekbarTimer;
    RecyclerView rvVideoFrame;
    TextView left_pointer;
    TextView right_pointer;
    TextView totalDuration;
    VideoView reverseVideoView;
    RadioButton with_reverse, without_reverse;

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
                this.aBoolean = ReverseActivity.myBoolean;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.aBoolean = false;
            videoSliceSeekBar.videoPlayingProgress(reverseVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingProgress(reverseVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingTime(UtilCommand.milliSecondsToTimer(reverseVideoView.getCurrentPosition()));

            if (!reverseVideoView.isPlaying() || reverseVideoView.getCurrentPosition() >= videoSliceSeekBar.getRightProgress()) {
                if (reverseVideoView.isPlaying()) {
                    reverseVideoView.pause();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse);

        rvVideoFrame = (RecyclerView) findViewById(R.id.rvVideoFrame);
        rvVideoFrame.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.downloadVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reverseVideoView.isPlaying()) {
                    reverseVideoView.pause();
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
                createNewVideoFile();
            }
        });
        if (myBoolean) {
            AppContext = this;
            imagePlayPause = (ImageView) findViewById(R.id.imagePlayPause);
            videoSliceSeekBar = (VideoSliceSeekBar5) findViewById(R.id.videoSliceSeekBar);
            seekbarTimer = (VideoSeekBarTimer) findViewById(R.id.seekbarTimer);
            left_pointer = (TextView) findViewById(R.id.left_pointer);
            right_pointer = (TextView) findViewById(R.id.right_pointer);
            totalDuration = (TextView) findViewById(R.id.totalDuration);
            reverseVideoView = (VideoView) findViewById(R.id.reverseVideoView);
            with_reverse = findViewById(R.id.with_reverse);
            without_reverse = findViewById(R.id.without_reverse);

            reverseVideoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reverseVideoView.isPlaying()) {
                        reverseVideoView.pause();
                        videoSliceSeekBar.setSliceBlocked(false);
                        imagePlayPause.setVisibility(View.VISIBLE);
                        imagePlayPause.setImageResource(R.drawable.video_play_img);
                        videoSliceSeekBar.removeVideoStatusThumb();
                        seekbarTimer.removeVideoStatusThumb();
                        return;
                    }
                    reverseVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
                    reverseVideoView.start();
                    videoSliceSeekBar.videoPlayingProgress(videoSliceSeekBar.getLeftProgress());
                    imagePlayPause.setVisibility(View.INVISIBLE);
                    handler.myMethod();
                }
            });

            videoPath = getIntent().getStringExtra("videofilename");
            if (videoPath == null) {
                finish();
            }

            reverseVideoView.setVideoPath(videoPath);
            reverseVideoView.seekTo(100);
            prepared();
            reverseVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
        sb.append(getResources().getString(R.string.VideoReverse));
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsoluteFile());
        sb2.append("/");
        sb2.append(getResources().getString(R.string.MainFolderName));
        sb2.append("/");
        sb2.append(getResources().getString(R.string.VideoReverse));
        sb2.append("/reverse");
        sb2.append(format);
        sb2.append(".mp4");

        finalVideoPath = "/storage/emulated/0/Movies" + sb2;

        String[] strArr = new String[0];
        if (without_reverse.isChecked()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(valueOf);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(valueOf2);
            strArr = new String[]{"-ss", sb3.toString(), "-y", "-i", videoPath, "-t", sb4.toString(), "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", "-vf", "reverse", finalVideoPath};

        } else if (with_reverse.isChecked()) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("");
            sb5.append(valueOf);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("");
            sb6.append(valueOf2);
            strArr = new String[]{"-ss", sb5.toString(), "-y", "-i", videoPath, "-t", sb6.toString(), "-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", "-vf", "reverse", "-af", "areverse", finalVideoPath};

        }
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
                    ReverseActivity.this.sendBroadcast(intent);
                    nextGo();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    try {
                        new File(str).delete();
                        ReverseActivity.this.deleteFromGallery(str);
                        Toast.makeText(ReverseActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    try {
                        new File(str).delete();
                        ReverseActivity.this.deleteFromGallery(str);
                        Toast.makeText(ReverseActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
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
        reverseVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {

                if (reverseVideoView.getDuration() / 1000 <= 13 && reverseVideoView.getDuration() / 1000 >= 6) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(ReverseActivity.this,
                            videoPath, 1000, 160, picassoLoader));

                } else if (reverseVideoView.getDuration() / 1000 <= 5 && reverseVideoView.getDuration() / 1000 >= 3) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(ReverseActivity.this,
                            videoPath, 500, 160, picassoLoader));

                } else if (reverseVideoView.getDuration() / 1000 <= 2 && reverseVideoView.getDuration() / 1000 >= 1) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(ReverseActivity.this,
                            videoPath, 100, 160, picassoLoader));

                } else {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(ReverseActivity.this,
                            videoPath, 2000, 160, picassoLoader));
                }

                videoSliceSeekBar.setSeekBarChangeListener(new VideoSliceSeekBar5.SeekBarChangeListener() {
                    public void SeekBarValueChanged(int i, int i2) {
                        if (videoSliceSeekBar.getSelectedThumb() == 1) {
                            reverseVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
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
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
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

//    private String reverseVideoAudio(String inputVideoPath) {
//        reversedVideoPath = getCacheDir() + "/reversed_video.mp4";
//
//
//        String[] cmd = new String[]{
//                "-y",
//                "-i", inputVideoPath,
//                "-vf", "reverse",
//                "-af", "areverse",
//                reversedVideoPath
//        };
//
//        FFmpeg.execute(cmd);
//
//        return reversedVideoPath;
//    }


//    private String reverseVideo_WithoutRevAudio(String inputVideoPath) {
//        reversedVideoPath = getCacheDir() + "/reversed_video.mp4";
//
//        String[] cmd = new String[]{
//                "-y",
//                "-i", inputVideoPath,
//                "-vf", "reverse",
//                reversedVideoPath
//        };
//
//        FFmpeg.execute(cmd);
//
//        return reversedVideoPath;
//    }

}
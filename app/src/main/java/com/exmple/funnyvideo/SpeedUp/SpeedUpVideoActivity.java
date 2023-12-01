package com.exmple.funnyvideo.SpeedUp;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
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
import com.exmple.funnyvideo.Adapter.MediaRetrieverAdapter;
import com.exmple.funnyvideo.R;
import com.exmple.funnyvideo.MyUtils.UtilCommand;
import com.exmple.funnyvideo.Activity.VideoPreviewActivity;
import com.exmple.funnyvideo.Model.VideoPlayerState;
import com.exmple.funnyvideo.Tools.ImageLoader;
import com.exmple.funnyvideo.Tools.VideoSeekBarTimer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class SpeedUpVideoActivity extends AppCompatActivity {

    private CheckBox audioEnbleCheckBox;
    public TextView left_pointer;
    public TextView right_pointer;
    VideoSliceSeekBar2 videoSliceSeekBar;
    VideoSeekBarTimer seekbarTimer;
    RecyclerView rvVideoFrame;
    public VideoView speedUpVideoView;
    ImageView imagePlayPause;

    public TextView speed_1x_Txt;
    public TextView speed_2x_Txt;
    public TextView speed_3x_Txt;
    public TextView speed_4x_Txt;
    public TextView speed_5x_Txt;
    public TextView speed_6x_Txt;

    String videoPath = null;
    private WakeLock wakeLock;
    String getTimeForTreck;
    String getTimeTreck = "00";

    static final boolean BOOLEAN = true;
    Boolean myBoolean = Boolean.valueOf(false);
    int myInt = 1;
    private PowerManager powerManager;

    public VideoPlayerState videoPlayerState = new VideoPlayerState();
    private myHandler handler = new myHandler();

    private class myHandler extends Handler {
        private boolean handlerBoolean;
        private Runnable handlerRunnable;

        private myHandler() {
            this.handlerBoolean = false;
            this.handlerRunnable = new Runnable() {
                public void run() {
                    myMethod();
                }
            };
        }

        public void myMethod() {
            if (!this.handlerBoolean) {
                this.handlerBoolean = SpeedUpVideoActivity.BOOLEAN;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.handlerBoolean = false;
            videoSliceSeekBar.videoPlayingProgress(speedUpVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingProgress(speedUpVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingTime(UtilCommand.milliSecondsToTimer(speedUpVideoView.getCurrentPosition()));
            if (!speedUpVideoView.isPlaying() || speedUpVideoView.getCurrentPosition() >= videoSliceSeekBar.getRightProgress()) {
                if (speedUpVideoView.isPlaying()) {
                    speedUpVideoView.pause();
                    myBoolean = Boolean.valueOf(false);
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
                videoSliceSeekBar.setSliceBlocked(false);
                videoSliceSeekBar.removeVideoStatusThumb();
                seekbarTimer.removeVideoStatusThumb();
                return;
            }
            postDelayed(this.handlerRunnable, 50);
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_speed_up_video);

        rvVideoFrame = (RecyclerView) findViewById(R.id.rvVideoFrame);
        rvVideoFrame.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        findViewById(R.id.back_img).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.downloadVideo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speedUpVideoView.isPlaying()) {
                    speedUpVideoView.pause();
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
                if (videoPlayerState.isValid()) {
                    fastmotioncommand();
                }
            }
        });

        if (BOOLEAN) {
            idBinding();
            powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(6, "My Tag");
            Object lastNonConfigurationInstance = getLastNonConfigurationInstance();
            if (lastNonConfigurationInstance != null) {
                videoPlayerState = (VideoPlayerState) lastNonConfigurationInstance;
            } else {
                Bundle extras = getIntent().getExtras();
                videoPlayerState.setFilename(extras.getString("videofilename"));
                videoPath = extras.getString("videofilename");
            }

            speedUpVideoView.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    myBoolean = Boolean.valueOf(false);
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
            });

            /*speedUpVideoView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (myBoolean.booleanValue()) {
                        speedUpVideoView.pause();
                        myBoolean = Boolean.valueOf(false);
                        imagePlayPause.setVisibility(View.VISIBLE);
                        imagePlayPause.setImageResource(R.drawable.video_play_img);
                    }
                    return SpeedUpVideoActivity.BOOLEAN;
                }
            });*/
            prepared();

            return;
        }


        throw new AssertionError();
    }

    public void nextGo() {
        Intent intent = new Intent(getApplicationContext(), VideoPreviewActivity.class);
        intent.putExtra("song", videoPath);
        startActivity(intent);
    }

    public void fastmotioncommand() {
        String[] strArr;
        String str = "";
        float f2 = this.myInt == 2 ? 0.5f : this.myInt == 3 ? 0.33333334f : this.myInt == 4 ? 0.25f : this.myInt == 5 ? 0.2f : this.myInt == 6 ? 0.16666667f : this.myInt == 7 ? 0.14285715f : this.myInt == 8 ? 0.125f : 2.0f;
        String valueOf = String.valueOf(videoPlayerState.getStart() / 1000);
        String valueOf2 = String.valueOf(videoPlayerState.getDuration() / 1000);
        String filename = videoPlayerState.getFilename();

        videoPath = "/storage/emulated/0/Movies" + FileUtils.getTargetFileName(this, filename);

        if (audioEnbleCheckBox.isChecked()) {
            if (this.myInt == 2) {
                str = "atempo=2.0";
            } else if (this.myInt == 3) {
                str = "atempo=2.0";
            } else if (this.myInt == 4) {
                str = "atempo=2.0,atempo=2.0";
            } else if (this.myInt == 5) {
                str = "atempo=2.0,atempo=2.0";
            } else if (this.myInt == 6) {
                str = "atempo=2.0,atempo=2.0,atempo=2.0";
            } else if (this.myInt == 7) {
                str = "atempo=2.0,atempo=2.0,atempo=2.0";
            } else if (this.myInt == 8) {
                str = "atempo=2.0,atempo=2.0,atempo=2.0,atempo=2.0";
            }
        }
        try {
            if (audioEnbleCheckBox.isChecked()) {
                StringBuilder sb = new StringBuilder();
                sb.append("setpts=");
                sb.append(f2);
                sb.append("*PTS");
                strArr = new String[]{"-y", "-ss", valueOf, "-i", filename, "-filter:v", sb.toString(), "-filter:a", str, "-r", "15", "-b:v", "2500k", "-strict", "experimental", "-t", valueOf2, videoPath};
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setpts=");
                sb2.append(f2);
                sb2.append("*PTS");
                strArr = new String[]{"-y", "-ss", valueOf, "-i", filename, "-filter:v", sb2.toString(), "-an", "-r", "15", "-ab", "128k", "-vcodec", "mpeg4", "-b:v", "2500k", "-sample_fmt", "s16", "-strict", "experimental", "-t", valueOf2, videoPath};
            }
            downloadVideo(strArr, videoPath);
        } catch (Exception unused) {
            File file = new File(videoPath);
            if (file.exists()) {
                file.delete();
                finish();
                return;
            }
            Toast.makeText(this, "please select Quality", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadVideo(String[] strArr, final String str) {
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
                    intent.setData(Uri.fromFile(new File(videoPath)));
                    SpeedUpVideoActivity.this.sendBroadcast(intent);

                    nextGo();
                    progressDialog.dismiss();
                    SpeedUpVideoActivity.this.refreshGallery(str);

                } else if (returnCode == RETURN_CODE_CANCEL) {
                    try {
                        new File(str).delete();
                        deleteFromGallery(str);
                        Toast.makeText(SpeedUpVideoActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    try {
                        new File(str).delete();
                        deleteFromGallery(str);
                        Toast.makeText(SpeedUpVideoActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        });

        getWindow().clearFlags(16);
    }

    private void idBinding() {

        speed_1x_Txt = (TextView) findViewById(R.id.speed_1x_Txt);
        speed_2x_Txt = (TextView) findViewById(R.id.speed_2x_Txt);
        speed_3x_Txt = (TextView) findViewById(R.id.speed_3x_Txt);
        speed_4x_Txt = (TextView) findViewById(R.id.speed_4x_Txt);
        speed_5x_Txt = (TextView) findViewById(R.id.speed_5x_Txt);
        speed_6x_Txt = (TextView) findViewById(R.id.speed_6x_Txt);

        left_pointer = (TextView) findViewById(R.id.left_pointer);
        right_pointer = (TextView) findViewById(R.id.right_pointer);
        imagePlayPause = (ImageView) findViewById(R.id.imagePlayPause);
        speedUpVideoView = (VideoView) findViewById(R.id.speedUpVideoView);
        speedUpVideoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBoolean.booleanValue()) {
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                    myBoolean = Boolean.valueOf(false);
                } else {
                    imagePlayPause.setVisibility(View.INVISIBLE);
                    myBoolean = Boolean.valueOf(SpeedUpVideoActivity.BOOLEAN);
                }
                videoCheck();
            }
        });

        audioEnbleCheckBox = (CheckBox) findViewById(R.id.audioEnbleCheckBox);
        videoSliceSeekBar = (VideoSliceSeekBar2) findViewById(R.id.videoSliceSeekBar);
        seekbarTimer = (VideoSeekBarTimer) findViewById(R.id.seekbarTimer);

        audioEnbleCheckBox.setChecked(false);

        speed_1x_Txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myInt = 1;
                unSelectSpeed();
                speed_1x_Txt.setBackgroundResource(R.drawable.speed_up_selected);
            }
        });

        speed_2x_Txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myInt = 2;
                unSelectSpeed();
                speed_2x_Txt.setBackgroundResource(R.drawable.speed_up_selected);
            }
        });

        speed_3x_Txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myInt = 3;
                unSelectSpeed();
                speed_3x_Txt.setBackgroundResource(R.drawable.speed_up_selected);
            }
        });

        speed_4x_Txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myInt = 4;
                unSelectSpeed();
                speed_4x_Txt.setBackgroundResource(R.drawable.speed_up_selected);
            }
        });

        speed_5x_Txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myInt = 5;
                unSelectSpeed();
                speed_5x_Txt.setBackgroundResource(R.drawable.speed_up_selected);
            }
        });

        speed_6x_Txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myInt = 6;
                unSelectSpeed();
                speed_6x_Txt.setBackgroundResource(R.drawable.speed_up_selected);
            }
        });
    }

    private void unSelectSpeed() {
        speed_1x_Txt.setBackgroundResource(R.drawable.speed_up_unselected);
        speed_2x_Txt.setBackgroundResource(R.drawable.speed_up_unselected);
        speed_3x_Txt.setBackgroundResource(R.drawable.speed_up_unselected);
        speed_4x_Txt.setBackgroundResource(R.drawable.speed_up_unselected);
        speed_5x_Txt.setBackgroundResource(R.drawable.speed_up_unselected);
        speed_6x_Txt.setBackgroundResource(R.drawable.speed_up_unselected);
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
        speedUpVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {

                if (speedUpVideoView.getDuration() / 1000 <= 13 && speedUpVideoView.getDuration() / 1000 >= 6) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(SpeedUpVideoActivity.this,
                            videoPath, 1000, 160, picassoLoader));

                } else if (speedUpVideoView.getDuration() / 1000 <= 5 && speedUpVideoView.getDuration() / 1000 >= 3) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(SpeedUpVideoActivity.this,
                            videoPath, 500, 160, picassoLoader));

                } else if (speedUpVideoView.getDuration() / 1000 <= 2 && speedUpVideoView.getDuration() / 1000 >= 1) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(SpeedUpVideoActivity.this,
                            videoPath, 100, 160, picassoLoader));

                } else {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(SpeedUpVideoActivity.this,
                            videoPath, 2000, 160, picassoLoader));
                }

                videoSliceSeekBar.setSeekBarChangeListener(new VideoSliceSeekBar2.SeekBarChangeListener() {
                    public void SeekBarValueChanged(int i, int i2) {
                        if (videoSliceSeekBar.getSelectedThumb() == 1) {
                            speedUpVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
                        }
                        left_pointer.setText(SpeedUpVideoActivity.getTimeForTrackFormat(i));
                        right_pointer.setText(SpeedUpVideoActivity.getTimeForTrackFormat(i2));
                        getTimeTreck = SpeedUpVideoActivity.getTimeForTrackFormat(i);
                        videoPlayerState.setStart(i);
                        getTimeForTreck = SpeedUpVideoActivity.getTimeForTrackFormat(i2);
                        videoPlayerState.setStop(i2);
                    }
                });
                getTimeForTreck = SpeedUpVideoActivity.getTimeForTrackFormat(mediaPlayer.getDuration());
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
        speedUpVideoView.setVideoPath(videoPlayerState.getFilename());
        getTimeForTreck = getTimeForTrackFormat(speedUpVideoView.getDuration());
    }

    public void videoCheck() {
        if (speedUpVideoView.isPlaying()) {
            speedUpVideoView.pause();
            videoSliceSeekBar.setSliceBlocked(false);
            videoSliceSeekBar.removeVideoStatusThumb();
            seekbarTimer.removeVideoStatusThumb();
            return;
        }
        speedUpVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
        speedUpVideoView.start();
        videoSliceSeekBar.videoPlayingProgress(videoSliceSeekBar.getLeftProgress());
        handler.myMethod();
    }

    public static String getTimeForTrackFormat(int i2) {
        long j2 = (long) i2;
        return String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j2)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)))});
    }


    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        if (!speedUpVideoView.isPlaying()) {
            myBoolean = Boolean.valueOf(false);
            imagePlayPause.setVisibility(View.VISIBLE);
            imagePlayPause.setImageResource(R.drawable.video_play_img);
        }
        /*speedUpVideoView.seekTo(videoPlayerState.getCurrentTime());*/
        speedUpVideoView.seekTo(100);
    }

    @Override
    public void onPause() {
        wakeLock.release();
        super.onPause();
        videoPlayerState.setCurrentTime(speedUpVideoView.getCurrentPosition());
    }

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
        finish();
    }
}

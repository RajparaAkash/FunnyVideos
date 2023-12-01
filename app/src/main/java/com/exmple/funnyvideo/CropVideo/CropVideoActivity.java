package com.exmple.funnyvideo.CropVideo;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.edmodo.cropper.CropImageView;
import com.edmodo.cropper.cropwindow.edge.Edge;
import com.exmple.funnyvideo.Activity.VideoPreviewActivity;
import com.exmple.funnyvideo.Adapter.MediaRetrieverAdapter;
import com.exmple.funnyvideo.Model.VideoPlayerState;
import com.exmple.funnyvideo.MyUtils.UtilCommand;
import com.exmple.funnyvideo.R;
import com.exmple.funnyvideo.Tools.ImageLoader;
import com.exmple.funnyvideo.Tools.VideoSeekBarTimer;
import com.squareup.picasso.Picasso;

import java.io.File;

@SuppressLint({"WrongConstant"})
public class CropVideoActivity extends AppCompatActivity {

    ImageView imagePlayPause;
    VideoSliceSeekBar4 videoSliceSeekBar;
    VideoSeekBarTimer seekbarTimer;
    RecyclerView rvVideoFrame;
    VideoView cropVideoView;
    CropImageView cropperView;

    ImageView videoCropImg_1;
    ImageView videoCropImg_2;
    ImageView videoCropImg_3;
    ImageView videoCropImg_4;
    ImageView videoCropImg_5;
    ImageView videoCropImg_6;
    ImageView videoCropImg_7;
    ImageView videoCropImg_8;

    TextView left_pointer;
    TextView right_pointer;

    static final boolean af = true;
    int A;
    int B;
    String videoPath;
    String videoPathFinal;
    String videoName;
    String videoDuration;
    String videoStartTime;
    String string1 = "00";
    String string2;
    String string3;
    Bitmap bitmap;
    VideoPlayerState videoPlayerState = new VideoPlayerState();
    myHandler handler = new myHandler();

    float aa;
    float ab;
    float ac;
    float ad;
    long ae;
    private int ag;
    private int ah;
    int m;
    int n;
    int o;
    int p;
    int q;
    int r;
    int s;
    int t;
    int u;
    int v;
    int w;
    int x;
    int y;
    int z;

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
                this.aBoolean = CropVideoActivity.af;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.aBoolean = false;
            videoSliceSeekBar.videoPlayingProgress(cropVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingProgress(cropVideoView.getCurrentPosition());
            seekbarTimer.videoPlayingTime(UtilCommand.milliSecondsToTimer(cropVideoView.getCurrentPosition()));
            if (!cropVideoView.isPlaying() || cropVideoView.getCurrentPosition() >= videoSliceSeekBar.getRightProgress()) {
                if (cropVideoView.isPlaying()) {
                    cropVideoView.pause();
                    imagePlayPause.setImageResource(R.drawable.crop_video_play_img);
                    cropVideoView.seekTo(100);
                }
                videoSliceSeekBar.setSliceBlocked(false);
                videoSliceSeekBar.removeVideoStatusThumb();
                seekbarTimer.removeVideoStatusThumb();
                return;
            }
            postDelayed(this.aRunnable, 50);
        }
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_crop_video);

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
                if (cropVideoView != null && cropVideoView.isPlaying()) {
                    cropVideoView.pause();
                }
                cropcommand();
            }
        });

        if (af) {
            this.videoName = getIntent().getStringExtra("videofilename");
            if (this.videoName != null) {
                this.bitmap = ThumbnailUtils.createVideoThumbnail(this.videoName, 1);
            }
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.rl_container);
            LayoutParams layoutParams = (LayoutParams) frameLayout.getLayoutParams();
            this.x = FileUtils.getScreenWidth();
            layoutParams.width = this.x;
            layoutParams.height = this.x;
            frameLayout.setLayoutParams(layoutParams);
            cropperView = (CropImageView) findViewById(R.id.cropperView);
            idBinding();
            cropVid();
            return;
        }
        throw new AssertionError();
    }

    public void nextGo() {
        Intent intent = new Intent(getApplicationContext(), VideoPreviewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("song", videoPathFinal);
        startActivity(intent);
        finish();
    }

    public void cropcommand() {
        videoHeightWidth();
        getpath();
    }

    @SuppressLint("InvalidWakeLockTag")
    public void getpath() {
        if (this.w == 90) {
            try {
                this.o = this.B;
                int i2 = this.z;
                this.u = this.B;
                this.v = this.A;
                this.m = this.y;
                this.n = this.z;
                this.s = this.y;
                this.t = this.A;
                this.ag = this.m - this.o;
                this.ah = this.v - i2;
                this.p = this.q - (this.ah + i2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (this.w == 270) {
            try {
                int i3 = this.B;
                int i4 = this.z;
                this.u = this.B;
                this.v = this.A;
                this.m = this.y;
                this.n = this.z;
                this.s = this.y;
                this.t = this.A;
                this.ag = this.m - i3;
                this.ah = this.v - i4;
                this.o = this.r - (this.ag + i3);
                this.p = i4;
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            try {
                this.o = this.z;
                this.p = this.B;
                this.u = this.A;
                this.v = this.B;
                this.m = this.z;
                this.n = this.y;
                this.s = this.A;
                this.t = this.y;
                this.ag = this.u - this.o;
                this.ah = this.n - this.p;
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        this.videoStartTime = String.valueOf(this.videoPlayerState.getStart() / 1000);
        this.videoDuration = String.valueOf(this.videoPlayerState.getDuration() / 1000);
        this.string3 = this.videoName;
        if (this.string3.contains(".3gp") || this.string3.contains(".3GP")) {
            try {
                this.videoPath = FileUtils.getTargetFileName(this, this.string3.replace(".3gp", ".mp4"));
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        } else if (this.string3.contains(".flv") || this.string3.contains(".FLv")) {
            try {
                this.videoPath = FileUtils.getTargetFileName(this, this.string3.replace(".flv", ".mp4"));
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        } else if (this.string3.contains(".mov") || this.string3.contains(".MOV")) {
            try {
                this.videoPath = FileUtils.getTargetFileName(this, this.string3.replace(".mov", ".mp4"));
            } catch (Exception e7) {
                e7.printStackTrace();
            }
        } else if (this.string3.contains(".wmv") || this.string3.contains(".WMV")) {
            try {
                this.videoPath = FileUtils.getTargetFileName(this, this.string3.replace(".wmv", ".mp4"));
            } catch (Exception e8) {
                e8.printStackTrace();
            }
        } else {
            try {
                this.videoPath = FileUtils.getTargetFileName(this, this.string3);
            } catch (Exception e9) {
                e9.printStackTrace();
            }
        }
        videoPathFinal = "/storage/emulated/0/Movies" + FileUtils.getTargetFileName(this, this.videoPath);
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long availableBlocks = ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
        File file = new File(this.videoPlayerState.getFilename());
        this.ae = 0;
        this.ae = file.length() / 1024;
        if ((availableBlocks / 1024) / 1024 >= this.ae / 1024) {
            ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(1, "VK_LOCK").acquire();
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("crop=w=");
                sb.append(this.ag);
                sb.append(":h=");
                sb.append(this.ah);
                sb.append(":x=");
                sb.append(this.o);
                sb.append(":y=");
                sb.append(this.p);
                String[] strArr = new String[]{"-y", "-ss", this.videoStartTime, "-t", this.videoDuration, "-i", this.string3, "-strict", "experimental", "-vf", sb.toString(), "-r", "15", "-ab", "128k", "-vcodec", "mpeg4", "-acodec", "copy", "-b:v", "2500k", "-sample_fmt", "s16", "-ss", "0", "-t", this.videoDuration, videoPathFinal};

                videoDownload(strArr, videoPathFinal);
            } catch (Exception unused) {
                File file2 = new File(videoPathFinal);
                if (file2.exists()) {
                    file2.delete();
                    finish();
                    return;
                }
                Toast.makeText(this, "please select any option!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Out of Memory!......", 0).show();
        }
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

                com.arthenica.mobileffmpeg.Config.printLastCommandOutput(Log.INFO);

                progressDialog.dismiss();
                if (returnCode == RETURN_CODE_SUCCESS) {
                    progressDialog.dismiss();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(videoPathFinal)));
                    CropVideoActivity.this.sendBroadcast(intent);
                    nextGo();
                    CropVideoActivity.this.refreshGallery(str);

                } else if (returnCode == RETURN_CODE_CANCEL) {

                    try {
                        new File(str).delete();
                        CropVideoActivity.this.deleteFromGallery(str);
                        Toast.makeText(CropVideoActivity.this, "Error Creating Video", Toast.LENGTH_LONG).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    try {
                        new File(str).delete();
                        CropVideoActivity.this.deleteFromGallery(str);
                        Toast.makeText(CropVideoActivity.this, "Error Creating Video", Toast.LENGTH_LONG).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onResume() {
        cropVideoView.seekTo(this.videoPlayerState.getCurrentTime());
        super.onResume();
    }


    public void onPause() {
        super.onPause();
        this.videoPlayerState.setCurrentTime(cropVideoView.getCurrentPosition());
    }

    private void idBinding() {

        left_pointer = (TextView) findViewById(R.id.left_pointer);
        right_pointer = (TextView) findViewById(R.id.right_pointer);
        videoSliceSeekBar = (VideoSliceSeekBar4) findViewById(R.id.videoSliceSeekBar);
        seekbarTimer = (VideoSeekBarTimer) findViewById(R.id.seekbarTimer);

        imagePlayPause = findViewById(R.id.imagePlayPause);
        imagePlayPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cropVideoView == null || !cropVideoView.isPlaying()) {
                    imagePlayPause.setImageResource(R.drawable.crop_video_pause_img);
                } else {
                    imagePlayPause.setImageResource(R.drawable.crop_video_play_img);
                }
                if (cropVideoView.isPlaying()) {
                    cropVideoView.pause();
                    videoSliceSeekBar.setSliceBlocked(false);
                    videoSliceSeekBar.removeVideoStatusThumb();
                    seekbarTimer.removeVideoStatusThumb();
                    return;
                }
                cropVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
                cropVideoView.start();
                videoSliceSeekBar.videoPlayingProgress(videoSliceSeekBar.getLeftProgress());
                handler.myMethod();
            }
        });
        Object lastNonConfigurationInstance = getLastNonConfigurationInstance();
        if (lastNonConfigurationInstance != null) {
            this.videoPlayerState = (VideoPlayerState) lastNonConfigurationInstance;
        } else {
            this.videoPlayerState.setFilename(this.videoName);
        }

        videoCropImg_1 = (ImageView) findViewById(R.id.videoCropImg_1);
        videoCropImg_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setFixedAspectRatio(false);
                unSelectedAll();
                videoCropImg_1.setImageResource(R.drawable.video_crop_img_1_);
            }
        });

        videoCropImg_2 = (ImageView) findViewById(R.id.videoCropImg_2);
        videoCropImg_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setFixedAspectRatio(CropVideoActivity.af);
                cropperView.setAspectRatio(10, 10);
                unSelectedAll();
                videoCropImg_2.setImageResource(R.drawable.video_crop_img_2_);
            }
        });

        videoCropImg_3 = (ImageView) findViewById(R.id.videoCropImg_3);
        videoCropImg_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setFixedAspectRatio(CropVideoActivity.af);
                cropperView.setAspectRatio(8, 16);
                unSelectedAll();
                videoCropImg_3.setImageResource(R.drawable.video_crop_img_3_);
            }
        });

        videoCropImg_4 = (ImageView) findViewById(R.id.videoCropImg_4);
        videoCropImg_4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setFixedAspectRatio(CropVideoActivity.af);
                cropperView.setAspectRatio(16, 8);
                unSelectedAll();
                videoCropImg_4.setImageResource(R.drawable.video_crop_img_4_);
            }
        });

        videoCropImg_5 = (ImageView) findViewById(R.id.videoCropImg_5);
        videoCropImg_5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setFixedAspectRatio(CropVideoActivity.af);
                cropperView.setAspectRatio(3, 2);
                unSelectedAll();
                videoCropImg_5.setImageResource(R.drawable.video_crop_img_5_);
            }
        });

        videoCropImg_6 = (ImageView) findViewById(R.id.videoCropImg_6);
        videoCropImg_6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setFixedAspectRatio(CropVideoActivity.af);
                cropperView.setAspectRatio(2, 3);
                unSelectedAll();
                videoCropImg_6.setImageResource(R.drawable.video_crop_img_6_);
            }
        });

        videoCropImg_7 = (ImageView) findViewById(R.id.videoCropImg_7);
        videoCropImg_7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setFixedAspectRatio(CropVideoActivity.af);
                cropperView.setAspectRatio(4, 3);
                unSelectedAll();
                videoCropImg_7.setImageResource(R.drawable.video_crop_img_7_);
            }
        });

        videoCropImg_8 = (ImageView) findViewById(R.id.videoCropImg_8);
        videoCropImg_8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.setFixedAspectRatio(CropVideoActivity.af);
                cropperView.setAspectRatio(5, 4);
                unSelectedAll();
                videoCropImg_8.setImageResource(R.drawable.video_crop_img_8_);
            }
        });
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

    @SuppressLint({"NewApi"})
    private void cropVid() {
        cropVideoView = (VideoView) findViewById(R.id.cropVideoView);
        cropVideoView.setVideoPath(this.videoName);
        string2 = getTimeForTrackFormat(cropVideoView.getDuration(), af);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.videoName);
        this.r = Integer.valueOf(mediaMetadataRetriever.extractMetadata(18)).intValue();
        this.q = Integer.valueOf(mediaMetadataRetriever.extractMetadata(19)).intValue();
        if (VERSION.SDK_INT > 16) {
            this.w = Integer.valueOf(mediaMetadataRetriever.extractMetadata(24)).intValue();
        } else {
            this.w = 0;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cropperView.getLayoutParams();
        if (this.w == 90 || this.w == 270) {
            if (this.r >= this.q) {
                if (this.r >= this.x) {
                    layoutParams.height = this.x;
                    layoutParams.width = (int) (((float) this.x) / (((float) this.r) / ((float) this.q)));
                } else {
                    layoutParams.width = this.x;
                    layoutParams.height = (int) (((float) this.q) * (((float) this.x) / ((float) this.r)));
                }
            } else if (this.q >= this.x) {
                layoutParams.width = this.x;
                layoutParams.height = (int) (((float) this.x) / (((float) this.q) / ((float) this.r)));
            } else {
                layoutParams.width = (int) (((float) this.r) * (((float) this.x) / ((float) this.q)));
                layoutParams.height = this.x;
            }
        } else if (this.r >= this.q) {
            if (this.r >= this.x) {
                layoutParams.width = this.x;
                layoutParams.height = (int) (((float) this.x) / (((float) this.r) / ((float) this.q)));
            } else {
                layoutParams.width = this.x;
                layoutParams.height = (int) (((float) this.q) * (((float) this.x) / ((float) this.r)));
            }
        } else if (this.q >= this.x) {
            layoutParams.width = (int) (((float) this.x) / (((float) this.q) / ((float) this.r)));
            layoutParams.height = this.x;
        } else {
            layoutParams.width = (int) (((float) this.r) * (((float) this.x) / ((float) this.q)));
            layoutParams.height = this.x;
        }
        cropperView.setLayoutParams(layoutParams);
        cropperView.setImageBitmap(Bitmap.createBitmap(layoutParams.width, layoutParams.height, Config.ARGB_8888));
        try {
            SearchVideo(getApplicationContext(), this.videoName, layoutParams.width, layoutParams.height);
        } catch (Exception unused) {
        }
        cropVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {

                if (cropVideoView.getDuration() / 1000 <= 13 && cropVideoView.getDuration() / 1000 >= 6) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(CropVideoActivity.this,
                            videoName, 1000, 160, picassoLoader));

                } else if (cropVideoView.getDuration() / 1000 <= 5 && cropVideoView.getDuration() / 1000 >= 3) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(CropVideoActivity.this,
                            videoName, 500, 160, picassoLoader));

                } else if (cropVideoView.getDuration() / 1000 <= 2 && cropVideoView.getDuration() / 1000 >= 1) {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(CropVideoActivity.this,
                            videoName, 100, 160, picassoLoader));

                } else {

                    rvVideoFrame.setAdapter(new MediaRetrieverAdapter(CropVideoActivity.this,
                            videoName, 2000, 160, picassoLoader));
                }

                videoSliceSeekBar.setSeekBarChangeListener(new VideoSliceSeekBar4.SeekBarChangeListener() {
                    public void SeekBarValueChanged(int i, int i2) {
                        if (videoSliceSeekBar.getSelectedThumb() == 1) {
                            cropVideoView.seekTo(videoSliceSeekBar.getLeftProgress());
                        }
                        left_pointer.setText(CropVideoActivity.getTimeForTrackFormat(i, CropVideoActivity.af));
                        right_pointer.setText(CropVideoActivity.getTimeForTrackFormat(i2, CropVideoActivity.af));
                        string1 = CropVideoActivity.getTimeForTrackFormat(i, CropVideoActivity.af);
                        videoPlayerState.setStart(i);
                        string2 = CropVideoActivity.getTimeForTrackFormat(i2, CropVideoActivity.af);
                        videoPlayerState.setStop(i2);
                    }
                });
                string2 = CropVideoActivity.getTimeForTrackFormat(mediaPlayer.getDuration(), CropVideoActivity.af);
                videoSliceSeekBar.setMaxValue(mediaPlayer.getDuration());
                seekbarTimer.setMaxValue(mediaPlayer.getDuration());
                videoSliceSeekBar.setLeftProgress(0);
                seekbarTimer.setLeftProgress(0);
                videoSliceSeekBar.setRightProgress(mediaPlayer.getDuration());
                seekbarTimer.setRightProgress(mediaPlayer.getDuration());
                videoSliceSeekBar.setProgressMinDiff(0);
                seekbarTimer.setProgressMinDiff(0);
                cropVideoView.seekTo(100);
            }
        });
    }


    public void unSelectedAll() {
        videoCropImg_1.setImageResource(R.drawable.video_crop_img_1);
        videoCropImg_2.setImageResource(R.drawable.video_crop_img_2);
        videoCropImg_3.setImageResource(R.drawable.video_crop_img_3);
        videoCropImg_4.setImageResource(R.drawable.video_crop_img_4);
        videoCropImg_5.setImageResource(R.drawable.video_crop_img_5);
        videoCropImg_6.setImageResource(R.drawable.video_crop_img_6);
        videoCropImg_7.setImageResource(R.drawable.video_crop_img_7);
        videoCropImg_8.setImageResource(R.drawable.video_crop_img_8);
    }

    public static String getTimeForTrackFormat(int i2, boolean z2) {
        String str;
        int i3 = i2 / 60000;
        int i4 = (i2 - ((i3 * 60) * 1000)) / 1000;
        StringBuilder sb = new StringBuilder(String.valueOf((!z2 || i3 >= 10) ? "" : "0"));
        sb.append(i3 % 60);
        sb.append(":");
        String sb2 = sb.toString();
        if (i4 < 10) {
            StringBuilder sb3 = new StringBuilder(String.valueOf(sb2));
            sb3.append("0");
            sb3.append(i4);
            str = sb3.toString();
        } else {
            StringBuilder sb4 = new StringBuilder(String.valueOf(sb2));
            sb4.append(i4);
            str = sb4.toString();
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Display Result");
        sb5.append(str);
        Log.e("", sb5.toString());
        return str;
    }

    private void videoHeightWidth() {
        if (this.w == 90 || this.w == 270) {
            this.aa = (float) this.q;
            this.ab = (float) this.r;
            this.ac = (float) cropperView.getWidth();
            this.ad = (float) cropperView.getHeight();
            this.z = (int) ((Edge.LEFT.getCoordinate() * this.aa) / this.ac);
            this.A = (int) ((Edge.RIGHT.getCoordinate() * this.aa) / this.ac);
            this.B = (int) ((Edge.TOP.getCoordinate() * this.ab) / this.ad);
            this.y = (int) ((Edge.BOTTOM.getCoordinate() * this.ab) / this.ad);
            return;
        }
        this.aa = (float) this.r;
        this.ab = (float) this.q;
        this.ac = (float) cropperView.getWidth();
        this.ad = (float) cropperView.getHeight();
        this.z = (int) ((Edge.LEFT.getCoordinate() * this.aa) / this.ac);
        this.A = (int) ((Edge.RIGHT.getCoordinate() * this.aa) / this.ac);
        this.B = (int) ((Edge.TOP.getCoordinate() * this.ab) / this.ad);
        this.y = (int) ((Edge.BOTTOM.getCoordinate() * this.ab) / this.ad);
    }

    public void SearchVideo(Context context, String str, int i2, int i3) {
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String[] strArr = {"_data", "_id"};
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        sb.append(str);
        sb.append("%");
        Cursor managedQuery = managedQuery(uri, strArr, "_data  like ?", new String[]{sb.toString()}, " _id DESC");
        int count = managedQuery.getCount();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("count");
        sb2.append(count);
        Log.e("", sb2.toString());
        if (count > 0) {
            managedQuery.moveToFirst();
            Long.valueOf(managedQuery.getLong(managedQuery.getColumnIndexOrThrow("_id")));
            managedQuery.moveToNext();
        }
    }


    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
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

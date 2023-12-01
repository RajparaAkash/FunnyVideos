package com.exmple.funnyvideo.ExtractMusic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.exmple.funnyvideo.ExtractMusic.Helper.App;
import com.exmple.funnyvideo.ExtractMusic.Helper.PreferenceUtil;
import com.exmple.funnyvideo.ExtractMusic.Helper.FFmpegConverter;
import com.exmple.funnyvideo.ExtractMusic.Helper.Utility;
import com.exmple.funnyvideo.R;

import java.io.IOException;

public class VideoConverterActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageView imagePlayPause;
    private EditText et_music_title;

    private LinearLayout formate_ly_1;
    private LinearLayout formate_ly_2;
    private LinearLayout bitrate_ly_1;
    private LinearLayout bitrate_ly_2;
    private LinearLayout bitrate_ly_3;

    private FFmpegConverter ffmpegConverter;
    private String videoPath = "";
    private long videoDuration = 0;

    private String valueFormat1 = ".mp3";
    private String valueFormat2 = "mp3";
    private String valueBitrate = "128K";

    private Handler myHandler;

    private final InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (charSequence == null || !"<>:*./".contains("" + ((Object) charSequence))) {
                return null;
            }
            return "";
        }
    };


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_converter);

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imagePlayPause = (ImageView) findViewById(R.id.imagePlayPause);
        formate_ly_1 = (LinearLayout) findViewById(R.id.formate_ly_1);
        formate_ly_2 = (LinearLayout) findViewById(R.id.formate_ly_2);
        bitrate_ly_1 = (LinearLayout) findViewById(R.id.bitrate_ly_1);
        bitrate_ly_2 = (LinearLayout) findViewById(R.id.bitrate_ly_2);
        bitrate_ly_3 = (LinearLayout) findViewById(R.id.bitrate_ly_3);

        this.ffmpegConverter = App.getInstance().fFmpegConverter;
        myHandler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        videoPath = intent.getStringExtra("video_path");
        String stringExtra = intent.getStringExtra("video_title");
        this.videoDuration = Long.parseLong(intent.getStringExtra("video_duration"));

        et_music_title = (EditText) findViewById(R.id.et_music_title);
        et_music_title.setFilters(new InputFilter[]{this.filter});

        String str = "";
        for (char c : stringExtra.toCharArray()) {
            if (!"<>:*./".contains("" + c)) {
                str = str + c;
            }
        }
        et_music_title.setText(str);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(this.videoPath));
        MediaController mediaController = new MediaController(this) {
            @Override
            public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {
                super.setMediaPlayer(mediaPlayerControl);
                show(4000);
            }

            @Override
            public void show() {
                super.show();
            }
        };
        mediaController.setAnchorView(videoView);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
            }
        });

        videoView.seekTo(100);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                } else {
                    videoView.start();
                    imagePlayPause.setVisibility(View.INVISIBLE);
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                imagePlayPause.setVisibility(View.VISIBLE);
                imagePlayPause.setImageResource(R.drawable.video_play_img);
            }
        });

        findViewById(R.id.music_extract_img_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
                convertVideo1();
            }
        });

        findViewById(R.id.music_extract_img_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    imagePlayPause.setVisibility(View.VISIBLE);
                    imagePlayPause.setImageResource(R.drawable.video_play_img);
                }
                convertVideo2();
            }
        });

        formate_ly_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelect1();
                formate_ly_1.setBackgroundResource(R.drawable.bg_7);
                valueFormat1 = ".mp3";
                valueFormat2 = "mp3";
            }
        });

        formate_ly_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelect1();
                formate_ly_2.setBackgroundResource(R.drawable.bg_7);
                valueFormat1 = ".aac";
                valueFormat2 = "aac";
            }
        });

        bitrate_ly_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelect2();
                bitrate_ly_1.setBackgroundResource(R.drawable.bg_7);
                valueBitrate = "128K";
            }
        });

        bitrate_ly_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelect2();
                bitrate_ly_2.setBackgroundResource(R.drawable.bg_7);
                valueBitrate = "192K";
            }
        });

        bitrate_ly_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelect2();
                bitrate_ly_3.setBackgroundResource(R.drawable.bg_7);
                valueBitrate = "320K";
            }
        });
    }


    private void unSelect1() {
        formate_ly_1.setBackgroundResource(R.drawable.bg_8);
        formate_ly_2.setBackgroundResource(R.drawable.bg_8);
    }

    private void unSelect2() {
        bitrate_ly_1.setBackgroundResource(R.drawable.bg_8);
        bitrate_ly_2.setBackgroundResource(R.drawable.bg_8);
        bitrate_ly_3.setBackgroundResource(R.drawable.bg_8);
    }

    public void convertVideo1() {

        String replace = et_music_title.getText().toString().replace('.', '_');
        if (replace.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Music title empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        String str = PreferenceUtil.getMusicOutputFolder() +
                System.getProperty("file.separator") + replace +
                (valueFormat1);
        String str2 = valueFormat2;
        String str3 = valueBitrate;

        if (ffmpegConverter.musicFileExists(str)) {
            Toast.makeText(getApplicationContext(), "Music file already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        ffmpegConverter.appendConversionOrder(
                videoPath, str, str2, str3, videoDuration);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Utility.isFinished) {

                    progressDialog.dismiss();

                    long duration = getVideoDuration(Uri.parse(str));
                    String fileName = getFileNameFromPath(str);

                    Intent intent2 = new Intent(VideoConverterActivity.this, AudioCutterActivity.class);
                    intent2.putExtra("music_path", str);
                    intent2.putExtra("music_filename", fileName);
                    intent2.putExtra("duration", duration);
                    startActivity(intent2);

                    myHandler.removeCallbacksAndMessages(null);

                    Utility.isFinished = false;
                    return;
                }
                myHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public void convertVideo2() {

        String replace = et_music_title.getText().toString().replace('.', '_');
        if (replace.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Music title empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        String str = PreferenceUtil.getMusicOutputFolder() +
                System.getProperty("file.separator") + replace +
                (valueFormat1);
        String str2 = valueFormat2;
        String str3 = valueBitrate;

        if (ffmpegConverter.musicFileExists(str)) {
            Toast.makeText(getApplicationContext(), "Music file already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        ffmpegConverter.appendConversionOrder(
                videoPath, str, str2, str3, videoDuration);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Utility.isFinished) {

                    progressDialog.dismiss();

                    long duration = getVideoDuration(Uri.parse(str));
                    String fileName = getFileNameFromPath(str);

                    Intent intent2 = new Intent(VideoConverterActivity.this, MusicPreviewActivity.class);
                    intent2.putExtra("music_path", str);
                    intent2.putExtra("music_filename", fileName);
                    intent2.putExtra("duration", duration);
                    startActivity(intent2);

                    myHandler.removeCallbacksAndMessages(null);

                    Utility.isFinished = false;
                    return;
                }
                myHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }

    private long getVideoDuration(Uri videoUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoUri);
        String durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Convert duration to long
        return Long.parseLong(durationString);
    }

    private String getFileNameFromPath(String filePath) {
        // Use File class to extract the file name from the path
        java.io.File file = new java.io.File(filePath);
        return file.getName();
    }
}

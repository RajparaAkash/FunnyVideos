package com.exmple.funnyvideo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Video.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.exmple.funnyvideo.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

@SuppressLint({"WrongConstant"})
public class VideoPreviewActivity extends AppCompatActivity implements OnSeekBarChangeListener {

    VideoView videoView;
    ImageView btnPlayVideo;
    TextView left_pointer;
    TextView right_pointer;
    SeekBar sbVideo;

    public static boolean myBool = true;
    Bundle bundle;
    int myint = 0;
    Handler handler = new Handler();
    boolean isMyBool = false;
    int pos = 0;
    Uri uri1;
    Uri uri2;
    String myString = "";
    Runnable myRunnable = new Runnable() {
        public void run() {
            if (videoView.isPlaying()) {
                int currentPosition = videoView.getCurrentPosition();
                sbVideo.setProgress(currentPosition);
                try {
                    left_pointer.setText(VideoPreviewActivity.formatTimeUnit((long) currentPosition));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (currentPosition == myint) {
                    sbVideo.setProgress(0);
                    left_pointer.setText("00:00");
                    handler.removeCallbacks(myRunnable);
                    return;
                }
                handler.postDelayed(myRunnable, 200);
                return;
            }
            sbVideo.setProgress(myint);
            try {
                left_pointer.setText(VideoPreviewActivity.formatTimeUnit((long) myint));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            handler.removeCallbacks(myRunnable);
        }
    };

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_preview);

        findViewById(R.id.back_img).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.homeImg).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPreviewActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.shareVideoImg).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    handler.removeCallbacks(myRunnable);
                    btnPlayVideo.setVisibility(View.VISIBLE);
                    btnPlayVideo.setImageResource(R.drawable.video_play_img);
                    isMyBool = false;
                }
                try {
                    File file = new File(myString);

                    Uri screenshotUri = FileProvider.getUriForFile(VideoPreviewActivity.this, getApplicationContext().getPackageName() + ".provider", file);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("video/*");
                    intent.putExtra("android.intent.extra.STREAM", screenshotUri);
                    startActivity(Intent.createChooser(intent, "Share File"));

                } catch (Exception unused) {
                }
            }
        });


        if (myBool) {
            this.bundle = getIntent().getExtras();
            if (this.bundle != null) {
                this.myString = this.bundle.getString("song");
                this.pos = this.bundle.getInt("position", 0);
            }
            try {
                GetVideo(getApplicationContext(), myString);
            } catch (Exception unused) {
            }

            videoView = (VideoView) findViewById(R.id.videoView);
            sbVideo = (SeekBar) findViewById(R.id.sbVideo);
            sbVideo.setOnSeekBarChangeListener(this);
            left_pointer = (TextView) findViewById(R.id.left_pointer);
            right_pointer = (TextView) findViewById(R.id.right_pointer);
            btnPlayVideo = (ImageView) findViewById(R.id.btnPlayVideo);

            ((TextView) findViewById(R.id.header_title)).setText(new File(myString).getName());

            videoView.setVideoPath(myString);
            videoView.seekTo(100);

            videoView.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    Toast.makeText(VideoPreviewActivity.this.getApplicationContext(), "Video Player Not Supproting", 0).show();
                    return VideoPreviewActivity.myBool;
                }
            });

            videoView.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer mediaPlayer) {
                    myint = videoView.getDuration();
                    sbVideo.setMax(myint);
                    left_pointer.setText("00:00");
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("duration : ");
                        sb.append(VideoPreviewActivity.formatTimeUnit((long) myint));
                        /*textView.setText(sb.toString());*/
                        right_pointer.setText(VideoPreviewActivity.formatTimeUnit((long) myint));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            videoView.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    videoView.setVisibility(0);
                    btnPlayVideo.setVisibility(View.VISIBLE);
                    btnPlayVideo.setImageResource(R.drawable.video_play_img);
                    videoView.seekTo(0);
                    sbVideo.setProgress(0);
                    left_pointer.setText("00:00");
                    handler.removeCallbacks(myRunnable);
                    VideoPreviewActivity.this.isMyBool ^= VideoPreviewActivity.myBool;
                }
            });

            btnPlayVideo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isMyBool) {
                        try {
                            videoView.pause();
                            handler.removeCallbacks(myRunnable);
                            btnPlayVideo.setVisibility(View.VISIBLE);
                            btnPlayVideo.setImageResource(R.drawable.video_play_img);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            videoView.seekTo(sbVideo.getProgress());
                            videoView.start();
                            handler.postDelayed(myRunnable, 200);
                            videoView.setVisibility(0);
                            btnPlayVideo.setVisibility(View.INVISIBLE);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    VideoPreviewActivity.this.isMyBool ^= VideoPreviewActivity.myBool;
                }
            });

            videoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isMyBool) {
                        try {
                            videoView.pause();
                            handler.removeCallbacks(myRunnable);
                            btnPlayVideo.setVisibility(View.VISIBLE);
                            btnPlayVideo.setImageResource(R.drawable.video_play_img);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            videoView.seekTo(sbVideo.getProgress());
                            videoView.start();
                            handler.postDelayed(myRunnable, 200);
                            videoView.setVisibility(0);
                            btnPlayVideo.setVisibility(View.INVISIBLE);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    VideoPreviewActivity.this.isMyBool ^= VideoPreviewActivity.myBool;
                }
            });


        } else {
            throw new AssertionError();
        }
    }

    public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
        if (z) {
            videoView.seekTo(i2);
            try {
                left_pointer.setText(formatTimeUnit((long) i2));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static String formatTimeUnit(long j2) throws ParseException {
        return String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j2)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)))});
    }

    public void GetVideo(Context context, String str) {
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String[] strArr = {"_id", "_data", "_display_name", "_size", "duration", "date_added", "album"};
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        sb.append(str);
        sb.append("%");
        String[] strArr2 = {sb.toString()};
        Cursor managedQuery = managedQuery(uri, strArr, "_data  like ?", strArr2, " _id DESC");
        if (managedQuery.moveToFirst()) {
            try {
                Uri withAppendedPath = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, getLong(managedQuery));
                this.uri1 = withAppendedPath;
                this.uri2 = withAppendedPath;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String getLong(Cursor cursor) {
        return String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /*public void Delete() {
        new AlertDialog.Builder(this).setMessage("Are you sure you want to delete this file ?").setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File file = new File(myString);
                if (file.exists()) {
                    file.delete();
                    try {
                        ContentResolver contentResolver = VideoPreviewActivity.this.getContentResolver();
                        Uri uri = VideoPreviewActivity.this.uri1;
                        StringBuilder sb = new StringBuilder();
                        sb.append("_data=\"");
                        sb.append(myString);
                        sb.append("\"");
                        contentResolver.delete(uri, sb.toString(), null);
                    } catch (Exception unused) {
                    }
                }
                VideoPreviewActivity.this.onBackPressed();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setCancelable(myBool).show();
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.Delete) {
            if (videoView.isPlaying()) {
                try {
                    videoView.pause();
                    handler.removeCallbacks(myRunnable);
                    btnPlayVideo.setVisibility(View.VISIBLE);
                    btnPlayVideo.setImageResource(R.drawable.video_play_img);
                    this.isMyBool = false;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            Delete();
        }
        return super.onOptionsItemSelected(menuItem);
    }*/
}

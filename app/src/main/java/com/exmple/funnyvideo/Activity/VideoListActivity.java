package com.exmple.funnyvideo.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.exmple.funnyvideo.Adapter.VideoListAdapter;
import com.exmple.funnyvideo.Adapter.VideoListAdapter1;
import com.exmple.funnyvideo.CompressVideo.CompressVideoActivity;
import com.exmple.funnyvideo.ExtractMusic.VideoConverterActivity;
import com.exmple.funnyvideo.FilterVideo.VideoFilterActivity;
import com.exmple.funnyvideo.ReverseVideo.ReverseActivity;
import com.exmple.funnyvideo.SlowMotion.SlowMotionVideoActivity;
import com.exmple.funnyvideo.SpeedUp.SpeedUpVideoActivity;
import com.exmple.funnyvideo.Model.AppPrefrence;
import com.exmple.funnyvideo.Model.MediaPojo;
import com.exmple.funnyvideo.MyUtils.MyUtil;
import com.exmple.funnyvideo.R;
import com.exmple.funnyvideo.TrimVideo.TrimVideoActivity;
import com.exmple.funnyvideo.CropVideo.CropVideoActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class VideoListActivity extends AppCompatActivity implements VideoListAdapter.OnVideoItemSelectedListener, VideoListAdapter1.OnVideoItemSelectedListener {

    ArrayList<MediaPojo> videoList;
    private RecyclerView recyclerView;
    TextView tv_NoResult;
    AppPrefrence appPrefrence;
    VideoListAdapter1 videoListAdapter1;
    ImageView rightImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        appPrefrence = new AppPrefrence(this);
        videoList = new ArrayList<MediaPojo>();
        videoList = getAllVideo(this);

        recyclerView = findViewById(R.id.rv_fileList);
        tv_NoResult = findViewById(R.id.tv_NoResult);
        rightImage = findViewById(R.id.rightImage);

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (appPrefrence.get_select().equals("one")) {
            VideoListAdapter adapter = new VideoListAdapter(VideoListActivity.this, videoList, this);
            recyclerView.setAdapter(adapter);
        } else if (appPrefrence.get_select().equals("two")) {
            videoListAdapter1 = new VideoListAdapter1(VideoListActivity.this, videoList, this);
            recyclerView.setAdapter(videoListAdapter1);
        } else {
            VideoListAdapter adapter = new VideoListAdapter(VideoListActivity.this, videoList, this);
            recyclerView.setAdapter(adapter);
        }
    }


    public static ArrayList<MediaPojo> getAllVideo(Context context) {
        ArrayList<MediaPojo> videoList = new ArrayList<>();
        String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            MediaPojo ImageModel = new MediaPojo(cursor.getInt(0), cursor.getString(1));
            videoList.add(ImageModel);
        }
        cursor.close();

        return videoList;
    }

    @Override
    public void onVideoSelected(int position) {

        rightImage.setAlpha(1f);
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyUtil.toolPos == 1) {

                    startActivity(new Intent(VideoListActivity.this, CropVideoActivity.class)
                            .putExtra("videofilename", videoList.get(position).getPath()));

                } else if (MyUtil.toolPos == 2) {

                    startActivity(new Intent(VideoListActivity.this, TrimVideoActivity.class)
                            .putExtra("videofilename", videoList.get(position).getPath()));

                } else if (MyUtil.toolPos == 4) {

                    startActivity(new Intent(VideoListActivity.this, VideoFilterActivity.class)
                            .putExtra("VideoPath", videoList.get(position).getPath()));

                } else if (MyUtil.toolPos == 5) {

                    startActivity(new Intent(VideoListActivity.this, CompressVideoActivity.class)
                            .putExtra("videofilename", videoList.get(position).getPath()));

                } else if (MyUtil.toolPos == 6) {

                    long duration = getVideoDuration(Uri.parse(videoList.get(position).getPath()));
                    String fileName = removeFileExtension(getFileNameFromPath(videoList.get(position).getPath()));

                    startActivity(new Intent(VideoListActivity.this, VideoConverterActivity.class)
                            .putExtra("video_path", videoList.get(position).getPath())
                            .putExtra("video_title", fileName)
                            .putExtra("video_duration", String.valueOf(duration)));

                } else if (MyUtil.toolPos == 7) {

                    startActivity(new Intent(VideoListActivity.this, SpeedUpVideoActivity.class)
                            .putExtra("videofilename", videoList.get(position).getPath()));

                } else if (MyUtil.toolPos == 8) {

                    startActivity(new Intent(VideoListActivity.this, SlowMotionVideoActivity.class)
                            .putExtra("videofilename", videoList.get(position).getPath()));

                } else if (MyUtil.toolPos == 9) {

                    startActivity(new Intent(VideoListActivity.this, ReverseActivity.class)
                            .putExtra("videofilename", videoList.get(position).getPath()));
                }
            }
        });
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

    private static String removeFileExtension(String filename) {
        // Find the last occurrence of the dot ('.') character
        int lastDotIndex = filename.lastIndexOf('.');

        // Check if the dot is found and not in the first position
        if (lastDotIndex != -1 && lastDotIndex > 0) {
            // Extract the substring before the last dot
            return filename.substring(0, lastDotIndex);
        } else {
            // No dot found or it's in the first position, return the original string
            return filename;
        }
    }

    @Override
    public void onVideoSelected(Set<Integer> selectedPositions) {
        for (int i = 0; i < 2; i++) {
            if (selectedPositions.size() > i) {
                rightImage.setAlpha(1f);
                rightImage.setEnabled(true);
            } else {
                rightImage.setAlpha(0.5f);
                rightImage.setEnabled(false);
            }
        }
        videoListAdapter1.notifyDataSetChanged();

        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer position : selectedPositions) {
                    Log.e("SelectedPath", "Path: " + videoList.get(position).getPath());
                }
            }
        });
    }
}
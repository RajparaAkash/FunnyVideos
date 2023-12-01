package com.exmple.funnyvideo.FilterVideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.daasuu.mp4compose.composer.Mp4Composer;
import com.daasuu.mp4compose.filter.GlBilateralFilter;
import com.daasuu.mp4compose.filter.GlBoxBlurFilter;
import com.daasuu.mp4compose.filter.GlBrightnessFilter;
import com.daasuu.mp4compose.filter.GlContrastFilter;
import com.daasuu.mp4compose.filter.GlFilter;
import com.daasuu.mp4compose.filter.GlGammaFilter;
import com.daasuu.mp4compose.filter.GlGrayScaleFilter;
import com.daasuu.mp4compose.filter.GlHueFilter;
import com.daasuu.mp4compose.filter.GlInvertFilter;
import com.daasuu.mp4compose.filter.GlLookUpTableFilter;
import com.daasuu.mp4compose.filter.GlLuminanceFilter;
import com.daasuu.mp4compose.filter.GlMonochromeFilter;
import com.daasuu.mp4compose.filter.GlPixelationFilter;
import com.daasuu.mp4compose.filter.GlSaturationFilter;
import com.daasuu.mp4compose.filter.GlSepiaFilter;
import com.daasuu.mp4compose.filter.GlSharpenFilter;
import com.daasuu.mp4compose.filter.GlVignetteFilter;
import com.exmple.funnyvideo.Activity.VideoPreviewActivity;
import com.exmple.funnyvideo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoFilterActivity extends AppCompatActivity {

    private VideoView videoView;
    private LinearLayout filterContainer;
    String videoPath;
    ImageView btnPlayVideo;
    boolean isMyBool = false;
    static final boolean myBool = true;

    private String selectedFilter = "";
    private View lastSelectedItem;
    private int[] filterDrawables = {
            R.drawable.filter_1,
            R.drawable.filter_2,
            R.drawable.filter_3,
            R.drawable.filter_4,
            R.drawable.filter_5,
            R.drawable.filter_6,
            R.drawable.filter_7,
            R.drawable.filter_8,
            R.drawable.filter_9,
            R.drawable.filter_10,
            R.drawable.filter_11,
            R.drawable.filter_12,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_filter);

        videoView = findViewById(R.id.videoView);
        filterContainer = findViewById(R.id.filterContainer);
        btnPlayVideo = (ImageView) findViewById(R.id.btnPlayVideo);

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Load filters dynamically
        loadFilters();

        // Set a sample video path
        videoPath = getIntent().getStringExtra("VideoPath");

        // Set video path and start playing
        videoView.setVideoPath(videoPath);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.setVisibility(View.VISIBLE);
                btnPlayVideo.setVisibility(View.VISIBLE);
                btnPlayVideo.setImageResource(R.drawable.video_play_img);
                videoView.seekTo(0);
                isMyBool ^= myBool;
            }
        });
        videoView.seekTo(100);


        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyBool) {
                    try {
                        videoView.pause();
                        btnPlayVideo.setVisibility(View.VISIBLE);
                        btnPlayVideo.setImageResource(R.drawable.video_play_img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        videoView.start();
                        videoView.setVisibility(View.VISIBLE);
                        btnPlayVideo.setVisibility(View.INVISIBLE);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                isMyBool ^= myBool;
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyBool) {
                    try {
                        videoView.pause();
                        btnPlayVideo.setVisibility(View.VISIBLE);
                        btnPlayVideo.setImageResource(R.drawable.video_play_img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        videoView.start();
                        videoView.setVisibility(View.VISIBLE);
                        btnPlayVideo.setVisibility(View.INVISIBLE);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                isMyBool ^= myBool;
            }
        });

        findViewById(R.id.downloadVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filteredVideoPath = applyFilter(selectedFilter);
                if (filteredVideoPath != null) {
                    downloadFilteredVideo(filteredVideoPath);
                } else {
                    Toast.makeText(VideoFilterActivity.this, "Error applying filter", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadFilters() {
        String[] filters = {
                "Sepia", "GrayScale", "Invert", "Monochrome", "Bilateral",
                "BoxBlur", "Vignette", "ToneCurve", "LookUpTable", "Contrast",
                "Brightness", "Hue"
        };


        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < filters.length; i++) {
            View filterItemView = inflater.inflate(R.layout.filter_item_layout, null);

            ImageView imageView = filterItemView.findViewById(R.id.filterImageView);
            imageView.setImageResource(filterDrawables[i % filterDrawables.length]);

            TextView textView = filterItemView.findViewById(R.id.filterTextView);
            textView.setText(filters[i]);

            ImageView selected = filterItemView.findViewById(R.id.selected);

            int finalI1 = i;
            filterItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectedItem != null && lastSelectedItem != selected) {
                        lastSelectedItem.setBackgroundResource(0);
                    }

                    selected.setBackgroundResource(R.drawable.select_border_filter);
                    lastSelectedItem = selected;
                    selectedFilter = filters[finalI1];

                    applyFilter(selectedFilter);
                }
            });
            filterContainer.addView(filterItemView);
        }
    }

    private ProgressDialog progressDialog;

    private String applyFilter(String selectedFilter) {
        // Show "Please Wait" dialog
        showProgressDialog();
        String outputVideoPath = getFilesDir().getAbsolutePath() + "/filtered_video.mp4";

        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }

        Mp4Composer mp4Composer = new Mp4Composer(videoPath, outputVideoPath)
                .filter(getFilterForName(selectedFilter))
                .listener(new Mp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        // Handle progress updates if needed
                    }

                    @Override
                    public void onCurrentWrittenVideoTime(long timeUs) {
                        // Handle the current written video time if needed
                    }

                    @Override
                    public void onCompleted() {
                        // Handle completion if needed
                        runOnUiThread(() -> {
                            // Dismiss the "Please Wait" dialog
                            dismissProgressDialog();

                            // Play the filtered video
                            videoView.setVideoPath(outputVideoPath);
                            videoView.setOnPreparedListener(mp -> {
                                // Ensure that the VideoView is prepared before starting playback
                                mp.start();
                            });
                            videoView.setOnCompletionListener(mp -> {
                                // Handle completion of video playback if needed
                            });
                            videoView.setOnErrorListener((mp, what, extra) -> {
                                // Handle errors during video playback if needed
                                return false;
                            });
                            videoView.requestFocus();
                            videoView.start();
                            btnPlayVideo.setVisibility(View.INVISIBLE);

                        });
                    }

                    @Override
                    public void onCanceled() {
                        // Handle cancellation if needed
                        runOnUiThread(() -> {
                            // Dismiss the "Please Wait" dialog
                            dismissProgressDialog();
                        });
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        // Handle failure
                        runOnUiThread(() -> {
                            // Dismiss the "Please Wait" dialog
                            dismissProgressDialog();
                            // Show an error dialog or log the error message
                            // Log.e("Mp4Composer", "Composition failed", exception);
                        });
                    }
                })
                .start();
        return outputVideoPath;
    }

    private void downloadFilteredVideo(String filteredVideoPath) {
        final ProgressDialog progressDialog = new ProgressDialog(VideoFilterActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Mp4Composer mp4Composer = new Mp4Composer(videoPath, filteredVideoPath)
                .filter(getFilterForName(selectedFilter))
                .listener(new Mp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        // Handle progress updates if needed
                    }

                    @Override
                    public void onCurrentWrittenVideoTime(long timeUs) {
                        // Handle the current written video time if needed
                    }

                    @Override
                    public void onCompleted() {
                        // Handle completion if needed
                        runOnUiThread(() -> {
                            progressDialog.dismiss();

                            // If the video is successfully filtered, initiate the download
                            initiateVideoDownload(filteredVideoPath);
                        });
                    }

                    @Override
                    public void onCanceled() {
                        // Handle cancellation if needed
                        runOnUiThread(() -> progressDialog.dismiss());
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        // Handle failure
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(VideoFilterActivity.this, "Error applying filter", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
    }


    private void initiateVideoDownload(String filteredVideoPath) {
        try {
            File sourceFile = new File(filteredVideoPath);

            if (sourceFile.exists()) {
                File targetDirectory = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MOVIES + "/" +
                                getResources().getString(R.string.app_name) + "/" +
                                getResources().getString(R.string.VideoFilter)).toURI());

                if (!targetDirectory.exists()) {
                    targetDirectory.mkdirs();
                }

                String timestamp = String.valueOf(System.currentTimeMillis());
                String uniqueFileName = "filtered_video_" + timestamp + ".mp4";

                File destinationFile = new File(targetDirectory, uniqueFileName);

                if (copyFile(sourceFile, destinationFile)) {
                    runOnUiThread(() -> {
                        Toast.makeText(VideoFilterActivity.this, "Download complete", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VideoFilterActivity.this, VideoPreviewActivity.class);
                        intent.putExtra("song", filteredVideoPath);
                        startActivity(intent);
                        // Handle other actions upon successful download if needed
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(VideoFilterActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(VideoFilterActivity.this, "Source file does not exist", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                Toast.makeText(VideoFilterActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private boolean copyFile(File sourceFile, File destFile) {
        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Applying Filter. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private GlFilter getFilterForName(String filterName) {
        switch (filterName) {
            case "Sepia":
                return new GlSepiaFilter();
            case "GrayScale":
                return new GlGrayScaleFilter();
            case "Invert":
                return new GlInvertFilter();
            case "Monochrome":
                return new GlMonochromeFilter();
            case "Bilateral":
                return new GlBilateralFilter();
            case "BoxBlur":
                return new GlBoxBlurFilter();
            case "Vignette":
                return new GlVignetteFilter();
            case "ToneCurve":
//                return new GlToneCurveFilter();
            case "LookUpTable":
                Bitmap lookupTableBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lookup_sample);
                return new GlLookUpTableFilter(lookupTableBitmap);
            case "Contrast":
                return new GlContrastFilter();
            case "Brightness":
                return new GlBrightnessFilter();
            case "Hue":
                return new GlHueFilter();
            case "Saturation":
                return new GlSaturationFilter();
            case "Sharpness":
                return new GlSharpenFilter();
            case "Gamma":
                return new GlGammaFilter();
            case "Luminance":
                return new GlLuminanceFilter();
            case "CrossProcess":
            case "Documentary":
            case "Autumn":
                return new GlPixelationFilter();
            case "Twilight":
            case "Watermark":
            case "Vintage":
                return new GlVignetteFilter();
            case "Fade":
                return new GlGammaFilter();
            case "Film":
                return new GlSepiaFilter();
            case "Pixelation":
                return new GlPixelationFilter();
            // Add more filters as needed
            default:
                return new GlSepiaFilter();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
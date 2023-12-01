package com.exmple.funnyvideo.LiveVideo.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.exmple.funnyvideo.LiveVideo.Common;
import com.exmple.funnyvideo.LiveVideo.Model.Data;
import com.exmple.funnyvideo.LiveVideo.database.DBHelper;
import com.exmple.funnyvideo.R;
import com.exmple.funnyvideo.VideoDownloader;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    Context mcontext;
    ArrayList<Data> courseDataArrayList;
    private ProgressDialog progressDialog;

    public RecyclerViewAdapter(ArrayList<Data> recyclerDataArrayList, Context mcontext) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Data data = courseDataArrayList.get(position);
        holder.videoName.setText(data.getTitle());

        /*holder.textlikes.setText(Integer.toString(data.getLikes()));*/

        holder.bind("http://159.65.146.129/EKYPBDF/78805655/" + data.getID() + ".mp4");

        /*holder.watchletter.setOnClickListener(view -> {
            Toast.makeText(mcontext, "Add to watch letter", Toast.LENGTH_SHORT).show();
            watchletterdata(courseDataArrayList.get(position));
        });*/

        if (checkFavoriteItem(data)) {
            holder.like.setVisibility(View.GONE);
            holder.unlike.setVisibility(View.VISIBLE);
            /*holder.textlikes.setText(Integer.toString(Integer.parseInt(holder.textlikes.getText().toString()) + 1));*/
        } else {
            holder.like.setVisibility(View.VISIBLE);
            holder.unlike.setVisibility(View.GONE);
        }

        holder.like.setOnClickListener(view -> {
            holder.like.setVisibility(View.GONE);
            holder.unlike.setVisibility(View.VISIBLE);
            /*Toast.makeText(mcontext, "Add to favourite ", Toast.LENGTH_SHORT).show();*/
            addFavoriteItem1(data);
        });

        holder.unlike.setOnClickListener(view -> {
            holder.unlike.setVisibility(View.GONE);
            holder.like.setVisibility(View.VISIBLE);
            /*Toast.makeText(mcontext, "Remove to favourite ", Toast.LENGTH_SHORT).show();*/
            addFavoriteItem2(data);
        });

        holder.videoPlayPauseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.player.isPlaying()) {
                    holder.videoPlayPauseImg.setVisibility(View.VISIBLE);
                    holder.player.pause();
                } else {
                    holder.videoPlayPauseImg.setVisibility(View.GONE);
                    holder.player.play();
                }
            }
        });

        holder.exoPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.player.isPlaying()) {
                    holder.videoPlayPauseImg.setVisibility(View.VISIBLE);
                    holder.player.pause();
                } else {
                    holder.videoPlayPauseImg.setVisibility(View.GONE);
                    holder.player.play();
                }
            }
        });

        holder.videoDownloadLy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {

                File downloadDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MOVIES);
                File videoFile = new File(downloadDir,
                        mcontext.getResources().getString(R.string.MainFolderName)
                                + "/Videos/" + data.getTitle() + ".mp4");
                if (videoFile.exists()) {
                    Toast.makeText(mcontext, "Already Downloaded", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressDialog();
                downloadVideo(
                        "http://159.65.146.129/EKYPBDF/78805655/" + data.getID() + ".mp4",
                        data.getTitle());
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setMessage("Downloading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void downloadVideo(String videoUrl, String title) {

        VideoDownloader.downloadVideo(mcontext, videoUrl, title, new VideoDownloader.DownloadListener() {
            @Override
            public void onProgress(final int progress) {

            }

            @Override
            public void onComplete() {
                MediaScannerConnection.scanFile(mcontext, new String[]{
                                new File(Environment.DIRECTORY_MOVIES,
                                        mcontext.getResources().getString(R.string.MainFolderName)
                                                + "/Videos/" + title + ".mp4").getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {

                            }
                        });
                progressDialog.dismiss();
                Toast.makeText(mcontext, "Download Complete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseDataArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView like;
        ImageView unlike;
        ImageView videoPlayPauseImg;
        TextView textlikes;
        TextView videoName;

        private PlayerView exoPlayerView;
        private SimpleExoPlayer player;
        ProgressBar progressBar;
        LinearLayout videoDownloadLy;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textlikes = itemView.findViewById(R.id.textlikes);
            like = itemView.findViewById(R.id.like);
            unlike = itemView.findViewById(R.id.unlike);
            videoPlayPauseImg = itemView.findViewById(R.id.videoPlayPauseImg);
            videoName = itemView.findViewById(R.id.videoName);

            exoPlayerView = itemView.findViewById(R.id.exoPlayerView);
            progressBar = itemView.findViewById(R.id.progressBar);
            videoDownloadLy = itemView.findViewById(R.id.videoDownloadLy);

            initializePlayer();
        }

        public void bind(String videoUrl) {
            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(false); // Autoplay can be managed as needed

            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    switch (playbackState) {
                        case Player.STATE_BUFFERING:
                            progressBar.setVisibility(View.VISIBLE);
                            videoPlayPauseImg.setVisibility(View.GONE);
                            break;
                        case Player.STATE_READY:
                            progressBar.setVisibility(View.GONE);
                            break;
                        case Player.STATE_ENDED:
                            // Handle video playback completed if needed
                            break;
                        default:
                            // Handle other states if needed
                            break;
                    }
                    Player.Listener.super.onPlaybackStateChanged(playbackState);
                }
            });

        }

        private void initializePlayer() {
            player = new SimpleExoPlayer.Builder(itemView.getContext()).build();
            exoPlayerView.setPlayer(player);
        }

        public void releasePlayer() {
            if (player != null) {
                player.pause();
                player.release();
            }
        }
    }

    void favdata(Data fav) {
        DBHelper dbHelper = new DBHelper(mcontext);

        boolean isContains = false;

        Cursor cursor = dbHelper.readdata();
        while (cursor.moveToNext()) {
            String id = cursor.getString(1);
            if (id.equals(fav.getID())) {
                isContains = true;
                break;
            }
        }
        if (!isContains) {
            dbHelper.insertData(fav.getID(), fav.getTitle(), fav.getCategory(), fav.getViews(), fav.getLikes());
        } else {
            Toast.makeText(mcontext, "All ready added", Toast.LENGTH_LONG).show();
        }
    }

    boolean checkFavoriteItem(Data item) {
        String likeListData = Common.getPreferenceString(mcontext, "likeList", "");
        if (!likeListData.isEmpty()) {
            List<String> likeList = Arrays.asList(likeListData.split(","));
            if (likeList.contains(item.getID())) {
                return true;
            }
        }
        return false;
    }

    void addFavoriteItem1(Data item) {
        String likeListData = Common.getPreferenceString(mcontext, "likeList", "");
        List<String> likeList = new ArrayList<String>();
        if (!likeListData.isEmpty()) {
            likeList.addAll(Arrays.asList(likeListData.split(",")));
        }
        if (likeList.contains(item.getID())) {
            likeList.remove(item.getID());
            DBHelper dbHelper = new DBHelper(mcontext);
            dbHelper.deleteData(item.getID());
        } else {
            likeList.add(item.getID());
            favdata(item);
        }
        String str = String.join(",", likeList);
        Common.setPreferenceString(mcontext, "likeList", str);
    }

    void addFavoriteItem2(Data item) {
        String likeListData = Common.getPreferenceString(mcontext, "likeList", "");
        List<String> likeList = new ArrayList<String>();
        if (!likeListData.isEmpty()) {
            likeList.addAll(Arrays.asList(likeListData.split(",")));
        }
        if (likeList.contains(item.getID())) {
            likeList.remove(item.getID());
            DBHelper dbHelper = new DBHelper(mcontext);
            dbHelper.deleteData(item.getID());

        } else {
            likeList.add(item.getID());
            favdata(item);
        }
        String str = String.join(",", likeList);
        Common.setPreferenceString(mcontext, "likeList", str);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.player.setPlayWhenReady(true);
        holder.videoPlayPauseImg.setVisibility(View.GONE);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.player.setPlayWhenReady(false);
        holder.releasePlayer();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerViewHolder holder) {
        super.onViewRecycled(holder);
        holder.player.release();
    }
}


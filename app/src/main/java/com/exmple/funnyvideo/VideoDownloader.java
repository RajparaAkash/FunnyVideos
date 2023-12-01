package com.exmple.funnyvideo;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class VideoDownloader {

    public interface DownloadListener {
        void onProgress(int progress);

        void onComplete();
    }

    private static long downloadId;

    public static void downloadVideo(Context context, String videoUrl, String title, DownloadListener listener) {

        File newFolder = new File(
                Environment.DIRECTORY_MOVIES
                        + "/" + context.getResources().getString(R.string.MainFolderName)
                        + "/Videos");

        // Check if the directory already exists
        if (!newFolder.exists()) {
            newFolder.mkdirs();
            newFolder.mkdir();
        }

        Uri uri = Uri.parse(videoUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(title);
        request.setDescription("Downloading video");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_MOVIES,
                context.getResources().getString(R.string.MainFolderName)
                        + "/Videos/" + title + ".mp4");

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            downloadId = manager.enqueue(request);

            // Register a BroadcastReceiver to receive download updates
            context.registerReceiver(new DownloadReceiver(manager, listener), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    private static class DownloadReceiver extends BroadcastReceiver {

        private final DownloadManager manager;
        private final DownloadListener listener;

        DownloadReceiver(DownloadManager manager, DownloadListener listener) {
            this.manager = manager;
            this.listener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == downloadId) {
                if (listener != null) {
                    listener.onComplete();
                }

                // Unregister the BroadcastReceiver
                context.unregisterReceiver(this);
            }
        }
    }

    public static void checkProgress(Context context, DownloadListener listener) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            try (Cursor cursor = manager.query(query)) {
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_RUNNING) {
                        long totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        long downloadedSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                        int progress = (int) ((downloadedSize * 100) / totalSize);

                        if (listener != null) {
                            listener.onProgress(progress);
                        }
                    }
                }
            }
        }
    }
}

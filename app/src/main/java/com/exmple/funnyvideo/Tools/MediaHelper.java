package com.exmple.funnyvideo.Tools;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;
import java.io.IOException;

public class MediaHelper {

    public static void getVideoMets(Context context, String str, VideoMetadata videoMetadata) {
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, Uri.parse(str));
            String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
            String extractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
            String extractMetadata3 = mediaMetadataRetriever.extractMetadata(19);
            videoMetadata.setDurationMs(!TextUtils.isEmpty(extractMetadata) ? Long.parseLong(extractMetadata) : getDuration(context, str));
            videoMetadata.setWidth(!TextUtils.isEmpty(extractMetadata2) ? Integer.parseInt(extractMetadata2) : 0);
            videoMetadata.setHeight(!TextUtils.isEmpty(extractMetadata3) ? Integer.parseInt(extractMetadata3) : 0);
            mediaMetadataRetriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e2) {
            Toast.makeText(context, e2.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static long getDuration(Context context, String str) {
        if (str.startsWith("content://")) {
            return getContentDuration(context, str);
        }
        return getFileDuration(context, str);
    }

    private static long getContentDuration(Context context, String str) {
        Cursor query = MediaStore.Video.query(context.getContentResolver(), Uri.parse(str), new String[]{"duration"});
        long j = query.moveToFirst() ? query.getLong(query.getColumnIndex("duration")) : 0L;
        query.close();
        return j;
    }

    private static long getFileDuration(Context context, String str) {
        Cursor query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"duration"}, "_data=?", new String[]{str}, null);
        if (query == null || !query.moveToFirst()) {
            return 0L;
        }
        long j = query.getLong(query.getColumnIndex("duration"));
        query.close();
        return j;
    }
}

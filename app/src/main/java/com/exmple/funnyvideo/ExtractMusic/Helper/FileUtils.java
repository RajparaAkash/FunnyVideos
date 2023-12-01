package com.exmple.funnyvideo.ExtractMusic.Helper;

import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;

import com.exmple.funnyvideo.R;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {

    public static void delete(final Context context, final ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, String str) throws Exception {
        MediaScannerConnection.scanFile(context, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str2, Uri uri) {
                try {
                    FileUtils.delete(context, activityResultLauncher, uri);
                } catch (Exception e) {

                }
            }
        });
    }

    public static void delete(Context context, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, Uri uri) throws Exception {
        ContentResolver contentResolver = context.getContentResolver();
        PendingIntent pendingIntent = null;
        try {
            contentResolver.delete(uri, null, null);
            Toast.makeText(context, (int) R.string.str_62, Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            if (Build.VERSION.SDK_INT >= 30) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(uri);
                pendingIntent = MediaStore.createDeleteRequest(contentResolver, arrayList);
            } else if (Build.VERSION.SDK_INT >= 29 && (e instanceof RecoverableSecurityException)) {
                pendingIntent = ((RecoverableSecurityException) e).getUserAction().getActionIntent();
            }
            if (pendingIntent != null) {
                activityResultLauncher.launch(new IntentSenderRequest.Builder(pendingIntent.getIntentSender()).build());
            }
        }
    }

    public static void createDir(String str) {
        File file = new File(str);
        if (!file.exists() && !file.isDirectory()) {
            if (file.mkdirs()) {
                Log.i("CreateDir", "App dir created");
                return;
            } else {
                Log.w("CreateDir", "Unable to create app dir!");
                return;
            }
        }
        Log.i("CreateDir", "App dir already exists");
    }
}

package soham.local.coursera.capstone.mooc;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Environment;

import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;


public class QRDownloaderService extends Service {
    public static final String EXTRA_FILE_URL = "soham.local.coursera.capstone.mooc.extra.FILE_URL";

    /*
        Helper class to start this Service
        param 1 : context of the calling site
        param 2 : Download url
     */
    public static void startDownload(Context context, String fileUrl) {
        Intent intent = new Intent(context, QRDownloaderService.class);
        intent.putExtra(EXTRA_FILE_URL, fileUrl);
        context.startService(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String fileUrl = intent.getStringExtra(EXTRA_FILE_URL);
        handleDownloadFile(fileUrl);
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
        Download the file from url with DownloadMangaer provided by the Android System.
     */
    private void handleDownloadFile(String fileUrl) {
        try {
            String timestamp = DateFormat.format("yyyyMMdd_HHmmss", new Date()).toString();
            String fileName = "QR_" + timestamp + ".png";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle("Downloading " + fileName);
            request.setMimeType("image/png");

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request); // this by default takes place on a separate thread
            } else
                return;
        } catch (Exception e) {
            return;
        }
    }

}
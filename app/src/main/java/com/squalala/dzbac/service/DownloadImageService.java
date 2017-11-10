package com.squalala.dzbac.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.squalala.dzbac.R;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import hugo.weaving.DebugLog;

/**
 * Created by Back Packer
 * Date : 24/11/15
 */
public class DownloadImageService extends Service {

    private ArrayList<String> urlImages;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    private int idNotification, counter = 0;

    private String path;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        urlImages = intent.getStringArrayListExtra("url_images_download");
        path = intent.getStringExtra("path");

        MainPreferences mainPreferences = new MainPreferences(this);

        idNotification = mainPreferences.getLastIdNotification();

        mNotifyManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(R.string.album))
                .setTicker(getString(R.string.telechargement_album_en_cours))
                .setContentText(getString(R.string.telechargement_album_en_cours))
                .setSmallIcon(R.drawable.ic_launcher);

        mNotifyManager.notify(idNotification, mBuilder.build());

        Picasso.with(this)
                .load(urlImages.get(0))
                .into(target);

        return Service.START_NOT_STICKY;
    }

    private Target target = new Target() {

        @Override
        public void onPrepareLoad(Drawable arg0) {
        }

        @DebugLog
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom arg1) {


            new Thread(new Runnable() {
                @Override
                public void run() {

                    Date date = new Date();

                    /*
                     * Pour vérifier que le dossier "DzBac" existe bien
                     */
                    File f = new File(Environment.getExternalStorageDirectory()+ "/DzBac/" + path);

                    if(!f.exists()){
                        f.mkdirs();
                    }

                    File file = new File(Environment.getExternalStorageDirectory() +
                            "/DzBac/" + path + "/" + date.getTime() + ".jpg");
                    try
                    {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.close();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri contentUri = Uri.fromFile(file);
                            mediaScanIntent.setData(contentUri);
                            sendBroadcast(mediaScanIntent);
                        } else {
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + "DzBac")));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).start();

            counter++;

            if (counter <= urlImages.size() - 1) {
                Picasso.with(DownloadImageService.this)
                        .load(urlImages.get(counter))
                        .into(target);
            }
            else { // Téléchargement terminé
                mBuilder.setAutoCancel(true);
                mBuilder.setContentTitle(getString(R.string.album));
                mBuilder.setContentText(getString(R.string.telechargement_album_termine));
                mBuilder.setTicker(getString(R.string.telechargement_album_termine));

                PendingIntent contentIntent = PendingIntent.getService(DownloadImageService.this, 1,
                        new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);

                mBuilder.setContentIntent(contentIntent);

                mNotifyManager.notify(idNotification, mBuilder.build());

                stopSelf();
            }

           // Crouton.makeText(ViewPagerActivity.this, getString(R.string.save_image_load_success),
           //       Style.CONFIRM).show();
        }

        @DebugLog
        @Override
        public void onBitmapFailed(Drawable arg0) {
            stopSelf();
          //  Crouton.makeText(ViewPagerActivity.this, getString(R.string.save_image_load_fail),
            //        Style.ALERT).show();
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

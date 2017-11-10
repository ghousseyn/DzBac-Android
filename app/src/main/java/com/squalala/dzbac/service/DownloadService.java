package com.squalala.dzbac.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;

import com.squalala.dzbac.R;
import com.squalala.dzbac.eventbus.events.DownloadEvent;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Back Packer
 * Date : 23/04/15
 */
public class DownloadService extends Service {

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    private String filename;
    private String urlDwonload;

    private String path;

    private int idNotification, idPending;

    private final int MAX_RETRY = 5;
    private int numberTry;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        filename = intent.getStringExtra("filename");
        urlDwonload = intent.getStringExtra("url");
        path = intent.getStringExtra("path");

        MainPreferences mainPreferences = new MainPreferences(this);

        idNotification = mainPreferences.getLastIdNotification();
        idPending = mainPreferences.getLastIdPendingIntent();

        String message = "Téléchargement en cours...";

        mNotifyManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(filename)
                .setTicker(message)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher);


        numberTry = 0;
        new AsyncDownloader().execute();

        return Service.START_NOT_STICKY;
    }

    private class AsyncDownloader extends AsyncTask<Void, Long, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            OkHttpClient httpClient = new OkHttpClient();
            Call call = httpClient.newCall(new Request.Builder().url(urlDwonload).get().build());

            try {
                Response response = call.execute();

                final File dir = new File(Environment.getExternalStorageDirectory() +
                        "/DzBac/" + path);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                final File file = new File(Environment.getExternalStorageDirectory() +
                        "/DzBac/" + path, filename);

                final OutputStream output = new FileOutputStream(file);

                if (response.code() == 200) {
                    InputStream inputStream = null;
                    try {
                        inputStream = response.body().byteStream();
                        byte[] buff = new byte[1024 * 4];
                        long downloaded = 0;
                        long target = response.body().contentLength();

                        publishProgress(0L, target);
                        while (true) {
                            int readed = inputStream.read(buff);

                            if(readed == -1){
                                break;
                            }

                            output.write(buff, 0, readed);

                            //write buff
                            downloaded += readed;
                            publishProgress(downloaded, target);
                            if (isCancelled()) {
                                return false;
                            }
                        }

                        output.flush();

                        return downloaded == target;
                    } catch (IOException ignore) {
                        return false;
                    }
                    finally {

                        if (output != null)
                            output.close();

                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Long... values) {

            mBuilder.setProgress(values[1].intValue(), values[0].intValue(), false);
            // Displays the progress bar for the first time.
            mNotifyManager.notify(idNotification, mBuilder.build());

            //    textViewProgress.setText(String.format("%d / %d", values[0], values[1]));
        }

        @Override
        protected void onPostExecute(Boolean result) {

            String response = null;

            if (result) {
                response = "Téléchargement terminé, le fichier est dans le dossier DzBac";
            }
            else {

                if (numberTry++ >= MAX_RETRY)
                    response = "Erreur, Fichier non télécharger";
                else {
                    new AsyncDownloader().execute();
                    return;
                }

            }

            mBuilder.setContentText(response)
                    .setProgress(0, 0, false);

            File file = new File(Environment.getExternalStorageDirectory() +
                    "/DzBac/" + path, filename);

            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            String mimeType = myMime.getMimeTypeFromExtension(FileUtils.fileExt(filename).substring(1));
            newIntent.setDataAndType(Uri.fromFile(file),mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this, idPending,
                    newIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            mBuilder.setAutoCancel(true);
            mBuilder.setTicker(response);
            mBuilder.setContentIntent(contentIntent);

            mNotifyManager.notify(idNotification, mBuilder.build());

            EventBus.getDefault().post(new DownloadEvent(true));

            stopSelf();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

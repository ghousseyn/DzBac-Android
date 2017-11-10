package com.squalala.dzbac.utils;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.squalala.dzbac.DjihtiConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : TypeFileMaker.java
 * Date : 5 août 2014
 *
 */
public class UploadHelper {

    private static final String TAG = UploadHelper.class.getSimpleName();
    private static final int IMAGE_MAX_WIDTH = 1900;

    /**
     *  Taille maximale que ne doit pas dépasser une image à uploader ( 300 KO )
     */
    private static final int IMAGE_MAX_SIZE = 307200;


    public static RequestBody getTypedFile(Context context , String path) {
        Log.e(TAG, "getTypedFile");

        long startTime = System.currentTimeMillis();

        String exifOrientation = null;

        ExifInterface oldExif = null;
        try {
            oldExif = new ExifInterface(path);
            exifOrientation = oldExif.getAttribute(ExifInterface.TAG_ORIENTATION);
        } catch (IOException e) {
            e.printStackTrace();
        }


        File file = new File(path);

        System.out.println("=========================== BEFORE ===========================");
        System.out.println("Name  : " + file.getName());
        System.out.println("Path  : " + file.getAbsolutePath());
        System.out.println("Size : " + humanReadableByteCount(file.length(), true));
        System.out.println("=========================== END ===========================");

        // On vérifie que ce n'est pas un gif
        if (!file.getName().endsWith(".gif"))
            file = decodeFile(file, file.getName(), 70);


        if (exifOrientation != null) {
            try {
                ExifInterface newExif = new ExifInterface(file.getAbsolutePath());
                newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation);
                newExif.saveAttributes();

                System.out.println("test " +exifOrientation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        try {
            System.out.println(humanReadableByteCount(Long.valueOf(file.length()), true));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("That took resize " + (endTime - startTime) + " milliseconds " + file.getName());
        //     System.out.println("That took " + ((endTime - startTime) / 1000) + " seconds");

        System.out.println("=========================== AFTER ===========================");
        System.out.println("Name  : " + file.getName());
        System.out.println("Path  : " + file.getAbsolutePath());
        System.out.println("Size : " + humanReadableByteCount(file.length(), true));
        System.out.println("=========================== END ===========================");

        return body;
    }


    public static RequestBody getTypedFileWithId(Context context , String path, String id) {
        Log.e(TAG, "getTypedFile");

        long startTime = System.currentTimeMillis();

        File file = new File(path);

        System.out.println("=========================== BEFORE ===========================");
        System.out.println("Name  : " + file.getName());
        System.out.println("Path  : " + file.getAbsolutePath());
        System.out.println("Size : " + humanReadableByteCount(file.length(), true));
        System.out.println("=========================== END ===========================");

        // On vérifie que ce n'est pas un gif
        if (!file.getName().endsWith(".gif"))
            file = decodeFile(file, file.getName(), 70);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", id)
                .addFormDataPart("files", file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        try {
            System.out.println(humanReadableByteCount(Long.valueOf(file.length()), true));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        long endTime = System.currentTimeMillis();

        System.out.println("That took resize " + (endTime - startTime) + " milliseconds " + file.getName());

        System.out.println("=========================== AFTER ===========================");
        System.out.println("Name  : " + file.getName());
        System.out.println("Path  : " + file.getAbsolutePath());
        System.out.println("Size : " + humanReadableByteCount(file.length(), true));
        System.out.println("=========================== END ===========================");

        return body;
    }



    private static File decodeFile(File f, String name, int quality)  {
        Log.e(TAG, "decodeFile");

        Bitmap bitmap = null;
        
        if (f.length() <= IMAGE_MAX_SIZE)
            return f;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        File file = null;

        try {

            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            //   fis.close();

            System.out.println("============= BREFORE =============");
            System.out.println("Height : " + o.outHeight);
            System.out.println("Width : " + o.outWidth);
            System.out.println("===================================");

            //Find the correct scale value. It should be the power of 2.
            int scale=1;

           if (o.outHeight > IMAGE_MAX_WIDTH || o.outWidth > IMAGE_MAX_WIDTH) {
                scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_WIDTH /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inScaled = false;
            o2.inDither = false;
            o2.inPreferredConfig = Bitmap.Config.ARGB_8888;

            fis = new FileInputStream(f);
            bitmap = BitmapFactory.decodeStream(fis, null, o2);

            System.out.println("============= AFTER =============");
            System.out.println(bitmap == null);
            System.out.println("Height : " + bitmap.getHeight());
            System.out.println("Width : " + bitmap.getWidth());
            System.out.println("===================================");
            //    fis.close();

            // Store to tmp file

            File mFolder = new File(DjihtiConstant.PATH_TEMP_FILE);
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            file = new File(mFolder.getAbsolutePath(), name);

            FileOutputStream fos = null;

            try {

                fos = new FileOutputStream(file);

                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);

                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            bitmap.recycle();

        } catch (Throwable e) {
        }
        finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }



    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String getPathFromIntent(Context context ,Intent data) {

        String url = data.getData().toString();
        Bitmap bitmap = null;
        InputStream is = null;

        if (url.startsWith("content://com.sec.android.")){
            try {
                is = context.getContentResolver().openInputStream(Uri.parse(url));
                bitmap = BitmapFactory.decodeStream(is);

                File f = new File(Environment.getExternalStorageDirectory()+"/inpaint/");
                if(!f.exists()){
                    f.mkdirs();
                }
                OutputStream outStream = null;

                File file = new File(Environment.getExternalStorageDirectory() + "/inpaint/temp.png");

                // On le supprime au cas ou il existe
                if (file.exists() && !file.isDirectory()) {
                    file.delete();

                    try {
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                                Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
                    } catch (SecurityException e) {
                        e.printStackTrace();

                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
                    }

                }

                outStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();

                //     Log.e("PATH 1", file.getAbsolutePath());

                return file.getAbsolutePath();

            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        else {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Log.e("PATH 2", picturePath);
            return picturePath;
        }

        return null;

    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;
    }




}

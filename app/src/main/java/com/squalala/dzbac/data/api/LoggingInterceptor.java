package com.squalala.dzbac.data.api;

import android.util.Log;

import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.utils.HmacSha1Signature;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by brio on 12/02/15.
 */
public class LoggingInterceptor implements Interceptor {

    private static final int LIMIT_NUMBER_RETRY = 5;

    private static final String TAG = "Retrofit" ; //LoggingInterceptor.class.getSimpleName();

    private MainPreferences mainPreferences;

    public LoggingInterceptor(MainPreferences mainPreferences) {
        this.mainPreferences = mainPreferences;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String timestamp = String.valueOf((System.currentTimeMillis() / 1000L));
        String url = request.url().toString();

        String data = url + timestamp + mainPreferences.getApiKey() + mainPreferences.getIdUser();

        String hash = null;

    //    Log.e(TAG, "data : " + data);

      /*  try {
            hash = HmacSha1Signature.calculateRFC2104HMAC(data, mainPreferences.getApiKey());
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }*/


    //    Log.e(TAG, request.toString());
      //  long t1 = System.nanoTime();
     //   Log.e(TAG, String.format("Sending request %s on %s%n%s",
     //           request.url(), chain.connection(), request.headers()));

        Log.e(TAG, "url path : " + request.url().toString());
        Log.e(TAG, "timestamp" + timestamp);
        Log.e(TAG, "user-id" + mainPreferences.getIdUser());
        Log.e(TAG, "api_key" + mainPreferences.getApiKey());
        Log.e(TAG, "data : " + data);
        Log.e(TAG, "hash" + hash);

        Request requestModified = request.newBuilder()
                .header("x-timestamp", timestamp)
                .header("x-user-id", mainPreferences.getIdUser())
                .header("x-sig", ' '+hash)
                .header("Accept-Language", Locale.getDefault().getLanguage())
                .build();

        Log.d("intercept", "Launch requestModified");
        //try the request
        Response response = null;


        boolean responseOK = false;
        int tryCount = 0;

        while (!responseOK && tryCount < LIMIT_NUMBER_RETRY) {
            try {
                response = chain.proceed(requestModified);
                responseOK = response.isSuccessful();
            }catch (Exception e){
                Log.d("intercept", "Request is not successful - " + tryCount);
            }finally{
                tryCount++;
            }
        }

        if (tryCount >= LIMIT_NUMBER_RETRY) {
            response = chain.proceed(requestModified);
        }


    /*    System.out.println("Response state : " + response.isSuccessful());
        long t2 = System.nanoTime();
        System.out.println(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));


        Log.e(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));*/

        return response;
    }


}
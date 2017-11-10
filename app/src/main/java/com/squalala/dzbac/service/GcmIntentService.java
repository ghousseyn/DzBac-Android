package com.squalala.dzbac.service;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squalala.dzbac.R;
import com.squalala.dzbac.broadcastreceiver.GcmBroadcastReceiver;
import com.squalala.dzbac.data.api.ApiResponse;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.eventbus.events.BannEvent;
import com.squalala.dzbac.ui.comment.CommentaryActivity;
import com.squalala.dzbac.ui.show_conversation.ShowConversationActivity;
import com.squalala.dzbac.ui.show_item.ShowAnnonceActivity;
import com.squalala.dzbac.ui.show_profile.ShowProfileActivity;
import com.squalala.dzbac.utils.StringUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class GcmIntentService extends IntentService {

	MainPreferences mainPreferences;


	public GcmIntentService() {
		super(TAG);
	}

	private static Bitmap bitmap;
	/*private static String title;
	private static String message;
	private static String idNotification;*/

	private static int icon;

	/**
	 *  Pour faire découvrir une application
	 */
	private static final String CASE_TYPE_MARKET = "market";

	/**
	 *  Pour demander aux utilisateurs de noter l'application
	 */
	private static final String CASE_TYPE_MARKET_NOTE = "market_note";

	/**
	 *  Pour inciter l'utilisateur à aimer la page facebook
	 */
	private static final String CASE_TYPE_FACEBOOK = "facebook";

	/**
	 *  Quand on reçoit un message
	 */
	private static final String CASE_SEND_MESSAGE = "send_message";

	/**
	 *  Pour changer le level de l'utilisateur
	 */
	private static final String CASE_LEVEL_USER = "level_user";

	/**
	 *  Quand un membre est banni
	 */
	private static final String CASE_USER_BANNED = "user_banned";

	/**
	 *  Quand on reçoit une notification
	 */
	private static final String CASE_NOTIFICATION = "notification";

	/**
	 *  Quand on reçoit une notification journalière
	 */
	private static final String CASE_TOP_POST = "top_post";

	/**
	 *  Quand une personne s'abonne à nous
	 */
	private static final String CASE_FOLLOWING = "following";

	/**
	 *  Pour prévenir l'utilisateur d'un nouveau post de celui qu'il suit
	 */
	private static final String CASE_NEW_POST = "new_post";


	private static final String TAG = "GCMIntentService";

	/**
	 *  Pour montrer un simple petit message
	 */
//	private static final String CASE_TYPE_MESSAGE = "message";

    @SuppressWarnings("deprecation")
	public boolean generateNotification(final Context context, Intent intentFromServer,
			boolean isVibrate_) {

    	Log.e(TAG, "generateNotification" + intentFromServer.getExtras().toString());


    	String message = intentFromServer.getExtras().getString("message");
    	String title = intentFromServer.getExtras().getString("title");
		String level = intentFromServer.getExtras().getString("level");
    	String idContent = intentFromServer.getExtras().getString("id_content");
    	String idReceveur = intentFromServer.getExtras().getString("id_receveur");
    	String idHeader = intentFromServer.getExtras().getString("id_header");
		String linkCall = intentFromServer.getExtras().getString("link_call");
		String username = intentFromServer.getExtras().getString("username");

        String typeNotification = intentFromServer.getExtras().getString("type_notification");
        final String urlAvatar = intentFromServer.getExtras().getString("url_avatar");



        Intent notificationIntent = null;

		if (typeNotification == null)
			return false;

        if (typeNotification.equals(CASE_TYPE_MARKET)) {
        	/*
        	 * icon = R.drawable.;
        	
        	notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        	notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        	*/
        }
        else if (typeNotification.equals(CASE_TYPE_MARKET_NOTE)) {

        	icon = R.drawable.ic_stat_bacdz;

        	String packageApp = context.getPackageName();

        	notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageApp));
        	notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        	sendNotification(context, notificationIntent, title, message);
        }
		else if (typeNotification.equals(CASE_USER_BANNED)) {

			icon = R.drawable.ic_stat_bacdz;

			title = getString(R.string.app_name);

			String packageApp = context.getPackageName();

			mainPreferences.setLevelUser(0);
			mainPreferences.setBanned(true);

			notificationIntent = new Intent();
			notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			sendNotification(context, notificationIntent, title, message);

			EventBus.getDefault().post(new BannEvent());
		}
		else if (typeNotification.equals(CASE_LEVEL_USER)) {

			mainPreferences.setLevelUser(Integer.valueOf(level));

			icon = R.drawable.ic_stat_bacdz;

			title = getString(R.string.app_name);

			notificationIntent = new Intent();
			notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			sendNotification(context, notificationIntent, title, message);

			//return true;
		}
        else if (typeNotification.equals(CASE_TYPE_FACEBOOK)) {

        	//icon = R.drawable.facebook;
        	icon = R.drawable.ic_stat_bacdz;

        	final String urlFb = "fb://page/"+"1531670483761285";

        	notificationIntent = new Intent(Intent.ACTION_VIEW);
        	notificationIntent.setData(Uri.parse(urlFb));

     	    // If Facebook application is installed, use that else launch a browser
     	    final PackageManager packageManager = context.getPackageManager();
     	    List<ResolveInfo> list =
     	        packageManager.queryIntentActivities(notificationIntent,
     	        PackageManager.MATCH_DEFAULT_ONLY);
     	    if (list.size() == 0) {
     	        final String urlBrowser = "https://www.facebook.com/dzbac.app.mobile";
     	       notificationIntent.setData(Uri.parse(urlBrowser));
     	    }

     	   sendNotification(context, notificationIntent, title, message);
        }
         // Cela veut dire que c'est une demande d'ajout
         // Et on doit envoyer la personne sur RequestContactActivity
        else if (typeNotification.equals(CASE_NOTIFICATION)) {

		/*	System.out.println("HERE NOTIF");

			System.out.println("DEBUG " + (mainPreferences.getCurrentCommentaireId() != null) +" "+
					mainPreferences.getCurrentCommentaireId().equals(idContent)); */

			if (mainPreferences.getCurrentCommentaireId() != null &&
					mainPreferences.getCurrentCommentaireId().equals(idContent))
			{
				String urlAudio = intentFromServer.getExtras().getString("audio");
				String dateCreation =  intentFromServer.getExtras().getString("date_creation");

                String urlStr = intentFromServer.getExtras().getString("url");
                String urls [] = null;

                if (urlStr != null && !urlStr.isEmpty())
                    urls = urlStr.replaceAll("\"", "")
                            .replaceAll("\\[", "")
                            .replaceAll("\\]", "")
                            .split(",");

                List<String> url;

                if (urls != null && urls[0].contains("http"))
				    url =  Arrays.asList(urls);
                else
                    url = new ArrayList<>();

                System.out.println(intentFromServer.getExtras().getString("url"));
                System.out.println(urls);
                System.out.println(url);
                System.out.println(url.size());

                String urlAvatarNotifieur =  intentFromServer.getExtras().getString("url_avatar_notifieur");
				String idMembre =  intentFromServer.getExtras().getString("id_membre");
                String idComment = intentFromServer.getExtras().getString("id");

				ApiResponse.Commentaire commentaire = new ApiResponse.Commentaire();
				commentaire.urlAudio = urlAudio;
				commentaire.pseudo = username;
				commentaire.commentaire = message;
				commentaire.url_images = url;
				commentaire.url_avatar = urlAvatarNotifieur;
				commentaire.id_membre = idMembre;
				commentaire.date_creation = dateCreation;
                commentaire.id = idComment;
                commentaire.level = Integer.valueOf(level);

				EventBus.getDefault().post(commentaire);

				return false;
			}

			/*
			 'title' => $data_notification,
                  'id_content' => $this->obfuscate_id->id_encode($id_item),
                  'message' => $message,
                  'url_avatar' => $url_avatar,
                  'type_notification' => 'notification',
                  'id_membre' => $data['id_membre'],
                  'url_avatar_notifieur' => $data['url_avatar_notifieur'],
                  'date_creation' => $data['date_creation'],
                  'audio' => $data['audio'],
                  'url' => $data['url']


				String message = intentFromServer.getExtras().getString("message");
    	String title = intentFromServer.getExtras().getString("title");
		String level = intentFromServer.getExtras().getString("level");
    	String idContent = intentFromServer.getExtras().getString("id_content");
    	String idReceveur = intentFromServer.getExtras().getString("id_receveur");
    	String idHeader = intentFromServer.getExtras().getString("id_header");
		String linkCall = intentFromServer.getExtras().getString("link_call");
		String username = intentFromServer.getExtras().getString("username");
			 */

        	notificationIntent = new Intent(context, CommentaryActivity.class);
        	notificationIntent.putExtra("is_notification", true);
        	notificationIntent.putExtra("id_item", idContent);

        	icon = R.drawable.ic_stat_bacdz;

        	final Intent mIntent = notificationIntent;
        	final String mTitle = title;
        	final String mMessage = message;

        	new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {

					Log.e(TAG, "doInBackground");
					bitmap = getBitmapFromURL(urlAvatar);

					return null;
				}

				protected void onPostExecute(Void result) {
					Log.e(TAG, "onPostExecute");
					sendNotification(context, mIntent, mTitle, mMessage);
				};

        	}.execute();
        }
		else if (typeNotification.equals(CASE_FOLLOWING)) {

			if (mainPreferences.isFollowing())
			{
				notificationIntent = new Intent(context, ShowProfileActivity.class);
				notificationIntent.putExtra("id_membre", idReceveur);

				icon = R.drawable.ic_stat_bacdz;

				final Intent mIntent = notificationIntent;
				final String mTitle = title;
				final String mMessage = message;

				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						Log.e(TAG, "doInBackground");
						bitmap = getBitmapFromURL(urlAvatar);

						return null;
					}

					protected void onPostExecute(Void result) {
						Log.e(TAG, "onPostExecute");
						sendNotification(context, mIntent, mTitle, mMessage);
					};

				}.execute();
			}

		}
		else if (typeNotification.equals(CASE_NEW_POST)) {

			notificationIntent = new Intent(context, ShowAnnonceActivity.class);
			notificationIntent.putExtra("is_notification", true);
			notificationIntent.putExtra("id_item", idContent);

			icon = R.drawable.ic_stat_bacdz;

			final Intent mIntent = notificationIntent;
			final String mTitle = title;
			final String mMessage = message;

			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {

					Log.e(TAG, "doInBackground");
					bitmap = getBitmapFromURL(urlAvatar);

					return null;
				}

				protected void onPostExecute(Void result) {
					Log.e(TAG, "onPostExecute");
					sendNotification(context, mIntent, mTitle, mMessage);
				};

			}.execute();

		}
		else if (typeNotification.equals(CASE_TOP_POST)) {

            if (!mainPreferences.isEnabledDailyNotification())
                return false;

			notificationIntent = new Intent(context, ShowAnnonceActivity.class);
			notificationIntent.putExtra("is_notification", true);
			notificationIntent.putExtra("type_notification", CASE_TOP_POST);
			notificationIntent.putExtra("id_item", idContent);

			icon = R.drawable.ic_stat_bacdz;

            Log.e(TAG, "title : " + title);

			sendNotification(context, notificationIntent, getString(R.string.top_post), title);
		}
        else if (typeNotification.equals(CASE_SEND_MESSAGE)) {

        	notificationIntent = new Intent(context, ShowConversationActivity.class);

        	notificationIntent.putExtra("id_membre_2", idReceveur);
        	notificationIntent.putExtra("id_header", idHeader);
			notificationIntent.putExtra("type_notification", CASE_SEND_MESSAGE);
        	notificationIntent.putExtra("sujet", title);

        	icon = R.drawable.ic_stat_bacdz;

        	final Intent mIntent = notificationIntent;
        	final String mTitle = title;
        	final String mMessage = message;

        	new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {

					Log.e(TAG, "doInBackground");
					bitmap = getBitmapFromURL(urlAvatar);

					return null;
				}

				protected void onPostExecute(Void result) {
					Log.e(TAG, "onPostExecute");
					sendNotification(context, mIntent, mTitle, mMessage);
				};

        	}.execute();
        }
        else {
        	icon = R.drawable.ic_stat_bacdz;
        	notificationIntent = new Intent();
        	sendNotification(context, notificationIntent, title, message);
        }

        return true;

    }

    private void sendNotification(Context context, Intent notificationIntent,
    		String title, String message) {

    	 long when = System.currentTimeMillis();

    	 NotificationManager mNotificationManager = (NotificationManager)
                 context.getSystemService(Context.NOTIFICATION_SERVICE);

         // set intent so it does not start a new activity
         notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				 Intent.FLAG_ACTIVITY_SINGLE_TOP);



         PendingIntent contentIntent = PendingIntent.getActivity(context, mainPreferences.getLastIdPendingIntent(),
				 notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder = null;

        int iconLarge = R.drawable.ic_launcher;


		if (message.contains("\\\\u") || title.contains("\\\\u"))
		{
			try {
				mBuilder =
						new NotificationCompat.Builder(context)
								.setSmallIcon(icon)
								.setLargeIcon(BitmapFactory.decodeResource(getResources(), iconLarge))
								.setContentTitle(StringUtils.toEmoji(title))
								.setAutoCancel(true)
								.setLights(Color.BLUE, 1000, 1000)
								.setTicker(StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava(message)))
								.setWhen(when)
								.setStyle(new NotificationCompat.BigTextStyle()
										.bigText(StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava(message))))
								.setContentText(StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava(message)));

			} catch ( NestableRuntimeException e) {

				mBuilder =
						new NotificationCompat.Builder(context)
								.setSmallIcon(icon)
								.setContentTitle(StringUtils.toEmoji(title))
								.setLargeIcon(BitmapFactory.decodeResource(getResources(), iconLarge))
								.setAutoCancel(true)
								.setLights(Color.BLUE, 1000, 1000)
								.setTicker(message)
								.setWhen(when)
								.setStyle(new NotificationCompat.BigTextStyle()
										.bigText(message))
								.setContentText(message);

			}

		}
		else
		{
			mBuilder =
					new NotificationCompat.Builder(context)
							.setSmallIcon(icon)
							.setLargeIcon(BitmapFactory.decodeResource(getResources(), iconLarge))
							.setContentTitle(title)
							.setAutoCancel(true)
							.setLights(Color.BLUE, 1000, 1000)
							.setTicker(message)
							.setWhen(when)
							.setStyle(new NotificationCompat.BigTextStyle()
									.bigText(message))
							.setContentText(message);
		}





         Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

         if (mainPreferences.isEnabledSound())
             mBuilder.setSound(uri);

         if (bitmap != null) {
        	 final Bitmap mBitmap = bitmap;
        	 mBuilder.setLargeIcon(mBitmap);
        	 bitmap = null;
         }

         Notification notification = mBuilder.build();

         if (mainPreferences.isEnabledVibrate())
        	mBuilder.setVibrate(new long[] { 1000, 1000, 1000});

        notification.defaults |= Notification.DEFAULT_SOUND;

    //     else
        //	 notification.defaults = 0;

         mBuilder.setContentIntent(contentIntent);

         /**
          *  ATTENTION L'ID DE LA NOTIFICATION PEUT CAUSER DES PROBLÈMES
          */
         mNotificationManager.notify(mainPreferences.getLastIdNotification(), mBuilder.build());
    }


	@Override
	protected void onHandleIntent(Intent intent) {

			Bundle extras = intent.getExtras();
	        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
	        // The getMessageType() intent parameter must be the intent you received
	        // in your BroadcastReceiver.
	        String messageType = gcm.getMessageType(intent);

	        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
	            /* 
	             * Filter messages based on message type. Since it is likely that GCM 
	             * will be extended in the future with new message types, just ignore 
	             * any message types you're not interested in, or that you don't 
	             * recognize. 
	             */
	            if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
	            //    sendNotification("Send error: " + extras.toString());
	            	Log.i(TAG, "Send error: " + extras.toString());
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_DELETED.equals(messageType)) {
	                        Log.i(TAG, "Deleted messages on server: " +
		                        extras.toString());
	            // If it's a regular GCM message, do some work.
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
	                // This loop represents the service doing some work.
	                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

	                // Post notification of received message.
	                checkMessage(intent);

	                Log.i(TAG, "Received: " + extras.toString());
	            }
	        }
	        // Release the wake lock provided by the WakefulBroadcastReceiver.
	        GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

	/**
	 * Pour savoir si l'on doit envoyer le message à l'activity
	 * ou générer une notification
	 * @param intent
	 */
	private void checkMessage(Intent intent) {

		mainPreferences = new MainPreferences(getApplicationContext());

		if (mainPreferences.getLevelContribution() < 2) {
			return;
		}

		if (mainPreferences.isConnected())
			generateNotification(this, intent, true);

	}

	public synchronized Bitmap getBitmapFromURL(String strURL) {
		try {
			URL url = new URL(strURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


}

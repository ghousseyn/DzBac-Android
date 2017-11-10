/**
 * 
 */
package com.squalala.dzbac;

import android.os.Environment;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : DjihtiConstant.java
 * Date : 22 juin 2014
 * 
 */
public class DjihtiConstant {

	public static final String CHAT_SERVER_URL = "http://www.squalala.xyz";


    public static final int CODE_ERROR_MULTIPLE_DEVICE = 9999;

    public static final int MAX_LENGTH_FILE = 10485760; // 10 Mo

    public static final int MAX_LENGTH_AUDIO = 10000000 * 5; // 50 Mo

	public static final int MAX_LENGTH_VIDEO = 1024 * 1024 * 8 * 10; // 80 Mo

	/**
	 *  Équivalent à 10
	 */
	public static final int LIMIT_NUMBER_SHOW_AD = 9;
	
	/**
	 * Pour avoir un effet de conversation instantanée ( 10 secondes ) 
	 */
	public final static int INTERVAL_CHECK = 1000 * 10;
	
	public static final String SENDER_ID = "950803615530";

	//public static final String HOME_URL = "http://128.199.46.85";
	//public static final String HOME_URL = "http://192.168.1.9";
	public static final String HOME_URL = "http://www.squalala.xyz";


	public static final String PASSWORD_FORGOT = "http://www.squalala.xyz/password/fpass.php";
    //public static final String HOME_URL = "http://178.62.57.167";

	public static final String APP_KEY_APPODEAL = "6a68d5010097d94c796a0b0d0d3a3a6eacc795cf398881fb";
	
	/**
	 *  1000 -> 1 seconde
	 */
	public final static int  SPLASH_TIME_OUT = 100;

	public final static int ADMIN_GRADUATION = 2;

	public final static int HEIGHT_ADMIN_GRADUATION = 4;

	public final static int MEDIUM_ADMIN_GRADUATION = 3;

	public final static int BANNED_GRADUATION = 0;

    public final static String PATH_IMAGES_POSTS = HOME_URL + "/dz_bac/images/posts/";

    public final static String PATH_IMAGES_COMMENTS = HOME_URL + "/dz_bac/images/commentaires/";


    public static final String PATH_TEMP_FILE = Environment.getExternalStorageDirectory().toString()
            + "/TMMFOLDER";
	
	/**
	 *  Paramètres par défault pour l'api quand la personne ne spécifie rien.
	 */
	public final static String API_URL = HOME_URL + "/dz_bac/index.php/apiv11/";

	public final static String URL_UPLOADS = HOME_URL + "/dz_bac/uploads/";

	public final static String URL_AUDIO = HOME_URL + "/dz_bac/audio/";

    public final static String URL_DEFAULT_AVATAR = HOME_URL + "/dz_bac/images/path3929eerer.png";

    public final static String URL_DEFAULT_BACKGROUND = HOME_URL + "/dz_bac/images/path3929eerereeeeeeeeeee.png";
	
	public final static String NO_IMAGE_DJIHTI_URL = HOME_URL + "/assests/images/djihti_no_image.png";
	
	/**
	 *  REQUEST CODES pour les sub-activity de filtres
	 */
	public final static int REQUEST_CODE_CONVERSATION_ACTIVITY = 6;
	public final static int REQUEST_CODE_NOTIFICATION_ACTIVITY = 20;
	public final static int REQUEST_CODE_PROFILE_ACTIVITY = 7;
	public final static int REQUEST_CODE_LOAD_AVATAR = 10;
	public final static int REQUEST_CODE_LOAD_BACKGROUND = 11;


	public final static String PATH_LOGO = "M 175.00,25.09\n" +
            "           C 175.00,25.09 205.00,22.96 205.00,22.96\n" +
            "             209.28,22.76 215.88,21.27 220.00,22.31\n" +
            "             224.05,23.34 229.85,30.73 232.96,34.00\n" +
            "             232.96,34.00 249.17,51.69 249.17,51.69\n" +
            "             252.95,54.84 256.16,53.62 257.89,56.43\n" +
            "             257.89,56.43 261.00,67.00 261.00,67.00\n" +
            "             261.00,67.00 256.39,68.02 256.39,68.02\n" +
            "             256.39,68.02 247.00,87.00 247.00,87.00\n" +
            "             247.00,87.00 251.00,88.00 251.00,88.00\n" +
            "             251.00,88.00 249.00,94.00 249.00,94.00\n" +
            "             249.00,94.00 239.00,91.00 239.00,91.00\n" +
            "             239.00,91.00 245.60,82.06 245.60,82.06\n" +
            "             245.60,82.06 252.00,67.00 252.00,67.00\n" +
            "             252.00,67.00 241.00,68.00 241.00,68.00\n" +
            "             241.13,60.51 234.65,57.30 229.00,53.15\n" +
            "             216.62,44.08 193.10,34.62 178.00,41.00\n" +
            "             178.00,41.00 164.00,26.00 164.00,26.00\n" +
            "             164.00,26.00 175.00,25.09 175.00,25.09 Z\n" +
            "           M 183.39,41.57\n" +
            "           C 184.64,40.99 186.64,41.03 188.00,41.00\n" +
            "             202.62,40.76 215.01,44.60 227.00,53.04\n" +
            "             229.63,54.89 236.50,60.11 237.35,63.18\n" +
            "             238.31,66.68 234.64,73.84 233.00,77.00\n" +
            "             233.00,77.00 212.00,63.12 212.00,63.12\n" +
            "             201.83,57.76 188.44,55.01 177.00,55.00\n" +
            "             177.79,51.63 180.15,43.10 183.39,41.57 Z\n" +
            "           M 167.00,62.92\n" +
            "           C 167.00,62.92 177.00,62.92 177.00,62.92\n" +
            "             188.10,63.28 203.39,67.31 213.00,72.87\n" +
            "             239.89,88.44 245.91,128.25 228.22,153.00\n" +
            "             222.30,161.28 216.92,164.66 208.00,169.00\n" +
            "             214.88,169.62 219.16,171.66 225.00,175.20\n" +
            "             256.96,194.61 260.31,248.70 230.00,271.24\n" +
            "             218.19,280.02 198.69,285.82 184.00,286.09\n" +
            "             184.00,286.09 133.00,286.09 133.00,286.09\n" +
            "             133.00,286.09 109.00,286.09 109.00,286.09\n" +
            "             109.00,286.09 93.00,287.00 93.00,287.00\n" +
            "             93.00,287.00 92.00,262.00 92.00,262.00\n" +
            "             92.00,262.00 92.00,216.00 92.00,216.00\n" +
            "             92.00,216.00 92.00,62.92 92.00,62.92\n" +
            "             92.00,62.92 167.00,62.92 167.00,62.92 Z\n" +
            "           M 170.00,163.00\n" +
            "           C 179.85,162.98 194.50,160.70 203.00,155.65\n" +
            "             226.13,141.91 226.12,101.00 203.00,87.35\n" +
            "             189.82,79.57 175.65,80.00 161.00,80.00\n" +
            "             161.00,80.00 110.00,80.00 110.00,80.00\n" +
            "             110.00,80.00 110.00,163.00 110.00,163.00\n" +
            "             110.00,163.00 170.00,163.00 170.00,163.00 Z\n" +
            "           M 184.00,269.00\n" +
            "           C 191.54,268.99 203.21,266.89 209.72,263.02\n" +
            "             212.00,261.66 215.84,258.24 218.00,256.41\n" +
            "             221.33,253.59 224.71,250.66 227.15,247.00\n" +
            "             232.03,239.68 232.01,230.44 232.00,222.00\n" +
            "             231.98,208.93 228.75,198.82 218.00,190.53\n" +
            "             206.71,181.82 192.58,180.84 179.00,179.91\n" +
            "             179.00,179.91 167.00,179.00 167.00,179.00\n" +
            "             167.00,179.00 110.00,179.00 110.00,179.00\n" +
            "             110.00,179.00 110.00,269.00 110.00,269.00\n" +
            "             110.00,269.00 184.00,269.00 184.00,269.00 Z";


}

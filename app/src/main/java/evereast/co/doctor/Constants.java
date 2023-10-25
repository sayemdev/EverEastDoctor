package evereast.co.doctor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Health Men created by Sayem Hossen Saimon on 12/10/2020 at 4:19 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class Constants {
    public static final String API_KEY="v1Zd99dG$Jcz7%!N4H!v$iVL";
    public static final String PROFILE_PREFERENCE = "PROFILE_PREFERENCE";
    public static final String AIR_PREFERENCE_PP = "AIR_PREFERENCE_PP";
    public static final String AIR_PREFERENCE_CC = "AIR_PREFERENCE_CC";
    public static final String AIR_PREFERENCE_HO = "AIR_PREFERENCE_HO";
    public static final String AIR_PREFERENCE_IX = "AIR_PREFERENCE_IX";
    public static final String AIR_PREFERENCE_DX = "AIR_PREFERENCE_DX";
    public static final String AIR_PREFERENCE_RX = "AIR_PREFERENCE_RX";
    public static final String AIR_PREFERENCE_AD = "AIR_PREFERENCE_AD";
    public static final String AIR_PREFERENCE_FU = "AIR_PREFERENCE_FU";
    public static final String AIR_PREFERENCE_LIST = "SELECTED_LIST";
    public static final String WP_URL = "https://www.projectdemo.xyz/telemedicine/wp-json/wp/v2/";
    public static final String LANGUAGE_PREFERENCES = "LANGUAGEPREFERNCE";
    public static final String BASE_URL = "https://projectdemo.xyz/telemedicine/android/Doctor/";
    public static final String SUGGESTION_XLS = "https://projectdemo.xyz/telemedicine/android/Doctor/suggestions/financial_sample.xls";
    public static final String PROFILE_FILES = "https://projectdemo.xyz/telemedicine/android/Patient/Profile/";
    public static final String PATIENT_URL = "https://projectdemo.xyz/telemedicine/android/Patient/";
    public static final String MAIN_URL = "https://projectdemo.xyz/telemedicine/android/";
    public static final String DOMAIN_URL = "https://projectdemo.xyz/telemedicine/";
    public static final String FILE_PATH = PATIENT_URL + "Files/";
    public static final String USER_DATA_PREF = "USER_DATA_PREF";
    public static final String USER_INFO = "USER_INFO";
    public static final String USER_ID = "USER_ID";
    public static final String SIGNATURE = "SIGNATURE";
    public static final String MY_TOKEN = "MY_TOKEN";
    public static final String USER_NAME = "USER_NAME";
    public static final String DEGREE = "DEGREE";
    public static final String H_DEGREE = "H_DEGREE";
    public static final String DESIGNATION = "DESIGNATION";
    public static final String BMDCNO = "BMDCNO";
    public static final String FEE = "FEE";
    public static final String JOB_EXPERIENCE = "JOB_EXPERIENCE";
    public static final String ABOUT = "ABOUT";
    public static final String EMAIL = "USER_EMAIL";
    public static final String PHONE = "USER_PHONE";
    public static final String PASSWORD = "PASSWORD";
    public static final String PROFILE_URL = "PROFILE_URL";
    public static final String PROFILE_PICTURE_URI = "PROFILE_PICTURE_URI";
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String HAS_OPEN = "HAS_OPEN";
    public static final String IS_APPROVED = "IS_APPROVED";
    public static final String P_NAME = "P_NAME";
    public static final String P_AGE = "P_AGE";
    public static final String P_ADDRESS = "P_ADDRESS";
    public static final String P_GENDER = "P_GENDER";
    public static final String P_PROBLEM = "P_PROBLEM";
    public static final String REPORTS = "REPORTS";
    public static final String APPOINTMENT_ID = "APPOINTMENT_ID";
    public static final String P_DETAILS = "P_DETAILS";
    public static final String ADDRESS = "USER_ADDRESS";


    public static boolean isEmailValid(String email) {
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, null, null);
        return Uri.parse(path);
    }

    public static String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public static Bitmap base64ToImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}


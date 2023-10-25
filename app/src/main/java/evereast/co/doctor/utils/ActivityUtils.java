package evereast.co.doctor.utils;

import static evereast.co.doctor.Constants.HAS_OPEN;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import evereast.co.doctor.Activity.HomeActivity;
import evereast.co.doctor.Activity.LoginActivity;
import evereast.co.doctor.Activity.MainActivity;
import evereast.co.doctor.Activity.PendingMessageActivity;
import evereast.co.doctor.Constants;

public class ActivityUtils {

    public static final int START_SIGN_IN_MANUALLY_ACTIVITY_REQUEST_CODE = 1;
    private static final String TAG = "ActivityUtils";

    @SuppressLint("LongLogTag")
    public static void startLoginActivityAndFinish(@NonNull Activity activity) {
        Log.i(BaseApplication.TAG, "[ActivityUtils] startAuthenticateActivityAndFinish()");

        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.USER_DATA_PREF, Context.MODE_PRIVATE);
        boolean hasOpen = sharedPreferences.getBoolean(HAS_OPEN, false);
        if (hasOpen) {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        }
        activity.finish();
    }

    @SuppressLint("LongLogTag")
    public static void startWaitingActivityAndFinish(@NonNull Activity activity) {
        Log.i(BaseApplication.TAG, "[ActivityUtils] startAuthenticateActivityAndFinish()");
        Intent intent = new Intent(activity, PendingMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @SuppressLint("LongLogTag")
    public static void startHomeActivityAndFinish(@NonNull Activity activity) {
        Log.i(BaseApplication.TAG, "[ActivityUtils] startMainActivityAndFinish()");

        Intent intent = new Intent(activity, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

}
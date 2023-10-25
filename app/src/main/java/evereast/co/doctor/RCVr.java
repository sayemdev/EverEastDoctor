package evereast.co.doctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import evereast.co.doctor.Activity.HistoryActivity;

/**
 * Doctor created by Sayem Hossen Saimon on 8/22/2021 at 9:57 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class RCVr extends BroadcastReceiver {
    private static final String TAG = "RCVr";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        Intent i = new Intent(context, HistoryActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}

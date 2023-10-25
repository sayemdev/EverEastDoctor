package evereast.co.doctor.Work;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import evereast.co.doctor.R;
import evereast.co.doctor.main.SplashActivity;
import evereast.co.doctor.utils.BaseApplication;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_IMMUTABLE;

/**
 * alarm test created by Sayem Hossen Saimon on 7/27/2021 at 12:32 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: RECVD");
//        Toast.makeText(context, "Alarmed", Toast.LENGTH_SHORT).show();

        int noti_id = (int) (System.currentTimeMillis() + 1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, BaseApplication.CHANNEL_1_ID).setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setSmallIcon(R.drawable.ic_noti_icon);
        builder.setColor(context.getResources().getColor(R.color.app_bar_color));
        builder.setAutoCancel(true);
        Intent intents = new Intent(context, SplashActivity.class);
        intents.putExtra("alarmKey", intent.getStringExtra("alarmKey"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) (System.currentTimeMillis() + 1), intents, PendingIntent.FLAG_ONE_SHOT|FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPriority(IMPORTANCE_HIGH);
        }

        Uri uri = Uri.parse("android.resource://" + context.getApplicationContext().getPackageName() + "/" + R.raw.alert);
        builder.setSound(uri);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCategory(NotificationCompat.CATEGORY_ALARM);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setContentText("Your duty in evereast will be started after 15 minutes");
        builder.setContentTitle("Reminder!!!");
        manager.notify(noti_id, builder.build());

    }
}

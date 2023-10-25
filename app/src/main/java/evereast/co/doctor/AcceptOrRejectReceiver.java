package evereast.co.doctor;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import evereast.co.doctor.login.VideoCallActivity;

public class AcceptOrRejectReceiver extends BroadcastReceiver {
    private static final String TAG = "AcceptOrRejectReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("accept")) {
            Log.d(TAG, "onReceive: " + intent.getAction());
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.parseInt(intent.getStringExtra("id")));
            Log.d(TAG, "onReceive: " + intent.getStringExtra("roomId"));
            Intent intent1 = new Intent(context, VideoCallActivity.class);
            intent1.putExtra("roomId", intent.getStringExtra("roomId"));
            intent1.putExtra("participant", intent.getStringExtra("participant"));
            intent1.putExtra("callerId", intent.getStringExtra("callerId"));
            intent1.putExtra("appointmentId", intent.getStringExtra("appointmentId"));
            intent1.putExtra("body", intent.getStringExtra("body"));
            intent1.putExtra("name", intent.getStringExtra("name"));
            intent1.putExtra("type", intent.getStringExtra("type"));
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else {
            Log.d(TAG, "onReceive: " + intent.getAction());
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.parseInt(intent.getStringExtra("id")));
        }

    }
}

package evereast.co.doctor.express;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import evereast.co.doctor.R;
import evereast.co.doctor.login.VideoCallActivity;

public class FloatingWindowService extends Service {
    private static final String TAG = "FloatingWindowService";
    WindowManager windowManager;
    LayoutInflater layoutInflater;
    Date callStartedAt;
    long when = 0;
    private WindowManager.LayoutParams layoutParams;
    private View display;
    private TextureView textureView;

    @Override
    public void onCreate() {
        super.onCreate();
        layoutInflater = LayoutInflater.from(FloatingWindowService.this);
        display = layoutInflater.inflate(R.layout.floating_layout, null);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        layoutParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_TOAST,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        // Set the video playback window size
        layoutParams.width = dm.widthPixels / 3;
        layoutParams.height = dm.heightPixels / 3;
        layoutParams.x = 700;
        layoutParams.y = 0;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //Get current streamId
        String streamId = intent.getStringExtra("streamId");
        Log.d(TAG, "onStartCommand: " + streamId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, new Intent(this, VideoCallActivity.class), PendingIntent.FLAG_ONE_SHOT | FLAG_IMMUTABLE);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String dtStart = getSharedPreferences("CALL", MODE_PRIVATE).getString("STARTED_AT", "00");
            callStartedAt = format.parse(dtStart);
            when = callStartedAt.getTime();
            Notification notification = new NotificationCompat.Builder(this, "AudioCall")
                    .setContentTitle(getSharedPreferences("CALL", MODE_PRIVATE).getString("name", ""))
                    .setContentText("Ongoing video call")
                    .setSmallIcon(R.drawable.ic_call_icon)
                    .setContentIntent(pendingIntent)
                    .setWhen(when)
                    .setUsesChronometer(true)
                    .build();
            startForeground(134475343, notification);
        } catch (ParseException e) {
            e.printStackTrace();
            Notification notification = new NotificationCompat.Builder(this, "AudioCall")
                    .setContentTitle(getSharedPreferences("CALL", MODE_PRIVATE).getString("name", ""))
                    .setContentText("Ongoing video call")
                    .setSmallIcon(R.drawable.ic_call_icon)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(134475343, notification);
        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
/*        if (windowManager != null)
            windowManager.removeViewImmediate(display);*/
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, new Intent(this, VideoCallActivity.class), PendingIntent.FLAG_ONE_SHOT | FLAG_IMMUTABLE);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String dtStart = getSharedPreferences("CALL", MODE_PRIVATE).getString("STARTED_AT", "00");
            callStartedAt = format.parse(dtStart);
            when = callStartedAt.getTime();
            Notification notification = new NotificationCompat.Builder(this, "AudioCall")
                    .setContentTitle(getSharedPreferences("CALL", MODE_PRIVATE).getString("name", ""))
                    .setContentText("Ongoing video call")
                    .setSmallIcon(R.drawable.ic_call_icon)
                    .setContentIntent(pendingIntent)
                    .setWhen(when)
                    .setUsesChronometer(true)
                    .build();
            startForeground(134475343, notification);
        } catch (ParseException e) {
            e.printStackTrace();
            Notification notification = new NotificationCompat.Builder(this, "AudioCall")
                    .setContentTitle(getSharedPreferences("CALL", MODE_PRIVATE).getString("name", ""))
                    .setContentText("Ongoing video call")
                    .setSmallIcon(R.drawable.ic_call_icon)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(134475343, notification);
        }

    }

}
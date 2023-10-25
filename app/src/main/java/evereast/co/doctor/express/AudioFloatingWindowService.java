package evereast.co.doctor.express;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import evereast.co.doctor.R;
import evereast.co.doctor.login.AudioCallActivity;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.constants.ZegoViewMode;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoUser;

public class AudioFloatingWindowService extends Service {
    private static final String TAG = "AudioFloatingWindowService";
    private final Handler handler = new Handler();
    WindowManager windowManager;
    Date callStartedAt;
    long when = 0;
    private WindowManager.LayoutParams layoutParams;
    private View display;
    private TextureView textureView;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
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
                    PixelFormat.TRANSPARENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
        }

        // Set the image format, the effect is transparent background.
//        layoutParams.format = PixelFormat.RGB_565;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // android 8.0 and later
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            //before android 8.0
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //The flags describe the mode of the window, whether it can be touched, can be
        // focused, etc.
        layoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // Set the video playback window size
/*        layoutParams.width = dm.widthPixels / 2;
        layoutParams.height = dm.heightPixels / 4;*/
        layoutParams.x = 400;
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

        ExpressManager.getInstance().setExpressHandler(new ExpressManager.ExpressManagerHandler() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
                Log.d(TAG, "onRoomUserUpdate: " + updateType + " roomId " + roomID + " User List " + userList);
                if (updateType == ZegoUpdateType.DELETE) {
                    Intent intent = new Intent("end_call");
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onRoomUserDeviceUpdate(ZegoDeviceUpdateType updateType, String userID, String roomID) {

            }

            @Override
            public void onRoomTokenWillExpire(String roomID, int remainTimeInSecond) {

            }
        });

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String dtStart = getSharedPreferences("CALL", MODE_PRIVATE).getString("STARTED_AT", "00");
            if (!dtStart.equals("00")) {
                callStartedAt = format.parse(dtStart);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            handler.postDelayed(this, 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(runnable, 0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, new Intent(this, AudioCallActivity.class), PendingIntent.FLAG_ONE_SHOT | FLAG_IMMUTABLE);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String dtStart = getSharedPreferences("CALL", MODE_PRIVATE).getString("STARTED_AT", "00");
            callStartedAt = format.parse(dtStart);
            when = callStartedAt.getTime();
            Notification notification = new NotificationCompat.Builder(this, "AudioCall")
                    .setContentTitle(getSharedPreferences("CALL", MODE_PRIVATE).getString("name", ""))
                    .setContentText("Ongoing audio call")
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
                    .setContentText("Ongoing audio call")
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
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, new Intent(this, AudioCallActivity.class), PendingIntent.FLAG_ONE_SHOT | FLAG_IMMUTABLE);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String dtStart = getSharedPreferences("CALL", MODE_PRIVATE).getString("STARTED_AT", "00");
            callStartedAt = format.parse(dtStart);
            when = callStartedAt.getTime();
            Notification notification = new NotificationCompat.Builder(this, "AudioCall")
                    .setContentTitle(getSharedPreferences("CALL", MODE_PRIVATE).getString("name", ""))
                    .setContentText("Ongoing audio call")
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
                    .setContentText("Ongoing audio call")
                    .setSmallIcon(R.drawable.ic_call_icon)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(134475343, notification);
        }

    }


    private ZegoCanvas generateCanvas(TextureView textureView) {
        ZegoCanvas canvas = new ZegoCanvas(textureView);
        canvas.viewMode = ZegoViewMode.ASPECT_FILL;
        return canvas;
    }
}
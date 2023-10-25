package evereast.co.doctor.utils;


import static android.app.Notification.VISIBILITY_PUBLIC;
import static evereast.co.doctor.AppointmentTimeFormatting.FormatDoctorTime;
import static evereast.co.doctor.AppointmentTimeFormatting.IsAvailable;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.IS_LOGGED_IN;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import evereast.co.doctor.Maps;
import evereast.co.doctor.R;
import evereast.co.doctor.Work.AlarmReceiver;
import evereast.co.doctor.express.AppCenter;
import evereast.co.doctor.express.ExpressManager;
import io.sentry.Sentry;

public class BaseApplication extends MultiDexApplication implements LifecycleObserver { // multidex

    public static final String VERSION = "1.4.0";

    public static final String TAG = "SendBirdApplication";

    // Refer to "https://github.com/sendbird/quickstart-calls-android".
    public static final String APP_ID = "A65EB94B-1654-4B3D-B51D-2B60F3311A77";
    public static final String ACCESS_TOKEN = "";
    public static String CHANNEL_1_ID = "DoctorAlert";
    FirebaseAuth firebaseAuth;
    DatabaseReference userDatabase;
    FirebaseUser firebaseUser;

    boolean isLoggedIn = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("LongLogTag")
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseCrashlytics.getInstance().log("App opened");
        Sentry.captureMessage("testing SDK setup");

        try {
            if (IsAvailable(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(Maps.AVAILABLE_ON, ""))) {
                Log.d(TAG, "onCreate: Your duty will be started");
                String time = FormatDoctorTime(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(Maps.START, ""));
                Calendar calendar = Calendar.getInstance();
                String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);

                calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
                calendar.add(Calendar.MINUTE, -15);
                calendar.set(Calendar.SECOND, 0);

                String alarmKey = date + " " + time;

                Intent intent2 = new Intent(this, AlarmReceiver.class);
                Bundle b = new Bundle();
                b.putString("alarmKey", alarmKey);
                int pendingKey = (int) calendar.getTimeInMillis();
                Log.d(TAG, "onCreate: " + pendingKey);
                intent2.putExtras(b);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this, pendingKey, intent2, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Log.d(TAG, "onCreate: " + calendar.getTime());
                if (calendar.getTime().after(Calendar.getInstance().getTime())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }
            } else {
                Log.d(TAG, "onCreate: Today is your holiday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(BaseApplication.TAG, "[BaseApplication] onCreate()");


        isLoggedIn = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getBoolean(IS_LOGGED_IN, false);

        CreateNotificationChannel();

        try {
            if (isLoggedIn) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                databaseReference.child("OnlineStatus").setValue("Online");
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        String token = task.getResult();
                        Log.i("TAG_H", "onCreate: " + token);

//                        databaseReference.child(MY_TOKEN).setValue(task.getResult());
                    }
                });
                long appID = 531842299;
                String appSign = "3d1393005cd51efee1e4eb8168908ef99be237a22e74b2980de499c8bad2e4d7";
                String userID = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""); // yourUserID, userID should only contain numbers, English characters, and '_'.
                String userName = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_NAME, "");   // yourUserName

                ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig;
                callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
                callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
                //This property needs to be set when you are building an Android app and when the notifyWhenAppRunningInBackgroundOrQuit is true.
                //androidNotificationConfig.channelID must be the same as the FCM Channel ID in [ZEGOCLOUD Admin Console|_blank]https://console.zegocloud.com),
                // and the androidNotificationConfig.channelName can be an arbitrary value.
                ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
                notificationConfig.sound = "zego_incoming";
                notificationConfig.channelID = "call";
                notificationConfig.channelName = "call";
                callInvitationConfig.notificationConfig = notificationConfig;
                ZegoUIKitPrebuiltCallInvitationService.init(this, appID, appSign, userID, userName, callInvitationConfig);
                Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, BASE_URL + "d_opened.php", response -> {
                    Log.d(TAG, "onCreate: "+response);
                }, Throwable::printStackTrace) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<>();
                        map.put("did", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                        map.put("time", System.currentTimeMillis() + "");
                        return map;
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExpressManager.getInstance().createEngine(this, AppCenter.appID);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNotificationChannel() {
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        NotificationChannel defaultNotificationChannel = new NotificationChannel("Default",
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH);
        defaultNotificationChannel.setSound(defaultSoundUri, attributes);
        defaultNotificationChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);
        defaultNotificationChannel.setDescription("Default");
        defaultNotificationChannel.setShowBadge(true);
        defaultNotificationChannel.enableVibration(true);
        defaultNotificationChannel.enableLights(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            defaultNotificationChannel.setAllowBubbles(true);
        }
        NotificationManager manager = getSystemService(NotificationManager.class);

        NotificationChannel channel = new NotificationChannel("Appointment",
                "Appointment Channel",
                NotificationManager.IMPORTANCE_HIGH);
        manager.deleteNotificationChannel(getResources().getString(R.string.fcm_fallback_notification_channel_label));
        channel.setSound(defaultSoundUri, attributes);
        channel.setLockscreenVisibility(VISIBILITY_PUBLIC);
        channel.setDescription("Default");
        channel.setShowBadge(true);
        channel.enableVibration(true);
        channel.enableLights(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            channel.setAllowBubbles(true);
        }

        NotificationChannel channelWP = new NotificationChannel(channelId,
                "Healthmen Patients",
                NotificationManager.IMPORTANCE_HIGH);

        channelWP.setSound(defaultSoundUri, attributes);
        channelWP.setLockscreenVisibility(VISIBILITY_PUBLIC);
        channelWP.setDescription("Default");
        channelWP.setShowBadge(true);
        channelWP.enableVibration(true);
        channelWP.enableLights(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            channelWP.setAllowBubbles(true);
        }


        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
        );

        channel1.setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.alert),
                new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setLegacyStreamType(AudioManager.STREAM_RING)
                        .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION).build());
        channel1.setLockscreenVisibility(VISIBILITY_PUBLIC);
        channel1.setDescription("This is Channel 1");
        channel1.setShowBadge(true);
        channel1.enableVibration(true);
        channel1.enableLights(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            channel1.setAllowBubbles(true);
        }
        channel1.setImportance(NotificationManager.IMPORTANCE_HIGH);
        NotificationChannel callNotifacation = new NotificationChannel(
                "CALL",
                "CALL",
                NotificationManager.IMPORTANCE_HIGH
        );

        Uri callSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        AudioAttributes callAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING)
                .build();

        callNotifacation.setSound(callSoundUri,callAttributes);
        callNotifacation.setLockscreenVisibility(VISIBILITY_PUBLIC);
        callNotifacation.setDescription("Call Channel");
        callNotifacation.setShowBadge(true);
        callNotifacation.enableVibration(true);
        callNotifacation.enableLights(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callNotifacation.setAllowBubbles(true);
        }
        callNotifacation.setImportance(NotificationManager.IMPORTANCE_HIGH);
        NotificationChannel audioCallChannel = new NotificationChannel("AudioCall","AudioCall",
                NotificationManager.IMPORTANCE_DEFAULT
        );


        audioCallChannel.setSound(defaultSoundUri, attributes);
        audioCallChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);
        audioCallChannel.setDescription("Audio Call Notification");
        audioCallChannel.setShowBadge(false);
        audioCallChannel.enableVibration(false);
        audioCallChannel.enableLights(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            audioCallChannel.setAllowBubbles(true);
        }
        audioCallChannel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);

        manager.createNotificationChannel(channel1);

        manager.createNotificationChannel(channelWP);
        manager.createNotificationChannel(callNotifacation);
        manager.createNotificationChannel(audioCallChannel);
        manager.createNotificationChannel(channel);
        manager.createNotificationChannel(defaultNotificationChannel);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.d("MyApp", "App in background");
        if (isLoggedIn) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, " "));
            databaseReference.child("OnlineStatus").setValue("Offline");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        Log.d("MyApp", "App in foreground");
        if (isLoggedIn) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, " "));
            databaseReference.child("OnlineStatus").setValue("Online");
        }
        if ( isExternalStorageWritable() ) {

            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/MyPersonalAppFolder" );
            File logDirectory = new File( appDirectory + "/logs" );
            File logFile = new File( logDirectory, "logcat_" + System.currentTimeMillis() + ".txt" );

            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }

            // create log folder
            if ( !logDirectory.exists() ) {
                logDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -d " + logFile);
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        } else if ( isExternalStorageReadable() ) {
            // only readable
        } else {
            // not accessible
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
            return true;
        }
        return false;
    }

}

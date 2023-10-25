package evereast.co.doctor.Notification;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import evereast.co.doctor.Activity.HomeActivity;
import evereast.co.doctor.R;
import evereast.co.doctor.chat.ChatActivity;
import evereast.co.doctor.main.SplashActivity;

import static evereast.co.doctor.Constants.MY_TOKEN;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

/**
 * PushTest created by Sayem Hossen Saimon on 3/14/2021 at 2:24 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class MyFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    public static void showNotificationForAppointment(String messageBody, String title, Context context) {
        Log.d("Hay8", "DCM8");
        Intent intent = new Intent(context, HomeActivity.class);
        Log.d("Hay9", "DCM9");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT|FLAG_IMMUTABLE);
        Log.d("Hay10", "DCM10");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Default")
                .setSmallIcon(R.drawable.splogo)
                .setColor(context.getResources().getColor(R.color.app_bar_color))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody)
                        .setBigContentTitle(title))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        Log.d("Hay11", "DCM11");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("Hay12", "DCM12");

        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(100000), builder.build());
    }
    // [END receive_message]

    // [START on_new_token]

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @SuppressLint("WrongThread")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            sendNotification(remoteMessage.getData().toString());

            /* if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                //getting the json data
                JSONObject data = json.getJSONObject("data");
                //parsing json data
                String title = data.getString("title");
                String message = data.getString("message");
                String imageUrl = data.getString("image");
//                sendNotification(message);
                new ShowNotificationWithImage(this, title, message,
                        imageUrl).execute();
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "onMessageReceived: " + remoteMessage.getData().toString());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            RemoteMessage.Notification notification = remoteMessage.getNotification();
//            Log.d(TAG, "onMessageReceived: "+remoteMessage.getData());
            if (remoteMessage.getData().get("type").equals("chat")) {
                sendNotificationForChat(remoteMessage.getNotification().getBody(),
                        remoteMessage.getNotification().getTitle(),
                        remoteMessage.getData());
            } else {
                sendNotificationForAp(remoteMessage.getNotification().getBody(),
                        remoteMessage.getNotification().getTitle(),
                        remoteMessage.getData());
            }

//            showSmallNotification(notification.getTitle(),remoteMessage.getNotification().getBody(),new Intent(this,ChatActivity.class));

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    // [END on_new_token]

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
        databaseReference.child(MY_TOKEN).setValue(token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

    private void sendNotificationForAp(String messageBody, String title, Map<String, String> data) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("senderId", data.get("senderId"));
        intent.putExtra("sender_name", data.get("sender_name"));
        intent.putExtra("sender_token", data.get("sender_token"));
        intent.putExtra("appointment_id", data.get("appointment_id"));
        intent.putExtra("type", data.get("type"));
        intent.putExtra("route", "Notification");
        String channelId = "Appointment";

        Log.d(TAG, "sendNotificationForChat: " + data.get("type"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT|FLAG_IMMUTABLE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.splogo)
                        .setColor(getResources().getColor(R.color.app_bar_color))
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(10000)/* 1 ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationForChat(String messageBody, String title, Map<String, String> data) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("senderId", data.get("senderId"));
        intent.putExtra("sender_name", data.get("sender_name"));
        intent.putExtra("sender_token", data.get("sender_token"));
        intent.putExtra("appointment_id", data.get("appointment_id"));
        intent.putExtra("type", data.get("type"));
        intent.putExtra("route", "Notification");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(intent);

        Log.d(TAG, "sendNotificationForChat: " + data.get("type"));
        String channelId = "Default";

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(114643, PendingIntent.FLAG_UPDATE_CURRENT|FLAG_IMMUTABLE
        );
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.splogo)
                        .setColor(getResources().getColor(R.color.app_bar_color))
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(10000), notificationBuilder.build());
    }


    public class ShowNotificationWithImage extends AsyncTask<String, Void, Bitmap> {

        private final Context mContext;
        private final String title;
        private final String message;
        private final String imageUrl;

        public ShowNotificationWithImage(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            Intent intent = new Intent(MyFirebaseMsgService.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("title", title);
            intent.putExtra("route", "Post");
            intent.putExtra("type", "appointment");
            PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMsgService.this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT|FLAG_IMMUTABLE);

            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(MyFirebaseMsgService.this, channelId)
                            .setSmallIcon(R.drawable.splogo)
                            .setColor(getResources().getColor(R.color.app_bar_color))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setLargeIcon(bitmap)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Healthmen Patients",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setShowBadge(true);
//                channel.setSound(defaultSoundUri);
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setShowBadge(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    channel.setAllowBubbles(true);
                }
                notificationManager.createNotificationChannel(channel);

            }*/

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        }
    }


}
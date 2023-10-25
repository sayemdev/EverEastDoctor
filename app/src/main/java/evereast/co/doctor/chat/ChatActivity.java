package evereast.co.doctor.chat;

import static evereast.co.doctor.Activity.HomeActivity.hasPermissions;
import static evereast.co.doctor.Constants.DOMAIN_URL;
import static evereast.co.doctor.Constants.MAIN_URL;
import static evereast.co.doctor.Constants.MY_TOKEN;
import static evereast.co.doctor.Constants.PATIENT_URL;
import static evereast.co.doctor.Constants.PROFILE_FILES;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;
import static evereast.co.doctor.RXActivities.PPActivity.ADDRESS;
import static evereast.co.doctor.RXActivities.PPActivity.AGE;
import static evereast.co.doctor.RXActivities.PPActivity.AGE_TYPE;
import static evereast.co.doctor.RXActivities.PPActivity.APPOINTMENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.DATE;
import static evereast.co.doctor.RXActivities.PPActivity.GENDER;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_NAME;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoLayoutGalleryConfig;
import com.zegocloud.uikit.plugin.signaling.ZegoSignalingPlugin;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evereast.co.doctor.Activity.GiveFromTemplateActivity;
import evereast.co.doctor.Activity.HomeActivity;
import evereast.co.doctor.Activity.SendingChatImageActivity;
import evereast.co.doctor.DialogFragment.ChoosePrescriptionTypeFragment;
import evereast.co.doctor.DialogFragment.RatePatientDialogFragment;
import evereast.co.doctor.DialogFragment.RequestPermissionDialog;
import evereast.co.doctor.DialogFragment.SelectPhoto;
import evereast.co.doctor.GetAge;
import evereast.co.doctor.Model.TemplateModel;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.PPActivity;
import evereast.co.doctor.RXActivities.ShowFUDialogFragment;
import evereast.co.doctor.RXFragments.PrescriptionFlowActivity;
import evereast.co.doctor.databinding.ActivityChatBinding;
import evereast.co.doctor.login.AudioCallActivity;
import evereast.co.doctor.login.VideoCallActivity;
import evereast.co.doctor.utils.PrefUtils;

public class ChatActivity extends AppCompatActivity implements ShowFUDialogFragment.SelectPhotoValueListener, SelectPhoto.SelectPhotoValueListener, ChoosePrescriptionTypeFragment.CreateNewOrReadAirPrescription, RatePatientDialogFragment.ConfirmationValueListener, RequestPermissionDialog.ConfirmationValueListener {
    private static final String TAG = "RETRIEVELOG";
    private static final int PICK_FILE_REQUEST = 1;
    private static final int CAMERA_FILE_REQUEST = 2;
    private static final int GALLERY_FILE_REQUEST = 200;
    String image;
    String user_name, chatUser, doctorName;
    ImageView backImageView, sendButton;
    TextView userNameTextView;
    EditText messageEditText;
    ChatAdapter chatAdapter;
    DatabaseReference userDatabase, rootReference;
    FirebaseAuth firebaseAuth;
    String currentUserId;
    CircularImageView profileImageView;

    RecyclerView recyclerView;
    List<Messages> messagesList;
    LinearLayoutManager layoutManager;
    DatabaseReference messageReference;
    String chatUserToken, myToken, appointment_id;
    Calendar calendar;
    int day;
    int month;
    int year;
    StringBuilder stringBuilder;
    TextView onlineStatusTV;
    String chatUserProfilePicture;
    String rating = "0";
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
    };
    int PERMISSION_ALL = 1;
    private ImageView addFileImageView;
    private ProgressBar progressBar;
    private Bitmap photo;
    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.i(TAG, "onCreate: " + getIntent().getStringExtra("sender_name"));

        chatUser = getIntent().getStringExtra("senderId");
        chatUserToken = getIntent().getStringExtra("sender_token");
        user_name = getIntent().getStringExtra("sender_name");
        appointment_id = getIntent().getStringExtra("appointment_id");

        progressBar = findViewById(R.id.chatLoader);
        onlineStatusTV = findViewById(R.id.onlineStatusTV);
        addFileImageView = findViewById(R.id.addMore);

        Log.d(TAG, "onCreate: Appointment" + chatUserToken);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUserId = sharedPreferences.getString(USER_ID, "Anonymous");
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(chatUser);
        rootReference = FirebaseDatabase.getInstance().getReference();
        myToken = PrefUtils.getPushToken(this);
        doctorName = sharedPreferences.getString(USER_NAME, "Unnamed");
        messageReference = FirebaseDatabase.getInstance().getReference().child("messages/" + currentUserId + "/" + chatUser);
        messagesList = new ArrayList<>();
        backImageView = findViewById(R.id.back);
        userNameTextView = findViewById(R.id.displayNameId);
        sendButton = findViewById(R.id.send);
        messageEditText = findViewById(R.id.content_id);
        recyclerView = findViewById(R.id.recyclerViewId);
        profileImageView = findViewById(R.id.topProfile);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        Picasso.get().load(PROFILE_FILES + chatUser + ".jpg").placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(profileImageView);
        Log.i(TAG, "onCreate: " + chatUser);
        DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference().child("Users").child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                String token = task.getResult();
                Log.i("TAG_H", "onCreate: " + token);
                databaseReferences.child(MY_TOKEN).setValue(task.getResult());
            }
        });
        try {
            if (user_name == null) {
                user_name = "Unknown";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userNameTextView.setText(user_name);
        initCallInviteService(currentUserId);

        initVoiceButton();

        initVideoButton();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, PATIENT_URL + "patient_info_by_id.php?patient_id=" + chatUser, response -> {
            Log.d(TAG, "onCreate: " + response);
            try {
                FirebaseDatabase.getInstance().getReference("Users").child(chatUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        onlineStatusTV.setText(snapshot.child("OnlineStatus").getValue(String.class));
                        chatUserToken = snapshot.child(MY_TOKEN).getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        error.toException().printStackTrace();
                        onlineStatusTV.setText("Offline");
                    }
                });
                JSONObject object = new JSONObject(response);
                chatUserProfilePicture = object.getString("patient_id") + ".jpg";
                user_name = object.getString("p_name");
                Glide.with(this).load(PROFILE_FILES + chatUserProfilePicture).placeholder(R.drawable.ic_profile).into(profileImageView);
                userNameTextView.setText(user_name);
                sendButton.setVisibility(View.VISIBLE);
                rating = object.getString("rate");
                addFileImageView.setVisibility(View.VISIBLE);
                retrieveMessage();
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }));

        FirebaseDatabase.getInstance().getReference("appointments").child(appointment_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("chat")) {
                    if (snapshot.child("chat").getValue(String.class).equals("Running")) {
                        binding.chatEnded.setVisibility(View.GONE);
                        binding.chatView.setVisibility(View.VISIBLE);
                    } else {
                        binding.chatEnded.setVisibility(View.VISIBLE);
                        binding.chatView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });


        backImageView.setOnClickListener(v -> finish());
        sendButton.setOnClickListener(v -> sendMessage("text", " "));
        addFileImageView.setOnClickListener(v -> {
            SelectPhoto selectPhoto = new SelectPhoto();
            selectPhoto.show(getSupportFragmentManager(), selectPhoto.getTag());
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermissions(this, PERMISSIONS)) {
            RequestPermissionDialog permissionDialog = new RequestPermissionDialog();
            permissionDialog.show(getSupportFragmentManager(), permissionDialog.getTag());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_FILE_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentcam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentcam, CAMERA_FILE_REQUEST);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.message_no_storage_permission_snackbar), Snackbar.LENGTH_LONG);
                    snackbar.setAction(getResources().getString(R.string.settings), v -> {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    });
                    snackbar.show();
                }
            }
        } else if (requestCode == GALLERY_FILE_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted :)
//                    downloadFile();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_FILE_REQUEST);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.message_no_storage_permission_snackbar), Snackbar.LENGTH_LONG);
                    snackbar.setAction(getResources().getString(R.string.settings), v -> {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    });
                    snackbar.show();
                }
            }
        } else if (requestCode == PERMISSION_ALL) {
            boolean micPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean writeExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            Log.d(TAG, "onRequestPermissionsResult: " + micPermission);
            Log.d(TAG, "onRequestPermissionsResult: " + cameraPermission);
            Log.d(TAG, "onRequestPermissionsResult: " + writeExternalFile);

            if (!cameraPermission || !writeExternalFile || !micPermission) {
                // write your logic here
                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please grant permissions to use full app",
                        Snackbar.LENGTH_LONG).setAction("Enable",
                        v -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }).show();
            }
        }

    }


    private void retrieveMessage() {

        messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesList.clear();


                if (dataSnapshot.exists()) {
                    messagesList.clear();
                    for (DataSnapshot catSnapshot : dataSnapshot.getChildren()) {
                        if (dataSnapshot.exists()) {

//                        String name=catSnapshot.getKey();
                            String message = catSnapshot.child("message").getValue(String.class);
                            String seen = catSnapshot.child("seen").getValue(String.class);
                            String from = catSnapshot.child("from").getValue(String.class);
                            String type = catSnapshot.child("type").getValue(String.class);
                            String profile = catSnapshot.child("profile").getValue(String.class);
                            try {
                                String timeLong = catSnapshot.child("time").getValue(String.class);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i(TAG, "onDataChange: " + e.getMessage());
                                Toast.makeText(ChatActivity.this, e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                            }
//                            long timeLong = catSnapshot.child("time").getValue(long.class);
//                            String timeLong = catSnapshot.child("time").getValue(String.class);

                            String time = catSnapshot.child("timeStamp").getValue(String.class);
//                            String time2 = String.valueOf(timeLong);

                            String push_id = catSnapshot.getKey();
                            if (push_id != null) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("seen", "true");
                                Messages model = new Messages(message, from, time, seen, type);
                                model.setChatUserId(chatUser);
                                model.setCurrentUserId(currentUserId);
                                messagesList.add(model);

                            } else {
//                            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                                Messages model = new Messages("Welcome to this app. Thanks for being a new member", currentUserId, "profile_image", "1599497112397", type + " ");
                                model.setChatUserId(chatUser);
//                                model.setCurrentUserId(currentUserId);
                                messagesList.add(model);
                            }
                            DatabaseReference currentUserSeenReference = FirebaseDatabase.getInstance().getReference().child("isSeen?").child(currentUserId).child(ChatActivity.this.chatUser);

                            currentUserSeenReference.child("seen").setValue("true");
                            final DatabaseReference chatUserSeenReference = FirebaseDatabase.getInstance().getReference().child("isSeen?").child(ChatActivity.this.chatUser).child(currentUserId);
                        }

                    }
                }
                chatAdapter = new ChatAdapter(messagesList, ChatActivity.this);
                chatAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void sendMessage(String type, String imageUrl) {
        if (type.equals("text")) {
            final String message = messageEditText.getText().toString();
            messageEditText.setText("");
            if (!TextUtils.isEmpty(message)) {
                final String current_user_ref = "messages/" + currentUserId + "/" + chatUser;
                final String current_user_chat_ref = "Chat/" + currentUserId + "/" + chatUser;
                final String chat_user_ref = "messages/" + chatUser + "/" + currentUserId;
                final String chat_user_chat_ref = "Chat/" + chatUser + "/" + currentUserId;

                DatabaseReference userMassagePush = rootReference.child(current_user_ref).push();
                final String push_id = userMassagePush.getKey();

                DatabaseReference currentUserSeenReference = FirebaseDatabase.getInstance().getReference().child("isSeen?").child(currentUserId).child(chatUser);
                DatabaseReference chatUserSeenReference = FirebaseDatabase.getInstance().getReference().child("isSeen?").child(chatUser).child(currentUserId);

                currentUserSeenReference.child("seen").setValue("false");
                chatUserSeenReference.child("seen").setValue("false");

                rootReference.child("Users").child(currentUserId).child("thumb_image").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
                        String timeStamp1 = String.valueOf(System.currentTimeMillis());

                        Map<String, Object> messageMap = new HashMap<>();
                        messageMap.put("message", message);
                        messageMap.put("seen", "false");
                        messageMap.put("type", type);
                        messageMap.put("timeStamp", timeStamp1);
                        messageMap.put("from", currentUserId);

                        Map<String, Object> messageUSerMap = new HashMap<>();
                        messageUSerMap.put(current_user_chat_ref + "/lastMessage", message);
//                    messageUSerMap.put(current_user_chat_ref + "/seen", true);
                        messageUSerMap.put(current_user_chat_ref + "/timeStamp", timeStamp1);
//                    messageUSerMap.put(current_user_chat_ref + "/seen", "true");
                        messageUSerMap.put(chat_user_chat_ref + "/lastMessage", message);
                        messageUSerMap.put(chat_user_chat_ref + "/timeStamp", timeStamp1);
//                    messageUSerMap.put(chat_user_chat_ref + "/seen", "false");
                        messageUSerMap.put(current_user_ref + "/" + push_id, messageMap);
                        messageUSerMap.put(chat_user_ref + "/" + push_id, messageMap);

                        rootReference.updateChildren(messageUSerMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error != null) {
                                    Log.e("MESSAGE_LOG", "onComplete: " + error.getMessage());
                                }
                            }
                        });
                        Volley.newRequestQueue(ChatActivity.this).add(new StringRequest(Request.Method.POST, MAIN_URL + "chatfcm.php",
                                response -> {
                                    Log.i(TAG, "onDataChange: " + response);
                                },
                                error -> {
                                    error.printStackTrace();
                                }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> maps = new HashMap<>();
                                maps.put("sender_token", myToken);
                                maps.put("token", chatUserToken);
                                maps.put("chat_user_id", chatUser);
                                maps.put("d_name", doctorName);
                                maps.put("text", message);
                                maps.put("sender", currentUserId);
                                maps.put("sender_name", doctorName);
                                maps.put("appointment_id", appointment_id);
                                Log.d(TAG, "getParams: " + maps);
                                return maps;
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.toException().printStackTrace();
                    }
                });
            }
        } else {
            final String current_user_ref = "messages/" + currentUserId + "/" + chatUser;
            final String current_user_chat_ref = "Chat/" + currentUserId + "/" + chatUser;
            final String chat_user_ref = "messages/" + chatUser + "/" + currentUserId;
            final String chat_user_chat_ref = "Chat/" + chatUser + "/" + currentUserId;

            DatabaseReference userMassagePush = rootReference.child(current_user_ref).push();
            final String push_id = userMassagePush.getKey();

            DatabaseReference currentUserSeenReference = FirebaseDatabase.getInstance().getReference().child("isSeen?").child(currentUserId).child(chatUser);
            DatabaseReference chatUserSeenReference = FirebaseDatabase.getInstance().getReference().child("isSeen?").child(chatUser).child(currentUserId);

            currentUserSeenReference.child("seen").setValue("false");
            chatUserSeenReference.child("seen").setValue("false");

            rootReference.child("Users").child(currentUserId).child("thumb_image").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
                    String timeStamp1 = String.valueOf(System.currentTimeMillis());

                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("message", imageUrl);
                    messageMap.put("seen", "false");
                    messageMap.put("type", type);
                    messageMap.put("timeStamp", timeStamp1);
                    messageMap.put("from", currentUserId);

                    Map<String, Object> messageUSerMap = new HashMap<>();
                    messageUSerMap.put(current_user_chat_ref + "/lastMessage", imageUrl);
                    messageUSerMap.put(current_user_chat_ref + "/timeStamp", timeStamp1);
                    messageUSerMap.put(chat_user_chat_ref + "/lastMessage", imageUrl);
                    messageUSerMap.put(chat_user_chat_ref + "/timeStamp", timeStamp1);
                    messageUSerMap.put(current_user_ref + "/" + push_id, messageMap);
                    messageUSerMap.put(chat_user_ref + "/" + push_id, messageMap);

                    rootReference.updateChildren(messageUSerMap, (error, ref) -> {
                        if (error != null) {
                            Log.e("MESSAGE_LOG", "onComplete: " + error.getMessage());
                        }
                    });
                    Volley.newRequestQueue(ChatActivity.this).add(new StringRequest(Request.Method.POST, MAIN_URL + "chatfcm.php",
                            response -> {
                                Log.i(TAG, "onDataChange: " + response);
                            },
                            error -> {
                                error.printStackTrace();
                            }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> maps = new HashMap<>();
                            maps.put("sender_token", myToken);
                            maps.put("token", chatUserToken);
                            maps.put("d_name", doctorName);
                            maps.put("text", "📷 Sent an image");
                            maps.put("chat_user_id", chatUser);
                            maps.put("sender", currentUserId);
                            maps.put("sender_name", doctorName);
                            maps.put("appointment_id", appointment_id);
                            Log.d(TAG, "getParams: " + maps);
                            return maps;
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                    messageEditText.setText(imageUrl);
                }
            });
        }
    }

    private void initVideoButton() {
        ZegoSendCallInvitationButton newVideoCall = findViewById(R.id.new_video_call);
        newVideoCall.setIsVideoCall(true);
        newVideoCall.setResourceID("zego_incoming");
        newVideoCall.setOnClickListener(v -> {
            String targetUserID = chatUser;
            String[] split = targetUserID.split(",");
            List<ZegoUIKitUser> users = new ArrayList<>();
            for (String userID : split) {
                String userName = user_name;
                users.add(new ZegoUIKitUser(userID, userName));
            }
            newVideoCall.setInvitees(users);
        });
    }

    private void initVoiceButton() {
        ZegoSendCallInvitationButton newVoiceCall = findViewById(R.id.new_voice_call);
        newVoiceCall.setIsVideoCall(false);
        newVoiceCall.setResourceID("zego_incoming");
        newVoiceCall.setOnClickListener(v -> {
            String targetUserID = chatUser;
            String[] split = targetUserID.split(",");
            List<ZegoUIKitUser> users = new ArrayList<>();
            for (String userID : split) {
                String userName = user_name;
                users.add(new ZegoUIKitUser(userID, userName));
            }
            newVoiceCall.setInvitees(users);
        });
    }

    public void initCallInviteService(String generateUserID) {
        long appID = 531842299;
        String appSign = "3d1393005cd51efee1e4eb8168908ef99be237a22e74b2980de499c8bad2e4d7";
        String userID = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""); // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_NAME, "");   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig;
        callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        callInvitationConfig.showDeclineButton=true;
        callInvitationConfig.innerText.incomingCallPageDeclineButton = "Decline";
        callInvitationConfig.innerText.incomingCallPageAcceptButton = "Accept";
        //This property needs to be set when you are building an Android app and when the notifyWhenAppRunningInBackgroundOrQuit is true.
        //androidNotificationConfig.channelID must be the same as the FCM Channel ID in [ZEGOCLOUD Admin Console|_blank]https://console.zegocloud.com),
        // and the androidNotificationConfig.channelName can be an arbitrary value.
//        callInvitationConfig.incomingCallRingtone="ringing";
        callInvitationConfig.outgoingCallRingtone="dialing";
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_incoming";
        notificationConfig.channelID = "call";
        notificationConfig.channelName = "call";
        callInvitationConfig.notificationConfig = notificationConfig;
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);
    }

/*    public void PushToAudioCall(View view) {
        if (!hasPermissions(this, PERMISSIONS)) {
            RequestPermissionDialog permissionDialog = new RequestPermissionDialog();
            permissionDialog.show(getSupportFragmentManager(), permissionDialog.getTag());
        } else {
            SendCall(false);

        }
    }*/

    public void PushToRX(View view) {
        ChoosePrescriptionTypeFragment choosePrescriptionTypeFragment = new ChoosePrescriptionTypeFragment(appointment_id);
        choosePrescriptionTypeFragment.show(getSupportFragmentManager(), choosePrescriptionTypeFragment.getTag());
    }

/*
    public void PushToVideoCallF(View view) {
        if (!hasPermissions(this, PERMISSIONS)) {
            RequestPermissionDialog permissionDialog = new RequestPermissionDialog();
            permissionDialog.show(getSupportFragmentManager(), permissionDialog.getTag());
        } else {
            SendCall(true);
        }

    }
*/

    void SendCall(boolean video) {
        if (getSharedPreferences("CALL", MODE_PRIVATE).getBoolean("roomStarted", false)) {
            SendToRoomCall(getSharedPreferences("CALL", MODE_PRIVATE).getBoolean("video", false));
        } else {
            if (video)
                SendVideo();
            else
                SendAudio();
        }

    }

    private void SendToRoomCall(boolean aBoolean) {
        if (aBoolean) {
            startActivity(new Intent(this, VideoCallActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            startActivity(new Intent(this, AudioCallActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void SendAudio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, DOMAIN_URL + "android/call_fcm.php", response -> {
                    Log.d(TAG, "onBindViewHolder: " + response);
                    Intent intent = new Intent(this, AudioCallActivity.class);
                    intent.putExtra("name", doctorName);
                    intent.putExtra("patient_name", user_name);
                    intent.putExtra("roomId", appointment_id);
                    intent.putExtra("participant", chatUser);
                    intent.putExtra("recieverId", chatUser);
                    intent.putExtra("callerId", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                    intent.putExtra("appointmentId", appointment_id);
                    intent.putExtra("token", chatUserToken);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Call").child(appointment_id).child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "")).child("info");
                    Map<String, String> map = new HashMap<>();
                    map.put("call_state", "0");//0==>> Calling
                    map.put("state_changed_on", System.currentTimeMillis() + "");
                    databaseReference.setValue(map);
                }, error -> {
                    error.printStackTrace();
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<>();
                        map.put("token", chatUserToken);
                        map.put("roomId", appointment_id);
                        map.put("name", doctorName);
                        map.put("participant", chatUser);
                        map.put("recieverId", chatUser);
                        map.put("callerId", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                        map.put("appointmentId", appointment_id);
                        map.put("body", "Incoming Audio Call");
                        map.put("type", "Outgoing");
                        map.put("call_type", "Audio");
                        Log.i(TAG, "getParams: " + map);

                        return map;
                    }
                });

            } else {
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent1, 1);
            }
        } else {
            Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, DOMAIN_URL + "android/call_fcm.php", response -> {
                Log.d(TAG, "onBindViewHolder: " + response);
                Intent intent = new Intent(this, AudioCallActivity.class);
                intent.putExtra("name", doctorName);
                intent.putExtra("roomId", appointment_id);
                intent.putExtra("participant", chatUser);
                intent.putExtra("patient_name", user_name);
                intent.putExtra("recieverId", chatUser);
                intent.putExtra("callerId", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                intent.putExtra("appointmentId", appointment_id);
                intent.putExtra("token", chatUserToken);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Call").child(appointment_id).child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "")).child("info");
                Map<String, String> map = new HashMap<>();
                map.put("call_state", "0");//0==>> Calling
                map.put("state_changed_on", System.currentTimeMillis() + "");
                databaseReference.setValue(map);
            }, error -> {
                error.printStackTrace();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("token", chatUserToken);
                    map.put("roomId", appointment_id);
                    map.put("name", doctorName);
                    map.put("participant", chatUser);
                    map.put("recieverId", chatUser);
                    map.put("callerId", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                    map.put("appointmentId", appointment_id);
                    map.put("body", "Incoming Audio Call");
                    map.put("type", "Outgoing");
                    map.put("call_type", "Audio");
                    return map;
                }
            });
        }
    }

    private void SendVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {

                Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, DOMAIN_URL + "android/call_fcm.php", response -> {
                    Log.d(TAG, "onBindViewHolder:SENDING CALL " + response);
                    Intent intent = new Intent(this, VideoCallActivity.class);
                    intent.putExtra("name", doctorName);
                    intent.putExtra("patient_name", user_name);
                    intent.putExtra("roomId", appointment_id);
                    intent.putExtra("participant", chatUser);
                    intent.putExtra("recieverId", chatUser);
                    intent.putExtra("callerId", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                    intent.putExtra("appointmentId", appointment_id);
                    intent.putExtra("token", chatUserToken);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Call").child(appointment_id).child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "")).child("info");
                    Map<String, String> map = new HashMap<>();
                    map.put("call_state", "0");//0==>> Calling
                    map.put("state_changed_on", System.currentTimeMillis() + "");
                    databaseReference.setValue(map);
                }, error -> {
                    error.printStackTrace();
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<>();
                        map.put("token", chatUserToken);
                        map.put("roomId", appointment_id);
                        map.put("name", doctorName);
                        map.put("participant", chatUser);
                        map.put("recieverId", chatUser);
                        map.put("callerId", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                        map.put("appointmentId", appointment_id);
                        map.put("body", "Incoming Video Call");
                        map.put("type", "Outgoing");
                        map.put("call_type", "Video");
                        return map;
                    }
                });
            } else {
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent1, 1);
            }
        } else {

            Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, DOMAIN_URL + "android/call_fcm.php", response -> {
                Log.d(TAG, "onBindViewHolder: SENDING CALL" + response);
                Intent intent = new Intent(this, VideoCallActivity.class);
                intent.putExtra("name", doctorName);
                intent.putExtra("patient_name", user_name);
                intent.putExtra("roomId", appointment_id);
                intent.putExtra("participant", chatUser);
                intent.putExtra("recieverId", chatUser);
                intent.putExtra("callerId", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                intent.putExtra("appointmentId", appointment_id);
                intent.putExtra("token", chatUserToken);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Call").child(appointment_id).child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "")).child("info");
                Map<String, String> map = new HashMap<>();
                map.put("call_state", "0");//0==>> Calling
                map.put("state_changed_on", System.currentTimeMillis() + "");
                databaseReference.setValue(map);
            }, error -> {
                error.printStackTrace();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("token", chatUserToken);
                    map.put("roomId", appointment_id);
                    map.put("name", doctorName);
                    map.put("participant", chatUser);
                    map.put("recieverId", chatUser);
                    map.put("callerId", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                    map.put("appointmentId", appointment_id);
                    map.put("body", "Incoming Video Call");
                    map.put("type", "Outgoing");
                    map.put("call_type", "Video");
                    return map;
                }
            });
        }
    }

    @Override
    public void Camera() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_FILE_REQUEST);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(this, SendingChatImageActivity.class);
                        intent.putExtra("isCamera", true);
                        intent.putExtra("senderId", chatUser);
                        intent.putExtra("sender_token", chatUserToken);
                        intent.putExtra("sender_name", user_name);
                        intent.putExtra("appointment_id", appointment_id);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(this, SendingChatImageActivity.class);
                    intent.putExtra("isCamera", true);
                    intent.putExtra("senderId", chatUser);
                    intent.putExtra("sender_token", chatUserToken);
                    intent.putExtra("sender_name", user_name);
                    intent.putExtra("appointment_id", appointment_id);
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(this, SendingChatImageActivity.class);
                intent.putExtra("isCamera", true);
                intent.putExtra("senderId", chatUser);
                intent.putExtra("sender_token", chatUserToken);
                intent.putExtra("sender_name", user_name);
                intent.putExtra("appointment_id", appointment_id);
                startActivity(intent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void Gallery() {

        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_FILE_REQUEST);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        galleryIntent();
                    }
                } else {
                    galleryIntent();
                }
            } else {
                galleryIntent();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void galleryIntent() {
        Intent intent = new Intent(this, SendingChatImageActivity.class);
        intent.putExtra("isCamera", false);
        intent.putExtra("senderId", chatUser);
        intent.putExtra("sender_token", chatUserToken);
        intent.putExtra("sender_name", user_name);
        intent.putExtra("appointment_id", appointment_id);
        startActivity(intent);

    }


    @Override
    public void createNew() {
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = (calendar.get(Calendar.MONTH));
        year = calendar.get(Calendar.YEAR);

        stringBuilder = new StringBuilder();
        stringBuilder.append(day)
                .append("/")
                .append(month + 1)
                .append("/")
                .append(year);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting information for appointment...");
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, PATIENT_URL + "get_patient_info.php?patient_id=" + chatUser + "&ap_id=" + appointment_id,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Intent intent = new Intent(this, PrescriptionFlowActivity.class);
                        intent.putExtra("appointmentId", appointment_id);
                        intent.putExtra("token", chatUserToken);
                        intent.putExtra(PATIENT_NAME, jsonObject.getString("p_name"));
                        intent.putExtra(ADDRESS, jsonObject.getString("p_address"));
                        intent.putExtra(GENDER, jsonObject.getString("p_gender"));
                        try {
                            String age = GetAge.getAge(jsonObject.getString("p_bdate"));
                            intent.putExtra(AGE, age);
                            Log.d(TAG, "PushToRX: " + age);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            intent.putExtra(AGE, "0");
                        }
                        intent.putExtra(AGE_TYPE, "Year");
                        intent.putExtra(DATE, stringBuilder.toString());
                        intent.putExtra("prescription", "appointment");
                        intent.putExtra(APPOINTMENT_ID, appointment_id);
                        intent.putExtra(PATIENT_ID, chatUser);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "You can't access Rx for this time try again later", Toast.LENGTH_SHORT).show();
                    }
                }, Throwable::printStackTrace));
    }

    @Override
    public void showFU(String appointmentId) {
        ShowFUDialogFragment showFUDialogFragment = new ShowFUDialogFragment(appointmentId);
        showFUDialogFragment.show(getSupportFragmentManager(), showFUDialogFragment.getTag());
    }

    @Override
    public void older(int position, TemplateModel templateModel) {
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = (calendar.get(Calendar.MONTH));
        year = calendar.get(Calendar.YEAR);

        stringBuilder = new StringBuilder();
        stringBuilder.append(day)
                .append("/")
                .append(month + 1)
                .append("/")
                .append(year);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting information for appointment...");
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, PATIENT_URL + "get_patient_info.php?patient_id=" + chatUser + "&ap_id=" + appointment_id,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Intent intent = new Intent(this, GiveFromTemplateActivity.class);
                        intent.putExtra("appointmentId", appointment_id);
                        intent.putExtra("token", chatUserToken);
                        intent.putExtra("rating", rating);
                        intent.putExtra(PATIENT_NAME, jsonObject.getString("p_name"));
                        intent.putExtra("PATIENT_PROFILE", chatUserProfilePicture);
                        intent.putExtra(ADDRESS, jsonObject.getString("p_address"));
                        intent.putExtra(GENDER, jsonObject.getString("p_gender"));
                        try {
                            intent.putExtra(AGE, GetAge.getAge(jsonObject.getString("p_bdate")));
                            Log.d(TAG, "PushToRX: " + GetAge.getAge(jsonObject.getString("p_bdate")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            intent.putExtra(AGE, "0");
                        }
                        intent.putExtra(AGE_TYPE, "Year");
                        intent.putExtra(DATE, stringBuilder.toString());
                        intent.putExtra("prescription", "appointment");
                        intent.putExtra(APPOINTMENT_ID, appointment_id);
                        intent.putExtra(PATIENT_ID, chatUser);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("full", templateModel.getFullJson());
                        intent.putExtra("doctorInfo", templateModel.getDoctorInfo());
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "You can't access Rx for this time try again later", Toast.LENGTH_SHORT).show();
                    }
                }, Throwable::printStackTrace));

    }

    @Override
    public void Confirm(boolean b) {
        RatePatientDialogFragment ratePatientDialogFragment = new RatePatientDialogFragment(this, appointment_id, currentUserId, chatUser, chatUserProfilePicture, rating, confirm -> {
        });
        ratePatientDialogFragment.show(getSupportFragmentManager(), ratePatientDialogFragment.getTag());
    }

    @Override
    public void FUDismiss() {

    }

    @Override
    public void Confirmation() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public void Confirmation(boolean confirm) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}
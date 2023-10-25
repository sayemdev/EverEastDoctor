package evereast.co.doctor.login;

import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.permissionx.guolindev.PermissionX;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import evereast.co.doctor.Activity.HomeActivity;
import evereast.co.doctor.R;
import evereast.co.doctor.chat.ChatActivity;
import evereast.co.doctor.databinding.ActivityTestAudioCallBinding;
import evereast.co.doctor.express.AppCenter;
import evereast.co.doctor.express.AudioFloatingWindowService;
import evereast.co.doctor.express.ExpressManager;
import evereast.co.doctor.express.ZegoDeviceUpdateType;
import evereast.co.doctor.express.ZegoMediaOptions;
import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.constants.ZegoViewMode;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoUser;

public class AudioCallActivity extends AppCompatActivity {

    private static final String TAG = "AudioCallActivity";
    private final Handler handler = new Handler();
    ActivityTestAudioCallBinding binding;
    boolean isOpenAble = false;
    Date callStartedAt;
    private Runnable runnable;

    String senderName="",senderId="",appointmentId="";
    @Override
    public void finish() {
        super.finish();
        if (senderName!=null|senderId!=null|appointmentId!=null) {
            if (!senderName.isEmpty()|!senderId.isEmpty()|!appointmentId.isEmpty()) {
                Intent intent = new Intent(AudioCallActivity.this, ChatActivity.class);
                intent.putExtra("senderId", senderId);
                intent.putExtra("sender_name", senderId);
                intent.putExtra("appointment_id", appointmentId);
                startActivity(intent);
            }else{
                startActivity(new Intent(this,HomeActivity.class));
            }
        }else{
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestAudioCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("end_call")) {
                    LeaveRoom();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("end_call"));

        binding.remoteName.setText(getIntent().getStringExtra("name"));
        ExpressManager.getInstance().setExpressHandler(new ExpressManager.ExpressManagerHandler() {
            @Override
            public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
                Log.d(TAG, "onRoomUserUpdate: " + updateType + " roomId " + roomID + " User List " + userList);
                if (updateType == ZegoUpdateType.ADD) {
                    for (int i = 0; i < userList.size(); i++) {
                        ZegoUser user = userList.get(i);
                        TextureView remoteTexture = binding.remoteTexture;
                        binding.remoteName.setText(user.userName);
                        setRemoteViewVisible(true);
                        ExpressManager.getInstance().setRemoteVideoView(user.userID, remoteTexture);
                        ExpressManager.getInstance().setLocalVideoView(binding.localTexture);
                        isOpenAble = true;

                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            Date date = new Date();
                            String dateTime = format.format(date);
                            getSharedPreferences("CALL", MODE_PRIVATE).edit().putString("STARTED_AT", dateTime).apply();
                            String dtStart = getSharedPreferences("CALL", MODE_PRIVATE).getString("STARTED_AT", dateTime);
                            callStartedAt = format.parse(dtStart);
                            runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        handler.postDelayed(this, 1000);
                                        Date current_date = new Date();
                                        long diff = current_date.getTime() - callStartedAt.getTime();
                                        long Minutes = diff / (60 * 1000) % 60;
                                        long Seconds = diff / 1000 % 60;

                                        binding.timeTV.setText(String.format("%02d:%02d", Minutes, Seconds));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            handler.postDelayed(runnable, 0);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                } else if (updateType == ZegoUpdateType.DELETE) {
                    for (int i = 0; i < userList.size(); i++) {
                        ZegoUser user = userList.get(i);
                        TextureView remoteTexture = binding.remoteTexture;
                        binding.remoteName.setText(user.userName);
                        setRemoteViewVisible(true);
                        ExpressManager.getInstance().setRemoteVideoView(user.userID, remoteTexture);
                        ExpressManager.getInstance().setLocalVideoView(binding.localTexture);
                    }
                    LeaveRoom();
                } else {
                    setRemoteViewVisible(false);
                }
            }

            @Override
            public void onRoomUserDeviceUpdate(ZegoDeviceUpdateType updateType, String userID, String roomID) {
                Log.d(TAG,
                        "onRoomUserDeviceUpdate() called with: updateType = [" + updateType + "], userID = [" + userID
                                + "], roomID = [" + roomID + "]");
                if (updateType == ZegoDeviceUpdateType.cameraOpen) {
                    setRemoteViewVisible(true);
                } else if (updateType == ZegoDeviceUpdateType.cameraClose) {
                    setRemoteViewVisible(false);
                }
            }

            @Override
            public void onRoomTokenWillExpire(String roomID, int remainTimeInSecond) {

            }
        });

        binding.micBtn.setSelected(true);
        binding.micBtn.setOnClickListener(v -> {
            boolean selected = v.isSelected();
            if (selected) {
                v.setSelected(false);
                ExpressManager.getInstance().enableMic(false);
                binding.micBtn.setBackgroundResource(R.drawable.circle_white);
            } else {
                v.setSelected(true);
                ExpressManager.getInstance().enableMic(true);
                binding.micBtn.setBackgroundResource(R.drawable.circle_darker);
            }
        });
        binding.speakerButton.setSelected(true);
        binding.speakerButton.setOnClickListener(v -> {
            boolean selected = v.isSelected();
            if (selected) {
                v.setSelected(false);
                ExpressManager.getInstance().enableSpeaker(false);
                binding.speakerButton.setBackgroundResource(R.drawable.circle_white);
            } else {
                v.setSelected(true);
                ExpressManager.getInstance().enableSpeaker(true);
                binding.speakerButton.setBackgroundResource(R.drawable.circle_darker);
            }
        });
        binding.endRoomButton.setOnClickListener(v -> {
            LeaveRoom();
        });
//        }

        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .request((allGranted, grantedList, deniedList) -> {
                });
    }

    private void LeaveRoom() {
        isOpenAble = false;
        ExpressManager.getInstance().leaveRoom();
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Call").child(getIntent().getStringExtra("appointmentId")).child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "")).child("info");
            Map<String, String> map = new HashMap<>();
            map.put("call_state", "10");//10==>> left
            map.put("state_changed_on", System.currentTimeMillis() + "");
            databaseReference.setValue(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RejectCall();
        finish();
        stopService();
        getSharedPreferences("CALL", MODE_PRIVATE).edit().clear().apply();
    }

    private void RejectCall() {
        if (getSharedPreferences("TEMP", MODE_PRIVATE).getBoolean("user_added", false)) {
            Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, "http://clinicure.co/android/call_fcm.php", response -> {
                Log.d(TAG, "RejectCall: " + response);
            }, error -> {
                error.printStackTrace();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    String token = getSharedPreferences("TEMP", MODE_PRIVATE).getString("token", "");
                    String apid = getSharedPreferences("TEMP", MODE_PRIVATE).getString("apointmentId", "");
                    map.put("token", token);
                    map.put("appointmentId", apid);
                    map.put("type", "remove");
                    return map;
                }
            });
        }
        getSharedPreferences("TEMP", MODE_PRIVATE).edit().clear().apply();
    }

    void startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(AudioCallActivity.this,
                        AudioFloatingWindowService.class));
            } else {
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent1, 1);
            }
        } else {
            startService(new Intent(AudioCallActivity.this, AudioFloatingWindowService.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService();
        if (getSharedPreferences("CALL", MODE_PRIVATE).getBoolean("roomStarted", false)) {
            startService();
        }
    }

    private ZegoCanvas generateCanvas(TextureView textureView) {
        ZegoCanvas canvas = new ZegoCanvas(textureView);
        canvas.viewMode = ZegoViewMode.ASPECT_FILL;
        return canvas;
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, AudioFloatingWindowService.class));
        binding.remoteTexture.setOpaque(false);
        binding.localTexture.setOpaque(false);

        SharedPreferences.Editor editor = getSharedPreferences("TEMP", MODE_PRIVATE).edit();
        editor.putBoolean("user_added", true);
        editor.putString("apointmentId", getIntent().getStringExtra("appointmentId"));
        editor.putString("token", getIntent().getStringExtra("token"));
        editor.apply();

        if (!getSharedPreferences("CALL", MODE_PRIVATE).getBoolean("roomStarted", false)) {
            binding.remoteName.setText(getIntent().getStringExtra("patient_name"));
            String participant = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Call").child(getIntent().getStringExtra("roomId"))
                    .child(participant).child("info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String callState = snapshot.child("call_state").getValue(String.class);
                    Log.d(TAG, "onDataChange: " + callState);

                    if (callState.equals("2")) {
                        binding.timeTV.setText("Ringing");
                    } else if (callState.equals("-1")) {
                        binding.timeTV.setText("Call declined");
                        LeaveRoom();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });
            senderId=getIntent().getStringExtra("participant");
            senderName= getIntent().getStringExtra("name");
            appointmentId=getIntent().getStringExtra("roomId");
            joinRoom();
        } else {
            senderId=getSharedPreferences("CALL",MODE_PRIVATE).getString("participant","");
            senderName= getSharedPreferences("CALL",MODE_PRIVATE).getString("name","");
            appointmentId=getSharedPreferences("CALL",MODE_PRIVATE).getString("roomId","");
            ZegoCanvas canvas = generateCanvas(binding.remoteTexture);
            ZegoExpressEngine.getEngine().startPlayingStream(ExpressManager.getInstance().generateStreamID(getSharedPreferences("CALL", MODE_PRIVATE).getString("participant", ""),
                    getSharedPreferences("CALL", MODE_PRIVATE).getString("roomId", "")), canvas);
            binding.remoteName.setText(getSharedPreferences("CALL", MODE_PRIVATE).getString("name", ""));
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
                                Date current_date = new Date();
                                long diff = current_date.getTime() - callStartedAt.getTime();
                                long Minutes = diff / (60 * 1000) % 60;
                                long Seconds = diff / 1000 % 60;

                                binding.timeTV.setText(String.format("%02d:%02d", Minutes, Seconds));

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

            if (getSharedPreferences("CALL", MODE_PRIVATE).getBoolean("isVideo", false)) {
                binding.remoteTexture.setVisibility(View.VISIBLE);
                binding.localTexture.setVisibility(View.VISIBLE);
                binding.remoteName.setVisibility(View.GONE);
                binding.timeTV.setVisibility(View.GONE);
                binding.cameraBtn.setBackgroundResource(R.drawable.circle_white);
                binding.cameraBtn.setSelected(true);
                ExpressManager.getInstance().setRemoteVideoView(getSharedPreferences("CALL", MODE_PRIVATE).getString("participant", ""), binding.remoteTexture);
                ExpressManager.getInstance().setLocalVideoView(binding.localTexture);
            } else {
                binding.remoteTexture.setVisibility(View.GONE);
                binding.localTexture.setVisibility(View.GONE);
                binding.remoteName.setVisibility(View.VISIBLE);
                binding.timeTV.setVisibility(View.VISIBLE);
                binding.cameraBtn.setBackgroundResource(R.drawable.circle_darker);
                binding.cameraBtn.setSelected(false);
            }
        }
    }

    private void joinRoom() {
        String userID = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "");
        String userName = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_NAME, "");
        ZegoUser user = new ZegoUser(userID, userName);
        String token = ExpressManager.generateToken(userID, AppCenter.appID, AppCenter.serverSecret);
        int mediaOptions = ZegoMediaOptions.autoPlayAudio |
                ZegoMediaOptions.publishLocalAudio;
        ExpressManager.getInstance().joinRoom(getIntent().getStringExtra("roomId"), user, token, mediaOptions, (errorCode, jsonObject) -> {
            if (errorCode == 0) {
                Log.d(TAG, "joinRoom: Started");
                SharedPreferences.Editor editor = getSharedPreferences("CALL", MODE_PRIVATE).edit();
                editor.putBoolean("roomStarted", true);
                editor.putString("participant", getIntent().getStringExtra("participant"));
                editor.putString("myId", userID);
                editor.putString("name", getIntent().getStringExtra("name"));
                editor.putString("roomId", getIntent().getStringExtra("roomId"));
                editor.putBoolean("video", false);
                editor.apply();
                binding.timeTV.setText("Calling");
                ExpressManager.getInstance().enableSpeaker(true);
            } else {
                Toast.makeText(getApplication(), "join room failed,errorCode :" + errorCode, Toast.LENGTH_LONG).show();
                Log.d(TAG, "joinRoom: join room failed,errorCode :" + errorCode + " json " + jsonObject);
            }
        });
    }

    private void setRemoteViewVisible(boolean visible) {
        /*if (visible){
            binding.remoteTexture.setVisibility(View.VISIBLE);
            ExpressManager.getInstance().setRemoteVideoView(getSharedPreferences("CALL",MODE_PRIVATE).getString("participant",""),binding.remoteTexture);
        }else{
            binding.remoteTexture.setVisibility(View.GONE);
        }*/
    }

    public void stopService() {
        stopService(new Intent(this, AudioFloatingWindowService.class));
    }


}
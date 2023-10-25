package evereast.co.doctor.login;

import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;

import android.Manifest;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import evereast.co.doctor.R;
import evereast.co.doctor.databinding.ActivityTestVideoCallBinding;
import evereast.co.doctor.express.AppCenter;
import evereast.co.doctor.express.ExpressManager;
import evereast.co.doctor.express.FloatingWindowService;
import evereast.co.doctor.express.ZegoDeviceUpdateType;
import evereast.co.doctor.express.ZegoMediaOptions;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.entity.ZegoUser;

public class VideoCallActivity extends AppCompatActivity {

    private static final String TAG = "VideoCallActivity";
    ActivityTestVideoCallBinding binding;
    boolean isOpenAble = false;
    boolean isInPictureInPictureMode=false;
    boolean isCamera = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestVideoCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

        binding.localView.setVisibility(View.GONE);
        ExpressManager.getInstance().setLocalVideoView(binding.remoteTexture);

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
                        binding.localView.setVisibility(View.VISIBLE);
                        binding.localTexture.setVisibility(View.VISIBLE);
                        ExpressManager.getInstance().setRemoteVideoView(user.userID, remoteTexture);
                        ExpressManager.getInstance().setLocalVideoView(binding.localTexture);
                        isOpenAble = true;
                    }
                    getSharedPreferences("CALL", MODE_PRIVATE).edit().putBoolean("USER_ADDED", true).apply();
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
                if (userID.equals(getSharedPreferences("CALL", MODE_PRIVATE).getString("participant", ""))) {
                    if (updateType == ZegoDeviceUpdateType.cameraOpen) {
                        setRemoteViewVisible2(true);
                            binding.closeCameraText.setVisibility(View.GONE);
                        isCamera = true;
                    } else if (updateType == ZegoDeviceUpdateType.cameraClose) {
                        setRemoteViewVisible2(false);
                        if (!isInPictureInPictureMode)
                            binding.closeCameraText.setVisibility(View.VISIBLE);
                        else
                            binding.closeCameraText.setVisibility(View.GONE);
                        isCamera = false;
                    }
                }
                Log.d(TAG, "onRoomUserDeviceUpdate: ");
            }

            @Override
            public void onRoomTokenWillExpire(String roomID, int remainTimeInSecond) {

            }
        });
        binding.remoteTexture.setOpaque(false);
        binding.localTexture.setOpaque(false);
        binding.localTextureMini.setOpaque(false);


        binding.switchBtn.setSelected(true);
        binding.switchBtn.setOnClickListener(v -> {
            boolean selected = v.isSelected();
            if (selected) {
                v.setSelected(false);
                ExpressManager.getInstance().switchFrontCamera(false);
            } else {
                v.setSelected(true);
                ExpressManager.getInstance().switchFrontCamera(true);
            }
        });

        binding.cameraBtn.setSelected(true);
        binding.cameraBtn.setOnClickListener(v -> {
            boolean selected = v.isSelected();
            if (selected) {
                v.setSelected(false);
                ExpressManager.getInstance().enableCamera(false);
                binding.cameraBtn.setBackgroundResource(R.drawable.circle_white);
            } else {
                v.setSelected(true);
                ExpressManager.getInstance().enableCamera(true);
                binding.cameraBtn.setBackgroundResource(R.drawable.circle_darker);
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
        binding.logoutRoom.setOnClickListener(v -> {
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
        finish();
        stopService();
        getSharedPreferences("CALL", MODE_PRIVATE).edit().clear().apply();
    }

    void startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            } else {
                startService(new Intent(VideoCallActivity.this,
                        FloatingWindowService.class));
            }
        } else {
            startService(new Intent(VideoCallActivity.this,
                    FloatingWindowService.class));
        }
        EnterPictureInPictureMode();
    }

    private void setRemoteViewVisible2(boolean enabled) {
        binding.videoToggleLT.setVisibility(!enabled ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isInPictureInPictureMode)
            finish();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        this.isInPictureInPictureMode = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            binding.videoController.setVisibility(View.GONE);
            binding.logoutRoom.setVisibility(View.GONE);
            binding.localTextureMini.setVisibility(View.VISIBLE);
            ExpressManager.getInstance().setLocalVideoView(binding.localTextureMini);
            binding.localView.setVisibility(View.GONE);
            binding.close.setVisibility(View.GONE);
            binding.closeMINI.setVisibility(View.VISIBLE);
            binding.closeCameraText.setVisibility(View.GONE);
        } else {
            binding.videoController.setVisibility(View.VISIBLE);
            binding.logoutRoom.setVisibility(View.VISIBLE);
            binding.localTextureMini.setVisibility(View.GONE);
            binding.localView.setVisibility(View.VISIBLE);
            ExpressManager.getInstance().setLocalVideoView(binding.localTexture);
            binding.close.setVisibility(View.VISIBLE);
            binding.closeMINI.setVisibility(View.GONE);
            if (!isCamera)
                binding.closeCameraText.setVisibility(View.VISIBLE);
            else {
                binding.closeCameraText.setVisibility(View.GONE);
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, FloatingWindowService.class));
        binding.remoteTexture.setOpaque(false);
        binding.localTexture.setOpaque(false);
        binding.localTextureMini.setOpaque(false);
        if (!getSharedPreferences("CALL", MODE_PRIVATE).getBoolean("roomStarted", false)) {
            binding.localView.setVisibility(View.GONE);
            joinRoom();
        } else {
            ExpressManager.getInstance().setRemoteVideoView(getSharedPreferences("CALL", MODE_PRIVATE).getString("participant", ""), binding.remoteTexture);
            ExpressManager.getInstance().setLocalVideoView(binding.localTexture);
            if (getSharedPreferences("CALL", MODE_PRIVATE).getBoolean("USER_ADDED", false)) {
                binding.localView.setVisibility(View.VISIBLE);
            }

        }
    }

    private void joinRoom() {
        String userID = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "");
        String userName = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_NAME, "");
        ZegoUser user = new ZegoUser(userID, userName);
        String token = ExpressManager.generateToken(userID, AppCenter.appID, AppCenter.serverSecret);
        int mediaOptions = ZegoMediaOptions.autoPlayAudio | ZegoMediaOptions.autoPlayVideo |
                ZegoMediaOptions.publishLocalAudio | ZegoMediaOptions.publishLocalVideo;
        ExpressManager.getInstance().joinRoom(getIntent().getStringExtra("roomId"), user, token, mediaOptions, (errorCode, jsonObject) -> {
            if (errorCode == 0) {
                Log.d(TAG, "joinRoom: Started");
                SharedPreferences.Editor editor = getSharedPreferences("CALL", MODE_PRIVATE).edit();
                editor.putBoolean("roomStarted", true);
                editor.putString("participant", getIntent().getStringExtra("participant"));
                editor.putString("myId", userID);
//                editor.putBoolean("isVideo", true);
                editor.putString("name", getIntent().getStringExtra("name"));
                editor.putString("roomId", getIntent().getStringExtra("roomId"));
                editor.putBoolean("video", true);
                editor.apply();
                ExpressManager.getInstance().enableSpeaker(false);
                ExpressManager.getInstance().setLocalVideoView(binding.remoteTexture);
                binding.localView.setVisibility(View.GONE);
                binding.localTexture.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplication(), "join room failed,errorCode :" + errorCode, Toast.LENGTH_LONG).show();
                Log.d(TAG, "joinRoom: join room failed,errorCode :" + errorCode + " json " + jsonObject);
            }
        });
    }

    private void setRemoteViewVisible(boolean visible) {
        binding.remoteTexture.setVisibility(View.VISIBLE);
    }

    public void stopService() {
        stopService(new Intent(this, FloatingWindowService.class));
    }

    public void EnterPictureInPictureMode() {
        if (!isInPictureInPictureMode)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Display d = getWindowManager()
                        .getDefaultDisplay();
                Point p = new Point();
                d.getSize(p);
                int width = p.x;
                int height = p.y;

                Rational ratio
                        = new Rational(width, height);
                PictureInPictureParams.Builder
                        pip_Builder
                        = new PictureInPictureParams
                        .Builder();
                pip_Builder.setAspectRatio(ratio).build();
                enterPictureInPictureMode(pip_Builder.build());
            }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        stopService();
        if (getSharedPreferences("CALL", MODE_PRIVATE).getBoolean("roomStarted", false)) {
            startService();
            EnterPictureInPictureMode();
        }
    }
}
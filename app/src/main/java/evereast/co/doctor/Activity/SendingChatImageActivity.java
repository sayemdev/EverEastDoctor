package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.MAIN_URL;
import static evereast.co.doctor.Constants.MY_TOKEN;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;
import static evereast.co.doctor.Constants.imageToString;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import evereast.co.doctor.R;
import evereast.co.doctor.Utils;
import evereast.co.doctor.databinding.ActivitySendingChatImageBinding;

public class SendingChatImageActivity extends AppCompatActivity {
    private static final String TAG = "SendingChatImageActivity";
    ActivitySendingChatImageBinding binding;
    Bitmap bmp;
    Uri imageUri;
    private String myToken, currentUserId;
    private String chatUser;
    private String chatUserToken;
    private String user_name;
    private String appointmentId;
    private DatabaseReference rootReference;
    private String myName;

    private void AppBarWork() {
        TextView titleBar = findViewById(R.id.title_app);
        ImageView back = findViewById(R.id.back);
        titleBar.setText("Send Image");
        back.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendingChatImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: Track" + getIntent().getStringExtra("sender_name"));

        AppBarWork();
        chatUser = getIntent().getStringExtra("senderId");
        chatUserToken = getIntent().getStringExtra("sender_token");
        user_name = getIntent().getStringExtra("sender_name");
        appointmentId = getIntent().getStringExtra("appointment_id");

        if (getIntent().getBooleanExtra("isCamera", false)) {
            ImagePicker.with(this).cameraOnly().start();
        } else {
            ImagePicker.with(this).galleryOnly().start();
        }

        rootReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        currentUserId = sharedPreferences.getString(USER_ID, "Anonymous");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, " "));
        myName = sharedPreferences.getString(USER_NAME, "Un named");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(MY_TOKEN).exists()) {
                    myToken = snapshot.child(MY_TOKEN).getValue(String.class);
                } else {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                        if (task.isComplete()) {
                            myToken = task.getResult();
                            databaseReference.setValue(MY_TOKEN, myToken);
                        } else {
                            Utils.redirectWithFinish(SendingChatImageActivity.this, HomeActivity.class);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.redirectWithFinish(SendingChatImageActivity.this, HomeActivity.class);
            }
        });


    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    binding.previewImageView.setImageURI(data.getData());
                    imageUri = data.getData();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        }
    }

    public void Send(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sending image");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        try {
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            String imgName = System.currentTimeMillis() + ".jpg";
            Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, MAIN_URL + "save_image.php", response -> {
                progressDialog.dismiss();
                if (response.equals("Uploaded")) {
                    sendMessage("image", imgName);
                    finish();
                } else {
                    finish();
                }
            }, error -> {
                progressDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(this, "Failed to uploading image", Toast.LENGTH_SHORT).show();
                finish();
            }) {
                @Nullable
                @org.jetbrains.annotations.Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("image_name", imgName);
                    map.put("profile_string", imageToString(selectedImage));
                    return map;
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            finish();
            Toast.makeText(this, "Image sending failed", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("LongLogTag")
    private void sendMessage(String type, String imageUrl) {
        {
            final String message = imageUrl;
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
                    messageUSerMap.put(current_user_chat_ref + "/timeStamp", timeStamp1);
                    messageUSerMap.put(chat_user_chat_ref + "/lastMessage", message);
                    messageUSerMap.put(chat_user_chat_ref + "/timeStamp", timeStamp1);
                    messageUSerMap.put(current_user_ref + "/" + push_id, messageMap);
                    messageUSerMap.put(chat_user_ref + "/" + push_id, messageMap);

                    rootReference.updateChildren(messageUSerMap, (error, ref) -> {
                        if (error != null) {
                            Log.e("MESSAGE_LOG", "onComplete: " + error.getMessage());
                        }
                    });
                    Volley.newRequestQueue(SendingChatImageActivity.this).add(new StringRequest(Request.Method.POST, MAIN_URL + "chatfcm.php",
                            response -> {
                                Log.i(TAG, "onDataChange: " + response);
                            },
                            error -> {
                                error.printStackTrace();
                            }) {
                        @SuppressLint("LongLogTag")
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> maps = new HashMap<>();
                            maps.put("sender_token", myToken);
                            maps.put("token", chatUserToken);
                            maps.put("d_name", myName);
                            maps.put("text", "📷 Sent an Image");
                            maps.put("chat_user_id", chatUser);
                            maps.put("sender", currentUserId);
                            maps.put("sender_name", myName);
                            maps.put("appointment_id", appointmentId);
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

    }

}
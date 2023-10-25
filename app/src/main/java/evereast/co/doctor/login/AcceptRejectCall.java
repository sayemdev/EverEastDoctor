package evereast.co.doctor.login;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import evereast.co.doctor.databinding.ActivityAcceptRejectBinding;

public class AcceptRejectCall extends AppCompatActivity {

    ActivityAcceptRejectBinding binding;

    String roomId, receiverId, callerId, appointmentId, body, name, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAcceptRejectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        roomId = intent.getStringExtra("roomId");
        receiverId = intent.getStringExtra("recieverId");
        callerId = intent.getStringExtra("callerId");
        appointmentId = intent.getStringExtra("appointmentId");
        body = intent.getStringExtra("body");
        name = intent.getStringExtra("name");
        type = intent.getStringExtra("type");

        binding.nameTV.setText(name);

        binding.acceptCall.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, VideoCallActivity.class);
            intent1.putExtra("rcv", true);
            intent1.putExtra("roomId", intent.getStringExtra("roomId"));
            intent1.putExtra("recieverId", intent.getStringExtra("recieverId"));
            intent1.putExtra("callerId", intent.getStringExtra("callerId"));
            intent1.putExtra("appointmentId", intent.getStringExtra("appointmentId"));
            intent1.putExtra("body", intent.getStringExtra("body"));
            intent1.putExtra("name", intent.getStringExtra("name"));
            intent1.putExtra("type", intent.getStringExtra("type"));
            startActivity(intent1);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.parseInt(intent.getStringExtra("id")));
            finish();
        });

        binding.rejectCall.setOnClickListener(v -> {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.parseInt(intent.getStringExtra("id")));
            finish();
        });

    }
}
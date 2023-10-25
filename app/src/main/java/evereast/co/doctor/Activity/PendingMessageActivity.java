package evereast.co.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import evereast.co.doctor.R;

public class PendingMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_message);
    }

    public void Close(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
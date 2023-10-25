package evereast.co.doctor.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import evereast.co.doctor.R;

public class MissedAppointmentActivity extends AppCompatActivity {
    SwipeRefreshLayout srl;
    LinearLayout doctorNotFoundLayout;
    RelativeLayout wholeThing;

    private void AppBarWork() {
        TextView titleBar = findViewById(R.id.title_app);
        ImageView back = findViewById(R.id.back);
        titleBar.setText("Missed Appointments");
        back.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_appointment);

        AppBarWork();

        srl = findViewById(R.id.srl);
        doctorNotFoundLayout = findViewById(R.id.doctorNotFound);
        wholeThing = findViewById(R.id.wholeThing);

    }
}
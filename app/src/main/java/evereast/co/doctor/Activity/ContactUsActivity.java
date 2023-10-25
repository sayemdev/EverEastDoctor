package evereast.co.doctor.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import evereast.co.doctor.R;
import evereast.co.doctor.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends AppCompatActivity {
    ActivityContactUsBinding binding;

    private void AppBarWork() {
        TextView titleBar = findViewById(R.id.title_app);
        ImageView back = findViewById(R.id.back);
        titleBar.setText("Contact Us");
        back.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppBarWork();
        binding.facebookLT.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/364838267494393"));
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/364838267494393/")));
            }
        });

        binding.messengerLT.setOnClickListener(view -> {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setPackage("com.facebook.orca");
                intent.setData(Uri.parse("https://m.me/364838267494393"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://m.me/364838267494393"));
                startActivity(intent);
            }
        });

        binding.whatsappLT.setOnClickListener(view -> {
            String url = "https://api.whatsapp.com/send?phone=+8801311040092";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        binding.phoneLT.setOnClickListener(view -> {
            String phone = "+8809678221190";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }
}
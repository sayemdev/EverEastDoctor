package evereast.co.doctor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import evereast.co.doctor.R;

public class ViewImageActivity extends AppCompatActivity {

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        Glide.with(this).load(getIntent().getStringExtra("link")).placeholder(R.drawable.round_health).error(R.drawable.round_health).into(photoView);

    }
}
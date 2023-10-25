package evereast.co.doctor.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.chip.ChipGroup;

import evereast.co.doctor.R;
import evereast.co.doctor.databinding.ActivityHistoryApBinding;

public class HistoryApActivity extends AppCompatActivity {
    NavController navController;
    NavHostFragment navHostFragment;
    ActivityHistoryApBinding binding;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryApBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBar.titleApp.setText("Appointments");
        binding.appBar.back.setOnClickListener(view -> finish());

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        chipGroup = findViewById(R.id.filterChipGroup);

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.allId) {

            } else if (checkedId == R.id.upcomingId) {

            } else if (checkedId == R.id.completedId) {

            }
        });

        binding.completedId.setOnClickListener(v -> {
            navController.navigate(R.id.completedApFragment);
        });

        binding.allId.setOnClickListener(v -> {
            navController.navigate(R.id.allApFragment);
        });

        binding.upcomingId.setOnClickListener(v -> {
            navController.navigate(R.id.upcomingApFragment);
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.DOMAIN_URL;
import static evereast.co.doctor.Constants.IS_LOGGED_IN;
import static evereast.co.doctor.Constants.PATIENT_URL;
import static evereast.co.doctor.Constants.PHONE;
import static evereast.co.doctor.Constants.PROFILE_FILES;
import static evereast.co.doctor.Constants.PROFILE_PREFERENCE;
import static evereast.co.doctor.Constants.PROFILE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_INFO;
import static evereast.co.doctor.Constants.USER_NAME;
import static evereast.co.doctor.Utils.CheckPermissionPoped;
import static evereast.co.doctor.Utils.PERMISSION_IS_POPED;
import static evereast.co.doctor.Utils.closeDrawer;
import static evereast.co.doctor.Utils.openDrawer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import evereast.co.doctor.DialogFragment.CreateNewOrRead;
import evereast.co.doctor.DialogFragment.ExitDialogFragment;
import evereast.co.doctor.DialogFragment.RequestPaymentDialogFragment;
import evereast.co.doctor.DialogFragment.RequestPermissionDialog;
import evereast.co.doctor.Drug.DrugDirectoryActivity;
import evereast.co.doctor.Fragments.BillingFragment;
import evereast.co.doctor.Fragments.HomeAppointmentFragment;
import evereast.co.doctor.Fragments.InboxFragment;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.PPActivity;
import evereast.co.doctor.UserInfo.UserInfo;
import evereast.co.doctor.Utils;
import evereast.co.doctor.databinding.ActivityHomeBinding;

public class
HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, CreateNewOrRead.CreateNewOrReadAirPrescription, RequestPermissionDialog.ConfirmationValueListener, RequestPaymentDialogFragment.ConfirmationValueListener, ExitDialogFragment.ClickListener {

    public static final String DATA_RECEIVE = "DATA_RECEIVE";
    private static final String TAG = "HomeActivity";
    DrawerLayout drawerLayout;
    TextView nameTextView, phoneTextView, editProfileButton;
    ImageView endToggle, closeDrawer;
    CircularImageView profileImageView;
    String userId, userName, userPhone, profile_url;
    SharedPreferences sharedPreferences, profilePreferences;
    RelativeLayout drugDirButton, historyButton, logoutButton, airPrescriptionButton, contactUsButton, termsAnConditionButton;
    FrameLayout frameLayout;
    HomeAppointmentFragment appointmentFragment;
    BottomNavigationView bottomNavigationView;
    LinearLayout profileLayout;
    TextView viewProfile;
    String route;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
    };
    int PERMISSION_ALL = 1;
    Type userType;
    UserInfo userInfo;
    /*, skinCareButton, medicineButton, pediatricsButton, gynaecologyButton, psychiatryButton, sexCureButton, nutritionButton*/
    ActivityHomeBinding binding;
    private TextView ratingTView;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean isLoggedIn = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getBoolean(IS_LOGGED_IN, false);
        if (!isLoggedIn) {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        }
        CheckPermission();

//        AutoStart.getInstance().getAutoStartPermission(this);

        appointmentFragment = new HomeAppointmentFragment();

        sharedPreferences = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        profilePreferences = getSharedPreferences(PROFILE_PREFERENCE, MODE_PRIVATE);

        userType = new TypeToken<UserInfo>() {
        }.getType();
//        userInfo = new Gson().fromJson(sharedPreferences.getString(USER_INFO, "[]"), userType);

        route = getIntent().getStringExtra("route");

        userName = sharedPreferences.getString(USER_NAME, "");
        userPhone = sharedPreferences.getString(PHONE, "");
        userId = sharedPreferences.getString(USER_ID, "");
        profile_url = PROFILE_FILES + sharedPreferences.getString(PROFILE_URL, "null");

        drawerLayout = findViewById(R.id.drawerLout);
        endToggle = findViewById(R.id.endToggle);
        profileImageView = findViewById(R.id.profileImage);
        nameTextView = findViewById(R.id.nameTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        logoutButton = findViewById(R.id.logoutId);
        closeDrawer = findViewById(R.id.closeDrawer);
        historyButton = findViewById(R.id.historyLtId);
        drugDirButton = findViewById(R.id.documentLtId);
        editProfileButton = findViewById(R.id.editProfile);
        airPrescriptionButton = findViewById(R.id.airPrescriptionLtId);
        frameLayout = findViewById(R.id.bottom_nav_frame_layout);
        nameTextView.setText(userName);
        phoneTextView.setText(userPhone);
        profileLayout = findViewById(R.id.profileLt);
        ratingTView = findViewById(R.id.ratingTV);
        contactUsButton = findViewById(R.id.contactUsLtId);
        termsAnConditionButton = findViewById(R.id.termsLtId);
        viewProfile = findViewById(R.id.viewProfile);


        contactUsButton.setOnClickListener(v -> {
//            https://www.healthmen.com.bd/contact/
            Intent intent = new Intent(this, ContactUsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        termsAnConditionButton.setOnClickListener(v -> {
//            https://www.healthmen.com.bd/contact/
            String url = DOMAIN_URL + "tc";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        viewProfile.setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            startActivity(new Intent(this, ViewProfileActivity.class));
        });

        try {
            Glide.with(HomeActivity.this).load(profile_url).placeholder(R.drawable.ic_profile).into(profileImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endToggle.setOnClickListener(v -> {
            openDrawer(drawerLayout);
        });
        closeDrawer.setOnClickListener(v -> closeDrawer(drawerLayout));

        logoutButton.setOnClickListener(v -> Utils.Logout(this));
        historyButton.setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            Intent intent = new Intent(this, HistoryApActivity.class);
            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        drugDirButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DrugDirectoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            closeDrawer(drawerLayout);
        });
        editProfileButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, EditProfileActivity.class));
        });

        airPrescriptionButton.setOnClickListener(v -> {
            CreateNewOrRead createNewOrRead = new CreateNewOrRead();
            createNewOrRead.show(getSupportFragmentManager(), createNewOrRead.getTag());
            closeDrawer(drawerLayout);
        });

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeMenuId);

        profileLayout.setOnClickListener(v -> {
//            startActivity(new Intent(this, ProfileActivity.class));
            closeDrawer(drawerLayout);
        });


        try {
            if (getIntent().getStringExtra("type").equals("appointment")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_frame_layout, new InboxFragment(route)).commit();
                bottomNavigationView.setSelectedItemId(R.id.inboxMenuId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_frame_layout, appointmentFragment).commit();
        }


        String rating_url = PATIENT_URL + "get_doctor_by_id.php?doctor_id=" + userId;
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, rating_url, response -> {
            Log.d(TAG, "onCreate: " + rating_url);

            try {
                JSONObject object = new JSONObject(response);
                ratingTView.setText(object.getJSONArray("doctor").getJSONObject(0).getString("rate"));
                SharedPreferences.Editor editor = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).edit();
                editor.putString(USER_INFO, response);
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }));

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {
                String rating_url = PATIENT_URL + "get_doctor_by_id.php?doctor_id=" + userId;
                Volley.newRequestQueue(HomeActivity.this).add(new StringRequest(Request.Method.GET, rating_url, response -> {
                    Log.d(TAG, "onCreate: " + rating_url);
                    try {
                        JSONObject object = new JSONObject(response);
                        ratingTView.setText(object.getJSONArray("doctor").getJSONObject(0).getString("rate"));
                        SharedPreferences.Editor editor = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).edit();
                        editor.putString(USER_INFO, response);
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    error.printStackTrace();
                }));
            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                Log.d(TAG, "CheckPermission: Should Rationale false");
            } else if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d(TAG, "CheckPermission: Should Rationale false");
            } else if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Log.d(TAG, "CheckPermission: Should Rationale false");
            } else {

            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isLoggedIn = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getBoolean(IS_LOGGED_IN, false);
        if (!isLoggedIn) {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        } else {
//            callAuthentication();
            if (!CheckPermissionPoped(this)) {
                if (!hasPermissions(this, PERMISSIONS)) {
                    RequestPermissionDialog permissionDialog = new RequestPermissionDialog();
                    permissionDialog.show(getSupportFragmentManager(), permissionDialog.getTag());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            Utils.closeDrawer(drawerLayout);
            ExitDialogFragment fragment = new ExitDialogFragment(this);
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homeMenuId) {
            binding.titleBar.setText("Pending Appointments");
            getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_frame_layout, appointmentFragment).commit();
        }
        if (item.getItemId() == R.id.inboxMenuId) {
            binding.titleBar.setText("Inbox");
            getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_frame_layout, new InboxFragment(route)).commit();
        }
        if (item.getItemId() == R.id.billingMenuId) {
            binding.titleBar.setText("Billing");
            getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_frame_layout, new BillingFragment()).commit();
        }
        return true;
    }

    @Override
    public void createNew() {
        Intent intent = new Intent(this, PPActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("prescription", "home");
        startActivity(intent);
    }

    @Override
    public void older() {
        Intent intent = new Intent(this, ShowTemplatesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("prescription", "home");
        startActivity(intent);
    }


    @Override
    public void Confirmation(String amount, boolean isOffline) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Applying for payment...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, BASE_URL + "request_payment.php", response -> {
            progressDialog.dismiss();
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            if (response.equals("Requested Successfully")) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.bottom_nav_frame_layout, new HomeAppointmentFragment())
                        .addToBackStack(null)
                        .commit();
                bottomNavigationView.setSelectedItemId(R.id.homeMenuId);
            }
        }, error -> {
            progressDialog.dismiss();
            error.printStackTrace();
            Log.e(TAG, "Confirmation: ", error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("amount", amount);
                map.put("online_offline", isOffline?"1":"0");
                map.put("doctorId", getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, ""));
                return map;
            }
        });
    }

    @Override
    public void Confirmation() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);
        if (requestCode == PERMISSION_ALL) {
            boolean micPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean writeExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            SharedPreferences.Editor editor = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).edit();
            editor.putBoolean(PERMISSION_IS_POPED, true);
            editor.apply();

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

    @Override
    public void CloseApp() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
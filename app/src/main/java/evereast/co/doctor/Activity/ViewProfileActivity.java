package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.ABOUT;
import static evereast.co.doctor.Constants.BMDCNO;
import static evereast.co.doctor.Constants.DEGREE;
import static evereast.co.doctor.Constants.DESIGNATION;
import static evereast.co.doctor.Constants.FEE;
import static evereast.co.doctor.Constants.PROFILE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_INFO;
import static evereast.co.doctor.Constants.USER_NAME;
import static evereast.co.doctor.Utils.UploadProfileImage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.canhub.cropper.CropImageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import evereast.co.doctor.Constants;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.ActivityViewProfileBinding;

public class ViewProfileActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 1211;
    private static final String TAG = "ViewProfileActivity";
    ActivityViewProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        binding.doctorNameTv.setText(sharedPreferences.getString(USER_NAME, " "));
        binding.about.setText(sharedPreferences.getString(ABOUT, " "));

        TextView titleBar = findViewById(R.id.title_app);
        ImageView back = findViewById(R.id.back);
        titleBar.setText(sharedPreferences.getString(USER_NAME, ""));
        back.setOnClickListener(v -> {
            finish();
        });
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString(USER_INFO, ""));
            JSONArray jsonArray1 = jsonObject.optJSONArray("doctor");
            JSONObject object = jsonArray1.getJSONObject(0);

            if (object.getString("d_subcategory").isEmpty()) {
                binding.category.setText(object.getString("d_category"));
            } else {
                binding.category.setText(object.getString("d_subcategory"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            binding.category.setVisibility(View.GONE);
        }
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString(USER_INFO, ""));
            JSONArray jsonArray1 = jsonObject.optJSONArray("doctor");
            JSONObject object = jsonArray1.getJSONObject(0);
            binding.degreeTv.setText(sharedPreferences.getString(DEGREE, " ") + "," + object.getString("d_higher_degree"));
        } catch (JSONException e) {
            e.printStackTrace();
            binding.degreeTv.setText(sharedPreferences.getString(DEGREE, " "));
        }
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString(USER_INFO, ""));
            JSONArray jsonArray1 = jsonObject.optJSONArray("doctor");
            JSONObject object = jsonArray1.getJSONObject(0);
            binding.ratingBar.setRating(Float.parseFloat(object.getString("rate")));
        } catch (JSONException e) {
            e.printStackTrace();
            binding.ratingBar.setRating(0);
        }

        Glide.with(this).load(Constants.PROFILE_FILES + sharedPreferences.getString(PROFILE_URL, "")).placeholder(R.drawable.round_health).into(binding.profileImageView);
        binding.designation.setText(sharedPreferences.getString(DESIGNATION, " "));
        binding.feeTv.setText("Fee:" + sharedPreferences.getString(FEE, " ") + "tk");
        if (sharedPreferences.getString(BMDCNO, "").equals("")) {
            binding.mbdc.setVisibility(View.GONE);
        } else {
            binding.mbdc.setVisibility(View.VISIBLE);
        }

        Log.d(TAG, "onCreate: " + sharedPreferences.getString("work_place", ""));

        if (sharedPreferences.getString("work_place", "").equals("")) {
            binding.worksAt.setVisibility(View.GONE);
        } else {
            binding.worksAt.setVisibility(View.VISIBLE);
        }

        if (sharedPreferences.getString(DESIGNATION, "").equals("")) {
            binding.designation.setVisibility(View.GONE);
        } else {
            binding.designation.setVisibility(View.VISIBLE);
        }

        if (sharedPreferences.getString(ABOUT, "").equals("")) {
            binding.about.setVisibility(View.GONE);
        } else {
            binding.about.setVisibility(View.VISIBLE);
        }

        binding.mbdc.setText(sharedPreferences.getString(BMDCNO, ""));
        binding.worksAt.setText(sharedPreferences.getString("work_place", " "));
       binding.editProfilePicImageview.setOnClickListener(v -> {
         /*    try {
                if (Build.VERSION.SDK_INT > 22) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                    } else {
                  *//*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_FILE_REQUEST);*//*
                        // start picker to get image for cropping and then use the image in cropping activity
                        CropImage.activity()
                                .setAspectRatio(1, 1)
                                .setActivityTitle("Select Image")
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(this);
                    }
                } else {
                    CropImage.activity()
                            .setAspectRatio(1, 1)
                            .setActivityTitle("Select Image")
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(this, "You can't capture image now", Toast.LENGTH_SHORT).show();
            }
        });*/

    });

    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                CircularImageView imageView = findViewById(R.id.profileImageView);
                ContentResolver cr = getContentResolver();
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(resultUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setTitle("Updating profile picture");
                    progressDialog.show();
                    UploadProfileImage(this, getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, " "), selectedImage, progressDialog);

                    Log.d(TAG, "onActivityResult: Camera");
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.failed_to_load), Toast.LENGTH_LONG)
                            .show();
                    e.printStackTrace();
                    Log.e("Camera", e.getMessage());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted :)
//                    downloadFile();
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setActivityTitle("Select Image")
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            } else {
                // permission was not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                        showStoragePermissionRationale();
                    Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar snackbar = Snackbar.make(binding.getRoot(), getString(R.string.message_no_storage_permission_snackbar), Snackbar.LENGTH_LONG);
                    snackbar.setAction(getResources().getString(R.string.settings), v -> {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    });
                    snackbar.show();
                }
            }
        }
    }


}*/
}
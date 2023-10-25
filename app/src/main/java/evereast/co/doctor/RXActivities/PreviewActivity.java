package evereast.co.doctor.RXActivities;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_AD;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_CC;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_DX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_FU;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_HO;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_IX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_PP;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_RX;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.BMDCNO;
import static evereast.co.doctor.Constants.DEGREE;
import static evereast.co.doctor.Constants.DESIGNATION;
import static evereast.co.doctor.Constants.H_DEGREE;
import static evereast.co.doctor.Constants.JOB_EXPERIENCE;
import static evereast.co.doctor.Constants.PATIENT_URL;
import static evereast.co.doctor.Constants.SIGNATURE;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;
import static evereast.co.doctor.Maps.WORK_PLACE;
import static evereast.co.doctor.RXActivities.PPActivity.ADDRESS;
import static evereast.co.doctor.RXActivities.PPActivity.AGE;
import static evereast.co.doctor.RXActivities.PPActivity.AGE_TYPE;
import static evereast.co.doctor.RXActivities.PPActivity.DATE;
import static evereast.co.doctor.RXActivities.PPActivity.GENDER;
import static evereast.co.doctor.RXActivities.PPActivity.ID;
import static evereast.co.doctor.RXActivities.PPActivity.INTENT_TYPE;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_NAME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import evereast.co.doctor.Activity.HomeActivity;
import evereast.co.doctor.DialogFragment.RatePatientDialogFragment;
import evereast.co.doctor.DialogFragment.TemplateTDFragment;
import evereast.co.doctor.R;
import evereast.co.doctor.Utils;
import evereast.co.doctor.databinding.ActivityPreviewBinding;

public class PreviewActivity extends AppCompatActivity implements ShowFUDialogFragment.SelectPhotoValueListener, RatePatientDialogFragment.ConfirmationValueListener, TemplateTDFragment.ConfirmationValueListener {

    public static Bitmap bitScroll;
    ScrollView rootLayout;
    TextView ccShowTV, hoShowTV, ixShowTV, dxShowTV, rxShowTV, adShowTV, fuShowTV, patientNameTV, patientAgeTV, entryDateTV, patientGenderTV, patientAddressTV, prescriptionIDTV;
    Button issueBt;
    String prescriptionID, patientName, patientGender, patientAge, entryDate, doctorId, doctorName, doctorInfo;
    StringBuilder stringBuilderOfCC, stringBuilderOfAD, stringBuilderOfRX, stringBuilderOfDX, stringBuilderOfIX, stringBuilderOfHO;
    String patientAddress;
    StringBuilder doctorInfoBuilder;
    String tamplate = "Template";
    String appointmentId;
    LinearLayout patientInfoLayout, headerViewLayout;
    ImageView signatureView;
    ActivityPreviewBinding binding;
    String rating, patientId, profile;
    private String signatureId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);


        issueBt = findViewById(R.id.issueBT);
        rootLayout = findViewById(R.id.rootLt);
        ccShowTV = findViewById(R.id.ccShowTV);
        hoShowTV = findViewById(R.id.hoShowTV);
        ixShowTV = findViewById(R.id.ixShowTV);
        dxShowTV = findViewById(R.id.dxShowTV);
        rxShowTV = findViewById(R.id.rxShowTV);
        adShowTV = findViewById(R.id.advicesShow);
        patientNameTV = findViewById(R.id.patientNameTv);
        patientAgeTV = findViewById(R.id.pAgeTV);
        entryDateTV = findViewById(R.id.issueDateTV);
        patientGenderTV = findViewById(R.id.patientGenderTV);
        patientAddressTV = findViewById(R.id.pAddressTV);
        prescriptionIDTV = findViewById(R.id.prescriptionId);
        patientInfoLayout = findViewById(R.id.patientInfoLT);
        headerViewLayout = findViewById(R.id.headerView);
        signatureView = findViewById(R.id.signatureView);

        GetPermission();

        SharedPreferences sharedPreferences = getSharedPreferences(AIR_PREFERENCE_CC, MODE_PRIVATE);
        SharedPreferences sharedPreferencesDr = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        signatureId = sharedPreferencesDr.getString(SIGNATURE, "sf");
        Glide.with(this).load(PATIENT_URL + "d_signature/" + signatureId).into(signatureView);
        TextView view = findViewById(R.id.drNameTV);
        view.setText(sharedPreferencesDr.getString(USER_NAME, "DR. Fahad"));
        String ccList = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");
        SharedPreferences sharedPreferencesHO = getSharedPreferences(AIR_PREFERENCE_HO, MODE_PRIVATE);
        String hoList = sharedPreferencesHO.getString(AIR_PREFERENCE_LIST, "");
        SharedPreferences sharedPreferencesIX = getSharedPreferences(AIR_PREFERENCE_IX, MODE_PRIVATE);
        String ixList = sharedPreferencesIX.getString(AIR_PREFERENCE_LIST, "");
        SharedPreferences sharedPreferencesDX = getSharedPreferences(AIR_PREFERENCE_DX, MODE_PRIVATE);
        String dxList = sharedPreferencesDX.getString(AIR_PREFERENCE_LIST, "");
        SharedPreferences sharedPreferencesRX = getSharedPreferences(AIR_PREFERENCE_RX, MODE_PRIVATE);
        String rxList = sharedPreferencesRX.getString(AIR_PREFERENCE_LIST, "");
        SharedPreferences sharedPreferencesAD = getSharedPreferences(AIR_PREFERENCE_AD, MODE_PRIVATE);
        String adList = sharedPreferencesAD.getString(AIR_PREFERENCE_LIST, "");
        SharedPreferences sharedPreferencesFU = getSharedPreferences(AIR_PREFERENCE_FU, MODE_PRIVATE);
        String fuList = sharedPreferencesFU.getString(AIR_PREFERENCE_LIST, "");
        SharedPreferences userPreferences = getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE);
        patientAgeTV.setText(userPreferences.getString(AGE, "") + " " + userPreferences.getString(AGE_TYPE, ""));
        patientAddressTV.setText(userPreferences.getString(ADDRESS, ""));
        patientGenderTV.setText(userPreferences.getString(GENDER, ""));
        patientNameTV.setText(userPreferences.getString(PATIENT_NAME, ""));
        prescriptionIDTV.setText(userPreferences.getString(ID, ""));
        prescriptionID = userPreferences.getString(ID, String.valueOf(now));
        entryDateTV.setText(userPreferences.getString(DATE, ""));

        appointmentId = getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE).getString(PPActivity.APPOINTMENT_ID, "template");

        GetPatientInfoByAppointmentId(appointmentId);

        patientAddress = userPreferences.getString(ADDRESS, "");
        patientName = userPreferences.getString(PATIENT_NAME, "");
        patientGender = userPreferences.getString(GENDER, "");
        entryDate = userPreferences.getString(DATE, "");
        patientAge = userPreferences.getString(AGE, "") + " " + userPreferences.getString(AGE_TYPE, "");

        try {
            if (userPreferences.getString(INTENT_TYPE, "Str").equals("appointment")) {
                issueBt.setEnabled(true);
                patientInfoLayout.setVisibility(View.VISIBLE);
                headerViewLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.drQualification.setText(sharedPreferencesDr.getString(DEGREE, " ")+", "+sharedPreferencesDr.getString(H_DEGREE, " "));
        binding.designation.setText(sharedPreferencesDr.getString(DESIGNATION, " "));
        binding.jobExperience.setText(sharedPreferencesDr.getString(JOB_EXPERIENCE, " "));
        binding.workPlace.setText(sharedPreferencesDr.getString(WORK_PLACE, " "));
        binding.bmdcNumber.setText(sharedPreferencesDr.getString(BMDCNO, " "));

        stringBuilderOfCC = new StringBuilder();
        stringBuilderOfHO = new StringBuilder();
        stringBuilderOfIX = new StringBuilder();
        stringBuilderOfDX = new StringBuilder();
        stringBuilderOfRX = new StringBuilder();
        stringBuilderOfAD = new StringBuilder();
        ArrayList<String> arrayListOfCC = Utils.GenerateListFromStringList(ccList);
        ArrayList<String> arrayListOfHO = Utils.GenerateListFromStringList(hoList);
        ArrayList<String> arrayListOfIX = Utils.GenerateListFromStringList(ixList);
        ArrayList<String> arrayListOfDX = Utils.GenerateListFromStringList(dxList);
        ArrayList<String> arrayListOfRX = Utils.GenerateListFromStringList(rxList);
        ArrayList<String> arrayListOfAD = Utils.GenerateListFromStringList(adList);
        ArrayList<String> arrayListOfFU = Utils.GenerateListFromStringList(fuList);

        for (int i = 0; i < arrayListOfCC.size(); i++) {
            stringBuilderOfCC.append(arrayListOfCC.get(i).trim()).append("\n");
        }
        Log.i("TAG_ST", "onCreate: " + stringBuilderOfCC);

        for (int i = 0; i < arrayListOfHO.size(); i++) {
            stringBuilderOfHO.append(arrayListOfHO.get(i).trim()).append("\n");
        }
        Log.i("TAG_ST", "onCreate: " + stringBuilderOfHO);

        for (int i = 0; i < arrayListOfIX.size(); i++) {
            stringBuilderOfIX.append(arrayListOfIX.get(i).trim()).append("\n");
        }
        Log.i("TAG_ST", "onCreate: " + stringBuilderOfIX);

        for (int i = 0; i < arrayListOfDX.size(); i++) {
            stringBuilderOfDX.append(arrayListOfDX.get(i).trim()).append("\n");
        }
        Log.i("TAG_ST", "onCreate: " + stringBuilderOfDX);

        for (int i = 0; i < arrayListOfRX.size(); i++) {
            stringBuilderOfRX.append(arrayListOfRX.get(i).trim()).append("\n");
        }
        Log.i("TAG_ST", "onCreate: " + stringBuilderOfRX);

        for (int i = 0; i < arrayListOfAD.size(); i++) {
            stringBuilderOfAD.append("\u25CF    ").append(arrayListOfAD.get(i).trim()).append("\n");
        }
        Log.i("TAG_ST", "onCreate: " + stringBuilderOfAD);
        SharedPreferences doctorPreferences = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        doctorInfoBuilder = new StringBuilder();
        doctorName = doctorPreferences.getString(USER_NAME, "");
        doctorInfoBuilder.append(doctorPreferences.getString(DEGREE, ""))
                .append(",")
                .append(doctorPreferences.getString(H_DEGREE, ""))
                .append("\n")
                .append(doctorPreferences.getString(DESIGNATION, ""))
                .append("\n")
                .append(doctorPreferences.getString(BMDCNO, ""))
                .append("\n")
                .append(doctorPreferences.getString(JOB_EXPERIENCE, ""));

        ccShowTV.setText(stringBuilderOfCC.toString());
        hoShowTV.setText(stringBuilderOfHO.toString());
        ixShowTV.setText(stringBuilderOfIX.toString());
        dxShowTV.setText(stringBuilderOfDX.toString());
        rxShowTV.setText(stringBuilderOfRX.toString());
        adShowTV.setText(stringBuilderOfAD.toString());

        if (stringBuilderOfCC.toString().isEmpty()) {
            ccShowTV.setVisibility(View.GONE);
        }
        if (stringBuilderOfHO.toString().isEmpty()) {
            hoShowTV.setVisibility(View.GONE);
        }
        if (stringBuilderOfIX.toString().isEmpty()) {
            ixShowTV.setVisibility(View.GONE);
        }
        if (stringBuilderOfDX.toString().isEmpty()) {
            dxShowTV.setVisibility(View.GONE);
        }
        if (stringBuilderOfRX.toString().isEmpty()) {
            rxShowTV.setVisibility(View.GONE);
        }
        if (stringBuilderOfAD.toString().isEmpty()) {
            adShowTV.setVisibility(View.GONE);
        }
    }

    private void GetPatientInfoByAppointmentId(String appointmentId) {
        Log.d("TAG", "GetPatientInfoByAppointmentId: " + BASE_URL + "GetPatientInfoByAppointmentId.php?appointment_id=" + appointmentId);
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, BASE_URL + "GetPatientInfoByAppointmentId.php?appointment_id=" + appointmentId, response -> {
            Log.d("TAG", "GetPatientInfoByAppointmentId: " + response);
            try {
                JSONObject object = new JSONObject(response);
                rating = object.getString("rate");
                patientId = object.getString("patient_id");
                profile = object.getString("p_profile");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }));
    }

    public void SavePDF() {
        takeScreenshot2();
    }

    private void GetPermission() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider storage
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //GRANTED
                    }
                } else {
                    //GRANTED
                }
            } else {
                //GRANTED
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
//                SavePDF();
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setTitle("Permission required!");
                builder.setMessage("You have to give storage permission to preview prescription");
                builder.setPositiveButton("Ok!", (dialog, which) -> {
                    GetPermission();
                });
                builder.setCancelable(false);
                builder.create().show();
            }
        }
    }

    public void TAK(View view) {

    }

    public void takeScreenshot2() {

    }

    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    public void saveBitmap(Bitmap bitmap) {
        OutputStream outputStream;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, prescriptionID + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "/Healthmen/Patient/Prescription/Image/");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Objects.requireNonNull(outputStream);
//                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
//                Toast.makeText(this, "Image Not Not  Saved: \n " + e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {

            File imagePathFile = new File((Environment.getExternalStorageDirectory() + File.separator + "/Healthmen/Patient/Prescription/Image/"));
            File imagePath = new File(Environment.getExternalStoragePublicDirectory("") + File.separator + "/Healthmen/Patient/Prescription/Image/" + prescriptionID + ".jpg");

            if (!imagePathFile.exists()) {
                imagePathFile.mkdirs();
            }

            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imagePath, false);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
//                Toast.makeText(getApplicationContext(), imagePath.getAbsolutePath() + "", Toast.LENGTH_LONG).show();
//                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.e("GREC", e.getMessage(), e);
//                Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("GREC", e.getMessage(), e);
//                Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void gen(Bitmap bitmap) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);

        pdfDocument.finishPage(page);

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
///Healthmen/Doctor/  /Healthmen/Doctor/
        File imagePathFile = new File(Environment.getExternalStorageDirectory() + File.separator + "/Healthmen/Doctor/Prescription/PDF");
        File imagePath = new File(Environment.getExternalStorageDirectory() + File.separator + "Healthmen/Doctor/Prescription/PDF/" + prescriptionID + ".pdf");

        boolean success = true;
        if (!imagePathFile.exists()) {
            success = imagePathFile.mkdirs();
        }
        if (success) {
            // Do something on success
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imagePath, false);
                pdfDocument.writeTo(fos);
                pdfDocument.close();
//                Toast.makeText(getApplicationContext(), imagePath.getAbsolutePath() + "", Toast.LENGTH_LONG).show();
                Log.e("ImageSave", "Saveimage");
            } catch (FileNotFoundException e) {
                Log.e("GREC", e.getMessage(), e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Do something else on failure
            Log.d("TAG_FILE", "gen: File not saved");
        }
    }

    public void Save(View view) {
        TemplateTDFragment templateTDFragment = new TemplateTDFragment();
        templateTDFragment.show(getSupportFragmentManager(), templateTDFragment.getTag());
    }

    private void UploadTemplate(String title, String desc) {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, BASE_URL + "upload_pdf_info.php", response -> {
            Log.d("TAG", "UploadPrescriptionInfo: " + response);
            if (response.equals("Issued successfully")) {
                Toast.makeText(this, "Template Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }) {
            @NotNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("doctor_name", doctorName);
                map.put("doctor_info", doctorInfoBuilder.toString());
                map.put("patient_name", tamplate);
                map.put("patient_age", tamplate);
                map.put("patient_gender", tamplate);
                map.put("patient_address", tamplate);
                map.put("signature", signatureId);
                map.put("issue_date", tamplate);
                map.put("send_time", System.currentTimeMillis() + "");
                map.put("cc", stringBuilderOfCC.toString());
                map.put("ho", stringBuilderOfHO.toString());
                map.put("ix", stringBuilderOfIX.toString());
                map.put("dx", stringBuilderOfDX.toString());
                map.put("title", title);
                map.put("desc", desc);
                map.put("rx", stringBuilderOfRX.toString());
                map.put("ad", stringBuilderOfAD.toString());
                map.put("prescription_type", tamplate);
                map.put("doctor_id", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                map.put("appointment_id_f_p", appointmentId);
                return map;
            }
        });
    }

    public void Issue(View view) {
        bitScroll = getBitmapFromView(rootLayout, rootLayout.getChildAt(0).getHeight(), rootLayout.getChildAt(0).getWidth());
        saveBitmap(bitScroll);
//        gen(bitScroll);
        UploadPrescriptionInfo();
    }

    private void UploadPrescriptionInfo() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Processing Prescription...");
        progressDialog.setMessage("Prescription sending to patient...");
        progressDialog.show();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, BASE_URL + "upload_pdf_info.php", response -> {
            progressDialog.dismiss();
            Log.d("TAG", "UploadPrescriptionInfo: " + response);
            if (response.equals("Issued successfully")) {
                //Show follow up dialog

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("appointments").child(appointmentId);
                databaseReference.child("prescription").setValue("Done");
                databaseReference.child("chat").setValue("Closed");

                ShowFUDialogFragment showFUDialogFragment = new ShowFUDialogFragment(appointmentId);
                showFUDialogFragment.show(getSupportFragmentManager(), showFUDialogFragment.getTag());
            } else {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            error.printStackTrace();
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("doctor_name", doctorName);
                map.put("doctor_info", doctorInfoBuilder.toString());
                map.put("patient_name", patientName);
                map.put("patient_age", patientAge);
                map.put("patient_gender", patientGender);
                map.put("patient_address", patientAddress);
                map.put("issue_date", entryDate);
                map.put("send_time", System.currentTimeMillis() + "");
                map.put("title", "appointment");
                map.put("desc", "appointment");
                map.put("titleNs", "Doctor issued your prescription.");
                map.put("bodyNs", "Please check");
                map.put("intentNS", "prescription");
                map.put("senderNS", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                map.put("signature", signatureId);
                map.put("cc", stringBuilderOfCC.toString());
                map.put("ho", stringBuilderOfHO.toString());
                map.put("ix", stringBuilderOfIX.toString());
                map.put("dx", stringBuilderOfDX.toString());
                map.put("rx", stringBuilderOfRX.toString());
                map.put("ad", stringBuilderOfAD.toString());
                map.put("prescription_type", "Consultation Prescription");
                map.put("doctor_id", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                map.put("appointment_id_f_p", getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE).getString(PPActivity.APPOINTMENT_ID, "template"));
                Log.d(TAG, "getParams: "+map);
                return map;
            }
        });
    }

    @Override
    public void Confirm(boolean b) {
        /*startActivity(new Intent(this, HomeActivity.class));
        finish();*/
        RatePatientDialogFragment ratePatientDialogFragment = new RatePatientDialogFragment(this, appointmentId, getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""), patientId, profile, rating, confirm -> {
        });
        ratePatientDialogFragment.show(getSupportFragmentManager(), ratePatientDialogFragment.getTag());
    }

    @Override
    public void FUDismiss() {

    }

    @Override
    public void Confirmation(String title, String desc) {
        UploadTemplate(title, desc);
        bitScroll = getBitmapFromView(rootLayout, rootLayout.getChildAt(0).getHeight(), rootLayout.getChildAt(0).getWidth());
        saveBitmap(bitScroll);
        gen(bitScroll);
    }

    @Override
    public void Confirmation(boolean confirm) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private static final String TAG = "PreviewActivity";
}
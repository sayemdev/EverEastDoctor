package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.H_DEGREE;
import static evereast.co.doctor.Constants.PATIENT_URL;
import static evereast.co.doctor.Constants.SIGNATURE;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.RXActivities.PPActivity.ADDRESS;
import static evereast.co.doctor.RXActivities.PPActivity.AGE;
import static evereast.co.doctor.RXActivities.PPActivity.AGE_TYPE;
import static evereast.co.doctor.RXActivities.PPActivity.APPOINTMENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.DATE;
import static evereast.co.doctor.RXActivities.PPActivity.GENDER;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_NAME;
import static evereast.co.doctor.Utils.generatePrescriptionId;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import evereast.co.doctor.Constants;
import evereast.co.doctor.DialogFragment.RatePatientDialogFragment;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.ShowFUDialogFragment;

public class GiveFromTemplateActivity extends AppCompatActivity implements ShowFUDialogFragment.SelectPhotoValueListener, RatePatientDialogFragment.ConfirmationValueListener {

    private static final String TAG = "GiveFromTemplateActivit";
    ScrollView rootLayout;
    TextView ccShowTV, hoShowTV, ixShowTV, dxShowTV, rxShowTV, adShowTV, drNameTextView, drQualification, patientNameTV, patientAgeTV, entryDateTV, patientGenderTV, patientAddressTV, prescriptionIDTV;
    Button issueBt;
    String appointmentId;
    RelativeLayout mainPrescriptionView;
    String cc, rx, dx, ix, ho, date, ad, address, ageType, age, gender, patientName, doctorName, dInfo, patientId, chatUserProfilePicture;
    String prescriptionID = "null";
    String signatureId;
    ImageView signatureView;
    private String rating;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_from_template);

        appointmentId = getIntent().getStringExtra(APPOINTMENT_ID);
//        Log.d(TAG, "onCreate: " + appointmentId);
        Log.d(TAG, "onCreate: " + getIntent().getStringExtra(APPOINTMENT_ID));

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
        drNameTextView = findViewById(R.id.drNameTV);
        drQualification = findViewById(R.id.drQualification);
        mainPrescriptionView = findViewById(R.id.mainView);
        signatureView = findViewById(R.id.signatureView);

        patientName = getIntent().getStringExtra(PATIENT_NAME);
        patientId = getIntent().getStringExtra(PATIENT_ID);
        chatUserProfilePicture = getIntent().getStringExtra("PATIENT_PROFILE");
        age = getIntent().getStringExtra(AGE);
        ageType = getIntent().getStringExtra(AGE_TYPE);
        date = getIntent().getStringExtra(DATE);
        gender = getIntent().getStringExtra(GENDER);
        rating = getIntent().getStringExtra("rating");
        prescriptionID = generatePrescriptionId(getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis())));
        address = getIntent().getStringExtra(ADDRESS);
//        appointmentId = getIntent().getStringExtra(APPOINTMENT_ID);

        patientNameTV.setText(patientName);
        patientAgeTV.setText(age + " " + ageType);
        entryDateTV.setText(date);
        patientGenderTV.setText(gender);
        prescriptionIDTV.setText(prescriptionID);
        patientAddressTV.setText(address);

        SharedPreferences sharedPreferencesDr = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        signatureId = sharedPreferencesDr.getString(SIGNATURE, "sf");
        Glide.with(this).load(PATIENT_URL + "d_signature/" + signatureId).into(signatureView);

        try {
            JSONObject object = new JSONObject(getIntent().getStringExtra("full"));
            drNameTextView.setText(object.getString("doctor_name"));
            dInfo = getIntent().getStringExtra("doctorInfo").trim();
            drQualification.setText(dInfo+", "+getSharedPreferences(USER_DATA_PREF,MODE_PRIVATE).getString(H_DEGREE,""));
            doctorName = object.getString("doctor_name");
            patientNameTV.setText(object.getString("patient_name"));
            patientAgeTV.setText(object.getString("patient_age"));
            patientAddressTV.setText(object.getString("patient_address"));
            entryDateTV.setText(object.getString("issue_date"));
            ccShowTV.setText(object.getString("cc"));
            cc = object.getString("cc");
            ixShowTV.setText(object.getString("ix"));
            ix = object.getString("ix");
            rxShowTV.setText(object.getString("rx"));
            rx = object.getString("rx");
            dxShowTV.setText(object.getString("dx"));
            dx = object.getString("dx");
            adShowTV.setText(object.getString("ad"));
            ad = object.getString("ad");
            hoShowTV.setText(object.getString("ho"));
            ho = object.getString("ho");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (cc.isEmpty()) {
            ccShowTV.setVisibility(View.GONE);
        }
        if (ho.isEmpty()) {
            hoShowTV.setVisibility(View.GONE);
        }
        if (ix.isEmpty()) {
            ixShowTV.setVisibility(View.GONE);
        }
        if (dx.isEmpty()) {
            dxShowTV.setVisibility(View.GONE);
        }
        if (rx.isEmpty()) {
            rxShowTV.setVisibility(View.GONE);
        }
        if (ad.isEmpty()) {
            adShowTV.setVisibility(View.GONE);
        }

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
                map.put("doctor_info", dInfo);
                map.put("patient_name", patientName);
                map.put("patient_age", age + " " + ageType);
                map.put("patient_gender", gender);
                map.put("patient_address", address);
                map.put("signature", signatureId);
                map.put("issue_date", date);
                map.put("title", "appointment");
                map.put("desc", "appointment");
                map.put("cc", cc);
                map.put("ho", ho);
                map.put("ix", ix);
                map.put("dx", dx);
                map.put("rx", rx);
                map.put("ad", ad);
                map.put("prescription_type", "Consultation Prescription");
                map.put("doctor_id", getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                map.put("appointment_id_f_p", appointmentId/* getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE).getString(PPActivity.APPOINTMENT_ID, "template")*/);
                return map;
            }
        });
    }

    public void Issue(View view) {
        UploadPrescriptionInfo();
    }

    @Override
    public void Confirm(boolean b) {
        RatePatientDialogFragment ratePatientDialogFragment = new RatePatientDialogFragment(this, appointmentId, getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""), patientId, chatUserProfilePicture, rating, confirm -> {
        });
        ratePatientDialogFragment.show(getSupportFragmentManager(), ratePatientDialogFragment.getTag());
    }

    @Override
    public void FUDismiss() {

    }

    @Override
    public void Confirmation(boolean confirm) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
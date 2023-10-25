package evereast.co.doctor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import evereast.co.doctor.Maps;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.BMDCNO;
import static evereast.co.doctor.Constants.DEGREE;
import static evereast.co.doctor.Constants.DESIGNATION;
import static evereast.co.doctor.Constants.EMAIL;
import static evereast.co.doctor.Constants.H_DEGREE;
import static evereast.co.doctor.Constants.IS_APPROVED;
import static evereast.co.doctor.Constants.IS_LOGGED_IN;
import static evereast.co.doctor.Constants.JOB_EXPERIENCE;
import static evereast.co.doctor.Constants.PHONE;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;
import static evereast.co.doctor.Constants.isEmailValid;
import static evereast.co.doctor.Utils.redirectToVerification;

public class ApplyToWorkActivity extends AppCompatActivity {
    private static final String TAG = "REGISTRATIONACTIVITY";
    EditText degreeEt, designationEt, nameEt, emailEt, jobExperienceEt, bmdcNumberEt, aboutSelfEt;
    String name, bmdcNumber, email, jobExperience, aboutSelf;
    TextView nextButton, haveAccount;
    ImageView back;
    String uid;
    ProgressDialog progressDialog;
    private String phone;
    private String degree, designation;

    public static void setValid(EditText editText) {
        editText.setBackgroundResource(R.drawable.edit_text_rect);
    }

    public static void editTextError(EditText editText, Context context) {
        editText.setBackgroundResource(R.drawable.error_edit_text_rect);
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.shake);
        editText.startAnimation(myAnim);
        editText.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_to_work);

        progressDialog = new ProgressDialog(this);

        phone = getIntent().getStringExtra("phone");
        Log.d(TAG, "onCreate: Phone:===> " + phone);


        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        jobExperienceEt = findViewById(R.id.jobExperienceEt);
        aboutSelfEt = findViewById(R.id.aboutSelf);
        bmdcNumberEt = findViewById(R.id.bmdcNumberEt);
        degreeEt = findViewById(R.id.degreeEt);
        designationEt = findViewById(R.id.designationEt);
        nextButton = findViewById(R.id.nextButton);
        haveAccount = findViewById(R.id.haveAccount);

        nextButton.setOnClickListener(v -> {
            name = nameEt.getText().toString().trim();
            email = emailEt.getText().toString().trim();
            jobExperience = jobExperienceEt.getText().toString().trim();
            aboutSelf = aboutSelfEt.getText().toString().trim();
            bmdcNumber = bmdcNumberEt.getText().toString().trim();
            degree = degreeEt.getText().toString().trim();
            designation = designationEt.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                editTextError(nameEt, v.getContext());
                setValid(emailEt);
                setValid(jobExperienceEt);
                setValid(aboutSelfEt);
                setValid(bmdcNumberEt);
                setValid(degreeEt);
                setValid(designationEt);
                Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(bmdcNumber)) {
                editTextError(bmdcNumberEt, this);
                setValid(nameEt);
                setValid(emailEt);
                setValid(jobExperienceEt);
                setValid(aboutSelfEt);
                setValid(degreeEt);
                setValid(designationEt);
                Toast.makeText(this, "Enter your " + getResources().getString(R.string.bmdc), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(email)) {
                editTextError(emailEt, v.getContext());
                setValid(nameEt);
                setValid(jobExperienceEt);
                setValid(aboutSelfEt);
                setValid(bmdcNumberEt);
                setValid(degreeEt);
                setValid(designationEt);
                Toast.makeText(this, "Enter your email address", Toast.LENGTH_SHORT).show();
            } else if (!isEmailValid(email)) {
                editTextError(emailEt, v.getContext());
                setValid(nameEt);
                setValid(jobExperienceEt);
                setValid(aboutSelfEt);
                setValid(bmdcNumberEt);
                setValid(degreeEt);
                setValid(designationEt);
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(degree)) {
                editTextError(degreeEt, v.getContext());
                setValid(nameEt);
                setValid(jobExperienceEt);
                setValid(aboutSelfEt);
                setValid(bmdcNumberEt);
                setValid(emailEt);
                setValid(designationEt);
                Toast.makeText(this, "Enter your degree", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(designation)) {
                editTextError(designationEt, v.getContext());
                setValid(nameEt);
                setValid(jobExperienceEt);
                setValid(aboutSelfEt);
                setValid(bmdcNumberEt);
                setValid(emailEt);
                setValid(degreeEt);
                Toast.makeText(this, "Enter your designation", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(jobExperience)) {
                editTextError(jobExperienceEt, v.getContext());
                setValid(nameEt);
                setValid(emailEt);
                setValid(bmdcNumberEt);
                setValid(aboutSelfEt);
                setValid(degreeEt);
                setValid(designationEt);
                Toast.makeText(this, "Enter job experience", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(aboutSelf)) {
                setValid(jobExperienceEt);
                setValid(nameEt);
                setValid(emailEt);
                setValid(degreeEt);
                setValid(bmdcNumberEt);
                editTextError(aboutSelfEt, v.getContext());
                Toast.makeText(this, "Write about your self", Toast.LENGTH_SHORT).show();
            } else {
                Random r = new Random();
                final int i1 = r.nextInt(1000000000 - 1000000) + 1000000;
                uid = phone + i1;
                setValid(nameEt);
                setValid(jobExperienceEt);
                setValid(aboutSelfEt);
                setValid(bmdcNumberEt);
                setValid(emailEt);
                setValid(degreeEt);
                setValid(designationEt);
                Registration(name, bmdcNumber, degree, designation, email, jobExperience);
            }

        });

        haveAccount.setOnClickListener(v -> {
            redirectToVerification(this, "Login");
        });

    }


    private void Registration(String name, String bmdcNumber, String degree, String designation, String email, String jobExperience) {
        progressDialog.setTitle("Registration in progress...");
        progressDialog.setMessage("Please wait for a moment");
        progressDialog.show();
        String url = BASE_URL + "signup.php";
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, url, response -> {
            progressDialog.dismiss();
            Log.d(TAG, "Registration: " + response);
            if (response.equals("Registration completed Successfully")) {
                SharedPreferences.Editor editor = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).edit();
                editor.clear();
                editor.putString(USER_ID, uid);
                editor.putString(USER_NAME, name);
                editor.putString(BMDCNO, bmdcNumber);
                editor.putString(DEGREE, degree);
                editor.putString(H_DEGREE, "");
                editor.putString(DESIGNATION, designation);
                editor.putString(EMAIL, email);
                editor.putString(PHONE, phone);
                editor.putString(JOB_EXPERIENCE, jobExperience);
                editor.putBoolean(IS_LOGGED_IN, true);
                editor.putBoolean(IS_APPROVED, false);
                editor.apply();
                Intent intent = new Intent(this, PendingMessageActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Already registered with this number", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put(Maps.DOCTOR_ID, uid);
                map.put(Maps.NAME, name);
                map.put(Maps.EMAIL, email);
                map.put(Maps.DEGREE, degree);
                map.put(Maps.DESIGNATION, designation);
                map.put(Maps.ABOUT, aboutSelf);
                map.put(Maps.PHONE, phone);
                map.put(Maps.BMDC, bmdcNumber);
                map.put(Maps.JOB_EXPERIENCE, jobExperience);

                Log.d(TAG, "getParams: "+map);

                return map;
            }
        });
    }


}
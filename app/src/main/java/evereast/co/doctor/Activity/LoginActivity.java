package evereast.co.doctor.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import evereast.co.doctor.R;
import evereast.co.doctor.Utils;
import evereast.co.doctor.utils.BaseApplication;
import evereast.co.doctor.utils.LogcatUtils;

import static evereast.co.doctor.Utils.hideKeyboard;
import static evereast.co.doctor.Utils.redirectToVerification;

import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView dontHaveAnAccount, forgotPasswordTextView;
    EditText doctorIdEt, passwordEditText;
    String from;
    ImageView back;
    String phone,doctorId;
    private String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        from = getIntent().getStringExtra("from");
        phone = getIntent().getStringExtra("phone");

        dontHaveAnAccount = findViewById(R.id.notHaveAccount);
        forgotPasswordTextView = findViewById(R.id.forgotPassword);
        doctorIdEt = findViewById(R.id.doctorIdEt);
        passwordEditText = findViewById(R.id.passwordEt);
        dontHaveAnAccount.setPaintFlags(dontHaveAnAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        dontHaveAnAccount.setOnClickListener(v -> redirectToVerification(LoginActivity.this, "Apply"));
        /* forgotPasswordTextView.setOnClickListener(v -> redirectToActivity(evereast.com.doctor.evereast.Activity.LoginActivity.this, ForgotPasswordActivity.class));
         */

        doctorIdEt.setText(getIntent().getStringExtra("id"));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
        if (Objects.equals(null, from)) {
            back.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        if (!Objects.equals(null, from)) {
            super.onBackPressed();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    public void Login(View view) {
        if (!TextUtils.isEmpty(doctorIdEt.getText().toString().trim()) && !TextUtils.isEmpty(passwordEditText.getText().toString().trim())) {
            hideKeyboard(this);
            // Example usage in your app's code
// Call this method when you want to capture the Logcat output
            LogcatUtils.saveLogcatToFile(getApplicationContext());
            Utils.Login(doctorIdEt.getText().toString().trim(), passwordEditText.getText().toString().trim(),phone,LoginActivity.this);
        }
    }

    private static final String TAG = "LoginActivity";

}
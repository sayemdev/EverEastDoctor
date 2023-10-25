package evereast.co.doctor.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

import evereast.co.doctor.DialogFragment.EditNumberFragment;
import evereast.co.doctor.HttpRetrofit.RetrofitClient;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.ActivityGetPhoneInputBinding;
import evereast.co.doctor.utils.AppSignatureHashHelper;
import evereast.co.doctor.utils.SMSReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPhoneInput extends AppCompatActivity implements EditNumberFragment.EditPhoneNumberValueListener, SMSReceiver.OTPReceiveListener {

    private static final String TAG = "GetPhoneInput";
    TextView getOtp, userPhoneTextView, editNumber, timerTextView;
    EditText phoneEditText;
    CountryCodePicker countryCodePicker;
    String countryCode, phone, phoneFromIntent;
    PinView pinView;
    StringBuilder stringBuilder;
    String phoneNormal, to;
    ImageView back;
    Button button;
    String phoneFinal;
    ProgressDialog dialog;
    SMSReceiver smsReceiver;
    ActivityGetPhoneInputBinding binding;
    private StringBuilder stringBuilder1;
    private FirebaseAuth firebaseAuth;
    private String vId;
    private LinearLayout confirmOtpLayout, sendOtpLayout;
    private String otp;
    private CountDownTimer countDownTimer;
    private String Hashkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetPhoneInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        phoneFromIntent = getIntent().getStringExtra("phone");
        to = getIntent().getStringExtra("to");

        binding.termsAndConditionRButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                binding.getOtp.setEnabled(true);
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        Log.d(TAG, "Hashkey: " + appSignatureHashHelper.getAppSignatures().get(0));
        Hashkey = appSignatureHashHelper.getAppSignatures().get(0);


        back = findViewById(R.id.back);
        timerTextView = findViewById(R.id.timer);
        editNumber = findViewById(R.id.editNumber);
        getOtp = findViewById(R.id.getOtp);
        phoneEditText = findViewById(R.id.phone);
        countryCodePicker = findViewById(R.id.ccp);
        pinView = findViewById(R.id.pinView);
        sendOtpLayout = findViewById(R.id.getOtpLayout);
        confirmOtpLayout = findViewById(R.id.otpLayout);
        userPhoneTextView = findViewById(R.id.userPhone);
        button = findViewById(R.id.verifyButton);

        if (phoneFromIntent != null) {
            phoneEditText.setText(phoneFromIntent);
        }
        back.setOnClickListener(v -> finish());

        timerTextView.setPaintFlags(timerTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        editNumber.setPaintFlags(editNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        countDownTimer = new CountDownTimer(120000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timerTextView.setText("Resend OTP in " + time);
            }

            @Override
            public void onFinish() {
                Toast.makeText(GetPhoneInput.this, "Connection timeout...\n Resend OTP", Toast.LENGTH_SHORT).show();
                getOtp.setText(R.string.resend_otp);
                sendOtpLayout.setVisibility(View.VISIBLE);
                confirmOtpLayout.setVisibility(View.GONE);
            }
        };

        getOtp.setOnClickListener(v -> {
            stringBuilder = new StringBuilder();
            phone = phoneEditText.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                phoneEditText.setError("Enter a phone number");
                phoneEditText.requestFocus();
            } else if (!TextUtils.isEmpty(phone)) {
                stringBuilder.append(phone);
                countryCode = countryCodePicker.getTextView_selectedCountry().getText().toString();
                stringBuilder1 = new StringBuilder(countryCode);
                if (String.valueOf(stringBuilder.charAt(0)).equals("0")) {
                    stringBuilder.deleteCharAt(0);
                }
                if (stringBuilder.length() != 10) {
                    phoneEditText.setError("Enter a valid phone number");
                    phoneEditText.requestFocus();
                } else {
                    phoneNormal = "0" + stringBuilder;
                    stringBuilder1.append(stringBuilder);
                    sendOtp(stringBuilder1.toString());
                    userPhoneTextView.setText(stringBuilder1);
                    confirmOtpLayout.setVisibility(View.VISIBLE);
                    countDownTimer.start();
                    sendOtpLayout.setVisibility(View.GONE);

                }
            }
        });
        editNumber.setOnClickListener(v -> {
            EditNumberFragment editNumberFragment = new EditNumberFragment("0" + stringBuilder.toString());
            editNumberFragment.show(getSupportFragmentManager(), editNumberFragment.getTag());
            countDownTimer.cancel();
        });
    }


    private void sendOtp(String phone) {

        Call<String> sendOtpCall = RetrofitClient.getInstance().getMyApi().SendOtp(binding.phone.getText().toString().trim());
        dialog.setTitle("Sending otp");
        dialog.setMessage("Please wait...");
        dialog.show();

        sendOtpCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                dialog.dismiss();
                Log.d(TAG, "onResponse: " + response.body());
                if (response.body().equals("OTP sent")) {
                    binding.verifyButton.setEnabled(true);
                    SmsRetrieverClient client = SmsRetriever.getClient(GetPhoneInput.this);
                    Task<Void> task = client.startSmsRetriever();
                    task.addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "onSuccess: ");
                        startSMSListener();
                    });

                    task.addOnFailureListener(e -> {
                        Log.d(TAG, "onFailure: ");
                        e.printStackTrace();
                    });
                } else if (response.body().startsWith("OTP already sent")) {
                    binding.verifyButton.setEnabled(true);
                } else {
                    getOtp.setText(R.string.resend_otp);
                    sendOtpLayout.setVisibility(View.VISIBLE);
                    countDownTimer.cancel();
                    confirmOtpLayout.setVisibility(View.GONE);
                    binding.verifyButton.setEnabled(false);
                }
                Toast.makeText(GetPhoneInput.this, response.body() + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                getOtp.setText(R.string.resend_otp);
                sendOtpLayout.setVisibility(View.VISIBLE);
                countDownTimer.cancel();
                confirmOtpLayout.setVisibility(View.GONE);
                binding.verifyButton.setEnabled(false);
                Toast.makeText(GetPhoneInput.this, "OTP sending failed. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(aVoid -> {
                // API successfully started
                Log.d(TAG, "startSMSListener: Started");

            });

            task.addOnFailureListener(e -> {
                // Fail to start API
                Log.d(TAG, "startSMSListener: failed");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOTPReceived(String otp) {
        Log.d(TAG, "onOTPReceived: " + otp);
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
            String OTP = otp.replaceAll("[^0-9]", "");
            binding.pinView.setText(OTP);
            VerifyOtp();
        }
    }

    @Override
    public void onOTPTimeOut() {
        Toast.makeText(this, "OTP timeout", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOTPReceivedError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    public void VerifyOtp() {
        Call<String> verifyCall = RetrofitClient.getInstance().getMyApi().VerifyOtp(binding.phone.getText().toString().trim(), binding.pinView.getText().toString().trim());
        dialog.setTitle("Verifying otp");
        dialog.setMessage("Please wait...");
        dialog.show();
        verifyCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + response.body());
                dialog.dismiss();
                if (response.body().equals("Verified successfully")) {
                    countDownTimer.cancel();
                    Intent intent2 = new Intent(GetPhoneInput.this, ApplyToWorkActivity.class);
                    intent2.putExtra("phone", phoneNormal);

                    Intent intent = new Intent(GetPhoneInput.this, LoginActivity.class);
                    intent.putExtra("phone", phoneFinal);
                    intent.putExtra("from", "non-null");

                    try {
                        if (to.equals("Apply")) {
                            startActivity(intent2);
                            finish();
                        } else if (to.equals("Login")) {
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        startActivity(intent2);
                        finish();
                    }

                } else {
                    getOtp.setText(R.string.resend_otp);
                    sendOtpLayout.setVisibility(View.VISIBLE);
                    countDownTimer.cancel();
                    confirmOtpLayout.setVisibility(View.GONE);
                    button.setEnabled(false);
                    pinView.setText("");
                }
                Toast.makeText(GetPhoneInput.this, response.body() + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(GetPhoneInput.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                getOtp.setText(R.string.resend_otp);
                sendOtpLayout.setVisibility(View.VISIBLE);
                countDownTimer.cancel();
                confirmOtpLayout.setVisibility(View.GONE);
                button.setEnabled(false);
                pinView.setText("");
                dialog.dismiss();
            }
        });

    }


    public void Verify(View view) {
        try {
            otp = pinView.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ADREG2", "ConfirmOTP: " + e.getMessage());
        }
        if (!TextUtils.isEmpty(otp)) {
            VerifyOtp();
        }
    }


    public void EditNumber(View view) {

    }

    @Override
    public void ApplyPhoneValue(String phone) {
        Intent intent = new Intent(this, GetPhoneInput.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
        finish();
    }

}
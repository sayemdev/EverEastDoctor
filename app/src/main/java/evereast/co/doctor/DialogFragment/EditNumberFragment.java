package evereast.co.doctor.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hbb20.CountryCodePicker;

import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/11/2020 at 4:31 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class EditNumberFragment extends AppCompatDialogFragment {

    EditText phoneEt;
    TextView confirmButton, cancelButton;
    String currentPhone;
    TextView currentPhoneTextView;
    EditPhoneNumberValueListener editPhoneNumberValueListener;
    private StringBuilder stringBuilder, stringBuilder1;
    private String countryCode;
    private CountryCodePicker countryCodePicker;

    public EditNumberFragment(String currentPhone) {
        // Required empty public constructor
        this.currentPhone = currentPhone;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_number_fragmet, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        countryCodePicker = view.findViewById(R.id.ccp);
        currentPhoneTextView = view.findViewById(R.id.currentPhone);
        phoneEt = view.findViewById(R.id.phone);
        confirmButton = view.findViewById(R.id.confirm);
        cancelButton = view.findViewById(R.id.cancel);
        currentPhoneTextView.setText(currentPhone);
        phoneEt.setText(currentPhone);

        confirmButton.setOnClickListener(v -> {
            String phone = phoneEt.getText().toString();
            if (!TextUtils.isEmpty(phone)) {
                try {
                    stringBuilder = new StringBuilder();
                    phone = phoneEt.getText().toString();
                    if (TextUtils.isEmpty(phone)) {
                        phoneEt.setBackgroundResource(R.drawable.error_edit_text_rect);
                        Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                        phoneEt.startAnimation(myAnim);
                        phoneEt.requestFocus();
                        Toast.makeText(getContext(), "Enter a phone number", Toast.LENGTH_SHORT).show();
                    } else if (!TextUtils.isEmpty(phone)) {
                        stringBuilder.append(phone);
                        countryCode = countryCodePicker.getTextView_selectedCountry().getText().toString();
                        stringBuilder1 = new StringBuilder(countryCode);
                        if (String.valueOf(stringBuilder.charAt(0)).equals("0")) {
                            stringBuilder.deleteCharAt(0);
                        }
                        if (stringBuilder.length() != 10) {
                            phoneEt.setBackgroundResource(R.drawable.error_edit_text_rect);
                            Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                            phoneEt.startAnimation(myAnim);
                            phoneEt.requestFocus();
                            Toast.makeText(getContext(), "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                        } else {
                            stringBuilder1.append(stringBuilder);
                            editPhoneNumberValueListener.ApplyPhoneValue("0" + stringBuilder);
                            dismiss();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                phoneEt.setBackgroundResource(R.drawable.error_edit_text_rect);
                Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                phoneEt.startAnimation(myAnim);
                phoneEt.requestFocus();
            }

        });

        cancelButton.setOnClickListener(v -> dismiss());

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            editPhoneNumberValueListener = (EditPhoneNumberValueListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditPhoneNumberValueListener");
        }
    }

    public interface EditPhoneNumberValueListener {
        void ApplyPhoneValue(String phone);
    }

}

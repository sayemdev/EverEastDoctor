package evereast.co.doctor.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/11/2020 at 7:55 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class GenderDialogFragment extends AppCompatDialogFragment {

    Button negativeButton, positiveButton;

    GenderValueListener genderValueListener;
    private RadioGroup radioGroup;
    private RadioButton genderRadioButton;

    public GenderDialogFragment() {
    }

    public GenderDialogFragment(GenderValueListener genderValueListener) {
        this.genderValueListener = genderValueListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.select_gender, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(false);

        negativeButton = view.findViewById(R.id.negativeBt);
        positiveButton = view.findViewById(R.id.positiveBt);
        radioGroup = view.findViewById(R.id.genderRadioGroup);

        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        positiveButton.setOnClickListener(v -> {
            int selectedID = radioGroup.getCheckedRadioButtonId();
            genderRadioButton = view.findViewById(selectedID);

            String gender = genderRadioButton.getText().toString();
            genderValueListener.Gender(gender);
            dismiss();
        });

        negativeButton.setOnClickListener(v -> {
                    genderValueListener.Gender("");
                    dismiss();
                }

        );

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            if(genderValueListener==null) {
                genderValueListener = (GenderValueListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement GenderValueListener");
        }
    }

    public interface GenderValueListener {
        void Gender(String gender);
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}

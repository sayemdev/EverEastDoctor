package evereast.co.doctor.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.checkbox.MaterialCheckBox;

import evereast.co.doctor.Activity.HomeActivity;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_AD;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_CC;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_DX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_FU;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_HO;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_IX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_PP;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_RX;

/**
 * Health Men created by Sayem Hossen Saimon on 12/21/2020 at 4:44 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class EndSessionConfirmation extends AppCompatDialogFragment {

    ConfirmationValueListener confirmationValueListener;
    Button proceedButton;
    ImageView cancelButton;
    MaterialCheckBox checkBox;

    public EndSessionConfirmation() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.end_session_confirmation_dialog_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(true);
        alert.setCanceledOnTouchOutside(false);

        proceedButton = view.findViewById(R.id.proceedButton);
        cancelButton = view.findViewById(R.id.cancel_button);
        checkBox = view.findViewById(R.id.savePrefCheckbox);

        proceedButton.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                view.getContext().getSharedPreferences(AIR_PREFERENCE_AD, Context.MODE_PRIVATE).edit().clear().apply();
                view.getContext().getSharedPreferences(AIR_PREFERENCE_CC, Context.MODE_PRIVATE).edit().clear().apply();
                view.getContext().getSharedPreferences(AIR_PREFERENCE_DX, Context.MODE_PRIVATE).edit().clear().apply();
                view.getContext().getSharedPreferences(AIR_PREFERENCE_RX, Context.MODE_PRIVATE).edit().clear().apply();
                view.getContext().getSharedPreferences(AIR_PREFERENCE_PP, Context.MODE_PRIVATE).edit().clear().apply();
                view.getContext().getSharedPreferences(AIR_PREFERENCE_LIST, Context.MODE_PRIVATE).edit().clear().apply();
                view.getContext().getSharedPreferences(AIR_PREFERENCE_IX, Context.MODE_PRIVATE).edit().clear().apply();
                view.getContext().getSharedPreferences(AIR_PREFERENCE_HO, Context.MODE_PRIVATE).edit().clear().apply();
                view.getContext().getSharedPreferences(AIR_PREFERENCE_FU, Context.MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                confirmationValueListener.Confirmation(true);
                dismiss();
            } else {
                confirmationValueListener.Confirmation(true);
                dismiss();
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            confirmationValueListener = (ConfirmationValueListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ConfirmationValueListener");
        }
    }

    public interface ConfirmationValueListener {
        void Confirmation(boolean confirm);
    }
}

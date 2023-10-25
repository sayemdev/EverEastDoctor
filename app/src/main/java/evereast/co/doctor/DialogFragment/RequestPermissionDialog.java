package evereast.co.doctor.DialogFragment;

import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Utils.PERMISSION_IS_POPED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/21/2020 at 4:44 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class RequestPermissionDialog extends AppCompatDialogFragment {
    Button proceedButton;
    Button cancelButton;
    ConfirmationValueListener confirmationValueListener;

    public RequestPermissionDialog() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.permission_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        proceedButton = view.findViewById(R.id.proceedButton);
        cancelButton = view.findViewById(R.id.cancel_button);

        proceedButton.setOnClickListener(v -> {
            dismiss();
            confirmationValueListener.Confirmation();
        });
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).edit().putBoolean(PERMISSION_IS_POPED, true).apply();



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
        void Confirmation();
    }
}

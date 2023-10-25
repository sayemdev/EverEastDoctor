package evereast.co.doctor.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/11/2020 at 4:31 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class CreateNewOrRead extends AppCompatDialogFragment {
    CreateNewOrReadAirPrescription createNewOrReadAirPrescription;
    Activity activity;
    Button templates, createNew;
    ImageView closeButton;

    public CreateNewOrRead() {
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_new_or_read_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        templates = view.findViewById(R.id.templates);
        createNew = view.findViewById(R.id.createNew);
        closeButton = view.findViewById(R.id.closeButton);

        templates.setOnClickListener(v -> {
            createNewOrReadAirPrescription.older();
            dismiss();
        });

        closeButton.setOnClickListener(view1 -> {
            dismiss();
        });

        createNew.setOnClickListener(v -> {
            createNewOrReadAirPrescription.createNew();
            dismiss();
        });

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            createNewOrReadAirPrescription = (CreateNewOrReadAirPrescription) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PPInputOnclickListener");
        }
    }

    public interface CreateNewOrReadAirPrescription {
        void createNew();

        void older();
    }

}

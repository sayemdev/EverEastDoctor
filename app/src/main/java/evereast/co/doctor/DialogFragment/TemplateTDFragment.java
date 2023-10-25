package evereast.co.doctor.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import evereast.co.doctor.R;

public class TemplateTDFragment extends DialogFragment {

    ImageView cancelButton;
    Button saveButton;
    EditText titleEditText, descriptionEditText;
    ConfirmationValueListener confirmationValueListener;

    public TemplateTDFragment() {
        // Required empty public constructor
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_template_t_d, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(true);
        alert.setCanceledOnTouchOutside(false);

        saveButton = view.findViewById(R.id.proceedButton);
        cancelButton = view.findViewById(R.id.cancel_button);
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(view.getContext(), "Enter a title", Toast.LENGTH_SHORT).show();
            } else if (description.isEmpty()) {
                Toast.makeText(view.getContext(), "Enter a description", Toast.LENGTH_SHORT).show();
            } else {
                confirmationValueListener.Confirmation(title, description);
                dismiss();
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
        void Confirmation(String title, String desc);
    }

}
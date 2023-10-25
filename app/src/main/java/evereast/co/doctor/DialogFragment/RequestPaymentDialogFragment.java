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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import evereast.co.doctor.R;

public class RequestPaymentDialogFragment extends DialogFragment {

    ImageView cancelButton;
    Button applyButton;
    EditText amountEditText;
    ConfirmationValueListener confirmationValueListener;
    int totalWithdraw, totalEarning;
    boolean isOffline;

    public RequestPaymentDialogFragment(int totalWithdraw, int totalEarning, boolean isOffline) {
        // Required empty public constructor
        this.totalEarning = totalEarning;
        this.totalWithdraw = totalWithdraw;
        this.isOffline=isOffline;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_request_payment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        applyButton = view.findViewById(R.id.proceedButton);
        cancelButton = view.findViewById(R.id.cancel_button);
        amountEditText = view.findViewById(R.id.titleEditText);

        applyButton.setOnClickListener(v -> {
            String amount = amountEditText.getText().toString().trim();
                confirmationValueListener.Confirmation(amount,isOffline);
                dismiss();
//            }
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
            throw new ClassCastException(activity + " must implement ConfirmationValueListener");
        }
    }

    public interface ConfirmationValueListener {
        void Confirmation(String amount, boolean isOffline);
    }

}
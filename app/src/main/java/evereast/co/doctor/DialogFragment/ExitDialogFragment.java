package evereast.co.doctor.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import evereast.co.doctor.databinding.FragmentExitDialogBinding;

public class ExitDialogFragment extends DialogFragment {
    FragmentExitDialogBinding binding;
    View view;
    ClickListener clickListener;

    public ExitDialogFragment() {
        // Required empty public constructor
    }

    public ExitDialogFragment(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentExitDialogBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(false);

        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        binding.yesButton.setOnClickListener(view1 ->
        {
            clickListener.CloseApp();
            dismiss();
        });

        binding.noButton.setOnClickListener(view1 -> {
            dismiss();
        });

        return alert;
    }

    public interface ClickListener {
        void CloseApp();
    }

}
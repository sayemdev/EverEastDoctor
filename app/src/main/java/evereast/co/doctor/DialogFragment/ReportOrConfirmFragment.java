package evereast.co.doctor.DialogFragment;

import android.annotation.SuppressLint;
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
import androidx.appcompat.app.AppCompatDialogFragment;

import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/11/2020 at 4:31 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class ReportOrConfirmFragment extends AppCompatDialogFragment {
    ReportOrConfirmListener reportOrConfirmListener;
    Activity activity;
    Button templates, createNew;
    ImageView closeButton;
    EditText reportET;

    public ReportOrConfirmFragment(ReportOrConfirmListener reportOrConfirmListener) {
        this.reportOrConfirmListener = reportOrConfirmListener;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.report_or_confirm, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        templates = view.findViewById(R.id.templates);
        createNew = view.findViewById(R.id.createNew);
        closeButton = view.findViewById(R.id.closeButton);
        reportET = view.findViewById(R.id.reportET);

        templates.setOnClickListener(v -> {
            reportOrConfirmListener.ConfirmAp();
            dismiss();
        });

        closeButton.setOnClickListener(view1 -> {
            dismiss();
        });

        createNew.setOnClickListener(v -> {

            if (createNew.getTag().toString().equals("First")) {
                createNew.setTag("Last");
                reportET.setVisibility(View.VISIBLE);
                createNew.setText("Submit");
            } else {
                reportOrConfirmListener.ReportAp(reportET.getText().toString().trim());
                dismiss();
            }
        });

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            reportOrConfirmListener = (ReportOrConfirmListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PPInputOnclickListener");
        }
    }

    public interface ReportOrConfirmListener {
        void ConfirmAp();

        void ReportAp(String s);
    }

}

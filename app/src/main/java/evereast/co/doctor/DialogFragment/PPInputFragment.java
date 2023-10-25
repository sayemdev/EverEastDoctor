package evereast.co.doctor.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import evereast.co.doctor.Model.TemplateModel;
import evereast.co.doctor.R;


public class PPInputFragment extends DialogFragment {
    PPInputOnclickListener PPInputOnclickListener;
    String name, age, ageType, dateString, gender, id, address;
    EditText nameEt, genderEt, dateEt, phoneEt, addressEt;
    Spinner ageSpinner, ageTypeSpinner;

    public PPInputFragment() {
        // Required empty public constructor
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_p_p_input, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(true);
        alert.setCanceledOnTouchOutside(false);


        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        nameEt = view.findViewById(R.id.nameEt);
        ageSpinner = view.findViewById(R.id.age);
        ageTypeSpinner = view.findViewById(R.id.typeSp);
        genderEt = view.findViewById(R.id.genderEt);
        dateEt = view.findViewById(R.id.dateEt);
        phoneEt = view.findViewById(R.id.phoneEt);
        addressEt = view.findViewById(R.id.addressEt);

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            PPInputOnclickListener = (PPInputOnclickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PPInputOnclickListener");
        }
    }

    private void InItPP(String name, String age, String ageType, String dateString, String gender, String id, String address, String appointmentId) {
        try {
            if (!name.isEmpty()) {
                nameEt.setText(name);
                ageSpinner.setSelection(Integer.parseInt(age) + 1);

                //Age Type Setting
                switch (ageType) {
                    case "Year":
                        ageTypeSpinner.setSelection(1);
                        break;
                    case "Month":
                        ageTypeSpinner.setSelection(2);
                        break;
                    case "Day":
                        ageTypeSpinner.setSelection(3);
                        break;
                }

                dateEt.setText(dateString);

                genderEt.setText(gender);
                phoneEt.setText(id);
                addressEt.setText(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PPInputOnclickListener {
        void createNew();

        void older(int position, TemplateModel templateModel);
    }


}
package evereast.co.doctor.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/11/2020 at 4:31 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class SelectSuggestion extends AppCompatDialogFragment {

    EditText suggestionEt;
    Button addButton;
    ImageView cancelButton;
    String symptom;
    SelectCCWithSuggestion selectCCWithSuggestion;
    Activity activity;
    ArrayList<String> suggestionList;
    ChipGroup chipGroup;
    boolean hasSuggestion;

    public SelectSuggestion(String symptom, Activity activity, boolean hasSuggestion,ArrayList<String>sgList) {
        this.symptom = symptom;
        this.activity = activity;
        this.hasSuggestion = hasSuggestion;
        this.suggestionList=sgList;
    }
    public SelectSuggestion(String symptom, Activity activity, boolean hasSuggestion,ArrayList<String>sgList,SelectCCWithSuggestion selectCCWithSuggestion) {
        this.symptom = symptom;
        this.activity = activity;
        this.hasSuggestion = hasSuggestion;
        this.suggestionList=sgList;
        this.selectCCWithSuggestion=selectCCWithSuggestion;
    }

    public SelectSuggestion(String symptom, Activity activity, boolean hasSuggestion) {
        this.symptom = symptom;
        this.activity = activity;
        this.hasSuggestion = hasSuggestion;
    }
    public SelectSuggestion(String symptom, Activity activity, boolean hasSuggestion,SelectCCWithSuggestion selectCCWithSuggestion) {
        this.symptom = symptom;
        this.activity = activity;
        this.hasSuggestion = hasSuggestion;
        this.selectCCWithSuggestion=selectCCWithSuggestion;
    }

    /* public SelectSuggestion(String symptom, Activity activity) {
        // Required empty public constructor
        this.symptom = symptom;
        this.activity=activity;
    }*/

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.selecct_sugegstion, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

//        suggestionList = new ArrayList<>();

        suggestionEt = view.findViewById(R.id.suggestionEt);
        addButton = view.findViewById(R.id.addS);
        cancelButton = view.findViewById(R.id.closeDialog);
        chipGroup = view.findViewById(R.id.cgSuggestion);

        suggestionEt.setText(symptom);

        addButton.setOnClickListener(v -> {
            try {
                if (hasSuggestion) {
                    if (!suggestionEt.getText().toString().equals(symptom)) {
                        selectCCWithSuggestion.SelectedSuggestion(suggestionEt.getText().toString().trim());
                    } else {
                        Toast.makeText(activity, "You have no selected item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    selectCCWithSuggestion.SelectedSuggestion(suggestionEt.getText().toString().trim());
                }
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        if (hasSuggestion) {
            SetChipGroup(suggestionList, chipGroup);
        }

        return alert;
    }


    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup) {
        for (int i = 0; i < mainList.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d("CHIP_CGs", "onCreate: SetChipGroup " + mainList.get(i));
            chip2.setText(mainList.get(i));
            chip2.setId(i);
            chipGroup.setSingleSelection(true);
            chip2.setOnClickListener(v -> {
                suggestionEt.setText(symptom + chip2.getText().toString().trim());
                suggestionEt.requestFocus();
            });
            /*chip2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    Toast.makeText(activity, chip2.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                    buttonView.setEnabled(false);
                }
            });*/
            chipGroup.addView(view);
        }
    }

    public interface SelectCCWithSuggestion {
        void SelectedSuggestion(String selectedText);

        void deselectSuggestion(int id);
    }

}

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
public class SelectRXSuggestion extends AppCompatDialogFragment {

    EditText suggestionEt;
    Button addButton;
    ImageView cancelButton;
    String symptom;
    SelectRXWithSuggestion selectRXWithSuggestion;
    Activity activity;
    ArrayList<String> suggestionList;
    ChipGroup dosageTimeChipGroup,dosageDaysChipGroup,dosageInSituationChipGroup,dosageOrderChipGroup,dosageCountChipGroup;
    boolean hasSuggestion;

    public SelectRXSuggestion(String symptom, Activity activity, boolean hasSuggestion, ArrayList<String> sgList) {
        this.symptom = symptom;
        this.activity = activity;
        this.hasSuggestion = hasSuggestion;
        this.suggestionList = sgList;
    }
    public SelectRXSuggestion(String symptom, Activity activity, boolean hasSuggestion, ArrayList<String> sgList,SelectRXWithSuggestion selectRXWithSuggestion) {
        this.symptom = symptom;
        this.activity = activity;
        this.hasSuggestion = hasSuggestion;
        this.suggestionList = sgList;
        this.selectRXWithSuggestion=selectRXWithSuggestion;
    }

    public SelectRXSuggestion(String symptom, Activity activity, boolean hasSuggestion) {
        this.symptom = symptom;
        this.activity = activity;
        this.hasSuggestion = hasSuggestion;
    }
    public SelectRXSuggestion(String symptom, Activity activity, boolean hasSuggestion,SelectRXWithSuggestion selectRXWithSuggestion) {
        this.symptom = symptom;
        this.activity = activity;
        this.hasSuggestion = hasSuggestion;
        this.selectRXWithSuggestion=selectRXWithSuggestion;
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.selecct_sugegstion_rx, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

//        suggestionList = new ArrayList<>();

        suggestionEt = view.findViewById(R.id.suggestionEt);
        addButton = view.findViewById(R.id.addS);
        cancelButton = view.findViewById(R.id.closeDialog);
        dosageTimeChipGroup = view.findViewById(R.id.cgSuggestion);
        dosageOrderChipGroup = view.findViewById(R.id.cgSuggestionDosageOrder);
        dosageInSituationChipGroup = view.findViewById(R.id.cgSuggestionInSituation);
        dosageCountChipGroup = view.findViewById(R.id.cgSuggestionDosageCount);
        dosageDaysChipGroup = view.findViewById(R.id.cgSuggestionDosageDays);

        suggestionEt.setText(symptom);

        addButton.setOnClickListener(v -> {
            try {
                if (!suggestionEt.getText().toString().equals(symptom)) {
                    selectRXWithSuggestion.SelectedSuggestion(suggestionEt.getText().toString().trim());
                } else {
                    Toast.makeText(activity, "You have no selected item", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());
        ArrayList<String>count=new ArrayList<>();
        count.add("১টি করে");
        count.add("২টি করে");
        count.add("৩টি করে");
        count.add("অর্ধেক করে");
        count.add("৩ ভাগের ১ ভাগ করে");
        count.add("৪ ভাগের ১ ভাগ করে");
        ArrayList<String>order=new ArrayList<>();
        order.add("খাওয়ার আগে");
        order.add("খাওয়ার পরে");
        ArrayList<String>inSituation=new ArrayList<>();
        inSituation.add("জ্বর থাকলে");
        inSituation.add("ব্যথা থাকলে");
        inSituation.add("মাথাব্যথা হলে");
        ArrayList<String>days=new ArrayList<>();
        days.add("৩ দিন");
        days.add("৫ দিন");
        days.add("৭ দিন");
        days.add("১০ দিন");
        days.add("১৪ দিন");
        days.add("১৫ দিন");
        days.add("২০ দিন");
        days.add("২৫ দিন");
        days.add("১ মাস");
        days.add("দেড় মাস");
        days.add("২ মাস");
        days.add("৩ মাস");
        SetChipGroup(suggestionList, dosageTimeChipGroup);
        SetChipGroupCount(count, dosageCountChipGroup);
        SetChipGroupOrder(order, dosageOrderChipGroup);
        SetChipGroupInSituation(inSituation, dosageInSituationChipGroup);
        SetChipGroupDays(days, dosageDaysChipGroup);
        return alert;
    }

    String doseTime ="";
    String dosageDays="",dosageInSituation="",dosageOrder="",dosageCount="";

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            if(selectRXWithSuggestion==null) {
                selectRXWithSuggestion = (SelectRXWithSuggestion) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SelectRXWithSuggestion");
        }
    }

    @SuppressLint("SetTextI18n")
    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup) {
        for (int i = 0; i < mainList.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d("CHIP_CGs", "onCreate: SetChipGroup " + mainList.get(i));
            chip2.setText(mainList.get(i));
            chip2.setId(i);
            chipGroup.setSingleSelection(true);
            chip2.setOnClickListener(v -> {
                    doseTime =chip2.getText().toString().trim();
                    suggestionEt.setText(symptom + doseTime+" "+dosageCount+" "+dosageOrder+" "+dosageDays+" খাবেন "+dosageInSituation);
                    suggestionEt.requestFocus();
            });
            chipGroup.addView(view);
        }
    } @SuppressLint("SetTextI18n")
    public void SetChipGroupCount(List<String> mainList, ChipGroup chipGroup) {
        for (int i = 0; i < mainList.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d("CHIP_CGs", "onCreate: SetChipGroup " + mainList.get(i));
            chip2.setText(mainList.get(i));
            chip2.setId(i);
            chipGroup.setSingleSelection(true);
            chip2.setOnClickListener(v -> {
                    dosageCount =chip2.getText().toString().trim();
                    suggestionEt.setText(symptom + doseTime+" "+dosageCount+" "+dosageOrder+" "+dosageDays+" খাবেন "+dosageInSituation);
                    suggestionEt.requestFocus();
            });
            chipGroup.addView(view);
        }
    } @SuppressLint("SetTextI18n")
    public void SetChipGroupOrder(List<String> mainList, ChipGroup chipGroup) {
        for (int i = 0; i < mainList.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d("CHIP_CGs", "onCreate: SetChipGroup " + mainList.get(i));
            chip2.setText(mainList.get(i));
            chip2.setId(i);
            chipGroup.setSingleSelection(true);
            chip2.setOnClickListener(v -> {
                    dosageOrder =chip2.getText().toString().trim();
                    suggestionEt.setText(symptom + doseTime+" "+dosageCount+" "+dosageOrder+" "+dosageDays+" খাবেন "+dosageInSituation);
                    suggestionEt.requestFocus();
            });
            chipGroup.addView(view);
        }
    } @SuppressLint("SetTextI18n")
    public void SetChipGroupInSituation(List<String> mainList, ChipGroup chipGroup) {
        for (int i = 0; i < mainList.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d("CHIP_CGs", "onCreate: SetChipGroup " + mainList.get(i));
            chip2.setText(mainList.get(i));
            chip2.setId(i);
            chipGroup.setSingleSelection(true);
            chip2.setOnClickListener(v -> {
                    dosageInSituation ="("+chip2.getText().toString().trim()+")";
                    suggestionEt.setText(symptom + doseTime+" "+dosageCount+" "+dosageOrder+" "+dosageDays+" খাবেন "+dosageInSituation);
                    suggestionEt.requestFocus();
            });
            chipGroup.addView(view);
        }
    } @SuppressLint("SetTextI18n")
    public void SetChipGroupDays(List<String> mainList, ChipGroup chipGroup) {
        for (int i = 0; i < mainList.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d("CHIP_CGs", "onCreate: SetChipGroup " + mainList.get(i));
            chip2.setText(mainList.get(i));
            chip2.setId(i);
            chipGroup.setSingleSelection(true);
            chip2.setOnClickListener(v -> {
                dosageDays =chip2.getText().toString().trim();
                suggestionEt.setText(symptom + doseTime+" "+dosageCount+" "+dosageOrder+" "+dosageDays+" খাবেন "+dosageInSituation);
                suggestionEt.requestFocus();
            });
            chipGroup.addView(view);
        }
    }

    public interface SelectRXWithSuggestion {
        void SelectedSuggestion(String selectedText);

        void deselectSuggestion(int id);
    }

}

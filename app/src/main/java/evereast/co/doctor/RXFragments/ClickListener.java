package evereast.co.doctor.RXFragments;

import android.app.Activity;

import java.util.ArrayList;

public interface ClickListener {
        void OnGenericItemClick(String symptom, Activity activity, boolean hasSuggestion, ArrayList<String> sgList);
        void OnMedicineItemClick(String symptom, Activity activity, boolean hasSuggestion, ArrayList<String> sgList);
    }
package evereast.co.doctor.RXFragments;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_CC;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Utils.GenerateListFromStringList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.DialogFragment.SelectSuggestion;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.CCActivity;
import evereast.co.doctor.databinding.FragmentCCBinding;

public class CCFragment extends Fragment implements SelectSuggestion.SelectCCWithSuggestion {
    public CCFragment() {
        // Required empty public constructor
    }
    FragmentCCBinding binding;
    View root;

    ArrayList<String> ccList;


    String list;
    ImageButton nextButton;
    ImageButton previousButton;
    SharedPreferences sharedPreferences;
    AutoCompleteTextView searchET;
    ArrayList<String> selectedList,suggestionList;
    ChipGroup mainChipGroup, selectedChipGroup;
    Button searchBt;

    ArrayAdapter<String> arrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCCBinding.inflate(inflater,container,false);
        root = binding.getRoot();
        ccList = new ArrayList<>();

        sharedPreferences = root.getContext().getSharedPreferences(AIR_PREFERENCE_CC, MODE_PRIVATE);
        list = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");

        selectedList = new ArrayList<>();

        searchET = root.findViewById(R.id.search_items);
        mainChipGroup = root.findViewById(R.id.cgNormal);
        selectedChipGroup = root.findViewById(R.id.cgSelected);
        searchBt = root.findViewById(R.id.addBT);


         arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, ccList);
        searchET.setThreshold(1);
        searchET.setAdapter(arrayAdapter);

        try {
            if (!list.isEmpty()) {
                selectedList = GenerateListFromStringList(list.trim());
                Log.i("TAG_L", "onCreate: " + list);
                SetChipGroup(selectedList, selectedChipGroup, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        suggestionList=new ArrayList<>();

        suggestionList.add("2 days");
        suggestionList.add("5 days");
        suggestionList.add("10 days");
        suggestionList.add("15 days");
        suggestionList.add("20 days");
        suggestionList.add("25 days");
        suggestionList.add("30 days");
        suggestionList.add("1 months");
        suggestionList.add("2 months");
        suggestionList.add("3 months");
        suggestionList.add("4 months");
        suggestionList.add("5 months");


        searchET.setOnItemClickListener((parent, view, position, id) -> {
            SelectSuggestion selectSuggestion = new SelectSuggestion(parent.getItemAtPosition(position)+" for ", getActivity(),true,suggestionList,this);
            selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
            searchET.setText("");
        });
        searchBt.setOnClickListener(v -> {
            if (!searchET.getText().toString().trim().isEmpty()) {
                SelectSuggestion selectSuggestion = new SelectSuggestion(searchET.getText().toString().trim()+" for ", getActivity(),true,suggestionList,this);
                selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                searchET.setText("");
            }
        });


        return root;
    }

    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup, boolean isSelected) {

        if (isSelected) {
            chipGroup.removeAllViews();
            for (int i = 0; i < mainList.size(); i++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.chip_custom, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i));
                chip2.setId(i);
                chip2.setChecked(true);
                chip2.setCheckable(false);
                chip2.setCloseIconEnabled(true);
                chip2.setOnCloseIconClickListener(view1 -> {
                    chipGroup.removeView(view1);
                    Close(view1.getId());
                });
                chipGroup.addView(view);
            }
        } else {
            for (int i = 0; i < (mainList.size() > 4 ? 5 : mainList.size()); i++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.chip_custom, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i).trim());
                chip2.setId(i);
                chip2.setCheckable(false);
                chip2.setOnClickListener(v -> {
                    SelectSuggestion selectSuggestion = new SelectSuggestion(chip2.getText().toString().trim()+" for ", getActivity(),true,suggestionList,this);
                    selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                });
                chipGroup.addView(view);
            }
        }
    }

    private void Close(int position) {
        selectedList.remove(position);
        SetChipGroup(selectedList, selectedChipGroup, true);
    }

    @Override
    public void SelectedSuggestion(String selectedText) {
        selectedList.add(selectedText);
        SetChipGroup(selectedList, selectedChipGroup, true);
    }

    @Override
    public void deselectSuggestion(int id) {

    }

    public boolean GoNext(){
        try {
            SharedPreferences.Editor editor = root.getContext().getSharedPreferences(AIR_PREFERENCE_CC, MODE_PRIVATE).edit();
            if (!selectedList.toString().equals("[]")) {
                editor.putString(AIR_PREFERENCE_LIST, selectedList.toString().trim());
            } else {
                editor.putString(AIR_PREFERENCE_LIST, "");
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            SharedPreferences.Editor editor = root.getContext().getSharedPreferences(AIR_PREFERENCE_CC, MODE_PRIVATE).edit();
            editor.putString(AIR_PREFERENCE_LIST, "");
            editor.apply();
        }
        return true;
    }

    public ArrayList<String> Lists(ArrayList<String>list){
        return list;
    }

    public void UpdateCCData(ArrayList<String> cList) {
        ccList=new ArrayList<>();
        ccList.addAll(cList);
        arrayAdapter.addAll(ccList);
        arrayAdapter.notifyDataSetChanged();

        SetChipGroup(ccList, mainChipGroup, false);
        Log.d(TAG, "UpdateCCData: "+this.ccList.size());
    }

    private static final String TAG = "CCFragment";
}
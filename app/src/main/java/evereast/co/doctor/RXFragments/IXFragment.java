package evereast.co.doctor.RXFragments;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_HO;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_IX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.RXActivities.PPActivity.INTENT_TYPE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.DialogFragment.SelectSuggestion;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.FragmentIXBinding;

public class IXFragment extends Fragment implements SelectSuggestion.SelectCCWithSuggestion {

    public IXFragment() {
        // Required empty public constructor
    }

    FragmentIXBinding binding;

    String list;
    ImageButton nextButton;
    ImageButton previousButton;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    ArrayList<String> selectedList;
    AutoCompleteTextView searchET;
    ArrayList<String> mainList;
    ChipGroup mainChipGroup, selectedChipGroup;
    Button searchBt;
    ProgressBar progressBar;
    TextView discountTextView;
    ArrayAdapter<String> arrayAdapter;
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentIXBinding.inflate(inflater,container,false);
        root=binding.getRoot();
        sharedPreferences = root.getContext().getSharedPreferences(AIR_PREFERENCE_IX, MODE_PRIVATE);
        list = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");

        selectedList = new ArrayList<>();
        mainList = new ArrayList<>();

        nextButton = root.findViewById(R.id.nextButton);
        previousButton = root.findViewById(R.id.previousButton);
        searchET = root.findViewById(R.id.search_items);
        mainChipGroup = root.findViewById(R.id.cgNormal);
        selectedChipGroup = root.findViewById(R.id.cgSelected);
        searchBt = root.findViewById(R.id.searchBt);
        arrayAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1, mainList);
        searchET.setThreshold(1);
        searchET.setAdapter(arrayAdapter);

        searchET.setOnItemClickListener((parent, view, position1, id) -> {
            if (parent.getItemAtPosition(position1).equals("Add New")) {
                SelectSuggestion selectSuggestion = new SelectSuggestion("", getActivity(), false,this);
                selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                searchET.setText("");
            } else {
                SelectSuggestion selectSuggestion = new SelectSuggestion(parent.getItemAtPosition(position1) + "", getActivity(), false,this);
                selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                searchET.setText("");
            }
        });

        searchBt.setOnClickListener(v -> {
            SelectSuggestion selectSuggestion = new SelectSuggestion(searchET.getText().toString().trim(), getActivity(), false,this);
            selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
        });


        discountTextView = root.findViewById(R.id.discount30);
        discountTextView.setOnClickListener(v -> {
            SelectSuggestion selectSuggestion = new SelectSuggestion("Give 30% Discount", getActivity(), false,this);
            selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
        });

        return root;
    }


    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup, boolean isSelected) {

        if (isSelected) {
            chipGroup.removeAllViews();
            for (int i = 0; i < mainList.size(); i++) {
                View view = LayoutInflater.from(root.getContext()).inflate(R.layout.chip_custom, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i).trim());
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
                View view = LayoutInflater.from(root.getContext()).inflate(R.layout.chip_custom, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i));
                chip2.setId(i);
                chip2.setCheckable(false);
                chip2.setOnClickListener(v -> {
                    SelectSuggestion selectSuggestion = new SelectSuggestion(chip2.getText().toString().trim(), getActivity(), false,this);
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

    public boolean GoNext(){
        try {
            SharedPreferences.Editor editor = root.getContext().getSharedPreferences(AIR_PREFERENCE_IX, MODE_PRIVATE).edit();
            if (!selectedList.toString().equals("[]")) {
                editor.putString(AIR_PREFERENCE_LIST, selectedList.toString().trim());
            } else {
                editor.putString(AIR_PREFERENCE_LIST, "");
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            SharedPreferences.Editor editor = root.getContext().getSharedPreferences(AIR_PREFERENCE_IX, MODE_PRIVATE).edit();
            editor.putString(AIR_PREFERENCE_LIST, "");
            editor.apply();
        }
        return true;
    }


    public void UpdateIXData(ArrayList<String> cList) {
        mainList=new ArrayList<>();
        mainList.addAll(cList);
        arrayAdapter.addAll(mainList);
        arrayAdapter.notifyDataSetChanged();

        SetChipGroup(mainList, mainChipGroup, false);
        Log.d(TAG, "UpdateHOData: "+mainList.size());
    }

    private static final String TAG = "IXFragment";

    @Override
    public void deselectSuggestion(int id) {

    }
}
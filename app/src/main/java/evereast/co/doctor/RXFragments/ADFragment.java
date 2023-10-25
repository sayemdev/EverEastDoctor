package evereast.co.doctor.RXFragments;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_AD;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Utils.GenerateListFromStringList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.DialogFragment.SelectSuggestion;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.FragmentADBinding;


public class ADFragment extends Fragment implements SelectSuggestion.SelectCCWithSuggestion {
    FragmentADBinding fragmentADBinding;
    List<String> mainList, selectedList;

    String selected;
    ChipGroup mainChipGroup, selectedChipGroup;
    SharedPreferences sharedPreferences;
    Context context;
    View root;

    public ADFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentADBinding = FragmentADBinding.inflate(inflater);
        root = fragmentADBinding.getRoot();
        context = root.getContext();

        mainList = new ArrayList<>();
        selectedList = new ArrayList<>();
        sharedPreferences = fragmentADBinding.getRoot().getContext().getSharedPreferences(AIR_PREFERENCE_AD, MODE_PRIVATE);

        mainChipGroup = fragmentADBinding.getRoot().findViewById(R.id.cgNormal);
        selectedChipGroup = fragmentADBinding.getRoot().findViewById(R.id.cgSelected);

        selected = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");

        mainList.add("সিগারেট বর্জন করবেন");
        mainList.add("সামুদ্রিক ও তৈলাক্ত মাছ বেশি করে খাবেন");
        mainList.add("ভাতের সাথে অতিরিক্ত লবন (কাঁচা লবন) খাবেন না");
        mainList.add("ভিটামিন- সি ও ই যুক্ত ফলমূল ও শাক-সবজি বেশি পরিমানে খাবেন");
        mainList.add("এসপিরিন ও ব্যাথানাশক ওষুধ এড়িয়ে চলুন");
        mainList.add("বেলার খাবার বেলায় সঠিক সময়ে খাবেন ও প্রচুর পরিমাণ পানি পান করবেন");
        mainList.add("মদ্যপান বন্ধ করুন");
        mainList.add("উচ্চ প্রোটিনযুক্ত, ফ্যাটবিহিন খাবার খাবেন");
        mainList.add("দুদ্ধজাতীয় খাবার বর্জন করবেন");
        mainList.add("সিগারেট বর্জন করবেন");
        mainList.add("পান খাওয়া নিষেধ। ধূমপান করবেন না");
        mainList.add("মসল্লাযুক্ত খাবার, গরম খাবার খাওয়া থেকে বিরত থাকুন");
        mainList.add("মুখ গহ্বরের পরিচ্ছনতা রক্ষা করতে প্রতিদিন ২বার ব্রাশ করুন");

        try {
            if (!selected.isEmpty()) {
                selectedList = GenerateListFromStringList(selected.trim());
                Log.i("TAG_L", "onCreate: " + selected);
                SetChipGroup(selectedList, selectedChipGroup, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SetChipGroup(mainList, mainChipGroup, false);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(fragmentADBinding.getRoot().getContext(), android.R.layout.simple_list_item_1, mainList);
        fragmentADBinding.searchItems.setThreshold(1);
        fragmentADBinding.searchItems.setAdapter(arrayAdapter);

        fragmentADBinding.searchItems.setOnItemClickListener((parent, view, position1, id) -> {
            SelectSuggestion selectSuggestion = new SelectSuggestion(parent.getItemAtPosition(position1) + "", getActivity(), false, this);
            selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
            fragmentADBinding.searchItems.setText("");
        });

        fragmentADBinding.addBT.setOnClickListener(v -> {
            if (!fragmentADBinding.searchItems.getText().toString().trim().isEmpty()) {
                SelectSuggestion selectSuggestion = new SelectSuggestion(fragmentADBinding.searchItems.getText().toString().trim(), getActivity(), false, this);
                selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                fragmentADBinding.searchItems.setText("");
            }
        });


        return root;
    }


    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup, boolean isSelected) {

        if (isSelected) {
            chipGroup.removeAllViews();
            for (int i = 0; i < mainList.size(); i++) {
                View view = LayoutInflater.from(fragmentADBinding.getRoot().getContext()).inflate(R.layout.chip_custom, null);
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
                View view = LayoutInflater.from(fragmentADBinding.getRoot().getContext()).inflate(R.layout.chip_custom, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i));
                chip2.setId(i);
                chip2.setCheckable(false);
                chip2.setOnClickListener(v -> {
                    SelectSuggestion selectSuggestion = new SelectSuggestion(chip2.getText().toString().trim(), getActivity(), false, this);
                    selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                });
                chipGroup.addView(view);
            }
        }


    }

    public boolean GoToPreview() {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(AIR_PREFERENCE_AD, MODE_PRIVATE).edit();
            if (!selectedList.toString().trim().equals("[]")) {
                editor.putString(AIR_PREFERENCE_LIST, selectedList.toString().trim());
            } else {
                editor.putString(AIR_PREFERENCE_LIST, "");
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            SharedPreferences.Editor editor = context.getSharedPreferences(AIR_PREFERENCE_AD, MODE_PRIVATE).edit();
            editor.putString(AIR_PREFERENCE_LIST, "");
            editor.apply();
        }

        return true;
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

}
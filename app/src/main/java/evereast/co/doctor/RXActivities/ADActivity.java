package evereast.co.doctor.RXActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.DialogFragment.EndSessionConfirmation;
import evereast.co.doctor.DialogFragment.SelectSuggestion;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_AD;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.RXActivities.PPActivity.INTENT_TYPE;
import static evereast.co.doctor.Utils.GenerateListFromStringList;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationPrevious;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationUpdated;

public class ADActivity extends AppCompatActivity implements SelectSuggestion.SelectCCWithSuggestion,EndSessionConfirmation.ConfirmationValueListener {
    TextView pp, cc, ho, ix, dx, rx, ad, fu;
    String list;
    ImageButton nextButton;
    ImageButton previousButton;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    ArrayList<String> selectedList;
    AutoCompleteTextView searchET;
    ArrayList<String> mainList;
    ChipGroup mainChipGroup, selectedChipGroup;
    ImageView searchBt;
    Button addBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_d);

        sharedPreferences = getSharedPreferences(AIR_PREFERENCE_AD, MODE_PRIVATE);
        list = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");

        selectedList = new ArrayList<>();
        mainList = new ArrayList<>();

        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        searchET = findViewById(R.id.search_items);
        mainChipGroup = findViewById(R.id.cgNormal);
        selectedChipGroup = findViewById(R.id.cgSelected);
        searchBt = findViewById(R.id.searchBt);
        addBT = findViewById(R.id.addBT);

        pp = findViewById(R.id.pp);
        cc = findViewById(R.id.cc);
        ho = findViewById(R.id.ho);
        ix = findViewById(R.id.ix);
        dx = findViewById(R.id.dx);
        rx = findViewById(R.id.rx);
        ad = findViewById(R.id.ad);
        fu = findViewById(R.id.fu);

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
            if (!list.isEmpty()) {
                selectedList = GenerateListFromStringList(list.trim());
                Log.i("TAG_L", "onCreate: " + list);
                SetChipGroup(selectedList, selectedChipGroup, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ad.setBackgroundResource(R.drawable.circle_for_air);
        ad.setTextColor(getResources().getColor(R.color.white));
        ad.setEnabled(false);

        pp.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, PPActivity.class, previousButton, AIR_PREFERENCE_AD, selectedList.toString());
        });

        cc.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, CCActivity.class, previousButton, AIR_PREFERENCE_AD, selectedList.toString());
        });

        ix.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, IXActivity.class, previousButton, AIR_PREFERENCE_AD, selectedList.toString());
        });
        dx.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, DXActivity.class, previousButton, AIR_PREFERENCE_AD, selectedList.toString());
        });

        rx.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, RXActivity.class, previousButton, AIR_PREFERENCE_AD, selectedList.toString());
        });

        ho.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, HOActivity.class, previousButton, AIR_PREFERENCE_AD, selectedList.toString());
        });

        fu.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, FUActivity.class, selectedChipGroup, AIR_PREFERENCE_AD, selectedList.toString());
        });

        SetChipGroup(mainList, mainChipGroup, false);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mainList);
        searchET.setThreshold(1);
        searchET.setAdapter(arrayAdapter);

        searchET.setOnItemClickListener((parent, view, position1, id) -> {
            SelectSuggestion selectSuggestion = new SelectSuggestion(parent.getItemAtPosition(position1) + "", this, false);
            selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
            searchET.setText("");
        });

        addBT.setOnClickListener(v -> {
            if (!searchET.getText().toString().trim().isEmpty()) {
                SelectSuggestion selectSuggestion = new SelectSuggestion(searchET.getText().toString().trim(), this, false);
                selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
                searchET.setText("");
            }
        });

        try {
            if (!sharedPreferences.getString(INTENT_TYPE, "Str").equals("appointment")) {
                pp.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Previous(View view) {
        RedirectToActivityWithAnimationUpdated(this, RXActivity.class, selectedChipGroup, AIR_PREFERENCE_AD, selectedList.toString());
        finish();
    }

    public void Next(View view) {
/*
        RedirectToActivityWithAnimationUpdated(this, FUActivity.class, selectedChipGroup, AIR_PREFERENCE_DX, selectedList.toString());
        finish();
*/
        try {
            SharedPreferences.Editor editor = getSharedPreferences(AIR_PREFERENCE_AD, MODE_PRIVATE).edit();
            if (! selectedList.toString().trim().equals("[]")) {
                editor.putString(AIR_PREFERENCE_LIST,  selectedList.toString().trim());
            } else {
                editor.putString(AIR_PREFERENCE_LIST, "");
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            SharedPreferences.Editor editor = getSharedPreferences(AIR_PREFERENCE_AD, MODE_PRIVATE).edit();
            editor.putString(AIR_PREFERENCE_LIST, "");
            editor.apply();
        }
        startActivity(new Intent(this, PreviewActivity.class));
    }

    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup, boolean isSelected) {

        if (isSelected) {
            chipGroup.removeAllViews();
            for (int i = 0; i < mainList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.chip_custom, null);
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
                View view = LayoutInflater.from(this).inflate(R.layout.chip_custom, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i));
                chip2.setId(i);
                chip2.setCheckable(false);
                chip2.setOnClickListener(v -> {
                    SelectSuggestion selectSuggestion = new SelectSuggestion(chip2.getText().toString().trim(), this, false);
                    selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
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

    public void EndSession(View view) {
        EndSessionConfirmation endSessionConfirmation = new EndSessionConfirmation();
        endSessionConfirmation.show(getSupportFragmentManager(), endSessionConfirmation.getTag());
    }

    @Override
    public void Confirmation(boolean confirm) {

    }
}
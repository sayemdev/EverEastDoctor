package evereast.co.doctor.RXActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.DialogFragment.EndSessionConfirmation;
import evereast.co.doctor.DialogFragment.SelectSuggestion;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_CC;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.RXActivities.PPActivity.INTENT_TYPE;
import static evereast.co.doctor.Utils.GenerateListFromStringList;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationPrevious;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationUpdated;

public class CCActivity extends AppCompatActivity implements SelectSuggestion.SelectCCWithSuggestion,EndSessionConfirmation.ConfirmationValueListener {

    TextView pp, cc, ho, ix, dx, rx, ad, fu;
    String list;
    ImageButton nextButton;
    ImageButton previousButton;
    SharedPreferences sharedPreferences;
    AutoCompleteTextView searchET;
    ArrayList<String> mainList,selectedList,suggestionList;
    ChipGroup mainChipGroup, selectedChipGroup;
    Button searchBt;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_c);
        sharedPreferences = getSharedPreferences(AIR_PREFERENCE_CC, MODE_PRIVATE);
        list = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");

        selectedList = new ArrayList<>();
        mainList = new ArrayList<>();

        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        searchET = findViewById(R.id.search_items);
        mainChipGroup = findViewById(R.id.cgNormal);
        selectedChipGroup = findViewById(R.id.cgSelected);
        searchBt = findViewById(R.id.addBT);
        progressBar=findViewById(R.id.loader);

        pp = findViewById(R.id.pp);
        cc = findViewById(R.id.cc);
        ho = findViewById(R.id.ho);
        ix = findViewById(R.id.ix);
        dx = findViewById(R.id.dx);
        rx = findViewById(R.id.rx);
        ad = findViewById(R.id.ad);
        fu = findViewById(R.id.fu);


        GetCC();

        try {
            if (!list.isEmpty()) {
                selectedList = GenerateListFromStringList(list.trim());
                Log.i("TAG_L", "onCreate: " + list);
                SetChipGroup(selectedList, selectedChipGroup, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cc.setBackgroundResource(R.drawable.circle_for_air);
        cc.setTextColor(getResources().getColor(R.color.white));
        cc.setEnabled(false);

        pp.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, PPActivity.class, previousButton, AIR_PREFERENCE_CC, selectedList.toString());
        });

        ho.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, HOActivity.class, selectedChipGroup, AIR_PREFERENCE_CC, selectedList.toString());
        });

        ix.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, IXActivity.class, selectedChipGroup, AIR_PREFERENCE_CC, selectedList.toString());
        });
        dx.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, DXActivity.class, selectedChipGroup, AIR_PREFERENCE_CC, selectedList.toString());
        });

        rx.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, RXActivity.class, selectedChipGroup, AIR_PREFERENCE_CC, selectedList.toString());
        });

        ad.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, ADActivity.class, selectedChipGroup, AIR_PREFERENCE_CC, selectedList.toString());
        });

        fu.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, FUActivity.class, selectedChipGroup, AIR_PREFERENCE_CC, selectedList.toString());
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mainList);
        searchET.setThreshold(1);
        searchET.setAdapter(arrayAdapter);
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
            SelectSuggestion selectSuggestion = new SelectSuggestion(parent.getItemAtPosition(position)+" for ", CCActivity.this,true,suggestionList);
            selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
            searchET.setText("");
        });
        searchBt.setOnClickListener(v -> {
            if (!searchET.getText().toString().trim().isEmpty()) {
                SelectSuggestion selectSuggestion = new SelectSuggestion(searchET.getText().toString().trim()+" for ", CCActivity.this,true,suggestionList);
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

    private void GetCC() {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, BASE_URL + "get_cc.php",
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        mainList.clear();
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            mainList.add(jsonObject.getString("cc_ho_dx").trim());
                            Log.i("TAG", "GetIX: "+jsonArray.getJSONObject(i).getString("cc_ho_dx"));
                        }
                        SetChipGroup(mainList, mainChipGroup, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }));
    }


    public void Previous(View view) {
        RedirectToActivityWithAnimationUpdated(this, PPActivity.class, selectedChipGroup, AIR_PREFERENCE_CC, selectedList.toString());
    }

    public void Next(View view) {
        RedirectToActivityWithAnimationUpdated(this, HOActivity.class, selectedChipGroup, AIR_PREFERENCE_CC, selectedList.toString());
    }

    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup, boolean isSelected) {

        if (isSelected) {
            chipGroup.removeAllViews();
            for (int i = 0; i < mainList.size(); i++) {
                View view = LayoutInflater.from(CCActivity.this).inflate(R.layout.chip_custom, null);
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
                View view = LayoutInflater.from(CCActivity.this).inflate(R.layout.chip_custom, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i).trim());
                chip2.setId(i);
                chip2.setCheckable(false);
                chip2.setOnClickListener(v -> {
                    SelectSuggestion selectSuggestion = new SelectSuggestion(chip2.getText().toString().trim()+" for ", CCActivity.this,true,suggestionList);
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
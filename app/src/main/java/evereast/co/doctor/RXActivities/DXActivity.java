package evereast.co.doctor.RXActivities;

import androidx.appcompat.app.AppCompatActivity;

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

import static evereast.co.doctor.Constants.AIR_PREFERENCE_DX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.RXActivities.PPActivity.INTENT_TYPE;
import static evereast.co.doctor.Utils.GenerateListFromStringList;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationPrevious;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationUpdated;

public class DXActivity extends AppCompatActivity implements SelectSuggestion.SelectCCWithSuggestion,EndSessionConfirmation.ConfirmationValueListener{
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
    Button searchBt;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_x);

        sharedPreferences = getSharedPreferences(AIR_PREFERENCE_DX, MODE_PRIVATE);
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

/*        mainList.add("Aphthous Ulcer");
        mainList.add("Candidiasis (Oral Thrush)");
        mainList.add("GERD");
        mainList.add("Ca Oesophagus");
        mainList.add("Barrett's oesophgus");
        mainList.add("Hiatus hernia");
        mainList.add("Carcinoma of oesophagus");
        mainList.add("Duodenal ulcer");
        mainList.add("Gastric ulcer");
        mainList.add("Nonulcer dyspepsia");
        mainList.add("Zollinger-Ellison syndrome");
        mainList.add("Pyloric stenosis");
        mainList.add("Carcinoma of stomach");*/

        GetDX();



        try {
            if (!list.isEmpty()) {
                selectedList = GenerateListFromStringList(list.trim());
                Log.i("TAG_L", "onCreate: " + list);
                SetChipGroup(selectedList, selectedChipGroup, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dx.setBackgroundResource(R.drawable.circle_for_air);
        dx.setTextColor(getResources().getColor(R.color.white));
        dx.setEnabled(false);

        pp.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, PPActivity.class, previousButton, AIR_PREFERENCE_DX, selectedList.toString());
        });

        cc.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, CCActivity.class, previousButton, AIR_PREFERENCE_DX, selectedList.toString());
        });

        ho.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, HOActivity.class, previousButton, AIR_PREFERENCE_DX, selectedList.toString());
        });
        ix.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, IXActivity.class, previousButton, AIR_PREFERENCE_DX, selectedList.toString());
        });

        rx.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, RXActivity.class, selectedChipGroup, AIR_PREFERENCE_DX, selectedList.toString());
        });

        ad.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, ADActivity.class, selectedChipGroup, AIR_PREFERENCE_DX, selectedList.toString());
        });

        fu.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, FUActivity.class, selectedChipGroup, AIR_PREFERENCE_DX, selectedList.toString());
        });

        SetChipGroup(mainList, mainChipGroup, false);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mainList);
        searchET.setThreshold(1);
        searchET.setAdapter(arrayAdapter);

        searchET.setOnItemClickListener((parent, view, position1, id) -> {
            SelectSuggestion selectSuggestion = new SelectSuggestion(parent.getItemAtPosition(position1)+"", this, false);
            selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
            searchET.setText("");
        });

        searchBt.setOnClickListener(v -> {
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

    private void GetDX() {
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
        RedirectToActivityWithAnimationUpdated(this, IXActivity.class, selectedChipGroup, AIR_PREFERENCE_DX, selectedList.toString());
        finish();
    }

    public void Next(View view) {
        RedirectToActivityWithAnimationUpdated(this, RXActivity.class, selectedChipGroup, AIR_PREFERENCE_DX, selectedList.toString());
        finish();
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
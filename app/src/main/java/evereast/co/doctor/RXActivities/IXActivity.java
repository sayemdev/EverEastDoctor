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
import evereast.co.doctor.PrescriptionDB.AlarmDatabaseClient;
import evereast.co.doctor.PrescriptionDB.IxEntityModel;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_IX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.RXActivities.PPActivity.INTENT_TYPE;
import static evereast.co.doctor.Utils.GenerateListFromStringList;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationPrevious;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationUpdated;

public class IXActivity extends AppCompatActivity implements SelectSuggestion.SelectCCWithSuggestion, EndSessionConfirmation.ConfirmationValueListener {
    private static final String TAG = "IXActivity";
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
    TextView discountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_x);

        sharedPreferences = getSharedPreferences(AIR_PREFERENCE_IX, MODE_PRIVATE);
        list = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");

        selectedList = new ArrayList<>();
        mainList = new ArrayList<>();

        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        searchET = findViewById(R.id.search_items);
        mainChipGroup = findViewById(R.id.cgNormal);
        selectedChipGroup = findViewById(R.id.cgSelected);
        searchBt = findViewById(R.id.searchBt);
        progressBar = findViewById(R.id.loader);

        pp = findViewById(R.id.pp);
        cc = findViewById(R.id.cc);
        ho = findViewById(R.id.ho);
        ix = findViewById(R.id.ix);
        dx = findViewById(R.id.dx);
        rx = findViewById(R.id.rx);
        ad = findViewById(R.id.ad);
        fu = findViewById(R.id.fu);

        GetIX();

        try {
            if (!list.isEmpty()) {
                selectedList = GenerateListFromStringList(list.trim());
                Log.i("TAG_L", "onCreate: " + list);
                SetChipGroup(selectedList, selectedChipGroup, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ix.setBackgroundResource(R.drawable.circle_for_air);
        ix.setTextColor(getResources().getColor(R.color.white));
        ix.setEnabled(false);

        pp.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, PPActivity.class, previousButton, AIR_PREFERENCE_IX, selectedList.toString());
        });

        cc.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, CCActivity.class, previousButton, AIR_PREFERENCE_IX, selectedList.toString());
        });

        ho.setOnClickListener(v -> {
            RedirectToActivityWithAnimationPrevious(this, HOActivity.class, previousButton, AIR_PREFERENCE_IX, selectedList.toString());
        });
        dx.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, DXActivity.class, selectedChipGroup, AIR_PREFERENCE_IX, selectedList.toString());
        });

        rx.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, RXActivity.class, selectedChipGroup, AIR_PREFERENCE_IX, selectedList.toString());
        });

        ad.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, ADActivity.class, selectedChipGroup, AIR_PREFERENCE_IX, selectedList.toString());
        });

        fu.setOnClickListener(v -> {
            RedirectToActivityWithAnimationUpdated(this, FUActivity.class, selectedChipGroup, AIR_PREFERENCE_IX, selectedList.toString());
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mainList);
        searchET.setThreshold(1);
        searchET.setAdapter(arrayAdapter);

        searchET.setOnItemClickListener((parent, view, position1, id) -> {
            if (parent.getItemAtPosition(position1).equals("Add New")) {
                SelectSuggestion selectSuggestion = new SelectSuggestion("", this, false);
                selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
                searchET.setText("");
            } else {
                SelectSuggestion selectSuggestion = new SelectSuggestion(parent.getItemAtPosition(position1) + "", this, false);
                selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
                searchET.setText("");
            }
        });

        searchBt.setOnClickListener(v -> {
            SelectSuggestion selectSuggestion = new SelectSuggestion(searchET.getText().toString().trim(), this, false);
            selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
        });

        try {
            if (!sharedPreferences.getString(INTENT_TYPE, "Str").equals("appointment")) {
                pp.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        discountTextView = findViewById(R.id.discount30);
        discountTextView.setOnClickListener(v -> {
            SelectSuggestion selectSuggestion = new SelectSuggestion("Give 30% Discount", this, false);
            selectSuggestion.show(getSupportFragmentManager(), selectSuggestion.getTag());
        });
    }

    private void GetIX() {
        List<IxEntityModel> productCartModelList = AlarmDatabaseClient.getInstance(getApplicationContext()).getCartDatabase().cartDao().OfflineIXList();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, BASE_URL + "get_ix.php",
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        mainList.clear();
                        AlarmDatabaseClient.getInstance(this).getCartDatabase().cartDao().DeleteAllCart();
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mainList.add(jsonObject.getString("investigation"));
                            Log.i("TAG", "GetIX: " + jsonArray.getJSONObject(i).getString("investigation"));
                            IxEntityModel ixEntityModel = new IxEntityModel();
                            ixEntityModel.setInvestigation(jsonObject.getString("investigation"));
                            AlarmDatabaseClient.getInstance(this).getCartDatabase().cartDao().AddToIXList(ixEntityModel);
                        }
                        mainList.add("Add New");
                        SetChipGroup(mainList, mainChipGroup, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mainList.clear();
                        for (int i = 0; i < productCartModelList.size(); i++) {
                            mainList.add(productCartModelList.get(i).getInvestigation());
                        }
                        mainList.add("Add New");
                        SetChipGroup(mainList, mainChipGroup, false);
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    mainList.clear();
                    for (int i = 0; i < productCartModelList.size(); i++) {
                        mainList.add(productCartModelList.get(i).getInvestigation());
                    }
                    mainList.add("Add New");
                    SetChipGroup(mainList, mainChipGroup, false);
                }));

    }

    public void Previous(View view) {
        RedirectToActivityWithAnimationUpdated(this, HOActivity.class, selectedChipGroup, AIR_PREFERENCE_IX, selectedList.toString());
        finish();
    }

    public void Next(View view) {
        RedirectToActivityWithAnimationUpdated(this, DXActivity.class, selectedChipGroup, AIR_PREFERENCE_IX, selectedList.toString());
        finish();
    }

    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup, boolean isSelected) {

        if (isSelected) {
            chipGroup.removeAllViews();
            for (int i = 0; i < mainList.size(); i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.chip_custom, null);
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
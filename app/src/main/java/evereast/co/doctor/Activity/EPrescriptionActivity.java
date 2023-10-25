package evereast.co.doctor.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import evereast.co.doctor.DialogFragment.GenderDialogFragment;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_AD;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_CC;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_DX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_HO;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_IX;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_PP;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_RX;
import static evereast.co.doctor.Constants.P_NAME;

public class EPrescriptionActivity extends AppCompatActivity implements GenderDialogFragment.GenderValueListener {

    private static final String AGE = "AGE", AGE_TYPE = "AGE_TYPE", DATE = "DATE", GENDER = "GENDER", PHONE = "ID", ADDRESS = "ADDRESS";
    private static final String CHIP_CGs = "CHIP_CGs";
    private static final String CHIEF_COMPLAINT = "CHIEF_COMPLAINT";
    private final int positionHO = 0;
    SharedPreferences.Editor airEditorPP, airEditorCC, airEditorHO, editorIX, editorDX, editorRX, editorAD, editorFU;
    SharedPreferences sharedPreferencesPP, sharedPreferencesCC, sharedPreferencesHO, sharedPreferencesIX, sharedPreferencesDX, sharedPreferencesRX, sharedPreferencesAD, sharedPreferencesFU;
    TextView pp, cc, ho, ix, dx, rx, ad, fu;
    ImageButton previous, next;
    String state = "pp";
    LinearLayout ppLayout, ccLayout, hoLayout, ixLayout, dxLayout, rxLayout, adLayout, fuLayout;
    EditText nameEt, genderEt, dateEt, phoneEt, addressEt;
    Spinner ageSpinner, ageTypeSpinner;
    ArrayAdapter<String> arrayAdapter;
    List<String> stringList, ccList, hoList, ixList, dxList, rxList, adList, fuList;
    List<String> stringListSelected, ccListSelected, hoListSelected, ixListSelected, dxListSelected, rxListSelected, adListSelected, fuListSelected;
    Calendar calendar;
    int day;
    int month;
    int year;
    StringBuilder stringBuilder;
    String name, age, ageType, dateString, gender, phone, address;
    Chip chipForCC, chipForHo, chipForIx;
    private ChipGroup chipGroupCC, chipGroupHO, chipGroupIX, chipGroupDX,chipGroupRX,chipGroupAD;
    private int position;
    private String time;
    private Chip chipCl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_prescription);

        sharedPreferencesPP = getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE);
        name = sharedPreferencesPP.getString(P_NAME, "");
        age = sharedPreferencesPP.getString(AGE, "");
        ageType = sharedPreferencesPP.getString(AGE_TYPE, "");
        dateString = sharedPreferencesPP.getString(DATE, "");
        gender = sharedPreferencesPP.getString(GENDER, "");
        phone = sharedPreferencesPP.getString(PHONE, "");
        address = sharedPreferencesPP.getString(ADDRESS, "");

        sharedPreferencesCC = getSharedPreferences(AIR_PREFERENCE_CC, MODE_PRIVATE);

        stringList = new ArrayList<>();
        ccList = new ArrayList<>();
        ccList.add("Painful mouth ulcer");
        ccList.add("Multiple white patches over tongue");
        ccList.add("Heart burn");
        ccList.add("Chest pain");
        ccList.add("Regurgitation");
        ccList.add("Dysphagia");
        ccList.add("Retrosternal discomfort");
        ccList.add("Anorexia");
        ccList.add("Weight loss");
        ccList.add("Nausea");
        ccList.add("Vomiting");
        ccList.add("Epigastric fullness");
        ccList.add("Pain in the abdomen");
        ccList.add("Haematemesis");
        ccList.add("Melaena");
        ccList.add("Pain in the epigastrium");
        ccList.add("Heart burn");
        ccList.add("Dyspepsia");
        ccList.add("Odynophagia");
        ccList.add("Haematemesis");
        ccList.add("Haematochezia");

        hoList = new ArrayList<>();
        hoList.add("Inflammatory bowel disease");
        hoList.add("Emotional stress");
        hoList.add("Poor oral hygiene");
        hoList.add("Diabetes mellitus");
        hoList.add("Prolonged use of antibiotic");
        hoList.add("Prolonged use of steroid");
        hoList.add("Smoking");
        hoList.add("Alcohol");
        hoList.add("Pregnancy");
        hoList.add("Obesity");
        hoList.add("Betel nuts");
        hoList.add("Alcoholism");
        hoList.add("Barrett's oesophagus");
        hoList.add("NSAID's");


        ixList = new ArrayList<>();
        ixList.add("Barium swallow X-ray of oesophagus");
        ixList.add("Barium swallow of oesophagus");
        ixList.add("CXR");
        ixList.add("Ultrasonography of whole abdomen.");
        ixList.add("MRI");
        ixList.add("Endoscopic USG");
        ixList.add("CBC with ESR");
        ixList.add("Endoscopy");

        dxList = new ArrayList<>();
        rxList = new ArrayList<>();
        adList = new ArrayList<>();
        fuList = new ArrayList<>();

//        SetHO();
//        SetIx();
        SetDx();
        SetRx();
        SetAd();

        ccListSelected = new ArrayList<>();
        hoListSelected = new ArrayList<>();
        ixListSelected = new ArrayList<>();
        dxListSelected = new ArrayList<>();
        rxListSelected = new ArrayList<>();
        adListSelected = new ArrayList<>();
        fuListSelected = new ArrayList<>();

        nameEt = findViewById(R.id.nameEt);
        ageSpinner = findViewById(R.id.age);
        ageTypeSpinner = findViewById(R.id.typeSp);
        genderEt = findViewById(R.id.genderEt);
        dateEt = findViewById(R.id.dateEt);
        phoneEt = findViewById(R.id.phoneEt);
        addressEt = findViewById(R.id.addressEt);

        pp = findViewById(R.id.pp);
        cc = findViewById(R.id.cc);
        ho = findViewById(R.id.ho);
        ix = findViewById(R.id.ix);
        dx = findViewById(R.id.dx);
        rx = findViewById(R.id.rx);
        ad = findViewById(R.id.ad);
        fu = findViewById(R.id.fu);
        ppLayout = findViewById(R.id.ppLt);
        ccLayout = findViewById(R.id.ccLt);
        hoLayout = findViewById(R.id.hoLt);
        ixLayout = findViewById(R.id.IXLt);
        dxLayout = findViewById(R.id.dxLt);
        rxLayout = findViewById(R.id.rxLt);
        adLayout = findViewById(R.id.adLt);
        fuLayout = findViewById(R.id.fuLt);
        next = findViewById(R.id.nextButton);
        previous = findViewById(R.id.previousButton);

        chipGroupCC = findViewById(R.id.cgCC);
        chipGroupHO = findViewById(R.id.cgHO);
        chipGroupIX = findViewById(R.id.cgIX);
        chipGroupDX = findViewById(R.id.cgDX);
        chipGroupRX = findViewById(R.id.cgRX);
        chipGroupAD = findViewById(R.id.cgAD);

        int number = 200;
        stringList.add("Select Age");
        for (int i = 1; i <= number; i++) {
            stringList.add(String.valueOf(i));
        }

        arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, stringList);
        ageSpinner.setAdapter(arrayAdapter);


        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = (calendar.get(Calendar.MONTH));
        year = calendar.get(Calendar.YEAR);

        stringBuilder = new StringBuilder();
        stringBuilder.append(day)
                .append("/")
                .append(month + 1)
                .append("/")
                .append(year);

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            calendar.set((Calendar.YEAR), year);
            calendar.set((Calendar.MONTH), monthOfYear);
            calendar.set((Calendar.DAY_OF_MONTH), dayOfMonth);
            view.setMinDate(System.currentTimeMillis() - 1000);
            updateLabel();
        };
        dateEt.setText(stringBuilder);
        dateEt.setOnClickListener(v -> {
            //TODO: To Separate year month day
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
                    .withLocale(Locale.UK);
            LocalDate date2 = formatter.parseLocalDate(dateEt.getText().toString().trim());
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, date2.getYear(), date2.getMonthOfYear() - 1, date2.getDayOfMonth());
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        try {
            if (!name.isEmpty()) {
                nameEt.setText(name);
                ageSpinner.setSelection(Integer.parseInt(age) + 1);

                //Age Type Setting
                switch (ageType) {
                    case "Year":
                        ageTypeSpinner.setSelection(1);
                        break;
                    case "Month":
                        ageTypeSpinner.setSelection(2);
                        break;
                    case "Day":
                        ageTypeSpinner.setSelection(3);
                        break;
                }

                dateEt.setText(dateString);

                genderEt.setText(gender);
                phoneEt.setText(phone);
                addressEt.setText(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        genderEt.setOnClickListener(v -> {
            GenderDialogFragment genderDialogFragment = new GenderDialogFragment();
            genderDialogFragment.show(getSupportFragmentManager(), genderDialogFragment.getTag());

        });

        SetChipGroup(ccList, chipGroupCC);
        SetChipGroup(hoList, chipGroupHO);
        SetChipGroup(ixList, chipGroupIX);
        SetChipGroup(dxList, chipGroupDX);
        SetChipGroup(rxList, chipGroupRX);
        SetChipGroup(adList, chipGroupAD);

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEt.setText(sdf.format(calendar.getTime()));
    }

    @SuppressLint("ResourceAsColor")
    private void SelectBt(TextView textView, LinearLayout linearLayout) {
        textView.setBackgroundResource(R.drawable.circle_for_air);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setElevation(4);
        ScrollView scrollView = findViewById(R.id.scrl);
        scrollView.setSmoothScrollingEnabled(true);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
        if (state.equals("pp")) {
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.GONE);
        } else {
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void Unselected(TextView textView, LinearLayout linearLayout) {
        textView.setBackgroundResource(R.drawable.circle_outlined_for_air);
        textView.setTextColor(getResources().getColor(R.color.app_bar_color));
        textView.setElevation(0);
        linearLayout.setVisibility(View.GONE);
//        linearLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
    }

    public void Next(View view) {
        if (state.equals("pp")) {
            if (nameEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            } else if (ageSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Please select an age", Toast.LENGTH_SHORT).show();
            } else if (ageTypeSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Please select an age type", Toast.LENGTH_SHORT).show();
            } else if (dateEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            } else if (genderEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            } else if (phoneEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter phone", Toast.LENGTH_SHORT).show();
            } else if (addressEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
            } else {
                state = "cc";
                Unselected(pp, ppLayout);
                SelectBt(cc, ccLayout);
                Unselected(ho, hoLayout);
                Unselected(ix, ixLayout);
                Unselected(dx, dxLayout);
                Unselected(rx, rxLayout);
                Unselected(ad, adLayout);
                Unselected(fu, fuLayout);

                airEditorPP = getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE).edit();
                airEditorPP.putString(P_NAME, nameEt.getText().toString().trim());
                airEditorPP.putString(AGE, ageSpinner.getSelectedItem().toString().trim());
                airEditorPP.putString(AGE_TYPE, ageTypeSpinner.getSelectedItem().toString().trim());
                airEditorPP.putString(DATE, dateEt.getText().toString().trim());
                airEditorPP.putString(GENDER, genderEt.getText().toString().trim());
                airEditorPP.putString(PHONE, phoneEt.getText().toString().trim());
                airEditorPP.putString(ADDRESS, addressEt.getText().toString().trim());
                airEditorPP.apply();
            }

        } else if (state.equals("cc")) {
            GetTexts(ccListSelected, ccList, chipGroupCC);
            if (ccListSelected.size() == 0) {
                Toast.makeText(this, "Please select at least one Chief Complaint", Toast.LENGTH_SHORT).show();
            } else {
                airEditorCC = getSharedPreferences(AIR_PREFERENCE_CC, MODE_PRIVATE).edit();
                airEditorCC.putString(CHIEF_COMPLAINT, ccListSelected.toString());
                airEditorCC.apply();

                Log.i("TAG_CC_LIST_SELECTED", "Next: " + ccListSelected);
                state = "ho";
                Unselected(pp, ppLayout);
                Unselected(cc, ccLayout);
                SelectBt(ho, hoLayout);
                Unselected(ix, ixLayout);
                Unselected(dx, dxLayout);
                Unselected(rx, rxLayout);
                Unselected(ad, adLayout);
                Unselected(fu, fuLayout);
            }
        } else if (state.equals("ho")) {
            GetTexts(hoListSelected, hoList, chipGroupHO);
            if (hoListSelected.size() == 0) {
                Toast.makeText(this, "Please Select at least one HO", Toast.LENGTH_SHORT).show();
            } else {
                airEditorHO = getSharedPreferences(AIR_PREFERENCE_HO, MODE_PRIVATE).edit();
                airEditorHO.putString("hoListSelected", hoListSelected.toString());
                airEditorHO.apply();

                Log.i("hoListSelected", "Next: " + hoListSelected);
                state = "ix";
                Unselected(pp, ppLayout);
                Unselected(cc, ccLayout);
                Unselected(ho, hoLayout);
                SelectBt(ix, ixLayout);
                Unselected(dx, dxLayout);
                Unselected(rx, rxLayout);
                Unselected(ad, adLayout);
                Unselected(fu, fuLayout);
            }
        } else if (state.equals("ix")) {
            GetTexts(ixListSelected, ixList, chipGroupIX);
            if (ixListSelected.size() == 0) {
                Toast.makeText(this, "Please Select at least one IX", Toast.LENGTH_SHORT).show();
            } else {
                editorIX = getSharedPreferences(AIR_PREFERENCE_IX, MODE_PRIVATE).edit();
                editorIX.putString("ixListSelected", ixListSelected.toString());
                editorIX.apply();
                Log.i("ixListSelected", "Next: " + ixListSelected);
                state = "dx";
                Unselected(pp, ppLayout);
                Unselected(cc, ccLayout);
                Unselected(ho, hoLayout);
                Unselected(ix, ixLayout);
                SelectBt(dx, dxLayout);
                Unselected(rx, rxLayout);
                Unselected(ad, adLayout);
                Unselected(fu, fuLayout);
            }

        } else if (state.equals("dx")) {
            GetTexts(dxListSelected, dxList, chipGroupDX);
            if (dxListSelected.size() == 0) {
                Toast.makeText(this, "Please Select at least one DX", Toast.LENGTH_SHORT).show();
            } else {
                editorDX = getSharedPreferences(AIR_PREFERENCE_DX, MODE_PRIVATE).edit();
                editorDX.putString("dxListSelected", dxListSelected.toString());
                editorDX.apply();
                Log.i("dxListSelected", "Next: " + dxListSelected);
                state = "rx";
                Unselected(pp, ppLayout);
                Unselected(cc, ccLayout);
                Unselected(ho, hoLayout);
                Unselected(ix, ixLayout);
                Unselected(dx, dxLayout);
                SelectBt(rx, rxLayout);
                Unselected(ad, adLayout);
                Unselected(fu, fuLayout);
            }
        } else if (state.equals("rx")) {
             GetTexts(rxListSelected, rxList, chipGroupRX);
            if (rxListSelected.size() == 0) {
                Toast.makeText(this, "Please Select at least one RX", Toast.LENGTH_SHORT).show();
            } else {
                editorRX = getSharedPreferences(AIR_PREFERENCE_RX, MODE_PRIVATE).edit();
                editorRX.putString("rxListSelected", rxListSelected.toString());
                editorRX.apply();
                Log.i("rxListSelected", "Next: " + rxListSelected);
            state = "ad";
            Unselected(pp, ppLayout);
            Unselected(cc, ccLayout);
            Unselected(ho, hoLayout);
            Unselected(ix, ixLayout);
            Unselected(dx, dxLayout);
            Unselected(rx, rxLayout);
            SelectBt(ad, adLayout);
            Unselected(fu, fuLayout);
            }
        } else if (state.equals("ad")) {
             GetTexts(adListSelected, adList, chipGroupAD);
            if (adListSelected.size() == 0) {
                Toast.makeText(this, "Please Select at least one Advice", Toast.LENGTH_SHORT).show();
            } else {
                editorAD = getSharedPreferences(AIR_PREFERENCE_AD, MODE_PRIVATE).edit();
                editorAD.putString("adListSelected", adListSelected.toString());
                editorAD.apply();
                Log.i("adListSelected", "Next: " + adListSelected);
                state = "fu";
                Unselected(pp, ppLayout);
                Unselected(cc, ccLayout);
                Unselected(ho, hoLayout);
                Unselected(ix, ixLayout);
                Unselected(dx, dxLayout);
                Unselected(rx, rxLayout);
                Unselected(ad, adLayout);
                SelectBt(fu, fuLayout);
            }
        }
    }

    public void Previous(View view) {
        if (state.equals("pp")) {

        } else if (state.equals("cc")) {
            state = "pp";
            SelectBt(pp, ppLayout);
            Unselected(cc, ccLayout);
            Unselected(ho, hoLayout);
            Unselected(ix, ixLayout);
            Unselected(dx, dxLayout);
            Unselected(rx, rxLayout);
            Unselected(ad, adLayout);
            Unselected(fu, fuLayout);
        } else if (state.equals("ho")) {
            state = "cc";
            Unselected(pp, ppLayout);
            SelectBt(cc, ccLayout);
            Unselected(ho, hoLayout);
            Unselected(ix, ixLayout);
            Unselected(dx, dxLayout);
            Unselected(rx, rxLayout);
            Unselected(ad, adLayout);
            Unselected(fu, fuLayout);
        } else if (state.equals("ix")) {
            state = "ho";
            Unselected(pp, ppLayout);
            Unselected(cc, ccLayout);
            SelectBt(ho, hoLayout);
            Unselected(ix, ixLayout);
            Unselected(dx, dxLayout);
            Unselected(rx, rxLayout);
            Unselected(ad, adLayout);
            Unselected(fu, fuLayout);
        } else if (state.equals("dx")) {
            state = "ix";
            Unselected(pp, ppLayout);
            Unselected(cc, ccLayout);
            Unselected(ho, hoLayout);
            SelectBt(ix, ixLayout);
            Unselected(dx, dxLayout);
            Unselected(rx, rxLayout);
            Unselected(ad, adLayout);
            Unselected(fu, fuLayout);
        } else if (state.equals("rx")) {
            state = "dx";
            Unselected(pp, ppLayout);
            Unselected(cc, ccLayout);
            Unselected(ho, hoLayout);
            Unselected(ix, ixLayout);
            SelectBt(dx, dxLayout);
            Unselected(rx, rxLayout);
            Unselected(ad, adLayout);
            Unselected(fu, fuLayout);
        } else if (state.equals("ad")) {
            state = "rx";
            Unselected(pp, ppLayout);
            Unselected(cc, ccLayout);
            Unselected(ho, hoLayout);
            Unselected(ix, ixLayout);
            Unselected(dx, dxLayout);
            SelectBt(rx, rxLayout);
            Unselected(ad, adLayout);
            Unselected(fu, fuLayout);
        } else if (state.equals("fu")) {
            state = "ad";
            Unselected(pp, ppLayout);
            Unselected(cc, ccLayout);
            Unselected(ho, hoLayout);
            Unselected(ix, ixLayout);
            Unselected(dx, dxLayout);
            Unselected(rx, rxLayout);
            SelectBt(ad, adLayout);
            Unselected(fu, fuLayout);
        } else {

        }
    }

    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup) {
        for (int i = 0; i < mainList.size(); i++) {
            View view = LayoutInflater.from(EPrescriptionActivity.this).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d(CHIP_CGs, "onCreate: SimpleDateFormat " + mainList.get(i));
            chip2.setText(mainList.get(i));
            chip2.setId(i);
            chip2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    Toast.makeText(this, chip2.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            chipGroup.addView(view);
        }

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
           chipCl=findViewById(checkedId);
            if (checkedId != -1) {
                Log.d("TAGSML", "onCreate: " + chipCl.getText().toString());
                Log.d("TAGSML", "onCreate: " + checkedId);
            }else {
                Log.d("TAGSML", "onCreate: " + checkedId);
            }
        });

    }

    public void SetHOChipGroup() {
        for (int i = 0; i < hoList.size(); i++) {
            View view = LayoutInflater.from(EPrescriptionActivity.this).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d(CHIP_CGs, "onCreate: SimpleDateFormat " + hoList.get(i));
            chip2.setText(hoList.get(i));
            chip2.setId(i);
            chipGroupHO.addView(view);
        }
    }

    public void SetIXChipGroup() {
        for (int i = 0; i < ixList.size(); i++) {
            View view = LayoutInflater.from(EPrescriptionActivity.this).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            Log.d(CHIP_CGs, "onCreate: SimpleDateFormat " + ixList.get(i));
            chip2.setText(ixList.get(i));
            chip2.setId(i);
            chipGroupIX.addView(view);
        }
    }

    public void GetTexts(List<String> list, List<String> baseList, ChipGroup chipGroup) {
        list.clear();
        List<Integer> checkedChipIds = chipGroup.getCheckedChipIds();
        for (int i = 0; i < checkedChipIds.size(); i++) {
            list.add(baseList.get(checkedChipIds.get(i)));
        }
    }

    public void GetIXTexts() {
        ixListSelected.clear();
        for (int i = 0; i < chipGroupIX.getCheckedChipIds().size(); i++) {
            chipForIx = findViewById(chipGroupIX.getCheckedChipIds().get(i));
            ixListSelected.add(chipForIx.getText().toString().trim());
        }

    }

    public void SetHO() {
        hoList.add("Inflammatory bowel disease");
        hoList.add("Emotional stress");
        hoList.add("Poor oral hygiene");
        hoList.add("Diabetes mellitus");
        hoList.add("Prolonged use of antibiotic");
        hoList.add("Prolonged use of steroid");
        hoList.add("Smoking");
        hoList.add("Alcohol");
        hoList.add("Pregnancy");
        hoList.add("Obesity");
        hoList.add("Betel nuts");
        hoList.add("Alcoholism");
        hoList.add("Barrett's oesophagus");
        hoList.add("NSAID's");
    }

    public void SetIx() {
        ixList.add("Barium swallow X-ray of oesophagus");
        ixList.add("Barium swallow of oesophagus");
        ixList.add("CXR");
        ixList.add("Ultrasonography of whole abdomen.");
        ixList.add("MRI");
        ixList.add("Endoscopic USG");
        ixList.add("CBC with ESR");
        ixList.add("Endoscopy");
    }

    public void SetRx() {
        rxList.add("Chlorhexidine 0.2% mouth wash");
        rxList.add("triamcinolone .01% \n 2-3 times daily");
        rxList.add("oral prednisolone");
        rxList.add("Topical antifungal--nystatin, econazole, miconazole");
        rxList.add("Antiseptic mouth wash");
        rxList.add("Antacid");
        rxList.add("PPI");
        rxList.add("H2 receptor blocker");
    }

    public void SetDx() {
        dxList.add("Aphthous Ulcer");
        dxList.add("Candidiasis (Oral Thrush)");
        dxList.add("GERD");
        dxList.add("Ca Oesophagus");
        dxList.add("Gastritis");
        dxList.add("PUD");
        dxList.add("Barrett's oesophgus");
        dxList.add("Hiatus hernia");
        dxList.add("Carcinoma of oesophagus");
        dxList.add("Duodenal ulcer");
        dxList.add("Gastric ulcer");
    }

    public void SetAd() {
        adList.add("পান খাওয়া নিষেধ। ধূমপান করবেন না।");
        adList.add("মসল্লাযুক্ত খাবার, গরম খাবার খাওয়া থেকে বিরত থাকুন।");
        adList.add("মুখ গহ্বরের পরিচ্ছনতা রক্ষা করতে প্রতিদিন ২বার ব্রাশ করুন।");
        adList.add("মুখে ভিতরে আঘাত পাওয়া থেকে সতর্ক থাকুন।");
        adList.add("মুখ গহ্বরের পরিচ্ছনতা রক্ষা করতে প্রতিদিন ২বার ব্রাশ করুন।");
        adList.add("ওজন কামাবেন");
        adList.add("আটসাট জামা, বেল্ট পরিধান করবেন না");
        adList.add("যে খাবার খেলে লক্ষ্যণ বাড়ে সেগুলো খাবেন না");
        adList.add("শোয়ার সময় মাথার অবস্থান উচুতে রাখুন।");
    }

    public void SetFu() {
        fuList.add("3 days later");
        fuList.add("5 days later");
        fuList.add("7 days later");
        fuList.add("10 days later");
        fuList.add("14 days later");
        fuList.add("18 days later");
        fuList.add("21 days later");
        fuList.add("25 days later");
        fuList.add("1 months later");
        fuList.add("2 months later");
    }

    public void SetFUs() {
        fuList.add("Hairy leukoplakia");
        fuList.add("Creamy white curd-like patches in the mouth and tongue");
        fuList.add("cachexic");
        fuList.add("Anaemia");
        fuList.add("hepatomegaly");
        fuList.add("Lymphadenopathy");
        fuList.add("Epigastric tenderness");
        fuList.add("Duodenal point tenderness");

    }

    @Override
    public void Gender(String gender) {
        genderEt.setText(gender);
    }
}
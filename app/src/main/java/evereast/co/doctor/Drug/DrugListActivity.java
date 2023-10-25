package evereast.co.doctor.Drug;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.R;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class DrugListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Medicines> medicinesList;
    DrugListAdapter drugListAdapter;
    Workbook workbook;
    EditText searchDrugEditText;
    boolean isTradeTrue = true;
    TextView tradeOrGeneric;

    private void AppBarWork() {
        TextView titleBar = findViewById(R.id.title_app);
        ImageView back = findViewById(R.id.back);
        titleBar.setText("Drugs");
        back.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_list);

        AppBarWork();

        medicinesList = new ArrayList<>();

        searchDrugEditText = findViewById(R.id.search_drug);
        recyclerView = findViewById(R.id.drug_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        tradeOrGeneric = findViewById(R.id.tradeOrGenTV);

        tradeOrGeneric.setOnClickListener(v -> {
            if (tradeOrGeneric.getTag().equals("trade")){
                tradeOrGeneric.setText("Generic");
                drugListAdapter = new DrugListAdapter(medicinesList, DrugListActivity.this, false);
                recyclerView.setAdapter(drugListAdapter);
                isTradeTrue=false;
                tradeOrGeneric.setTag("gen");
            }else {
                drugListAdapter = new DrugListAdapter(medicinesList, DrugListActivity.this, true);
                recyclerView.setAdapter(drugListAdapter);
                isTradeTrue=true;
                tradeOrGeneric.setText("Trade");
                tradeOrGeneric.setTag("trade");
            }
            searchDrugEditText.setText("");
        });

        searchDrugEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!s.toString().trim().isEmpty()) {
                        filter(s.toString(), drugListAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        try {
            AssetManager assetManager = getAssets();
            InputStream file = assetManager.open("medicines.xls");
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setGCDisabled(true);
            if (file != null) {
                try {
                    workbook = Workbook.getWorkbook(file);
                    Sheet[] sheets = workbook.getSheets();
                    for (int i = 0; i < sheets.length; i++) {
                        Sheet sheet = sheets[i];
                        for (int j = 1; j < sheet.getRows(); j++) {
                            Cell[] rows = sheet.getRow(j);
                            Medicines medicines = new Medicines();
                            if (rows[5].getContents().endsWith(")")) {
                                StringBuilder stringBuilder = new StringBuilder(rows[5].getContents());
                                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                                String[] arrOfStr = stringBuilder.toString().split("\\(", 0);
                                medicines.setPrice1(arrOfStr[0].trim().replaceAll("�"," "));
                                medicines.setPrice2(arrOfStr[1].trim().replaceAll("�"," "));
                            } else {
                                medicines.setPrice1(rows[5].getContents().replaceAll("�"," "));
                                medicines.setPrice2("");
                            }

                            StringBuilder stringBuilder = new StringBuilder(rows[2].getContents());
                            stringBuilder.deleteCharAt(0);
                            medicines.setBrandName(rows[1].getContents().replaceAll("�"," "));
                            medicines.setDosageForm(stringBuilder.toString().replaceAll("�"," "));
                            medicines.setStrength(rows[3].getContents().replaceAll("�"," "));
                            medicines.setCompany(rows[4].getContents().replaceAll("�"," "));
                            medicines.setGenericName(rows[6].getContents().replaceAll("�"," "));
                            medicinesList.add(medicines);
                        }
                    }
                    drugListAdapter = new DrugListAdapter(medicinesList, this, true);
                    recyclerView.setAdapter(drugListAdapter);
                } catch (IOException | BiffException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("TAG", "onCreate: end "+" "+medicinesList.get(6).getGenericName().charAt(medicinesList.get(6).getGenericName().length()-1));

    }

    private void filter(String text, DrugListAdapter drugListAdapter) {
        List<Medicines> filteredList = new ArrayList<>();
        for (Medicines medicines : medicinesList) {
            if (medicines.getBrandName().toLowerCase().contains(text.toLowerCase()) ||
                    medicines.getGenericName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(medicines);
            }
        }
        drugListAdapter.filteredList(filteredList);
    }
}
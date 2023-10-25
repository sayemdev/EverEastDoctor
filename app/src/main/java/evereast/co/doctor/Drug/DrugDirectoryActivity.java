package evereast.co.doctor.Drug;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_RX;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimationUpdated;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.DialogFragment.EndSessionConfirmation;
import evereast.co.doctor.DialogFragment.SelectRXSuggestion;
import evereast.co.doctor.HttpRetrofit.GenericsModel;
import evereast.co.doctor.HttpRetrofit.MedicinesModel;
import evereast.co.doctor.HttpRetrofit.RetrofitClient;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.ADActivity;
import evereast.co.doctor.RXActivities.IXActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrugDirectoryActivity extends AppCompatActivity implements SelectRXSuggestion.SelectRXWithSuggestion, EndSessionConfirmation.ConfirmationValueListener {
    ImageButton nextButton;
    ImageButton previousButton;
    SharedPreferences sharedPreferences;
    ArrayList<String> selectedList;
    ArrayList<String> mainList, priceList;
    ChipGroup selectedChipGroup;
    ToolTipsManager toolTipsManager;
    RelativeLayout mRootLayout;


    RecyclerView recyclerView;
    List<MedicinesModel> medicinesList;
    DrugListAdapter drugListAdapter;
    EditText searchDrugEditText;
    boolean isTradeTrue = true;
    TextView tradeOrGeneric;
    ProgressBar rxProgressBar;
    List<GenericsModel> genericsModelList;
    GenericAdapter genericAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_x);

        toolTipsManager = new ToolTipsManager();

        selectedList = new ArrayList<>();
        mainList = new ArrayList<>();
        priceList = new ArrayList<>();
        medicinesList = new ArrayList<>();
        genericsModelList = new ArrayList<>();

        genericAdapter = new GenericAdapter(genericsModelList, this);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        selectedChipGroup = findViewById(R.id.cgNormal);
        mRootLayout = findViewById(R.id.rootLt);
        rxProgressBar = findViewById(R.id.progressBarDrug);

        searchDrugEditText = findViewById(R.id.search_drug);
        recyclerView = findViewById(R.id.drug_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        tradeOrGeneric = findViewById(R.id.tradeOrGenTV);

        tradeOrGeneric.setOnClickListener(v -> {
            if (tradeOrGeneric.getTag().equals("trade")) {
                tradeOrGeneric.setText("Generic");
                GetGeneric();
                isTradeTrue = false;
                tradeOrGeneric.setTag("gen");
            } else {
                GetDrug();
                isTradeTrue = true;
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
                    if (toolTipsManager != null) {
                        toolTipsManager.dismissAll();
                    }
                    if (!s.toString().trim().isEmpty()) {
                        if (isTradeTrue) {
                            filter(s.toString(), drugListAdapter);
                        } else {
                            GenericFilter(s.toString(), genericAdapter);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NestedScrollView nestedScrollView = findViewById(R.id.nsSv);
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                toolTipsManager.dismissAll();
            });
        }

        GetDrug();

    }

    public void GetDrug() {

        if (medicinesList.size() == 0) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading drugs");
            progressDialog.setTitle("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Call<List<MedicinesModel>> medicinesCall = RetrofitClient.getInstance().getMyApi().GetMedicines();
            medicinesCall.enqueue(new Callback<List<MedicinesModel>>() {
                @Override
                public void onResponse(Call<List<MedicinesModel>> call, Response<List<MedicinesModel>> response) {
                    medicinesList = response.body();
                    drugListAdapter = new DrugListAdapter(medicinesList, DrugDirectoryActivity.this, toolTipsManager, mRootLayout);
                    drugListAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(drugListAdapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<MedicinesModel>> call, Throwable t) {
                    t.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(DrugDirectoryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            drugListAdapter = new DrugListAdapter(medicinesList, DrugDirectoryActivity.this, toolTipsManager, mRootLayout);
            drugListAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(drugListAdapter);
        }
    }

    public void GetGeneric() {
        if (genericsModelList.size() == 0) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading generics");
            progressDialog.setTitle("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Call<List<GenericsModel>> genericsCall = RetrofitClient.getInstance().getMyApi().GetGenerics();
            genericsCall.enqueue(new Callback<List<GenericsModel>>() {
                @Override
                public void onResponse(Call<List<GenericsModel>> call, Response<List<GenericsModel>> response) {
                    genericsModelList = response.body();
                    genericAdapter = new GenericAdapter(genericsModelList, DrugDirectoryActivity.this);
                    recyclerView.setAdapter(genericAdapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<GenericsModel>> call, Throwable t) {
                    t.printStackTrace();
                    progressDialog.dismiss();
                }
            });
        } else {
            genericAdapter = new GenericAdapter(genericsModelList, DrugDirectoryActivity.this);
            recyclerView.setAdapter(genericAdapter);
        }
    }

    public void Previous(View view) {
        RedirectToActivityWithAnimationUpdated(this, IXActivity.class, selectedChipGroup, AIR_PREFERENCE_RX, selectedList.toString());
        finish();
    }

    public void Next(View view) {
        RedirectToActivityWithAnimationUpdated(this, ADActivity.class, selectedChipGroup, AIR_PREFERENCE_RX, selectedList.toString());
        finish();
    }

    @SuppressLint("ResourceAsColor")
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
                View view = LayoutInflater.from(this).inflate(R.layout.chip_custom_white_bg, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i));
                chip2.setId(i);
                int finalI = i;
                chip2.setOnCloseIconClickListener(v -> {
                    toolTipsManager.dismissAll();
                    ToolTip.Builder builder = new ToolTip.Builder(this, findViewById(finalI), mRootLayout, priceList.get(finalI), ToolTip.POSITION_ABOVE);
                    builder.setAlign(ToolTip.ALIGN_CENTER);
                    builder.setBackgroundColor(getResources().getColor(R.color.app_bar_color));
                    builder.setGravity(ToolTip.GRAVITY_CENTER);
                    builder.setTextAppearance(R.style.TooltipTextAppearance);
                    builder.withArrow(true);
                    toolTipsManager.show(builder.build());

                });
                chip2.setOnClickListener(v -> {
                    SelectRXSuggestion selectSuggestion = new SelectRXSuggestion(chip2.getText().toString().trim(), this, false);
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


    private void filter(String text, DrugListAdapter drugListAdapter) {
        List<MedicinesModel> filteredList = new ArrayList<>();
        for (MedicinesModel medicines : medicinesList) {
            if (medicines.getBrandName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(medicines);
            }
        }
        drugListAdapter.filteredList(filteredList);
    }

    private void GenericFilter(String text, GenericAdapter genericAdapter) {
        List<GenericsModel> filteredList = new ArrayList<>();
        for (GenericsModel medicines : genericsModelList) {
            if (medicines.getCompany().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(medicines);
            }
        }
        genericAdapter.filteredList(filteredList);
    }

    @Override
    public void SelectedSuggestion(String selectedText) {
        selectedList.add(selectedText);
        SetChipGroup(selectedList, selectedChipGroup, true);
    }

    @Override
    public void deselectSuggestion(int id) {

    }

    public void Get(View view) {
        NestedScrollView nestedScrollView = findViewById(R.id.nsSv);
    }

    public void EndSession(View view) {
        EndSessionConfirmation endSessionConfirmation = new EndSessionConfirmation();
        endSessionConfirmation.show(getSupportFragmentManager(), endSessionConfirmation.getTag());
    }

    @Override
    public void Confirmation(boolean confirm) {

    }

    static class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.ViewHolder> {

        List<MedicinesModel> medicinesList;
        Activity activity;
        ToolTipsManager toolTipsManager;
        ViewGroup rootLt;


        public DrugListAdapter(List<MedicinesModel> medicinesList, Activity activity, ToolTipsManager toolTipsManager, ViewGroup rootLt) {
            this.medicinesList = medicinesList;
            this.activity = activity;
            this.rootLt = rootLt;
            this.toolTipsManager = toolTipsManager;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_list_item, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String price = medicinesList.get(position).getPrice().replaceAll("\\?", "৳").replaceAll("\\(", "\n");
            StringBuilder stringBuilder = new StringBuilder(price);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            holder.drugNameTextView.setText(medicinesList.get(position).getBrandName().trim());

            holder.infoImageView.setOnClickListener(v -> {
                toolTipsManager.dismissAll();
                ToolTip.Builder builder = new ToolTip.Builder(activity, holder.itemView, rootLt, stringBuilder.toString(), ToolTip.POSITION_ABOVE);
                builder.setAlign(ToolTip.ALIGN_CENTER);
                builder.setBackgroundColor(activity.getResources().getColor(R.color.app_bar_color));
                builder.setGravity(ToolTip.GRAVITY_CENTER);
                builder.setTextAppearance(R.style.TooltipTextAppearance);
                builder.withArrow(true);
                toolTipsManager.show(builder.build());
            });

        }

        @Override
        public int getItemCount() {
            if (medicinesList!=null) {
                return Math.min(medicinesList.size(), 40);
            }else{
                return 0;
            }
        }

        public void filteredList(List<MedicinesModel> userArrayListList) {
            medicinesList = userArrayListList;
            notifyDataSetChanged();
            if (toolTipsManager != null)
                toolTipsManager.dismissAll();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView drugNameTextView;
            RelativeLayout rootLt;
            ImageView infoImageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                drugNameTextView = itemView.findViewById(R.id.drugNameTv);
                rootLt = itemView.findViewById(R.id.rootLt);
                infoImageView = itemView.findViewById(R.id.infoView);
            }
        }
    }

    static class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {

        List<GenericsModel> genericsModelList1;
        Activity activity;

        public GenericAdapter(List<GenericsModel> medicinesList, Activity activity) {
            this.genericsModelList1 = medicinesList;
            this.activity = activity;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_list_item, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.drugNameTextView.setText(genericsModelList1.get(position).getGenericName().trim() + "\n" + genericsModelList1.get(position).getCompany());
            holder.infoImageView.setVisibility(View.GONE);

        }

        @Override
        public int getItemCount() {
            return Math.min(genericsModelList1.size(), 40);
        }

        public void filteredList(List<GenericsModel> userArrayListList) {
            genericsModelList1 = userArrayListList;
            notifyDataSetChanged();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView drugNameTextView;
            RelativeLayout rootLt;
            ImageView infoImageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                drugNameTextView = itemView.findViewById(R.id.drugNameTv);
                rootLt = itemView.findViewById(R.id.rootLt);
                infoImageView = itemView.findViewById(R.id.infoView);
            }
        }
    }

}
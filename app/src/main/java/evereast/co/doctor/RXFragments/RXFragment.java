package evereast.co.doctor.RXFragments;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_RX;
import static evereast.co.doctor.Utils.GenerateListFromStringList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.DialogFragment.SelectRXSuggestion;
import evereast.co.doctor.HttpRetrofit.GenericsModel;
import evereast.co.doctor.HttpRetrofit.MedicinesModel;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.FragmentRXBinding;

public class RXFragment extends Fragment implements SelectRXSuggestion.SelectRXWithSuggestion, ClickListener {

    public RXFragment() {
        // Required empty public constructor
    }

    String list;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    ArrayList<String> selectedList;
    AutoCompleteTextView searchET;
    ArrayList<String> mainList, priceList;
    ChipGroup mainChipGroup, selectedChipGroup;
    ImageView searchBt;
    ToolTipsManager toolTipsManager;
    RelativeLayout mRootLayout;


    RecyclerView recyclerView;
    List<MedicinesModel> medicinesList;
    DrugListAdapter drugListAdapter;
    EditText searchDrugEditText;
    boolean isTradeTrue = true;
    TextView tradeOrGeneric;

    ProgressBar rxProgressBar;

    ArrayList<String> suggestionList;
    private List<GenericsModel> genericsModelList;
    private GenericAdapter genericAdapter;

    FragmentRXBinding binding;
    View root;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRXBinding.inflate(inflater,container,false);
        root=binding.getRoot();
        context=root.getContext();


        toolTipsManager = new ToolTipsManager();

        sharedPreferences = context.getSharedPreferences(AIR_PREFERENCE_RX, MODE_PRIVATE);
        list = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");

        selectedList = new ArrayList<>();
        mainList = new ArrayList<>();
        priceList = new ArrayList<>();
        medicinesList = new ArrayList<>();
        suggestionList = new ArrayList<>();
        genericsModelList = new ArrayList<>();


        suggestionList.add("সকাল+দুপর+রাতে");
        suggestionList.add("সকাল+দুপুরে");
        suggestionList.add("সকাল+রাতে");
        suggestionList.add("সকালে");
        suggestionList.add("দুপুর+রাতে");
        suggestionList.add("রাতে");
        suggestionList.add("দুপুরে");


        selectedChipGroup = root.findViewById(R.id.cgNormal);
        mRootLayout = root.findViewById(R.id.rootLt);
        rxProgressBar = root.findViewById(R.id.progressBarDrug);

        searchDrugEditText = root.findViewById(R.id.search_drug);
        recyclerView = root.findViewById(R.id.drug_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        tradeOrGeneric = root.findViewById(R.id.tradeOrGenTV);

        GetDrug();

        try {
            if (!list.isEmpty()) {
                selectedList = GenerateListFromStringList(list.trim());
                Log.i("TAG_L", "onCreate: " + list);
                SetChipGroup(selectedList, selectedChipGroup, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tradeOrGeneric.setOnClickListener(v -> {
            if (toolTipsManager != null) {
                toolTipsManager.dismissAll();
            }
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
            NestedScrollView nestedScrollView = root.findViewById(R.id.nsSv);
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                toolTipsManager.dismissAll();
            });
        }



        return root;
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

    private void GetGeneric() {
        genericAdapter = new GenericAdapter(genericsModelList, getActivity(),this);
        recyclerView.setAdapter(genericAdapter);
    }

    private void GetDrug() {
        drugListAdapter = new DrugListAdapter(medicinesList, getActivity(), toolTipsManager, mRootLayout,this);
        recyclerView.setAdapter(drugListAdapter);
    }

    @SuppressLint("ResourceAsColor")
    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup, boolean isSelected) {

        if (isSelected) {
            chipGroup.removeAllViews();
            for (int i = 0; i < mainList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.chip_custom, null);
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
                View view = LayoutInflater.from(context).inflate(R.layout.chip_custom_white_bg, null);
                Chip chip2 = view.findViewById(R.id.chup);
                Log.d("CHIP_CGs", "onCreate: SimpleDateFormat " + mainList.get(i));
                chip2.setText(mainList.get(i));
                chip2.setId(i);
                int finalI = i;
                chip2.setOnCloseIconClickListener(v -> {
                    toolTipsManager.dismissAll();
                    ToolTip.Builder builder = new ToolTip.Builder(context, root.findViewById(finalI), mRootLayout, priceList.get(finalI), ToolTip.POSITION_ABOVE);
                    builder.setAlign(ToolTip.ALIGN_CENTER);
                    builder.setBackgroundColor(getResources().getColor(R.color.app_bar_color));
                    builder.setGravity(ToolTip.GRAVITY_CENTER);
                    builder.setTextAppearance(R.style.TooltipTextAppearance);
                    builder.withArrow(true);
                    toolTipsManager.show(builder.build());

                });
                chip2.setOnClickListener(v -> {
                    SelectRXSuggestion selectSuggestion = new SelectRXSuggestion(chip2.getText().toString().trim(), getActivity(), false,this);
                    selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                });
                chipGroup.addView(view);
            }
        }
    }

    public boolean GoNext(){
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(AIR_PREFERENCE_RX, MODE_PRIVATE).edit();
            if (!selectedList.equals("[]")) {
                editor.putString(AIR_PREFERENCE_LIST, selectedList.toString().trim());
            } else {
                editor.putString(AIR_PREFERENCE_LIST, "");
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            SharedPreferences.Editor editor = context.getSharedPreferences(AIR_PREFERENCE_RX, MODE_PRIVATE).edit();
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

    public void UpdateRxData(List<MedicinesModel> drugList, List<GenericsModel> genericsModelList){
        this.medicinesList=new ArrayList<>();
        this.genericsModelList=new ArrayList<>();
        medicinesList.addAll(drugList);
        this.genericsModelList.addAll(genericsModelList);
        GetDrug();
    }

    @Override
    public void OnGenericItemClick(String symptom, Activity activity, boolean hasSuggestion, ArrayList<String> sgList) {
        SelectRXSuggestion selectSuggestion = new SelectRXSuggestion(symptom, activity, hasSuggestion, sgList,this);
        selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());

    }

    @Override
    public void OnMedicineItemClick(String symptom, Activity activity, boolean hasSuggestion, ArrayList<String> sgList) {
        SelectRXSuggestion selectSuggestion = new SelectRXSuggestion(symptom, activity, hasSuggestion, sgList,this);
        selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
    }


    class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.ViewHolder> {

        List<MedicinesModel> medicinesList;
        Activity activity;
        ToolTipsManager toolTipsManager;
        ViewGroup rootLt;

        ClickListener clickListener;

        public DrugListAdapter(List<MedicinesModel> medicinesList, Activity activity, ToolTipsManager toolTipsManager, ViewGroup rootLt, ClickListener clickListener) {
            this.medicinesList = medicinesList;
            this.activity = activity;
            this.toolTipsManager = toolTipsManager;
            this.rootLt = rootLt;
            this.clickListener = clickListener;
        }

        public DrugListAdapter(List<MedicinesModel> medicinesList, Activity activity, ToolTipsManager toolTipsManager, ViewGroup rootLt) {
            this.medicinesList = medicinesList;
            this.activity = activity;
            this.toolTipsManager = toolTipsManager;
            this.rootLt = rootLt;
        }

        @NonNull
        @Override
        public DrugListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DrugListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_list_item, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DrugListAdapter.ViewHolder holder, int position) {
            String price = medicinesList.get(position).getPrice().replaceAll("\\?", "৳").replaceAll("\\(", "\n");
            StringBuilder stringBuilder = new StringBuilder(price);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            holder.drugNameTextView.setText(medicinesList.get(position).getBrandName().trim());

            holder.infoImageView.setOnClickListener(v -> {
                if(clickListener!=null){
                    clickListener.OnMedicineItemClick(medicinesList.get(position).getBrandName(), getActivity(), true, suggestionList);
                }else {
                    SelectRXSuggestion selectSuggestion = new SelectRXSuggestion(medicinesList.get(position).getBrandName(), getActivity(), true, suggestionList);
                    selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                }
            });
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

            holder.itemView.setOnClickListener(v -> {
                if(clickListener!=null) {
                    clickListener.OnMedicineItemClick(medicinesList.get(position).getBrandName() + " ", getActivity(), true, suggestionList);
              }else{
                    SelectRXSuggestion selectSuggestion = new SelectRXSuggestion(medicinesList.get(position).getBrandName() + " ", getActivity(), true, suggestionList);
                    selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());

                }
            });

        }

        @Override
        public int getItemCount() {
            return Math.min(medicinesList.size(), 6);
        }

        public void filteredList(List<MedicinesModel> userArrayListList) {
            medicinesList = userArrayListList;
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

    class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {

        List<GenericsModel> genericsModelList1;
        Activity activity;

        ClickListener clickListener;

        public GenericAdapter(List<GenericsModel> genericsModelList1, Activity activity, ClickListener clickListener) {
            this.genericsModelList1 = genericsModelList1;
            this.activity = activity;
            this.clickListener = clickListener;
        }

        public GenericAdapter(List<GenericsModel> medicinesList, Activity activity) {
            this.genericsModelList1 = medicinesList;
            this.activity = activity;
        }


        @NonNull
        @Override
        public GenericAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GenericAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_list_item, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull GenericAdapter.ViewHolder holder, int position) {
            holder.drugNameTextView.setText(genericsModelList1.get(position).getGenericName().trim());
            holder.infoImageView.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                if(clickListener!=null){
                    clickListener.OnGenericItemClick(genericsModelList1.get(position).getGenericName()+" ", getActivity(), true, suggestionList);
                }else{
                    SelectRXSuggestion selectSuggestion = new SelectRXSuggestion(genericsModelList1.get(position).getGenericName()+" ", getActivity(), true, suggestionList);
                    selectSuggestion.show(getActivity().getSupportFragmentManager(), selectSuggestion.getTag());
                }
            });
        }

        @Override
        public int getItemCount() {
            return Math.min(genericsModelList1.size(), 4);
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


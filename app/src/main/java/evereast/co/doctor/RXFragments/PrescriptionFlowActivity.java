package evereast.co.doctor.RXFragments;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.RXActivities.PPActivity.ADDRESS;
import static evereast.co.doctor.RXActivities.PPActivity.AGE;
import static evereast.co.doctor.RXActivities.PPActivity.AGE_TYPE;
import static evereast.co.doctor.RXActivities.PPActivity.APPOINTMENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.DATE;
import static evereast.co.doctor.RXActivities.PPActivity.GENDER;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.HttpRetrofit.GenericsModel;
import evereast.co.doctor.HttpRetrofit.MedicinesModel;
import evereast.co.doctor.PrescriptionDB.IxEntityModel;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.PreviewActivity;
import evereast.co.doctor.databinding.ActivityPrescriptionFlowBinding;
import evereast.co.doctor.utils.CustomPagerAdapter;
import evereast.co.doctor.utils.DataFetchingThread;
import evereast.co.doctor.utils.NonSwipeableViewPager;

public class PrescriptionFlowActivity extends AppCompatActivity {
    private static final String TAG = "PrescriptionFlowActivit";
    ActivityPrescriptionFlowBinding binding;
    int position = 0;
    String pp, cc, ho, ix, dx, rx, ad;
    PPFragment ppFragment;
    CCFragment ccFragment;
    HOFragment hoFragment;
    IXFragment ixFragment;
    DXFragment dxFragment;
    RXFragment rxFragment;
    ADFragment adFragment;
    ArrayList<String> ccList, ixList;
    List<MedicinesModel> drugList;
    String name = "", age = "",dateString="", ageType = "", gender = "", id = "", address = "", type = "", from = "", appointmentId = "";
    private NonSwipeableViewPager viewPager;
    private List<GenericsModel> genericsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrescriptionFlowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ccList = new ArrayList<>();
        ixList = new ArrayList<>();
        drugList = new ArrayList<>();
        genericsModelList = new ArrayList<>();

        Intent intent = getIntent();

        appointmentId = intent.getStringExtra("appointmentId");
        name = intent.getStringExtra(PATIENT_NAME);
        address = intent.getStringExtra(ADDRESS);
        gender = intent.getStringExtra(GENDER);
        age = intent.getStringExtra(AGE);
        dateString = intent.getStringExtra(DATE);
        ageType = intent.getStringExtra(AGE_TYPE);
        from = intent.getStringExtra("prescription");
        appointmentId = intent.getStringExtra(APPOINTMENT_ID);
        id = intent.getStringExtra(PATIENT_ID);

        viewPager = findViewById(R.id.viewPager);
        ppFragment = new PPFragment(name, age, ageType, gender, id, address, type, from, appointmentId);
        ccFragment = new CCFragment();
        hoFragment = new HOFragment();
        ixFragment = new IXFragment();
        dxFragment = new DXFragment();
        rxFragment = new RXFragment();
        adFragment = new ADFragment();

        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 6) {
                    binding.nextButton.setVisibility(View.GONE);
                    binding.previewButton.setVisibility(View.VISIBLE);
                    binding.previousButton.setVisibility(View.VISIBLE);
                } else if (position == 0) {
                    binding.previousButton.setVisibility(View.GONE);
                    binding.nextButton.setVisibility(View.VISIBLE);
                    binding.previewButton.setVisibility(View.GONE);
                } else {
                    binding.nextButton.setVisibility(View.VISIBLE);
                    binding.previewButton.setVisibility(View.GONE);
                    binding.previousButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setSwipeEnabled(false);
        if (position == 0) {
            binding.previousButton.setVisibility(View.GONE);
        }

        binding.nextButton.setOnClickListener(view -> {
            if (position == 0) {
                boolean ppResult = ppFragment.updateValue();
                if (ppResult) {
                    position++;
                    viewPager.setCurrentItem(position);
                    ccFragment.UpdateCCData(ccList);
                }
            } else if (position == 1) {
                boolean hoResult = ccFragment.GoNext();
                if (hoResult) {
                    position++;
                    viewPager.setCurrentItem(position);
                    hoFragment.UpdateHOData(ccList);
                }
            } else if (position == 2) {
                boolean hoResult = hoFragment.GoNext();
                if (hoResult) {
                    position++;
                    viewPager.setCurrentItem(position);
                    ixFragment.UpdateIXData(ixList);
                }
            } else if (position == 3) {
                boolean hoResult = ixFragment.GoNext();
                if (hoResult) {
                    position++;
                    viewPager.setCurrentItem(position);
                    dxFragment.UpdateDXData(ccList);
                }
            } else if (position == 4) {
                boolean hoResult = dxFragment.GoNext();
                if (hoResult) {
                    position++;
                    viewPager.setCurrentItem(position);
                    rxFragment.UpdateRxData(drugList, genericsModelList);
                }
            } else if (position == 5) {
                boolean rxResult = rxFragment.GoNext();
                if (rxResult) {
                    position++;
                    viewPager.setCurrentItem(position);
                }
            }
        });
        binding.previewButton.setOnClickListener(view -> {
            boolean rxResult = rxFragment.GoNext();
            if (rxResult) {
                startActivity(new Intent(this, PreviewActivity.class));
            }
        });

        binding.previousButton.setOnClickListener(view -> {
            position--;
            viewPager.setCurrentItem(position);
        });

        GetData();

    }

    private void GetData() {

        List<String> urls = new ArrayList<>();
        urls.add(BASE_URL + "get_cc.php");
        urls.add(BASE_URL + "get_ix.php");
        urls.add(BASE_URL + "medicine.php");
        urls.add(BASE_URL + "generics.php");

        List<DataFetchingThread.DataFetchingListener> listeners = new ArrayList<>();
        listeners.add(new DataFetchingThread.DataFetchingListener() {
            @Override
            public void onDataFetched(String jsonData) {
                // Handle fetched data for the first URL

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    ccList.clear();
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ccList.add(jsonObject.getString("cc_ho_dx").trim());
                    }
                    Log.d(TAG, "onDataFetched: " + ccList.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onDataFetchError(IOException e) {
                // Handle error for the first URL
                e.printStackTrace();
                Log.d(TAG, "onDataFetchError: ");
            }
        });

        listeners.add(new DataFetchingThread.DataFetchingListener() {
            @Override
            public void onDataFetched(String jsonData) {
                // Handle fetched data for the second URL
                Log.d(TAG, "onDataFetched: IX");

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    ixList.clear();
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ixList.add(jsonObject.getString("investigation"));
                        Log.i("TAG", "GetIX: " + jsonArray.getJSONObject(i).getString("investigation"));
                        IxEntityModel ixEntityModel = new IxEntityModel();
                        ixEntityModel.setInvestigation(jsonObject.getString("investigation"));
                    }
                    ixList.add("Add New");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onDataFetchError(IOException e) {
                // Handle error for the second URL
                e.printStackTrace();
            }
        });

        listeners.add(new DataFetchingThread.DataFetchingListener() {
            @Override
            public void onDataFetched(String jsonData) {
                // Handle fetched data for the third URL
                Gson gson = new Gson();
                Type listType = new TypeToken<List<MedicinesModel>>() {
                }.getType();
                drugList = gson.fromJson(jsonData, listType);
                Log.d(TAG, "onDataFetched: " + drugList.size());


            }

            @Override
            public void onDataFetchError(IOException e) {
                // Handle error for the third URL
                e.printStackTrace();
            }
        });

        listeners.add(new DataFetchingThread.DataFetchingListener() {
            @Override
            public void onDataFetched(String jsonData) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<GenericsModel>>() {
                }.getType();
                genericsModelList.clear();
                genericsModelList = gson.fromJson(jsonData, listType);

                runOnUiThread(() -> {
                    if (genericsModelList.size() > 0) {
                        binding.prescriptionView.setVisibility(View.VISIBLE);
                        binding.loadingView.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onDataFetchError(IOException e) {
                e.printStackTrace();
            }
        });

        DataFetchingThread dataFetchingThread = new DataFetchingThread(urls, listeners);
        dataFetchingThread.start();

    }

    private void setupViewPager(ViewPager viewPager) {
        CustomPagerAdapter adapter = new CustomPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ppFragment);//Position == 0, "Fragment PP"
        adapter.addFragment(ccFragment);//Position == 1, "Fragment CC"
        adapter.addFragment(hoFragment);//Position == 2, "Fragment HO"
        adapter.addFragment(ixFragment);//Position == 3, "Fragment IX"
        adapter.addFragment(dxFragment);//Position == 4, "Fragment DX"
        adapter.addFragment(rxFragment);//Position == 5, "Fragment RX"
        adapter.addFragment(adFragment);//Position == 6, "Fragment AD"
        viewPager.setAdapter(adapter);
    }

}
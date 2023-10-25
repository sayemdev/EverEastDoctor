package evereast.co.doctor.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.FEE;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.Adapter.RunningAppointmentsAdapter;
import evereast.co.doctor.Model.HistoryModel;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.FragmentOfflineBinding;

public class OfflineFragment extends Fragment {

    public OfflineFragment() {
        // Required empty public constructor
    }
    FragmentOfflineBinding binding;
    View view;
    RecyclerView recyclerView;
    List<HistoryModel> historyModelList;
    RunningAppointmentsAdapter runningAppointmentsAdapter;
    SharedPreferences sharedPreferences;
    String userId, name, fee;
    ProgressBar progressBar;
    LinearLayout doctorNotFoundLayout;
    Context context;


    private static final String TAG = "OfflineFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentOfflineBinding.inflate(inflater);
        view=binding.getRoot();

        doctorNotFoundLayout = view.findViewById(R.id.doctorNotFound);


        try {
            sharedPreferences = getContext().getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
            userId = sharedPreferences.getString(USER_ID, "");
            name = sharedPreferences.getString(USER_NAME, "");
            fee = sharedPreferences.getString(FEE, "");
            Log.d(TAG, "onCreate: " + userId);
            historyModelList = new ArrayList<>();
            recyclerView = view.findViewById(R.id.historyList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBar = view.findViewById(R.id.progressBarId);
        GetAppointmentList();

//        srl.setOnRefreshListener(this::GetAppointmentList);

        return binding.getRoot();
    }


    private void GetAppointmentList() {
        String url = BASE_URL + "appointment_info_list.php?doctor_id=" + userId;
        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, url,
                response -> {
//                    srl.setRefreshing(false);
                    Log.d(TAG, "GetAppointmentList: " + url);
                    Log.d(TAG, "GetAppointmentList: " + response);
                    progressBar.setVisibility(View.GONE);
                    doctorNotFoundLayout.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONArray jsonArray = jsonObject1.getJSONArray("appointment_list");
                        historyModelList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HistoryModel historyModel = new HistoryModel();
                            historyModel.setAppointmentType(jsonObject.getString("appointment_type"));
                            try {
                                historyModel.setHospital(jsonObject.getString("offline_work_place"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            historyModel.setAppointmentId(jsonObject.getString("appointment_id"));
                            historyModel.setDate(jsonObject.getString("date"));
                            historyModel.setTime(jsonObject.getString("time"));
                            historyModel.setPatientName(jsonObject.getString("patient_name"));
                            historyModel.setDoctorName(jsonObject.getString("d_name"));
                            historyModel.setCategory(jsonObject.getString("d_category"));
                            historyModel.setProfileName(jsonObject.getString("d_image"));
                            historyModel.setPatientBirthday(jsonObject.getString("patient_age"));
                            historyModel.setpAddress(jsonObject.getString("patient_address"));
                            historyModel.setPatientGender(jsonObject.getString("patient_gender"));
                            historyModel.setPatientID(jsonObject.getString("patient_id"));
                            historyModel.setFee(jsonObject.getString("after_charge"));
                            historyModel.setRated(jsonObject.getString("doctor_rated"));
                            historyModel.setRating(jsonObject.getString("rate"));
                            Log.d("ViewHold_Ac", "onBindViewHolder: " + jsonObject.getString("patient_name") + "========" + jsonObject.getString("d_category"));
                            if (!jsonObject.getString("date").equals("null")
                                    && jsonObject.getString("status").equals("Confirmed")
                                    && historyModel.getAppointmentType().equals("1")
                                    && jsonObject.getString("payment_status").equals("Paid")) {
                                historyModelList.add(historyModel);
                            }
                        }

                        if (historyModelList.size() == 0) {
                            doctorNotFoundLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        runningAppointmentsAdapter = new RunningAppointmentsAdapter(historyModelList, getContext());
                        recyclerView.setAdapter(runningAppointmentsAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        doctorNotFoundLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    doctorNotFoundLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }));
    }



}
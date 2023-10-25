package evereast.co.doctor.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.Adapter.BillingHistoryAdapter;
import evereast.co.doctor.GetAge;
import evereast.co.doctor.LinearLayoutManagerWrapper;
import evereast.co.doctor.Model.HistoryModel;
import evereast.co.doctor.R;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

public class BillingHistoryFragment extends Fragment {

    private static final String TAG = "BillingHistoryFragment";
    RecyclerView recyclerView;
    List<HistoryModel> historyModelList;
    BillingHistoryAdapter historyAdapter;
    SharedPreferences sharedPreferences;
    String userId;
    SwipeRefreshLayout srl;

    public BillingHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing_history, container, false);

        sharedPreferences = view.getContext().getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, "");
        Log.d(TAG, "onCreate: " + userId);
        historyModelList = new ArrayList<>();

        srl = view.findViewById(R.id.srl);
        recyclerView = view.findViewById(R.id.historyList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        srl.setOnRefreshListener(() -> {
            GetAppointmentList(view);
        });

        GetAppointmentList(view);


        return view;
    }

    private void GetAppointmentList(View view) {
        String url = BASE_URL + "billing_list.php?doctor_id=" + userId;
        Volley.newRequestQueue(view.getContext()).add(new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "GetAppointmentList: " + url);
                    srl.setRefreshing(false);
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
                            historyModel.setDoctorId(jsonObject.getString("s_doctor_id"));
                            historyModel.setCategory(jsonObject.getString("d_category"));
                            historyModel.setProfileName(jsonObject.getString("d_image"));
                            historyModel.setFee(jsonObject.getString("after_charge"));
                            historyModel.setPaymentStatus(jsonObject.getString("payment_status"));
                            historyModel.setApStatus(jsonObject.getString("status"));
                            historyModel.setPatientGender(jsonObject.getString("patient_gender"));
                            historyModel.setPatientBirthday(GetAge.getAge(jsonObject.getString("patient_age")));
                            historyModel.setPatientID(jsonObject.getString("patient_id"));
                            historyModel.setRated(jsonObject.getString("doctor_rated"));
                            if (jsonObject.getString("rate").equals("NAN")) {
                                historyModel.setRating("0");
                            } else {
                                historyModel.setRating(jsonObject.getString("rate"));
                            }
                            if (!jsonObject.getString("date").equals("null")) {
                                historyModelList.add(historyModel);
                            }
                        }
                        historyAdapter = new BillingHistoryAdapter(view.getContext(), historyModelList);
                        recyclerView.setAdapter(historyAdapter);
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    srl.setRefreshing(false);
                    Toast.makeText(view.getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }));
    }

}
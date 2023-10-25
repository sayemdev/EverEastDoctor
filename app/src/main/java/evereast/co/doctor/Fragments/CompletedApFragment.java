package evereast.co.doctor.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.Adapter.HistoryAdapter;
import evereast.co.doctor.DialogFragment.RatePatientDialogFragment;
import evereast.co.doctor.LinearLayoutManagerWrapper;
import evereast.co.doctor.Model.HistoryModel;
import evereast.co.doctor.R;

public class CompletedApFragment extends Fragment implements RatePatientDialogFragment.ConfirmationValueListener {

    private static final String TAG = "CompletedApFragment";
    View view;
    RecyclerView recyclerView;
    List<HistoryModel> historyModelList;
    HistoryAdapter historyAdapter;
    SharedPreferences sharedPreferences;
    String userId;
    SwipeRefreshLayout srl;
    RelativeLayout wholeThing;
    LinearLayout dataNotAvailable;
    ShimmerFrameLayout shimmerFrameLayout;
    Context context;

    public CompletedApFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_completed_ap, container, false);


        context = view.getContext();
        sharedPreferences = context.getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, "");
        Log.d(TAG, "onCreate: " + userId);
        historyModelList = new ArrayList<>();

        shimmerFrameLayout = view.findViewById(R.id.shimmerContentView);
        wholeThing = view.findViewById(R.id.wholeThing);
        dataNotAvailable = view.findViewById(R.id.doctorNotFound);
        srl = view.findViewById(R.id.srl);
        recyclerView = view.findViewById(R.id.historyList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        srl.setOnRefreshListener(this::GetAppointmentList);

        GetAppointmentList();

        return view;
    }

    private void GetAppointmentList() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        recyclerView.setVisibility(View.GONE);
        String url = BASE_URL + "appointment_info_list.php?doctor_id=" + userId;
        Volley.newRequestQueue(context).add(new StringRequest(Request.Method.GET, url,
                response -> {
                    srl.setRefreshing(false);
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    recyclerView.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONArray jsonArray = jsonObject1.getJSONArray("appointment_list");
                        historyModelList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HistoryModel historyModel = new HistoryModel();
                            historyModel.setAppointmentId(jsonObject.getString("appointment_id"));
                            historyModel.setAppointmentType(jsonObject.getString("appointment_type"));
                            try {
                                historyModel.setHospital(jsonObject.getString("offline_work_place"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
                            historyModel.setPatientID(jsonObject.getString("patient_id"));
                            historyModel.setRated(jsonObject.getString("doctor_rated"));
                            if (jsonObject.getString("rate").equals("NAN")) {
                                historyModel.setRating("0");
                            } else {
                                historyModel.setRating(jsonObject.getString("rate"));
                            }
                            if (!jsonObject.getString("date").equals("null")) {
                                if (jsonObject.getString("status").equals("Completed")) {
                                    historyModelList.add(historyModel);
                                }
                            }
                        }
                        if (historyModelList.size() == 0) {
                            dataNotAvailable.setVisibility(View.VISIBLE);
                            wholeThing.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        historyAdapter = new HistoryAdapter(historyModelList, context,CompletedApFragment.this);
                        recyclerView.setAdapter(historyAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();
                        dataNotAvailable.setVisibility(View.VISIBLE);
                        wholeThing.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                },
                error -> {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    srl.setRefreshing(false);
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    dataNotAvailable.setVisibility(View.VISIBLE);
                    wholeThing.setBackgroundColor(getResources().getColor(R.color.white));
                }));
    }

    @Override
    public void Confirmation(boolean confirm) {
        Log.d(TAG, "Confirmation: ");
        GetAppointmentList();
    }

}
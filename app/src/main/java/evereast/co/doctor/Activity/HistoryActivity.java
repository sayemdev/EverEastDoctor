package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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


public class HistoryActivity extends AppCompatActivity implements RatePatientDialogFragment.ConfirmationValueListener {

    private static final String TAG = "HistoryActivity";
    RecyclerView recyclerView;
    List<HistoryModel> historyModelList;
    HistoryAdapter historyAdapter;
    SharedPreferences sharedPreferences;
    String userId;
    SwipeRefreshLayout srl;
    LinearLayout wholeThing, dataNotAvailable;
    ShimmerFrameLayout shimmerFrameLayout;

    private void AppBarWork() {
        TextView titleBar = findViewById(R.id.title_app);
        ImageView back = findViewById(R.id.back);
        titleBar.setText("Appointment History");
        back.setOnClickListener(v -> {
            finish();
        });
    }

    /*   @Override
       public void finish() {
           super.finish();
           overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
       }
   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        AppBarWork();

        sharedPreferences = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, "");
        Log.d(TAG, "onCreate: " + userId);
        historyModelList = new ArrayList<>();

        shimmerFrameLayout = findViewById(R.id.shimmerContentView);
        wholeThing = findViewById(R.id.wholeThing);
        dataNotAvailable = findViewById(R.id.doctorNotFound);
        srl = findViewById(R.id.srl);
        recyclerView = findViewById(R.id.historyList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        srl.setOnRefreshListener(() -> {
            GetAppointmentList();
        });

        GetAppointmentList();
    }

    private void GetAppointmentList() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        recyclerView.setVisibility(View.GONE);
        String url = BASE_URL + "appointment_info_list.php?doctor_id=" + userId;
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, url,
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
                            historyModel.setAppointmentType(jsonObject.getString("appointment_type"));
                            try {
                                historyModel.setHospital(jsonObject.getString("offline_work_place"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
                        historyAdapter = new HistoryAdapter(historyModelList, this,this);
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
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
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
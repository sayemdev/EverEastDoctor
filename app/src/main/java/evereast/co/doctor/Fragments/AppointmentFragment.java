package evereast.co.doctor.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.FEE;
import static evereast.co.doctor.Constants.PATIENT_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evereast.co.doctor.Adapter.RunningAppointmentsAdapter;
import evereast.co.doctor.Model.HistoryModel;
import evereast.co.doctor.R;

public class AppointmentFragment extends Fragment {

    private static final String TAG = "HistoryActivity";
    Activity activity;
    RecyclerView recyclerView;
    List<HistoryModel> historyModelList;
    RunningAppointmentsAdapter runningAppointmentsAdapter;
    SharedPreferences sharedPreferences;
    String userId, name, fee;
    ProgressBar progressBar;
    ArrayList<String> arrayList;
    SwitchCompat switchCompat;
    RelativeLayout relativeLayout;
    LinearLayout statusTV;
    String myId;
    boolean isNew = true;
    SwipeRefreshLayout srl;
    LinearLayout doctorNotFoundLayout;
    RelativeLayout wholeThing;
    Context context;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    public AppointmentFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        context = view.getContext();
        isNew = true;

        arrayList = new ArrayList<>();

        statusTV = view.findViewById(R.id.activeInactive);
        switchCompat = view.findViewById(R.id.statusSwitch);
        relativeLayout = view.findViewById(R.id.trackBackground);
        srl = view.findViewById(R.id.srl);
        doctorNotFoundLayout = view.findViewById(R.id.doctorNotFound);
        wholeThing = view.findViewById(R.id.wholeThing);

        myId = view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, "");

        relativeLayout = view.findViewById(R.id.trackBackground);

        relativeLayout = view.findViewById(R.id.trackBackground);


        String url = BASE_URL + "gp_status.php?id=" + myId;
   /*     Volley.newRequestQueue(view.getContext()).add(new StringRequest(Request.Method.GET, url, response -> {
            Log.d(TAG, "onCreateView: " + url);
            try {
                Log.d(TAG, "onCreateView: " + response);
                JSONObject object = new JSONObject(response);
                if (object.getString("d_category").equals("General Practitioner")) {
                    statusTV.setVisibility(View.VISIBLE);
                    if (object.getString("d_active").equals("Online")) {
                        switchCompat.setChecked(false);
                    } else if (object.getString("d_active").equals("Offline")) {
                        switchCompat.setChecked(true);
                    }
                    isNew = false;
                } else {
                    statusTV.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }));*/

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isNew) {
                Log.d(TAG, "onCreateView: OLD");
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(view.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage(!isChecked ? "By turning on you will be available for an instant appointment." : "By turning off you will go offline and patients might not get your appointment.");
                builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                    isNew = true;
                    switchCompat.setChecked(!isChecked);
                    dialogInterface.dismiss();
                });
                builder.setPositiveButton("Okay", (dialogInterface, i) -> {
                    isNew = false;
                    if (isChecked) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, "")).child("OnlineStatus").setValue("Online");
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, "")).child("OnlineStatus").setValue("Offline");
                    }
                    CheckUncheck(isChecked ? "Offline" : "Online", view);
                    dialogInterface.dismiss();
                });
                builder.create().show();
            } else {
                isNew = false;
            }
            if (isChecked) {
                relativeLayout.setBackgroundResource(R.drawable.status_background_checked);
            } else {
                relativeLayout.setBackgroundResource(R.drawable.status_background);
            }

        });


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

        srl.setOnRefreshListener(this::GetAppointmentList);

        return view;
    }

    private void CheckUncheck(String status, View view) {
        Volley.newRequestQueue(view.getContext()).add(new StringRequest(Request.Method.POST, PATIENT_URL + "update_status.php", response -> {
            Log.d(TAG, "CheckUncheck: " + response);
        }, error -> {
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("id", myId);
                map.put("status", status);
                return map;
            }
        });
    }

    private void GetAppointmentList() {
        String url = BASE_URL + "appointment_info_list.php?doctor_id=" + userId;
        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, url,
                response -> {
                    srl.setRefreshing(false);
                    Log.d(TAG, "GetAppointmentList: " + url);
                    Log.d(TAG, "GetAppointmentList: " + response);
                    progressBar.setVisibility(View.GONE);
                    doctorNotFoundLayout.setVisibility(View.GONE);
                    wholeThing.setBackgroundColor(context.getResources().getColor(R.color.dark_background));
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
                            } catch (Exception e) {
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
                            Log.d("ViewHold_Ac", "onBindViewHolder: " + jsonObject.getString("appointment_type"));
                            if (!jsonObject.getString("date").equals("null")
                                    && jsonObject.getString("status").equals("Confirmed")
                                    && !jsonObject.getString("appointment_type").equals("1")
                                    && jsonObject.getString("payment_status").equals("Paid")) {
                                historyModelList.add(historyModel);
                            }
                        }

                        if (historyModelList.size() == 0) {
                            doctorNotFoundLayout.setVisibility(View.VISIBLE);
                            wholeThing.setBackgroundColor(context.getResources().getColor(R.color.white));
                            recyclerView.setVisibility(View.GONE);
                        }
                        runningAppointmentsAdapter = new RunningAppointmentsAdapter(historyModelList, getContext());
                        recyclerView.setAdapter(runningAppointmentsAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        doctorNotFoundLayout.setVisibility(View.VISIBLE);
                        wholeThing.setBackgroundColor(context.getResources().getColor(R.color.white));
                        recyclerView.setVisibility(View.GONE);
                    }
                },
                error -> {
                    srl.setRefreshing(false);
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    doctorNotFoundLayout.setVisibility(View.VISIBLE);
                    wholeThing.setBackgroundColor(context.getResources().getColor(R.color.white));
                    recyclerView.setVisibility(View.GONE);
                }));
    }


}

package evereast.co.doctor.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.Adapter.AppointmentNotificationAdapter;
import evereast.co.doctor.Constants;
import evereast.co.doctor.Model.AppointmentNotificationModel;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.PROFILE_FILES;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

public class AppointmentNotificationFragment extends Fragment {
    private static final String TAG = "AppointmentFragment";
    RecyclerView recyclerView;
    List<AppointmentNotificationModel> notificationModelList;
    String userId;

    public AppointmentNotificationFragment() {
        // Required empty public constructor
    }
Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_notification, container, false);
context=view.getContext();
        userId = view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, "");

        notificationModelList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.ap_notification_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

     GetNotifications();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetNotifications();
    }

    private void GetNotifications() {
        String url = Constants.BASE_URL + "get_notification.php?user_id=" + userId;
        Volley.newRequestQueue(context).add(new StringRequest(Request.Method.GET, url, response -> {
            try {
                Log.d(TAG, "onCreateView: " + url);
                JSONArray jsonArray = new JSONArray(response);
                notificationModelList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    AppointmentNotificationModel notificationModel = new AppointmentNotificationModel();
                    notificationModel.setTitle(object.getString("title"));
                    notificationModel.setBody(object.getString("body"));
                    notificationModel.setNotificationId(object.getString("notification_id"));
                    notificationModel.setSender(object.getString("sender"));
                    notificationModel.setReceiver(object.getString("reciever"));
                    notificationModel.setIntent(object.getString("intent"));
                    notificationModel.setStatus(object.getString("status"));
                    notificationModel.setAppointmentId(object.getString("appointment_id"));
                    notificationModel.setSendTime(object.getString("send_time"));
                    notificationModel.setImage(PROFILE_FILES+object.getString("patient_id")+".jpg");
                    notificationModel.setPatientId(object.getString("patient_id"));
                    notificationModel.setDoctorName(object.getString("p_name"));
                    notificationModel.setPatientAddress(object.getString("p_address"));
                    notificationModel.setPatientBirthday(object.getString("p_bdate"));
                    notificationModel.setPatientGender(object.getString("p_gender"));
                    notificationModel.setPatientToken(object.getString("d_android_token"));
                    notificationModelList.add(notificationModel);
                }
                recyclerView.setAdapter(new AppointmentNotificationAdapter(notificationModelList,context));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }));

    }
}
package evereast.co.doctor.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import evereast.co.doctor.Adapter.HomePagerAdapter;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    Activity activity;
    SwitchCompat switchCompat;
    RelativeLayout relativeLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout onlineView;

    public HomeFragment(Activity activity) {
        this.activity = activity;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        onlineView = view.findViewById(R.id.onlineStatusLT);
        switchCompat = view.findViewById(R.id.statusSwitch);
        relativeLayout = view.findViewById(R.id.trackBackground);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        AppointmentFragment appointmentFragment = new AppointmentFragment(activity);
        EPrescriptionFragment ePrescriptionFragment = new EPrescriptionFragment();
        DrugDirectoryFragment drugDirectoryFragment = new DrugDirectoryFragment();

        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        homePagerAdapter.addFragment("lklj", appointmentFragment, activity, R.drawable.appointment_doct);
        homePagerAdapter.addFragment("lkj", drugDirectoryFragment, activity, R.drawable.ic_search);
        homePagerAdapter.addFragment("lkj", ePrescriptionFragment, activity, R.drawable.ic_prescription);
        viewPager.setAdapter(homePagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.appointment_doct);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_prescription);


        Volley.newRequestQueue(view.getContext()).add(new StringRequest(Request.Method.GET, BASE_URL + "gp_status.php?id=" + view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, ""), response -> {
            try {
                Log.d(TAG, "onCreateView: " + response);
                JSONObject object = new JSONObject(response);
                if (object.getString("d_category").equals("General Practitioner")) {
                    onlineView.setVisibility(View.VISIBLE);
                    switchCompat.setChecked(!object.getString("d_active").equals("Online"));
                } else {
                    onlineView.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }));

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                relativeLayout.setBackgroundResource(R.drawable.status_background_checked);
                FirebaseDatabase.getInstance().getReference().child("Users").child(view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, "")).child("OnlineStatus").setValue("Online");
            } else {
                relativeLayout.setBackgroundResource(R.drawable.status_background);
                FirebaseDatabase.getInstance().getReference().child("Users").child(view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, "")).child("OnlineStatus").setValue("Offline");
            }
        });

        return view;
    }
}
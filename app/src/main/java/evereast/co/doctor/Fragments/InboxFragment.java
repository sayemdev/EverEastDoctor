package evereast.co.doctor.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import evereast.co.doctor.Adapter.NotificationPagerAdapter;
import evereast.co.doctor.R;

public class InboxFragment extends Fragment {

    String route;
    TabLayout tabLayout;
    ViewPager viewPager;
    public InboxFragment(String route) {
        // Required empty public constructor
        this.route = route;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        NotificationsFragment notificationsFragment = new NotificationsFragment();
        AppointmentNotificationFragment appointmentFragment = new AppointmentNotificationFragment();

        NotificationPagerAdapter notificationPagerAdapter = new NotificationPagerAdapter(getChildFragmentManager());
        notificationPagerAdapter.addFragment("Appointments", appointmentFragment);
        notificationPagerAdapter.addFragment("Notifications", notificationsFragment);

        viewPager.setAdapter(notificationPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        try {
            if (route.equals("Post")) {
                viewPager.setCurrentItem(1);
            } else {
                viewPager.setCurrentItem(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }
}
package evereast.co.doctor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import evereast.co.doctor.Adapter.NotificationPagerAdapter;
import evereast.co.doctor.databinding.FragmentHomeAppointmentBinding;

public class HomeAppointmentFragment extends Fragment {

    public HomeAppointmentFragment() {

    }

    FragmentHomeAppointmentBinding binding;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeAppointmentBinding.inflate(inflater);
        view=binding.getRoot();

        AppointmentFragment appointmentFragment = new AppointmentFragment();
        OfflineFragment offlineFragment = new OfflineFragment();

        NotificationPagerAdapter notificationPagerAdapter = new NotificationPagerAdapter(getChildFragmentManager());
        notificationPagerAdapter.addFragment("ONLINE", appointmentFragment);
//        notificationPagerAdapter.addFragment("OFFLINE", offlineFragment);

        binding.viewPager.setSaveEnabled(false);
        binding.viewPager.setAdapter(notificationPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}
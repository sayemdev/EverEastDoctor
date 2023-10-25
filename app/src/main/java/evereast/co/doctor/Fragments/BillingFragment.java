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

public class BillingFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public BillingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        BillingSummaryFragment billingSummaryFragment = new BillingSummaryFragment();
        BillingHistoryFragment billingHistoryFragment = new BillingHistoryFragment();

        NotificationPagerAdapter notificationPagerAdapter = new NotificationPagerAdapter(getChildFragmentManager());
        notificationPagerAdapter.addFragment("Billing Summary", billingSummaryFragment);
        notificationPagerAdapter.addFragment("Billing History", billingHistoryFragment);

        viewPager.setAdapter(notificationPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
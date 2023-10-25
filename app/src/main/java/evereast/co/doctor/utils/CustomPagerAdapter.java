package evereast.co.doctor.utils;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private Fragment currentFragment;

        public CustomPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            Fragment newFragment = (Fragment) object;

            if (currentFragment != newFragment) {
                // Pause the previous fragment
                if (currentFragment != null) {
                    currentFragment.onPause();
                }

                // Resume the new fragment
                newFragment.onResume();

                currentFragment = newFragment;
            }

            super.setPrimaryItem(container, position, object);
        }
    }

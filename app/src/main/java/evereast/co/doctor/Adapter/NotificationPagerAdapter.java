package evereast.co.doctor.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Health Men created by Sayem Hossen Saimon on 1/3/2021 at 9:57 AM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class NotificationPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment>fragmentList=new ArrayList<>();
    List<String>stringList=new ArrayList<>();

    public NotificationPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public NotificationPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(String title, Fragment fragment){
        fragmentList.add(fragment);
        stringList.add(title);
    }
}

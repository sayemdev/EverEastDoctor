package evereast.co.doctor.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.R;
import jxl.Workbook;

/**
 * Health Men created by Sayem Hossen Saimon on 12/10/2020 at 3:57 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class AirPresSliderAdapter extends PagerAdapter {


    Activity activity;
    LayoutInflater layoutInflater;
    String heading;
    ArrayList<String> selectedItemsList, mainItemsList=new ArrayList<>(), suggestionList;
    private AsyncHttpClient client;
    private Workbook workbook;

    public AirPresSliderAdapter(Activity activity,ArrayList<String>suggestionList) {
        this.activity = activity;
        this.suggestionList=suggestionList;
    }


    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.air_pager_item, container, false);

        TextView sliderHeadingTextView = view.findViewById(R.id.headingTV);
        sliderHeadingTextView.setText(heading);
        ChipGroup chipGroup=view.findViewById(R.id.cgNormal);
        SetChipGroup(suggestionList,chipGroup);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ScrollView) object);
    }

    public void SetChipGroup(List<String> mainList, ChipGroup chipGroup) {
        Log.i("TAG", "SetChipGroup: "+mainList);
        for (int i = 0; i < mainList.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.chip_custom, null);
            Chip chip2 = view.findViewById(R.id.chup);
            chip2.setText(mainList.get(i));
            chip2.setId(i);
            chip2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
//                    buttonView.setEnabled(false);
                }
            });
            chipGroup.addView(view);
        }
    }
}

package evereast.co.doctor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/10/2020 at 3:57 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class SliderAdapterBn extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapterBn(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);
        ImageView sliderImageView = view.findViewById(R.id.sliderImageView);
        TextView sliderHeadingTextView = view.findViewById(R.id.headerTextView);
        TextView sliderDescriptionTextView = view.findViewById(R.id.paragraphTextView);
        //Arrays
        int[] slide_image = {
                R.drawable.ic_doctor,
                R.drawable.ic_doctor,
                R.drawable.ic_doctor
        };
        String[] slide_description_bn = {
                context.getString(R.string.lorem_text_bn),
                context.getString(R.string.lorem_text_bn),
                context.getString(R.string.lorem_text_bn),
        };
        String[] slide_headings_bn = {
                context.getString(R.string.lorem_heading_bn),
                context.getString(R.string.lorem_heading_bn),
                context.getString(R.string.lorem_heading_bn),
        };
        try {
            sliderImageView.setImageResource(slide_image[position]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sliderHeadingTextView.setText(slide_headings_bn[position]);
        sliderDescriptionTextView.setText(slide_description_bn[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}

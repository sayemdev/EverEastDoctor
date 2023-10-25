package evereast.co.doctor.Adapter;


import static evereast.co.doctor.Constants.FILE_PATH;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import evereast.co.doctor.Model.Image;
import evereast.co.doctor.R;


/**
 * Health Men created by Sayem Hossen Saimon on 12/10/2020 at 3:57 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class ImageSliderAdapter extends PagerAdapter {


    Context context;
    LayoutInflater layoutInflater;
    List<Image> imagesLists;
    ClickListener clickListener;

    public ImageSliderAdapter(Context context, List<Image> imagesLists, ClickListener clickListener) {
        this.context = context;
        this.imagesLists = imagesLists;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return imagesLists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PhotoView photoView = new PhotoView(context);
        Glide.with(context).load(FILE_PATH + imagesLists.get(position).getImageName()).placeholder(R.drawable.round_health).into(photoView);
        container.addView(photoView);
        photoView.setOnClickListener(view -> {
            clickListener.OnClick(position);
        });
        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((PhotoView) object);
    }

    public interface ClickListener {
        void OnClick(int position);
    }
}

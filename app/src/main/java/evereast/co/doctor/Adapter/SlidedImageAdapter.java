package evereast.co.doctor.Adapter;

import static evereast.co.doctor.Constants.FILE_PATH;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.List;

import evereast.co.doctor.Model.Image;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.SlidedImageItemBinding;

public class SlidedImageAdapter extends RecyclerView.Adapter<SlidedImageAdapter.ViewHolder> {
    private static final String TAG = "SlidedImageAdapter";
    OnClickListener onClickListener;
    Context context;
    List<Image> imagesModelList;
    int lastPosition;
    ViewPager viewPager;


    public SlidedImageAdapter(OnClickListener onClickListener, Context context, List<Image> imagesModelList, int position, ViewPager viewPager) {
        this.onClickListener = onClickListener;
        this.context = context;
        this.imagesModelList = imagesModelList;
        this.lastPosition = position;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SlidedImageItemBinding binding = SlidedImageItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (lastPosition == holder.getAdapterPosition()) {
            holder.binding.getRoot().setBackgroundResource(R.drawable.rounded_outlined);
            Log.d(TAG, "onBindViewHolder: " + lastPosition + "   Position    " + holder.getAdapterPosition());
//            Toast.makeText(context, "Position " + position, Toast.LENGTH_SHORT).show();
            onClickListener.SelectedImageListener(holder.getAdapterPosition(), imagesModelList.get(position).getTitle());
        } else {
            holder.binding.getRoot().setBackground(null);
        }

        Glide.with(context).load(FILE_PATH + imagesModelList.get(holder.getAdapterPosition()).getImageName()).placeholder(R.drawable.round_health).into(holder.binding.imageId);

        holder.itemView.setOnClickListener(v -> {
            viewPager.setCurrentItem(holder.getAdapterPosition());
            onClickListener.OnClick(holder.getAdapterPosition(), imagesModelList.get(position).getImageName());
            lastPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return imagesModelList.size();
    }

    public interface OnClickListener {
        void OnClick(int position, String imageName);

        void SelectedImageListener(int position, String title);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SlidedImageItemBinding binding;

        public ViewHolder(@NonNull SlidedImageItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

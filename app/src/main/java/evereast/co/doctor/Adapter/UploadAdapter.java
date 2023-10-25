package evereast.co.doctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import evereast.co.doctor.Activity.ZoomImageActivity;
import evereast.co.doctor.Constants;
import evereast.co.doctor.Model.UploadModel;
import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/30/2020 at 8:27 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder> {

    Context context;
    List<UploadModel> uploadModelList;
    String response;

    public UploadAdapter(Context context, List<UploadModel> uploadModelList, String response) {
        this.context = context;
        this.uploadModelList = uploadModelList;
        this.response = response;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UploadModel uploadModel = uploadModelList.get(position);
        Glide.with(context).load(Constants.FILE_PATH + uploadModel.getImageUrl()).placeholder(R.drawable.ic_upload_file).into(holder.appointmentResImageView);
        holder.titleTextView.setText(WordUtils.capitalizeFully(uploadModel.getTitle()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, ZoomImageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("link",Constants.FILE_PATH +  uploadModel.getImageUrl());
            intent.putExtra("imagesList", response);
            intent.putExtra("selectedPosition", position);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return uploadModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appointmentResImageView;
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentResImageView = itemView.findViewById(R.id.uploadImagePreview);
            titleTextView = itemView.findViewById(R.id.imageTitle);
        }
    }
}

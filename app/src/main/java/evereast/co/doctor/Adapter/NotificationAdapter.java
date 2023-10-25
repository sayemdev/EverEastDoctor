package evereast.co.doctor.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import evereast.co.doctor.DialogFragment.NotificationFragment;
import evereast.co.doctor.Model.NotificationModel;
import evereast.co.doctor.R;

/**
 * Doctor created by Sayem Hossen Saimon on 3/13/2021 at 7:26 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<NotificationModel> notificationModelList;
/*

    public NotificationAdapter(Context context, List<NotificationModel> notificationModelList) {
        this.context = context;
        this.notificationModelList = notificationModelList;
    }
*/

    public NotificationAdapter(Context context, List<NotificationModel> notificationModelList) {
        this.context = context;
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notificationModel=notificationModelList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.bodyTV.setText(Html.fromHtml(notificationModel.getBody(),Html.FROM_HTML_MODE_COMPACT));
        }else{
            holder.bodyTV.setText(Html.fromHtml(notificationModel.getBody()));
        }
        holder.titleTV.setText(notificationModel.getTitle());
        Glide.with(context).load(notificationModel.getImage()).into(holder.notificationImage);

        holder.itemView.setOnClickListener(v -> {
            NotificationFragment notificationFragment=new NotificationFragment(notificationModel.getTitle(),notificationModel.getBody(),notificationModel.getImage(),notificationModel.getLink());
            notificationFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), notificationFragment.getTag());
        });

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV,bodyTV;
        ImageView notificationImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV=itemView.findViewById(R.id.titleTV);
            bodyTV=itemView.findViewById(R.id.bodyTV);
            notificationImage=itemView.findViewById(R.id.notificationImage);
        }
    }
}

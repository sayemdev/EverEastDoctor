package evereast.co.doctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evereast.co.doctor.Activity.ViewDetailsActivity;
import evereast.co.doctor.Model.AppointmentNotificationModel;
import evereast.co.doctor.R;
import evereast.co.doctor.chat.ChatActivity;
import evereast.co.doctor.utils.GetTimeAgo;

import static evereast.co.doctor.Constants.BASE_URL;

/**
 * Helthmen Patient created by Sayem Hossen Saimon on 6/26/2021 at 8:28 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class AppointmentNotificationAdapter extends RecyclerView.Adapter<AppointmentNotificationAdapter.ViewHolder> {

    private static final String TAG = "AppointmentNotification";
    List<AppointmentNotificationModel> notificationModelList;
    Context context;

    public AppointmentNotificationAdapter(List<AppointmentNotificationModel> notificationModelList, Context context) {
        this.notificationModelList = notificationModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        if (!notificationModelList.get(position).getStatus().equals("New")) {
            holder.materialCardView.setCardBackgroundColor(Color.WHITE);
        } else {
            holder.materialCardView.setCardBackgroundColor(context.getResources().getColor(R.color.unseen_bg_color));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.notificationSubtitleTextView.setText(Html.fromHtml(notificationModelList.get(position).getBody(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.notificationSubtitleTextView.setText(Html.fromHtml(notificationModelList.get(position).getBody()));
        }
        holder.notificationTitleTextView.setText(notificationModelList.get(position).getTitle());
        holder.notificationTimeTextView.setText(GetTimeAgo.getTimeAgo1(Long.parseLong(notificationModelList.get(position).getSendTime()), context));
        Glide.with(context).load(/*PROFILE_FILES +*/ notificationModelList.get(position).getImage()).placeholder(R.drawable.round_health).into(holder.circleImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("appointmentId", notificationModelList.get(position).getAppointmentId());
            intent.putExtra("p_name", notificationModelList.get(position).getDoctorName());
            intent.putExtra("pAddress", notificationModelList.get(position).getPatientAddress());
            intent.putExtra("pBirthday", notificationModelList.get(position).getPatientBirthday());
            intent.putExtra("Patient_id", notificationModelList.get(position).getPatientId());
            intent.putExtra("pGender", notificationModelList.get(position).getPatientGender());

            Intent chatIntent = new Intent(context, ChatActivity.class);
            chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            chatIntent.putExtra("appointmentId", notificationModelList.get(position).getAppointmentId());
            chatIntent.putExtra("senderId", notificationModelList.get(position).getPatientId());
            chatIntent.putExtra("sender_token", notificationModelList.get(position).getDoctorName());
            chatIntent.putExtra("sender_name", notificationModelList.get(position).getPatientToken());

            if (notificationModelList.get(position).getIntent().equals("chat")) {
                context.startActivity(chatIntent);
            } else if (notificationModelList.get(position).getIntent().equals("appointment")) {
                context.startActivity(intent);
            }
            Volley.newRequestQueue(context).add(new StringRequest(Request.Method.POST, BASE_URL + "see_notification.php", response -> {
                Log.d(TAG, "onBindViewHolder: " + response);
            }, error -> {
                error.printStackTrace();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("noti_id", notificationModelList.get(position).getNotificationId());
                    return map;
                }
            });

        });

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitleTextView, notificationSubtitleTextView, notificationTimeTextView;
        ImageView circleImageView;
        MaterialCardView materialCardView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            notificationSubtitleTextView = itemView.findViewById(R.id.notificationSubtitle);
            notificationTimeTextView = itemView.findViewById(R.id.notificationTime);
            notificationTitleTextView = itemView.findViewById(R.id.notificationTitle);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            materialCardView = itemView.findViewById(R.id.mcv);
        }
    }
}

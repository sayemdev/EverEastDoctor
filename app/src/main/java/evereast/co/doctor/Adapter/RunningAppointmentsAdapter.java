package evereast.co.doctor.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import evereast.co.doctor.Activity.ViewDetailsActivity;
import evereast.co.doctor.Constants;
import evereast.co.doctor.Model.HistoryModel;
import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/29/2020 at 1:10 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class RunningAppointmentsAdapter extends RecyclerView.Adapter<RunningAppointmentsAdapter.ViewHolder> {

    List<HistoryModel> historyModelList;
    Context context;

    public RunningAppointmentsAdapter(List<HistoryModel> historyModelList, Context context) {
        this.historyModelList = historyModelList;
        this.context = context;
    }

    public RunningAppointmentsAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.running_ap_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryModel historyModel = historyModelList.get(position);
        holder.appointmentTimeTextView.setText(historyModel.getDate());
        holder.patientNameTextView.setText("Patient: " + historyModel.getPatientName());
        holder.doctorNameTextView.setText(historyModel.getDoctorName());
        holder.doctorCategoryTextView.setText("Service: " + historyModel.getCategory());
        Glide.with(context).load(Constants.PROFILE_FILES + historyModel.getPatientID()+".jpg").placeholder(R.drawable.round_health).into(holder.dProfileImageView);
        holder.feeTextView.setText("Fee:" + historyModel.getFee() + "tk");

        if (historyModel.getRated().equals("Yes")) {
            holder.rateNow.setVisibility(View.VISIBLE);
        } else {
            holder.rateNow.setVisibility(View.GONE);
        }

        if (historyModel.getAppointmentType().equals("1")) {
            holder.appointmentTimeTextView.setText(historyModel.getDate());
        } else {
            holder.appointmentTimeTextView.setText(historyModel.getDate() + ", " + historyModel.getTime());
        }
        holder.ratingBar.setRating(Float.parseFloat(historyModel.getRating()));

        Log.d("ViewHolder_TAG", "onBindViewHolder: " + historyModel.getCategory() + "========" + historyModel.getFee());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewDetailsActivity.class);
            intent.putExtra("appointmentId", historyModel.getAppointmentId());
            intent.putExtra("p_name", historyModel.getPatientName());
            intent.putExtra("pAddress", historyModel.getpAddress());
            intent.putExtra("pBirthday", historyModel.getPatientBirthday());
            intent.putExtra("pGender", historyModel.getPatientGender());
            intent.putExtra("Patient_id", historyModel.getPatientID());
            intent.putExtra("history", false);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appointmentTimeTextView, patientNameTextView, doctorNameTextView, doctorCategoryTextView, feeTextView, rateNow;
        CircularImageView dProfileImageView;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentTimeTextView = itemView.findViewById(R.id.showDate);
            patientNameTextView = itemView.findViewById(R.id.patientNameTv);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTv);
            doctorCategoryTextView = itemView.findViewById(R.id.doctorCategory);
            feeTextView = itemView.findViewById(R.id.feeTv);
            dProfileImageView = itemView.findViewById(R.id.dProfileImageView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rateNow = itemView.findViewById(R.id.rateNowTV);
        }
    }
}

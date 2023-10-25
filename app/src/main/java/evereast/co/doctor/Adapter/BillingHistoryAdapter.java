package evereast.co.doctor.Adapter;

import static evereast.co.doctor.Constants.PROFILE_FILES;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import evereast.co.doctor.Model.HistoryModel;
import evereast.co.doctor.R;

/**
 * Doctor created by Sayem Hossen Saimon on 8/16/2021 at 12:46 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class BillingHistoryAdapter extends RecyclerView.Adapter<BillingHistoryAdapter.ViewHolder> {
    Context context;
    List<HistoryModel> historyModelList;

    public BillingHistoryAdapter(Context context, List<HistoryModel> historyModelList) {
        this.context = context;
        this.historyModelList = historyModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        HistoryModel historyModel = historyModelList.get(position);
        holder.dateTimeTV.setText(historyModel.getDate() + ", " + historyModel.getTime());
        holder.patientNameTV.setText("Patient: " + historyModel.getPatientName());
        Glide.with(context).load(PROFILE_FILES + historyModel.getPatientID() + ".jpg").placeholder(R.drawable.round_health).into(holder.patientProfileImageView);
        String fee = historyModel.getFee().equals("null") ? "Free" : "Fee:" + historyModel.getFee() + "tk";
        holder.feeTV.setText(fee);
        String gender = historyModel.getPatientGender().equals("মহিলা") ? context.getString(R.string.female) : historyModel.getPatientGender().equals("পুরুষ") ? context.getString(R.string.male) : historyModel.getPatientGender().equals("বলতে চাই না") ? "rather not to say" : historyModel.getPatientGender();
        holder.genderAndAgeTV.setText(historyModel.getPatientBirthday() + " years old " + gender);
        holder.ratingBar.setRating(Float.parseFloat(historyModel.getRating()));

        holder.appointmentStatusTV.setText(historyModel.getApStatus());
        holder.paymentStatusTV.setText(historyModel.getPaymentStatus());

        if (historyModel.getPaymentStatus().equals("Paid")) {
            holder.paymentStatusTV.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.light_green)));
        }

        if (historyModel.getApStatus().equals("Confirmed")) {
            holder.appointmentStatusTV.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.deep_green)));
        } else if (historyModel.getApStatus().equals("Completed")) {
            holder.appointmentStatusTV.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.blue)));
        } else {
            holder.appointmentStatusTV.setVisibility(View.GONE);
            holder.paymentStatusTV.setBackgroundResource(R.drawable.round);
        }

        if (historyModel.getAppointmentType().trim().equals("1")) {
            holder.offline.setVisibility(View.VISIBLE);
            holder.offline.setText(historyModel.getHospital());
            holder.dateTimeTV.setText(historyModel.getDate());
        } else {
            holder.offline.setVisibility(View.VISIBLE);
            holder.offline.setText("Online");
            holder.dateTimeTV.setText(historyModel.getDate() + ", " + historyModel.getTime());
        }


    }

    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appointmentStatusTV;
        public TextView paymentStatusTV;
        TextView dateTimeTV, patientNameTV, genderAndAgeTV, feeTV;
        CircularImageView patientProfileImageView;
        RatingBar ratingBar;
        TextView offline;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dateTimeTV = itemView.findViewById(R.id.showDate);
            patientNameTV = itemView.findViewById(R.id.patientNameTv);
            genderAndAgeTV = itemView.findViewById(R.id.ageGenderTv);
            patientProfileImageView = itemView.findViewById(R.id.dProfileImageView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            feeTV = itemView.findViewById(R.id.feeTV);
            appointmentStatusTV = itemView.findViewById(R.id.appointmentStatusTV);
            paymentStatusTV = itemView.findViewById(R.id.paymentStatusTV);
            offline = itemView.findViewById(R.id.offline);
        }
    }
}

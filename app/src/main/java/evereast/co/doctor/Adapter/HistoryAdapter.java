package evereast.co.doctor.Adapter;

import static evereast.co.doctor.Constants.PROFILE_FILES;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import evereast.co.doctor.Activity.ViewDetailsActivity;
import evereast.co.doctor.DialogFragment.RatePatientDialogFragment;
import evereast.co.doctor.Model.HistoryModel;
import evereast.co.doctor.R;


/**
 * Health Men created by Sayem Hossen Saimon on 12/29/2020 at 1:10 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<HistoryModel> historyModelList;
    Context context;
    RatePatientDialogFragment.ConfirmationValueListener confirmationValueListener;

    public HistoryAdapter(List<HistoryModel> historyModelList, Context context, RatePatientDialogFragment.ConfirmationValueListener confirmationValueListener) {
        this.historyModelList = historyModelList;
        this.context = context;
        this.confirmationValueListener = confirmationValueListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryModel historyModel = historyModelList.get(position);
        holder.appointmentTimeTextView.setText(historyModel.getDate() + ", " + historyModel.getTime());
        holder.patientNameTextView.setText("Patient: " + historyModel.getPatientName());
        holder.doctorNameTextView.setText(historyModel.getDoctorName());
        holder.doctorCategoryTextView.setText("Service: " + historyModel.getCategory());
        Glide.with(context).load(PROFILE_FILES + historyModel.getPatientID() + ".jpg").placeholder(R.drawable.round_health).into(holder.dProfileImageView);
        if (historyModel.getFee().equals("null")) {
            holder.feeTextView.setText("Free");
        } else {
            holder.feeTextView.setText("Fee:" + historyModel.getFee() + "tk");
        }
        holder.appointmentStatusTV.setText(historyModel.getApStatus());
        holder.paymentStatusTV.setText(historyModel.getPaymentStatus());

        if (historyModel.getPaymentStatus().equals("Paid") && historyModel.getApStatus().equals("Completed") && historyModel.getRated().equals("No")) {
            holder.rateNowTV.setVisibility(View.VISIBLE);
        } else {
            holder.rateNowTV.setVisibility(View.GONE);
        }

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
        if (historyModel.getAppointmentType().equals("1")) {
            holder.offline.setVisibility(View.VISIBLE);
            holder.offline.setText(historyModel.getHospital());
            holder.appointmentTimeTextView.setText(historyModel.getDate());
        } else {
            holder.offline.setVisibility(View.GONE);
            holder.appointmentTimeTextView.setText(historyModel.getDate() + ", " + historyModel.getTime());
        }
        holder.rateNowTV.setOnClickListener(v -> {
            RatePatientDialogFragment ratePatientDialogFragment = new RatePatientDialogFragment(context, historyModel.getAppointmentId(), historyModel.getDoctorId(), historyModel.getPatientID(), historyModel.getProfileName(), historyModel.getRating(), confirmationValueListener);
            ratePatientDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), ratePatientDialogFragment.getTag());
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewDetailsActivity.class);
            intent.putExtra("appointmentId", historyModel.getAppointmentId());
            intent.putExtra("p_name", historyModel.getPatientName());
            intent.putExtra("pAddress", historyModel.getpAddress());
            intent.putExtra("pBirthday", historyModel.getPatientBirthday());
            intent.putExtra("pGender", historyModel.getPatientGender());
            intent.putExtra("Patient_id", historyModel.getPatientID());
            intent.putExtra("history", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity) context).finish();
            context.startActivity(intent);
        });

        holder.ratingBar.setRating(Float.parseFloat(historyModel.getRating()));

    }

    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appointmentTimeTextView, patientNameTextView, doctorNameTextView, doctorCategoryTextView, feeTextView, appointmentStatusTV, rateNowTV, paymentStatusTV;
        CircularImageView dProfileImageView;
        RatingBar ratingBar;
        TextView offline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentTimeTextView = itemView.findViewById(R.id.showDate);
            patientNameTextView = itemView.findViewById(R.id.patientNameTv);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTv);
            doctorCategoryTextView = itemView.findViewById(R.id.doctorCategory);
            feeTextView = itemView.findViewById(R.id.feeTv);
            dProfileImageView = itemView.findViewById(R.id.dProfileImageView);
            paymentStatusTV = itemView.findViewById(R.id.paymentStatusTV);
            appointmentStatusTV = itemView.findViewById(R.id.appointmentStatusTV);
            rateNowTV = itemView.findViewById(R.id.rateNowTV);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            offline = itemView.findViewById(R.id.offline);
        }
    }
}

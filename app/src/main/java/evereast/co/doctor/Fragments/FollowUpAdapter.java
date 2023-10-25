package evereast.co.doctor.Fragments;

import static evereast.co.doctor.RXActivities.PPActivity.ADDRESS;
import static evereast.co.doctor.RXActivities.PPActivity.AGE;
import static evereast.co.doctor.RXActivities.PPActivity.AGE_TYPE;
import static evereast.co.doctor.RXActivities.PPActivity.APPOINTMENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.DATE;
import static evereast.co.doctor.RXActivities.PPActivity.GENDER;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.ParseException;
import java.util.List;

import evereast.co.doctor.Constants;
import evereast.co.doctor.DialogFragment.RatePatientDialogFragment;
import evereast.co.doctor.GetAge;
import evereast.co.doctor.Model.PatientsModel;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.PPActivity;
import evereast.co.doctor.Utils;

/**
 * Health Men created by Sayem Hossen Saimon on 1/26/2021 at 2:26 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class FollowUpAdapter extends RecyclerView.Adapter<FollowUpAdapter.ViewHolder> {
    Context context;
    List<PatientsModel> patientModelList;

    public FollowUpAdapter(Context context, List<PatientsModel> patientModelList) {
        this.context = context;
        this.patientModelList = patientModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.follow_up_appointment, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientsModel patientModel = patientModelList.get(position);
        holder.appointmentTimeTextView.setText(patientModel.getDate() + " at " + patientModel.getTime());
        holder.patientNameTextView.setText(patientModel.getPatientName());
        holder.feeTextView.setText("Fee:" + patientModel.getFeeAfterDiscount() + "tk");
        holder.doctorNameTextView.setText(patientModel.getDoctorName());
        holder.doctorCategoryTextView.setText(patientModel.getDoctorCategory());
        Glide.with(context).load(Constants.PROFILE_FILES + patientModel.getPatientId() + ".jpg").into(holder.dProfileImageView);

        int i = Integer.parseInt(patientModel.getAppointmentPosition()) + 1;
        holder.serial.setText(Utils.ordinal(i) + " appointment");

        holder.viewPrescription.setOnClickListener(v -> {
            Log.i("AGE_TRACK_TAG", "onBindViewHolder: " + "GOT");
            Intent intent = new Intent(context, PPActivity.class);
            intent.putExtra("appointmentId", patientModel.getAppointmentId());
            intent.putExtra(PATIENT_NAME, patientModel.getPatientName());
            intent.putExtra(ADDRESS, patientModel.getPatientAddress());
            intent.putExtra(GENDER, patientModel.getPatientGender());
            try {
                intent.putExtra(AGE, GetAge.getAge(patientModel.getPatientBirthDay()));
                Log.i("AGE_TRACK_TAG", "onBindViewHolder: " + "GOT");
            } catch (ParseException e) {
                e.printStackTrace();
                intent.putExtra(AGE, "");
                Log.i("AGE_TRACK_TAG", "ParseException " + "GOT" + e.getMessage());
            }
            intent.putExtra(AGE_TYPE, "Year");
            intent.putExtra(DATE, patientModel.getDate());
            intent.putExtra("prescription", "appointment");
            intent.putExtra(APPOINTMENT_ID, patientModel.getAppointmentId());
            intent.putExtra(PATIENT_ID, patientModel.getPatientId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        if (patientModel.getRated().equals("No") && patientModel.getAppointmentStatus().equals("Completed")) {
            holder.rateNowTV.setVisibility(View.VISIBLE);
        } else {
            holder.rateNowTV.setVisibility(View.GONE);
        }

        holder.ratingBar.setRating(Float.parseFloat(patientModel.getRating()));

        holder.rateNowTV.setOnClickListener(v -> {
            RatePatientDialogFragment ratePatientDialogFragment = new RatePatientDialogFragment(context, patientModel.getAppointmentId(), patientModel.getDoctorId(), patientModel.getPatientId(), patientModel.getPatientId() + ".jpg", patientModel.getRating(), confirm -> {
            });
            ratePatientDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), ratePatientDialogFragment.getTag());
        });

        holder.paymentStatusTV.setText(patientModel.getPaymentStatus());
        holder.appointmentStatusTV.setText(patientModel.getAppointmentStatus());

    }

    @Override
    public int getItemCount() {
        return patientModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentTimeTextView, patientNameTextView, doctorNameTextView, doctorCategoryTextView, feeTextView, rateNowTV, appointmentStatusTV, paymentStatusTV;
        CircularImageView dProfileImageView;
        Chip serial;
        Button viewPrescription;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentTimeTextView = itemView.findViewById(R.id.showDate);
            patientNameTextView = itemView.findViewById(R.id.patientNameTv);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTv);
            doctorCategoryTextView = itemView.findViewById(R.id.doctorCategory);
            feeTextView = itemView.findViewById(R.id.feeTv);
            dProfileImageView = itemView.findViewById(R.id.dProfileImageView);
            serial = itemView.findViewById(R.id.appointmentNumberChip);
            viewPrescription = itemView.findViewById(R.id.upPres);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rateNowTV = itemView.findViewById(R.id.rateNowTV);
            appointmentStatusTV = itemView.findViewById(R.id.appointmentStatusTV);
            paymentStatusTV = itemView.findViewById(R.id.paymentStatusTV);
        }
    }
}

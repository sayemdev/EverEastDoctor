package evereast.co.doctor.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.PROFILE_FILES;

public class RatePatientDialogFragment extends DialogFragment {

    private static final String TAG = "RateDoctorDialogFragmen";
    Context context;
    ConfirmationValueListener confirmationValueListener;
    RatingBar ratingBar;
    String rating = "0.5";
    Button rateNow;
    ImageView cancelButton, dImageView;
    String profileName;
    ProgressBar ratingProgressBar;
    LinearLayout mainRV;
    TextView ratingCountTV;
    EditText feedBackEt;
    String pRating;
    RatingBar pRatingBar;
    private final String appointmentId;
    private final String doctorId;
    private final String patientId;

    public RatePatientDialogFragment(Context context, String appointmentId, String doctorId, String patientId, String profileName, String rating,ConfirmationValueListener confirmationValueListener) {
        this.context = context;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.profileName = profileName;
        this.pRating = rating;
        this.confirmationValueListener=confirmationValueListener;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rate_doctor_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(true);
        alert.setCanceledOnTouchOutside(false);

        ratingCountTV = view.findViewById(R.id.ratingCount);
        dImageView = view.findViewById(R.id.pImageView);
        cancelButton = view.findViewById(R.id.cancel_bt);
        ratingBar = view.findViewById(R.id.rating);
        mainRV = view.findViewById(R.id.mainRV);
        ratingProgressBar = view.findViewById(R.id.ratingProgressbar);
        feedBackEt = view.findViewById(R.id.feedBackEt);
        ratingProgressBar.setVisibility(View.GONE);
        pRatingBar = view.findViewById(R.id.drRating);

        pRatingBar.setRating(Float.parseFloat(pRating));

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Glide.with(context).load(PROFILE_FILES + patientId + ".jpg").placeholder(R.drawable.round_health).into(dImageView);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            Log.d(TAG, "onCreateDialog: " + rating);
            this.rating = String.valueOf(rating);
            ratingCountTV.setText("Rating (" + rating + ")");
        });

        cancelButton.setOnClickListener(v -> dismiss());

        rateNow = view.findViewById(R.id.rateNow);
        rateNow.setOnClickListener(v -> {
            ratingProgressBar.setVisibility(View.VISIBLE);
            mainRV.setVisibility(View.INVISIBLE);
            Volley.newRequestQueue(context).add(new StringRequest(Request.Method.POST, BASE_URL + "rate.php", response -> {
                if (response.equals("Rated Successfully")) {
                    Toast.makeText(context, "You have rated successfully", Toast.LENGTH_SHORT).show();
                    confirmationValueListener.Confirmation(true);
                    dismiss();
                } else {
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    ratingProgressBar.setVisibility(View.GONE);
                    mainRV.setVisibility(View.VISIBLE);
                }
            }, error -> {
                ratingProgressBar.setVisibility(View.GONE);
                mainRV.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("patient_id", patientId);
                    map.put("doctor_id", doctorId);
                    map.put("rating", rating);
                    map.put("appointment_id", appointmentId);
                    map.put("feed_back", feedBackEt.getText().toString().trim());
                    return map;
                }
            });
        });

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            confirmationValueListener = (ConfirmationValueListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ConfirmationValueListener");
        }
    }

    public interface ConfirmationValueListener {
        void Confirmation(boolean confirm);
    }

}
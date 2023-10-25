package evereast.co.doctor.Fragments;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Utils.dateFromCalender;
import static evereast.co.doctor.Utils.getMonthForInt;
import static evereast.co.doctor.Utils.getTimeFromCalender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import evereast.co.doctor.DialogFragment.RequestPaymentDialogFragment;
import evereast.co.doctor.R;
import evereast.co.doctor.databinding.FragmentBillingSummaryBinding;

public class BillingSummaryFragment extends Fragment {

    private static final String TAG = "BillingSummaryFragment";
    ImageView goPreviousButton, goNextButton;
    TextView monthAndYearTV;
    TextView tillDateTV, totalDueTV, lastUpdateTV, totalPatientTV, totalEarningsTV, totalPatientInMonthTV, totalEarningsInMonthTV, oldPatientsTV, oldPatientsEarningsTV, newPatientsEarningsTV, newPatientsTV;
    Button applyForPayment;
    TextView totalBillReceivedTV;
    View view;
    FragmentBillingSummaryBinding binding;
    boolean isOffline = false;
    private FragmentActivity myContext;
    private TextView totalWithdrawnTV, lastUpdatedTopTV;
    private int totalEarning = 0, totalWithdraw = 0;

    public BillingSummaryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBillingSummaryBinding.inflate(getLayoutInflater());
        view = binding.getRoot();

        lastUpdatedTopTV = view.findViewById(R.id.lastUpdatedTop);
        totalBillReceivedTV = view.findViewById(R.id.totalBillReceivedTV);
        totalDueTV = view.findViewById(R.id.totalDue);
        monthAndYearTV = view.findViewById(R.id.monthAndYearTV);
        goNextButton = view.findViewById(R.id.goNextImageView);
        goPreviousButton = view.findViewById(R.id.goPreviousImageView);
        totalEarningsTV = view.findViewById(R.id.totalEarning);
        totalEarningsInMonthTV = view.findViewById(R.id.totalEarningInMonth);
        totalPatientTV = view.findViewById(R.id.totalPatient);
        totalPatientInMonthTV = view.findViewById(R.id.totalPatientInMonth);
        oldPatientsTV = view.findViewById(R.id.oldPatient);
        oldPatientsEarningsTV = view.findViewById(R.id.oldPatientEarning);
        newPatientsEarningsTV = view.findViewById(R.id.newPatientEarning);
        newPatientsTV = view.findViewById(R.id.newPatientMonth);
        tillDateTV = view.findViewById(R.id.tillDate);
        lastUpdateTV = view.findViewById(R.id.lastUpdated);
        applyForPayment = view.findViewById(R.id.applyForPayment);
        totalWithdrawnTV = view.findViewById(R.id.totalWithdraw);

        Calendar calendar = Calendar.getInstance();
        SetTextOnResult(calendar);
        goNextButton.setEnabled(false);

        binding.offline.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                isOffline = true;
                binding.online.setChecked(false);
                GetBillingSummary(view.getContext(), calendar);
            }
        });

        binding.online.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                isOffline = false;
                binding.offline.setChecked(false);
                GetBillingSummary(view.getContext(), calendar);
            }
        });


        goPreviousButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            SetTextOnResult(calendar);
            GetBillingSummary(view.getContext(), calendar);
            if (calendar.get(Calendar.YEAR) == (Calendar.getInstance().get(Calendar.YEAR)) && calendar.get(Calendar.MONTH) >= Calendar.getInstance().get(Calendar.MONTH)) {
                goNextButton.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                goNextButton.setEnabled(false);
            } else {
                goNextButton.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.app_bar_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                goNextButton.setEnabled(true);
            }
        });


        goNextButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            SetTextOnResult(calendar);
            GetBillingSummary(view.getContext(), calendar);
            if (calendar.get(Calendar.YEAR) == (Calendar.getInstance().get(Calendar.YEAR)) && calendar.get(Calendar.MONTH) >= Calendar.getInstance().get(Calendar.MONTH)) {
                goNextButton.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                goNextButton.setEnabled(false);
            } else {
                goNextButton.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.app_bar_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                goNextButton.setEnabled(true);
            }
        });

        applyForPayment.setOnClickListener(view1 -> {
            RequestPaymentDialogFragment requestPaymentDialogFragment = new RequestPaymentDialogFragment(totalWithdraw, totalEarning,isOffline);
            requestPaymentDialogFragment.show(myContext.getSupportFragmentManager(), requestPaymentDialogFragment.getTag());
        });

        GetBillingSummary(view.getContext(), calendar);

        return view;
    }

    private void GetBillingSummary(Context context, Calendar calendar) {
        String month = (calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1) + "";
        String url = BASE_URL + (isOffline ? "offline_billing_summury.php?date=" : "billing_summury.php?date=") + calendar.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + calendar.get(Calendar.YEAR) + "&doctor_id=" + context.getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, "") + "&month=" + month + "&year=" + calendar.get(Calendar.YEAR) + "&day=" + calendar.get(Calendar.DAY_OF_MONTH);
        Volley.newRequestQueue(context).add(new StringRequest(Request.Method.GET, url, response -> {
            Log.d(TAG, "GetBillingSummary: " + url);
            try {
                JSONObject object = new JSONObject(response);
                JSONObject jsonObject = object.getJSONObject("others");
                totalPatientInMonthTV.setText(jsonObject.getString("patients"));
                totalEarningsInMonthTV.setText("৳" + jsonObject.getString("bill"));
                oldPatientsEarningsTV.setText("৳" + jsonObject.getString("old_earnings"));
                oldPatientsTV.setText("Count: " + jsonObject.getString("old"));
                newPatientsEarningsTV.setText("৳" + jsonObject.getString("new_earnings"));
                newPatientsTV.setText("Count: " + jsonObject.getString("new"));
                totalPatientTV.setText(jsonObject.getString("all_time_patients"));
                totalEarningsTV.setText("৳" + jsonObject.getString("all_time_earnings"));

                try {
                    totalEarning = Integer.parseInt(jsonObject.getString("all_time_earnings"));
                    totalWithdraw = Integer.parseInt(jsonObject.getString("withdrawn") + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                totalWithdrawnTV.setText("৳" + jsonObject.getString("withdrawn"));
                totalBillReceivedTV.setText("৳" + jsonObject.getString("total_bill_received"));
                totalDueTV.setText("৳" + (totalEarning - totalWithdraw));

                if (jsonObject.getString("requested").equals("0")) {
                    applyForPayment.setEnabled(true);
                    applyForPayment.setText("Send Payment Query");
                } else {
                    applyForPayment.setEnabled(false);
                    applyForPayment.setText("Processing");
                }

                tillDateTV.setText("Total Summary: " + jsonObject.getString("approved_on"));
                lastUpdateTV.setText("Last Updated " + getTimeFromCalender(Calendar.getInstance()) + " " + dateFromCalender(Calendar.getInstance()));
                lastUpdatedTopTV.setText("Last Updated " + getTimeFromCalender(Calendar.getInstance()) + " " + dateFromCalender(Calendar.getInstance()));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "GetBillingSummary: ", e);
            }
        }, error -> {
            error.printStackTrace();
            Log.e(TAG, "GetBillingSummary: ", error);
        }));
    }

    private void SetTextOnResult(Calendar calendar) {
        monthAndYearTV.setText(getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR));
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
}

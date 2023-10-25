package evereast.co.doctor.RXActivities;

import static evereast.co.doctor.Constants.BASE_URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import evereast.co.doctor.DialogFragment.ChoosePrescriptionTypeFragment;
import evereast.co.doctor.R;

public class ShowFUDialogFragment extends DialogFragment {

    private static final String TAG = "ShowFUDialogFragment";
    ImageView cancelButton;
    TextView followUpDateTV;
    EditText followUpNoteET;
    Button confirmButton;
    SelectPhotoValueListener selectphotovaluelistener;
    int day;
    int month;
    int year;
    DatePickerDialog.OnDateSetListener date;
    String fuDate;
    StringBuilder stringBuilder;
    String appointmentId;
    ChoosePrescriptionTypeFragment fragment;
    CheckBox followUpCheckboxId;
    private Calendar calendar;

    public ShowFUDialogFragment(String appointmentId, ChoosePrescriptionTypeFragment fragment) {
        this.appointmentId = appointmentId;
        this.fragment = fragment;
    }

    public ShowFUDialogFragment(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public ShowFUDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_show_f_u_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(true);
        alert.setCanceledOnTouchOutside(false);


        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        cancelButton = view.findViewById(R.id.cancelButton);
        followUpDateTV = view.findViewById(R.id.followUpDateTV);
        followUpNoteET = view.findViewById(R.id.followUpNoteET);
        confirmButton = view.findViewById(R.id.confirmButton);
        followUpCheckboxId = view.findViewById(R.id.followUpCheckboxId);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = (calendar.get(Calendar.MONTH));
        year = calendar.get(Calendar.YEAR);

        stringBuilder = new StringBuilder();
        stringBuilder.append(day)
                .append("/")
                .append(month + 1)
                .append("/")
                .append(year);
        fuDate = stringBuilder.toString().trim();
        final DatePickerDialog.OnDateSetListener date = (view2, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            calendar.set((Calendar.YEAR), year);
            calendar.set((Calendar.MONTH), monthOfYear);
            calendar.set((Calendar.DAY_OF_MONTH), dayOfMonth);
            view2.setMinDate(System.currentTimeMillis() - 1000);
            updateLabel();
        };

        followUpDateTV.setText("Select Date for Follow up");

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });


        followUpDateTV.setOnClickListener(v -> {
            //TODO: To Separate year month day
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
                    .withLocale(Locale.UK);
            LocalDate date2 = formatter.parseLocalDate(fuDate);
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), date, date2.getYear(), date2.getMonthOfYear() - 1, date2.getDayOfMonth());
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        followUpNoteET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmButton.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmButton.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setTitle("Completing appointment...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Volley.newRequestQueue(view.getContext()).add(new StringRequest(Request.Method.POST, BASE_URL + "set_follow_up.php", response -> {
                progressDialog.dismiss();
                Log.d(TAG, "onCreateDialog: " + response);
                if (!response.equals("Failed to confirming follow up")) {
                    selectphotovaluelistener.Confirm(followUpCheckboxId.isChecked());
                    progressDialog.dismiss();
                    if (fragment != null) {
                        fragment.dismiss();
                    }
                    dismiss();
                } else {
                    progressDialog.dismiss();
                }

                Toast.makeText(view.getContext(), response, Toast.LENGTH_SHORT).show();

            }, error -> {
                progressDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(view.getContext(), "Something went wrong!!! Try again", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("appointment_id", appointmentId);
                    map.put("end_date", fuDate);
                    map.put("follow_up", String.valueOf(followUpCheckboxId.isChecked()));
                    map.put("send_time", System.currentTimeMillis() + "");
                    map.put("follow_up_note", followUpNoteET.getText().toString().trim() + " ");
                    Log.i(TAG, "getParams: " + map);
                    return map;
                }
            });
        });

        followUpCheckboxId.setOnCheckedChangeListener((buttonView, isChecked) -> {
            confirmButton.setEnabled(true);
        });

        return alert;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fuDate = sdf.format(calendar.getTime());
        followUpDateTV.setText("Follow up: " + sdf.format(calendar.getTime()));
        followUpCheckboxId.setChecked(true);
        followUpCheckboxId.setEnabled(true);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            selectphotovaluelistener = (SelectPhotoValueListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement SelectPhotoValueListener");
        }
    }

    public interface SelectPhotoValueListener {
        void Confirm(boolean isFollow);

        void FUDismiss();
    }

}
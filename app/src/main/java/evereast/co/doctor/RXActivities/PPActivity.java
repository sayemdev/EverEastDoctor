package evereast.co.doctor.RXActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import evereast.co.doctor.Constants;
import evereast.co.doctor.DialogFragment.EndSessionConfirmation;
import evereast.co.doctor.DialogFragment.GenderDialogFragment;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_PP;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimation;
import static evereast.co.doctor.Utils.generatePrescriptionId;

public class PPActivity extends AppCompatActivity implements GenderDialogFragment.GenderValueListener, EndSessionConfirmation.ConfirmationValueListener {

    public static final String PATIENT_NAME = "PATIENT_NAME", AGE = "AGE", AGE_TYPE = "AGE_TYPE", DATE = "DATE", GENDER = "GENDER", ID = "ID", ADDRESS = "ADDRESS", PRESCRIPTION_ID = "PRESCRIPTION_ID";
    public static final String INTENT_TYPE = "INTENT_TYPE", APPOINTMENT_ID = "APPOINTMENT_ID";
    public static final String PATIENT_ID = "PATIENT_ID";

    String name, age, ageType, dateString, gender, id, address;
    EditText nameEt, genderEt, dateEt, phoneEt, addressEt;
    Spinner ageSpinner, ageTypeSpinner;
    ArrayAdapter<String> arrayAdapter;
    Calendar calendar;
    int day;
    int month;
    int year;
    StringBuilder stringBuilder;
    ArrayList<String> stringList, ml;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferencesMain;
    TextView pp, cc, ho, ix, dx, rx, ad, fu;
    ImageButton nextButton, previousButton;
    String type;
    String from, appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_p);

        stringList = new ArrayList<>();

        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        nameEt = findViewById(R.id.nameEt);
        ageSpinner = findViewById(R.id.age);
        ageTypeSpinner = findViewById(R.id.typeSp);
        genderEt = findViewById(R.id.genderEt);
        dateEt = findViewById(R.id.dateEt);
        phoneEt = findViewById(R.id.phoneEt);
        addressEt = findViewById(R.id.addressEt);

        pp = findViewById(R.id.pp);
        cc = findViewById(R.id.cc);
        ho = findViewById(R.id.ho);
        ix = findViewById(R.id.ix);
        dx = findViewById(R.id.dx);
        rx = findViewById(R.id.rx);
        ad = findViewById(R.id.ad);
        fu = findViewById(R.id.fu);

        cc.setOnClickListener(v -> {
            GOTONEXT(this, CCActivity.class, nextButton);
//            RedirectToActivityWithAnimation(this, CCActivity.class, nextButton);
        });

        ho.setOnClickListener(v -> {
            GOTONEXT(this, HOActivity.class, nextButton);
//            RedirectToActivityWithAnimation(this, HOActivity.class, nextButton);
        });

        ix.setOnClickListener(v -> {
//            RedirectToActivityWithAnimation(this, IXActivity.class, nextButton);
            GOTONEXT(this, IXActivity.class, nextButton);
        });
        dx.setOnClickListener(v -> {
//            RedirectToActivityWithAnimation(this, DXActivity.class, nextButton);
            GOTONEXT(this, DXActivity.class, nextButton);

        });

        rx.setOnClickListener(v -> {
//            RedirectToActivityWithAnimation(this, RXActivity.class, nextButton);
            GOTONEXT(this, RXActivity.class, nextButton);
        });

        ad.setOnClickListener(v -> {
//            RedirectToActivityWithAnimation(this, ADActivity.class, nextButton);
            GOTONEXT(this, ADActivity.class, nextButton);
        });

        fu.setOnClickListener(v -> {
//            RedirectToActivityWithAnimation(this, FUActivity.class, nextButton);
            GOTONEXT(this, FUActivity.class, nextButton);
        });


        int number = 200;
        stringList.add("Select Age");
        for (int i = 0; i <= number; i++) {
            stringList.add(String.valueOf(i));
        }

        arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, stringList);
        ageSpinner.setAdapter(arrayAdapter);


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

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            calendar.set((Calendar.YEAR), year);
            calendar.set((Calendar.MONTH), monthOfYear);
            calendar.set((Calendar.DAY_OF_MONTH), dayOfMonth);
//            view.setMinDate(System.currentTimeMillis() - 1000);
            updateLabel();
        };
        dateEt.setText(stringBuilder);
        dateEt.setOnClickListener(v -> {
            //TODO: To Separate year month day
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
                    .withLocale(Locale.UK);
            LocalDate date2 = formatter.parseLocalDate(dateEt.getText().toString().trim());
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, date2.getYear(), date2.getMonthOfYear() - 1, date2.getDayOfMonth());
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });


        genderEt.setOnClickListener(v -> {
            GenderDialogFragment genderDialogFragment = new GenderDialogFragment();
            genderDialogFragment.show(getSupportFragmentManager(), genderDialogFragment.getTag());

        });

        from = getIntent().getStringExtra("prescription");
        SharedPreferences sharedPreferences = getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE);
        try {
            Log.d("TAG_IN_TRY", "onCreate: ");
            if (from.equals("appointment")) {
                InItPP(getIntent().getStringExtra(PATIENT_NAME),
                        getIntent().getStringExtra(AGE),
                        getIntent().getStringExtra(AGE_TYPE),
                        getIntent().getStringExtra(DATE),
                        getIntent().getStringExtra(GENDER),
                        generatePrescriptionId(getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))),
                        getIntent().getStringExtra(ADDRESS),
                        getIntent().getStringExtra(APPOINTMENT_ID));
                appointmentId = getIntent().getStringExtra(APPOINTMENT_ID);
            } else if (from.equals("home")) {
                name = sharedPreferences.getString(PATIENT_NAME, "");
                age = sharedPreferences.getString(AGE, "");
                ageType = sharedPreferences.getString(AGE_TYPE, "");
                dateString = sharedPreferences.getString(DATE, "");
                gender = sharedPreferences.getString(GENDER, "");
                id = sharedPreferences.getString(ID, generatePrescriptionId(getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))));
                address = sharedPreferences.getString(ADDRESS, "");
                type = sharedPreferences.getString(INTENT_TYPE, "HOME");
                id = sharedPreferences.getString(ID, generatePrescriptionId(getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))));
                InItPP(name, age, ageType, dateString, gender, id, address, getIntent().getStringExtra(APPOINTMENT_ID));
                appointmentId = "Template";
                SharedPreferences.Editor editor = getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE).edit();
                editor.putString(PATIENT_NAME, name);
                editor.putString(AGE, age);
                editor.putString(AGE_TYPE, ageType);
                editor.putString(DATE, dateString);
                editor.putString(GENDER, gender);
                editor.putString(ID, id);
                editor.putString(ADDRESS, address);
                editor.putString(INTENT_TYPE, from);
                editor.putString(APPOINTMENT_ID, appointmentId);
                editor.apply();
                Log.d("TAG_GOTONEXT", "GOTONEXT: " + appointmentId);
                startActivity(new Intent(this, CCActivity.class));
                finish();
            } else {
                name = sharedPreferences.getString(PATIENT_NAME, "");
                age = sharedPreferences.getString(AGE, "");
                ageType = sharedPreferences.getString(AGE_TYPE, "");
                dateString = sharedPreferences.getString(DATE, "");
                gender = sharedPreferences.getString(GENDER, "");
                id = sharedPreferences.getString(ID, generatePrescriptionId(getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))));
                address = sharedPreferences.getString(ADDRESS, "");
                type = sharedPreferences.getString(INTENT_TYPE, "HOME");
                id = sharedPreferences.getString(ID, generatePrescriptionId(getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))));
                InItPP(name, age, ageType, dateString, gender, id, address, getIntent().getStringExtra(APPOINTMENT_ID));
                appointmentId = sharedPreferences.getString(APPOINTMENT_ID, "Template");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG_STACK", "onCreate: " + dateString);
        }

    }

    private void InItPP(String name, String age, String ageType, String dateString, String gender, String id, String address, String appointmentId) {
        try {
            if (!name.isEmpty()) {
                nameEt.setText(name);
                ageSpinner.setSelection(Integer.parseInt(age) + 1);

                //Age Type Setting
                switch (ageType) {
                    case "Year":
                        ageTypeSpinner.setSelection(1);
                        break;
                    case "Month":
                        ageTypeSpinner.setSelection(2);
                        break;
                    case "Day":
                        ageTypeSpinner.setSelection(3);
                        break;
                }

                dateEt.setText(dateString);

                genderEt.setText(gender);
                phoneEt.setText(id);
                addressEt.setText(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Previous(View view) {

    }

    public void Next(View view) {
        GOTONEXT(this, CCActivity.class, nextButton);
    }

    private void GOTONEXT(Activity activity, Class target, View view) {
        name = nameEt.getText().toString().trim();
        age = ageSpinner.getSelectedItem().toString();
        ageType = ageTypeSpinner.getSelectedItem().toString();
        dateString = dateEt.getText().toString().trim();
        gender = genderEt.getText().toString().trim();
        address = addressEt.getText().toString().trim();
        try {
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            } else if (ageSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Please select an age", Toast.LENGTH_SHORT).show();
            } else if (ageTypeSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Please select an age type", Toast.LENGTH_SHORT).show();
            } else if (dateEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            } else if (genderEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            }else if (addressEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("WHERE?", "GOTONEXT: ");
                SharedPreferences.Editor editor = getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE).edit();
                editor.putString(PATIENT_NAME, name);
                editor.putString(AGE, age);
                editor.putString(AGE_TYPE, ageType);
                editor.putString(DATE, dateString);
                editor.putString(GENDER, gender);
                editor.putString(ID, id);
                editor.putString(ADDRESS, address);
                editor.putString(INTENT_TYPE, from);
                editor.putString(APPOINTMENT_ID, appointmentId);
                editor.apply();
                Log.d("TAG_GOTONEXT", "GOTONEXT: " + appointmentId);
                RedirectToActivityWithAnimation(this, target, view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEt.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void Gender(String gender) {
        genderEt.setText(gender);
    }

    public void EndSession(View view) {
        EndSessionConfirmation endSessionConfirmation = new EndSessionConfirmation();
        endSessionConfirmation.show(getSupportFragmentManager(), endSessionConfirmation.getTag());
    }

    @Override
    public void Confirmation(boolean confirm) {

    }
}
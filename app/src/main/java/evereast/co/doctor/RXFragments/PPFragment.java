package evereast.co.doctor.RXFragments;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_PP;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimation;
import static evereast.co.doctor.Utils.generatePrescriptionId;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import evereast.co.doctor.RXActivities.CCActivity;
import evereast.co.doctor.databinding.FragmentPPBinding;

public class PPFragment extends Fragment implements GenderDialogFragment.GenderValueListener {

    FragmentPPBinding binding;

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
    String type;
    String from, appointmentId;
    View root;

    public PPFragment(String name, String age, String ageType, String gender, String id, String address, String type, String from, String appointmentId) {
        this.name = name;
        this.age = age;
        this.ageType = ageType;
        this.gender = gender;
        this.id = id;
        this.address = address;
        this.type = type;
        this.from = from;
        this.appointmentId = appointmentId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPPBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        stringList = new ArrayList<>();

        int number = 200;
        stringList.add("Select Age");
        for (int i = 0; i <= number; i++) {
            stringList.add(String.valueOf(i));
        }

        nameEt = root.findViewById(R.id.nameEt);
        ageSpinner = root.findViewById(R.id.age);
        ageTypeSpinner = root.findViewById(R.id.typeSp);
        genderEt = root.findViewById(R.id.genderEt);
        dateEt = root.findViewById(R.id.dateEt);
        phoneEt = root.findViewById(R.id.phoneEt);
        addressEt = root.findViewById(R.id.addressEt);

        arrayAdapter = new ArrayAdapter<>(root.getContext(), R.layout.support_simple_spinner_dropdown_item, stringList);
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
            DatePickerDialog datePickerDialog = new DatePickerDialog(root.getContext(), date, date2.getYear(), date2.getMonthOfYear() - 1, date2.getDayOfMonth());
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });


        genderEt.setOnClickListener(v -> {
            GenderDialogFragment genderDialogFragment = new GenderDialogFragment(this);
            genderDialogFragment.show(getActivity().getSupportFragmentManager(), genderDialogFragment.getTag());

        });

        SharedPreferences sharedPreferences = root.getContext().getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE);
        try {
            Log.d("TAG_IN_TRY", "onCreate: ");
            if (from.equals("appointment")) {
                InItPP(name,
                        age,
                       ageType,
                        dateString,
                        gender,
                        generatePrescriptionId(root.getContext().getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))),
                        address,
                        appointmentId);
            } else if (from.equals("home")) {
                name = sharedPreferences.getString(PATIENT_NAME, "");
                age = sharedPreferences.getString(AGE, "");
                ageType = sharedPreferences.getString(AGE_TYPE, "");
                dateString = sharedPreferences.getString(DATE, stringBuilder.toString());
                gender = sharedPreferences.getString(GENDER, "");
                id = sharedPreferences.getString(ID, generatePrescriptionId(root.getContext().getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))));
                address = sharedPreferences.getString(ADDRESS, "");
                type = sharedPreferences.getString(INTENT_TYPE, "HOME");
                id = sharedPreferences.getString(ID, generatePrescriptionId(root.getContext().getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))));
                InItPP(name, age, ageType, dateString, gender, id, address, appointmentId);
                appointmentId = "Template";
                SharedPreferences.Editor editor = root.getContext().getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE).edit();
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
//                startActivity(new Intent(this, CCActivity.class));
            } else {
                name = sharedPreferences.getString(PATIENT_NAME, "");
                age = sharedPreferences.getString(AGE, "");
                ageType = sharedPreferences.getString(AGE_TYPE, "");
                dateString = sharedPreferences.getString(DATE, stringBuilder.toString());
                gender = sharedPreferences.getString(GENDER, "");
                id = sharedPreferences.getString(ID, generatePrescriptionId(root.getContext().getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))));
                address = sharedPreferences.getString(ADDRESS, "");
                type = sharedPreferences.getString(INTENT_TYPE, "HOME");
                id = sharedPreferences.getString(ID, generatePrescriptionId(root.getContext().getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE).getString(Constants.USER_ID, String.valueOf(System.currentTimeMillis()))));
                InItPP(name, age, ageType, dateString, gender, id, address, appointmentId);
                appointmentId = sharedPreferences.getString(APPOINTMENT_ID, "Template");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG_STACK", "onCreate: " + dateString);
        }

        return root;

    }

    public boolean updateValue() {
        name = nameEt.getText().toString().trim();
        age = ageSpinner.getSelectedItem().toString();
        ageType = ageTypeSpinner.getSelectedItem().toString();
        dateString = dateEt.getText().toString().trim();
        gender = genderEt.getText().toString().trim();
        address = addressEt.getText().toString().trim();
        try {
            if (name.isEmpty()) {
                Toast.makeText(root.getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                return false;
            } else if (ageSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(root.getContext(), "Please select an age", Toast.LENGTH_SHORT).show();
                return false;
            } else if (ageTypeSpinner.getSelectedItemPosition() == 0) {
                Toast.makeText(root.getContext(), "Please select an age type", Toast.LENGTH_SHORT).show();
                return false;
            } else if (dateEt.getText().toString().isEmpty()) {
                Toast.makeText(root.getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
                return false;
            } else if (genderEt.getText().toString().isEmpty()) {
                Toast.makeText(root.getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
                return false;
            }else if (addressEt.getText().toString().isEmpty()) {
                Toast.makeText(root.getContext(), "Please enter address", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Log.d("WHERE?", "GOTONEXT: ");
                SharedPreferences.Editor editor = root.getContext().getSharedPreferences(AIR_PREFERENCE_PP, MODE_PRIVATE).edit();
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
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static final String TAG = "PPFragment";
    private void InItPP(String name, String age, String ageType, String dateString, String gender, String id, String address, String appointmentId) {
        try {
            if (!name.isEmpty()) {
               binding.nameEt.setText(name);
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

                binding.dateEt.setText(stringBuilder);

                genderEt.setText(gender);
                phoneEt.setText(id);
                addressEt.setText(address);
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





}
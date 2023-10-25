package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.MAIN_URL;
import static evereast.co.doctor.Constants.MY_TOKEN;
import static evereast.co.doctor.Constants.PATIENT_URL;
import static evereast.co.doctor.Constants.PROFILE_FILES;
import static evereast.co.doctor.RXActivities.PPActivity.ADDRESS;
import static evereast.co.doctor.RXActivities.PPActivity.AGE;
import static evereast.co.doctor.RXActivities.PPActivity.AGE_TYPE;
import static evereast.co.doctor.RXActivities.PPActivity.APPOINTMENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.DATE;
import static evereast.co.doctor.RXActivities.PPActivity.GENDER;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_ID;
import static evereast.co.doctor.RXActivities.PPActivity.PATIENT_NAME;
import static evereast.co.doctor.Utils.Logout;
import static evereast.co.doctor.Utils.closeDrawer;
import static evereast.co.doctor.Utils.openDrawer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.WriterException;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import evereast.co.doctor.Adapter.UploadAdapter;
import evereast.co.doctor.Constants;
import evereast.co.doctor.DialogFragment.ChoosePrescriptionTypeFragment;
import evereast.co.doctor.DialogFragment.RatePatientDialogFragment;
import evereast.co.doctor.DialogFragment.ReportOrConfirmFragment;
import evereast.co.doctor.Fragments.FollowUpAdapter;
import evereast.co.doctor.GetAge;
import evereast.co.doctor.Model.PatientsModel;
import evereast.co.doctor.Model.TemplateModel;
import evereast.co.doctor.Model.UploadModel;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.PPActivity;
import evereast.co.doctor.RXActivities.ShowFUDialogFragment;
import evereast.co.doctor.chat.ChatActivity;
import evereast.co.doctor.databinding.ActivityViewDetailsBinding;

public class ViewDetailsActivity extends AppCompatActivity implements RatePatientDialogFragment.ConfirmationValueListener, ChoosePrescriptionTypeFragment.CreateNewOrReadAirPrescription, ReportOrConfirmFragment.ReportOrConfirmListener,ShowFUDialogFragment.SelectPhotoValueListener {

    private static final String TAG = "VIEW_DETAILS";
    private final String chatUser = "";
    TextView appointmentTimeTextView, patientNameTextView, doctorNameTextView, doctorCategoryTextView, feeTextView, complainTextView;
    CircularImageView dProfileImageView, profileImageView;
    String appointmentId;
    LinearLayout wholeThing;
    DrawerLayout drawerLayout;
    TextView nameTextView, phoneTextView, feeTv;
    ImageView endToggle, closeDrawer;
    ProgressBar progressBar;
    RelativeLayout logoutButton, documentButton, historyButton;
    String userId, userName, userPhone, userProfile, profile_url;
    SharedPreferences sharedPreferences, profilePreferences;
    RecyclerView resourceRecyclerView, postAppointmentRecyclerView;
    List<UploadModel> uploadModelList;
    List<PatientsModel> patientModelList;
    UploadAdapter uploadAdapter;
    FollowUpAdapter followUpAdapter;
    ProgressBar progressBarPostAp;
    boolean isHistory = false;
    String fee;
    Calendar calendar;
    int day;
    int month;
    int year;
    DatePickerDialog.OnDateSetListener date;
    TextView followUpDateTV, appointmentStatusTV, paymentStatusTV;
    String fuDate;
    String patientBirthday, patientAddress, patientGender, doctorId, patientToken;
    Button floatingActionButton;
    int state = 0;
    TextView rateNowTV;
    RatingBar ratingBar;
    ActivityViewDetailsBinding binding;
    AbstractViewRenderer page;
    StringBuilder stringBuilder;

    public static File commonDocumentDirPath(String FolderName) {
        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + FolderName);
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/" + FolderName);
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }
        return dir;
    }

    private void AppBarWork() {
        TextView titleBar = findViewById(R.id.title_app);
        ImageView back = findViewById(R.id.back);
        ImageView endToggle = findViewById(R.id.endToggle);
        titleBar.setText("Appointment Details");
        back.setOnClickListener(v -> {
            finish();
        });
    }
String myToken="";
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarWork();

        uploadModelList = new ArrayList<>();
        patientModelList = new ArrayList<>();

        isHistory = getIntent().getBooleanExtra("history", false);

        profilePreferences = getSharedPreferences(Constants.PROFILE_PREFERENCE, MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(Constants.USER_DATA_PREF, MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.USER_ID, "");
//        userName = sharedPreferences.getString(Constants.USER_NAME, "");
        userPhone = sharedPreferences.getString(Constants.PHONE, "");
        userProfile = profilePreferences.getString(Constants.PROFILE_PICTURE_URI, "");

        floatingActionButton = findViewById(R.id.consultPatient);


        userName = getIntent().getStringExtra("p_name");
        patientAddress = getIntent().getStringExtra("pAddress");
        patientBirthday = getIntent().getStringExtra("pBirthday");
        patientGender = getIntent().getStringExtra("pGender");
        doctorId = getIntent().getStringExtra("Patient_id");

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                myToken=task.getResult();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(doctorId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientToken = snapshot.child(MY_TOKEN).getValue(String.class);
                floatingActionButton.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
                patientToken = "No token";
                floatingActionButton.setEnabled(true);
            }
        });

        profile_url = PROFILE_FILES + userId + ".jpg";

        followUpDateTV = findViewById(R.id.followUpDateTV);
        appointmentTimeTextView = findViewById(R.id.showDate);
        patientNameTextView = findViewById(R.id.patientNameTv);
        doctorNameTextView = findViewById(R.id.doctorNameTv);
        doctorCategoryTextView = findViewById(R.id.doctorCategory);
        feeTextView = findViewById(R.id.feeTv);
        complainTextView = findViewById(R.id.problemBrief);
        dProfileImageView = findViewById(R.id.dProfileImageView);
        wholeThing = findViewById(R.id.wholeThing);
        progressBar = findViewById(R.id.progressBar);
        resourceRecyclerView = findViewById(R.id.resourceList);
        resourceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        resourceRecyclerView.setHasFixedSize(true);
        progressBarPostAp = findViewById(R.id.progressBarId);
        ratingBar = findViewById(R.id.ratingBar);
        rateNowTV = findViewById(R.id.rateNowTV);
        paymentStatusTV = findViewById(R.id.paymentStatusTV);
        appointmentStatusTV = findViewById(R.id.appointmentStatusTV);
        postAppointmentRecyclerView = findViewById(R.id.postAppointmentList);
        postAppointmentRecyclerView.setHasFixedSize(true);
        postAppointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //drawer works
        drawerLayout = findViewById(R.id.drawerLout);
        endToggle = findViewById(R.id.endToggle);
        profileImageView = findViewById(R.id.profileImage);
        nameTextView = findViewById(R.id.nameTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        logoutButton = findViewById(R.id.logoutId);
        closeDrawer = findViewById(R.id.closeDrawer);
        documentButton = findViewById(R.id.documentLtId);
        historyButton = findViewById(R.id.historyLtId);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        nameTextView.setText(userName);
        phoneTextView.setText("+88" + userPhone);
        try {
            Glide.with(this).load(profile_url).placeholder(R.drawable.ic_profile).into(profileImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endToggle.setOnClickListener(v -> {
            openDrawer(drawerLayout);
        });
        closeDrawer.setOnClickListener(v -> closeDrawer(drawerLayout));

        logoutButton.setOnClickListener(v -> Logout(this));

        appointmentId = getIntent().getStringExtra("appointmentId");

        GetAppointmentInfo();

        binding.bookingReceiptButton.setOnClickListener(view -> {
            new PdfDocument.Builder(this).addPage(page).orientation(PdfDocument.A4_MODE.PORTRAIT)
                    .progressMessage(R.string.gen_pdf_file).progressTitle(R.string.gen_please_wait)
                    .renderWidth(2100).renderHeight(2970)
                    .saveDirectory(commonDocumentDirPath("Evereast Offline Receipt"))
                    .filename("Evereast- " + appointmentId)
                    .listener(new PdfDocument.Callback() {
                        @Override
                        public void onComplete(File file) {
                            Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete " + file.getPath());
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
                        }
                    }).create().createPdf(this);
        });

        GetResourcesList(appointmentId);
        GetPostAppointment();

    }

    private void GetAppointmentInfo() {
        String url = BASE_URL + "appointment_info.php?appointment_id=" + appointmentId;
        Log.d(TAG, "GetAppointmentInfo: " + url);
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, url,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    wholeThing.setVisibility(View.VISIBLE);
                    Log.i(TAG, "onCreate: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        appointmentTimeTextView.setText(jsonObject.getString("date") + "," + jsonObject.getString("time"));
                        patientNameTextView.setText("Patient: " + getIntent().getStringExtra("p_name"));
                        doctorNameTextView.setText(jsonObject.getString("d_name"));
                        doctorCategoryTextView.setText(jsonObject.getString("d_category"));
                        Glide.with(this).load(PROFILE_FILES + jsonObject.getString("patient_id") + ".jpg").placeholder(R.drawable.round_health).into(dProfileImageView);
                        complainTextView.setText(jsonObject.getString("patient_problem"));
                        if (jsonObject.getString("after_charge").equals("null")) {
                            feeTextView.setText("Free");
                            fee = "Free";
                        } else {
                            feeTextView.setText("Fee:" + jsonObject.getString("after_charge") + "tk");
                            fee = "Fee:" + jsonObject.getString("after_charge") + "tk";
                        }

                        if (jsonObject.getString("appointment_type").equals("1")) {
                            binding.offline.setVisibility(View.VISIBLE);
                            binding.consultPatient.setText("Issue prescription");
                            binding.consultPatient.setEnabled(true);
                            binding.bookingReceiptButton.setVisibility(View.GONE);
                            binding.consultPatient.setOnClickListener(view -> {
                                ChoosePrescriptionTypeFragment choosePrescriptionTypeFragment = new ChoosePrescriptionTypeFragment(appointmentId);
                                choosePrescriptionTypeFragment.show(getSupportFragmentManager(), choosePrescriptionTypeFragment.getTag());
                            });
                        } else {
                            binding.bookingReceiptButton.setVisibility(View.GONE);
                            binding.consultPatient.setOnClickListener(view -> {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            SendNotification(binding.getRoot(), snapshot.child("MY_TOKEN").getValue(String.class));
                                        } else {
                                            Toast.makeText(ViewDetailsActivity.this, "Patient not available to send alert", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        error.toException().printStackTrace();
                                    }
                                });
                            });
                            binding.offline.setVisibility(View.GONE);
                        }

                        fuDate = jsonObject.getString("end_date");
                        if (!jsonObject.getString("end_date").equals("") &&
                                !jsonObject.getString("end_date").equals("null")) {
                            followUpDateTV.setText("Follow up: " + jsonObject.getString("end_date"));
                        } else {
                            followUpDateTV.setText("Set follow up date");
                        }

                        String rate = jsonObject.getString("rate");

                        ratingBar.setRating(Float.parseFloat(rate));

                        if (jsonObject.getString("doctor_rated").equals("No") && jsonObject.getString("status").equals("Completed")) {
                            rateNowTV.setVisibility(View.VISIBLE);
                        } else {
                            rateNowTV.setVisibility(View.GONE);
                        }

                        if (jsonObject.getString("status").equals("Confirmed") && jsonObject.getString("report_status").equals("Pending")) {
                            ReportOrConfirmFragment reportOrConfirmFragment = new ReportOrConfirmFragment(ViewDetailsActivity.this);
                            reportOrConfirmFragment.show(getSupportFragmentManager(), reportOrConfirmFragment.getTag());
                            floatingActionButton.setEnabled(true);
                            floatingActionButton.setText("Consult with Patient");
                        } else if (jsonObject.getString("status").equals("Completed") || jsonObject.getString("status").equals("Pending")) {
                            floatingActionButton.setEnabled(false);
                            floatingActionButton.setText("Appointment Completed");
                        }

                        appointmentStatusTV.setText(jsonObject.getString("status"));
                        paymentStatusTV.setText(jsonObject.getString("payment_status"));
                        if (jsonObject.getString("payment_status").equals("Paid")) {
                            paymentStatusTV.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_green)));
                        }
                        if (jsonObject.getString("status").equals("Confirmed")) {
                            appointmentStatusTV.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_green)));
                        } else if (jsonObject.getString("status").equals("Completed")) {
                            appointmentStatusTV.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        }
                        rateNowTV.setOnClickListener(v -> {
                            RatePatientDialogFragment ratePatientDialogFragment = new RatePatientDialogFragment(this, appointmentId, doctorId, userId, userProfile, rate, this::Confirmation);
                            ratePatientDialogFragment.show(getSupportFragmentManager(), ratePatientDialogFragment.getTag());
                        });

                        if (jsonObject.getString("appointment_type").equals("1")) {
                            binding.offline.setVisibility(View.VISIBLE);
                            appointmentTimeTextView.setText(jsonObject.getString("date"));
                        } else {
                            binding.offline.setVisibility(View.GONE);
                            appointmentTimeTextView.setText(jsonObject.getString("date") + "," + jsonObject.getString("time"));
                        }

                        if (jsonObject.getString("appointment_type").equals("1")) {
                            binding.bookingReceiptButton.setVisibility(View.GONE);
                            page = new AbstractViewRenderer(this, R.layout.fragment_demo_invoice) {
                                @Override
                                protected void initView(View view) {
                                    ImageView hospitalQrView = view.findViewById(R.id.hospitalQR);
                                    ImageView patientQrView = view.findViewById(R.id.patientQrCode);
                                    QRGEncoder hospitalQrgEncoder;


                                    TextView patientNameTV, doctorNameTV, departmentTV, paidOnTV, hospitalTV, ageTV, sexTV, apDateTV, locationTV, amountTV, modeOfPaymentTV, appointmentTypeTV;
                                    TextView patientNameTVP, doctorNameTVP, departmentTVP, paidOnTVP, hospitalTVP, ageTVP, sexTVP, apDateTVP, locationTVP, amountTVP, modeOfPaymentTVP, appointmentTypeTVP;

                                    patientNameTV = view.findViewById(R.id.patientNameTv);
                                    doctorNameTV = view.findViewById(R.id.doctorNameTv);
                                    departmentTV = view.findViewById(R.id.departmentTV);
                                    paidOnTV = view.findViewById(R.id.paidOnTV);
                                    hospitalTV = view.findViewById(R.id.hospitalNameTV);
                                    ageTV = view.findViewById(R.id.ageTV);
                                    sexTV = view.findViewById(R.id.sexTV);
                                    apDateTV = view.findViewById(R.id.apDateTV);
                                    locationTV = view.findViewById(R.id.locationTV);
                                    amountTV = view.findViewById(R.id.amountTV);
                                    modeOfPaymentTV = view.findViewById(R.id.modeOfPaymentTV);
                                    appointmentTypeTV = view.findViewById(R.id.appointmentTypeTV);
                                    patientNameTVP = view.findViewById(R.id.patientNameTvP);
                                    doctorNameTVP = view.findViewById(R.id.doctorNameTvP);
                                    departmentTVP = view.findViewById(R.id.departmentTVP);
                                    paidOnTVP = view.findViewById(R.id.paidOnTVP);
                                    hospitalTVP = view.findViewById(R.id.hospitalNameTVP);
                                    ageTVP = view.findViewById(R.id.ageTVP);
                                    sexTVP = view.findViewById(R.id.sexTVP);
                                    apDateTVP = view.findViewById(R.id.apDateTVP);
                                    locationTVP = view.findViewById(R.id.locationTVP);
                                    amountTVP = view.findViewById(R.id.amountTVP);
                                    modeOfPaymentTVP = view.findViewById(R.id.modeOfPaymentTVP);
                                    appointmentTypeTVP = view.findViewById(R.id.appointmentTypeTVP);

                                    try {
                                        String[] strings = jsonObject.getString("payment_date").split(" ");
                                        StringBuilder date = new StringBuilder();
                                        for (int i = 0; i < strings.length; i++) {
                                            if (i == 1) {
                                                date.append(" [").append(strings[1]).append("]");
                                            } else {
                                                date.append(" ").append(strings[i]);
                                            }
                                        }

                                        patientNameTV.setText("Patient Name: " + jsonObject.getString("patient_name"));
                                        patientNameTVP.setText("Patient Name: " + jsonObject.getString("patient_name"));
                                        doctorNameTV.setText("Doctor Name: " + jsonObject.getString("d_name"));
                                        doctorNameTVP.setText("Doctor Name: " + jsonObject.getString("d_name"));
                                        departmentTV.setText("Department: " + jsonObject.getString("d_category"));
                                        departmentTVP.setText("Department: " + jsonObject.getString("d_category"));
                                        paidOnTV.setText("Paid On: " + date);
                                        paidOnTVP.setText("Paid On: " + date);
                                        hospitalTV.setText("Hospital: " + jsonObject.getString("offline_work_place"));
                                        hospitalTVP.setText("Hospital: " + jsonObject.getString("offline_work_place"));

                                        try {
                                            ageTV.setText("Age: " + GetAge.getAge(jsonObject.getString("patient_age")) + "");
                                            ageTVP.setText("Age: " + GetAge.getAge(jsonObject.getString("patient_age")) + "");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            ageTV.setText("Age: " + 0 + "");
                                            ageTVP.setText("Age: " + 0 + "");
                                        }

                                        sexTV.setText("Sex: " + jsonObject.getString("patient_gender"));
                                        sexTVP.setText("Sex: " + jsonObject.getString("patient_gender"));
                                        apDateTV.setText("Ap. Date: " + jsonObject.getString("date"));
                                        apDateTVP.setText("Ap. Date: " + jsonObject.getString("date"));
                                        locationTV.setText("Location: " + jsonObject.getString("offline_location"));
                                        locationTVP.setText("Location: " + jsonObject.getString("offline_location"));
                                        amountTV.setText("Amount: " + jsonObject.getString("after_charge"));
                                        amountTVP.setText("Amount: " + jsonObject.getString("after_discount"));
                                        modeOfPaymentTV.setText("Mode of payment: " + jsonObject.getString("payment_method"));
                                        modeOfPaymentTVP.setText("Mode of payment: " + jsonObject.getString("payment_method"));
                                        appointmentTypeTV.setText("Appointment Type: " + jsonObject.getString("type"));
                                        appointmentTypeTVP.setText("Appointment Type: " + jsonObject.getString("type"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

                                    Display display = manager.getDefaultDisplay();
                                    Point point = new Point();
                                    display.getSize(point);
                                    int width = point.x;
                                    int height = point.y;
                                    int dimen = Math.min(width, height);
                                    dimen = dimen * 3 / 4;
                                    hospitalQrgEncoder = new QRGEncoder("Receipt number: 454544", null, QRGContents.Type.TEXT, dimen);
                                    try {
                                        Bitmap bitmap = hospitalQrgEncoder.encodeAsBitmap();
                                        hospitalQrView.setImageBitmap(bitmap);
                                        patientQrView.setImageBitmap(bitmap);
                                    } catch (WriterException e) {
                                        Log.e("Tag", e.toString());
                                    }
                                }
                            };
                        } else {
                            binding.bookingReceiptButton.setVisibility(View.GONE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            progressBar.setVisibility(View.GONE);
            error.printStackTrace();
            Toast.makeText(this, "Poor internet connection", Toast.LENGTH_SHORT).show();
        }));

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = (calendar.get(Calendar.MONTH));
        year = calendar.get(Calendar.YEAR);

        date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            calendar.set((Calendar.YEAR), year);
            calendar.set((Calendar.MONTH), monthOfYear);
            calendar.set((Calendar.DAY_OF_MONTH), dayOfMonth);
            view.setMinDate(System.currentTimeMillis() - 1000);
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle("Are you sure?");
            alertDialogBuilder.setMessage("This date will be set as follow up end date in patient");
            alertDialogBuilder.setPositiveButton("Confirm", (dialog, which) -> {

                updateLabel();
            });

            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {

            });
            alertDialogBuilder.create();
            alertDialogBuilder.show();
        };

    }

    private void updateLabel() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Confirming follow up...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fuDate = sdf.format(calendar.getTime());

        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, BASE_URL + "set_follow_up.php", response -> {
            progressDialog.dismiss();
            if (!response.equals("Failed to confirming follow up")) {
                followUpDateTV.setText("Follow up: " + sdf.format(calendar.getTime()));
            }
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        }, error -> {
            progressDialog.dismiss();
            error.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("appointment_id", appointmentId);
                map.put("end_date", sdf.format(calendar.getTime()));
                Log.i(TAG, "getParams: " + map);
                return map;
            }
        });

    }

    private void GetPostAppointment() {
        String url = BASE_URL + "follow_ups.php?followup_for=" + appointmentId;
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "GetPostAppointment: " + url);
                    progressBarPostAp.setVisibility(View.GONE);
                    patientModelList.clear();
                    try {
                        postAppointmentRecyclerView.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("appointment_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            PatientsModel patientsModel = new PatientsModel();
                            patientsModel.setAppointmentPosition(String.valueOf(jsonArray.length() - i));
                            patientsModel.setPatientName(getIntent().getStringExtra("p_name"));
                            patientsModel.setPatientAddress(patientAddress);
                            patientsModel.setPatientGender(patientGender);
                            patientsModel.setPatientId(doctorId);
                            patientsModel.setDoctorName(object.getString("d_name"));
                            patientsModel.setAppointmentId(object.getString("appointment_id"));
                            patientsModel.setDoctorId(object.getString("doctor_id"));
                            patientsModel.setPatientBirthDay(patientBirthday + "");
                            patientsModel.setDate(object.getString("date"));
                            patientsModel.setTime(object.getString("time"));
                            patientsModel.setDoctorCategory(object.getString("d_designation"));
                            patientsModel.setDoctorProfile(object.getString("d_image"));
                            patientsModel.setFeeAfterDiscount(object.getString("after_discount"));
                            patientsModel.setAppointmentStatus(object.getString("status"));
                            patientsModel.setPaymentStatus(object.getString("payment_status"));
                            patientsModel.setRated(object.getString("doctor_rated"));
                            patientsModel.setRating(object.getString("rate"));
                            patientModelList.add(patientsModel);
                        }
                        followUpAdapter = new FollowUpAdapter(this, patientModelList);
                        postAppointmentRecyclerView.setAdapter(followUpAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            progressBarPostAp.setVisibility(View.GONE);
            error.printStackTrace();
        }));
    }

    private void GetResourcesList(String appointmentId) {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, PATIENT_URL + "get_images.php?appointment_id=" + appointmentId,
                response -> {
                    try {
                        uploadModelList.clear();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("images");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            UploadModel uploadModel = new UploadModel();
                            uploadModel.setImageUrl(jsonObject1.getString("image_name"));
                            uploadModel.setTitle(jsonObject1.getString("title"));
                            uploadModelList.add(uploadModel);
                        }
                        uploadAdapter = new UploadAdapter(this, uploadModelList, response);
                        resourceRecyclerView.setAdapter(uploadAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }));
    }

    public void SetAppointment(View view) {
    }

/*    public void ConsultWithPatient(View view) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SendNotification(view, snapshot.child("MY_TOKEN").getValue(String.class));
                } else {
                    Toast.makeText(ViewDetailsActivity.this, "Patient not available to send alert", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }*/

    public void SetFollow(View view) {
        //TODO: To Separate year month day
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
                .withLocale(Locale.UK);
        LocalDate date2;
        if (!fuDate.equals("") &&
                !fuDate.equals("null") &&
                !fuDate.equals(null)) {
            date2 = formatter.parseLocalDate(fuDate);
        } else {
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            date2 = formatter.parseLocalDate(sdf.format(calendar.getTime()));
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, date2.getYear(), date2.getMonthOfYear() - 1, date2.getDayOfMonth());
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void SendNotification(View view, String patientToken) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        alertDialogBuilder.setCancelable(false)
                .setTitle("Alert patient")
                .setMessage("This patient not available in this time. Are you sure to alert patient?")
                .setNegativeButton("No", (dialog, which) -> {
//                    view.setEnabled(true);
                    Intent intent = new Intent(ViewDetailsActivity.this, ChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("senderId", doctorId);
                    intent.putExtra("sender_name", userName);
                    intent.putExtra("sender_token", patientToken);
                    intent.putExtra("appointment_id", appointmentId);
                    startActivity(intent);
                }).setPositiveButton("Yes", (dialog, which) -> {
//            view.setEnabled(false);

                    Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, MAIN_URL + "chatfcm.php",
                            response -> {
                                Log.i(TAG, "onDataChange: " + response);
                                Log.i(TAG, "onDataChange apb: " + MAIN_URL + "chatfcm.php");
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("appointments").child(appointmentId);

                                Map<String, Object> map = new HashMap();
                                map.put("appointment_id", appointmentId);
                                map.put("chat", "Running");
                                map.put("sender_name", userName);
                                map.put("senderId", userId);
                                map.put("senderToken",myToken+"");
                                map.put("prescription", "Pending");
                                databaseReference.setValue(map);

                                Intent intent = new Intent(ViewDetailsActivity.this, ChatActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("senderId", doctorId);
                                intent.putExtra("sender_name", userName);
                                intent.putExtra("sender_token", patientToken);
                                intent.putExtra("appointment_id", appointmentId);
                                startActivity(intent);
                            },
                            error -> {
                                Log.d(TAG, "SendNotification: " + error.getMessage());
                                error.printStackTrace();
                            }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> maps = new HashMap<>();
                            maps.put("token", patientToken);
                            maps.put("d_name", "Please open Evereast Patient application");
                            maps.put("text", doctorNameTextView.getText().toString() + " is ready to give you consultation");
                            maps.put("sender_token", myToken+"");
                            maps.put("sender", userId);
                            maps.put("sender_name", userName);
                            maps.put("appointment_id", appointmentId);
                            maps.put("notiType", "chat");
                            maps.put("send_time", System.currentTimeMillis() + "");
                            maps.put("body", "Doctor sent you a message");
                            maps.put("title", doctorNameTextView.getText().toString().trim());
                            maps.put("intent", "chat");
                            maps.put("d_or_p", "patient");
                            maps.put("senderN", userId);
                            maps.put("reciever", doctorId);
                            Log.d(TAG, "getParams: " + maps);
                            return maps;
                        }
                    });
                }).create();
        alertDialogBuilder.show();
    }

    @Override
    public void Confirmation(boolean confirm) {
        GetAppointmentInfo();
        GetPostAppointment();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void ConfirmAp() {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, BASE_URL + "report_ap.php", response -> {
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        }, Throwable::printStackTrace) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("appointment_id", appointmentId);
                map.put("report_status", "Confirmed");
                return map;
            }
        });
    }

    @Override
    public void ReportAp(String s) {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, BASE_URL + "report_ap.php", response -> {
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        }, Throwable::printStackTrace) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("report", s);
                map.put("appointment_id", appointmentId);
                map.put("report_status", "Reported");
                return map;
            }
        });
    }

    @Override
    public void createNew() {
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

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting information for appointment...");
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, PATIENT_URL + "get_patient_info.php?patient_id=" + chatUser + "&ap_id=" + appointmentId,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Intent intent = new Intent(this, PPActivity.class);
                        intent.putExtra("appointmentId", appointmentId);
                        intent.putExtra("token", patientToken);
                        intent.putExtra(PATIENT_NAME, jsonObject.getString("p_name"));
                        intent.putExtra(ADDRESS, jsonObject.getString("p_address"));
                        intent.putExtra(GENDER, jsonObject.getString("p_gender"));
                        try {
                            String age = GetAge.getAge(jsonObject.getString("p_bdate"));
                            intent.putExtra(AGE, age);
                            Log.d(TAG, "PushToRX: " + age);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            intent.putExtra(AGE, "0");
                        }
                        intent.putExtra(AGE_TYPE, "Year");
                        intent.putExtra(DATE, stringBuilder.toString());
                        intent.putExtra("prescription", "appointment");
                        intent.putExtra(APPOINTMENT_ID, appointmentId);
                        intent.putExtra(PATIENT_ID, chatUser);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "You can't access Rx for this time try again later", Toast.LENGTH_SHORT).show();
                    }
                }, Throwable::printStackTrace));
    }

    @Override
    public void showFU(String appointmentId) {
        ShowFUDialogFragment showFUDialogFragment = new ShowFUDialogFragment(appointmentId);
        showFUDialogFragment.show(getSupportFragmentManager(), showFUDialogFragment.getTag());
    }

    @Override
    public void older(int position, TemplateModel templateModel) {
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

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting information for appointment...");
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, PATIENT_URL + "get_patient_info.php?patient_id=" + chatUser + "&ap_id=" + appointmentId,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String chatUserProfilePicture = jsonObject.getString("patient_id") + ".jpg";
                        Intent intent = new Intent(this, GiveFromTemplateActivity.class);
                        intent.putExtra("appointmentId", appointmentId);
                        intent.putExtra("token", patientToken);
                        intent.putExtra("rating", jsonObject.getString("rate"));
                        intent.putExtra(PATIENT_NAME, jsonObject.getString("p_name"));
                        intent.putExtra("PATIENT_PROFILE", chatUserProfilePicture);
                        intent.putExtra(ADDRESS, jsonObject.getString("p_address"));
                        intent.putExtra(GENDER, jsonObject.getString("p_gender"));
                        try {
                            intent.putExtra(AGE, GetAge.getAge(jsonObject.getString("p_bdate")));
                            Log.d(TAG, "PushToRX: " + GetAge.getAge(jsonObject.getString("p_bdate")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            intent.putExtra(AGE, "0");
                        }
                        intent.putExtra(AGE_TYPE, "Year");
                        intent.putExtra(DATE, stringBuilder.toString());
                        intent.putExtra("prescription", "appointment");
                        intent.putExtra(APPOINTMENT_ID, appointmentId);
                        intent.putExtra(PATIENT_ID, chatUser);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("full", templateModel.getFullJson());
                        intent.putExtra("doctorInfo", templateModel.getDoctorInfo());
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "You can't access Rx for this time try again later", Toast.LENGTH_SHORT).show();
                    }
                }, Throwable::printStackTrace));

    }

    @Override
    public void Confirm(boolean isFollow) {

    }

    @Override
    public void FUDismiss() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
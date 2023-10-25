package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.HAS_OPEN;
import static evereast.co.doctor.Constants.LANGUAGE_PREFERENCES;
import static evereast.co.doctor.Constants.USER_DATA_PREF;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.Adapter.AccountsAdapter;
import evereast.co.doctor.Adapter.SliderAdapter;
import evereast.co.doctor.Adapter.SliderAdapterBn;
import evereast.co.doctor.Model.AccountsModel;
import evereast.co.doctor.R;
import evereast.co.doctor.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RANDOM";
    SwitchCompat switchCompat;
    ViewPager slideViewPager;
    LinearLayout dotLayout;
    TextView nextButton, previousButton, getStartedButton;
    int currentPage;
    SliderAdapter sliderAdapter;
    SliderAdapterBn sliderAdapterBn;
    TextView[] dots;
    LinearLayout workWithUs, login;

    SharedPreferences.Editor editor;
    String language;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        language = getSharedPreferences(LANGUAGE_PREFERENCES, MODE_PRIVATE).getString(LANGUAGE_PREFERENCES, "b");
        editor = getSharedPreferences(LANGUAGE_PREFERENCES, MODE_PRIVATE).edit();

        switchCompat = findViewById(R.id.switchCompat);
        slideViewPager = findViewById(R.id.slideViewPager);
        dotLayout = findViewById(R.id.dots);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        getStartedButton = findViewById(R.id.getStartedButton);
        sliderAdapter = new SliderAdapter(this);
        sliderAdapterBn = new SliderAdapterBn(this);

        nextButton.setOnClickListener(v -> slideViewPager.setCurrentItem(currentPage + 1));
        previousButton.setOnClickListener(v -> slideViewPager.setCurrentItem(currentPage - 1));
        getStartedButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).edit();
            editor.putBoolean(HAS_OPEN, true);
            editor.apply();
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    MainActivity.this, R.style.BottomSheetDialogTheme
            );
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_bottom_sheet,
                    findViewById(R.id.bottom_sheet_container));

            progressBar = view.findViewById(R.id.progressBarId);
            RecyclerView recyclerView = view.findViewById(R.id.userAccountsList);
            List<AccountsModel> accountsModelList = new ArrayList<>();

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            Volley.newRequestQueue(this).add(new StringRequest(BASE_URL + "users_list.php?device_id=" + Utils.getPsuedoUniqueID(), response -> {
                try {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onCreate: " + response);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AccountsModel accountsModel = new AccountsModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        accountsModel.setDeviceId(object.getString("device_id"));
                        accountsModel.setPhone(object.getString("d_phone"));
                        accountsModel.setUserName(object.getString("d_name"));
                        accountsModel.setPatientId(object.getString("patient_id"));
                        accountsModel.setTempID(object.getString("temp_id"));
                        accountsModelList.add(accountsModel);
                    }

                    recyclerView.setAdapter(new AccountsAdapter(accountsModelList, this));

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }, error -> {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                error.printStackTrace();
            }));

            progressBar.setVisibility(View.VISIBLE);


            workWithUs = view.findViewById(R.id.work_with_us);
            login = view.findViewById(R.id.logInYourAccount);

            workWithUs.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();
                Utils.redirectToVerification(this, "Apply");
            });
            login.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();
//                Utils.redirectToVerification(this, "Login");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });

            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        });

        /*if (language.equals("e")) {
            slideViewPager.setAdapter(sliderAdapter);
            switchCompat.setChecked(true);
            nextButton.setText(getString(R.string.next));
            previousButton.setText(getString(R.string.previous));
            getStartedButton.setText(getString(R.string.get_started));
        } else {
            slideViewPager.setAdapter(sliderAdapterBn);
            nextButton.setText(getString(R.string.next_bn));
            previousButton.setText(getString(R.string.previous_bn));
            getStartedButton.setText(getString(R.string.get_started_bn));
        }

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchCompat.isChecked()) {
                nextButton.setText(getString(R.string.next));
                previousButton.setText(getString(R.string.previous));
                getStartedButton.setText(getString(R.string.get_started));
                editor.clear();
                editor.putString(LANGUAGE_PREFERENCES, "e");
                slideViewPager.setCurrentItem(0);
                slideViewPager.setAdapter(sliderAdapter);
            } else {
                nextButton.setText(getString(R.string.next_bn));
                previousButton.setText(getString(R.string.previous_bn));
                getStartedButton.setText(getString(R.string.get_started_bn));
                editor.clear();
                editor.putString(LANGUAGE_PREFERENCES, "b");
                slideViewPager.setCurrentItem(0);
                slideViewPager.setAdapter(sliderAdapterBn);

            }
            editor.apply();
        });
        */
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void addDotIndicator(int position) {
        dots = new TextView[3];
        dotLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transparent));
            dotLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.app_bar_color));
        }

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
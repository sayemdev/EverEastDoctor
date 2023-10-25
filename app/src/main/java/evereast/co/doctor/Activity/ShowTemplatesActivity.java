package evereast.co.doctor.Activity;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.Adapter.TemplatesAdapter;
import evereast.co.doctor.Model.TemplateModel;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.PPActivity;
import evereast.co.doctor.databinding.ActivityShowTemplatesBinding;

public class
ShowTemplatesActivity extends AppCompatActivity implements TemplatesAdapter.ClickListener {
    private static final String TAG = "ShowTemplatesActivity";
    RecyclerView recyclerView;
    List<TemplateModel> templateModelList;
    ActivityShowTemplatesBinding binding;

    private void AppBarWork() {
        TextView titleBar = findViewById(R.id.title_app);
        ImageView back = findViewById(R.id.back);
        titleBar.setText("Templates");
        back.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_templates);
        binding = ActivityShowTemplatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarWork();

        templateModelList = new ArrayList<>();

        recyclerView = findViewById(R.id.templatesList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        String url = BASE_URL + "get_prescription_info.php?doctor_id=" + getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "");
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "onCreate: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            TemplateModel templateModel = new TemplateModel();
                            templateModel.setDoctorName(object.getString("doctor_name"));
                            templateModel.setDoctorInfo(object.getString("doctor_info"));
                            templateModel.setIssueDate(object.getString("issue_date"));
                            templateModel.setPrescId(object.getString("presc_id"));
                            templateModel.setCc(object.getString("cc"));
                            templateModel.setHo(object.getString("ho"));
                            templateModel.setIx(object.getString("ix"));
                            templateModel.setDx(object.getString("dx"));
                            templateModel.setRx(object.getString("rx"));
                            templateModel.setAd(object.getString("ad"));
                            templateModel.setTitle(object.getString("title"));
                            templateModel.setDescription(object.getString("description"));
                            templateModel.setFullJson(object.toString());
                            templateModelList.add(templateModel);
                        }
                        if (templateModelList.size() == 0) {
                            binding.templatesList.setVisibility(View.GONE);
                            binding.progressBarId.setVisibility(View.GONE);
                            binding.templateNotFound.setVisibility(View.VISIBLE);
                        } else {
                            binding.templatesList.setVisibility(View.VISIBLE);
                            binding.progressBarId.setVisibility(View.GONE);
                            binding.templateNotFound.setVisibility(View.GONE);
                        }
                        recyclerView.setAdapter(new TemplatesAdapter(templateModelList, this, this, false));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.templatesList.setVisibility(View.GONE);
                        binding.progressBarId.setVisibility(View.GONE);
                        binding.templateNotFound.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            error.printStackTrace();
            binding.templatesList.setVisibility(View.GONE);
            binding.progressBarId.setVisibility(View.GONE);
            binding.templateNotFound.setVisibility(View.VISIBLE);
        }));

    }

    public void CreateNew(View view) {
        Intent intent = new Intent(this, PPActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("prescription", "home");
        startActivity(intent);
    }

    @Override
    public void OnClick(int position, TemplateModel templateModel) {

    }

    @Override
    public void OnDeleted(List<TemplateModel> templateModelList) {
        if (templateModelList == null) {
            binding.templateNotFound.setVisibility(View.VISIBLE);
        }
    }
}
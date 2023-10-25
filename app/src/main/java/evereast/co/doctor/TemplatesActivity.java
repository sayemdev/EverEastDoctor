package evereast.co.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.Model.TemplatesModel;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.PATIENT_URL;

public class TemplatesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<TemplatesModel>templatesModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        templatesModelList=new ArrayList<>();

        recyclerView=findViewById(R.id.templatesList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GetPrescription();

    }

    private void GetPrescription() {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, PATIENT_URL + "get_prescription_info.php?appointment_id=",
                response -> {
                    Log.d("TAG", "GetPrescription: "+response+"url"+PATIENT_URL + "get_prescription_info.php?appointment_id=");
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject object = jsonArray.getJSONObject(0);
                        TemplatesModel templatesModel=new TemplatesModel();
                        templatesModel.setEntryDate(object.getString("issue_date"));
                        templatesModel.setCc(object.getString("cc"));
                        templatesModel.setIx(object.getString("ix"));
                        templatesModel.setRx(object.getString("rx"));
                        templatesModel.setDx(object.getString("dx"));
                        templatesModel.setAd(object.getString("ad"));
                        templatesModel.setHo(object.getString("presc_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }));
    }
}
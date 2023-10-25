package evereast.co.doctor.DialogFragment;

import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.Adapter.TemplatesAdapter;
import evereast.co.doctor.Model.TemplateModel;
import evereast.co.doctor.R;

public class ChoosePrescriptionTypeFragment extends DialogFragment implements TemplatesAdapter.ClickListener {

    Button templates, createNew, completeAppointmentButton;
    CreateNewOrReadAirPrescription createNewOrReadAirPrescription;
    RecyclerView recyclerView;
    List<TemplateModel> templateModelList;
    String appointmentId;
    ImageView close;

    public ChoosePrescriptionTypeFragment(String appointment) {
        // Required empty public constructor
        this.appointmentId = appointment;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_choose_prescription_type, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(true);
        alert.setCanceledOnTouchOutside(false);

        templateModelList = new ArrayList<>();

        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        templates = view.findViewById(R.id.templates);
        createNew = view.findViewById(R.id.createNew);
        completeAppointmentButton = view.findViewById(R.id.completeAppointment);
        close = view.findViewById(R.id.close);
        close.setOnClickListener(view1 -> {
            dismiss();
        });
        recyclerView = view.findViewById(R.id.templatesList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Volley.newRequestQueue(view.getContext()).add(new StringRequest(Request.Method.POST, BASE_URL + "get_prescription_info.php?doctor_id=" + view.getContext().getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE).getString(USER_ID, ""),
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            TemplateModel templateModel = new TemplateModel();
                            templateModel.setDoctorName(object.getString("doctor_name"));
                            templateModel.setDoctorInfo(object.getString("doctor_info"));
                            templateModel.setIssueDate(object.getString("issue_date"));
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
//                            Toast.makeText(view.getContext(), "No template created yet. Created new one", Toast.LENGTH_SHORT).show();
                            /*createNewOrReadAirPrescription.createNew();
                            dismiss();*/
                            completeAppointmentButton.setVisibility(View.VISIBLE);
                        } else {
                            completeAppointmentButton.setVisibility(View.GONE);
                        }

                        recyclerView.setAdapter(new TemplatesAdapter(templateModelList, view.getContext(), this, true));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        }));
        templates.setOnClickListener(v -> {
            templates.setVisibility(View.GONE);
            createNew.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            /*   PPInputOnclickListener.older();
            dismiss();*/
        });

        completeAppointmentButton.setOnClickListener(v -> {
            createNewOrReadAirPrescription.showFU(appointmentId);
            dismiss();
        });

        createNew.setOnClickListener(v -> {
            createNewOrReadAirPrescription.createNew();
            dismiss();
        });

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            createNewOrReadAirPrescription = (CreateNewOrReadAirPrescription) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement PPInputOnclickListener");
        }
    }

    @Override
    public void OnClick(int position, TemplateModel templateModel) {
        createNewOrReadAirPrescription.older(position, templateModel);
        dismiss();
    }

    @Override
    public void OnDeleted(List<TemplateModel> templateModelList) {

    }

    public interface CreateNewOrReadAirPrescription {
        void createNew();

        void showFU(String appointmentId);

        void older(int position, TemplateModel templateModel);
    }
}
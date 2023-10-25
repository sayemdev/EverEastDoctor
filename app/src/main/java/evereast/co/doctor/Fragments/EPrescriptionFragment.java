package evereast.co.doctor.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import evereast.co.doctor.Drug.DrugListActivity;
import evereast.co.doctor.R;
import evereast.co.doctor.RXActivities.PPActivity;

public class EPrescriptionFragment extends Fragment {

    public EPrescriptionFragment() {
        // Required empty public constructor
    }

    Button templatesButton;
    Button createNewButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_e_prescription, container, false);

        templatesButton=view.findViewById(R.id.templates);
        createNewButton=view.findViewById(R.id.createNew);

        createNewButton.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), PPActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        templatesButton.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), DrugListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }


}
package evereast.co.doctor.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import evereast.co.doctor.R;


/**
 * Health Men created by Sayem Hossen Saimon on 12/12/2020 at 5:13 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class SelectPhoto extends AppCompatDialogFragment {
    LinearLayout cameraLayout, galleryLayout;
    private SelectPhotoValueListener selectphotovaluelistener;

    public SelectPhoto() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.gallery_or_camera, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(false);

        cameraLayout = view.findViewById(R.id.camera);
        galleryLayout = view.findViewById(R.id.gallery);

        AlertDialog alert = builder.create();
        setCancelable(true);
        alert.setCanceledOnTouchOutside(false);
        cameraLayout.setOnClickListener(v -> {
            selectphotovaluelistener.Camera();
            dismiss();
        });

        galleryLayout.setOnClickListener(v -> {
            selectphotovaluelistener.Gallery();
            dismiss();
        });

        return alert;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            selectphotovaluelistener = (SelectPhotoValueListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SelectPhotoValueListener");
        }
    }

    public interface SelectPhotoValueListener {
        void Camera();

        void Gallery();
    }
}


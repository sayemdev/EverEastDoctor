package evereast.co.doctor.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

import evereast.co.doctor.R;

/**
 * Health Men created by Sayem Hossen Saimon on 12/11/2020 at 4:31 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class NotificationFragment extends AppCompatDialogFragment {

    ImageView cancelButton, notiImageView;
    String title, body, image, link;
    TextView titleTV, bodyTV;
    Button viewInWeb;

    public NotificationFragment() {
        // Required empty public constructor

    }

    public NotificationFragment(String title, String body, String image, String link) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.link = link;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_notification_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alert = builder.create();
        setCancelable(true);
        alert.setCanceledOnTouchOutside(false);

        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        titleTV = view.findViewById(R.id.notificationTitleTV);
        bodyTV = view.findViewById(R.id.notificationBodyTV);
        cancelButton = view.findViewById(R.id.cancel_button);
        notiImageView = view.findViewById(R.id.notificationImageView);

        cancelButton.setOnClickListener(v -> dismiss());
        titleTV.setText(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bodyTV.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_COMPACT));
        } else {
            bodyTV.setText(Html.fromHtml(body));
        }

        bodyTV.setMovementMethod(LinkMovementMethod.getInstance());

        Glide.with(view.getContext()).load(image).into(notiImageView);

        viewInWeb=view.findViewById(R.id.viewInWeb);
        viewInWeb.setOnClickListener(v -> {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        return alert;
    }

}

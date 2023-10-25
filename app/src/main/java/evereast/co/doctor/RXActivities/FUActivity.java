package evereast.co.doctor.RXActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import evereast.co.doctor.DialogFragment.EndSessionConfirmation;
import evereast.co.doctor.R;

import static evereast.co.doctor.Constants.AIR_PREFERENCE_FU;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.RXActivities.PPActivity.INTENT_TYPE;
import static evereast.co.doctor.Utils.GenerateListFromStringList;
import static evereast.co.doctor.Utils.RedirectToActivityWithAnimation;

public class FUActivity extends AppCompatActivity implements EndSessionConfirmation.ConfirmationValueListener{
    TextView pp, cc, ho, ix, dx, rx, ad, fu;
    String list;
    ImageButton nextButton, previousButton;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    ArrayList<String> selectedList;
    AutoCompleteTextView searchET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_u);

        sharedPreferences = getSharedPreferences(AIR_PREFERENCE_FU, MODE_PRIVATE);
        list = sharedPreferences.getString(AIR_PREFERENCE_LIST, "");

        selectedList = new ArrayList<>();

        try {
            if (!list.isEmpty()) {
                selectedList = GenerateListFromStringList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        searchET=findViewById(R.id.search_items);

        pp = findViewById(R.id.pp);
        cc = findViewById(R.id.cc);
        ho = findViewById(R.id.ho);
        ix = findViewById(R.id.ix);
        dx = findViewById(R.id.dx);
        rx = findViewById(R.id.rx);
        ad = findViewById(R.id.ad);
        fu = findViewById(R.id.fu);

        fu.setBackgroundResource(R.drawable.circle_for_air);
        fu.setTextColor(getResources().getColor(R.color.white));
        fu.setEnabled(false);

        pp.setOnClickListener(v -> {
            RedirectToActivityWithAnimation(this, PPActivity.class,previousButton);
        });

        cc.setOnClickListener(v -> {
            RedirectToActivityWithAnimation(this, CCActivity.class,previousButton);
        });

        ix.setOnClickListener(v -> {
            RedirectToActivityWithAnimation(this, IXActivity.class,previousButton);
        });
        dx.setOnClickListener(v -> {
            RedirectToActivityWithAnimation(this, DXActivity.class,previousButton);
        });

        rx.setOnClickListener(v -> {
            RedirectToActivityWithAnimation(this, RXActivity.class,previousButton);
        });

        ad.setOnClickListener(v -> {
            RedirectToActivityWithAnimation(this, ADActivity.class,previousButton);
        });

        ho.setOnClickListener(v -> {
            RedirectToActivityWithAnimation(this, HOActivity.class,previousButton);
        });
        try {
            if (!sharedPreferences.getString(INTENT_TYPE, "Str").equals("appointment")) {
                pp.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Previous(View view) {

    }

    public void Next(View view) {

    }

    public void EndSession(View view) {
        EndSessionConfirmation endSessionConfirmation = new EndSessionConfirmation();
        endSessionConfirmation.show(getSupportFragmentManager(), endSessionConfirmation.getTag());
    }

    @Override
    public void Confirmation(boolean confirm) {

    }
}
package evereast.co.doctor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import evereast.co.doctor.Adapter.AirPresSliderAdapter;
import evereast.co.doctor.R;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class GetPInputActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private TextView[] dots;
    private LinearLayout dotLayout;
    ImageButton nextButton,previousButton;
    ViewPager viewPager;
    AsyncHttpClient client;
    Workbook workbook;
    int currentPosition=0;
    ArrayList<String> arrayList1,arrayList2,arrayList3,arrayList4,arrayList5,arrayList6,arrayList7;
    private Cell[] rows;
    private AirPresSliderAdapter airPresSliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_p_input);

        dotLayout=findViewById(R.id.lt);
        nextButton=findViewById(R.id.nextButton);
        previousButton=findViewById(R.id.previousButton);
        viewPager=findViewById(R.id.view_pager);

        arrayList1=new ArrayList<>();
        arrayList2=new ArrayList<>();
        arrayList3=new ArrayList<>();
        arrayList4=new ArrayList<>();
        arrayList5=new ArrayList<>();
        arrayList6=new ArrayList<>();
        arrayList7=new ArrayList<>();

        client=new AsyncHttpClient();
        String url="https://github.com/Sayerm/School-Management-System/blob/master/Financial%20Sample.xls?raw=true";
        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(GetPInputActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
                Toast.makeText(GetPInputActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Toast.makeText(GetPInputActivity.this, "Loaded", Toast.LENGTH_SHORT).show();
                WorkbookSettings workbookSettings=new WorkbookSettings();
                workbookSettings.setGCDisabled(true);
                if (file!=null){
                    try {
                        workbook=Workbook.getWorkbook(file);
                        Sheet sheet=workbook.getSheet(0);
                        for (int i = 0; i < sheet.getRows(); i++) {
                            rows=sheet.getRow(i);
                            arrayList1.add(rows[0].getContents());
                            arrayList2.add(rows[1].getContents());
                            arrayList3.add(rows[2].getContents());
                            arrayList4.add(rows[3].getContents());
                            arrayList5.add(rows[4].getContents());
                            arrayList6.add(rows[5].getContents());
                            arrayList7.add(rows[6].getContents());
                        }

//                        arrayList1.remove(0);
                        for (int i=0;i<rows.length;i++){
                            Log.i("TAG", "onSuccess: "+rows[i].getContents());
                        }
                        viewPager.addOnPageChangeListener(GetPInputActivity.this);
                        airPresSliderAdapter=new AirPresSliderAdapter(GetPInputActivity.this,arrayList2);
                        try {
                            viewPager.setAdapter(airPresSliderAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        Log.i("TAG", "onSuccess: row 2"+arrayList2 );
                        Log.i("TAG", "onSuccess: row  =====================================================================================" );
                        Log.i("TAG", "onSuccess: row 3"+arrayList3 );
                    } catch (IOException | BiffException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        nextButton.setOnClickListener(v -> viewPager.setCurrentItem(currentPosition + 1));
        previousButton.setOnClickListener(v -> viewPager.setCurrentItem(currentPosition - 1));

        addDotIndicator(0);

    }
    String[] title_list={"Cc","Ho","Ix","Dx","Rx","Ad","Fu"};
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void addDotIndicator(int position) {
        dots = new TextView[title_list.length];
        dotLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,12,8,12);
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(title_list[i]);
            dots[i].setGravity(Gravity.CENTER);
            dots[i].setLayoutParams(layoutParams);
            dots[i].setBackgroundResource(R.drawable.circle_outlined_for_air);
            dots[i].setTextColor(getResources().getColor(R.color.app_bar_color));
            dotLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setBackgroundResource(R.drawable.circle_for_air);
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }
        if (position == 0) {
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        } else if (position == (title_list.length-1)) {
            nextButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.VISIBLE);
        } else {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }

    }

    public void Previous(View view) {

    }

    public void Next(View view) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        addDotIndicator(position);
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
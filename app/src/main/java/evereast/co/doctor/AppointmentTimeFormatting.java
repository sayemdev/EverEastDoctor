package evereast.co.doctor;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Doctor created by Sayem Hossen Saimon on 8/25/2021 at 2:43 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class AppointmentTimeFormatting {

    private static final String TAG = "AppointmentTimeFormatti";

    public static String FormatDoctorTime(String time) {
        String[] values = time.split("\\.");
        int hr = Integer.parseInt(values[0]);
        int min;
        if (values.length > 1) {
            min = 30;
        } else {
            min = 0;
        }
        return hr + ":" + min;
    }

    public static ArrayList<String> GenerateListFromString(String str) {
        try {
            if (!str.isEmpty() && !str.equals("[]")) {
                String[] arrOfStr = str.split(",", 0);
                ArrayList<String> arrayList = new ArrayList<>();
                for (String s : arrOfStr) {
                    arrayList.add(s.trim());
                    Log.i("GENERATING_LIST", "GenerateListFromStringList: " + s);
                }
                return arrayList;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public static String getDate(String dateInString) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = inFormat.parse(dateInString);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        return outFormat.format(date);
    }

    public static boolean IsAvailable(String days) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        int truePosition = 0;
        Log.d(TAG, "IsAvailable: " + days);
        Log.d(TAG, "onDateSelected: " + getDate(date));
        ArrayList<String> arrayList = GenerateListFromString(days);
        Log.d(TAG, "IsAvailable: " + arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            Log.d(TAG, "onDateSelected: " + arrayList.get(i));
            if (getDate(date).trim().toLowerCase().startsWith(arrayList.get(i).toLowerCase().trim())) {
                truePosition++;
            }
        }

        return truePosition > 0;
    }

}

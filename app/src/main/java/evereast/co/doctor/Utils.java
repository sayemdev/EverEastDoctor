package evereast.co.doctor;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.AIR_PREFERENCE_LIST;
import static evereast.co.doctor.Constants.API_KEY;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.BMDCNO;
import static evereast.co.doctor.Constants.DEGREE;
import static evereast.co.doctor.Constants.DESIGNATION;
import static evereast.co.doctor.Constants.EMAIL;
import static evereast.co.doctor.Constants.FEE;
import static evereast.co.doctor.Constants.H_DEGREE;
import static evereast.co.doctor.Constants.IS_APPROVED;
import static evereast.co.doctor.Constants.IS_LOGGED_IN;
import static evereast.co.doctor.Constants.JOB_EXPERIENCE;
import static evereast.co.doctor.Constants.MY_TOKEN;
import static evereast.co.doctor.Constants.PASSWORD;
import static evereast.co.doctor.Constants.PATIENT_URL;
import static evereast.co.doctor.Constants.PHONE;
import static evereast.co.doctor.Constants.PROFILE_PREFERENCE;
import static evereast.co.doctor.Constants.PROFILE_URL;
import static evereast.co.doctor.Constants.SIGNATURE;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;
import static evereast.co.doctor.Constants.USER_NAME;
import static evereast.co.doctor.Constants.imageToString;
import static evereast.co.doctor.Maps.AVAILABLE_ON;
import static evereast.co.doctor.Maps.AVERAGE_DELAY;
import static evereast.co.doctor.Maps.END;
import static evereast.co.doctor.Maps.START;
import static evereast.co.doctor.Maps.WORK_PLACE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.format.DateFormat;
import android.transition.Slide;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import evereast.co.doctor.Activity.ContactUsActivity;
import evereast.co.doctor.Activity.GetPhoneInput;
import evereast.co.doctor.Activity.HomeActivity;
import evereast.co.doctor.utils.ActivityUtils;
import evereast.co.doctor.utils.BaseApplication;
import io.sentry.Sentry;

/**
 * Health Men created by Sayem Hossen Saimon on 12/13/2020 at 7:05 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class Utils {
    public static final String PERMISSION_IS_POPED = "PERMISSION_IS_POPED";
    private static final String TAG = "UTILS";
    static FirebaseAuth firebaseAuth;
    static DatabaseReference userDatabase;
    FirebaseUser firebaseUser;

    public static void closeDrawer(DrawerLayout drawerLayout) {
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    public static void setAnimation(Activity activity) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        slide.setDuration(400);
        slide.setInterpolator(new AccelerateDecelerateInterpolator());
        activity.getWindow().setExitTransition(slide);
        activity.getWindow().setEnterTransition(slide);
    }

    public static String dateFromCalender(Calendar calendar) {
        String date = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH) + "";
        String month = calendar.get(Calendar.MONTH) + 1 < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1) + "";
        return date + "/" + month + "/" + calendar.get(Calendar.YEAR);
    }

    public static String getTimeFromCalender(Calendar calendar) {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, calendar);
    }

    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

   /* public static void redirectToDoctorFinal(Activity activity, String category) {
        Intent intent = new Intent(activity, FindDoctorActivity.class);
        intent.putExtra("category", category);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }*/

    public static void redirectToActivity(Activity activity, Class targetClass) {
        Intent intent = new Intent(activity, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void redirectToVerification(Activity activity, String to) {
        Intent intent = new Intent(activity, GetPhoneInput.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("to", to);
        activity.startActivity(intent);
    }

    public static void redirectToDoctor(Activity activity, Class targetClass, String category) {
        Intent intent = new Intent(activity, targetClass);
        intent.putExtra("category", category);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void recreate(Activity activity) {
        activity.recreate();
    }

    public static String GetUserId(Context context) {
        return context.getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, "");
    }

    public static void Logout(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Logging out...");
        progressDialog.show();

                    FirebaseAuth.getInstance().signOut();
                    String url = BASE_URL + "logout.php?user_id=" + GetUserId(activity);
                    Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.GET, url, response -> {
                        Log.d(TAG, "Logout: " + response);
                    }, error -> {
                        error.printStackTrace();
                    }));
                    progressDialog.dismiss();
                    Log.i(TAG, "onCreate: SignedOut");
                    activity.getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).edit().clear().apply();
                    activity.getSharedPreferences(PROFILE_PREFERENCE, MODE_PRIVATE).edit().clear().apply();
                    ActivityUtils.startLoginActivityAndFinish(activity);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public static void back(Activity activity) {
        activity.finish();
    }

    public static void redirectWithFinish(Activity activity, Class<HomeActivity> targetClass) {
        Intent intent = new Intent(activity, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void Login(String doctorId, String password, String phone, Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Logging in...");
        progressDialog.setMessage("Please wait while login successful");
        progressDialog.show();
        String url = BASE_URL + "login.php?doctor_id=" + doctorId + "&password=" + password + "&device_id=" + getPsuedoUniqueID();
        Log.d(TAG, "Login: " + url);
        Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.GET, url, response -> {
            if (response.equals("\"Invalid ID or password\"")) {
                progressDialog.dismiss();
                Toast.makeText(activity, "ID or password wrong", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    JSONObject userObject = jsonArray.getJSONObject(0);

                    Log.d(TAG, "Login: "+userObject);

//                    String userId = "+88" + phone;
                    String appId = BaseApplication.APP_ID;
                    String accessToken = BaseApplication.ACCESS_TOKEN;
                    String userName = userObject.getString(Maps.NAME);
                    String userIdForShPr = userObject.getString(Maps.DOCTOR_ID);
                    String degree = userObject.getString(Maps.DEGREE);
                    String h_degree = userObject.getString(Maps.H_DEGREE);
                    String designation = userObject.getString(Maps.DESIGNATION);
                    String email = userObject.getString(Maps.EMAIL);
                    String proefileUrl = userObject.getString("d_image");
                    String signature = userObject.getString("d_signature");
                    String phoneShPr = userObject.getString(Maps.PHONE);
                    String jobExperience = userObject.getString(Maps.JOB_EXPERIENCE);
                    String workPlace = userObject.getString(Maps.WORK_PLACE);
                    String bmdc = userObject.getString(Maps.BMDC);
                    String start = userObject.getString(START);
                    String end = userObject.getString(Maps.END);
                    String avg_delay = userObject.getString(Maps.AVERAGE_DELAY);
                    String availableOn = userObject.getString(Maps.AVAILABLE_ON);
                    String fee = userObject.getString(Maps.FEE);
//                    String passwordShPr=userObject.getString("password");
                    if(!userObject.getString("d_status").trim().equals("Suspended")) {

                                    progressDialog.dismiss();
                                    activity.setResult(Activity.RESULT_OK, null);
//                                ActivityUtils.startHomeActivityAndFinish(activity);
                                    SharedPreferences.Editor editor = activity.getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).edit();
                                    editor.putString(USER_NAME, userName);
                                    editor.putString(USER_ID, userIdForShPr);
                                    editor.putString(SIGNATURE, signature);
                                    editor.putString(DEGREE, degree);
                                    editor.putString(H_DEGREE, h_degree);
                                    editor.putBoolean(IS_APPROVED, true);
                                    editor.putString(EMAIL, email);
                                    editor.putString(START, start);
                                    editor.putString(END, end);
                                    editor.putString(AVERAGE_DELAY, avg_delay);
                                    editor.putString(DESIGNATION, designation);
                                    editor.putString(AVAILABLE_ON, availableOn);
                                    editor.putString(JOB_EXPERIENCE, jobExperience);
                                    editor.putString(WORK_PLACE, workPlace);
                                    editor.putString(PHONE, phoneShPr);
                                    editor.putString(BMDCNO, bmdc);
                                    editor.putString(FEE, fee);
                                    editor.putString(PROFILE_URL, proefileUrl);
                                    editor.putBoolean(IS_LOGGED_IN, true);
                                    editor.apply();
                                    userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdForShPr);

                                    userDatabase.child("OnlineStatus").setValue("Offline");
                                    Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.POST, PATIENT_URL + "update_status.php", responseOff -> {
                                        Log.d(TAG, "CheckUncheck: " + responseOff);
                                    }, error -> {
                                        error.printStackTrace();
                                        Sentry.captureMessage(error.getMessage());
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> map = new HashMap<>();
                                            map.put("id", userIdForShPr);
                                            map.put("status", "Offline");
                                            return map;
                                        }
                                    });

                                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                                        if (task.isComplete()) {
                                            String token = task.getResult();
                                            Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.GET,
                                                    "https://www.healthmen.com.bd/wp-json/pd/fcm/subscribe?api_secret_key=" + API_KEY + "&user_email=" + email + "&device_token=" + token + "&subscribed=Doctor&device_name=" + Build.MODEL,
                                                    response2 -> {
                                                        Log.i("TAG", "onCreate: " + response2);
                                                    },
                                                    Throwable::printStackTrace));
                                            Log.i("TAG_H", "onCreate: " + token);
                                            editor.putString(MY_TOKEN, token);
                                            editor.apply();
                                            userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdForShPr);

                                            Map<String, String> map = new HashMap<>();

                                            map.put(USER_NAME, userName);
                                            map.put(USER_ID, userIdForShPr);
                                            map.put(DEGREE, degree);
                                            map.put(H_DEGREE, h_degree);
                                            map.put(EMAIL, email);
                                            map.put(JOB_EXPERIENCE, jobExperience);
                                            map.put(PHONE, phoneShPr);
                                            map.put(BMDCNO, bmdc);
                                            map.put(FEE, fee);
                                            map.put(PROFILE_URL, proefileUrl);
                                            map.put(PASSWORD, password);
                                            map.put(MY_TOKEN, task.getResult());
                                            map.put("OnlineStatus", "Offline");
                                            userDatabase.setValue(map).addOnCompleteListener(task1 -> {
                                                if(task1.isComplete()){
                                                    Log.d(TAG, "Login: "+task.getResult());
                                                }
                                            });
                                        } else {
                                            editor.apply();
                                        }
                                        redirectWithFinish(activity, HomeActivity.class);
                                    });
                    }else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                        alertDialog.setTitle("Account Suspended");
                        alertDialog.setMessage("Your account has been suspended. Please contact with Clinicure support to reactivate your account");
                        alertDialog.setPositiveButton("Go to support", (dialog, which) -> {
                            dialog.dismiss();
                            activity.startActivity(new Intent(activity, ContactUsActivity.class));
                        });
                        alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        alertDialog.show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Sentry.captureMessage(e.getMessage());
                    Toast.makeText(activity, "ID or password wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            progressDialog.dismiss();
            error.printStackTrace();
            Log.d(TAG, "Login: " + error.getMessage());
            Sentry.captureMessage(error.getMessage());
            Toast.makeText(activity, "Something went wrong. Please check your internet", Toast.LENGTH_SHORT).show();
        }));

    }


    public static void ChangePassword(String phone, String password, Activity activity, Class<HomeActivity> targetClass) {
        Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.POST, BASE_URL + "reset_pass.php",
                response -> {
                    if (response.equals("Failed to changing password")) {
                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                    } else {
                        redirectWithFinish(activity, targetClass);
                    }
                }, error -> {
            Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("phone", phone);
                map.put("password", password);
                return map;
            }
        });
    }

    public static void UploadProfileImage(Activity activity, String uid, Bitmap bitmap, ProgressDialog progressDialog) {
        String url = BASE_URL + "update_profile_pic.php";
        String imageName = uid + ".jpg";
        Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.POST, url,
                response -> {
                    progressDialog.dismiss();
                    if (response.equals("Profile picture added")) {
                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(activity, HomeActivity.class));
                    } else {
                        Toast.makeText(activity, response + "", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressDialog.dismiss();
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("patient_id", uid);
                map.put("p_profile", imageName);
                map.put("profile_string", imageToString(bitmap));
                return map;
            }
        });

    }

    public static String generateFileName(String userId, String fileType) {
        Random r = new Random();
        final int i1 = r.nextInt(1000000000 - 1000000) + 1000000;
        return userId + System.currentTimeMillis() + i1 + fileType;
    }

    public static String generateAppointmentId(String userId, String doctorId) {
        Random r = new Random();
        final int i1 = r.nextInt(1000000000 - 1000000) + 1000000;
        return userId + System.currentTimeMillis() + i1 + doctorId;
    }

    public static String generatePrescriptionId(String userId) {
        return userId + System.currentTimeMillis();
    }

    public static ArrayList<String> GenerateListFromStringList(String str) {
        try {
            if (!str.isEmpty() && !str.equals("[]")) {
                StringBuilder stringBuilder = new StringBuilder(str);
                stringBuilder.deleteCharAt(0);
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                String[] arrOfStr = stringBuilder.toString().split(",", 0);
                ArrayList<String> arrayList = new ArrayList<>();
                for (String s : arrOfStr) {
                    arrayList.add(s.trim());
                    Log.i(TAG, "GenerateListFromStringList: " + s);
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

    public static void RedirectToActivityWithAnimation(Activity activity, Class targetClass, View view) {
        Intent intent = new Intent(activity, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("prescription", "home");
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<>(view, "transition");
//        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);, activityOptions.toBundle()
        activity.startActivity(intent);
    }

    public static void RedirectToActivityWithAnimationUpdated(Activity activity, Class targetClass, ChipGroup chipGroup, String editorName, String list) {
        Intent intent = new Intent(activity, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences(editorName, MODE_PRIVATE).edit();
            if (!list.equals("[]")) {
                editor.putString(AIR_PREFERENCE_LIST, list.trim());
            } else {
                editor.putString(AIR_PREFERENCE_LIST, "");
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            SharedPreferences.Editor editor = activity.getSharedPreferences(editorName, MODE_PRIVATE).edit();
            editor.putString(AIR_PREFERENCE_LIST, "");
            editor.apply();
        }
//        intent.putExtra("prescription","home");
        activity.startActivity(intent);
    }

    public static void RedirectToActivityWithAnimationPrevious(Activity activity, Class targetClass, View view, String editorName, String list) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(editorName, MODE_PRIVATE).edit();
        editor.putString(AIR_PREFERENCE_LIST, list);
        editor.apply();
        Intent intent = new Intent(activity, targetClass);
        intent.putExtra("prescription", "home");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);//, activityOptions.toBundle()
    }

    public static String ordinal(int i) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    /**
     * Return pseudo unique ID
     *
     * @return ID
     */
    public static String getPsuedoUniqueID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" +
                (Build.BOARD.length() % 10)
                + (Build.BRAND.length() % 10)
                + (Build.CPU_ABI.length() % 10)
                + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10)
                + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static boolean CheckPermissionPoped(Context context) {
        return context.getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getBoolean(PERMISSION_IS_POPED, false);
    }

}

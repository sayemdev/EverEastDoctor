package evereast.co.doctor.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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

import evereast.co.doctor.Adapter.NotificationAdapter;
import evereast.co.doctor.Constants;
import evereast.co.doctor.Model.NotificationModel;
import evereast.co.doctor.R;

public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";
    RecyclerView recyclerView;
    List<NotificationModel> notificationModelList;
    String title, description, link, image;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
context=view.getContext();
        notificationModelList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.notiList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        /*Volley.newRequestQueue(view.getContext()).add(new StringRequest(Request.Method.GET, WP_URL + "posts",
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject titleObject = jsonObject.getJSONObject("title");
                            JSONObject descriptionObj = jsonObject.getJSONObject("content");
                            title = titleObject.getString("rendered");
                            description = descriptionObj.getString("rendered");
                            link = jsonObject.getString("link");
                            try {
                                image = jsonObject.getString("featured_image_src")*//*.replaceAll("-150x150","")*//*;
                            } catch (Exception e) {
                                e.printStackTrace();
                                image = "No Image";
                            }
                            if (jsonObject.getJSONArray("categories").toString().equals("[145]")) {
                                Log.d(TAG, "onCreateView: doctor");
                                Log.i("TAG_TITLE", "onCreate: " + jsonObject.getString("featured_media"));
                                NotificationModel notificationModel = new NotificationModel(title, description, image, link);
                                notificationModelList.add(notificationModel);
                            }
                        }
                        recyclerView.setAdapter(new NotificationAdapter(view.getContext(), notificationModelList));
                    } catch (JSONException e) {
                        image = "notfound";
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        }));*/

/*        SetNotification("10% off in CSCR","Up to 10% discount in CSCR","123.jpg", link);
        SetNotification("20% discount in Ibn Sina","Up to 20% discount in Ibn Sina","124.jpg", link);
        SetNotification("10% off in National Hospital","Up to 10% discount in National Hospital","125.jpg", link);
        SetNotification("15% off in Chevron","Up to 15% discount in Chevron","126.jpg", link);
        SetNotification("30% off in Epic Health Care","Up to 30% discount in Epic Health Care","127.jpg", link);*/

//        recyclerView.setAdapter(new NotificationAdapter(view.getContext(), notificationModelList));

        GetNotification();
        return view;
    }
    Context context;
    public void GetNotification() {
        Volley.newRequestQueue(context).add(new StringRequest(Request.Method.GET, Constants.MAIN_URL + "blog_notifications.php",
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        notificationModelList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            title = jsonObject.getString("title");
                            description = jsonObject.getString("content");
                            link = jsonObject.getString("link");
                            image = jsonObject.getString("image");
                            NotificationModel notificationModel = new NotificationModel(title, description, image, link);
                            notificationModelList.add(notificationModel);
                        }
                        recyclerView.setAdapter(new NotificationAdapter(context, notificationModelList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        }));

    }

    private void SetNotification(String title, String body, String image, String link) {
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setTitle(title);
        notificationModel.setBody(body);
        notificationModel.setImage(image);
        notificationModel.setLink(link);
        notificationModelList.add(notificationModel);
    }
}
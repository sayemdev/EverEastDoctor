package evereast.co.doctor.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static evereast.co.doctor.Constants.BASE_URL;
import static evereast.co.doctor.Constants.USER_DATA_PREF;
import static evereast.co.doctor.Constants.USER_ID;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evereast.co.doctor.Activity.ViewTemplatePrescription;
import evereast.co.doctor.Model.TemplateModel;
import evereast.co.doctor.R;

/**
 * Doctor created by Sayem Hossen Saimon on 7/26/2021 at 12:06 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class TemplatesAdapter extends RecyclerView.Adapter<TemplatesAdapter.ViewHolder> {
    private static final String TAG = "TemplatesAdapter";
    List<TemplateModel> templateModelList;
    Context context;
    ClickListener clickListener;
    boolean isDialog;

    public TemplatesAdapter(List<TemplateModel> templateModelList, Context context, ClickListener clickListener, boolean isDialog) {
        this.templateModelList = templateModelList;
        this.context = context;
        this.clickListener = clickListener;
        this.isDialog = isDialog;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.template_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.titleTV.setText(templateModelList.get(holder.getAdapterPosition()).getTitle());
        holder.descriptionTV.setText(templateModelList.get(holder.getAdapterPosition()).getDescription());

        holder.itemView.setOnClickListener(v -> {
            //Delete Template
            if (isDialog) {
                clickListener.OnClick(holder.getAdapterPosition(), templateModelList.get(holder.getAdapterPosition()));
            } else {
                Intent intent = new Intent(context, ViewTemplatePrescription.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("full", templateModelList.get(holder.getAdapterPosition()).getFullJson());
                intent.putExtra("title", templateModelList.get(holder.getAdapterPosition()).getTitle());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            Volley.newRequestQueue(context).add(new StringRequest(Request.Method.POST, BASE_URL + "delete_template.php", response -> {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                if (response.equals("Prescription deleted successfully")) {
                    templateModelList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    clickListener.OnDeleted(templateModelList);
                }
            }, error -> {
                error.printStackTrace();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("doctor_id", context.getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE).getString(USER_ID, ""));
                    map.put("pres_id", templateModelList.get(holder.getAdapterPosition()).getPrescId());
                    return map;
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return templateModelList.size();
    }

    public interface ClickListener {
        void OnClick(int position, TemplateModel templateModel);

        void OnDeleted(List<TemplateModel> templateModelList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, descriptionTV;
        ImageView deleteButton;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }

}

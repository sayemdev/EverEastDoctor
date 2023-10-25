package evereast.co.doctor.Drug;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.util.ArrayList;
import java.util.List;

import evereast.co.doctor.R;

/**
 * Doctor created by Sayem Hossen Saimon on 2/20/2021 at 11:10 AM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.ViewHolder> {

    List<Medicines>medicinesList;
    Activity activity;
    boolean isTrade;
    ToolTipsManager toolTipsManager;
    ViewGroup rootLt;
    ArrayList<String>sugList;

    public DrugListAdapter(List<Medicines> medicinesList, Activity activity, boolean isTrade, ToolTipsManager toolTipsManager,ViewGroup rootLt,ArrayList<String>sugList) {
        this.medicinesList = medicinesList;
        this.activity = activity;
        this.isTrade = isTrade;
        this.toolTipsManager = toolTipsManager;
        this.rootLt=rootLt;
        this.sugList=sugList;
    }

    public DrugListAdapter(List<Medicines> medicinesList, Activity activity, boolean isTrade, ToolTipsManager toolTipsManager,ViewGroup rootLt) {
        this.medicinesList = medicinesList;
        this.activity = activity;
        this.isTrade = isTrade;
        this.toolTipsManager = toolTipsManager;
    }

    public DrugListAdapter(List<Medicines> medicinesList, Activity activity, boolean isTrade) {
        this.medicinesList = medicinesList;
        this.activity = activity;
        this.isTrade=isTrade;
    }

    public DrugListAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_list_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isTrade){
            holder.drugNameTextView.setText(medicinesList.get(position).getBrandName()+" "+medicinesList.get(position).getStrength());
        }else {
            holder.drugNameTextView.setText(medicinesList.get(position).getGenericName()+" "+medicinesList.get(position).getStrength());
        }

        holder.drugNameTextView.setOnLongClickListener(v -> {
            toolTipsManager.dismissAll();
            ToolTip.Builder builder = new ToolTip.Builder(activity, holder.drugNameTextView, rootLt, medicinesList.get(position).getPrice1(), ToolTip.POSITION_ABOVE);
            builder.setAlign(ToolTip.ALIGN_CENTER);
            builder.setBackgroundColor(activity.getResources().getColor(R.color.app_bar_color));
            builder.setGravity(ToolTip.GRAVITY_CENTER);
            builder.setTextAppearance(R.style.TooltipTextAppearance);
            builder.withArrow(true);
            toolTipsManager.show(builder.build());
            return false;
        });

        holder.itemView.setOnClickListener(v -> {
//            1+1+1,1+1+0,1+0+1,1+0+0,0+1+1,0+0+1

        });

    }

    @Override
    public int getItemCount() {
        return Math.min(medicinesList.size(), 20);
//        return medicinesList.size();
    }
    public void filteredList(List<Medicines> userArrayListList){
        medicinesList=userArrayListList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView drugNameTextView;
        RelativeLayout rootLt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drugNameTextView=itemView.findViewById(R.id.drugNameTv);
            rootLt=itemView.findViewById(R.id.rootLt);
        }
    }
}

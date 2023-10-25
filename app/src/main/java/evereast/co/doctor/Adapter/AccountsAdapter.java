package evereast.co.doctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import evereast.co.doctor.Activity.LoginActivity;
import evereast.co.doctor.Model.AccountsModel;
import evereast.co.doctor.R;

/**
 * Helthmen Patient created by Sayem Hossen Saimon on 8/4/2021 at 4:32 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {
    private static final String TAG = "AccountsAdapter";
    List<AccountsModel> accountsModelList;
    Context context;

    public AccountsAdapter(List<AccountsModel> accountsModelList, Context context) {
        this.accountsModelList = accountsModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_accounts_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        AccountsModel accountsModel = accountsModelList.get(position);

        String[] arrOfStr = accountsModel.getUserName().trim().split(" ", 0);
        StringBuilder s1 = new StringBuilder();
        String sd = "";
        if (arrOfStr.length == 1) {
            sd = String.valueOf(arrOfStr[0].charAt(0));
        } else {
            for (int i = 0; i < 2; i++) {
                String s = arrOfStr[i];
                s1.append(s.charAt(0));
            }
            sd = s1.toString();
        }

        Log.d(TAG, "onBindViewHolder: " + sd);

        holder.namePrefixTextView.setText(sd);
        holder.nameTextView.setText(accountsModel.getUserName());
        holder.phoneTextView.setText(accountsModel.getPhone());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("phone", accountsModel.getPhone());
            intent.putExtra("id", accountsModel.getTempID());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return accountsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namePrefixTextView, nameTextView, phoneTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            namePrefixTextView = itemView.findViewById(R.id.namePrefixTV);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);

        }
    }
}

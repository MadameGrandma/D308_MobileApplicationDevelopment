package com.thins15.d308vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thins15.d308vacationplanner.R;
import com.thins15.d308vacationplanner.dao.ExcursionDAO;
import com.thins15.d308vacationplanner.entities.Excursion;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    public ExcursionAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;
        private final TextView excursionItemView2;
        private final TextView excursionItemView3;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.excurTitle1);
            excursionItemView2 = itemView.findViewById(R.id.excurVacay);
            excursionItemView3 = itemView.findViewById(R.id.excurDate2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionID());
                    intent.putExtra("title", current.getExcursionTitle());
                    intent.putExtra("startDate", current.getExcursionDate());
                    intent.putExtra("vacayID", current.getVacationID());
                    context.startActivity(intent);
                }
            });
        }
    }


        @NonNull
        @Override
        public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
            return new ExcursionViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
            if (mExcursions != null) {
                Excursion current = mExcursions.get(position);
                String title = current.getExcursionTitle();
                int vacayID = current.getVacationID();
                String date = current.getExcursionDate();
                holder.excursionItemView.setText(title);
                holder.excursionItemView2.setText(Integer.toString(vacayID));
                holder.excursionItemView3.setText(date);
            } else {
                holder.excursionItemView.setText("No excursion title");
                holder.excursionItemView2.setText("No excursion ID");
                holder.excursionItemView3.setText("No excursion date");
            }
        }



    public void setExcursions(List<Excursion> excursions){
        mExcursions = excursions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mExcursions != null){
            return mExcursions.size();
        } else return 0;
    }

}

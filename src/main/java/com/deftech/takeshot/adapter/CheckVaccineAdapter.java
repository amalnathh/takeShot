package com.deftech.takeshot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deftech.takeshot.R;
import com.deftech.takeshot.model.Centers;
import com.deftech.takeshot.model.CentersIndividual;

import java.util.ArrayList;
import java.util.List;

public class CheckVaccineAdapter extends RecyclerView.Adapter<CheckVaccineAdapter.thisAdapterViewHolder> implements Filterable {

    private List<Centers> parentCentersOriginal;
    private List<Centers> parentCentersCopy;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    @NonNull
    @Override
    public CheckVaccineAdapter.thisAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_checker_centers, parent, false);
        return new thisAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CheckVaccineAdapter.thisAdapterViewHolder holder, int position) {
        final Centers parentCurrentCenter = parentCentersOriginal.get(position);


        holder.centername.setText(parentCurrentCenter.getCenter_name());
        holder.address.setText(parentCurrentCenter.getAddress());

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.childRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false
        );
        layoutManager.setInitialPrefetchItemCount(2);
        SessionsAdapter sessionsAdapter = new SessionsAdapter(parentCurrentCenter.getSessions(), parentCurrentCenter.getFee_type());
        holder.childRecyclerView.setLayoutManager(layoutManager);
        holder.childRecyclerView.setAdapter(sessionsAdapter);
        holder.childRecyclerView.setRecycledViewPool(viewPool);
        holder.progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return parentCentersOriginal.size();
    }

    @Override
    public Filter getFilter() {
        return arrayFilter;
    }

    private Filter arrayFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Centers> filterdList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filterdList.addAll(parentCentersCopy);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Centers item : parentCentersCopy) {
                    if (item.getCenter_name().toLowerCase().trim().contains(filterPattern) || item.getFee_type().toLowerCase().trim().contains(filterPattern)) {
                        filterdList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            parentCentersOriginal.clear();
            parentCentersOriginal.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public CheckVaccineAdapter(List<Centers> arrayCenters) {
        this.parentCentersOriginal= arrayCenters;
        parentCentersCopy = new ArrayList<>(arrayCenters);
    }

    class thisAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView centername;
        private TextView address;
        private RecyclerView childRecyclerView;
        private RelativeLayout mContactItem;
private ProgressBar progressBar;
        public thisAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            centername = itemView.findViewById(R.id.tv_center_name);
            address = itemView.findViewById(R.id.tv_address);
            mContactItem = itemView.findViewById(R.id.item_contact_layout);
            childRecyclerView = itemView.findViewById(R.id.recycler_view_sessions);
            progressBar = itemView.findViewById(R.id.progressSpinnerIndiCen);
        }
    }
}

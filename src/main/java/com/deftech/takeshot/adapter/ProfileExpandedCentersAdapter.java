package com.deftech.takeshot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.deftech.takeshot.R;
import com.deftech.takeshot.model.CentersIndividual;

import java.util.List;

public class ProfileExpandedCentersAdapter extends RecyclerView.Adapter<ProfileExpandedCentersAdapter.thisAdapterViewHolder> {
    List<CentersIndividual> selectedCentersArray;
    String doseName;
    RequestQueue requestQueue;
    Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


    @NonNull
    @Override
    public thisAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_checker_centers, parent, false);
        requestQueue = Volley.newRequestQueue(parent.getContext());
        return new thisAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull thisAdapterViewHolder holder, int position) {
        CentersIndividual currentCenter = selectedCentersArray.get(position);
        holder.centername.setText(currentCenter.getName());
        holder.address.setText(currentCenter.getAddress());
        holder.progress.setVisibility(View.GONE);
        holder.childRecyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.childRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false
        );
        layoutManager.setInitialPrefetchItemCount(2);
        SessionsAdapter sessionsAdapter = new SessionsAdapter(currentCenter.getSessions(), currentCenter.getFee());
        holder.childRecyclerView.setLayoutManager(layoutManager);
        holder.childRecyclerView.setAdapter(sessionsAdapter);
        holder.childRecyclerView.setRecycledViewPool(viewPool);

    }


    public ProfileExpandedCentersAdapter(String doseName, List<CentersIndividual> selectedCentersArray, Context context) {
        this.doseName = doseName;
        this.selectedCentersArray = selectedCentersArray;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return selectedCentersArray.size();
    }

    class thisAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView centername;
        private TextView address;
        private RecyclerView childRecyclerView;
        private RelativeLayout mContactItem;
        private ProgressBar progress;

        public thisAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            centername = itemView.findViewById(R.id.tv_center_name);
            address = itemView.findViewById(R.id.tv_address);
            progress = itemView.findViewById(R.id.progressSpinnerIndiCen);
            mContactItem = itemView.findViewById(R.id.item_contact_layout);
            childRecyclerView = itemView.findViewById(R.id.recycler_view_sessions);
        }
    }
}

package com.deftech.takeshot.adapter;

import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.deftech.takeshot.R;
import com.deftech.takeshot.model.CentersIndividual;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NearByCentersAdapter extends RecyclerView.Adapter<NearByCentersAdapter.thisAdapterViewHolder> implements Filterable {
    private static final String TAG = "SelectCenterAdapter";
    private List<CentersIndividual> individualsCenterDetailsOriginal;
    private List<CentersIndividual> individualsCenterDetailsCopy;
    Location startpoint = new Location("locationA");


    @NonNull
    @Override
    public NearByCentersAdapter.thisAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_center, parent, false);
        return new thisAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NearByCentersAdapter.thisAdapterViewHolder holder, final int position) {
        final CentersIndividual individualCenter = individualsCenterDetailsOriginal.get(position);
//todo wrong logic down
        if (individualCenter.isSelected()) {
            holder.centerSelected.setChecked(true);
        } else {
            holder.centerSelected.setChecked(false);
        }
        Location endPoint = new Location("locationA");
        endPoint.setLatitude(Double.parseDouble(individualCenter.getLat()));
        endPoint.setLongitude(Double.parseDouble(individualCenter.getLon()));
        double distance = startpoint.distanceTo(endPoint) / 1000;
        DecimalFormat df = new DecimalFormat("#.##");
        holder.centername.setText(individualCenter.getName());
        holder.blockname.setText(individualCenter.getBlock_name());
        holder.feetype.setText(df.format(distance) + " " + "km");
        holder.address.setText(individualCenter.getAddress());
        final CheckBox checkbox = holder.centerSelected;
        holder.centerSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               individualCenter.setSelected(b);
            }
        });
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: NearByCenterItem");
                if (checkbox.isChecked()) {
                    checkbox.setChecked(false);
                } else {
                    checkbox.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return individualsCenterDetailsOriginal.size();
    }

    @Override
    public Filter getFilter() {
        return arrayFilter;
    }

    private Filter arrayFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<CentersIndividual> filterdList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filterdList.addAll(individualsCenterDetailsCopy);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                Log.d(TAG, individualsCenterDetailsCopy.toString());
                for (CentersIndividual item : individualsCenterDetailsCopy) {
                    Log.d(TAG, "performFiltering: for loop");
                    if (item.getAddress().toLowerCase().contains(filterPattern) || item.getName().toLowerCase().contains(filterPattern)) {
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
            Log.d(TAG, "publishResults: published");
            Log.d(TAG, filterResults.values.toString());
            individualsCenterDetailsOriginal.clear();
            individualsCenterDetailsOriginal.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public NearByCentersAdapter(List<CentersIndividual> arrayIndividualCenters, String lattitude, String longitude) {
        Log.d(TAG, arrayIndividualCenters.toString());
        this.individualsCenterDetailsOriginal = arrayIndividualCenters;
        individualsCenterDetailsCopy = new ArrayList<>(individualsCenterDetailsOriginal);
        startpoint.setLatitude(Double.parseDouble(lattitude));
        startpoint.setLongitude(Double.parseDouble(longitude));
    }

    public ArrayList<CentersIndividual> getSelectedCentersArray() {
        ArrayList<CentersIndividual> selectedCentersArray = new ArrayList<>();
        for (CentersIndividual center:individualsCenterDetailsOriginal) {
            if (center.isSelected()){
                selectedCentersArray.add(center);
            }
        }
        return selectedCentersArray;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull thisAdapterViewHolder holder) {
        if (individualsCenterDetailsOriginal.get(holder.getAdapterPosition()).isSelected()){
            holder.centerSelected.setChecked(true);
        }else {
            holder.centerSelected.setChecked(false);
        }
        super.onViewAttachedToWindow(holder);
    }

    class thisAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView centername;
        TextView address;
        TextView blockname;
        TextView feetype;
        CheckBox centerSelected;
        ConstraintLayout constraintLayout;

        public thisAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            centername = itemView.findViewById(R.id.ic_name);
            address = itemView.findViewById(R.id.ic_address);
            blockname = itemView.findViewById(R.id.ic_block_name);
            feetype = itemView.findViewById(R.id.ic_fee_type);
            centerSelected = itemView.findViewById(R.id.ic_selected);
            constraintLayout = itemView.findViewById(R.id.ic_view_parent);
        }
    }
}

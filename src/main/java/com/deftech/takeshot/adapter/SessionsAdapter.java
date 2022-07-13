package com.deftech.takeshot.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deftech.takeshot.R;
import com.deftech.takeshot.WebViewActivity;
import com.deftech.takeshot.model.Sessions;

import java.util.List;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionsViewHolder> {
    private List<Sessions> sessionsList;
    private String fee;

    @NonNull
    @Override
    public SessionsAdapter.SessionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sessions_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        return new SessionsViewHolder(view);
    }

    SessionsAdapter(List<Sessions> sessionsList, String fee) {
        this.sessionsList = sessionsList;
        this.fee = fee;
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsAdapter.SessionsViewHolder holder, int position) {
        final Sessions currentSession = sessionsList.get(position);
        if (!currentSession.getSession_id().equals("codeNullSessions")) {
            holder.vaccine.setText(currentSession.getVaccine());
            if (currentSession.getMin_age() == 22882) {
                holder.feeView.setText("");
                holder.feeView.setText("");
                holder.ageView.setVisibility(View.GONE);
                holder.feeView.setVisibility(View.GONE);
            } else {
                holder.ageView.setVisibility(View.VISIBLE);
                holder.feeView.setVisibility(View.VISIBLE);
                holder.ageView.setText(currentSession.getMin_age() + "+");
                holder.feeView.setText(fee);
            }
            holder.capacity.setText(currentSession.getAvailable_capacity() + "");
            holder.date.setText(currentSession.getDate());
            if (currentSession.getAvailable_capacity() == 0) {
                holder.capacity.setText("BOOKED");
                holder.dose.setText("");
                holder.capacity.setTextColor(Color.RED);
            } else if (!(currentSession.getAvailable_capacity() == 0)) {
                if (currentSession.getAvailable_capacity() == 0) {
                    if (currentSession.getMin_age() == 22882) {
                        holder.dose.setText("");
                    } else {
                        holder.dose.setText("dose");
                    }
                } else {
                    if (currentSession.getMin_age() == 22882) {
                        holder.dose.setText("");
                    } else {
                        holder.dose.setText("doses");
                    }
                }
                holder.capacity.setText(currentSession.getAvailable_capacity() + "");
                holder.capacity.setTextColor(Color.parseColor("#7BBD7C"));
            }
        }
        else {
            holder.ageView.setVisibility(View.GONE);
            holder.capacity.setVisibility(View.GONE);
            holder.feeView.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
            holder.dose.setVisibility(View.GONE);
            holder.vaccine.setText("No slots found.");
        }

    }

    @Override
    public int getItemCount() {
        return sessionsList.size();
    }

    class SessionsViewHolder extends RecyclerView.ViewHolder {
        TextView vaccine;
        TextView date;
        TextView capacity;
        TextView dose;
        TextView ageView;
        TextView feeView;

        public SessionsViewHolder(@NonNull View itemView) {
            super(itemView);
            vaccine = itemView.findViewById(R.id.vaccine);
            date = itemView.findViewById(R.id.session_date);
            capacity = itemView.findViewById(R.id.capacity);
            dose = itemView.findViewById(R.id.dose);
            ageView = itemView.findViewById(R.id.age);
            feeView = itemView.findViewById(R.id.fee);
        }
    }
}

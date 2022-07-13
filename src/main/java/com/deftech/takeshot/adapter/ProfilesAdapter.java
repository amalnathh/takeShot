package com.deftech.takeshot.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deftech.takeshot.ProfileExpandedActivity;
import com.deftech.takeshot.R;
import com.deftech.takeshot.model.CentersIndividual;
import com.deftech.takeshot.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.thisAdapterViewHolder> {
    List<User> usersArray = new ArrayList<>();
    private RequestQueue  requestQueue;
    private JsonObjectRequest objectRequest;

    @NonNull
    @Override
    public ProfilesAdapter.thisAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_profile, parent, false);
        requestQueue = Volley.newRequestQueue(parent.getContext());

        return new ProfilesAdapter.thisAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfilesAdapter.thisAdapterViewHolder holder, int position) {
        final User currentUser = usersArray.get(position);
        holder.userName.setText(currentUser.getName());
        holder.age.setText(currentUser.getAge()+"");
        holder.progressBarLayer.setVisibility(View.VISIBLE);
        searchVaccine(currentUser, holder);
        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileExpandedActivity.class);
                intent.putExtra("currentUserDetails",currentUser);
                view.getContext().startActivity(intent);
            }
        });

    }

    private void searchVaccine(final User currentUser, final thisAdapterViewHolder holder) {

        objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL(currentUser.getDistrictId()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray centers = (JSONArray) response.get("centers");
                            for (int i = 0; i < centers.length(); i++) {
                                JSONObject jsonObject = (JSONObject) centers.get(i);
                                JSONArray jsonArrayOfSessions = (JSONArray) jsonObject.get("sessions");
                                for (int a =0;a<currentUser.getSelectedCentersArray().size();a++){
                                    CentersIndividual currentUserCurrentCenter = currentUser.getSelectedCentersArray().get(a);
                                    if (currentUserCurrentCenter.getCenter_id()==(Integer.parseInt(jsonObject.get("center_id").toString()))){
                                        for (int b = 0; b < jsonArrayOfSessions.length(); b++) {
                                            JSONObject sessionsElement = (JSONObject) jsonArrayOfSessions.get(b);
                                            if (currentUser.getDose().toLowerCase().trim().equals("1stdose")) {
                                                if (!sessionsElement.get("available_capacity_dose1").toString().equals("0")) {
                                                    holder.infoBar.setText("Vaccines are available");
                                                } else {
                                                    holder.infoBar.setText("No vaccines available");
                                                }
                                            } else if (currentUser.getDose().toLowerCase().trim().equals("2nddose")) {
                                                if (!sessionsElement.get("available_capacity_dose2").toString().equals("0")) {
                                                    holder.infoBar.setText("Vaccines are available");
                                                } else {
                                                    holder.infoBar.setText("No vaccines available");
                                                }
                                            }
                                        }
                                    }else{
                                        holder.infoBar.setText("No vaccines available");
                                    }
                                }

                            }
                            holder.progressBarLayer.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(objectRequest);
    }


    private String URL(String districtId) {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(time);
        return "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" + districtId + "&date=" + date;
    }

    public ProfilesAdapter(List<User> array) {
        this.usersArray = array;
    }

    @Override
    public int getItemCount() {
        return usersArray.size();
    }

    class thisAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView infoBar;
        ProgressBar progressBar;
        ConstraintLayout progressBarLayer;
        ConstraintLayout viewItem;
        TextView age;
        ImageButton deleteBtn;

        public thisAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username);
            age = itemView.findViewById(R.id.userage);
            infoBar = itemView.findViewById(R.id.info_bar);
            progressBarLayer = itemView.findViewById(R.id.progressBarLayout);
            deleteBtn = itemView.findViewById(R.id.deleteProfileBtn);
            progressBar = itemView.findViewById(R.id.progressBarHorizontal);
            viewItem = itemView.findViewById(R.id.constraintLayout2);
        }
    }
}

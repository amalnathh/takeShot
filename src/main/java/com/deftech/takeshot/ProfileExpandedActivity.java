package com.deftech.takeshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deftech.takeshot.adapter.ProfileExpandedCentersAdapter;
import com.deftech.takeshot.model.CentersIndividual;
import com.deftech.takeshot.model.Sessions;
import com.deftech.takeshot.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProfileExpandedActivity extends AppCompatActivity {
    ProfileExpandedCentersAdapter profileExpandedCentersAdapter;
    Toolbar toolbar;
    FloatingActionButton addNewCenter;
    RecyclerView recyclerView;
    List<CentersIndividual> selectedCenters;
    private JsonObjectRequest objectRequest;
    private Timer t;
    RequestQueue requestQueue;
    Intent intent;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_expanded);
        selectedCenters = new ArrayList<>();

        intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        t = new Timer();
        currentUser = (User) intent.getSerializableExtra("currentUserDetails");
        selectedCenters = new ArrayList<>(currentUser.getSelectedCentersArray());
        Log.d("profileexpanded", selectedCenters.toString());

        Log.d("Selectintent", currentUser.getName().toString());
        toolbar = findViewById(R.id.toolbarExpandedProfile);
        addNewCenter = findViewById(R.id.addNewCenterBtn);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        profileExpandedCentersAdapter = new ProfileExpandedCentersAdapter(currentUser.getDose(), currentUser.getSelectedCentersArray(),this);

        toolbar.setTitle(currentUser.getName());
        addNewCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCentersActivity.class);
                intent.putExtra("currentUserId", currentUser.getUserId());
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.profileExpandedRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(profileExpandedCentersAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        profileExpandedCentersAdapter.notifyDataSetChanged();
        startChecking(currentUser);
    }


    private void startChecking(final User currentUser) {
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("profileexpstartchecking", currentUser.getSelectedCentersArray().size() + "");
                requestQueue.cancelAll(this);
                newObjectRequest(currentUser);
                objectRequest.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(objectRequest);
            }
        }, 0, 1000 * 30);
    }

    private void newObjectRequest(final User currentUser) {
        objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL(currentUser.getDistrictId()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            Log.d("profileexpNewObject", currentUser.getSelectedCentersArray().size()+"");
                            JSONArray centers = (JSONArray) response.get("centers");
                            for (int i = 0; i < centers.length(); i++) {
                                ArrayList<Sessions> sessionsArrayList = new ArrayList<>();
                                JSONObject jsonObject = (JSONObject) centers.get(i);
                                JSONArray jsonArrayOfSessions = (JSONArray) jsonObject.get("sessions");
//                              Log.d("ProfileExpanded", "processReq: processing"+currentUser.getSelectedCentersArray().size());
                                for (int a = 0; a < currentUser.getSelectedCentersArray().size(); a++) {
                                    if (Integer.parseInt(jsonObject.get("center_id").toString()) == currentUser.getSelectedCentersArray().get(a).getCenter_id()) {
                                        Log.d("ProfileExpanded", "processReq: Found common centers");
                                        for (int b = 0; b < jsonArrayOfSessions.length(); b++) {
                                            JSONObject sessionsElement = (JSONObject) jsonArrayOfSessions.get(b);
                                            Sessions sessions = new Sessions(sessionsElement.get("session_id"), sessionsElement.get("date"), sessionsElement.get("available_capacity"), sessionsElement.get("min_age_limit"), sessionsElement.get("vaccine").toString(), sessionsElement.getJSONArray("slots"));
                                            if ((currentUser.getAge() == sessions.getMin_age())) {
                                                sessionsArrayList.add(sessions);
                                            }
                                        }
                                        selectedCenters.get(a).setSessions(sessionsArrayList);
                                        Log.d("profile", "onResponse: " + selectedCenters.get(a).getName());
                                        //todo if sessionsarraylist is empty add aa notifying data
                                        break;
                                    }
                                }
                            }
                            if (!(selectedCenters.size() == 0)) {
                                for (int c = 0; c < selectedCenters.size(); c++) {
                                    if (selectedCenters.get(c).getSessions().size() == 0) {
                                        Log.d("profile expan", "onResponse: nullsession");
                                        Sessions sessions = new Sessions("codeNullSessions", "22/06/2021", 0, 0, "No slots.", new JSONArray());
                                        ArrayList<Sessions> codeNullSession = new ArrayList<>();
                                        codeNullSession.add(sessions);
                                        selectedCenters.get(c).setSessions(codeNullSession);
                                    }
                                }
                                Log.d("profileexpanded", "onResponse: selectedCenters is not zero");
                                profileExpandedCentersAdapter = new ProfileExpandedCentersAdapter(currentUser.getDose(), selectedCenters, getApplicationContext());
                                profileExpandedCentersAdapter.notifyDataSetChanged();
                            }

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
    }


    private String URL(String districtId) {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(time);
        return "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/" + queryParameter() + districtId + "&date=" + date;
    }

    private String queryParameter() {
//        if (districtId.equals("defaultValue") || districtId.isEmpty()) {
//            return "calendarByPin?pincode=";
//        }
        return "calendarByDistrict?district_id=";
    }

    @Override
    protected void onDestroy() {
        t.purge();
        requestQueue.cancelAll(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        t.purge();
        requestQueue.cancelAll(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        profileExpandedCentersAdapter.notifyDataSetChanged();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("profileexpstartchecking", currentUser.getSelectedCentersArray().size() + "");
                requestQueue.cancelAll(this);
                newObjectRequest(currentUser);
                objectRequest.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(objectRequest);
            }
        }, 0, 1000 * 20);
        super.onResume();
    }
    //    private int valueParameter() {
//        return Integer.parseInt(thisUser.getDistrictId());
////        return  222;
//    }


}
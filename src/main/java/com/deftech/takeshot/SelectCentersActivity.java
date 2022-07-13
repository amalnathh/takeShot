package com.deftech.takeshot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deftech.takeshot.adapter.NearByCentersAdapter;
import com.deftech.takeshot.model.CentersIndividual;
import com.deftech.takeshot.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SelectCentersActivity extends AppCompatActivity {
    private static final String TAG = "SelectActivity";
    private ArrayList<CentersIndividual> selectedCenters;
    private NearByCentersAdapter adapter;
    private FloatingActionButton nextBtn;
    String lattitude;
    String longitude;
    Intent thisActivityIntent;
    SharedPreferences userDetailsArraySp;
    ArrayList<User> usersArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_centers);
        selectedCenters = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userDetailsArraySp = getSharedPreferences("userDetailsArraySp", Context.MODE_PRIVATE);
        nextBtn = findViewById(R.id.next_button);

        thisActivityIntent = getIntent();
        lattitude = thisActivityIntent.getStringExtra("lat");
        longitude = thisActivityIntent.getStringExtra("lon");

        if (TextUtils.isEmpty(thisActivityIntent.getStringExtra("isContinuedFromAddUser"))) {
            Log.d(TAG, "onCreate: isContinued is true");
            try {
                long currentUserId = thisActivityIntent.getLongExtra("currentUserId", 1000);
                Gson gson = new Gson();
                Type type = new TypeToken<List<User>>() {
                }.getType();
                String json = userDetailsArraySp.getString("UserArray", "");
                usersArrayList = gson.fromJson(json, type);
                for (int i = 0; i < usersArrayList.size(); i++) {
                    if (usersArrayList.get(i).getUserId() == currentUserId) {
                        lattitude = usersArrayList.get(i).getLattitude();
                        longitude = usersArrayList.get(i).getLongitude();
                        break;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
//            preferredCentersIdArray.add(999999999);
            //todo
        }

        Log.d(TAG, "onCreate: " + lattitude + " " + longitude);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!TextUtils.isEmpty(thisActivityIntent.getStringExtra("isContinuedFromAddUser"))) {
                    Snackbar.make(view, "Profile Created.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Preferrences changed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(thisActivityIntent.getStringExtra("isContinuedFromAddUser"))) {
                            long currentUserId = thisActivityIntent.getLongExtra("currentUserId", 1000);

                            Gson gson = new Gson();
                            Type type = new TypeToken<List<User>>() {
                            }.getType();
                            String json = userDetailsArraySp.getString("UserArray", "");
                            usersArrayList = gson.fromJson(json, type);
                            for (int i = 0; i < usersArrayList.size(); i++) {
                                if (usersArrayList.get(i).getUserId() == currentUserId) {
                                    Log.d(TAG, "run: data changed successfully");
                                    usersArrayList.get(i).setSelectedCentersArray(adapter.getSelectedCentersArray());
                                    SharedPreferences.Editor userDetailsArraySpEditor = userDetailsArraySp.edit();
                                    json = gson.toJson(usersArrayList);
                                    userDetailsArraySpEditor.putString("UserArray", json);
                                    userDetailsArraySpEditor.apply();
                                    finish();
                                    break;
                                } else {
                                    Snackbar.make(view, "Error! Data was tampered.", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }

                        } else {
                            User userDetails = new User(
                                    thisActivityIntent.getStringExtra("name"),
                                    System.currentTimeMillis(),
                                    lattitude,
                                    longitude,
                                    thisActivityIntent.getStringExtra("districtName"),
                                    thisActivityIntent.getStringExtra("stateName"),
                                    thisActivityIntent.getIntExtra("age", 18),
                                    thisActivityIntent.getStringExtra("doseName"),
                                    thisActivityIntent.getStringExtra("districtId"),
                                    thisActivityIntent.getIntExtra("userCode", 403),
                                    adapter.getSelectedCentersArray()
                            );

                            Gson gson = new Gson();
                            Type type = new TypeToken<List<User>>() {
                            }.getType();
                            String json = userDetailsArraySp.getString("UserArray", "");
                            if (!json.equals("")) {
                                usersArrayList = gson.fromJson(json, type);
                            } else {
                                usersArrayList = new ArrayList<>();
                            }
                            usersArrayList.add(userDetails);

                            SharedPreferences.Editor userDetailsArraySpEditor = userDetailsArraySp.edit();
                            json = gson.toJson(usersArrayList);
                            userDetailsArraySpEditor.putString("UserArray", json);
                            userDetailsArraySpEditor.apply();
                            if (NewUserActivity.NewUserA == null) {
                                AddUserActivity.NewUserA.finish();
                            } else {
                                NewUserActivity.NewUserA.finish();
                            }

                            Intent intent = new Intent(getApplicationContext(), TabbedActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                }, 800);
            }
        });

        recyclerView = findViewById(R.id.recycle_view_select_centers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getCentersFromLocation();
    }

    public void getCentersFromLocation() {
        Log.d(TAG, "getCentersFromLocation: called");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url(lattitude, longitude),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: success");
                            processingResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public String url(String lat, String lon) {
        return "https://cdn-api.co-vin.in/api/v2/appointment/centers/public/findByLatLong?lat=" + lat + "&long=" + lon;
    }

    public void processingResponse(JSONObject response) throws JSONException {
        JSONArray centers = (JSONArray) response.get("centers");
        for (int i = 0; i < centers.length(); i++) {
            ArrayList<CentersIndividual> nearbyCentersList = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) centers.get(i);
            Log.d(TAG, response.toString());
            CentersIndividual individualCenter
                    = new CentersIndividual(jsonObject.get("center_id"), jsonObject.get("name"), jsonObject.get("location"), jsonObject.get("state_name"), jsonObject.get("district_name"), jsonObject.get("block_name"), jsonObject.get("lat"), jsonObject.get("long"));
            nearbyCentersList.add(individualCenter);
            selectedCenters.add(individualCenter);
        }
        adapter = new NearByCentersAdapter(selectedCenters, lattitude, longitude);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_centers_menu, menu);
        MenuItem searchMenu = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setQueryHint("Center name or place");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
//        SearchView searchView = (SearchView) searchViewItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                adapter.getFilter().filter(s.toString());
//                return false;
//            }
//        });
        return true;
    }
//Lat and long by pincode
//    public void getLatAndLongByPincode(int pincode){
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        GeocoderRe
//    }
}
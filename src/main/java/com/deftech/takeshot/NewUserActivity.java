package com.deftech.takeshot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.location.LocationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deftech.takeshot.model.Districts;
import com.deftech.takeshot.model.States;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NewUserActivity extends AppCompatActivity {
    private static final String TAG = "new user activity";
    private static final int MY_PERMISSION_REQUEST_LOCATION = 99;
    private FloatingActionButton nextBtn;
    private SharedPreferences spUserArray;
    private RadioGroup radioGroupDose;

    double latitude;
    double longitude;
    private MaterialAutoCompleteTextView states;
    private MaterialAutoCompleteTextView district;
    public TextInputEditText lattitude_view;
    public TextInputEditText longitude_view;
    private TextInputEditText name;
    public TextInputEditText pincode;
    private String district_Id;
    private FloatingActionButton closeBtn;
    private String doseName;
    private String stateName;
    private  String districtName;
    private TextInputEditText ageInput;
    private FloatingActionButton accessLocation;
    public static Activity NewUserA;
    private FusedLocationProviderClient fusedLocationProviderClient;
SharedPreferences userDetailsArraySp;
    ArrayList<States> stateListWithId = new ArrayList<>();
    ArrayAdapter<States> stateadapter;
    ArrayList<Districts> districtListWithId = new ArrayList<>();
    ArrayAdapter<Districts> districtadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        nextBtn = findViewById(R.id.add_profile);
        name = findViewById(R.id.name);
        states = findViewById(R.id.stateSelect);
        district = findViewById(R.id.districtSelect);
        lattitude_view = findViewById(R.id.lattitude);
        longitude_view = findViewById(R.id.longitude);
        closeBtn = findViewById(R.id.imageView);
        accessLocation = findViewById(R.id.floatingActionButtonLocation);
        pincode = findViewById(R.id.pincode);
        radioGroupDose = findViewById(R.id.radioGroupDose);
        ageInput = findViewById(R.id.age);

//        userDetailsArraySp = getSharedPreferences("UserDetailsArray", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = userDetailsArraySp.edit();
//        Gson gson = new Gson();

        NewUserA = this;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        stateadapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, stateListWithId);
        districtadapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, districtListWithId);
        states.setAdapter(stateadapter);
        district.setAdapter(districtadapter);
        nextBtn.setEnabled(false);

        radioGroupDose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int subRdid = radioGroup.getCheckedRadioButtonId();
                RadioButton currBtn = findViewById(subRdid);
                doseName = (String) currBtn.getText();
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    nextBtn.setEnabled(false);
                } else {
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        longitude_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 4) {
                    nextBtn.setEnabled(false);
                } else {
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lattitude_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 4) {
                    nextBtn.setEnabled(false);
                } else {
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 6) {
                    nextBtn.setEnabled(false);
                } else {
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        district.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    nextBtn.setEnabled(false);
                } else {
                    nextBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        accessLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlocation();
            }
        });

        states.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                States states = (States) adapterView.getItemAtPosition(i);
                stateName=states.getName();
                Log.d(TAG, states.getName());
                String stateId = states.getId();
//                district.setText("");
                chooseDistrict(stateId);
            }
        });
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Districts districts = (Districts) adapterView.getItemAtPosition(i);
                districtName = districts.getName();
                district_Id = districts.getId();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spUserArray = getSharedPreferences("UsersArray", Context.MODE_PRIVATE);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCentersActivity.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("lat",latitude+"".toString());
                intent.putExtra("lon",longitude+"".toString());
                intent.putExtra("districtName",districtName.toString());
                intent.putExtra("stateName",stateName.toString());
                intent.putExtra("age",Integer.parseInt(ageInput.getText().toString()));
                intent.putExtra("doseName",doseName.toString());
                intent.putExtra("districtId",district_Id.toString());
                intent.putExtra("userCode",808);
                intent.putExtra("isContinuedFromAddUser","true");
                startActivity(intent);
            }
        });
        chooseState();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getlocation();
            }
        }, 2000);
    }


    private void getlocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getFusedLocation();
        } else {
            accessLocation.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getFusedLocation() {
        if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    try {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            processLatAndLong(location);
                            lattitude_view.setText(latitude + "");
                            longitude_view.setText(longitude + "");
                        } else {
                            LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                            boolean isEnabled = manager != null && LocationManagerCompat.isLocationEnabled(manager);
                            if (isEnabled) {
                                Log.d(TAG, "location is enabled");
                            } else {
                                Log.d(TAG, "location is disabled");
                            }
                            Snackbar.make(accessLocation, "Turn on your location", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            lattitude_view.setText(R.string.access_denied);
                            longitude_view.setText(R.string.access_denied);
                            Log.d(TAG, "getlocation: access denied");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(accessLocation, "Please turn on your location", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent enableGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(enableGPS);
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_LOCATION) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getFusedLocation();
            } else {
                Snackbar.make(accessLocation, "Turn on your location", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                lattitude_view.setText(R.string.access_denied);
                longitude_view.setText(R.string.access_denied);
                Log.d(TAG, "getlocation: access denied");
            }
        }
    }

    private void processLatAndLong(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        String statename = addresses.get(0).getAdminArea();
        final String districtName = addresses.get(0).getSubAdminArea();
        pincode.setText(addresses.get(0).getPostalCode());

        Log.d(TAG, "processLatAndLong: " + statename + " " + districtName);

        for (int i = 0; i < stateListWithId.size(); i++) {
            States state = stateListWithId.get(i);
            if (state.getName().toLowerCase().trim().contains(statename.toLowerCase().trim())) {
//                states.setListSelection(i);
                Log.d(TAG, "processLatAndLong: setting state");
                states.setInputType(InputType.TYPE_CLASS_TEXT);
                states.setText((CharSequence) stateListWithId.get(i).getName());
                stateName = stateListWithId.get(i).getName();
                chooseDistrict(state.getId());

                final Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (districtListWithId.size() != 0) {
                            for (int c = 0; c < districtListWithId.size(); c++) {
                                setDistrictNameOnView(districtListWithId.get(c), districtName);
                            }
                            timer.cancel();
                        }
                    }
                }, 0, 500);

            }
        }
    }

    private void setDistrictNameOnView(final Districts districts, final String district_Name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (districts.getName().toLowerCase().trim().contains(district_Name.toLowerCase().trim())) {
                    district.setInputType(InputType.TYPE_CLASS_TEXT);
                    districtName = districts.getName();
                    district.setText((CharSequence) districts.getName());
                    district_Id = districts.getId();
                }
            }
        });
    }

    public void chooseDistrict(String state_id) {
        districtListWithId.clear();
        districtadapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String URL = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/" + state_id;

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray districtslist = (JSONArray) response.get("districts");
                            for (int i = 0; i < districtslist.length(); i++) {
                                JSONObject jsonObject = (JSONObject) districtslist.get(i);
                                Districts districts = new Districts(jsonObject.get("district_id"), jsonObject.get("district_name"));
                                districtListWithId.add(districts);
                                districtadapter.notifyDataSetChanged();
                                Log.d(TAG, "onResponse:district " + "working");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void chooseState() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String URL = "https://cdn-api.co-vin.in/api/v2/admin/location/states";

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: success");
                            JSONArray stateslist = (JSONArray) response.get("states");
                            for (int i = 0; i < stateslist.length(); i++) {
                                JSONObject jsonObject = (JSONObject) stateslist.get(i);
                                States states = new States(jsonObject.get("state_id"), jsonObject.get("state_name"));
                                stateListWithId.add(states);
                            }
                            stateadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

}

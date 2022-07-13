package com.deftech.takeshot.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
import com.deftech.takeshot.R;
import com.deftech.takeshot.adapter.CheckVaccineAdapter;
import com.deftech.takeshot.model.Centers;
import com.deftech.takeshot.model.Districts;
import com.deftech.takeshot.model.Sessions;
import com.deftech.takeshot.model.States;
import com.deftech.takeshot.model.VaccCheckerModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VaccineChecker extends Fragment {
    private RequestQueue requestQueue;
    private JsonObjectRequest objectRequest;
    TextInputEditText searchInput;
    ArrayList<Centers> centersArrayList = new ArrayList<>();
    private CheckVaccineAdapter mAdapter;
    SharedPreferences userDetailsArraySp;
    ConstraintLayout progress;
    ConstraintLayout addStateAndDis;
    ConstraintLayout recyclerViewParent;
    private Timer t;
    boolean onlySlots;
    ArrayList<VaccCheckerModel> vcDetails;
    RecyclerView recyclerView;
    private MaterialAutoCompleteTextView states;
    private MaterialAutoCompleteTextView district;
    ArrayList<States> stateListWithId =new ArrayList<>();
    ArrayAdapter<States> stateadapter;
    ArrayList<Districts> districtListWithId =new ArrayList<>();
    ArrayAdapter<Districts> districtadapter;
    RadioGroup age;
    RadioGroup fee;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.vaccine_checker_fragment, container, false);
        userDetailsArraySp = getActivity().getSharedPreferences("userDetailsArraySp", Context.MODE_PRIVATE);
        t = new Timer();
        requestQueue = Volley.newRequestQueue(getActivity());
        setHasOptionsMenu(true);
        onlySlots = false;
        progress = rootView.findViewById(R.id.progressBarLayoutVaccineChecker);
        recyclerView = rootView.findViewById(R.id.recycler_view_check_vaccine);
        searchInput = rootView.findViewById(R.id.vaccine_checker_search_input);
        addStateAndDis = rootView.findViewById(R.id.addStateAndDis);
        states = rootView.findViewById(R.id.stateSelectVc);
        district = rootView.findViewById(R.id.districtSelectVc);
        recyclerViewParent = rootView.findViewById(R.id.recycler_view_parent_vc);
        age = rootView.findViewById(R.id.age);
        fee = rootView.findViewById(R.id.fee);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progress.setVisibility(View.VISIBLE);
        searchInput.setInputType(InputType.TYPE_NULL);


        stateadapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, stateListWithId);
        districtadapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, districtListWithId);
        states.setAdapter(stateadapter);
        district.setAdapter(districtadapter);

        mAdapter = new CheckVaccineAdapter(centersArrayList);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//setData();
        checkVaccinesFromThisFragment();
        return rootView;
    }

    public VaccineChecker() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.vaccine_checker_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("menu", "onOptionsItemSelected: called");
        int id = item.getItemId();
        if (id == R.id.available_centers_only) {
            availableCentersOnly(item.isChecked());
            return true;
        } else if (id == R.id.change_state_dis) {
            changeStateDis();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeStateDis() {
        setData();
    }

    private void availableCentersOnly(boolean checked) {
        onlySlots = checked;
    }

    @Override
    public void onDestroy() {
        t.purge();
        super.onDestroy();
    }

    public void checkVaccinesFromThisFragment() {
        vcDetails=new ArrayList<>();
        Gson gson = new Gson();
        recyclerView.setVisibility(View.GONE);
        String json = userDetailsArraySp.getString("VaccineCheckerDetails", "noValue");
        if (json.equals("noValue")) {
            setData();
        } else {
            Type type = new TypeToken<List<VaccCheckerModel>>(){}.getType();
            vcDetails = gson.fromJson(json, type);
            if(vcDetails.get(0).getAge()<45){
               RadioButton rd18 = rootView.findViewById(R.id.radio18);
               rd18.setChecked(true);
            }else {
                RadioButton rd45 = rootView.findViewById(R.id.radio45);
                rd45.setChecked(true);
            }
            if (vcDetails.get(0).getFee().equals("free")){
                RadioButton rdfree = rootView.findViewById(R.id.free);
                rdfree.setChecked(true);
            }else {
                RadioButton rdpaid = rootView.findViewById(R.id.paid);
                rdpaid.setChecked(true);
            }
            startTimedActivity();
        }
    }

    //setting data
    private void setData() {
        chooseState();
        progress.setVisibility(View.GONE);
        recyclerViewParent.setVisibility(View.GONE);
        addStateAndDis.setVisibility(View.VISIBLE);
        searchInput.setInputType(InputType.TYPE_NULL);
        states.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                States states = (States) adapterView.getItemAtPosition(i);
                String stateId = states.getId();
                chooseDistrict(stateId);
            }
        });
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Districts districts = (Districts) adapterView.getItemAtPosition(i);
                VaccCheckerModel vaccCheckerModel = new VaccCheckerModel(Integer.parseInt(districts.getId()), 18, "free");
                if (!(vcDetails.size()==0)){
                    vcDetails.clear();
                }
                vcDetails.add(vaccCheckerModel);
                SharedPreferences.Editor editor;
                editor = userDetailsArraySp.edit();
                Gson gson = new Gson();
                String json = gson.toJson(vcDetails);
                editor.putString("VaccineCheckerDetails", json);
                editor.apply();
                startTimedActivity();
            }
        });
    }

    public void chooseState() {
        stateadapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "https://cdn-api.co-vin.in/api/v2/admin/location/states";

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
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

    public void chooseDistrict(String state_id) {
        districtadapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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

    //Checking Vaccines
    private void startTimedActivity() {
        addStateAndDis.setVisibility(View.GONE);
        recyclerViewParent.setVisibility(View.VISIBLE);
        startChecking();
        age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int subRdid = radioGroup.getCheckedRadioButtonId();
                RadioButton currBtn = rootView.findViewById(subRdid);

                SharedPreferences.Editor editor;
                editor = userDetailsArraySp.edit();


                StringBuffer sb = new StringBuffer(currBtn.getText());
                sb.deleteCharAt(sb.length() - 1);
                Gson gson = new Gson();
                String json = userDetailsArraySp.getString("VaccineCheckerDetails", "noValue");
                Type type = new TypeToken<List<VaccCheckerModel>>(){}.getType();
                vcDetails = gson.fromJson(json, type);
                VaccCheckerModel newVcDetails = vcDetails.get(0);

                newVcDetails.setAge(Integer.parseInt(sb.toString()));
                vcDetails.clear();
                vcDetails.add(newVcDetails);
                String vcDetailsJson = gson.toJson(vcDetails);
                editor.putString("VaccineCheckerDetails", vcDetailsJson);
                editor.apply();
                t.purge();
                progress.setVisibility(View.VISIBLE);
                startChecking();
            }
        });
        fee.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int subRdid = radioGroup.getCheckedRadioButtonId();
                RadioButton currBtn = rootView.findViewById(subRdid);

                SharedPreferences.Editor editor;
                editor = userDetailsArraySp.edit();

                Gson gson = new Gson();
                String json = userDetailsArraySp.getString("VaccineCheckerDetails", "noValue");
                Type type = new TypeToken<List<VaccCheckerModel>>(){}.getType();
                vcDetails = gson.fromJson(json, type);
                VaccCheckerModel newVcDetails = vcDetails.get(0);


                newVcDetails.setFee(currBtn.getText().toString().toLowerCase());
                vcDetails.clear();
                vcDetails.add(newVcDetails);
                String vcDetailsJson = gson.toJson(vcDetails);
                editor.putString("VaccineCheckerDetails", vcDetailsJson);
                editor.apply();
                t.purge();
                progress.setVisibility(View.VISIBLE);
                startChecking();
            }
        });
    }

    private void startChecking() {
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestQueue.cancelAll(this);
                newObjectRequest();
                objectRequest.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(objectRequest);
            }
        }, 0, 1000 * 20);
    }

    private void newObjectRequest() {
        objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            centersArrayList.clear();
                            processReq(response);

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

    private void processReq(JSONObject response) throws JSONException {
        centersArrayList.clear();
        JSONArray centers = (JSONArray) response.get("centers");
        for (int i = 0; i < centers.length(); i++) {
            ArrayList<Sessions> sessionsArrayList = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) centers.get(i);
            JSONArray jsonArrayOfSessions = (JSONArray) jsonObject.get("sessions");
            for (int a = 0; a < jsonArrayOfSessions.length(); a++) {
                JSONObject sessionsElement = (JSONObject) jsonArrayOfSessions.get(a);
                Sessions sessions = new Sessions(sessionsElement.get("session_id"), sessionsElement.get("date"), sessionsElement.get("available_capacity"), sessionsElement.get("min_age_limit"), sessionsElement.get("vaccine").toString(), sessionsElement.getJSONArray("slots"));

                if (onlySlots) {
                    if ((vcDetails.get(0).getAge() == sessions.getMin_age()) && (vcDetails.get(0).getFee().equals(jsonObject.get("fee_type").toString().toLowerCase()))) {
                        if (!(sessions.getAvailable_capacity() == 0)) {
                            sessionsArrayList.add(sessions);
                        }
                    }
                } else {
                    if ((vcDetails.get(0).getAge() == sessions.getMin_age()) && (vcDetails.get(0).getFee().equals(jsonObject.get("fee_type").toString().toLowerCase()))) {
                        sessionsArrayList.add(sessions);
                    }
                }
            }
            try {
                if (!sessionsArrayList.isEmpty()) {
                    Centers center = new Centers(jsonObject.get("name"), jsonObject.get("block_name"), jsonObject.get("pincode"), jsonObject.get("fee_type"), jsonObject.get("address"), jsonObject.get("district_name"), sessionsArrayList);
                    centersArrayList.add(center);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        if (centersArrayList.isEmpty()) {
            ArrayList<Sessions> sessionsArrayListnull = new ArrayList<>();
            JSONArray jsonArrays = new JSONArray();
            jsonArrays.put("122");
            Sessions sessionNull = new Sessions("", "", 0, 2282, "", jsonArrays);
            sessionsArrayListnull.add(sessionNull);
            Centers center = new Centers("No centers found!", "", 0, "", "Click 🔍 button to notify when available.", "No slots found!", sessionsArrayListnull);
            centersArrayList.add(center);
        }
        progress.setVisibility(View.GONE);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        searchInput.setInputType(InputType.TYPE_CLASS_TEXT);
        mAdapter.notifyDataSetChanged();
    }


    private int valueParameter() {
//        if (pincode.equals("defaultValue") || pincode.isEmpty()) {
//            return districtId;
//        }
//        return pincode;
        return vcDetails.get(0).getDistrictId();
    }

    //
    private String URL() {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(time);
        return "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/" + queryParameter() + valueParameter() + "&date=" + date;
    }

    //
//
    private String queryParameter() {
//        if (districtId.equals("defaultValue") || districtId.isEmpty()) {
//            return "calendarByPin?pincode=";
//        }
        return "calendarByDistrict?district_id=";
    }
}

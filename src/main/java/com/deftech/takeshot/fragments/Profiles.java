package com.deftech.takeshot.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deftech.takeshot.AddUserActivity;
import com.deftech.takeshot.R;
import com.deftech.takeshot.adapter.ProfilesAdapter;
import com.deftech.takeshot.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Profiles extends Fragment {
    FloatingActionButton addNewProfileBtn;
    SharedPreferences userDetailsArraySp;
    ArrayList<User> usersArrayList = new ArrayList<>();
    ProfilesAdapter profilesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.profiles_fragment, container, false);
        setProfileAdapter();
        addNewProfileBtn = view.findViewById(R.id.addNewProfileFromProfileFrag);
        RecyclerView recyclerView = view.findViewById(R.id.profilesRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(profilesAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        addNewProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddUserActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void setProfileAdapter() {
        userDetailsArraySp = getActivity().getSharedPreferences("userDetailsArraySp", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<List<User>>() {
        }.getType();
        String json = userDetailsArraySp.getString("UserArray", "");
        usersArrayList = gson.fromJson(json, type);
        profilesAdapter = new ProfilesAdapter(usersArrayList);
        profilesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        setProfileAdapter();
        super.onResume();
    }
}

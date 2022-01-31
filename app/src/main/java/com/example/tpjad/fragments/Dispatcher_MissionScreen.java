package com.example.tpjad.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tpjad.R;
import com.example.tpjad.adapters.SpinAdapter;
import com.example.tpjad.api.Api;
import com.example.tpjad.api.Service;
import com.example.tpjad.dto.MissionDTO;
import com.example.tpjad.model.Mission;
import com.example.tpjad.model.Team;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dispatcher_MissionScreen extends Fragment implements AdapterView.OnItemSelectedListener {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String TOKEN_KEY = "token_key";
    public static final String TEAMS_KEY = "teams_key";
    SharedPreferences sharedpreferences;
    private SpinAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dispatcher_mission, container, false);
    }
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Spinner element
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        sharedpreferences =  this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = new Gson().fromJson(sharedpreferences.getString(TOKEN_KEY, null), String.class);
        Service service = Api.createService(Service.class, token);
        Call<List<Team>> callAsync = service.findAllTeams();
        callAsync.enqueue(new Callback<List<Team>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                List<Team> teams = response.body();
                List<Team> aTeams = (List<Team>) teams.stream().filter(team -> {
                    return team.getStatus().equals("available");
                }).collect(Collectors.toList());

                //give teams to key thing
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TEAMS_KEY, new Gson().toJson(aTeams));
                editor.apply();
                // Spinner Drop down elements


                adapter = new SpinAdapter(getActivity(),android.R.layout.simple_spinner_item, aTeams);

                // Drop down layout style - list view with radio button
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(adapter);

            }
            @Override
            public void onFailure(Call<List<Team>> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });


        EditText location = view.findViewById(R.id.missionLocation);
        EditText details = view.findViewById(R.id.missionDetails);
        Button addMission = view.findViewById(R.id.addMission);
        addMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // server
                MissionDTO mission = new MissionDTO();

                String loc = location.getText().toString();
                String det = details.getText().toString();
                mission.setLocation(loc);
                mission.setDescription(det);

                Team selectedTeam = (Team)spinner.getSelectedItem();
                mission.setTeam(selectedTeam.getId());

                mission.setStatus("in progress");
                Service service = Api.createService(Service.class, null);
                Call<Mission> callAsync = service.createMission(mission);
                callAsync.enqueue(new Callback<Mission>() {
                    @Override
                    public void onResponse(Call<Mission> call, Response<Mission> response) {
                        Toast.makeText(getActivity().getApplicationContext(), "Mission Added", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<Mission> call, Throwable throwable) {
                        System.out.println(throwable);
                    }
                });

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
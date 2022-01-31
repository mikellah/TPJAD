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
import android.widget.Button;
import android.widget.EditText;

import com.example.tpjad.R;
import com.example.tpjad.api.Api;
import com.example.tpjad.api.Service;
import com.example.tpjad.dto.RequestString;
import com.example.tpjad.model.Mission;
import com.example.tpjad.model.UserEntity;
import com.google.gson.Gson;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Agent_MissionScreen extends Fragment {
    public static final String SHARED_PREFS = "shared_prefs";

    public static final String TOKEN_KEY = "token_key";
    public static final String USER_KEY = "user_key";
    private Mission currentMission;

    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agent_mission, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //EditTexts here:
        EditText location = view.findViewById(R.id.mLocation);
        EditText details = view.findViewById(R.id.mDetails);
        Button status = view.findViewById(R.id.mStatus);
        EditText mission = view.findViewById(R.id.currentMission);

        // Setup any handles to view objects here

        sharedpreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        UserEntity user;
        user = new Gson().fromJson(sharedpreferences.getString(USER_KEY, null), UserEntity.class);
        String token = new Gson().fromJson(sharedpreferences.getString(TOKEN_KEY, null), String.class);
        Service service = Api.createService(Service.class, token);
        Call<List<Mission>> callAsync = service.getMission();
        callAsync.enqueue(new Callback<List<Mission>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                List<Mission> missions = response.body();

                Optional<Mission> m = missions.stream().filter(mission -> { return mission.getTeam().getId().equals(user.getTeam().getId()) && mission.getStatus().equals("in progress");
                }).findFirst();
                if(m.isPresent()){
                    currentMission=m.get();
                    location.setVisibility(View.VISIBLE);
                    details.setVisibility(View.VISIBLE);
                    status.setVisibility(View.VISIBLE);
                    location.setText(currentMission.getLocation());
                    details.setText(currentMission.getDescription());
                }
                else {
                    mission.setText("No current Mission");
                    location.setVisibility(View.INVISIBLE);
                    details.setVisibility(View.INVISIBLE);
                    status.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<Mission>> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Mission> callAsync = service.changeStatus(currentMission.getId(), new RequestString("finished"));
                callAsync.enqueue(new Callback<Mission>(){
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<Mission> call, Response<Mission> response) {
                        Mission missions = response.body();
                        mission.setText("No current Mission");
                        location.setVisibility(View.INVISIBLE);
                        details.setVisibility(View.INVISIBLE);
                        status.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Mission> call, Throwable throwable) {
                        System.out.println(throwable);
                    }
                });
            }
        });
    }
}

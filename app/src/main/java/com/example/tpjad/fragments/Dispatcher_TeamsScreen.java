package com.example.tpjad.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tpjad.adapters.ListAdapter;
import com.example.tpjad.api.Api;
import com.example.tpjad.api.Service;
import com.example.tpjad.databinding.FragmentDispatcherTeamsBinding;
import com.example.tpjad.model.Team;
import com.example.tpjad.model.UserEntity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dispatcher_TeamsScreen extends Fragment {

    FragmentDispatcherTeamsBinding binding;
    public static final String SHARED_PREFS = "shared_prefs";

    public static final String TOKEN_KEY = "token_key";
    public static final String USER_KEY = "user_key";

    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup any handles to view objects here

        sharedpreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        binding = FragmentDispatcherTeamsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();

        UserEntity user;
        user = new Gson().fromJson(sharedpreferences.getString(USER_KEY, null), UserEntity.class);
        String token = new Gson().fromJson(sharedpreferences.getString(TOKEN_KEY, null), String.class);
        Service service = Api.createService(Service.class, token);
        Call<List<Team>> callAsync = service.findAllTeams();

        callAsync.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                List<Team> teams = response.body();

                ListAdapter listAdapter = new ListAdapter(getActivity(), (ArrayList<Team>) teams);
                binding.listview.setAdapter(listAdapter);

            }
            @Override
            public void onFailure(Call<List<Team>> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
        return v;
    }

}
package com.example.tpjad.api;

import com.example.tpjad.dto.MissionDTO;
import com.example.tpjad.dto.RequestString;
import com.example.tpjad.model.LoginResponse;
import com.example.tpjad.model.Mission;
import com.example.tpjad.model.Team;
import com.example.tpjad.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Service {

    @GET("/api/teams")
    public Call<List<Team>> findAllTeams();

    @POST("/login")
    public Call<LoginResponse> login(@Body User user);

    @POST("/api/missions")
    public Call<Mission> createMission(@Body MissionDTO missionDTO);

    @GET("/api/missions")
    public Call<List<Mission>> getMission();

    @PUT("/api/missions/{id}/status")
    public Call<Mission> changeStatus(@Path("id") String id, @Body RequestString status);

}

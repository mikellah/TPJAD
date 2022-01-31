package com.example.tpjad.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tpjad.activities.MainActivity;
import com.example.tpjad.R;
import com.example.tpjad.model.UserEntity;
import com.google.gson.Gson;

public class Dispatcher_WelcomeScreen extends Fragment {

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String EMAIL_KEY = "email_key";

    // key for storing password.
    public static final String PASSWORD_KEY = "password_key";

    public static final String TOKEN_KEY = "token_key";

    public static final String USER_KEY = "user_key";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email;
    UserEntity user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dispatcher_welcome, container, false);
    }

    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        // initializing our shared preferences.
        sharedpreferences =  this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // getting data from shared prefs and
        // storing it in our string variable.
        user = new Gson().fromJson(sharedpreferences.getString(USER_KEY, null), UserEntity.class);

        // initializing our textview and button.
        TextView welcomeTV = view.findViewById(R.id.idTVWelcome1);
        welcomeTV.setText("Welcome dispatcher \n"+ user.getName());
        Button logoutBtn = view.findViewById(R.id.idBtnLogout1);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to edit values in shared prefs.
                SharedPreferences.Editor editor = sharedpreferences.edit();

                // below line will clear
                // the data in shared prefs.
                editor.clear();

                // below line will apply empty
                // data to shared prefs.
                editor.apply();

                // starting mainactivity after
                // clearing values in shared preferences.
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
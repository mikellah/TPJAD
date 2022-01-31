package com.example.tpjad.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpjad.R;
import com.example.tpjad.api.Api;
import com.example.tpjad.api.Service;
import com.example.tpjad.model.LoginResponse;
import com.example.tpjad.model.User;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

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
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing EditTexts and our Button
        EditText emailEdt = findViewById(R.id.idEdtEmail);
        EditText passwordEdt = findViewById(R.id.idEdtPassword);
        Button loginBtn = findViewById(R.id.idBtnLogin);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // in shared prefs inside het string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
        email = sharedpreferences.getString(EMAIL_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY, null);

        // calling on click listener for login button.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to check if the user fields are empty or not.
                if (TextUtils.isEmpty(emailEdt.getText().toString()) && TextUtils.isEmpty(passwordEdt.getText().toString())) {
                    // this method will call when email and password fields are empty.
                    Toast.makeText(MainActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    String user = emailEdt.getText().toString();
                    String pass = passwordEdt.getText().toString();

                    // server
                    User u = new User(user,pass);

                    Service service = Api.createService(Service.class, null);
                    Call<LoginResponse> callAsync = service.login(u);

                    callAsync.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse loginResponse = response.body();
                            System.out.println("Works, user" + user + "\n");
                            editor.putString(TOKEN_KEY, loginResponse.getToken());
                            editor.putString(USER_KEY, new Gson().toJson(loginResponse.getUser()));
                            editor.apply();

                            if (loginResponse.getUser().getRole().getAuthority().equals("DISPATCHER")) {
                                Intent home = new Intent(MainActivity.this, MainActivity_Dispatcher.class);
                                startActivity(home);

                            } else {
                                Intent home2 = new Intent(MainActivity.this, MainActivity_Agent.class);
                                startActivity(home2);
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            System.out.println(throwable);
                        }
                    });

                }
            }
        });
    }


}

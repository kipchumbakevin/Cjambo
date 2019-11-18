package com.example.cjambo.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cjambo.ui.MainActivity;
import com.example.cjambo.R;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText emailUser,passwordpass;
    private static final String KEY_USERNAME = "email";
    private static final String KEY_STATUS = "status";
    RelativeLayout progressLyt;
    private static final String KEY_MESSAGE = "message";
    private static final String login_url = "http://cjambo.ampleshelter.com/dashboard/databaseFiles/login.php";
//    private com.example.cjambo.SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        session = new com.example.cjambo.SessionHandler(getApplicationContext());
//        if(session.isLoggedIn()){
//            loadDashboard();
//        }
        login = findViewById(R.id.login);
        emailUser = findViewById(R.id.email);
        progressLyt = findViewById(R.id.progressLoad);
        passwordpass = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = emailUser.getText().toString();
                String pass = passwordpass.getText().toString();
                if (emailAddress.isEmpty()){
                    emailUser.setError("Required");
                }if(pass.isEmpty()){
                    passwordpass.setError("Required");
                }else{
                    loginUser(emailAddress,pass);
                    Log.d("sss", emailAddress + pass);

                }

            }
        });
    }
    private void loginUser(final String email, final String password) {
        showProgress();
        emailUser.getText().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1")){

                        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        passwordpass.getText().clear();
                        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,"catch",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(LoginActivity.this,"damn",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showProgress(){
        progressLyt.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        progressLyt.setVisibility(View.GONE);
    }
}

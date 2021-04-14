package com.example.planet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText TextEmail;
    EditText TextPassword;
    TextView lblMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextEmail = findViewById(R.id.TextEmail);closeContextMenu();closeContextMenu();
        TextPassword = findViewById(R.id.TextPassword);
        lblMsg = findViewById(R.id.lblMsg);
    }

    public void loginEvent(View view) throws JSONException {
        String email= TextEmail.getText().toString();
        String password= TextPassword.getText().toString();
        String url= "https://android.parthrai.ca/api/android/login";

        JSONObject payload= new JSONObject();
        payload.put("email",email);
        payload.put("password",password);

        JSONArray arr_payload= new JSONArray();
        arr_payload.put(payload);

        RequestQueue queue= Volley.newRequestQueue(this);

        JsonArrayRequest request= new JsonArrayRequest(Request.Method.POST, url, arr_payload, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length()== 0){
                    lblMsg.setText("Invalid email or password!");
                    return;
                }

                try {
                    JSONObject user = response.getJSONObject(0);
//                    Log.d("name", user.get("name").toString());
                    startDashboardIntent(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.toString());

            }

    });
        queue.add(request);


    }

    public void registerEvent(View view) {
        Intent registrationIntent = new Intent(this, RegistrationActivity.class);
        startActivity(registrationIntent);
    }

    public void startDashboardIntent(JSONObject user){
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        dashboardIntent.putExtra("UserInfo", user.toString());
        startActivity(dashboardIntent);

    }

}
package com.example.planet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {
    EditText TextName;
    EditText Textemail;
    EditText TextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextName= findViewById(R.id.TextName);
        Textemail= findViewById(R.id.TextEmail);
        TextPassword= findViewById(R.id.TextPassword);
    }

    public void registerEvent(View view) {

        String name= TextName.getText().toString();
        String email= Textemail.getText().toString();
        String password= TextPassword.getText().toString();

        RequestQueue queue= Volley.newRequestQueue(this);
        String url="https://android.parthrai.ca/api/android/register";

        JSONObject payload= new JSONObject();

        try {
            payload.put("name",name);
            payload.put("email",email);
            payload.put("password", password);
            payload.put("dev","Rafael");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest register= new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());

            }
        });

    queue.add(register);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);

    }
}
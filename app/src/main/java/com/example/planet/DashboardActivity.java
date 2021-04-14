package com.example.planet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.api.Context;
import com.google.api.services.translate.Translate;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.google.api.services.translate.Translate.*;

public class DashboardActivity extends AppCompatActivity {

    private String originalText;
    private String translatedText;
    private EditText lang1;
    private TextView lang2;
    private boolean connected;
    com.google.cloud.translate.Translate translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent i= getIntent();

        try {
            JSONObject userObj= new JSONObject(i.getStringExtra("UserInfo"));
            TextView lblWelcome= findViewById(R.id.lblWelcome);
            lblWelcome.setText("Welcome " + userObj.get("name").toString() +"!");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        lang1 = findViewById(R.id.lang1);
        lang2 = findViewById(R.id.lang2);
        Button Translate = findViewById(R.id.Translate);


        Translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInternetConnection()) {

                    getTranslateService();
                    translate();

                } else {
                    lang2.setText(getResources().getString(R.string.no_connection));
                }
            }
        });
    }
    public void getTranslateService(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream  is = getResources().openRawResource(R.raw.credentials)) {

            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            TranslateOptions translateOptions= TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate =  translateOptions.getService();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void translate(){

        originalText = lang1.getText().toString();
        Translation translation = translate.translate (originalText, translate.("tr"), Translate.TranslateOption.model("base"));
        translatedText =translation.getTranslatedText();

        lang2.setText(translatedText);
    }

 public boolean checkInternetConnection() {
     ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTVITY_SERVICE);
     connected= ConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||
             ConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED;

     return connected;

    }


}
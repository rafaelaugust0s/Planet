package com.example.planet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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
import java.util.Locale;

import static com.google.api.services.translate.Translate.*;

public class DashboardActivity extends AppCompatActivity {

    private String originalText;
    private String translatedText;
    private EditText lang1;
    private TextView lang2;
    private boolean connected;
    com.google.cloud.translate.Translate translate;
    private TextToSpeech tts;
    private Button mButtonSpeak;
    private SeekBar pitch;
    private SeekBar speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent i = getIntent();

        try {
            JSONObject userObj = new JSONObject(i.getStringExtra("UserInfo"));
            TextView lblWelcome = findViewById(R.id.lblWelcome);
            lblWelcome.setText("Welcome " + userObj.get("name").toString() + "!");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        mButtonSpeak = findViewById(R.id.BtnSpeak);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.FRENCH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("tts", "Language not supported");
                    } else {
                        mButtonSpeak.setEnabled(true);

                    }
                } else {
                    Log.e("tts", "Initialization failed!");

                }
            }

        });
         lang2 = findViewById(R.id.lang2);
         pitch = findViewById(R.id.pitch_bar);
         speed = findViewById(R.id.speed_bar);
        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });


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

    private void speak(){
        String text = lang2.getText().toString();
        float p = (float)pitch.getProgress()/50;
        if(p < 0.1) p = 0.1f;
        float s = (float)speed.getProgress()/50;
        if(s < 0.1) s = 0.1f;

        tts.setPitch(p);
        tts.setSpeechRate(s);

        tts.speak(text,TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    protected void onDestroy() {

        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void translate() {


        originalText = lang1.getText().toString();
        Translation translation = translate.translate(originalText, com.google.cloud.translate.Translate.TranslateOption.targetLanguage("fr"), com.google.cloud.translate.Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();

        lang2.setText(translatedText);
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
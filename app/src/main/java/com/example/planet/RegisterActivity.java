package com.example.planet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText TextName;
    EditText Textemail;
    EditText TextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        TextName= findViewById(R.id.TextName);
        Textemail= findViewById(R.id.TextEmail);
        TextPassword= findViewById(R.id.TextPassword);


    }
    public void btnRegisterClick(View view){


}
}
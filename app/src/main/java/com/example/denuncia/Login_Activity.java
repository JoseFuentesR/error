package com.example.denuncia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Registro(View view) {
        Intent intent = new Intent(this,Register_Activity.class);
        startActivity(intent);
        fileList();
    }

    public void login(View view) {
    }

    public void login_google(View view) {
    }
}
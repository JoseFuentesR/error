package com.example.denuncia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Register_Activity extends AppCompatActivity {

    EditText txtemail,txtnombre,txtrut,txtpass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtemail = findViewById(R.id.register_email);
        txtnombre = findViewById(R.id.register_nombre);
        txtrut = findViewById(R.id.registar_rut);
        txtpass = findViewById(R.id.register_contraseña);

        mAuth = FirebaseAuth.getInstance();
    }

    public void CreateAccount(View view) {
        String email,nombre,rut,pass;
        email = txtemail.getText().toString();
        nombre = txtnombre.getText().toString();
        rut = txtrut.getText().toString();
        pass = txtpass.getText().toString();

        if (email.isEmpty() || nombre.isEmpty() || rut.isEmpty() || pass.isEmpty()){
            Toast.makeText(this,"Complete la informacion",Toast.LENGTH_LONG).show();
        }else{


        }
    }

    public void Launchloggin(View view) {
        Intent  intent = new Intent(this,Login_Activity.class);
        startActivity(intent);
        finish();//destroy login
    }
}
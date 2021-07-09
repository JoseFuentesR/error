package com.example.denuncia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {
    EditText txt_email,txt_pass;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_email = findViewById(R.id.login_email);
        txt_pass  = findViewById(R.id.login_pass);

        auth = FirebaseAuth.getInstance();
    }


    public void Registro(View view) {
        Intent intent = new Intent(this,Register_Activity.class);
        startActivity(intent);
        fileList();
}

    public void login(View view) {
        String email = txt_email.getText().toString();
        String pass  = txt_pass.getText().toString();

        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(Login_Activity.this, "Complete la informacion", Toast.LENGTH_LONG).show();
        }else {
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //cargar perfil
                        Intent intent = new Intent(Login_Activity.this, perfil_Activity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        String msg = task.getException().getMessage();
                        Toast.makeText(Login_Activity.this, msg, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }//final de verificado de login
        }

    public void login_google(View view) {
    }
}

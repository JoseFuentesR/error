package com.example.denuncia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import com.example.denuncia.model.Encargado;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Login_Activity extends AppCompatActivity {
    EditText txt_email, txt_pass;
    FirebaseAuth auth;
    List<Encargado> list;
    ProgressDialog prograsD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_email = findViewById(R.id.login_email);
        txt_pass = findViewById(R.id.login_pass);
        prograsD = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
    }

    //dirige al reguistro previamente verificanco conceccion a internet
    public void Registro(View view) {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            Intent intent = new Intent(this, Register_Activity.class);
            startActivity(intent);
            fileList();//destruye esta actividad
        }else {
            Toast.makeText(getApplicationContext(),"No tiene coneccion a internet",Toast.LENGTH_LONG).show();
        }
    }

    public void login(View view) {

        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //Verifica conceccion a internet

        if (networkInfo!=null && networkInfo.isConnectedOrConnecting()) {
            String email = txt_email.getText().toString();
            String pass = txt_pass.getText().toString();

            //verifica si los campos de contraceña y email
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(Login_Activity.this, "Complete la informacion", Toast.LENGTH_LONG).show();
            } else {
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    prograsD.setTitle("Cargando .....");
                                    prograsD.setMessage("Ingresando");
                                    prograsD.setCancelable(false);
                                    prograsD.show();

                                    String uid = auth.getCurrentUser().getUid();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("Encargado");


                                    //hace la verificacion si la cuenta es de encargado o usuario y redirecciona a su respectivo perfi
                                    myRef.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Intent intent = new Intent(Login_Activity.this, perfil_Activity_encargado.class);
                                                startActivity(intent);
                                                finish();
                                                prograsD.dismiss();
                                            } else {
                                                Intent intent = new Intent(Login_Activity.this, perfil_Activity.class);
                                                startActivity(intent);
                                                finish();
                                                prograsD.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                        }
                                    });

                                } else {
                                    String msg = task.getException().getMessage();
                                    Toast.makeText(Login_Activity.this, msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }//final de verificado de login
        }else {
            Toast.makeText(getApplicationContext(),"No tiene coneccion a internet",Toast.LENGTH_LONG).show();
        }
    }

}

package com.example.denuncia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.denuncia.model.Usuarios;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public boolean hay_internet(){
        boolean conneted = false;
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        Network [] networks = connectivityManager.getAllNetworks();
        for (Network network : networks){
            NetworkInfo into = connectivityManager.getNetworkInfo(network);
            if (into.isConnected()){
                conneted = true;
            }
        }
        return conneted;
    }

    public void registro(View view) {
        Intent intent = new Intent(this,Register_Activity.class);
        startActivity(intent);
        fileList();//destruye esta actividad(login)
    }



    //public void BotonGoogle(View view) {
    //Snackbar.make(view,"replace with your own action",Snackbar.LENGTH_LONG)
    //        .setAction("action",null).show();
    //    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //    DatabaseReference myRef = database.getReference("Ususario");
    //    Usuarios usuarios = new Usuarios();
    //    usuarios.setRut(193867391);
    //    usuarios.setNombres_apellidos("Javier puentes fabbri");
    //    usuarios.setCorreo("javieripf12@live.com");
    //    usuarios.setPassword("pepe1234");
    //   myRef.push().setValue(usuarios);
    //}
}
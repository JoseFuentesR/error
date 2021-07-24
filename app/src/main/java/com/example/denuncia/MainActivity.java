package com.example.denuncia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://denunciaciudadana-2798d-default-rtdb.firebaseio.com/";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //dirige al reguistro previamente verificanco conceccion a internet
    public void registro(View view) {
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

    //dirige al login previamente verificanco conceccion a internet
    public void login_main(View view) {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            Intent intent = new Intent(MainActivity.this, Login_Activity.class);
            startActivity(intent);
            fileList();//destruye esta actividad
        }else {
            Toast.makeText(getApplicationContext(),"No tiene coneccion a internet",Toast.LENGTH_LONG).show();
        }
    }

}



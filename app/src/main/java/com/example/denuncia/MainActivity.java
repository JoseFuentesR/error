package com.example.denuncia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

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





}
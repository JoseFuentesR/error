package com.example.denuncia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.denuncia.model.Usuarios;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String URL= "https://denunciaciudadana-2798d-default-rtdb.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processHttp();
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

    public void processHttp(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String data = new String(responseBody);
                Log.d("INFO", data);
                
                processUsers(data);
            }

            public void processUsers(String data) {
                try {
                    JSONObject usuarios = new JSONObject(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void login_main(View view) {
        Intent intent = new Intent(MainActivity.this,Login_Activity.class);
        startActivity(intent);
        fileList();//destruye esta actividad
    }

    public void login_main_google(View view) {
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
package com.example.denuncia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.denuncia.model.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class perfil_Activity_encargado extends Activity {
    TextView txtnombre,txtemail,txtrut;
    FirebaseAuth auth;
    List<Usuarios> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_encargado);

        txtnombre = findViewById(R.id.txtnombre);
        txtrut = findViewById(R.id.txtrut);
        txtemail = findViewById(R.id.txtemail);


        auth = FirebaseAuth.getInstance();
        String uid =  auth.getCurrentUser().getUid();
        list = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Encargado");
        //Muestra en el perfil nombre rut y correo del encargado
        myRef.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Usuarios usuarios = ds.getValue(Usuarios.class);
                        list.add(usuarios);

                    }
                    for (Usuarios u : list) {
                        txtnombre.setText(u.getNombres_apellidos());
                        txtemail.setText(u.getCorreo());
                        txtrut.setText(u.getRut());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    //redirige a crear denuncia
    public void crear_denuncias(View view) {
        Intent intent = new Intent(perfil_Activity_encargado.this,denuncia_Activity_encargado.class);
        startActivity(intent);
        finish();

    }
    //redirige a ver denuncias encargado
    public void verDenuncias(View view) {
        Intent intent = new Intent(perfil_Activity_encargado.this,ver_denuncias_encargado.class);
        startActivity(intent);
        finish();
    }
    //redirige a crear cuenta de encargado
    public void Registrar_encargado(View view) {
        Intent intent = new Intent(this,Register_Activity_encargado.class);
        startActivity(intent);
        fileList();//destruye esta actividad
    }
}

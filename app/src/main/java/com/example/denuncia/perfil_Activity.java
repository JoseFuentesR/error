package com.example.denuncia;

import androidx.appcompat.app.AppCompatActivity;

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

public class perfil_Activity extends AppCompatActivity {
    TextView txtnombre,txtemail,txtrut;
    FirebaseAuth auth;
    List<Usuarios> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        txtnombre = findViewById(R.id.txtnombre);
        txtrut = findViewById(R.id.txtrut);
        txtemail = findViewById(R.id.txtemail);
        auth = FirebaseAuth.getInstance();
        String uid =  auth.getCurrentUser().getUid();
        list = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios");
        //muesta en el perfil el nombre rut y correo del usuario

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

    //redirige a crear denuncias
    public void crear_denuncias(View view) {
        Intent intent = new Intent(perfil_Activity.this,denuncia_Activity.class);
        startActivity(intent);
        finish();

    }
    //redirige a ver denuncias
    public void verDenuncias(View view) {
        Intent intent = new Intent(perfil_Activity.this,ver_denuncias.class);
        startActivity(intent);
        finish();
    }
}
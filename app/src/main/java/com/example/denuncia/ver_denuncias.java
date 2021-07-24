package com.example.denuncia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.denuncia.adapter.DenunciaAdapter;
import com.example.denuncia.adapter.DenunciaService;
import com.example.denuncia.model.Denuncia;
import com.example.denuncia.model.Encargado;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ver_denuncias extends AppCompatActivity {

    RecyclerView rc;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_denuncias);
        firebaseAuth        = FirebaseAuth.getInstance();
        rc = findViewById(R.id.rc);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        rc.setLayoutManager(lm);
        DenunciaAdapter adapter = new DenunciaAdapter(DenunciaService.denuncias,R.layout.item,this);
        rc.setAdapter(adapter);
        DenunciaService.denuncias.clear();
        cargaDatosFirebase();

    }

    //carga las denuncias del usuario de la base de datos
    public void cargaDatosFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference myRef = database.getReference("Denuncias").child(uid);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Denuncia denuncia = snapshot.getValue(Denuncia.class);

                if (!DenunciaService.denuncias.contains(denuncia)) {
                    DenunciaService.addDenuncia(denuncia);
                }
                    rc.getAdapter().notifyDataSetChanged();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Denuncia denuncia = snapshot.getValue(Denuncia.class);
                if (DenunciaService.denuncias.contains(denuncia)) {
                    DenunciaService.updateDenuncia(denuncia);
                }
                rc.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //redirige al perfil
    public void perfil(View view) {
        Intent intent = new Intent(ver_denuncias.this, perfil_Activity.class);
        startActivity(intent);
        finish();

    }
}
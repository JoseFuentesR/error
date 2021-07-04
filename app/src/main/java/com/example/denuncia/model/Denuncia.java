package com.example.denuncia.model;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.denuncia.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class Denuncia extends AppCompatActivity {

    private int id;
    private String tipo_denuncia;
    private String Estado_denuncia;
    private String Denuncia_tipo;
    private Date fecha;
    private String denuncia_detalles;

    private Button mUploadbtn;
    private StorageReference mStorage;

    private static final int GALLERY_INTENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        mStorage = FirebaseStorage.getInstance().getReference();

        mUploadbtn = (Button) findViewById(R.id.boton_subir);

        mUploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
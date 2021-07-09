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
    private Usuarios usuarios;

    public Denuncia(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo_denuncia() {
        return tipo_denuncia;
    }

    public void setTipo_denuncia(String tipo_denuncia) {
        this.tipo_denuncia = tipo_denuncia;
    }

    public String getEstado_denuncia() {
        return Estado_denuncia;
    }

    public void setEstado_denuncia(String estado_denuncia) {
        Estado_denuncia = estado_denuncia;
    }

    public String getDenuncia_tipo() {
        return Denuncia_tipo;
    }

    public void setDenuncia_tipo(String denuncia_tipo) {
        Denuncia_tipo = denuncia_tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDenuncia_detalles() {
        return denuncia_detalles;
    }

    public void setDenuncia_detalles(String denuncia_detalles) {
        this.denuncia_detalles = denuncia_detalles;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }



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


}
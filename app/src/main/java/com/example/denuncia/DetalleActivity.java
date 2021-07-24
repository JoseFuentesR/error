package com.example.denuncia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.denuncia.model.Tipo_denuncia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetalleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        TextView txtnombre,txtdireccion,txtdetalle,txttipo,txtestado;

        String nombre = getIntent().getStringExtra("NOMBRE");
        String direccion = getIntent().getStringExtra("DIRECCION");
        String detalle = getIntent().getStringExtra("DETALLE");
        String tipo = getIntent().getStringExtra("TIPO");

        txtnombre = findViewById(R.id.tituloD);
        txtdireccion = findViewById(R.id.direccionD);
        txtdetalle = findViewById(R.id.detalleD);
        txttipo = findViewById(R.id.tipoD);

        txttipo.setText(tipo);
        txtnombre.setText(nombre);
        txtdireccion.setText(direccion);
        txtdetalle.setText(detalle);






    }

    public void guardar_denuncia(View view) {
    }
}
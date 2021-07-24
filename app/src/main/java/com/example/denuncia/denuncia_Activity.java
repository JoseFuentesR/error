package com.example.denuncia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.example.denuncia.model.Denuncia;
import com.example.denuncia.model.Tipo_denuncia;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class denuncia_Activity extends Activity {

    String tipo = "", tipodenuncia;
    Spinner txt_tipo;
    EditText txt_detalle, txt_direccion, txt_titulo;
    ProgressDialog prograsD;
    FirebaseAuth auth;
    Button btn_galeria, btn_camara;
    ImageView img;
    Uri uri_img;
    StorageReference reference;

    public static final int CODE_CAMERA = 21;
    public static final int CODE_GALLERY = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);
        prograsD = new ProgressDialog(this);
        txt_detalle = findViewById(R.id.detalles_denuncia);
        txt_direccion = findViewById(R.id.direccion);
        txt_titulo = findViewById(R.id.titulo_denuncia);
        txt_tipo = findViewById(R.id.tipo_denuncia);
        btn_camara = findViewById(R.id.abrir_camara);
        btn_galeria = findViewById(R.id.subir_imagen);
        img = findViewById(R.id.denuncia_imagen);
        loadTipoDenuncias();
        //esta es la funcion de cargar foto desde galeria
        btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, denuncia_Activity.CODE_GALLERY);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference fref = storageReference.child("Fotos_Denuncias").child(new Date().toString());
                reference = fref;
            }
        });
        //esta es fa funcion para tomar una foto para la denuncia
        btn_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                File foto = new File(getExternalFilesDir(null), "test.jpg");
                uri_img = FileProvider.getUriForFile(denuncia_Activity.this, getPackageName() + ".provider", foto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_img);
                startActivityForResult(intent, CODE_CAMERA);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference fref = storageReference.child("Fotos_Denuncias").child(new Date().toString());
                reference = fref;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case denuncia_Activity.CODE_GALLERY:
                if (data != null) {
                    uri_img = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_img);
                        img.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_CAMERA:
                Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir(null) + "/test.jpg");
                img.setImageBitmap(bitmap);
                break;
        }
    }

    //carga los tipos de denuncias de la base de datos
    public void loadTipoDenuncias() {
        final List<Tipo_denuncia> tipo_denuncias = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tipo_denuncia");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        tipo_denuncias.add(new Tipo_denuncia(nombre));
                    }//fin ford
                    ArrayAdapter<Tipo_denuncia> arrayAdapter = new ArrayAdapter<>(denuncia_Activity.this, R.layout.support_simple_spinner_dropdown_item, tipo_denuncias);
                    txt_tipo.setAdapter(arrayAdapter);
                    txt_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            tipo = parent.getItemAtPosition(position).toString();
                            tipodenuncia = tipo;
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }//fin if
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    //creacion de las denuncias
    public void registrar_denuncia(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date), info = "Selecciona el tipo de denuncia";
        auth = FirebaseAuth.getInstance();
        prograsD.setTitle("Cargando .....");
        prograsD.setMessage("Creando denuncia");
        prograsD.setCancelable(false);
        prograsD.show();

            String noimagen ="https://firebasestorage.googleapis.com/v0/b/denunciaciudadana-2798d.appspot.com/o/depositphotos_324611040-stock-illustration-no-image-vector-icon-no.jpg?alt=media&token=98ea066a-198a-4364-b3fb-73c87e5f5b4d";
            String detalle = txt_detalle.getText().toString();
            String direccion = txt_direccion.getText().toString();
            String titulo = txt_titulo.getText().toString();
            String tipo = tipodenuncia;
            String uid = auth.getCurrentUser().getUid();

            if (direccion.isEmpty() || uid.isEmpty() || tipodenuncia.equals(info) || titulo.isEmpty()) {
                prograsD.dismiss();
                Toast.makeText(denuncia_Activity.this, "Faltan datos", Toast.LENGTH_LONG).show();
            } else {
                //verifica si uno sube una imagen
                if (reference != null){
                    reference.putFile(uri_img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        //verifica si ya se subio la imagen
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            //Se crea la denuncia
                                            Toast.makeText(denuncia_Activity.this, "Se subio la foto", Toast.LENGTH_LONG).show();
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference("Denuncias").child(uid);

                                            Denuncia denuncia = new Denuncia();
                                            denuncia.setUrl(imageUrl);
                                            denuncia.setEstado_denuncia("Pendiente");
                                            denuncia.setDireccion(direccion);
                                            denuncia.setTitulo_denuncia(titulo);
                                            denuncia.setDenuncia_detalles(detalle);
                                            denuncia.setTipo_denuncia(tipo);
                                            denuncia.setFecha(fecha);

                                            myRef.push().setValue(denuncia);

                                            Intent intent = new Intent(denuncia_Activity.this, perfil_Activity.class);
                                            startActivity(intent);
                                            finish();
                                            prograsD.dismiss();
                                            Toast.makeText(denuncia_Activity.this, "Denuncia creada con exito", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }else{
                    //si no tiene imagen se crea la denuncia con una imagen por defecto que muestra que no subio imagen
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Denuncias").child(uid);

                    Denuncia denuncia = new Denuncia();
                    denuncia.setUrl(noimagen);
                    denuncia.setEstado_denuncia("Pendiente");
                    denuncia.setDireccion(direccion);
                    denuncia.setTitulo_denuncia(titulo);
                    denuncia.setDenuncia_detalles(detalle);
                    denuncia.setTipo_denuncia(tipo);
                    denuncia.setFecha(fecha);

                    myRef.push().setValue(denuncia);

                    Intent intent = new Intent(denuncia_Activity.this, perfil_Activity.class);
                    startActivity(intent);
                    finish();
                    prograsD.dismiss();
                    Toast.makeText(denuncia_Activity.this, "Denuncia creada con exito", Toast.LENGTH_LONG).show();
                }
            }//fin else
    }
    //redirige a ver denuncias
    public void ver_denuncias(View view) {
        Intent intent = new Intent(denuncia_Activity.this, ver_denuncias.class);
        startActivity(intent);
        finish();
    }
    //redirige al perfil
    public void atras(View view) {
        Intent intent = new Intent(denuncia_Activity.this, perfil_Activity.class);
        startActivity(intent);
        finish();
    }
}

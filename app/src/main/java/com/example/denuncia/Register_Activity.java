package com.example.denuncia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.google.firebase.database.ChildEventListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.denuncia.adapter.DenunciaService;
import com.example.denuncia.model.Denuncia;
import com.example.denuncia.model.Usuarios;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register_Activity extends AppCompatActivity {

    EditText txt_rut,txt_nombre_apellido,txt_correo,txt_password;
    ProgressDialog prograsD;
    List<Usuarios> list;
    Usuarios usuarios = new Usuarios();

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_rut             = findViewById(R.id.registar_rut);
        txt_nombre_apellido = findViewById(R.id.register_nombre);
        txt_correo          = findViewById(R.id.register_email);
        txt_password        = findViewById(R.id.register_contraseña);
        prograsD            = new ProgressDialog(this);
        firebaseAuth        = FirebaseAuth.getInstance();
    }


    public void Launchloggin(View view) {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
            startActivity(intent);
            fileList();//destruye esta actividad
        }else {
            Toast.makeText(getApplicationContext(),"No tiene coneccion a internet",Toast.LENGTH_LONG).show();
        }
    }
    //aqui valida si el rut existe en el sistema
    public void recorrerUsuarios(String rut){
        list = new ArrayList<>();
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference("Usuarios");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Usuarios usuarios = ds.getValue(Usuarios.class);
                        list.add(usuarios);
                    }
                    for (Usuarios u : list) {
                        if (rut.equals(u.getRut())) {
                            i++;
                            break;
                        }
                    }
                }
                if (i>0){
                    Toast.makeText(getApplicationContext(),"El rut ya esta registrado",Toast.LENGTH_LONG).show();
                }else{
                    crearusuario();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void validarRut(String rut) {
        boolean validacion = false;
        try {
            rut = rut.toUpperCase().replace(".", "").replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
            char dv = rut.charAt(rut.length() - 1);
            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
                recorrerUsuarios(rut);
            }
        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        if (validacion==false){
            Toast.makeText(getApplicationContext(),"Rut no valido",Toast.LENGTH_LONG).show();
        }
    }

    public void CreateAccount(View view) {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnectedOrConnecting()) {
            final String rut = txt_rut.getText().toString().replace(".", "").replace("-", "");
            validarRut(rut);
    }else {
            Toast.makeText(getApplicationContext(),"No tiene coneccion a internet",Toast.LENGTH_LONG).show();
        }
    }

    public void crearusuario(){
            final String rut = txt_rut.getText().toString().toUpperCase().replace(".", "").replace("-", "");
            final String email = txt_correo.getText().toString();
            final String nombre_apellido = txt_nombre_apellido.getText().toString();
            final String pass = txt_password.getText().toString();
            //valida los campos
        if (email.isEmpty() || nombre_apellido.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Complete la informacion", Toast.LENGTH_LONG).show();
        }else{
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Register_Activity.this, task1 -> {
                                //muestra imagen de carga
                                prograsD.setTitle("Cargando .....");
                                prograsD.setMessage("Creando cuenta");
                                prograsD.setCancelable(false);
                                prograsD.show();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                DatabaseReference myRef = database.getReference("Usuarios");

                                usuarios.setUid(uid);
                                usuarios.setRut(rut);
                                usuarios.setPassword(pass);
                                usuarios.setCorreo(email);
                                usuarios.setNombres_apellidos(nombre_apellido);

                                myRef.push().setValue(usuarios);
                                //cargar perfil
                                prograsD.dismiss();
                                Intent intent = new Intent(Register_Activity.this, perfil_Activity.class);
                                startActivity(intent);
                                finish();
                            });
                            Toast.makeText(Register_Activity.this, "Cuenta creada", Toast.LENGTH_LONG).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Register_Activity.this, "El correo ya esta en uso", Toast.LENGTH_LONG).show();
                            } else {
                                if (pass.length() < 6) {
                                    Toast.makeText(Register_Activity.this, "La contraseña es muy corta minimo 6 de largo", Toast.LENGTH_LONG).show();
                                } else {
                                    String msg = task.getException().getMessage();
                                    Toast.makeText(Register_Activity.this, msg, Toast.LENGTH_LONG).show();
                                    //Toast.makeText(Register_Activity.this, "Hubo un error inesperado", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            }
    }

}
package com.example.denuncia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.denuncia.model.Encargado;
import com.example.denuncia.model.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register_Activity_encargado extends Activity {
    EditText txt_rut,txt_nombre_apellido,txt_correo,txt_password;
    ProgressDialog prograsD;
    Encargado encargado = new Encargado();
    List<Encargado> list;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_encargado);
        prograsD = new ProgressDialog(this);
        txt_rut             = findViewById(R.id.registar_rut);
        txt_nombre_apellido = findViewById(R.id.register_nombre);
        txt_correo          = findViewById(R.id.register_email);
        txt_password        = findViewById(R.id.register_contraseña);
        prograsD            = new ProgressDialog(this);
        firebaseAuth        = FirebaseAuth.getInstance();
    }
    //redirige al perfil del encargado
    public void Atras(View view) {
        Intent intent = new Intent(Register_Activity_encargado.this, perfil_Activity_encargado.class);
        startActivity(intent);
        finish();
    }
    //aqui valida si el rut existe en el sistema
    public void recorrerUsuarios(String rut){
        list = new ArrayList<>();

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference("Encargado");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Encargado encargado = ds.getValue(Encargado.class);
                        list.add(encargado);
                    }
                    for (Encargado u : list) {
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
    //valida si el rut es valido o no
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
    //verifica la coneccion a internet y los campos obligatorios
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
                            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Register_Activity_encargado.this, task1 -> {
                                //crea una imagen de carga
                                prograsD.setTitle("Cargando .....");
                                prograsD.setMessage("Creando cuenta");
                                prograsD.setCancelable(false);
                                prograsD.show();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                DatabaseReference myRef = database.getReference("Encargado");

                                encargado.setUid(uid);
                                encargado.setRut(rut);
                                encargado.setPassword(pass);
                                encargado.setCorreo(email);
                                encargado.setNombres_apellidos(nombre_apellido);

                                myRef.push().setValue(email);
                                //cargar perfil
                                prograsD.dismiss();
                                Intent intent = new Intent(Register_Activity_encargado.this, perfil_Activity_encargado.class);
                                startActivity(intent);
                                finish();
                            });
                            Toast.makeText(Register_Activity_encargado.this, "Cuenta creada", Toast.LENGTH_LONG).show();
                        } else {
                            //se muestran mensajes de error
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Register_Activity_encargado.this, "El correo ya esta en uso", Toast.LENGTH_LONG).show();
                            } else {
                                if (pass.length() < 6) {
                                    Toast.makeText(Register_Activity_encargado.this, "La contraseña es muy corta minimo 6 de largo", Toast.LENGTH_LONG).show();
                                } else {
                                    String msg = task.getException().getMessage();
                                    Toast.makeText(Register_Activity_encargado.this, msg, Toast.LENGTH_LONG).show();
                                    //Toast.makeText(Register_Activity.this, "Hubo un error inesperado", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }
}

package com.example.ireserve;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private Button registrarse;
    private EditText nombre, edad, email, contrasena;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();

        registrarse = (Button) findViewById(R.id.registrarse);
        registrarse.setOnClickListener(this);

        nombre = (EditText) findViewById(R.id.titulo);
        edad = (EditText) findViewById(R.id.direccion);
        email = (EditText) findViewById(R.id.descripcion);
        contrasena = (EditText) findViewById(R.id.contrasena);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registrarse:
                registro();
                break;
        }


    }

    private void registro() {
        String em = email.getText().toString().trim();
        String nom = nombre.getText().toString().trim();
        String cont = contrasena.getText().toString().trim();
        String ed = edad.getText().toString().trim();

        if (nom.isEmpty()) {
            nombre.setError("Nombre completo requerido");
            nombre.requestFocus();
            return;
        }
        if (ed.isEmpty()) {
            edad.setError("Edad requerida");
            edad.requestFocus();
            return;
        }
        if (em.isEmpty()) {
            email.setError("Correo requerido");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.setError("Debe utilizar un correo válido");
            email.requestFocus();
            return;
        }
        if (cont.isEmpty()) {
            contrasena.setError("Contraseña requerida");
            contrasena.requestFocus();
            return;
        }
        if (cont.length() < 6) {
            contrasena.setError("La contraseña debe tener como mínimo 6 carácteres");
            contrasena.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(em, cont).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("nombreCompleto", nom);
                    usuario.put("email", em);
                    usuario.put("edad", Integer.parseInt(ed));
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    FirebaseDatabase.getInstance("https://ireseve-8ead4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Usuarios")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Registro.this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();

                                        SingletonMap.getInstance().iniciarSingletonMap();

                                        Intent login = new Intent(Registro.this, MainActivity.class);
                                        startActivity(login);
                                    } else {
                                        Toast.makeText(Registro.this, "Error al registrar usuario", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });
                } else {
                    Toast.makeText(Registro.this, "Error al registrar usuario", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
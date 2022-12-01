package com.example.ireserve;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private Button inicio;
    private EditText email, contrasena;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.registro);
        register.setOnClickListener(this);

        inicio = (Button) findViewById(R.id.login);
        inicio.setOnClickListener(this);

        email = (EditText) findViewById(R.id.email2);
        contrasena = (EditText) findViewById(R.id.contrasena2);

        progressbar = (ProgressBar) findViewById(R.id.progressBar);

        SingletonMap.getInstance().iniciarSingletonMap();

        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registro:
                startActivity(new Intent(this, Registro.class));
                break;
            case R.id.login:
                login();
                break;
        }
    }

    private void login() {
        String em = email.getText().toString().trim();
        String con = contrasena.getText().toString().trim();

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
        if (con.isEmpty()) {
            contrasena.setError("Contraseña requerida");
            contrasena.requestFocus();
            return;
        }
        if (con.length() < 6) {
            contrasena.setError("La contraseña debe tener como mínimo 6 carácteres");
            contrasena.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(em, con).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Inicio de sesión con éxito", Toast.LENGTH_LONG).show();
                    Intent home = new Intent(MainActivity.this, HomeUsuario.class);
                    startActivity(home);
                } else {
                    Toast.makeText(MainActivity.this, "Error al iniciar sesión", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }


}
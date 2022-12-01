package com.example.ireserve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class HomeUsuario extends AppCompatActivity implements View.OnClickListener {

    private TextView logOut;
    private TextView filtroBusqueda;
    private Button publicar;
    private Button buscar;
    private TextView nombre;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String correo;
    private ListView lvreservas;
    List<Reserva> listaReservas = new ArrayList<Reserva>();
    List<Reserva> listaReservasUsuario = new ArrayList<Reserva>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_usuario);

        logOut = (TextView) findViewById(R.id.cerrarsesion);
        logOut.setOnClickListener(this);

        publicar = (Button) findViewById(R.id.publicarservicio);
        publicar.setOnClickListener(this);

        buscar = (Button) findViewById(R.id.buscar);
        buscar.setOnClickListener(this);

        nombre = (TextView) findViewById(R.id.perfil);
        nombre.setOnClickListener(this);

        lvreservas = (ListView) findViewById(R.id.lvreservas);

        filtroBusqueda = (TextView) findViewById(R.id.filtroBusqueda);


        String em = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        List<Usuario> lu = SingletonMap.getInstance().map.get("usuarios");
        for (Usuario u : lu) {
            if (u.email.equals(em)) {
                nombre.setText(u.nombreCompleto);
            }
        }


        listaReservas = SingletonMap.getInstance().map.get("reservas");
        for (Reserva l : listaReservas) {
            if (l.usuario.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                listaReservasUsuario.add(l);
            }
        }

        ReservaAdapter reservaAdapter = new ReservaAdapter(HomeUsuario.this, R.layout.list_item_reserva, listaReservasUsuario);
        lvreservas.setAdapter(reservaAdapter);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cerrarsesion:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeUsuario.this, "Cierre de sesión con éxito", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.publicarservicio:
                startActivity(new Intent(this, Publicar.class));
                break;
            case R.id.buscar:
                Intent i = new Intent(this, BusquedaPublicaciones.class);
                String filtro = filtroBusqueda.getText().toString().trim();
                i.putExtra("filtro", filtro);
                startActivity(i);
                break;
            case R.id.perfil:
                startActivity(new Intent(this, MisPublicaciones.class));
                break;

        }
    }
}
package com.example.ireserve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MisPublicaciones extends AppCompatActivity {

    ListView listaPublicaciones;
    DatabaseReference ref;
    List<Publicacion> listaPub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mis_publicaciones);
        listaPublicaciones = findViewById(R.id.listaPublicaciones);

        listaPub = SingletonMap.getInstance().map.get("publicaciones");

        List<Publicacion> l = new ArrayList<>();
        for (Publicacion p : listaPub) {
            if (p.creador.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                l.add(p);
            }
        }
        PublicacionAdapter publicacionAdapter = new PublicacionAdapter(MisPublicaciones.this, R.layout.list_item, l);
        listaPublicaciones.setAdapter(publicacionAdapter);

        listaPublicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MisPublicaciones.this, VistaMiPublicacion.class);
                intent.putExtra("idPublicacion", l.get(position).getFoto());
                startActivity(intent);
            }
        });
    }

}
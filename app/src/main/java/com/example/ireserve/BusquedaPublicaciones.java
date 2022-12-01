package com.example.ireserve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusquedaPublicaciones extends AppCompatActivity {

    ListView listaPublicaciones;
    DatabaseReference ref;
    List<Publicacion> listaPub;
    String filtro;
    PublicacionAdapter adaptador;
    ArrayList<Publicacion> arrayentidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filtro = extras.getString("filtro");
            //The key argument here must match that used in the other activity
        }

        setContentView(R.layout.activity_busqueda_publicaciones);
        listaPublicaciones = findViewById(R.id.listaPublicacionesBusqueda);

        listaPub = SingletonMap.getInstance().map.get("publicaciones");

        List<Publicacion> l = new ArrayList<>();
        for(Publicacion p : listaPub){
            if(((containsIgnoreCase(p.titulo,filtro) || containsIgnoreCase(p.descripcion,filtro) || containsIgnoreCase(p.direccion,filtro)) && !p.creador.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){
                l.add(p);
            }
        }

        PublicacionAdapter publicacionAdapter = new PublicacionAdapter(BusquedaPublicaciones.this,R.layout.list_item,l);
        listaPublicaciones.setAdapter(publicacionAdapter);

        //arrayentidad = GetArrayItems();
        //adapatador = new PublicacionAdapter(this, arrayentidad);
        listaPublicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BusquedaPublicaciones.this, VistaPublicacion.class);
                intent.putExtra("idPublicacion", l.get(position).getFoto());
                startActivity(intent);
            }
        });
    }

    public static boolean containsIgnoreCase(String str, String searchStr)     {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }
}
package com.example.ireserve;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VistaMiPublicacion extends AppCompatActivity {

    private String idPublicacion;
    private DatabaseReference mDatabase;
    private TextView ptitulo;

    private TextView pprecio;
    private TextView pdireccion;
    private ImageView pfoto;

    private ListView lvdias;
    private ListView lvhoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_mi_publicacion);

        ptitulo = (TextView) findViewById(R.id.ptituloMi);
        pdireccion = (TextView) findViewById(R.id.pdireccionMi);
        pprecio = (TextView) findViewById(R.id.pprecioMi);
        pfoto = (ImageView) findViewById(R.id.pfotoMi);
        lvdias = (ListView) findViewById(R.id.lvdiasMi);
        lvhoras = (ListView) findViewById(R.id.lvhorasMi);

        idPublicacion = (String) getIntent().getSerializableExtra("idPublicacion");

        List<Publicacion> listaPub = SingletonMap.getInstance().map.get("publicaciones");

        String titulo = "";
        String precio = "";
        String direccion = "";
        String foto = "";
        List<String> dia = new ArrayList<>();
        List<String> hora = new ArrayList<>();
        List<Publicacion> l = new ArrayList<>();
        for (Publicacion p : listaPub) {
            if (p.foto.equals(idPublicacion)) {
                titulo = p.titulo;
                precio = p.precio;
                direccion = p.direccion;
                foto = p.foto;
                dia = p.dias;
                hora = p.horas;
            }
        }
        ptitulo.setText(titulo);
        pprecio.setText(precio + "€");
        pdireccion.setText(direccion);

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Imagenes/" + foto);
        try {
            File localFile = File.createTempFile("imagen", "jpg");
            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    pfoto.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] dias = new String[dia.size()];
        dias = dia.toArray(dias);
        for (int i = 0; i < dias.length; i++) {
            if (dias[i].contains("[")) {
                dias[i] = dias[i].substring(1);
            }
            if (dias[i].contains("]")) {
                dias[i] = dias[i].substring(0, dias[i].length() - 1);
            }
        }

        String[] horas = new String[hora.size()];
        horas = hora.toArray(horas);
        for (int i = 0; i < horas.length; i++) {
            if (horas[i].contains("[")) {
                horas[i] = horas[i].substring(1);
            }
            if (horas[i].contains("]")) {
                horas[i] = horas[i].substring(0, horas[i].length() - 1);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VistaMiPublicacion.this, android.R.layout.simple_list_item_1, dias);
        lvdias.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(VistaMiPublicacion.this, android.R.layout.simple_list_item_1, horas);
        lvhoras.setAdapter(adapter2);
    }
}
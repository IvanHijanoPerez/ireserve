package com.example.ireserve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class VistaPublicacion extends AppCompatActivity implements View.OnClickListener {

    private String idPublicacion;
    private String fechaReserva = "";
    private String horaReserva = "";
    private DatabaseReference mDatabase;
    private TextView ptitulo;
    private String[] dias;
    private String[] horas;
    private String[] horasFinal;

    private TextView pprecio;
    private TextView pdireccion;
    private ImageView pfoto;
    private Button pboton;

    private ListView lvdias;
    private ListView lvhoras;

    List<String> dia = new ArrayList<>();
    List<String> hora = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_publicacion);

        ptitulo = (TextView) findViewById(R.id.ptitulo);
        pdireccion = (TextView) findViewById(R.id.pdireccion);
        pprecio = (TextView) findViewById(R.id.pprecio);
        pfoto = (ImageView) findViewById(R.id.pfoto);
        lvdias = (ListView) findViewById(R.id.lvdias);
        lvhoras = (ListView) findViewById(R.id.lvhoras);
        pboton = (Button) findViewById(R.id.pboton);
        pboton.setOnClickListener(this);

        idPublicacion = (String) getIntent().getSerializableExtra("idPublicacion");

        List<Publicacion> listaPub = SingletonMap.getInstance().map.get("publicaciones");

        String titulo = "";
        String precio = "";
        String direccion = "";
        String foto = "";

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

        dias = new String[dia.size()];
        dias = dia.toArray(dias);
        /*for (int i = 0; i < dias.length; i++) {
            if (dias[i].contains("[")) {
                dias[i] = dias[i].substring(1);
            }
            if (dias[i].contains("]")) {
                dias[i] = dias[i].substring(0, dias[i].length() - 1);
            }
        }*/

        LocalDate ld = LocalDate.now();

        for(int i = 0 ; i<dias.length; i++){
            if(dias[i].equalsIgnoreCase("lunes")){
                dias[i] =getString(R.string.lunes) + " " + ld.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).toString();
            }
            if(dias[i].equalsIgnoreCase("martes")){
                dias[i] =getString(R.string.martes) + " " + ld.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).toString();
            }
            if(dias[i].equalsIgnoreCase("miercoles")){
                dias[i] =getString(R.string.miercoles) + " " + ld.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)).toString();
            }
            if(dias[i].equalsIgnoreCase("jueves")){
                dias[i] =getString(R.string.jueves) + " " + ld.with(TemporalAdjusters.next(DayOfWeek.THURSDAY)).toString();
            }
            if(dias[i].equalsIgnoreCase("viernes")){
                dias[i] =getString(R.string.viernes) + " " + ld.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toString();
            }
            if(dias[i].equalsIgnoreCase("sabado")){
                dias[i] =getString(R.string.sabado) + " " + ld.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).toString();
            }
            if(dias[i].equalsIgnoreCase("domingo")){
                dias[i] =getString(R.string.domingo) + " " + ld.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toString();
            }
        }






        /*for (int i = 0; i < horas.length; i++) {
            if (horas[i].contains("[")) {
                horas[i] = horas[i].substring(1);
            }
            if (horas[i].contains("]")) {
                horas[i] = horas[i].substring(0, horas[i].length() - 1);
            }
        }*/


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VistaPublicacion.this, android.R.layout.simple_list_item_single_choice, dias);
        lvdias.setAdapter(adapter);
        lvdias.setChoiceMode(1);



        lvdias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] part = dias[position].split(" ");
                fechaReserva = part[1];


                horas = new String[hora.size()];
                horas = hora.toArray(horas);



                List<String> fHoras = new ArrayList<>();

                List<Reserva> res = SingletonMap.getInstance().map.get("reservas");
                for (int i = 0; i < horas.length; i++) {
                    Boolean esta = false;
                    for(Reserva r : res){
                        if(r.fecha.equals(fechaReserva) && r.hora.equals(horas[i])){
                            esta = true;
                        }
                    }
                    if(!esta){
                        fHoras.add(horas[i]);
                    }
                }
                horasFinal = new String[fHoras.size()];
                horasFinal = fHoras.toArray(horasFinal);



                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(VistaPublicacion.this, android.R.layout.simple_list_item_single_choice, horasFinal);
                lvhoras.setAdapter(adapter2);
                lvhoras.setChoiceMode(1);
            }
        });

        lvhoras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                horaReserva = horasFinal[position];
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pboton:
                crearReserva();
                break;
        }

    }

    public void crearReserva(){
        if(fechaReserva == "" || horaReserva == ""){
            Toast.makeText(VistaPublicacion.this,"Debe de seleccionar una fecha y hora", Toast.LENGTH_LONG).show();
            return;
        }
        String key = FirebaseDatabase.getInstance("https://ireseve-8ead4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservas").push().getKey();

        Map<String, Object> reserva = new HashMap<>();
        reserva.put("publicacion",idPublicacion);
        reserva.put("usuario",FirebaseAuth.getInstance().getCurrentUser().getUid());
        reserva.put("fecha",fechaReserva);
        reserva.put("hora",horaReserva);


        FirebaseDatabase.getInstance("https://ireseve-8ead4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservas")
                .child(key)
                .setValue(reserva).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(VistaPublicacion.this,"Reserva registrada con éxito", Toast.LENGTH_LONG).show();

                    SingletonMap.getInstance().anadirReserva(idPublicacion,fechaReserva,horaReserva);

                    Intent home = new Intent(VistaPublicacion.this, HomeUsuario.class);
                    startActivity(home);
                }else{
                    Toast.makeText(VistaPublicacion.this,"Error al reservar", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
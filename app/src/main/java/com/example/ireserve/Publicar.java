package com.example.ireserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Publicar extends AppCompatActivity {

    private EditText titulo, direccion, descripcion, precio, horas, fecha;
    private Button direccionboton, fechaboton, publicar;
    Button imagenboton;
    ImageView foto;
    Uri FilePathUri;
    CheckBox lunes, martes, miercoles, jueves, viernes, sabado, domingo;
    int Image_Request_Code = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        precio = findViewById(R.id.precio);
        direccion = findViewById(R.id.direccion);
        lunes = findViewById(R.id.lunes);
        martes = findViewById(R.id.martes);
        miercoles = findViewById(R.id.miercoles);
        jueves = findViewById(R.id.jueves);
        viernes = findViewById(R.id.viernes);
        sabado = findViewById(R.id.sabado);
        horas = findViewById(R.id.horas);
        domingo = findViewById(R.id.domingo);
        //fecha = findViewById(R.id.fecha);
        //fechaboton = findViewById(R.id.fechaboton);
        direccionboton = findViewById(R.id.direccionboton);

        publicar = findViewById(R.id.publicarservicio);
        Places.initialize(getApplicationContext(),"AIzaSyC1GXp71yv3zHYbZBvgu6a0CTL7SMxEZzk");


        //DIRECCION


        direccionboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(Publicar.this);
                startActivityForResult(intent,100);
            }
        });

        //FECHA

        /*fechaboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFecha();

            }
        });*/


        //IMAGEN

        //storageReference = FirebaseStorage.getInstance().getReference("Images");
        //databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        imagenboton = (Button)findViewById(R.id.imagenboton);
        foto = (ImageView)findViewById(R.id.foto);


        imagenboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });

        //PUBLICAR
        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar();

            }
        });
    }

    //FECHA

    /*private void showFecha(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hour);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        fecha.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(Publicar.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        };
        new DatePickerDialog(Publicar.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //FECHA
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            direccion.setText(place.getAddress());
        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }

        //IMAGEN
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                foto.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }



    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    //PUBLICAR
    private void publicar(){
        String tit = titulo.getText().toString().trim();
        String dir = direccion.getText().toString().trim();
        String des = descripcion.getText().toString().trim();
        //String fe = fecha.getText().toString().trim();
        String pre = precio.getText().toString().trim();
        String hor = horas.getText().toString().trim();

        if(tit.isEmpty()){
            titulo.setError("Título requerido");
            titulo.requestFocus();
            return;
        }
        if(dir.isEmpty()){
            direccion.setError("Dirección requerida");
            direccion.requestFocus();
            return;
        }
        if(des.isEmpty()){
            descripcion.setError("Descripción requerida");
            descripcion.requestFocus();
            return;
        }

        if(!lunes.isChecked() && !martes.isChecked() && !miercoles.isChecked() && !jueves.isChecked() && !viernes.isChecked() && !sabado.isChecked() && !domingo.isChecked()){
            Toast.makeText(Publicar.this,"Debes elegir mínimo un día", Toast.LENGTH_LONG).show();
            return;
        }

        if(hor.isEmpty()){
            horas.setError("Hora requerida");
            horas.requestFocus();
            return;
        }

        if(!hor.contains("-") || !hor.contains(":")){
            horas.setError("El horario debe tener horas válidas (XX:XX-XX:XX)");
            horas.requestFocus();
            return;
        }
        /*if(fe.isEmpty()){
            fecha.setError("Fecha requerida");
            fecha.requestFocus();
            return;
        }*/
        if(pre.isEmpty()){
            precio.setError("Precio requerido");
            precio.requestFocus();
            return;
        }
        if(FilePathUri == null){
            Toast.makeText(Publicar.this,"Imágen requerida", Toast.LENGTH_LONG).show();
            return;
        }

        String key = FirebaseDatabase.getInstance("https://ireseve-8ead4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Publicaciones").push().getKey();
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference("Imagenes").child(key);
        storageReference2.putFile(FilePathUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Map<String, Object> publicacion = new HashMap<>();
                        publicacion.put("titulo",tit);
                        publicacion.put("direccion",dir);
                        publicacion.put("descripcion",des);
                        List dias = new ArrayList();
                        if(lunes.isChecked()){ dias.add("lunes");}
                        if(martes.isChecked()){ dias.add("martes");}
                        if(miercoles.isChecked()){ dias.add("miercoles");}
                        if(jueves.isChecked()){ dias.add("jueves");}
                        if(viernes.isChecked()){ dias.add("viernes");}
                        if(sabado.isChecked()){ dias.add("sabado");}
                        if(domingo.isChecked()){ dias.add("domingo");}
                        List horas = Arrays.asList(hor.split(",").clone());
                        publicacion.put("horas",horas);
                        publicacion.put("dias",dias);
                        //publicacion.put("fecha",fe);
                        publicacion.put("creador",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        publicacion.put("precio",pre);
                        publicacion.put("foto", key);

                        FirebaseDatabase.getInstance("https://ireseve-8ead4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Publicaciones")
                                .child(key)
                                .setValue(publicacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(Publicar.this,"Publicación registrada con éxito", Toast.LENGTH_LONG).show();

                                    SingletonMap.getInstance().anadirPublicacion(des,dir,key,pre,tit,dias,horas);

                                    Intent home = new Intent(Publicar.this, HomeUsuario.class);
                                    startActivity(home);
                                }else{
                                    Toast.makeText(Publicar.this,"Error al registrar la publicación", Toast.LENGTH_LONG).show();
                                }

                            }
                        });


                    }
                });
    }

}
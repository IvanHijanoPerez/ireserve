package com.example.ireserve;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PublicacionAdapter extends ArrayAdapter<Publicacion> {


    public PublicacionAdapter(Context context, int list_item, List<Publicacion> publicacionArrayList){
        super(context,R.layout.list_item,publicacionArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Publicacion publicacion = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        }

        ImageView foto = convertView.findViewById(R.id.fotoItem);
        TextView titulo = convertView.findViewById(R.id.tituloItem);
        TextView direccion = convertView.findViewById(R.id.direccionItem);

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Imagenes/"+publicacion.foto);
        try{
            File localFile = File.createTempFile("imagen","jpg");
            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    foto.setImageBitmap(bitmap);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }


        titulo.setText(publicacion.titulo);
        direccion.setText(publicacion.direccion);


        return convertView;
    }
}

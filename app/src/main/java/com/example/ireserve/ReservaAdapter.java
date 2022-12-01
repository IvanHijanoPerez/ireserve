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

public class ReservaAdapter extends ArrayAdapter<Reserva> {
    public ReservaAdapter(Context context, int list_item_reserva, List<Reserva> reservaArrayList) {
        super(context, R.layout.list_item_reserva, reservaArrayList);
    }

    Publicacion publicacion;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Reserva reserva = getItem(position);
        List<Publicacion> listaPublicaciones = SingletonMap.getInstance().map.get("publicaciones");
        for (Publicacion p : listaPublicaciones) {
            if (p.getFoto().equals(reserva.publicacion)) {
                publicacion = p;
            }
        }

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_reserva, parent, false);

        }

        ImageView foto = convertView.findViewById(R.id.fotoItemR);
        TextView titulo = convertView.findViewById(R.id.tituloItemR);
        TextView direccion = convertView.findViewById(R.id.direccionItemR);
        TextView tvfechacompleta = convertView.findViewById(R.id.tvfechacompleta);

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Imagenes/" + publicacion.foto);
        try {
            File localFile = File.createTempFile("imagen", "jpg");
            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    foto.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        titulo.setText(publicacion.titulo);
        direccion.setText(publicacion.direccion);
        tvfechacompleta.setText("El " + reserva.fecha + " a las " + reserva.hora);


        return convertView;
    }
}

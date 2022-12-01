package com.example.ireserve;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SingletonMap extends HashMap< String , Object > {

    SortedMap< String , List> map ;
    static final String SHARED_DATA_KEY = " SHARED_MAP_KEY ";
    List<Usuario> listaUsuarios = new ArrayList<>();
    List<Publicacion> listaPublicaciones = new ArrayList<>();
    List<Reserva> listaReservas = new ArrayList<>();

    private static class SingletonHolder {
        private static final SingletonMap ourInstance = new SingletonMap ();
    }
    public static SingletonMap getInstance () {
        return SingletonHolder.ourInstance ;
    }
    private SingletonMap () {}

    public void iniciarSingletonMap() {
        clear();
        map = new TreeMap<>();
        listaUsuarios = new ArrayList<>();
        listaPublicaciones = new ArrayList<>();
        listaReservas = new ArrayList<>();
        actualizarListas();
        map.put("usuarios", listaUsuarios);
        map.put("publicaciones", listaPublicaciones);
        map.put("reservas", listaReservas);
        put(SHARED_DATA_KEY, map);



    }

    public void anadirPublicacion(String des, String dir, String foto, String pre, String tit, List<String> dias, List<String> horas){
        Publicacion res = new Publicacion(FirebaseAuth.getInstance().getCurrentUser().getUid(),des,dir,foto,pre,tit,dias,horas);
        List<Publicacion> lPu = SingletonMap.getInstance().map.get("publicaciones");
        lPu.add(res);
        SortedMap< String , List> map = new TreeMap<>();
        List<Usuario> lUs = SingletonMap.getInstance().map.get("usuarios");
        List<Reserva> lRes = SingletonMap.getInstance().map.get("reservas");
        map.put("usuarios", lUs);
        map.put("publicaciones", lPu);
        map.put("reservas", lRes);
        SingletonMap.getInstance().clear();
        SingletonMap.getInstance().put(SHARED_DATA_KEY, map);
    }

    public void anadirReserva(String idPublicacion, String fechaReserva, String horaReserva){
        Reserva res = new Reserva(idPublicacion,FirebaseAuth.getInstance().getCurrentUser().getUid(),fechaReserva,horaReserva);
        List<Reserva> lRes = SingletonMap.getInstance().map.get("reservas");
        lRes.add(res);
        SortedMap< String , List> map = new TreeMap<>();
        List<Usuario> lUs = SingletonMap.getInstance().map.get("usuarios");
        List<Publicacion> lPu = SingletonMap.getInstance().map.get("publicaciones");
        map.put("usuarios", lUs);
        map.put("publicaciones", lPu);
        map.put("reservas", lRes);
        SingletonMap.getInstance().clear();
        SingletonMap.getInstance().put(SingletonMap.SHARED_DATA_KEY, map);
    }

    public void actualizarListas(){

        FirebaseDatabase bd;
        bd = FirebaseDatabase.getInstance("https://ireseve-8ead4-default-rtdb.europe-west1.firebasedatabase.app");


        bd.getReference("Usuarios").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Map<String,Object> pu = (Map<String,Object>) dataSnapshot.getValue();

                        for (Map.Entry<String, Object> entry : pu.entrySet()){

                            Map publi = (Map) entry.getValue();
                            String email = (String)publi.get("email");
                            int edad = (int)(long)publi.get("edad");
                            String nombreCompleto = (String)publi.get("nombreCompleto");

                            Usuario nuevapub = new Usuario(email,nombreCompleto,edad);
                            listaUsuarios.add(nuevapub);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        bd.getReference("Publicaciones").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Map<String,Object> pu = (Map<String,Object>) dataSnapshot.getValue();
                        if(pu != null){
                            for (Map.Entry<String, Object> entry : pu.entrySet()){

                                Map publi = (Map) entry.getValue();
                                String creador = (String)publi.get("creador");
                                String descripcion = (String)publi.get("descripcion");
                                List<String> dias = (List<String>)publi.get("dias");
                                String direccion = (String)publi.get("direccion");
                                String foto = (String)publi.get("foto");
                                List<String> horas = (List<String>)publi.get("horas");
                                String precio = (String)publi.get("precio");
                                String titulo = (String)publi.get("titulo");

                                Publicacion nuevapub = new Publicacion(creador,descripcion,direccion,foto,precio,titulo,dias,horas);
                                listaPublicaciones.add(nuevapub);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        bd.getReference("Reservas").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Map<String,Object> pu = (Map<String,Object>) dataSnapshot.getValue();
                        if(pu != null){
                            for (Map.Entry<String, Object> entry : pu.entrySet()){

                                Map publi = (Map) entry.getValue();
                                String publicacion = (String)publi.get("publicacion");
                                String usuario = (String)publi.get("usuario");
                                String fecha = (String)publi.get("fecha");
                                String hora = (String)publi.get("hora");

                                Reserva nuevapub = new Reserva(publicacion,usuario,fecha,hora);
                                listaReservas.add(nuevapub);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


    }

}
